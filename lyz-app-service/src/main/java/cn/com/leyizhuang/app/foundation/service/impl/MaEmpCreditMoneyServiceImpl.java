package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.constant.AppConstant;
import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.core.constant.EmpCreditMoneyChangeType;
import cn.com.leyizhuang.app.core.exception.SystemBusyException;
import cn.com.leyizhuang.app.foundation.dao.MaEmpCreditMoneyDAO;
import cn.com.leyizhuang.app.foundation.pojo.management.guide.*;
import cn.com.leyizhuang.app.foundation.service.MaEmpCreditMoneyService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("maEmpCreditMoneyService")
public class MaEmpCreditMoneyServiceImpl implements MaEmpCreditMoneyService {

    @Autowired
    private MaEmpCreditMoneyDAO maEmpCreditMoneyDAO;

    @Override
    public void saveEmpCreditMoney(GuideCreditMoney guideCreditMoney) {
        this.maEmpCreditMoneyDAO.saveEmpCreditMoney(guideCreditMoney);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(GuideCreditMoneyDetail guideCreditMoneyDetail, GuideCreditChangeDetail guideCreditChangeDetail) throws RuntimeException {
        //判断新增还是修改导购额度

        GuideCreditMoney guideCreditMoney = new GuideCreditMoney();
        guideCreditMoney.setCreditLimit(guideCreditMoneyDetail.getCreditLimit());
        guideCreditMoney.setCreditLimitAvailable(guideCreditMoneyDetail.getCreditLimitAvailable());
        guideCreditMoney.setEmpId(guideCreditMoneyDetail.getEmpId());
        guideCreditMoney.setTempCreditLimit(guideCreditMoneyDetail.getTempCreditLimit());
        guideCreditMoney.setLastUpdateTime(guideCreditMoneyDetail.getLastUpdateTime());
        //得到更新后的可用额度
        BigDecimal creditLimitChangeAmount = guideCreditMoneyDetail.getCreditLimit().subtract(guideCreditMoneyDetail.getOriginalCreditLimit());
        BigDecimal tempCreditLimitChangeAmount = guideCreditMoneyDetail.getTempCreditLimit().subtract(guideCreditMoneyDetail.getOriginalTempCreditLimit());
        BigDecimal allChangeAmount = creditLimitChangeAmount.add(tempCreditLimitChangeAmount);
        guideCreditMoneyDetail.setCreditLimitAvailable(guideCreditMoneyDetail.getOriginalCreditLimitAvailable().add(allChangeAmount));
        //更新导购信用金
        for (int i = 1; i <= AppConstant.OPTIMISTIC_LOCK_RETRY_TIME; i++) {
            Integer affectLine = this.maEmpCreditMoneyDAO.update(guideCreditMoney);
            //更新变更详情父表与子表
            if (affectLine > 0) {
                this.saveCreditMoneyChange(guideCreditMoneyDetail, guideCreditChangeDetail);
                break;
            } else {
                if (i == AppConstant.OPTIMISTIC_LOCK_RETRY_TIME) {
                    throw new SystemBusyException("系统繁忙，请稍后再试!");
                }
            }
        }

    }

    @Override
    public int updateGuideCreditMoneyByRepayment(Long sellerId, BigDecimal availableCreditMoney, Date lastUpdateTime) {
        return this.maEmpCreditMoneyDAO.updateGuideCreditMoneyByRepayment(sellerId, availableCreditMoney, lastUpdateTime);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void clearTempCreditLimit(GuideCreditMoneyDetail guideCreditMoneyDetail, GuideCreditChangeDetail guideCreditChangeDetail) {
        //导购零时额度清零
        for (int i = 1; i <= AppConstant.OPTIMISTIC_LOCK_RETRY_TIME; i++) {
            Integer affectLine = this.maEmpCreditMoneyDAO.clearTempCreditLimit(guideCreditMoneyDetail);
            if (affectLine > 0) {
                //得到存入变更字表的id
                GuideTempCreditChange guideTempCreditChange = new GuideTempCreditChange();
                GuideAvailableCreditChange guideAvailableCreditChange = new GuideAvailableCreditChange();
                guideTempCreditChange.setTempCreditLimitAfterChange(BigDecimal.ZERO);
                guideTempCreditChange.setTempCreditLimitChangeAmount(guideCreditMoneyDetail.getTempCreditLimit().subtract(guideCreditMoneyDetail.getOriginalTempCreditLimit()));
                guideTempCreditChange.setChangeType(EmpCreditMoneyChangeType.TEMPORARY_CLEAR.toString());
                guideTempCreditChange.setChangeTypeDesc(EmpCreditMoneyChangeType.TEMPORARY_CLEAR.getDescription());

                guideAvailableCreditChange.setCreditLimitAvailableAfterChange(guideCreditMoneyDetail.getCreditLimitAvailable());
                guideAvailableCreditChange.setCreditLimitAvailableChangeAmount(guideCreditMoneyDetail.getCreditLimitAvailable().subtract(guideCreditMoneyDetail.getOriginalCreditLimitAvailable()));
                guideAvailableCreditChange.setChangeType(EmpCreditMoneyChangeType.AVALIABLED_CHANGE_BY_TEMP.toString());
                guideAvailableCreditChange.setChangeTypeDesc(EmpCreditMoneyChangeType.AVALIABLED_CHANGE_BY_TEMP.getDescription());

                maEmpCreditMoneyDAO.saveTempCreditLimitChange(guideTempCreditChange);
                maEmpCreditMoneyDAO.saveCreditLimitAvailableChange(guideAvailableCreditChange);
                //补全额度改变明细
                guideCreditChangeDetail.setAvailableCreditChangId(guideAvailableCreditChange.getId());
                guideCreditChangeDetail.setTempCreditChangeId(guideTempCreditChange.getId());
                //存入额度变更明细主表
                this.saveCreditChange(guideCreditChangeDetail);
                break;
            } else {
                if (i == AppConstant.OPTIMISTIC_LOCK_RETRY_TIME) {
                    throw new SystemBusyException("系统繁忙，请稍后再试!");
                }
            }
        }
    }

    @Override
    public PageInfo<GuideCreditChangeDetailDO> queryAvailableCreditMoneyChangePage(Integer page, Integer size, Long id) {
        PageHelper.startPage(page, size);
        List<GuideCreditChangeDetailDO> pageCreditMoneyDOList = this.maEmpCreditMoneyDAO.queryAvailableCreditMoneyChangePage(id);
        return new PageInfo<>(pageCreditMoneyDOList);
    }

    @Override
    public PageInfo<GuideCreditChangeDetailDO> queryTempCreditMoneyChangePage(Integer page, Integer size, Long id) {
        PageHelper.startPage(page, size);
        List<GuideCreditChangeDetailDO> pageCreditMoneyDOList = this.maEmpCreditMoneyDAO.queryTempCreditMoneyChangePage(id);
        return new PageInfo<>(pageCreditMoneyDOList);
    }

    @Override
    public PageInfo<GuideCreditChangeDetailDO> queryFixedCreditMoneyChangePage(Integer page, Integer size, Long id) {
        PageHelper.startPage(page, size);
        List<GuideCreditChangeDetailDO> pageCreditMoneyDOList = this.maEmpCreditMoneyDAO.queryFixedCreditMoneyChangePage(id);
        return new PageInfo<>(pageCreditMoneyDOList);
    }

    @Override
    public void saveCreditChange(GuideCreditChangeDetail guideCreditChangeDetail) {
        if (null != guideCreditChangeDetail) {
            guideCreditChangeDetail.setOperatorType(AppIdentityType.ADMINISTRATOR.getDescription());
            guideCreditChangeDetail.setCreateTime(new Date());
            this.maEmpCreditMoneyDAO.saveCreditChange(guideCreditChangeDetail);
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveCreditMoneyChange(GuideCreditMoneyDetail guideCreditMoneyDetail, GuideCreditChangeDetail guideCreditChangeDetail) {
        //得到金额变更字表ID集合
        Map<String, Long> idList = this.saveAllCreditMoneyChange(guideCreditMoneyDetail);
        Long tempCreditChangeId = idList.get("tempCreditLimitId");
        Long fixedCreditChangeId = idList.get("fixedCreditLimitId");
        Long availableCreditChangId = idList.get("creditLimitAvailableId");
        //更新变更详情
        guideCreditChangeDetail.setAvailableCreditChangId(availableCreditChangId);
        guideCreditChangeDetail.setFixedCreditChangeId(fixedCreditChangeId);
        guideCreditChangeDetail.setTempCreditChangeId(tempCreditChangeId);
        this.saveCreditChange(guideCreditChangeDetail);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Long> saveAllCreditMoneyChange(GuideCreditMoneyDetail guideCreditMoneyDetail) {
        if (null != guideCreditMoneyDetail) {
            Map idMap = new HashMap(3);
            if (null == guideCreditMoneyDetail.getOriginalTempCreditLimit()) {
                guideCreditMoneyDetail.setOriginalTempCreditLimit(BigDecimal.ZERO);
            }
            if (null == guideCreditMoneyDetail.getOriginalCreditLimitAvailable()) {
                guideCreditMoneyDetail.setOriginalCreditLimitAvailable(BigDecimal.ZERO);
            }
            if (null == guideCreditMoneyDetail.getOriginalCreditLimit()) {
                guideCreditMoneyDetail.setOriginalCreditLimit(BigDecimal.ZERO);
            }
            //判断可用额度是否有改变 有保存 无返回空
            if (null != guideCreditMoneyDetail.getCreditLimitAvailable() && guideCreditMoneyDetail.getOriginalCreditLimitAvailable().compareTo(guideCreditMoneyDetail.getCreditLimitAvailable()) != 0) {
                BigDecimal creditLimitAvailableChangeAmount = guideCreditMoneyDetail.getCreditLimitAvailable().subtract(guideCreditMoneyDetail.getOriginalCreditLimitAvailable());
                GuideAvailableCreditChange guideAvailableCreditChange = new GuideAvailableCreditChange();
                guideAvailableCreditChange.setCreditLimitAvailableAfterChange(guideCreditMoneyDetail.getCreditLimitAvailable());
                guideAvailableCreditChange.setCreditLimitAvailableChangeAmount(creditLimitAvailableChangeAmount);
                int isFixEqual = guideCreditMoneyDetail.getCreditLimit().compareTo(guideCreditMoneyDetail.getOriginalCreditLimit());
                int isTempEqual = guideCreditMoneyDetail.getTempCreditLimit().compareTo(guideCreditMoneyDetail.getOriginalTempCreditLimit());
                if (0 != isFixEqual && 0 == isTempEqual) {
                    guideAvailableCreditChange.setChangeType(EmpCreditMoneyChangeType.AVALIABLED_CHANGE_BY_FIXE.toString());
                    guideAvailableCreditChange.setChangeTypeDesc(EmpCreditMoneyChangeType.AVALIABLED_CHANGE_BY_FIXE.getDescription());
                } else if (0 != isTempEqual && 0 == isFixEqual) {
                    guideAvailableCreditChange.setChangeType(EmpCreditMoneyChangeType.AVALIABLED_CHANGE_BY_TEMP.toString());
                    guideAvailableCreditChange.setChangeTypeDesc(EmpCreditMoneyChangeType.AVALIABLED_CHANGE_BY_TEMP.getDescription());
                }
                this.maEmpCreditMoneyDAO.saveCreditLimitAvailableChange(guideAvailableCreditChange);
                idMap.put("creditLimitAvailableId", guideAvailableCreditChange.getId());
            } else {
                idMap.put("creditLimitAvailableId", null);
            }
            //判断临时额度是否有改变 有保存 无返回空
            if (null != guideCreditMoneyDetail.getTempCreditLimit() && guideCreditMoneyDetail.getOriginalTempCreditLimit().compareTo(guideCreditMoneyDetail.getTempCreditLimit()) != 0) {
                BigDecimal tempCreditLimitChangeAmount = guideCreditMoneyDetail.getTempCreditLimit().subtract(guideCreditMoneyDetail.getOriginalTempCreditLimit());
                GuideTempCreditChange guideTempCreditChange = new GuideTempCreditChange(null, tempCreditLimitChangeAmount, guideCreditMoneyDetail.getTempCreditLimit(), EmpCreditMoneyChangeType.TEMPORARY_ADJUSTMENT.toString(), EmpCreditMoneyChangeType.TEMPORARY_ADJUSTMENT.getDescription());
                this.maEmpCreditMoneyDAO.saveTempCreditLimitChange(guideTempCreditChange);
                idMap.put("tempCreditLimitId", guideTempCreditChange.getId());
            } else {
                idMap.put("tempCreditLimitId", null);
            }
            //判断固定额度是否有改变 有保存 无返回空
            if (null != guideCreditMoneyDetail.getCreditLimit() && guideCreditMoneyDetail.getCreditLimit().compareTo(guideCreditMoneyDetail.getOriginalCreditLimit()) != 0) {
                BigDecimal fixedCreditLimitChangeAmount = guideCreditMoneyDetail.getCreditLimit().subtract(guideCreditMoneyDetail.getOriginalCreditLimit());
                GuideFixedCreditChange guideFixedCreditChange = new GuideFixedCreditChange(null, fixedCreditLimitChangeAmount, guideCreditMoneyDetail.getCreditLimit(), EmpCreditMoneyChangeType.FIXEDAMOUNT_ADJUSTMENT.toString(), EmpCreditMoneyChangeType.FIXEDAMOUNT_ADJUSTMENT.getDescription());
                this.maEmpCreditMoneyDAO.saveFixedCreditLimitChange(guideFixedCreditChange);
                idMap.put("fixedCreditLimitId", guideFixedCreditChange.getId());
            } else {
                idMap.put("fixedCreditLimitId", null);
            }
            return idMap;
        } else {
            return null;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void autoClearTempCreditMoney() throws RuntimeException {
        List<GuideCreditMoney> guideCreditMoneyList = this.findAllGuideCreditMoney();
        if (guideCreditMoneyList.size() == 0) {
            throw new RuntimeException("查找到导购信用额度列表为空");
        }
        for (GuideCreditMoney guideCreditMoney : guideCreditMoneyList) {
            if (guideCreditMoney.getTempCreditLimit().compareTo(BigDecimal.ZERO) != 0) {
                for (int i = 1; i <= AppConstant.OPTIMISTIC_LOCK_RETRY_TIME; i++) {
                    GuideCreditMoneyDetail guideCreditMoneyDetail = new GuideCreditMoneyDetail();
                    guideCreditMoneyDetail.setOriginalCreditLimitAvailable(guideCreditMoney.getCreditLimitAvailable());
                    guideCreditMoneyDetail.setOriginalTempCreditLimit(guideCreditMoney.getTempCreditLimit());
                    guideCreditMoneyDetail.setOriginalCreditLimit(guideCreditMoney.getCreditLimit());
                    guideCreditMoneyDetail.setCreditLimitAvailable(guideCreditMoneyDetail.getOriginalCreditLimitAvailable().subtract(guideCreditMoneyDetail.getOriginalTempCreditLimit()));
                    guideCreditMoneyDetail.setTempCreditLimit(BigDecimal.ZERO);
                    guideCreditMoneyDetail.setCreditLimit(guideCreditMoney.getCreditLimit());
                    guideCreditMoneyDetail.setEmpId(guideCreditMoney.getEmpId());
                    guideCreditMoneyDetail.setLastUpdateTime(guideCreditMoney.getLastUpdateTime());
                    Integer affectLine = this.maEmpCreditMoneyDAO.clearTempCreditLimit(guideCreditMoneyDetail);
                    if (affectLine > 0) {
                        GuideTempCreditChange guideTempCreditChange = new GuideTempCreditChange();
                        GuideAvailableCreditChange guideAvailableCreditChange = new GuideAvailableCreditChange();
                        guideTempCreditChange.setTempCreditLimitAfterChange(BigDecimal.ZERO);
                        guideTempCreditChange.setTempCreditLimitChangeAmount(BigDecimal.ZERO.subtract(guideCreditMoney.getTempCreditLimit()));
                        guideTempCreditChange.setChangeType(EmpCreditMoneyChangeType.TEMPORARY_CLEAR.toString());
                        guideTempCreditChange.setChangeTypeDesc(EmpCreditMoneyChangeType.TEMPORARY_CLEAR.getDescription());

                        guideAvailableCreditChange.setCreditLimitAvailableAfterChange(guideCreditMoney.getCreditLimitAvailable().subtract(guideCreditMoney.getTempCreditLimit()));
                        guideAvailableCreditChange.setCreditLimitAvailableChangeAmount(BigDecimal.ZERO.subtract(guideCreditMoney.getTempCreditLimit()));
                        guideAvailableCreditChange.setChangeType(EmpCreditMoneyChangeType.AVALIABLED_CHANGE_BY_TEMP.toString());
                        guideAvailableCreditChange.setChangeTypeDesc(EmpCreditMoneyChangeType.AVALIABLED_CHANGE_BY_TEMP.getDescription());

                        maEmpCreditMoneyDAO.saveTempCreditLimitChange(guideTempCreditChange);
                        maEmpCreditMoneyDAO.saveCreditLimitAvailableChange(guideAvailableCreditChange);
                        //补全导购信用金变更详情VO
                        GuideCreditChangeDetail guideCreditChangeDetail = new GuideCreditChangeDetail();
                        guideCreditChangeDetail.setEmpId(guideCreditMoney.getEmpId());
                        guideCreditChangeDetail.setChangeType(EmpCreditMoneyChangeType.TEMPORARY_CLEAR);
                        guideCreditChangeDetail.setChangeTypeDesc(EmpCreditMoneyChangeType.TEMPORARY_CLEAR.getDescription());
                        guideCreditChangeDetail.setAvailableCreditChangId(guideAvailableCreditChange.getId());
                        guideCreditChangeDetail.setTempCreditChangeId(guideTempCreditChange.getId());
                        this.saveCreditChange(guideCreditChangeDetail);
                    }
                }
            }
        }
    }

    @Override
    public void clearAllTempCredit() {
        maEmpCreditMoneyDAO.clearAllTempCredit();
    }

    @Override
    public List<GuideCreditMoney> findAllGuideCreditMoney() {
        List<GuideCreditMoney> guideCreditMoneyList = this.maEmpCreditMoneyDAO.findAllGuideCreditMoney();
        return guideCreditMoneyList;
    }


    @Override
    public GuideCreditMoney findGuideCreditMoneyAvailableByEmpId(Long sellerId) {
        return this.maEmpCreditMoneyDAO.findGuideCreditMoneyAvailableByEmpId(sellerId);
    }


}

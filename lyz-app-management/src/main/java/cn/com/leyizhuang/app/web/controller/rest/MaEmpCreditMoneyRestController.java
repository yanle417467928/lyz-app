package cn.com.leyizhuang.app.web.controller.rest;

import cn.com.leyizhuang.app.core.constant.EmpCreditMoneyChangeType;
import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.core.utils.order.OrderUtils;
import cn.com.leyizhuang.app.foundation.pojo.management.employee.SimpleEmployeeParam;
import cn.com.leyizhuang.app.quartz.QuartzManager;
import cn.com.leyizhuang.app.core.config.shiro.ShiroUser;
import cn.com.leyizhuang.app.core.utils.IpUtil;
import cn.com.leyizhuang.app.foundation.pojo.GridDataVO;
import cn.com.leyizhuang.app.foundation.pojo.management.guide.*;
import cn.com.leyizhuang.app.foundation.service.MaClearTempCreditService;
import cn.com.leyizhuang.app.foundation.vo.management.guide.GuideCreditChangeDetailVO;
import cn.com.leyizhuang.app.foundation.service.MaEmpCreditMoneyService;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import com.github.pagehelper.PageInfo;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = MaEmpCreditMoneyRestController.PRE_URL, produces = "application/json;charset=utf-8")
public class MaEmpCreditMoneyRestController extends BaseRestController {
    protected static final String PRE_URL = "/rest/guideLine";

    private final Logger logger = LoggerFactory.getLogger(MaEmpCreditMoneyRestController.class);

    @Autowired
    private MaEmpCreditMoneyService maEmpCreditMoneyService;
    @Autowired
    private MaClearTempCreditService maClearTempCreditService;

    /**
     * 修改员工额度
     *
     * @param guideCreditMoneyDetail
     * @param result
     * @return
     */
    @PutMapping
    public ResultDTO<?> restGuideCreditMoneyVOPut(@Valid GuideCreditMoneyDetail guideCreditMoneyDetail, BindingResult result, HttpServletRequest request) {
        logger.info("restGuideCreditMoneyVOPut 后台修改员工额度 ,入参 guideCreditMoneyDetail:{},", guideCreditMoneyDetail);
        try {
            if (!result.hasErrors()) {
                GuideCreditMoney guideCreditMoneyBefore = maEmpCreditMoneyService.findGuideCreditMoneyAvailableByEmpId(guideCreditMoneyDetail.getEmpId());

                ShiroUser shiroUser = this.getShiroUser();
                GuideCreditChangeDetail guideCreditChangeDetail = new GuideCreditChangeDetail();
                guideCreditChangeDetail.setOperatorId(shiroUser.getId());
                guideCreditChangeDetail.setOperatorName(shiroUser.getName());
                guideCreditChangeDetail.setEmpId(guideCreditMoneyDetail.getEmpId());
                guideCreditChangeDetail.setOperatorIp(IpUtil.getIpAddress(request));
                guideCreditChangeDetail.setChangeReason(guideCreditMoneyDetail.getModifyReason());
                //随即生成一个单号
                guideCreditChangeDetail.setReferenceNumber(OrderUtils.getRefundNumber());
                //判断修改类型
                if (null == guideCreditMoneyBefore) {
                    guideCreditMoneyDetail.setOriginalCreditLimitAvailable(BigDecimal.ZERO);
                    guideCreditMoneyDetail.setOriginalCreditLimit(BigDecimal.ZERO);
                    guideCreditMoneyDetail.setOriginalTempCreditLimit(BigDecimal.ZERO);
                } else {
                    guideCreditMoneyDetail.setOriginalCreditLimitAvailable(guideCreditMoneyBefore.getCreditLimitAvailable());
                    guideCreditMoneyDetail.setOriginalCreditLimit(guideCreditMoneyBefore.getCreditLimit());
                    guideCreditMoneyDetail.setOriginalTempCreditLimit(guideCreditMoneyBefore.getTempCreditLimit());
                }
                int isFixEqual = guideCreditMoneyDetail.getOriginalCreditLimit().compareTo(guideCreditMoneyDetail.getCreditLimit());
                int isTempEqual = guideCreditMoneyDetail.getOriginalTempCreditLimit().compareTo(guideCreditMoneyDetail.getTempCreditLimit());
                if (0 != isFixEqual && 0 == isTempEqual) {
                    guideCreditChangeDetail.setChangeType(EmpCreditMoneyChangeType.FIXEDAMOUNT_ADJUSTMENT);
                    guideCreditChangeDetail.setChangeTypeDesc(EmpCreditMoneyChangeType.FIXEDAMOUNT_ADJUSTMENT.getDescription());
                } else if (0 == isFixEqual && 0 != isTempEqual) {
                    guideCreditChangeDetail.setChangeType(EmpCreditMoneyChangeType.TEMPORARY_ADJUSTMENT);
                    guideCreditChangeDetail.setChangeTypeDesc(EmpCreditMoneyChangeType.TEMPORARY_ADJUSTMENT.getDescription());
                } else if(0 == isFixEqual && 0 == isTempEqual){
                    logger.info("固定额度和零时额度都未修改");
                    return new ResultDTO<>(CommonGlobal.COMMON_ERROR_PARAM_CODE, "请修改你需要的额度", null);
                } else{
                    logger.info("固定额度和零时额度都修改了,不能判断该导购的主要变更类型");
                    return new ResultDTO<>(CommonGlobal.COMMON_ERROR_PARAM_CODE, "请不要同时修改两个额度", null);
                }
                //判断 是新增导购额度还是 修改导购额度
                if (null == guideCreditMoneyBefore) {
                    //存入并添加日志
                    maEmpCreditMoneyService.saveEmpCreditMoney(guideCreditMoneyDetail, guideCreditChangeDetail);
                } else {
                   //更新并添加日志
                    this.maEmpCreditMoneyService.update(guideCreditMoneyDetail, guideCreditChangeDetail);
                }
                logger.info("restGuideCreditMoneyVOPut ,后台修改员工额度成功");
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
            } else {
                List<ObjectError> allErrors = result.getAllErrors();
                logger.warn("页面提交的数据有错误：errors = {}", errorMsgToHtml(allErrors));
                System.err.print(allErrors);
                logger.warn("restGuideCreditMoneyVOPut ,后台修改员工额度失败,参数有误");
                return new ResultDTO<>(CommonGlobal.COMMON_ERROR_PARAM_CODE,
                        errorMsgToHtml(allErrors), null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("restGuideCreditMoneyVOPut EXCEPTION,发生未知错误，后台修改员工额度失败");
            logger.warn("{}", e);
            return null;
        }
    }


    /**
     * 手动清零临时额度
     *
     * @param
     * @return
     */
    @PostMapping(value = "/clearTempCreditLimit")
    public ResultDTO<?> clearTempCreditLimit(@RequestParam(value = "empId") Long empId, HttpServletRequest request) {
        logger.info("clearTempCreditLimit 后台手动清零临时额度 ,入参 empId:{},", empId);
        try {
            if (null != empId) {
                //获取当前操作人,并设置额度变更明细
                ShiroUser shiroUser = this.getShiroUser();
                GuideCreditChangeDetail guideCreditChangeDetail = new GuideCreditChangeDetail();
                guideCreditChangeDetail.setOperatorId(shiroUser.getId());
                guideCreditChangeDetail.setOperatorName(shiroUser.getName());
                guideCreditChangeDetail.setEmpId(empId);
                guideCreditChangeDetail.setChangeType(EmpCreditMoneyChangeType.TEMPORARY_CLEAR);
                guideCreditChangeDetail.setChangeTypeDesc(EmpCreditMoneyChangeType.TEMPORARY_CLEAR.getDescription());
                guideCreditChangeDetail.setOperatorIp(IpUtil.getIpAddress(request));
                //获取额度变更明细
                GuideCreditMoneyDetail guideCreditMoneyDetail = new GuideCreditMoneyDetail();
                GuideCreditMoney guideCreditMoney = maEmpCreditMoneyService.findGuideCreditMoneyAvailableByEmpId(empId);
                guideCreditMoneyDetail.setOriginalCreditLimitAvailable(guideCreditMoney.getCreditLimitAvailable());
                guideCreditMoneyDetail.setOriginalTempCreditLimit(guideCreditMoney.getTempCreditLimit());
                guideCreditMoneyDetail.setOriginalCreditLimit(guideCreditMoney.getCreditLimit());
                guideCreditMoneyDetail.setCreditLimitAvailable(guideCreditMoneyDetail.getOriginalCreditLimitAvailable().subtract(guideCreditMoneyDetail.getOriginalTempCreditLimit()));
                guideCreditMoneyDetail.setTempCreditLimit(BigDecimal.ZERO);
                guideCreditMoneyDetail.setCreditLimit(guideCreditMoney.getCreditLimit());
                guideCreditMoneyDetail.setEmpId(empId);
                guideCreditMoneyDetail.setLastUpdateTime(guideCreditMoney.getLastUpdateTime());
                this.maEmpCreditMoneyService.clearTempCreditLimit(guideCreditMoneyDetail, guideCreditChangeDetail);
                logger.info("clearTempCreditLimit ,后台手动清零临时额度成功");
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
            } else {
                logger.warn("clearTempCreditLimit ,后台手动清零临时额度,参数有误 empId:{}", empId);
                return new ResultDTO<>(CommonGlobal.COMMON_ERROR_PARAM_CODE, "参数错误", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("clearTempCreditLimit EXCEPTION,发生未知错误，后台手动清零临时额度失败");
            logger.warn("{}", e);
            return null;
        }
    }


    /**
     * 更新自动清空临时额度时间
     *
     * @param cronTime,jobName
     * @return
     */
    @PutMapping(value = "/change")
    public ResultDTO<?> changeClearTempCreditLimitTime(@RequestParam(value = "cronTime") String cronTime, @RequestParam(value = "jobName") String jobName) {
        logger.info("changeClearTempCreditLimitTime 后台更新自动清空临时额度时间 ,入参 cronTime:{},jobName:{},", cronTime, jobName);
        try {
            if (StringUtils.isNotBlank(cronTime) && StringUtils.isNotBlank(jobName)) {
                Boolean enable = maClearTempCreditService.update(cronTime, jobName);
                if (enable) {
                    QuartzManager.modifyJobTime("clearTempCredit", "jobGroup", "trigger", "triggerGroup", cronTime);
                    logger.info("changeClearTempCreditLimitTime ,后台更新自动清空临时额度时间成功");
                    return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
                } else {
                    logger.warn("后台更新自动清空临时额度时间失败");
                    return new ResultDTO<>(CommonGlobal.COMMON_ERROR_PARAM_CODE, "后台更新自动清空临时额度时间失败"
                            , null);
                }
            } else {
                logger.warn("页面提交ID有误：errors = {}");
                return new ResultDTO<>(CommonGlobal.COMMON_ERROR_PARAM_CODE, "参数为空"
                        , null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("changeClearTempCreditLimitTime EXCEPTION,发生未知错误，后台更新自动清空临时额度时间失败");
            logger.warn("{}", e);
            return null;
        }
    }

    /**
     * 可用额度改变详情列表
     *
     * @param offset
     * @param size
     * @param keywords
     * @param id
     * @return
     */
    @GetMapping(value = "/availableCreditChangePage/grid/{id}")
    public GridDataVO<GuideCreditChangeDetailVO> restAvailableCreditChangesPageGird(Integer offset, Integer size, String keywords, @PathVariable(value = "id") Long id) {
        logger.info("restAvailableCreditChangesPageGird 后台获取可用额度改变详情列表 ,入参 offset:{},size:{},keywords:{},id:{}", offset, size, keywords, id);
        try {
            size = getSize(size);
            Integer page = getPage(offset, size);
            PageInfo<GuideCreditChangeDetailDO> creditMoneyChangePage = this.maEmpCreditMoneyService.queryAvailableCreditMoneyChangePage(page, size, id);
            List<GuideCreditChangeDetailDO> creditMoneyChangePageList = creditMoneyChangePage.getList();
            List<GuideCreditChangeDetailVO> creditMoneyChangeVOPageList = GuideCreditChangeDetailVO.transform(creditMoneyChangePageList);
            logger.info("restAvailableCreditChangesPageGird ,后台获取可用额度改变详情列表成功", creditMoneyChangeVOPageList.size());
            return new GridDataVO<GuideCreditChangeDetailVO>().transform(creditMoneyChangeVOPageList, creditMoneyChangePage.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("restAvailableCreditChangesPageGird EXCEPTION,发生未知错误，后台获取可用额度改变详情列表失败");
            logger.warn("{}", e);
            return null;
        }
    }

    /**
     * 临时额度改变详情页
     *
     * @param offset
     * @param size
     * @param keywords
     * @param id
     * @return
     */
    @GetMapping(value = "/tempCreditChangePage/grid/{id}")
    public GridDataVO<GuideCreditChangeDetailVO> restTempCreditChangesPageGird(Integer offset, Integer size, String keywords, @PathVariable(value = "id") Long id) {
        logger.info("restTempCreditChangesPageGird 后台获取临时额度改变详列表 ,入参 offset:{},size:{},keywords:{},id:{}", offset, size, keywords, id);
        try {
            size = getSize(size);
            Integer page = getPage(offset, size);
            PageInfo<GuideCreditChangeDetailDO> creditMoneyChangePage = this.maEmpCreditMoneyService.queryTempCreditMoneyChangePage(page, size, id);
            List<GuideCreditChangeDetailDO> creditMoneyChangePageList = creditMoneyChangePage.getList();
            List<GuideCreditChangeDetailVO> creditMoneyChangeVOPageList = GuideCreditChangeDetailVO.transform(creditMoneyChangePageList);
            logger.info("restTempCreditChangesPageGird ,后台获取临时额度改变详列表成功", creditMoneyChangeVOPageList.size());
            return new GridDataVO<GuideCreditChangeDetailVO>().transform(creditMoneyChangeVOPageList, creditMoneyChangePage.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("restTempCreditChangesPageGird EXCEPTION,发生未知错误，后台获取临时额度改变详列表失败");
            logger.warn("{}", e);
            return null;
        }
    }

    /**
     * 固定额度改变详情页
     *
     * @param offset
     * @param size
     * @param keywords
     * @param id
     * @return
     */
    @GetMapping(value = "/fixedCreditChangePage/grid/{id}")
    public GridDataVO<GuideCreditChangeDetailVO> restFixedCreditChangesPageGird(Integer offset, Integer size, String keywords, @PathVariable(value = "id") Long id) {
        logger.info("restFixedCreditChangesPageGird 后台获取固定额度改变详情列表 ,入参 offset:{},size:{},keywords:{},id:{}", offset, size, keywords, id);
        try {
            size = getSize(size);
            Integer page = getPage(offset, size);
            PageInfo<GuideCreditChangeDetailDO> creditMoneyChangePage = this.maEmpCreditMoneyService.queryFixedCreditMoneyChangePage(page, size, id);
            List<GuideCreditChangeDetailDO> creditMoneyChangePageList = creditMoneyChangePage.getList();
            List<GuideCreditChangeDetailVO> creditMoneyChangeVOPageList = GuideCreditChangeDetailVO.transform(creditMoneyChangePageList);
            logger.info("restFixedCreditChangesPageGird ,后台获取固定额度改变详情列表成功", creditMoneyChangeVOPageList.size());
            return new GridDataVO<GuideCreditChangeDetailVO>().transform(creditMoneyChangeVOPageList, creditMoneyChangePage.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("restFixedCreditChangesPageGird EXCEPTION,发生未知错误，后台获取固定额度改变详情列表失败");
            logger.warn("{}", e);
            return null;
        }
    }

}

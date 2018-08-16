package cn.com.leyizhuang.app.foundation.service.impl;


import cn.com.leyizhuang.app.core.constant.AppCustomerCreateType;
import cn.com.leyizhuang.app.core.constant.AppCustomerLightStatus;
import cn.com.leyizhuang.app.core.constant.AppCustomerType;
import cn.com.leyizhuang.app.core.constant.LoanSubjectType;
import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.dao.MaCustomerDAO;
import cn.com.leyizhuang.app.foundation.dto.CusLebiDTO;
import cn.com.leyizhuang.app.foundation.dto.CusPreDepositDTO;
import cn.com.leyizhuang.app.foundation.pojo.AppStore;
import cn.com.leyizhuang.app.foundation.pojo.PayhelperInfo;
import cn.com.leyizhuang.app.foundation.pojo.RankStore;
import cn.com.leyizhuang.app.foundation.pojo.management.customer.CustomerDO;
import cn.com.leyizhuang.app.foundation.pojo.management.customer.MaCustomerPreDeposit;
import cn.com.leyizhuang.app.foundation.pojo.management.customer.MaSimpleCustomerParam;
import cn.com.leyizhuang.app.foundation.pojo.response.MaCreateOrderPeopleResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.ManageUpdateCustomerTypeResponse;
import cn.com.leyizhuang.app.foundation.pojo.user.*;
import cn.com.leyizhuang.app.foundation.service.*;
import cn.com.leyizhuang.app.foundation.vo.management.customer.CusPreDepositLogVO;
import cn.com.leyizhuang.app.foundation.vo.management.customer.CustomerDetailVO;
import cn.com.leyizhuang.app.foundation.vo.management.customer.CustomerLebiVO;
import cn.com.leyizhuang.app.foundation.vo.management.customer.CustomerPreDepositVO;
import cn.com.leyizhuang.common.core.exception.AppConcurrentExcp;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class MaCustomerServiceImpl implements MaCustomerService {

    @Resource
    private MaCustomerDAO maCustomerDAO;

    @Autowired
    private MaCusPreDepositLogService maCusPreDepositLogService;

    @Autowired
    private MaCusLebiLogService maCusLebiLogService;

    @Resource
    private AppStoreService appStoreService;

    @Resource
    private AppCustomerService appCustomerService;

    @Resource
    private AppEmployeeService appEmployeeService;


    @Override
    public PageInfo<CustomerDO> queryPageVO(Integer page, Integer size, List<Long> storeIds) {
        PageHelper.startPage(page, size);
        List<CustomerDO> CustmoerList = maCustomerDAO.findAllVO(storeIds);
        return new PageInfo<>(CustmoerList);
    }

    @Override
    public CustomerDO queryCustomerVOById(Long cusId) {
        if (cusId != null) {
            CustomerDO customerDO = maCustomerDAO.queryCustomerVOById(cusId);
            return customerDO;
        }
        return null;
    }

    @Override
    public PageInfo<CustomerDO> queryCustomerVOByCityId(Integer page, Integer size, Long cityId, List<Long> storeIds) {
        PageHelper.startPage(page, size);
        List<CustomerDO> CustmoerList = maCustomerDAO.queryCustomerVOByCityId(cityId, storeIds);
        return new PageInfo<>(CustmoerList);
    }


    @Override
    public PageInfo<CustomerDO> queryCustomerVOByStoreId(Integer page, Integer size, Long storeId) {
        PageHelper.startPage(page, size);
        List<CustomerDO> CustmoerList = maCustomerDAO.queryCustomerVOByStoreId(storeId);
        return new PageInfo<>(CustmoerList);
    }

    @Override
    public PageInfo<CustomerDO> queryCustomerVOByGuideId(Integer page, Integer size, Long guideId) {
        PageHelper.startPage(page, size);
        List<CustomerDO> CustmoerList = maCustomerDAO.queryCustomerVOByGuideId(guideId);
        return new PageInfo<>(CustmoerList);
    }

    @Override
    public PageInfo<CustomerDO> queryCustomerVOByPhone(Integer page, Integer size, Long queryCusInfo, List<Long> storeIds) {
        PageHelper.startPage(page, size);
        List<CustomerDO> CustmoerList = maCustomerDAO.queryCustomerVOByPhone(queryCusInfo, storeIds);
        return new PageInfo<>(CustmoerList);
    }

    @Override
    public PageInfo<CustomerDO> queryCustomerVOByName(Integer page, Integer size, String queryCusInfo, List<Long> storeIds) {
        PageHelper.startPage(page, size);
        List<CustomerDO> CustmoerList = maCustomerDAO.queryCustomerVOByName(queryCusInfo, storeIds);
        return new PageInfo<>(CustmoerList);
    }

    @Override
    public void saveCustomer(CustomerDetailVO customer) {
        if (null != customer) {
            Date date = new Date();
            customer.setCreateTime(date);
            customer.setLight(AppCustomerLightStatus.GREEN);
            customer.setCreateType(AppCustomerCreateType.ADMIN_CREATE);
            if (null != customer.getSalesConsultId()) {
                customer.setBindingTime(date);
            }
            CustomerDO customerDO = CustomerDO.transform(customer);
            maCustomerDAO.save(customerDO);

            CustomerLeBi leBi = new CustomerLeBi();
            CustomerPreDeposit preDeposit = new CustomerPreDeposit();
            leBi.setCusId(customerDO.getCusId());
            leBi.setQuantity(leBi.getQuantity() == null ? 0 : leBi.getQuantity());
            appCustomerService.saveLeBi(leBi);
            preDeposit.setCusId(customerDO.getCusId());
            preDeposit.setBalance(preDeposit.getBalance() == null ? 0 : preDeposit.getBalance());
            appCustomerService.savePreDeposit(preDeposit);
        }
    }


    @Override
    public void updateCustomer(CustomerDetailVO customer) {
        if (null != customer) {
            CustomerDO customerDO = CustomerDO.transform(customer);
            maCustomerDAO.updateCustomer(customerDO);
        }
    }

    @Override
    public Boolean isExistPhoneNumber(Long moblie) {
        return maCustomerDAO.isExistPhoneNumber(moblie);
    }

    @Override
    public Boolean isExistPhoneNumberByCusId(Long mobile, Long cusId) {
        return maCustomerDAO.isExistPhoneNumberByCusId(mobile, cusId);
    }

    @Override
    public PageInfo<CustomerPreDepositVO> findAllCusPredeposit(Integer page, Integer size, Long cityId, Long storeId, String keywords, List<Long> storeIds) {
        PageHelper.startPage(page, size);
        List<CustomerPreDepositVO> list = this.maCustomerDAO.findAllCusPredeposit(cityId, storeId, keywords, storeIds);
        return new PageInfo<>(list);
    }

    @Override
    public CustomerPreDepositVO queryCusPredepositByCusId(Long cusId) {
        return this.maCustomerDAO.queryCusPredepositByCusId(cusId);
    }

    @Override
    public void changeCusPredepositByCusId(CusPreDepositDTO cusPreDepositDTO) throws Exception {
        Long userId = cusPreDepositDTO.getCusId();
        Double money = cusPreDepositDTO.getChangeMoney();
        CustomerPreDeposit customerPreDeposit = this.maCustomerDAO.findPreDepositByCusId(userId);
        if (null == customerPreDeposit) {
            customerPreDeposit = new CustomerPreDeposit();
            customerPreDeposit.setBalance(money);
            customerPreDeposit.setCusId(userId);
            customerPreDeposit.setCreateTime(new Date());
            customerPreDeposit.setLastUpdateTime(new Timestamp(System.currentTimeMillis()));
            this.maCustomerDAO.savePreDeposit(customerPreDeposit);
        } else {
            int row = this.maCustomerDAO.updateDepositByUserId(userId, money, new Timestamp(System.currentTimeMillis()), customerPreDeposit.getLastUpdateTime());
            if (1 != row) {
                throw new AppConcurrentExcp("账号余额信息过期！");
            }
        }
        this.maCusPreDepositLogService.save(cusPreDepositDTO);
    }

    @Override
    public PageInfo<CustomerLebiVO> findAllCusLebi(Integer page, Integer size, Long cityId, Long storeId, String keywords, List<Long> storeIds) {
        PageHelper.startPage(page, size);
        List<CustomerLebiVO> list = this.maCustomerDAO.findAllCusLebi(cityId, storeId, keywords, storeIds);
        return new PageInfo<>(list);
    }

    @Override
    public CustomerLebiVO queryCusLebiByCusId(Long cusId) {
        return this.maCustomerDAO.queryCusLebiByCusId(cusId);
    }

    @Override
    public void changeCusLebiByCusId(CusLebiDTO cusLebiDTO) throws Exception {
        Long userId = cusLebiDTO.getCusId();
        Integer quantity = cusLebiDTO.getChangeNum();
        CustomerLeBi customerLeBi = this.maCustomerDAO.findLebiByCusId(userId);
        if (null == customerLeBi) {
            customerLeBi = new CustomerLeBi();
            customerLeBi.setQuantity(quantity);
            customerLeBi.setCusId(userId);
            customerLeBi.setCreateTime(new Date());
            customerLeBi.setLastUpdateTime(new Timestamp(System.currentTimeMillis()));
            this.maCustomerDAO.saveLebi(customerLeBi);
        } else {
            int row = this.maCustomerDAO.updateLebiByUserId(userId, quantity, new Timestamp(System.currentTimeMillis()), customerLeBi.getLastUpdateTime());
            if (1 != row) {
                throw new AppConcurrentExcp("账号余额信息过期！");
            }
        }
        this.maCusLebiLogService.save(cusLebiDTO);
    }

    @Override
    public List<CustomerDO> findCustomerByCityIdAndStoreId(List<Long> storeIds) {
        return this.maCustomerDAO.findCustomerByCityIdAndStoreId(storeIds);
    }

    @Override
    public List<CustomerDO> findCustomerByCityIdAndStoreIdAndCustomerNameAndCustomerPhone(String customerQueryConditions, List<Long> storeIds) {
        List<CustomerDO> customerDOList = null;
        if (customerQueryConditions.matches("[0-9]{11}")) {
            customerDOList = this.maCustomerDAO.findCustomerByCustomerPhone(customerQueryConditions);
        } else {
            customerDOList = this.maCustomerDAO.findCustomerByCityIdAndStoreIdAndCustomerNameAndCustomerPhone(customerQueryConditions, storeIds);
        }
        return customerDOList;
    }

    @Override
    public Integer updateDepositByUserIdAndVersion(Long userId, Double customerDeposit, Date version) {
        return maCustomerDAO.updateDepositByUserIdAndVersion(userId, customerDeposit, version);
    }


    @Override
    public void saveCusPreDepositLog(MaCustomerPreDeposit customerPreDeposit) {
        this.maCustomerDAO.saveCusPreDepositLog(customerPreDeposit);
    }

    @Override
    public Long findCityIdByCusId(Long cusId) {
        return this.maCustomerDAO.findCityIdByCusId(cusId);
    }

    @Override
    public ManageUpdateCustomerTypeResponse queryCustomerById(Long id) {
        if (null != id) {
            return this.maCustomerDAO.findCustomerById(id);
        } else {
            return null;
        }

    }

    @Override
    public List<RankClassification> findRankAll() {
        List<RankClassification> rankClassificationList = this.maCustomerDAO.findRankAll();
        RankClassification rankClassification = new RankClassification();
        rankClassification.setRankCode("COMMON");
        rankClassification.setRankName("一般会员");
        rankClassificationList.add(rankClassification);
        return rankClassificationList;

    }

    @Override
    public Boolean findRankStoreByStoreId(Long storeId) {
        RankStore rankStore = this.maCustomerDAO.findRankStoreByStoreId(storeId);
        if (null == rankStore) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMemberType(ManageUpdateCustomerTypeResponse manageUpdateCustomerTypeResponse) throws UnsupportedEncodingException {
        CusRankDO cusRankDO = this.maCustomerDAO.findCusRankByCusId(manageUpdateCustomerTypeResponse.getCusId());
        RankClassification rankClassification = this.maCustomerDAO.findRankClassificationByRankCode(manageUpdateCustomerTypeResponse.getMemberType());
        AppCustomer customer = appCustomerService.findById(manageUpdateCustomerTypeResponse.getCusId());
        RankStore rankStore = this.maCustomerDAO.findRankStoreByStoreId(customer.getStoreId());
        PayhelperInfo payhelperInfo = this.maCustomerDAO.findPayhelperInfoByCusId(manageUpdateCustomerTypeResponse.getCusId());

        if ("COMMON".equals(manageUpdateCustomerTypeResponse.getMemberType())) {
            if (null != cusRankDO) {
                this.maCustomerDAO.deleteCusRankByCusId(manageUpdateCustomerTypeResponse.getCusId());
            }
            if (null == customer.getSalesConsultId() || !manageUpdateCustomerTypeResponse.getSellerId().equals(customer.getSalesConsultId())) {
                AppEmployee employee = appEmployeeService.findById(manageUpdateCustomerTypeResponse.getSellerId());
                AppStore store1 = appStoreService.findById(employee.getStoreId());
                appCustomerService.updateCustomerSellerIdStoreIdByCusId(manageUpdateCustomerTypeResponse.getCusId(), employee.getStoreId(), employee.getEmpId(), new Date());
                if (null == payhelperInfo) {
                    payhelperInfo = new PayhelperInfo();
                    payhelperInfo.setCreateTime(new Date());
                    payhelperInfo.setLenderId(customer.getCusId());
                    payhelperInfo.setLenderName(customer.getName());
                    payhelperInfo.setLenderPhone(customer.getMobile());
                    payhelperInfo.setLoanSubjectType(LoanSubjectType.ZG);
                    payhelperInfo.setStoreCode(store1.getStoreCode());
                    payhelperInfo.setSellerManagerId(employee.getEmpId());
                    payhelperInfo.setSellerManagerName(employee.getName());
                    payhelperInfo.setSellerManagerPhone(employee.getMobile());
                    payhelperInfo.setIsOpenPayhepler(Boolean.TRUE);
                    this.maCustomerDAO.addPayhelperInfo(payhelperInfo);
                } else {
                    payhelperInfo.setStoreCode(store1.getStoreCode());
                    payhelperInfo.setSellerManagerId(employee.getEmpId());
                    payhelperInfo.setSellerManagerName(employee.getName());
                    payhelperInfo.setSellerManagerPhone(employee.getMobile());
                    payhelperInfo.setIsOpenPayhepler(Boolean.TRUE);
                    this.maCustomerDAO.updatePayhelperInfoByCusId(payhelperInfo);
                }
            } else {
                if (null == payhelperInfo) {
                    AppStore store = appStoreService.findById(customer.getStoreId());
                    AppEmployee appEmployee = appEmployeeService.findById(customer.getSalesConsultId());
                    payhelperInfo = new PayhelperInfo();
                    payhelperInfo.setCreateTime(new Date());
                    payhelperInfo.setLenderId(customer.getCusId());
                    payhelperInfo.setLenderName(customer.getName());
                    payhelperInfo.setLenderPhone(customer.getMobile());
                    payhelperInfo.setLoanSubjectType(LoanSubjectType.ZG);
                    payhelperInfo.setStoreCode(store.getStoreCode());
                    payhelperInfo.setSellerManagerId(appEmployee.getEmpId());
                    payhelperInfo.setSellerManagerName(appEmployee.getName());
                    payhelperInfo.setSellerManagerPhone(appEmployee.getMobile());
                    payhelperInfo.setIsOpenPayhepler(Boolean.TRUE);
                    this.maCustomerDAO.addPayhelperInfo(payhelperInfo);
                } else {
                    payhelperInfo.setIsOpenPayhepler(Boolean.TRUE);
                    this.maCustomerDAO.updatePayhelperInfoByCusId(payhelperInfo);
                }
            }
            return;
        }
        if (null == customer.getSalesConsultId() || !manageUpdateCustomerTypeResponse.getSellerId().equals(customer.getSalesConsultId())) {
            AppEmployee employee = appEmployeeService.findById(manageUpdateCustomerTypeResponse.getSellerId());
            appCustomerService.updateCustomerSellerIdStoreIdByCusId(manageUpdateCustomerTypeResponse.getCusId(), employee.getStoreId(), employee.getEmpId(), new Date());
            RankStore rankStore2 = this.maCustomerDAO.findRankStoreByStoreId(employee.getStoreId());
            AppStore store1 = appStoreService.findById(employee.getStoreId());
            if (null == rankStore2) {
                RankStore newRankStore1 = new RankStore();
                newRankStore1.setStoreId(store1.getStoreId());
                newRankStore1.setStoreCode(store1.getStoreCode());
                newRankStore1.setStoreName(store1.getStoreName());
                newRankStore1.setCityId(store1.getCityId());
                newRankStore1.setCityName(store1.getCity());
                newRankStore1.setCompanyCode(store1.getStoreStructureCode());
                newRankStore1.setCompanyName(null);
                newRankStore1.setCreateTime(new Date());
                maCustomerDAO.saveRankStore(newRankStore1);
            }
            if (null == payhelperInfo) {
                payhelperInfo = new PayhelperInfo();
                payhelperInfo.setCreateTime(new Date());
                payhelperInfo.setLenderId(customer.getCusId());
                payhelperInfo.setLenderName(customer.getName());
                payhelperInfo.setLenderPhone(customer.getMobile());
                payhelperInfo.setLoanSubjectType(LoanSubjectType.ZG);
                payhelperInfo.setStoreCode(store1.getStoreCode());
                payhelperInfo.setSellerManagerId(employee.getEmpId());
                payhelperInfo.setSellerManagerName(employee.getName());
                payhelperInfo.setSellerManagerPhone(employee.getMobile());
                payhelperInfo.setIsOpenPayhepler(Boolean.TRUE);
                this.maCustomerDAO.addPayhelperInfo(payhelperInfo);
            } else {
                payhelperInfo.setStoreCode(store1.getStoreCode());
                payhelperInfo.setSellerManagerId(employee.getEmpId());
                payhelperInfo.setSellerManagerName(employee.getName());
                payhelperInfo.setSellerManagerPhone(employee.getMobile());
                payhelperInfo.setIsOpenPayhepler(Boolean.TRUE);
                this.maCustomerDAO.updatePayhelperInfoByCusId(payhelperInfo);
            }
        } else {
            AppStore store = appStoreService.findById(customer.getStoreId());
            AppEmployee appEmployee = appEmployeeService.findById(customer.getSalesConsultId());
            if (null == rankStore) {
                RankStore newRankStore = new RankStore();
                newRankStore.setStoreId(store.getStoreId());
                newRankStore.setStoreCode(store.getStoreCode());
                newRankStore.setStoreName(store.getStoreName());
                newRankStore.setCityId(store.getCityId());
                newRankStore.setCityName(store.getCity());
                newRankStore.setCompanyCode(store.getStoreStructureCode());
                newRankStore.setCompanyName(null);
                newRankStore.setCreateTime(new Date());
                maCustomerDAO.saveRankStore(newRankStore);
            }
            if (null == payhelperInfo) {
                payhelperInfo = new PayhelperInfo();
                payhelperInfo.setCreateTime(new Date());
                payhelperInfo.setLenderId(customer.getCusId());
                payhelperInfo.setLenderName(customer.getName());
                payhelperInfo.setLenderPhone(customer.getMobile());
                payhelperInfo.setLoanSubjectType(LoanSubjectType.ZG);
                payhelperInfo.setStoreCode(store.getStoreCode());
                payhelperInfo.setSellerManagerId(appEmployee.getEmpId());
                payhelperInfo.setSellerManagerName(appEmployee.getName());
                payhelperInfo.setSellerManagerPhone(appEmployee.getMobile());
                payhelperInfo.setIsOpenPayhepler(Boolean.TRUE);
                this.maCustomerDAO.addPayhelperInfo(payhelperInfo);
            } else {
                payhelperInfo.setIsOpenPayhepler(Boolean.TRUE);
                this.maCustomerDAO.updatePayhelperInfoByCusId(payhelperInfo);
            }
        }

        if (manageUpdateCustomerTypeResponse.getMemberType() != null && !manageUpdateCustomerTypeResponse.getMemberType().equals("")
                && !manageUpdateCustomerTypeResponse.equals("COMMON") && AppCustomerType.MEMBER != customer.getCustomerType()) {
            AppCustomer appCustomer = new AppCustomer();
            appCustomer.setCusId(customer.getCusId());
            appCustomer.setCustomerType(AppCustomerType.MEMBER);
            appCustomerService.update(appCustomer);
        }

        if (null == cusRankDO) {
            CusRankDO newCusRank = new CusRankDO();
            newCusRank.setCusId(manageUpdateCustomerTypeResponse.getCusId());
            newCusRank.setRankId(rankClassification.getRankId());
            newCusRank.setNumber(null);
            newCusRank.setCreateTime(new Date());
            maCustomerDAO.saveCusRank(newCusRank);
        } else {
            maCustomerDAO.updateMemberTypeByRankIdAndCusId(rankClassification.getRankId(), manageUpdateCustomerTypeResponse.getCusId());
        }
    }

    @Override
    public PageInfo<MaCreateOrderPeopleResponse> maFindCreatePeople(Integer page, Integer size, String keywords, String peopleType, Long storeId) {
        PageHelper.startPage(page, size);
        List<MaCreateOrderPeopleResponse> maCreateOrderPeopleResponseList = maCustomerDAO.maFindCreatePeople(keywords, peopleType, storeId);
        return new PageInfo<>(maCreateOrderPeopleResponseList);
    }

    @Override
    public PageInfo<MaCreateOrderPeopleResponse> maFindProxyCreatePeople(Integer page, Integer size, String keywords) {
        PageHelper.startPage(page, size);
        List<MaCreateOrderPeopleResponse> maCreateOrderPeopleResponseList = maCustomerDAO.maFindProxyCreatePeople(keywords);
        return new PageInfo<>(maCreateOrderPeopleResponseList);
    }

    @Override
    public PageInfo<MaCreateOrderPeopleResponse> maFindCreatePeopleByStoreId(Integer page, Integer size, Long storeId) {
        PageHelper.startPage(page, size);
        List<MaCreateOrderPeopleResponse> maCreateOrderPeopleResponseList = maCustomerDAO.maFindCreatePeopleByStoreId(storeId);
        return new PageInfo<>(maCreateOrderPeopleResponseList);
    }

    @Override
    public CusPreDepositLogVO queryLastDecorativeCreditChange(Long id) {

        if (id == null) {
            return null;
        }

        return maCustomerDAO.queryLastDecorativeCreditChange(id);
    }

    @Override
    public List<MaSimpleCustomerParam> findCustomerByStoreId(Long storeId) {
        if (null == storeId) {
            return null;
        }
        return maCustomerDAO.findCustomerByStoreId(storeId);
    }
}





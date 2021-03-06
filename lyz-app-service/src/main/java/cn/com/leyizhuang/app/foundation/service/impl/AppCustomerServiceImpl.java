package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.constant.AppConstant;
import cn.com.leyizhuang.app.core.constant.CustomerPreDepositChangeType;
import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.dao.AppCustomerDAO;
import cn.com.leyizhuang.app.foundation.pojo.*;
import cn.com.leyizhuang.app.foundation.pojo.request.UserSetInformationReq;
import cn.com.leyizhuang.app.foundation.pojo.response.*;
import cn.com.leyizhuang.app.foundation.pojo.user.*;
import cn.com.leyizhuang.app.foundation.service.AppCustomerService;
import cn.com.leyizhuang.app.foundation.service.AppEmployeeService;
import cn.com.leyizhuang.app.foundation.service.AppStoreService;
import cn.com.leyizhuang.app.foundation.service.CusPreDepositLogService;
import cn.com.leyizhuang.common.core.exception.AppConcurrentExcp;
import cn.com.leyizhuang.common.util.AssertUtil;
import cn.com.leyizhuang.common.util.CountUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * lyz-app-facade用户服务实现类
 *
 * @author Richard
 * Created on 2017-09-19 11:23
 **/
@Service
public class AppCustomerServiceImpl implements AppCustomerService {
    @Resource
    private AppCustomerDAO customerDAO;

    @Resource
    private AppEmployeeService appEmployeeService;

    @Resource
    private AppStoreService appStoreService;

    @Autowired
    private CusPreDepositLogService cusPreDepositLogServiceImpl;

    @Override
    @Transactional
    public AppCustomer save(AppCustomer appCustomer) {
        if (null != appCustomer) {
            customerDAO.save(appCustomer);
            return appCustomer;
        }
        return null;
    }

    @Override
    public AppCustomer findByOpenId(String openId) throws UnsupportedEncodingException {
        if (null != openId && !"".equalsIgnoreCase(openId)) {
            AppCustomer customer = customerDAO.findByOpenId(openId);
            if (null != customer && null != customer.getNickName()) {
                String nickName = URLDecoder.decode(customer.getNickName(), "utf-8");
                customer.setNickName(nickName);
            }
            return customer;
        }
        return null;
    }

    @Override
    public AppCustomer findByMobile(String phone) throws UnsupportedEncodingException {
        if (null != phone) {
            AppCustomer customer = customerDAO.findByMobile(phone);
            if (null != customer && null != customer.getNickName()) {
                String nickName = URLDecoder.decode(customer.getNickName(), "utf-8");
                customer.setNickName(nickName);
            }
            return customer;
        }
        return null;
    }

    @Override
    @Transactional
    public void update(AppCustomer phoneUser) throws UnsupportedEncodingException {
        if (null != phoneUser) {
            if (null != phoneUser.getNickName()) {
                String utf8NickName = URLEncoder.encode(phoneUser.getNickName(), "utf-8");
                phoneUser.setNickName(utf8NickName);
            }

            customerDAO.update(phoneUser);
        }
    }

    @Override
    public AppCustomer findById(Long cusId) throws UnsupportedEncodingException {
        if (null != cusId) {
            AppCustomer customer = customerDAO.findById(cusId);
            if (null != customer && null != customer.getNickName()) {
                String nickName = URLDecoder.decode(customer.getNickName(), "utf-8");
                customer.setNickName(nickName);
            }
            return customer;

        }
        return null;
    }

    @Override
    public List<CashCouponResponse> findCashCouponByCustomerId(Long userId) {
        if (null != userId) {
            return customerDAO.findCashCouponByCustomerId(userId);
        }
        return null;
    }



    @Override
    public List<ProductCouponResponse> findProductCouponByCustomerId(Long userId) {
        if (null != userId) {
            return customerDAO.findProductCouponByCustomerIdNew(userId);
        }
        return null;
    }

    @Override
    public PageInfo<AppCustomer> findListByUserIdAndIdentityType(Long userId, Integer identityType, Integer page, Integer size) {
        if (null != userId && null != identityType && identityType == 0) {
            PageHelper.startPage(page, size);
            List<AppCustomer> appCustomerList = customerDAO.findListBySalesConsultId(userId);
            return new PageInfo<>(appCustomerList);
        }
        return null;
    }

    @Override
    public List<CustomerListResponse> searchByUserIdAndKeywordsAndIdentityType(Long userId, String keywords, Integer identityType) {
        if (StringUtils.isNotBlank(keywords) && null != userId && null != identityType && identityType == 0) {
            List<AppCustomer> appCustomerList = customerDAO.searchBySalesConsultIdAndKeywords(userId, keywords);
            return CustomerListResponse.transform(appCustomerList);
        }
        return null;
    }

    @Override
    @Transactional
    public void modifyCustomerInformation(UserSetInformationReq userInformation) {
        if (null != userInformation) {
//            customerDAO.update(transform(userInformation));
        }
    }

    @Override
    public Boolean existsByCustomerId(Long userId) {
        return this.customerDAO.existsByCustomerId(userId);
    }

    @Override
    public Double findPreDepositBalanceByUserIdAndIdentityType(Long userId, Integer identityType) {
        if (null != userId && null != identityType && identityType == 6) {
            return customerDAO.findPreDepositBalanceByUserId(userId);
        }
        return null;
    }

    @Override
    public Integer findLeBiQuantityByUserIdAndIdentityType(Long userId, Integer identityType) {
        if (null != userId && null != identityType && identityType == 6) {
            return customerDAO.findLeBiQuantityByUserId(userId);
        }
        return null;
    }

    @Override
    public Map<String, Object> findLeBiByUserIdAndGoodsMoney(Long userId, Double goodsMoney) {
        if (null != userId && null != goodsMoney) {
            Map<String, Object> lbMap = new HashMap<>(2);
            Integer qty = customerDAO.findLeBiQuantityByUserId(userId);
            qty = null == qty ? 0 : qty;
            Double rebate = CountUtil.div(qty, AppConstant.RMB_TO_LEBI_RATIO);
            if (rebate >= goodsMoney) {
                rebate = goodsMoney;
                qty = (int) CountUtil.mul(goodsMoney, AppConstant.RMB_TO_LEBI_RATIO);
            }
            lbMap.put("quantity", qty);
            lbMap.put("rebate", rebate);
            return lbMap;
        }
        return null;
    }

    @Override
    @Transactional
    public void addLeBiQuantityByUserIdAndIdentityType(Long userId, Integer identityType, int qty) {
        if (null != userId && null != identityType && identityType == 6) {
            customerDAO.updateLeBiQuantityByUserId(userId, qty);
        }
    }

    @Override
    @Transactional
    public void modifyCustomerMobileByUserId(Long userId, String mobile) {
        if (null != userId && StringUtils.isNotBlank(mobile)) {
            customerDAO.updateCustomerMobileByUserId(userId, mobile);
        }
    }

    @Override
    @Transactional
    public void saveLeBi(CustomerLeBi leBi) {
        if (null != leBi) {
            customerDAO.saveLeBi(leBi);
        }
    }

    @Override
    @Transactional
    public void savePreDeposit(CustomerPreDeposit preDeposit) {
        if (null != preDeposit) {
            customerDAO.savePreDeposit(preDeposit);
        }
    }

    @Override
    public void updateCustomerSignInfoByCustomerId(Long cusId, Date date, int consecutiveSignDays) {
        if (null != cusId) {
            customerDAO.updateCustomerSignInfoByCustomerId(cusId, date, consecutiveSignDays);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int lockCustomerDepositByUserIdAndDeposit(Long userId, Double customerDeposit, Timestamp version) {
        if (null != userId && null != customerDeposit) {
            return customerDAO.updateDepositByUserIdAndDeposit(userId, customerDeposit, version);
        }
        return 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int lockCustomerLebiByUserIdAndQty(Long userId, Integer lebiQty, Timestamp version) {
        if (null != userId && null != lebiQty) {
            return customerDAO.updateLeBiQuantityByUserIdAndQty(userId, lebiQty, version);
        }
        return 0;
    }

    @Override
    public CustomerHomePageResponse findCustomerInfoByUserId(Long userId) {
        if (null != userId) {
            return customerDAO.findCustomerInfoByUserId(userId);
        }
        return null;
    }

    @Override
    @Transactional
    public int lockCustomerProductCouponByUserIdAndProductCoupons(Long userId, Map<Long, Integer> productCoupon) {
        if (null != userId && !productCoupon.isEmpty()) {
            for (Long index : productCoupon.keySet()) {
                int result = customerDAO.updateProductCouponByUserIdAndProductCoupons(userId, index, productCoupon.get(index));
                if (result == 0) {
                    return 0;
                }
            }
        }
        return 1;
    }

    @Override
    @Transactional
    public int lockCustomerCashCouponByUserIdAndCashCoupons(Long userId, Map<Long, Integer> cashCoupon) {
        if (null != userId && !cashCoupon.isEmpty()) {
            for (Long index : cashCoupon.keySet()) {
                int result = customerDAO.updateCashCouponByUserIdAndCashCoupons(userId, index, cashCoupon.get(index));
                if (result == 0) {
                    return 0;
                }
            }
        }
        return 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unlockCustomerDepositByUserIdAndDeposit(Long userId, Double customerDeposit) {
        if (null != userId && null != customerDeposit) {
            customerDAO.updateDepositByUserId(userId, customerDeposit);
        }
    }

    @Override
    @Transactional
    public void unlockCustomerLebiByUserIdAndQty(Long userId, Integer lebiQty) {
        if (null != userId && null != lebiQty) {
            customerDAO.updateLeBiByUserIdAndQuantity(userId, lebiQty);
        }
    }

    @Override
    @Transactional
    public void unlockCustomerProductCouponByUserIdAndProductCoupons(Long userId, Map<Long, Integer> productCoupon) {
        if (null != userId && !productCoupon.isEmpty()) {
            for (Long index : productCoupon.keySet()) {
                customerDAO.updateProductCouponByUserIdAndGoodsIdAndProductCoupons(userId, index, productCoupon.get(index));
            }
        }
    }

    @Override
    @Transactional
    public void unlockCustomerCashCouponByUserIdAndCashCoupons(Long userId, Map<Long, Integer> cashCoupon) {
        if (null != userId && !cashCoupon.isEmpty()) {
            for (Long index : cashCoupon.keySet()) {
                customerDAO.updateCashCouponByUserIdAndGoodsIdAndCashCoupons(userId, index, cashCoupon.get(index));
            }
        }
    }

    @Override
    public AppCustomer findStoreSellerByCustomerId(Long userId) {
        return this.customerDAO.findStoreSellerByCustomerId(userId);
    }

    @Override
    public CashCouponResponse findCashCouponByCcIdAndUserIdAndQty(Long id, Long userId, Integer qty) {
        if (null != userId && null != id && null != qty) {
            return customerDAO.findCashCouponByCcIdAndUserIdAndQty(id, userId, qty);
        }
        return null;
    }

    @Override
    public List<CashCouponResponse> findCashCouponUseableByCustomerId(Long customerId, Double totalOrderAmount) {
        if (null != customerId && null != totalOrderAmount) {
            return customerDAO.findCashCouponUseableByCustomerId(customerId, totalOrderAmount);
        }
        return null;
    }

    @Override
    public List<ProductCouponResponse> findProductCouponBySellerIdAndCustomerId(Long sellerId, Long cusId) {
        if (null != sellerId && null != cusId) {
            return customerDAO.findProductCouponBySellerIdAndCustomerIdNew(sellerId, cusId);
        }
        return null;
    }

    @Override
    public List<ProductCouponCustomer> findProductCouponCustomerBySellerId(Long userId, String keywords) {
        if (null != userId) {
            return customerDAO.findProductCouponCustomerBySellerId(userId, keywords);
        }
        return null;
    }

    @Override
    public CustomerCashCoupon findCashCouponByCcid(Long id) {
        if (null != id) {
            return customerDAO.findCashCouponByCcid(id);
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer lockCustomerCashCouponById(Long id, String orderNumber) {
        if (null != id) {
            return customerDAO.updateCustomerCashCouponById(id, orderNumber);
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CustomerLeBi findCustomerLebiByCustomerId(Long customerId) {
        if (null != customerId) {
            return customerDAO.findCustomerLebiByCustomerId(customerId);
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addCustomerCashCouponChangeLog(CustomerCashCouponChangeLog log) {
        if (null != log) {
            customerDAO.addCustomerCashCouponChangeLog(log);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addCustomerLeBiVariationLog(CustomerLeBiVariationLog log) {
        if (null != log) {
            customerDAO.addCustomerLeBiVariationLog(log);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addCusPreDepositLog(CusPreDepositLogDO cusPreDepositLogDO) {
        if (null != cusPreDepositLogDO) {
            customerDAO.addCusPreDepositLog(cusPreDepositLogDO);
        }
    }

    @Override
    public Integer findCashCouponAvailQtyByCustomerId(Long userId) {
        if (null != userId) {
            return customerDAO.findCashCouponAvailQtyByCustomerId(userId);
        }
        return null;
    }

    @Override
    public Integer findProductCouponAvailQtyByCustomerId(Long userId) {
        if (null != userId) {
            return customerDAO.findProductCouponAvailQtyByCustomerId(userId);
        }
        return null;
    }

    @Override
    public Integer countSignDaysByCusId(Long cusId, Date startDate, Date endDate) {
        if (null != cusId && null != startDate && null != endDate) {
            return customerDAO.countSignDaysByCusId(cusId, startDate, endDate);
        }
        return null;
    }

    @Override
    public Integer countTotalSignDaysByCusId(Long cusId) {
        if (null != cusId) {
            return customerDAO.countTotalSignDaysByCusId(cusId);
        }
        return null;
    }

    @Override
    public Integer countSignAwardLebiQtyByCusId(Long cusId) {
        if (null != cusId) {
            return customerDAO.countSignAwardLebiQtyByCusId(cusId);
        }
        return null;
    }

    @Override
    public PageInfo<CustomerSignLogBrief> findCustomerSignDetailByCusIdWithPageable(Long cusId, Integer page, Integer size) {
        if (null != cusId) {
            PageHelper.startPage(page, size);
            List<CustomerSignLogBrief> logBriefList = customerDAO.findCustomerSignDetailByCusId(cusId);
            return new PageInfo<>(logBriefList);
        }
        return null;
    }

    @Override
    public void saveSignLog(CusSignLog log) {
        if (null != log) {
            customerDAO.saveSignLog(log);
        }
    }

    @Override
    public List<CustomerCashCoupon> findCashCouponsByCcids(List<Long> cashCouponList) {
        if (null != cashCouponList && cashCouponList.size() > 0) {
            return customerDAO.findCashCouponsByCcids(cashCouponList);
        }
        return null;
    }

    @Override
    public List<CustomerProductCoupon> findProductCouponsByCustomerIdAndGoodsIdAndQty(Long customerIdTemp, Long salesConsultId, Long id, Integer qty) {
        if (null != customerIdTemp && null != id && null != qty) {
            return customerDAO.findProductCouponsByCustomerIdAndSalesConsultIdAndGoodsIdAndQty(customerIdTemp, salesConsultId, id, qty);
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer lockCustomerProductCouponById(Long couponId, String orderNumber) {
        if (null != couponId && null != orderNumber) {
            return customerDAO.updateCustomerProductCouponById(couponId, orderNumber);
        }
        return 0;
    }

    @Override
    public List<CustomerProfession> getCustomerProfessionListByStatus(String status) {
        return customerDAO.getCustomerProfessionListByStatus(status);
    }

    @Override
    public Integer updateDepositByUserIdAndVersion(Long userId, Double customerDeposit, Date version) {
        return customerDAO.updateDepositByUserIdAndVersion(userId, customerDeposit, version);
    }

    @Override
    public List<CustomerCashCoupon> findCashCouponsByids(List<Long> cashCouponList) {
        if (AssertUtil.isNotEmpty(cashCouponList)) {
            return customerDAO.findCashCouponsByids(cashCouponList);
        }
        return null;
    }

    @Override
    public CustomerProfession findCustomerProfessionByTitle(String customerProfession) {
        if (StringUtils.isNotBlank(customerProfession)) {
            return customerDAO.findCustomerProfessionByTitle(customerProfession);
        }
        return null;
    }

    @Override
    public void unbindingCustomerWeChat(Long userId) {
        if (null != userId) {
            customerDAO.unbindingCustomerWeChat(userId);
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public CustomerPreDeposit findByCusId(Long cusId) {
        return this.customerDAO.findByCusId(cusId);
    }

    /**
     * @param
     * @return
     * @throws
     * @title 充值加预存款和日志
     * @descripe
     * @author GenerationRoad
     * @date 2017/11/21
     */
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void preDepositRecharge(PaymentDataDO paymentDataDO, CustomerPreDepositChangeType type) {
        Long userId = paymentDataDO.getUserId();
        Double money = paymentDataDO.getTotalFee();
        CustomerPreDeposit customerPreDeposit = this.customerDAO.findByCusId(userId);
        if (null == customerPreDeposit) {
            customerPreDeposit = new CustomerPreDeposit();
            customerPreDeposit.setBalance(money);
            customerPreDeposit.setCusId(userId);
            customerPreDeposit.setCreateTime(new Date());
            customerPreDeposit.setLastUpdateTime(new Timestamp(System.currentTimeMillis()));
            this.customerDAO.savePreDeposit(customerPreDeposit);
        } else {
            int row = this.customerDAO.updateDepositByUserIdAndLastUpdateTime(userId, money, new Timestamp(System.currentTimeMillis()), customerPreDeposit.getLastUpdateTime());
            if (1 != row) {
                throw new AppConcurrentExcp("账号余额信息过期！");
            }
        }
        CusPreDepositLogDO log = new CusPreDepositLogDO();
        log.setCreateTimeAndChangeMoneyAndType(LocalDateTime.now(), money, type);
        log.setUserIdAndOperatorinfo(userId, userId, paymentDataDO.getAppIdentityType(), "");
        log.setOrderNumber(paymentDataDO.getOutTradeNo());
        log.setMerchantOrderNumber(paymentDataDO.getTradeNo());
        log.setBalance(CountUtil.add(customerPreDeposit.getBalance(), money));
        log.setChangeTypeDesc(type.getDescription());
        this.cusPreDepositLogServiceImpl.save(log);
    }

    public void preDepositWithdraw() {

    }


    @Override
    public List<SupportHotlineResponse> getCustomerSupportHotline(Long userId, Integer identityType) {
        List<SupportHotlineResponse> supportHotlineList = customerDAO.findAllSupportHotline();
        SupportHotlineResponse supportHotlineResponse = new SupportHotlineResponse();
        if (6 == identityType) {
            String sellerMobile = customerDAO.getCustomerSupportHotline(userId);
            if (null != sellerMobile) {
                supportHotlineResponse.setName("导购热线");
                supportHotlineResponse.setSupportHotline(sellerMobile);
                supportHotlineList.add(supportHotlineResponse);
            }
        } else if (3 == identityType || 2 == identityType) {
            AppStore appStore = appStoreService.findStoreByUserIdAndIdentityType(userId, identityType);
            String storeCode = appStore.getStoreCode();
            String sellerMobile = appEmployeeService.getSalesManagerSupportHotline(storeCode);
            if (null != sellerMobile) {
                supportHotlineResponse.setName("销售经理热线");
                supportHotlineResponse.setSupportHotline(sellerMobile);
                supportHotlineList.add(supportHotlineResponse);
            }
        }
        return supportHotlineList;
    }

    @Override
    public List<CustomerProductCoupon> findProductCouponsByGetOrderNumber(String orderNumber) {
        if (StringUtils.isNotBlank(orderNumber)) {
            return customerDAO.findProductCouponsByGetOrderNumber(orderNumber);
        }
        return null;
    }

    @Override
    public CustomerRankInfoResponse findCusRankinfoByCusId(Long cusId) {
        return this.customerDAO.findCusRankinfoByCusId(cusId);
    }

    @Override
    public List<FindCustomerResponse> findCustomerByCusPhone(Long cityId, String mobile) {
        if (null != cityId && null != mobile) {
            return customerDAO.findCustomerByCusPhone(cityId, mobile);
        } else {
            return null;
        }
    }

    @Override
    public List<FindCustomerResponse> findCustomerByCusNameOrPhone(Long storeId, String keywords) {
        if (null != storeId && null != keywords) {
            return customerDAO.findCustomerByCusNameOrPhone(storeId, keywords);
        } else {
            return null;
        }
    }

    @Override
    public void updateCustomerTypeByUserId(Long userId) {
        if (null != userId) {
            customerDAO.updateCustomerTypeByUserId(userId);
        }
    }

    @Override
    public CustomerPreDeposit findCustomerPreDepositByCustomerId(Long cusId) {
        if (null != cusId) {
            return customerDAO.findCustomerPreDepositByCustomerId(cusId);
        }
        return null;
    }

    @Override
    public List<AppCustomer> findAllCustomer() {

        return customerDAO.findAllCustomer();
    }

    @Override
    @Transactional
    public void createCustomerPreDepositAccount(Long cusId) {
        if (null != cusId) {
            CustomerPreDeposit customerPreDeposit = new CustomerPreDeposit();
            customerPreDeposit.setBalance(0D);
            customerPreDeposit.setCreateTime(new Date());
            customerPreDeposit.setCusId(cusId);
            customerDAO.savePreDeposit(customerPreDeposit);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createCustomerLeBiAccount(Long cusId) {
        if (null != cusId) {
            CustomerLeBi customerLeBi = new CustomerLeBi();
            customerLeBi.setQuantity(0);
            customerLeBi.setCreateTime(new Date());
            customerLeBi.setCusId(cusId);
            customerDAO.saveLeBi(customerLeBi);
        }
    }

    @Override
    public void updateCustomerSellerIdStoreIdByCusId(Long cusId, Long storeId, Long salesConsultId, Date date) {
        customerDAO.updateCustomerSellerIdStoreIdByCusId(cusId, storeId, salesConsultId, date);
    }

    @Override
    public void updateCusProductCouponsStatusIsFlaseById(Long id) {
        this.customerDAO.updateCusProductCouponsStatusIsFlaseById(id);
    }

    @Override
    public List<CustomerProductCoupon> findOverdueCustomerProductCoupon() {

        return customerDAO.findOverdueCustomerProductCoupon();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveCustomerProductCouponTransferPreDepositRecord(CustomerProductCouponTransferPreDepositRecord record) {
        if (null != record) {
            customerDAO.saveCustomerProductCouponTransferPreDepositRecord(record);
        }
    }

    @Override
    public Integer findProductCouponAvailQtyByCustomerIdAndGid(Long userId, Long gid, Long sellerId) {
        return this.customerDAO.findProductCouponAvailQtyByCustomerIdAndGid(userId, gid, sellerId);
    }


    @Override
    public Integer findAllNotUsedCoupons(Long userId) {
        return this.customerDAO.findAllNotUsedCoupons(userId);
    }

    @Override
    public Integer findAllUsedCoupons(Long userId) {
        return this.customerDAO.findAllUsedCoupons(userId);
    }

    @Override
    public Integer findAllOverdueCoupons(Long userId) {
        return this.customerDAO.findAllOverdueCoupons(userId);
    }



    @Override
    public List<ProductCouponResponse> findAllNotUsedCouponsDetails(Long userId) {
        if (null != userId) {
            return customerDAO.findAllNotUsedCouponsDetails(userId);
        }
        return null;
    }

    @Override
    public List<ProductCouponResponse> findAllUsedCouponsDetails(Long userId) {
        if (null != userId) {
            return customerDAO.findAllUsedCouponsDetails(userId);
        }
        return null;
    }

    @Override
    public List<ProductCouponResponse> findAllOverdueCouponsDetails(Long userId) {
        if (null != userId) {
            return customerDAO.findAllOverdueCouponsDetails(userId);
        }
        return null;
    }
}

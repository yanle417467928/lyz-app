package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.config.shiro.ShiroUser;
import cn.com.leyizhuang.app.core.constant.*;
import cn.com.leyizhuang.app.core.utils.SmsUtils;
import cn.com.leyizhuang.app.core.utils.order.OrderUtils;
import cn.com.leyizhuang.app.foundation.dao.AppCustomerDAO;
import cn.com.leyizhuang.app.foundation.dao.AppStoreDAO;
import cn.com.leyizhuang.app.foundation.dao.CusPreDepositWithdrawDAO;
import cn.com.leyizhuang.app.foundation.dao.StPreDepositWithdrawDAO;
import cn.com.leyizhuang.app.foundation.pojo.*;
import cn.com.leyizhuang.app.foundation.pojo.city.City;
import cn.com.leyizhuang.app.foundation.pojo.recharge.RechargeOrder;
import cn.com.leyizhuang.app.foundation.pojo.recharge.RechargeReceiptInfo;
import cn.com.leyizhuang.app.foundation.pojo.request.PreDepositWithdrawParam;
import cn.com.leyizhuang.app.foundation.pojo.user.AppCustomer;
import cn.com.leyizhuang.app.foundation.pojo.user.AppEmployee;
import cn.com.leyizhuang.app.foundation.pojo.user.CusPreDepositWithdraw;
import cn.com.leyizhuang.app.foundation.pojo.user.CustomerPreDeposit;
import cn.com.leyizhuang.app.foundation.service.*;
import cn.com.leyizhuang.common.foundation.pojo.SmsAccount;
import cn.com.leyizhuang.common.util.CountUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * 预存款提现
 * Created by panjie on 2018/2/5.
 */
@Service
public class AppPreDepositWithdrawServiceImpl implements AppPreDepositWithdrawService {

    private final Logger logger = LoggerFactory.getLogger(AppPreDepositWithdrawServiceImpl.class);

    @Autowired
    private CusPreDepositWithdrawDAO cusPreDepositWithdrawDAO;

    @Autowired
    private StPreDepositWithdrawDAO stPreDepositWithdrawDAO;

    @Autowired
    private AppCustomerService appCustomerService;

    @Autowired
    private AppEmployeeService appEmployeeService;

    @Resource
    private AppCustomerDAO customerDAO;

    @Resource
    private AppStoreDAO appStoreDAO;

    @Autowired
    private CusPreDepositLogService cusPreDepositLogServiceImpl;

    @Resource
    private StorePreDepositLogService storePreDepositLogService;

    @Resource
    private AppStoreService appStoreService;

    @Resource
    private SmsAccountServiceImpl smsAccountService;

    @Resource
    private CityService cityService;

    @Resource
    private WithdrawService withdrawService;

    @Resource
    private RechargeService rechargeService;

    @Override
    @Transactional
    public String cusSave(PreDepositWithdrawParam param) throws UnsupportedEncodingException {

        // 获取提现人信息
        AppCustomer appCustomer = appCustomerService.findById(param.getId());
        // 城市信息
        City city = cityService.findById(appCustomer.getCityId());

        CusPreDepositWithdraw cusPreDepositWithdraw = new CusPreDepositWithdraw();
        cusPreDepositWithdraw.setApplyNo(OrderUtils.generateWithdrawNumber(appCustomer.getCityId()));
        cusPreDepositWithdraw.setCreateTime(new Date());
        cusPreDepositWithdraw.setApplyCusId(param.getId());
        cusPreDepositWithdraw.setApplyCusName(param.getRealName());
        cusPreDepositWithdraw.setApplyCusPhone(param.getRealPhone());
        cusPreDepositWithdraw.setIdentityType(AppIdentityType.getAppIdentityTypeByValue(param.getIdentityType()));
        cusPreDepositWithdraw.setWithdrawAmount(param.getAmount());
        cusPreDepositWithdraw.setAccountType(param.getAccountType());
        cusPreDepositWithdraw.setAccount(param.getAccount());
        cusPreDepositWithdraw.setWithdrawAmount(param.getAmount());
        cusPreDepositWithdraw.setStatus(PreDepositWithdrawStatus.CHECKING);
        cusPreDepositWithdraw.setCityId(city.getCityId());
        cusPreDepositWithdraw.setCityName(city.getName());

        cusPreDepositWithdrawDAO.save(cusPreDepositWithdraw);

        // 修改顾客预存款
        Long cusID = cusPreDepositWithdraw.getApplyCusId();
        CustomerPreDeposit customerPreDeposit = appCustomerService.findByCusId(cusID);

        // 可现预存款
        Double canWithdrawAmount = customerPreDeposit.getBalance() == null ? 0.00 : customerPreDeposit.getBalance();
        // 需要提现预存款
        Double needWithdrawAmount = cusPreDepositWithdraw.getWithdrawAmount() == null ? 0.00 : cusPreDepositWithdraw.getWithdrawAmount();

        // 扣款 取负数
        Double subBalance = -needWithdrawAmount;
        int row = customerDAO.updateDepositByUserIdAndLastUpdateTime(param.getId(), subBalance, new Timestamp(System.currentTimeMillis()), customerPreDeposit.getLastUpdateTime());
        if (1 != row) {
            throw new RuntimeException("提现申请失败");
        }

        // 记录日志
        CusPreDepositLogDO log = new CusPreDepositLogDO();
        log.setCreateTimeAndChangeMoneyAndType(LocalDateTime.now(), subBalance, CustomerPreDepositChangeType.WITHDRAW);
        log.setUserIdAndOperatorinfo(param.getId(), param.getId(), AppIdentityType.CUSTOMER, "");
        log.setOrderNumber(cusPreDepositWithdraw.getApplyNo());
        log.setMerchantOrderNumber("");
        log.setBalance(CountUtil.add(customerPreDeposit.getBalance(), subBalance));
        log.setChangeTypeDesc("顾客预存款提现");
        this.cusPreDepositLogServiceImpl.save(log);
        //生成提现退款信息
        WithdrawRefundInfo withdrawRefundInfo = new WithdrawRefundInfo();
        withdrawRefundInfo.setCreateTime(new Date());
        if (null != appCustomer.getCityId()) {
            withdrawRefundInfo.setWithdrawNo(cusPreDepositWithdraw.getApplyNo());
            withdrawRefundInfo.setRefundNumber(OrderUtils.getRefundNumber());
        } else {
            throw new RuntimeException("顾客城市信息为空！");
        }
        withdrawRefundInfo.setWithdrawChannel(cusPreDepositWithdraw.getAccountType());
        withdrawRefundInfo.setWithdrawChannelDesc(withdrawRefundInfo.getWithdrawChannel().getDescription());
        withdrawRefundInfo.setWithdrawAccountType(RechargeAccountType.CUS_PREPAY);
        withdrawRefundInfo.setWithdrawAccountTypeDesc(withdrawRefundInfo.getWithdrawAccountType().getDescription());
        withdrawRefundInfo.setWithdrawAmount(cusPreDepositWithdraw.getWithdrawAmount());
        withdrawRefundInfo.setWithdrawSubjectType(PaymentSubjectType.CUSTOMER);
        withdrawRefundInfo.setWithdrawSubjectTypeDesc(withdrawRefundInfo.getWithdrawSubjectType().getDescription());
        withdrawService.saveWithdrawRefundInfo(withdrawRefundInfo);
        return withdrawRefundInfo.getRefundNumber();
    }

    @Override
    @Transactional
    public String stSave(PreDepositWithdrawParam param) {

        AppEmployee appEmployee = appEmployeeService.findById(param.getId());
        // 城市信息
        City city = cityService.findById(appEmployee.getCityId());

        StPreDepositWithdraw stPreDepositWithdraw = new StPreDepositWithdraw();

        stPreDepositWithdraw.setApplyNo(OrderUtils.generateWithdrawNumber(appEmployee.getCityId()));
        stPreDepositWithdraw.setCreateTime(new Date());
        stPreDepositWithdraw.setApplyStId(param.getId());
        stPreDepositWithdraw.setApplyStName(param.getRealName());
        stPreDepositWithdraw.setApplyStPhone(param.getRealPhone());
        stPreDepositWithdraw.setIdentityType(AppIdentityType.getAppIdentityTypeByValue(param.getIdentityType()));
        stPreDepositWithdraw.setAccountType(param.getAccountType());
        stPreDepositWithdraw.setAccount(param.getAccount());
        stPreDepositWithdraw.setWithdrawAmount(param.getAmount());
        stPreDepositWithdraw.setCityId(city.getCityId());
        stPreDepositWithdraw.setCityName(city.getName());
        stPreDepositWithdraw.setStatus(PreDepositWithdrawStatus.CHECKING);

        stPreDepositWithdrawDAO.save(stPreDepositWithdraw);

        // 修改门店预存款
        StorePreDeposit preDeposit = appStoreService.findStorePreDepositByEmpId(param.getId());
        // 可现预存款
        Double canWithdrawAmount = preDeposit.getBalance() == null ? 0.00 : preDeposit.getBalance();
        // 需要提现预存款
        Double needWithdrawAmount = param.getAmount() == null ? 0.00 : param.getAmount();

        // 扣款
        Double subBalance = needWithdrawAmount;
        int row = appStoreDAO.updateStoreDepositByUserIdAndStoreDeposit(param.getId(), subBalance, preDeposit.getLastUpdateTime());
        if (1 != row) {
            throw new RuntimeException("提现申请失败");
        }

        // 记录日志
        StPreDepositLogDO log = new StPreDepositLogDO();
        log.setCreateTimeAndChangeMoneyAndType(LocalDateTime.now(), -subBalance, StorePreDepositChangeType.WITHDRAW);
        log.setUserIdAndOperatorinfo(param.getId(), param.getId(), AppIdentityType.SELLER, "");
        log.setOrderNumber(stPreDepositWithdraw.getApplyNo());
        log.setMerchantOrderNumber("");
        log.setBalance(CountUtil.add(preDeposit.getBalance(), -subBalance));
        log.setChangeTypeDesc("门店预存款提现");
        this.storePreDepositLogService.save(log);

        //生成提现退款信息
        WithdrawRefundInfo withdrawRefundInfo = new WithdrawRefundInfo();
        withdrawRefundInfo.setCreateTime(new Date());
        if (null != appEmployee.getCityId()) {
            withdrawRefundInfo.setWithdrawNo(stPreDepositWithdraw.getApplyNo());
            withdrawRefundInfo.setRefundNumber(OrderUtils.getRefundNumber());
        } else {
            throw new RuntimeException("顾客城市信息为空！");
        }
        withdrawRefundInfo.setWithdrawChannel(stPreDepositWithdraw.getAccountType());
        withdrawRefundInfo.setWithdrawChannelDesc(withdrawRefundInfo.getWithdrawChannel().getDescription());
        withdrawRefundInfo.setWithdrawAccountType(RechargeAccountType.ST_PREPAY);
        withdrawRefundInfo.setWithdrawAccountTypeDesc(withdrawRefundInfo.getWithdrawAccountType().getDescription());
        withdrawRefundInfo.setWithdrawAmount(stPreDepositWithdraw.getWithdrawAmount());
        withdrawRefundInfo.setWithdrawSubjectType(PaymentSubjectType.STORE);
        withdrawRefundInfo.setWithdrawSubjectTypeDesc(withdrawRefundInfo.getWithdrawSubjectType().getDescription());
        withdrawService.saveWithdrawRefundInfo(withdrawRefundInfo);
        return withdrawRefundInfo.getRefundNumber();
    }

    @Override
    public PageInfo<CusPreDepositWithdraw> cusApplyList(Integer page, Integer size, Long cusId, Integer status) {

        PageHelper.startPage(page, size);

        PreDepositWithdrawStatus preDepositWithdrawStatus;

        if (status == null) {
            preDepositWithdrawStatus = null;
        } else {
            preDepositWithdrawStatus = PreDepositWithdrawStatus.getPreDepositWithdrawStatusByValue(status);
        }

        List<CusPreDepositWithdraw> cusPreDepositWithdraws = cusPreDepositWithdrawDAO.findByCusId(cusId, preDepositWithdrawStatus);
        return new PageInfo<>(cusPreDepositWithdraws);
    }

    @Override
    public PageInfo<StPreDepositWithdraw> stApplyList(Integer page, Integer size, Long stId, Integer status) {
        PageHelper.startPage(page, size);

        PreDepositWithdrawStatus preDepositWithdrawStatus;

        if (status == null) {
            preDepositWithdrawStatus = null;
        } else {
            preDepositWithdrawStatus = PreDepositWithdrawStatus.getPreDepositWithdrawStatusByValue(status);
        }
        List<StPreDepositWithdraw> stPreDepositWithdraws = stPreDepositWithdrawDAO.findByStId(stId, preDepositWithdrawStatus);
        return new PageInfo<>(stPreDepositWithdraws);
    }

    @Override
    @Transactional
    public String cusCancelApply(Long applyId, Long cusId) {

        CusPreDepositWithdraw cusPreDepositWithdraw = cusPreDepositWithdrawDAO.findById(applyId);


        if (cusPreDepositWithdraw != null && cusPreDepositWithdraw.getStatus().equals(PreDepositWithdrawStatus.CHECKING)) {
            cusPreDepositWithdraw.setStatus(PreDepositWithdrawStatus.CANCEL);

            cusPreDepositWithdrawDAO.update(cusPreDepositWithdraw);

            // 修改顾客预存款
            Long cusID = cusPreDepositWithdraw.getApplyCusId();
            CustomerPreDeposit customerPreDeposit = appCustomerService.findByCusId(cusID);

            // 可现预存款
            Double canWithdrawAmount = customerPreDeposit.getBalance() == null ? 0.00 : customerPreDeposit.getBalance();
            // 需要恢复预存款
            Double needWithdrawAmount = cusPreDepositWithdraw.getWithdrawAmount() == null ? 0.00 : cusPreDepositWithdraw.getWithdrawAmount();

            Double subBalance = needWithdrawAmount;
            int row = customerDAO.updateDepositByUserIdAndLastUpdateTime(cusId, subBalance, new Timestamp(System.currentTimeMillis()), customerPreDeposit.getLastUpdateTime());
            if (1 != row) {
                throw new RuntimeException("取消申请失败");
            }

            // 记录日志
            CusPreDepositLogDO log = new CusPreDepositLogDO();
            log.setCreateTimeAndChangeMoneyAndType(LocalDateTime.now(), subBalance, CustomerPreDepositChangeType.RETURN_WITHDRAW);
            log.setUserIdAndOperatorinfo(cusId, cusId, AppIdentityType.CUSTOMER, "");
            log.setOrderNumber(cusPreDepositWithdraw.getApplyNo());
            log.setMerchantOrderNumber("");
            log.setBalance(CountUtil.add(customerPreDeposit.getBalance(), subBalance));
            log.setChangeTypeDesc("顾客预存款提现取消");
            this.cusPreDepositLogServiceImpl.save(log);


            //生成充值单
            RechargeOrder rechargeOrder = new RechargeOrder();
            rechargeOrder.setStatus(AppRechargeOrderStatus.PAID);
            rechargeOrder.setRechargeNo(OrderUtils.generateRechargeNumber(cusPreDepositWithdraw.getCityId()));
            rechargeOrder.setAmount(cusPreDepositWithdraw.getWithdrawAmount());
            rechargeOrder.setPaymentSubjectType(PaymentSubjectType.CUSTOMER);
            rechargeOrder.setPaymentSubjectTypeDesc(rechargeOrder.getPaymentSubjectType().getDescription());
            rechargeOrder.setPayType(OrderBillingPaymentType.ALIPAY);
            rechargeOrder.setPayTypeDesc(rechargeOrder.getPayType().getDescription());
            rechargeOrder.setCustomerId(cusPreDepositWithdraw.getApplyCusId());
            AppStore store = appStoreService.findStoreByUserIdAndIdentityType(cusPreDepositWithdraw.getApplyCusId(), AppIdentityType.CUSTOMER.getValue());
            if (null != store) {
                rechargeOrder.setStoreId(store.getStoreId());
            }
            rechargeOrder.setRechargeAccountType(RechargeAccountType.CUS_PREPAY);
            rechargeOrder.setRechargeAccountTypeDesc(rechargeOrder.getRechargeAccountType().getDescription());
            rechargeOrder.setCreatorId(rechargeOrder.getCustomerId());
            rechargeOrder.setCreatorIdentityType(AppIdentityType.CUSTOMER);
            rechargeOrder.setCreateTime(new Date());
            rechargeOrder.setPayUpTime(new Date());
            rechargeOrder.setWithdrawNo(cusPreDepositWithdraw.getApplyNo());
            rechargeService.saveRechargeOrder(rechargeOrder);

            //生成充值收款款
            RechargeReceiptInfo receiptInfo = new RechargeReceiptInfo();
            receiptInfo.setCreateTime(new Date());
            receiptInfo.setPayTime(new Date());
            receiptInfo.setAmount(cusPreDepositWithdraw.getWithdrawAmount());
            receiptInfo.setPaymentSubjectType(PaymentSubjectType.CUSTOMER);
            receiptInfo.setRechargeAccountType(RechargeAccountType.CUS_PREPAY);
            receiptInfo.setPaymentSubjectTypeDesc(receiptInfo.getPaymentSubjectType().getDescription());
            receiptInfo.setRechargeAccountTypeDesc(receiptInfo.getRechargeAccountType().getDescription());
            receiptInfo.setPayType(OrderBillingPaymentType.ALIPAY);
            receiptInfo.setRechargeNo(rechargeOrder.getRechargeNo());
            receiptInfo.setPayTypeDesc(receiptInfo.getPayType().getDescription());
            receiptInfo.setWithdrawNo(cusPreDepositWithdraw.getApplyNo());
            receiptInfo.setReceiptNumber(OrderUtils.generateReceiptNumber(cusPreDepositWithdraw.getCityId()));
            rechargeService.saveRechargeReceiptInfo(receiptInfo);
            return receiptInfo.getRechargeNo();
        } else {
            throw new RuntimeException("取消申请失败");
        }
    }

    @Override
    @Transactional
    public String stCancelApply(Long applyId, Long stId) {
        StPreDepositWithdraw stPreDepositWithdraw = this.stPreDepositWithdrawDAO.findById(applyId);
        if (stPreDepositWithdraw != null && stPreDepositWithdraw.getStatus().equals(PreDepositWithdrawStatus.CHECKING)) {
            stPreDepositWithdraw.setStatus(PreDepositWithdrawStatus.CANCEL);

            this.stPreDepositWithdrawDAO.update(stPreDepositWithdraw);
            // 修改门店预存款
            StorePreDeposit preDeposit = appStoreService.findStorePreDepositByEmpId(stId);
            // 可现预存款
            Double canWithdrawAmount = preDeposit.getBalance() == null ? 0.00 : preDeposit.getBalance();
            // 需要提现预存款
            Double needWithdrawAmount = stPreDepositWithdraw.getWithdrawAmount() == null ? 0.00 : stPreDepositWithdraw.getWithdrawAmount();

            // 扣款
            Double subBalance = needWithdrawAmount;
            int row = appStoreDAO.updateStoreDepositByUserIdAndStoreDeposit(stId, -subBalance, preDeposit.getLastUpdateTime());
            if (1 != row) {
                throw new RuntimeException("提现申请失败");
            }

            // 记录日志
            StPreDepositLogDO log = new StPreDepositLogDO();
            log.setCreateTimeAndChangeMoneyAndType(LocalDateTime.now(), subBalance, StorePreDepositChangeType.RETURN_WITHDRAW);
            log.setUserIdAndOperatorinfo(stId, stId, AppIdentityType.SELLER, "");
            log.setOrderNumber(stPreDepositWithdraw.getApplyNo());
            log.setMerchantOrderNumber("");
            log.setBalance(CountUtil.add(preDeposit.getBalance(), subBalance));
            log.setChangeTypeDesc("门店预存款提现取消");
            this.storePreDepositLogService.save(log);

            //生成充值单
            RechargeOrder rechargeOrder = new RechargeOrder();
            rechargeOrder.setStatus(AppRechargeOrderStatus.PAID);
            rechargeOrder.setRechargeNo(OrderUtils.generateRechargeNumber(stPreDepositWithdraw.getCityId()));
            rechargeOrder.setAmount(stPreDepositWithdraw.getWithdrawAmount());
            rechargeOrder.setPaymentSubjectType(PaymentSubjectType.SELLER);
            rechargeOrder.setPaymentSubjectTypeDesc(rechargeOrder.getPaymentSubjectType().getDescription());
            rechargeOrder.setPayType(OrderBillingPaymentType.ALIPAY);
            rechargeOrder.setPayTypeDesc(rechargeOrder.getPayType().getDescription());
            rechargeOrder.setStoreId(preDeposit.getStoreId());
            rechargeOrder.setRechargeAccountType(RechargeAccountType.ST_PREPAY);
            rechargeOrder.setRechargeAccountTypeDesc(rechargeOrder.getRechargeAccountType().getDescription());
            rechargeOrder.setCreatorId(stPreDepositWithdraw.getApplyStId());
            rechargeOrder.setCreatorIdentityType(AppIdentityType.SELLER);
            rechargeOrder.setCreateTime(new Date());
            rechargeOrder.setPayUpTime(new Date());
            rechargeOrder.setWithdrawNo(stPreDepositWithdraw.getApplyNo());
            rechargeService.saveRechargeOrder(rechargeOrder);

            RechargeReceiptInfo receiptInfo = new RechargeReceiptInfo();
            receiptInfo.setCreateTime(new Date());
            receiptInfo.setPayTime(new Date());
            receiptInfo.setAmount(stPreDepositWithdraw.getWithdrawAmount());
            receiptInfo.setPaymentSubjectType(PaymentSubjectType.STORE);
            receiptInfo.setRechargeAccountType(RechargeAccountType.ST_PREPAY);
            receiptInfo.setPaymentSubjectTypeDesc(receiptInfo.getPaymentSubjectType().getDescription());
            receiptInfo.setRechargeAccountTypeDesc(receiptInfo.getRechargeAccountType().getDescription());
            receiptInfo.setPayType(OrderBillingPaymentType.ALIPAY);
            receiptInfo.setPayTypeDesc(receiptInfo.getPayType().getDescription());
            receiptInfo.setRechargeNo(rechargeOrder.getRechargeNo());
            receiptInfo.setWithdrawNo(stPreDepositWithdraw.getApplyNo());
            receiptInfo.setReceiptNumber(OrderUtils.generateReceiptNumber(stPreDepositWithdraw.getCityId()));
            rechargeService.saveRechargeReceiptInfo(receiptInfo);
            return receiptInfo.getRechargeNo();
        } else {
            throw new RuntimeException("取消提现申请失败");
        }
    }


    public void remitStApply(String applyNo, ShiroUser shiroUser) {

        // 申请单
        StPreDepositWithdraw stPreDepositWithdraw = stPreDepositWithdrawDAO.findByApplyNo(applyNo);

        if (stPreDepositWithdraw == null) {
            throw new RuntimeException("申请单不存在");
        }

        // 更新状态
        stPreDepositWithdraw.setStatus(PreDepositWithdrawStatus.REMITED);
        stPreDepositWithdraw.setCheckId(shiroUser.getId());
        stPreDepositWithdraw.setCheckCode(shiroUser.getName());
        stPreDepositWithdraw.setCheckName(shiroUser.getLoginName());


        // TODO 短信通知

    }

    private String createCode() {
        String code = "TX";

//        if (cityCode.equals("2121")){
//            code = "CD";
//        }else if (cityCode.equals("2033")){
//            code = "ZZ";
//        }else if (cityCode.equals("2044")){
//            code = "CQ";
//        }

        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMMddhhmmssSSS");
        String now = LocalDateTime.now().format(format);
        Random random = new Random();
        String suiji = random.nextInt(900) + 100 + "";
        return code + "_" + now + suiji;
    }

    /********************************  后台方法 ********************************************/

    @Override
    public PageInfo<CusPreDepositWithdraw> getCusPageInfo(Integer page, Integer size, String keywords, String status, String startDateTime, String endDateTime) {
        PageHelper.startPage(page, size);

        if (keywords.equals("")) {
            keywords = null;
        }

        if (status.equals("")) {
            status = null;
        }

        List<CusPreDepositWithdraw> cusPreDepositWithdrawList = cusPreDepositWithdrawDAO.findByKeywords(keywords, status, startDateTime, endDateTime);
        return new PageInfo<>(cusPreDepositWithdrawList);
    }

    @Override
    public PageInfo<StPreDepositWithdraw> getStPageInfo(Integer page, Integer size, String keywords, String status, String startDateTime, String endDateTime) {
        PageHelper.startPage(page, size);

        if (keywords.equals("")) {
            keywords = null;
        }

        if (status.equals("")) {
            status = null;
        }

        List<StPreDepositWithdraw> stPreDepositWithdraws = stPreDepositWithdrawDAO.findByKeywords(keywords, status, startDateTime, endDateTime);

        return new PageInfo<>(stPreDepositWithdraws);
    }

    /**
     * 顾客 -- 通过
     *
     * @param applyId
     * @param shiroUser
     * @throws Exception
     */
    @Override
    public void cusApplyPass(Long applyId, ShiroUser shiroUser) throws Exception {

        CusPreDepositWithdraw apply = cusPreDepositWithdrawDAO.findById(applyId);
        if (apply != null) {

            this.checkCusApply(apply, shiroUser, PreDepositWithdrawStatus.CHECKPASS);
        } else {
            throw new Exception("预存款提现，申请单不存在！");
        }
    }

    /**
     * 顾客 -- 驳回
     *
     * @param applyId
     * @param shiroUser
     * @throws Exception
     */
    @Override
    public String cusApplyreject(Long applyId, ShiroUser shiroUser) throws Exception {

        CusPreDepositWithdraw apply = cusPreDepositWithdrawDAO.findById(applyId);
        if (apply != null) {
            if (apply.getStatus().equals(PreDepositWithdrawStatus.CHECKING)) {
                // dai审核状态的单子才可以驳回
                this.checkCusApply(apply, shiroUser, PreDepositWithdrawStatus.CHECKRETURN);

                //生成充值单
                RechargeOrder rechargeOrder = new RechargeOrder();
                rechargeOrder.setStatus(AppRechargeOrderStatus.PAID);
                rechargeOrder.setRechargeNo(OrderUtils.generateRechargeNumber(apply.getCityId()));
                rechargeOrder.setAmount(apply.getWithdrawAmount());
                rechargeOrder.setPaymentSubjectType(PaymentSubjectType.CUSTOMER);
                rechargeOrder.setPaymentSubjectTypeDesc(rechargeOrder.getPaymentSubjectType().getDescription());
                rechargeOrder.setPayType(OrderBillingPaymentType.ALIPAY);
                rechargeOrder.setPayTypeDesc(rechargeOrder.getPayType().getDescription());
                rechargeOrder.setCustomerId(apply.getApplyCusId());
                AppStore store = appStoreService.findStoreByUserIdAndIdentityType(apply.getApplyCusId(), AppIdentityType.CUSTOMER.getValue());
                if (null != store) {
                    rechargeOrder.setStoreId(store.getStoreId());
                }
                rechargeOrder.setRechargeAccountType(RechargeAccountType.CUS_PREPAY);
                rechargeOrder.setRechargeAccountTypeDesc(rechargeOrder.getRechargeAccountType().getDescription());
                rechargeOrder.setCreatorId(rechargeOrder.getCustomerId());
                rechargeOrder.setCreatorIdentityType(AppIdentityType.CUSTOMER);
                rechargeOrder.setCreateTime(new Date());
                rechargeOrder.setPayUpTime(new Date());
                rechargeOrder.setWithdrawNo(apply.getApplyNo());
                rechargeService.saveRechargeOrder(rechargeOrder);

                //生成充值收款款
                RechargeReceiptInfo receiptInfo = new RechargeReceiptInfo();
                receiptInfo.setCreateTime(new Date());
                receiptInfo.setPayTime(new Date());
                receiptInfo.setAmount(apply.getWithdrawAmount());
                receiptInfo.setPaymentSubjectType(PaymentSubjectType.CUSTOMER);
                receiptInfo.setRechargeAccountType(RechargeAccountType.CUS_PREPAY);
                receiptInfo.setPaymentSubjectTypeDesc(receiptInfo.getPaymentSubjectType().getDescription());
                receiptInfo.setRechargeAccountTypeDesc(receiptInfo.getRechargeAccountType().getDescription());
                receiptInfo.setPayType(OrderBillingPaymentType.ALIPAY);
                receiptInfo.setRechargeNo(rechargeOrder.getRechargeNo());
                receiptInfo.setPayTypeDesc(receiptInfo.getPayType().getDescription());
                receiptInfo.setWithdrawNo(apply.getApplyNo());
                receiptInfo.setReceiptNumber(OrderUtils.generateReceiptNumber(apply.getCityId()));
                rechargeService.saveRechargeReceiptInfo(receiptInfo);
                return receiptInfo.getRechargeNo();
            }
        } else {
            throw new Exception("预存款提现，申请单不存在！");
        }
        return null;
    }

    /**
     * 打款
     *
     * @param applyId
     * @param shiroUser
     * @throws Exception
     */
    @Override
    public void cusApplyRemit(Long applyId, ShiroUser shiroUser) throws Exception {

        CusPreDepositWithdraw apply = cusPreDepositWithdrawDAO.findById(applyId);
        if (apply != null) {

            this.checkCusApply(apply, shiroUser, PreDepositWithdrawStatus.REMITED);
        } else {
            throw new Exception("预存款提现，申请单不存在！");
        }
    }

    /**
     * 审核顾客预存款提现申请单
     *
     * @param cusPreDepositWithdraw
     */
    @Transactional
    public void checkCusApply(CusPreDepositWithdraw cusPreDepositWithdraw, ShiroUser shiroUser, PreDepositWithdrawStatus status) {

        if (cusPreDepositWithdraw == null) {
            throw new RuntimeException("申请单不存在");
        }

        cusPreDepositWithdraw.setStatus(status);
        cusPreDepositWithdraw.setCheckId(shiroUser.getId());
        cusPreDepositWithdraw.setCheckName(shiroUser.getName());
        cusPreDepositWithdraw.setCheckCode(shiroUser.getLoginName());

        cusPreDepositWithdrawDAO.update(cusPreDepositWithdraw);

        if (status.equals(PreDepositWithdrawStatus.CHECKRETURN)) {
            // 申请退回
            CustomerPreDeposit customerPreDeposit = appCustomerService.findByCusId(cusPreDepositWithdraw.getApplyCusId());
            //退回预存款
            int row = customerDAO.updateDepositByUserIdAndLastUpdateTime(cusPreDepositWithdraw.getApplyCusId(), cusPreDepositWithdraw.getWithdrawAmount(), new Timestamp(System.currentTimeMillis()), customerPreDeposit.getLastUpdateTime());
            if (1 != row) {
                throw new RuntimeException("提现申请失败");
            }

            // 记录日志
            CusPreDepositLogDO log = new CusPreDepositLogDO();
            log.setCreateTimeAndChangeMoneyAndType(LocalDateTime.now(), cusPreDepositWithdraw.getWithdrawAmount(), CustomerPreDepositChangeType.RETURN_WITHDRAW);
            log.setUserIdAndOperatorinfo(cusPreDepositWithdraw.getId(), cusPreDepositWithdraw.getId(), AppIdentityType.CUSTOMER, "");
            log.setOrderNumber(cusPreDepositWithdraw.getApplyNo());
            log.setMerchantOrderNumber("");
            log.setBalance(CountUtil.add(customerPreDeposit.getBalance(), cusPreDepositWithdraw.getWithdrawAmount()));
            log.setChangeTypeDesc("顾客预存款提现退回");
            this.cusPreDepositLogServiceImpl.save(log);

            // TODO 调预存款退款接口

            //  发送短信
            this.sendSms(cusPreDepositWithdraw.getApplyCusPhone(), cusPreDepositWithdraw.getApplyNo(), cusPreDepositWithdraw.getStatus());

        } else if (status.equals(PreDepositWithdrawStatus.REMITED)) {
            // 打款

            //  短信通知
            this.sendSms(cusPreDepositWithdraw.getApplyCusPhone(), cusPreDepositWithdraw.getApplyNo(), cusPreDepositWithdraw.getStatus());
        }
    }


    /**
     * 门店 -- 通过
     *
     * @param applyId
     * @param shiroUser
     * @throws Exception
     */
    @Override
    public void stApplyPass(Long applyId, ShiroUser shiroUser) throws Exception {

        StPreDepositWithdraw apply = stPreDepositWithdrawDAO.findById(applyId);
        if (apply != null) {

            this.checkStApply(apply, shiroUser, PreDepositWithdrawStatus.CHECKPASS);
        } else {
            throw new Exception("预存款提现，申请单不存在！");
        }
    }

    /**
     * 门店 -- 驳回
     *
     * @param applyId
     * @param shiroUser
     * @throws Exception
     */
    @Override
    public String stApplyreject(Long applyId, ShiroUser shiroUser) throws Exception {

        StPreDepositWithdraw apply = stPreDepositWithdrawDAO.findById(applyId);
        if (apply != null) {
            if (apply.getStatus().equals(PreDepositWithdrawStatus.CHECKING)) {
                // dai审核状态的单子才可以驳回
                this.checkStApply(apply, shiroUser, PreDepositWithdrawStatus.CHECKRETURN);

                //生成充值单
                RechargeOrder rechargeOrder = new RechargeOrder();
                rechargeOrder.setStatus(AppRechargeOrderStatus.PAID);
                rechargeOrder.setRechargeNo(OrderUtils.generateRechargeNumber(apply.getCityId()));
                rechargeOrder.setAmount(apply.getWithdrawAmount());
                rechargeOrder.setPaymentSubjectType(PaymentSubjectType.SELLER);
                rechargeOrder.setPaymentSubjectTypeDesc(rechargeOrder.getPaymentSubjectType().getDescription());
                rechargeOrder.setPayType(OrderBillingPaymentType.ALIPAY);
                rechargeOrder.setPayTypeDesc(rechargeOrder.getPayType().getDescription());
                AppStore store = appStoreService.findStoreByUserIdAndIdentityType(apply.getApplyStId(), AppIdentityType.SELLER.getValue());
                rechargeOrder.setStoreId(store.getStoreId());
                rechargeOrder.setRechargeAccountType(RechargeAccountType.ST_PREPAY);
                rechargeOrder.setRechargeAccountTypeDesc(rechargeOrder.getRechargeAccountType().getDescription());
                rechargeOrder.setCreatorId(apply.getApplyStId());
                rechargeOrder.setCreatorIdentityType(AppIdentityType.SELLER);
                rechargeOrder.setCreateTime(new Date());
                rechargeOrder.setPayUpTime(new Date());
                rechargeOrder.setWithdrawNo(apply.getApplyNo());
                rechargeService.saveRechargeOrder(rechargeOrder);

                RechargeReceiptInfo receiptInfo = new RechargeReceiptInfo();
                receiptInfo.setCreateTime(new Date());
                receiptInfo.setPayTime(new Date());
                receiptInfo.setAmount(apply.getWithdrawAmount());
                receiptInfo.setPaymentSubjectType(PaymentSubjectType.STORE);
                receiptInfo.setRechargeAccountType(RechargeAccountType.ST_PREPAY);
                receiptInfo.setPaymentSubjectTypeDesc(receiptInfo.getPaymentSubjectType().getDescription());
                receiptInfo.setRechargeAccountTypeDesc(receiptInfo.getRechargeAccountType().getDescription());
                receiptInfo.setPayType(OrderBillingPaymentType.ALIPAY);
                receiptInfo.setPayTypeDesc(receiptInfo.getPayType().getDescription());
                receiptInfo.setRechargeNo(rechargeOrder.getRechargeNo());
                receiptInfo.setWithdrawNo(apply.getApplyNo());
                receiptInfo.setReceiptNumber(OrderUtils.generateReceiptNumber(apply.getCityId()));
                rechargeService.saveRechargeReceiptInfo(receiptInfo);
                return receiptInfo.getRechargeNo();
            }
        } else {
            throw new Exception("预存款提现，申请单不存在！");
        }
        return null;
    }

    /**
     * 门店 -- 打款
     *
     * @param applyId
     * @param shiroUser
     * @throws Exception
     */
    @Override
    public void stApplyRemit(Long applyId, ShiroUser shiroUser) throws Exception {

        StPreDepositWithdraw apply = stPreDepositWithdrawDAO.findById(applyId);
        if (apply != null) {

            this.checkStApply(apply, shiroUser, PreDepositWithdrawStatus.REMITED);
        } else {
            throw new Exception("预存款提现，申请单不存在！");
        }
    }

    /**
     * 审核门店预存款提现申请单
     *
     * @param stPreDepositWithdraw
     * @param shiroUser
     * @param status
     */
    public void checkStApply(StPreDepositWithdraw stPreDepositWithdraw, ShiroUser shiroUser, PreDepositWithdrawStatus status) {

        if (stPreDepositWithdraw == null) {
            throw new RuntimeException("申请单不存在");
        }

        stPreDepositWithdraw.setStatus(status);
        stPreDepositWithdraw.setCheckId(shiroUser.getId());
        stPreDepositWithdraw.setCheckName(shiroUser.getName());
        stPreDepositWithdraw.setCheckCode(shiroUser.getLoginName());

        stPreDepositWithdrawDAO.update(stPreDepositWithdraw);

        if (status.equals(PreDepositWithdrawStatus.CHECKRETURN)) {
            // 申请退回
            StorePreDeposit preDeposit = appStoreService.findStorePreDepositByEmpId(stPreDepositWithdraw.getApplyStId());
            int row = appStoreDAO.updateStoreDepositByUserIdAndStoreDeposit(stPreDepositWithdraw.getApplyStId(), -stPreDepositWithdraw.getWithdrawAmount(), preDeposit.getLastUpdateTime());
            if (1 != row) {
                throw new RuntimeException("提现申请失败");
            }

            // 记录日志
            StPreDepositLogDO log = new StPreDepositLogDO();
            log.setCreateTimeAndChangeMoneyAndType(LocalDateTime.now(), -stPreDepositWithdraw.getWithdrawAmount(), StorePreDepositChangeType.RETURN_WITHDRAW);
            log.setUserIdAndOperatorinfo(stPreDepositWithdraw.getId(), stPreDepositWithdraw.getId(), AppIdentityType.SELLER, "");
            log.setOrderNumber(stPreDepositWithdraw.getApplyNo());
            log.setMerchantOrderNumber("");
            log.setBalance(CountUtil.add(preDeposit.getBalance(), stPreDepositWithdraw.getWithdrawAmount()));
            log.setChangeTypeDesc("门店预存款提现退回");
            this.storePreDepositLogService.save(log);

            // TODO 调预存款退款接口

            //  发送短信
            this.sendSms(stPreDepositWithdraw.getApplyStPhone(), stPreDepositWithdraw.getApplyNo(), stPreDepositWithdraw.getStatus());

        } else if (status.equals(PreDepositWithdrawStatus.REMITED)) {
            // 打款

            //  短信通知
            this.sendSms(stPreDepositWithdraw.getApplyStPhone(), stPreDepositWithdraw.getApplyNo(), stPreDepositWithdraw.getStatus());
        }
    }

    public void sendSms(String phone, String applyNo, PreDepositWithdrawStatus status) {

        String msg = "您的预存款提现申请:" + status.getDescription() + ", 单号：" + applyNo;
        String content = null;

        try {
            content = URLEncoder.encode(msg, "GB2312");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            logger.info("预存款提现审核 短信发送失败，消息内容转码失败！");
        }

        SmsAccount account = smsAccountService.findOne();
        String returnCode = null;
        try {
            returnCode = SmsUtils.sendMessageQrCode(account.getEncode(), account.getEnpass(), account.getUserName(), phone, content);
        } catch (IOException e) {
            logger.info("预存款提现审核 短信发送失败");
            logger.warn("{}", e);
        } catch (Exception e) {
            logger.info("预存款提现审核 短信发送失败");
            logger.warn("{}", e);
        }
        if ("00".equalsIgnoreCase(returnCode)) {

        } else {
            logger.info("预存款提现审核 短信发送失败");
        }


    }


}

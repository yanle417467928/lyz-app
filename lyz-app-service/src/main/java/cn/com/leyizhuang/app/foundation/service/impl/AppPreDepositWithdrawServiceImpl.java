package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.config.shiro.ShiroUser;
import cn.com.leyizhuang.app.core.constant.*;
import cn.com.leyizhuang.app.core.utils.order.OrderUtils;
import cn.com.leyizhuang.app.foundation.dao.AppCustomerDAO;
import cn.com.leyizhuang.app.foundation.dao.AppStoreDAO;
import cn.com.leyizhuang.app.foundation.dao.CusPreDepositWithdrawDAO;
import cn.com.leyizhuang.app.foundation.dao.StPreDepositWithdrawDAO;
import cn.com.leyizhuang.app.foundation.pojo.*;
import cn.com.leyizhuang.app.foundation.pojo.request.PreDepositWithdrawParam;
import cn.com.leyizhuang.app.foundation.pojo.user.AppCustomer;
import cn.com.leyizhuang.app.foundation.pojo.user.CusPreDepositWithdraw;
import cn.com.leyizhuang.app.foundation.pojo.user.CustomerPreDeposit;
import cn.com.leyizhuang.app.foundation.service.*;
import cn.com.leyizhuang.common.util.CountUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
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

    @Autowired
    private CusPreDepositWithdrawDAO cusPreDepositWithdrawDAO;

    @Autowired
    private StPreDepositWithdrawDAO stPreDepositWithdrawDAO;

    @Autowired
    private AppCustomerService appCustomerService;

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
    private WithdrawService withdrawService;

    @Resource

    @Override
    @Transactional
    public String cusSave(PreDepositWithdrawParam param) throws UnsupportedEncodingException {

        // 获取提现人信息
        AppCustomer appCustomer = appCustomerService.findById(param.getId());

        CusPreDepositWithdraw cusPreDepositWithdraw = new CusPreDepositWithdraw();
        cusPreDepositWithdraw.setApplyNo(OrderUtils.generateWithdrawNumber(appCustomer.getCityId()));
        cusPreDepositWithdraw.setCreateTime(new Date());
        cusPreDepositWithdraw.setApplyCusId(param.getId());
        cusPreDepositWithdraw.setApplyCusName(param.getRealName());
        cusPreDepositWithdraw.setApplyCusPhone(param.getRealPhone());
        cusPreDepositWithdraw.setWithdrawAmount(param.getAmount());
        cusPreDepositWithdraw.setAccountType(param.getAccountType());
        cusPreDepositWithdraw.setAccount(param.getAccount());
        cusPreDepositWithdraw.setWithdrawAmount(param.getAmount());
        cusPreDepositWithdraw.setStatus(PreDepositWithdrawStatus.CHECKING);

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
    public void stSave(PreDepositWithdrawParam param) {

        StPreDepositWithdraw stPreDepositWithdraw = new StPreDepositWithdraw();

        stPreDepositWithdraw.setApplyNo(this.createCode());
        stPreDepositWithdraw.setCreateTime(new Date());
        stPreDepositWithdraw.setApplyStId(param.getId());
        stPreDepositWithdraw.setApplyStName(param.getRealName());
        stPreDepositWithdraw.setApplyStPhone(param.getRealPhone());
        stPreDepositWithdraw.setAccountType(param.getAccountType());
        stPreDepositWithdraw.setAccount(param.getAccount());
        stPreDepositWithdraw.setWithdrawAmount(param.getAmount());
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
    }

    @Override
    public PageInfo<CusPreDepositWithdraw> cusApplyList(Integer page, Integer size, Long cusId) {

        PageHelper.startPage(page, size);
        List<CusPreDepositWithdraw> cusPreDepositWithdraws = cusPreDepositWithdrawDAO.findByCusId(cusId);
        return new PageInfo<>(cusPreDepositWithdraws);
    }

    @Override
    public PageInfo<StPreDepositWithdraw> stApplyList(Integer page, Integer size, Long stId) {
        PageHelper.startPage(page, size);
        List<StPreDepositWithdraw> stPreDepositWithdraws = stPreDepositWithdrawDAO.findByStId(stId);
        return new PageInfo<>(stPreDepositWithdraws);
    }

    @Override
    @Transactional
    public void cusCancelApply(Long applyId, Long cusId) {

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
        } else {
            throw new RuntimeException("取消申请失败");
        }


    }

    @Override
    @Transactional
    public void stCancelApply(Long applyId, Long stId) {

        StPreDepositWithdraw stPreDepositWithdraw = this.stPreDepositWithdrawDAO.findById(stId);
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
        } else {
            throw new RuntimeException("取消提现申请失败");
        }
    }





    public void remitStApply(String applyNo,ShiroUser shiroUser){

        // 申请单
        StPreDepositWithdraw stPreDepositWithdraw = stPreDepositWithdrawDAO.findByApplyNo(applyNo);

        if (stPreDepositWithdraw == null){
            throw  new RuntimeException("申请单不存在");
        }

        // 更新状态
        stPreDepositWithdraw.setStatus(PreDepositWithdrawStatus.REMITED);
        stPreDepositWithdraw.setCheckId(shiroUser.getId());
        stPreDepositWithdraw.setCheckCode(shiroUser.getName());
        stPreDepositWithdraw.setCheckName(shiroUser.getLoginName());


        // TODO 短信通知

    }

    private String createCode(){
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
        String suiji = random.nextInt(900)+100+"";
        return  code+"_"+now+suiji;
    }

    /********************************  后台方法 ********************************************/

    @Override
    public PageInfo<CusPreDepositWithdraw> getCusPageInfo(Integer page, Integer size, String keywords, String status){
        PageHelper.startPage(page, size);

        if (keywords.equals("")){
            keywords = null;
        }

        if (status.equals("")){
            status = null;
        }

        List<CusPreDepositWithdraw> cusPreDepositWithdrawList = cusPreDepositWithdrawDAO.findByKeywords(keywords,status);

        return new PageInfo<>(cusPreDepositWithdrawList);
    }

    @Override
    public PageInfo<StPreDepositWithdraw> getStPageInfo(Integer page, Integer size, String keywords, String status){
        PageHelper.startPage(page,size);

        if (keywords.equals("")){
            keywords = null;
        }

        if (status.equals("")){
            status = null;
        }

        List<StPreDepositWithdraw> stPreDepositWithdraws = stPreDepositWithdrawDAO.findByKeywords(keywords,status);

        return new PageInfo<>(stPreDepositWithdraws);
    }

    /**
     * 顾客 -- 通过
     * @param applyId
     * @param shiroUser
     * @throws Exception
     */
    public void cusApplyPass(Long applyId,ShiroUser shiroUser) throws Exception {

        CusPreDepositWithdraw apply = cusPreDepositWithdrawDAO.findById(applyId);
        if (apply != null){

            this.checkCusApply(apply,shiroUser,PreDepositWithdrawStatus.CHECKPASS);
        }else{
            throw new Exception("预存款提现，申请单不存在！");
        }
    }

    /**
     * 顾客 -- 驳回
     * @param applyId
     * @param shiroUser
     * @throws Exception
     */
    public void cusApplyreject(Long applyId,ShiroUser shiroUser) throws Exception {

        CusPreDepositWithdraw apply = cusPreDepositWithdrawDAO.findById(applyId);
        if (apply != null){
            if (apply.getStatus().equals(PreDepositWithdrawStatus.CHECKING)){
                // dai审核状态的单子才可以驳回
                this.checkCusApply(apply,shiroUser,PreDepositWithdrawStatus.CHECKRETURN);
            }
        }else{
            throw new Exception("预存款提现，申请单不存在！");
        }
    }

    /**
     * 打款
     * @param applyId
     * @param shiroUser
     * @throws Exception
     */
    public void cusApplyRemit(Long applyId,ShiroUser shiroUser) throws Exception {

        CusPreDepositWithdraw apply = cusPreDepositWithdrawDAO.findById(applyId);
        if (apply != null){

            this.checkCusApply(apply,shiroUser,PreDepositWithdrawStatus.REMITED);
        }else{
            throw new Exception("预存款提现，申请单不存在！");
        }
    }

    /**
     * 审核顾客预存款提现申请单
     *
     * @param applyNo
     */
    public void checkCusApply(String applyNo, ShiroUser shiroUser, PreDepositWithdrawStatus status) {

        // 申请单
        CusPreDepositWithdraw cusPreDepositWithdraw = cusPreDepositWithdrawDAO.findByApplyNo(applyNo);

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
            CustomerPreDeposit customerPreDeposit = appCustomerService.findByCusId(cusPreDepositWithdraw.getId());
            //退回预存款
            int row = customerDAO.updateDepositByUserIdAndLastUpdateTime(cusPreDepositWithdraw.getId(), cusPreDepositWithdraw.getWithdrawAmount(), new Timestamp(System.currentTimeMillis()), customerPreDeposit.getLastUpdateTime());
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

            // TODO 发送短信
        }
    }

    /**
     * 审核门店预存款提现申请单
     *
     * @param applyNo
     * @param shiroUser
     * @param status
     */
    public void checkStApply(String applyNo, ShiroUser shiroUser, PreDepositWithdrawStatus status) {

        // 申请单
        StPreDepositWithdraw stPreDepositWithdraw = stPreDepositWithdrawDAO.findByApplyNo(applyNo);

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
            StorePreDeposit preDeposit = appStoreService.findStorePreDepositByEmpId(stPreDepositWithdraw.getId());
            int row = appStoreDAO.updateStoreDepositByUserIdAndStoreDeposit(stPreDepositWithdraw.getId(), stPreDepositWithdraw.getWithdrawAmount(), preDeposit.getLastUpdateTime());
            if (1 != row) {
                throw new RuntimeException("提现申请失败");
            }

            // 记录日志
            CusPreDepositLogDO log = new CusPreDepositLogDO();
            log.setCreateTimeAndChangeMoneyAndType(LocalDateTime.now(), stPreDepositWithdraw.getWithdrawAmount(), CustomerPreDepositChangeType.RETURN_WITHDRAW);
            log.setUserIdAndOperatorinfo(stPreDepositWithdraw.getId(), stPreDepositWithdraw.getId(), AppIdentityType.SELLER, "");
            log.setOrderNumber(stPreDepositWithdraw.getApplyNo());
            log.setMerchantOrderNumber("");
            log.setBalance(CountUtil.add(preDeposit.getBalance(), stPreDepositWithdraw.getWithdrawAmount()));
            log.setChangeTypeDesc("门店预存款提现退回");
            this.cusPreDepositLogServiceImpl.save(log);

            // TODO 发送短信

        }
    }

    /**
     * 打款 顾客
     *
     * @param applyNo
     * @param shiroUser
     */
    public void remitCusApply(String applyNo, ShiroUser shiroUser) {

        // 申请单
        CusPreDepositWithdraw cusPreDepositWithdraw = cusPreDepositWithdrawDAO.findByApplyNo(applyNo);

        if (cusPreDepositWithdraw == null) {
            throw new RuntimeException("申请单不存在");
        }

        // 更新订单状态
        cusPreDepositWithdraw.setStatus(PreDepositWithdrawStatus.REMITED);
        cusPreDepositWithdraw.setCheckId(shiroUser.getId());
        cusPreDepositWithdraw.setCheckName(shiroUser.getName());
        cusPreDepositWithdraw.setCheckCode(shiroUser.getLoginName());

        // TODO 调用打款方法

        // TODO 短信通知
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

        // TODO 调用打款方法

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

    public PageInfo<CusPreDepositWithdraw> getCusPageInfo(Integer page, Integer size, String keywords, String status) {
        PageHelper.startPage(page, size);
        List<CusPreDepositWithdraw> cusPreDepositWithdrawList = cusPreDepositWithdrawDAO.findByKeywords(keywords, status);

        return new PageInfo<>(cusPreDepositWithdrawList);
    }

    public PageInfo<StPreDepositWithdraw> getStPageInfo(Integer page, Integer size, String keywords, String status) {
        PageHelper.startPage(page, size);

        List<StPreDepositWithdraw> stPreDepositWithdraws = stPreDepositWithdrawDAO.findByKeywords(keywords, status);

        return new PageInfo<>(stPreDepositWithdraws);
    }

}

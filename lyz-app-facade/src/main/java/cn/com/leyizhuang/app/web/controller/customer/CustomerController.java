package cn.com.leyizhuang.app.web.controller.customer;

import cn.com.leyizhuang.app.core.constant.*;
import cn.com.leyizhuang.app.core.utils.JwtUtils;
import cn.com.leyizhuang.app.foundation.pojo.*;
import cn.com.leyizhuang.app.foundation.pojo.request.CustomerRegistryParam;
import cn.com.leyizhuang.app.foundation.pojo.response.*;
import cn.com.leyizhuang.app.foundation.pojo.user.AppCustomer;
import cn.com.leyizhuang.app.foundation.pojo.user.AppEmployee;
import cn.com.leyizhuang.app.foundation.pojo.user.CustomerLeBi;
import cn.com.leyizhuang.app.foundation.pojo.user.CustomerPreDeposit;
import cn.com.leyizhuang.app.foundation.service.*;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * Richard
 *
 * @author 顾客控制器
 *         Created on 2017-09-21 12:59
 **/
@RestController
@RequestMapping(value = "/app/customer")
public class CustomerController {
    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @Resource
    private AppCustomerService customerService;

    @Resource
    private AppEmployeeService employeeService;

    @Resource
    private AppStoreService storeService;

    @Resource
    private CommonService commonService;

    @Autowired
    private CusPreDepositLogService cusPreDepositLogServiceImpl;

    @Resource
    private LeBiVariationLogService leBiVariationLogService;

    /**
     * App 顾客登录
     *
     * @param openId   微信openId
     * @param response 请求响应
     * @return resultDTO
     */
    @PostMapping(value = "/login", produces = "application/json;charset=UTF-8")
    public ResultDTO<CustomerLoginResponse> customerLogin(String openId, HttpServletResponse response) {
        logger.info("customerLogin CALLED,顾客登录，入参 openId:{}", openId);
        ResultDTO<CustomerLoginResponse> resultDTO;
        try {
            if (null == openId || "".equalsIgnoreCase(openId)) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "openId为空！", null);
                logger.info("customerLogin OUT,顾客登录失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            AppCustomer customer = customerService.findByOpenId(openId);
            if (customer == null) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "没有找到该用户！",
                        new CustomerLoginResponse(Boolean.FALSE, null, null,null));
                logger.info("customerLogin OUT,顾客登录失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            //拼装accessToken
            String accessToken = JwtUtils.createJWT(String.valueOf(customer.getCusId()), String.valueOf(customer.getMobile()),
                    JwtConstant.EXPPIRES_SECOND * 1000);
            System.out.println(accessToken);
            response.setHeader("token", accessToken);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null,
                    new CustomerLoginResponse(Boolean.TRUE, customer.getCusId(), customer.getMobile(),customer.getCityId()));
            logger.info("customerLogin OUT,顾客登录成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知异常", null);
            logger.warn("customerLogin EXCEPTION,顾客登录出现异常，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    /**
     * App 顾客注册
     *
     * @param registryParam 注册参数
     * @param response      请求响应
     * @return resultDTO
     */
    @PostMapping(value = "/registry", produces = "application/json;charset=UTF-8")
    public ResultDTO<CustomerRegistResponse> customerRegistry(CustomerRegistryParam registryParam,HttpServletResponse response) {
        logger.info("customerRegistry CALLED,顾客注册，入参 loginParam:{}", registryParam);
        ResultDTO<CustomerRegistResponse> resultDTO;
        try {
            if (null == registryParam.getOpenId() || "".equalsIgnoreCase(registryParam.getOpenId())) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "openId不能为空！", new CustomerRegistResponse(Boolean.FALSE, null));
                logger.info("customerRegistry OUT,顾客注册失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == registryParam.getPhone() || "".equalsIgnoreCase(registryParam.getPhone())) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "手机号码不能为空！", new CustomerRegistResponse(Boolean.FALSE, null));
                logger.info("customerRegistry OUT,顾客注册失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == registryParam.getCityId()) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "城市id不能为空！", new CustomerRegistResponse(Boolean.FALSE, null));
                logger.info("customerRegistry OUT,顾客注册失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            AppCustomer customer = customerService.findByOpenId(registryParam.getOpenId());
            if (customer != null) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "openId已存在！",
                        new CustomerRegistResponse(Boolean.TRUE, customer.getCusId()));
                logger.info("customerRegistry OUT,顾客注册失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            AppCustomer phoneUser = customerService.findByMobile(registryParam.getPhone());
            if (phoneUser != null) { //如果电话号码已经存在
                phoneUser.setOpenId(registryParam.getOpenId());
                phoneUser.setNickName(registryParam.getNickName());
                phoneUser.setPicUrl(registryParam.getPicUrl());
                customerService.update(phoneUser);
                String accessToken = JwtUtils.createJWT(String.valueOf(phoneUser.getCusId()), String.valueOf(phoneUser.getMobile()),
                        JwtConstant.EXPPIRES_SECOND * 1000);
                System.out.println(accessToken);
                response.setHeader("token", accessToken);
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null,
                        new CustomerRegistResponse(Boolean.FALSE, phoneUser.getCusId()));
                logger.info("customerRegistry OUT,顾客注册成功，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            } else {//如果电话号码不存在
                AppCustomer newUser = new AppCustomer();
                newUser.setCreateTime(LocalDateTime.now());
                newUser.setCreateType(AppCustomerCreateType.APP_REGISTRY);
                newUser.setOpenId(registryParam.getOpenId());
                newUser.setStatus(Boolean.TRUE);
                newUser.setSex((null != registryParam.getSex() && !registryParam.getSex()) ? SexType.FEMALE : SexType.MALE);
                newUser.setNickName(registryParam.getNickName());
                if(null == registryParam.getSex()){
                    newUser.setSex(SexType.SECRET);
                }else{
                    newUser.setSex(registryParam.getSex()?SexType.MALE:SexType.FEMALE);
                }
                newUser.setPicUrl(registryParam.getPicUrl());
                newUser.setCityId(registryParam.getCityId());
                newUser.setMobile(registryParam.getPhone());
                newUser.setLight(AppCustomerLightStatus.GREEN);
                newUser.setIsCashOnDelivery(Boolean.FALSE);
                AppCustomer returnUser =commonService .saveCustomerInfo(newUser,new CustomerLeBi(),new CustomerPreDeposit());
                //拼装accessToken
                String accessToken = JwtUtils.createJWT(String.valueOf(returnUser.getCusId()), String.valueOf(returnUser.getMobile()),
                        JwtConstant.EXPPIRES_SECOND * 1000);
                System.out.println(accessToken);
                response.setHeader("token", accessToken);
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null,
                        new CustomerRegistResponse(Boolean.FALSE, returnUser.getCusId()));
                logger.info("customerRegistry OUT,顾客注册成功，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知异常,注册失败", new CustomerRegistResponse(Boolean.FALSE, null));
            logger.warn("customerRegistry EXCEPTION,顾客注册失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }


    /**
     * 顾客注册 - 绑定导购
     * @param userId     顾客id
     * @param guidePhone 导购电话
     * @return resultDTO
     */
    @PostMapping(value = "/binding/seller", produces = "application/json;charset=UTF-8")
    public ResultDTO<CustomerBindingSellerResponse> customerBindingSeller(Long userId, String guidePhone) {
        logger.info("customerBindingSeller CALLED,顾客绑定服务导购，入参 userId {},guidePhone{}", userId, guidePhone);

        ResultDTO<CustomerBindingSellerResponse> resultDTO;
        try {
            if (null == userId) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "userId不能为空！", new CustomerBindingSellerResponse(Boolean.FALSE, null, null));
                logger.info("customerBindingSeller OUT,服务导购绑定失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            AppCustomer customer = customerService.findById(userId);
            if (customer == null) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户不存在！",
                        new CustomerBindingSellerResponse(Boolean.FALSE, null, null));
                logger.info("customerBindingSeller OUT,服务导购绑定失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null != guidePhone && !"".equalsIgnoreCase(guidePhone)) {//如果填写了推荐导购电话
                AppEmployee seller = employeeService.findByMobile(guidePhone);
                if (seller == null) {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "导购不存在！",
                            new CustomerBindingSellerResponse(Boolean.FALSE, null, null));
                    logger.info("customerBindingSeller OUT,服务导购绑定失败，出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }
                if (seller.getCityId() != customer.getCityId()){
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE,"不能绑定其他城市的导购！",null);
                    logger.info("customerBindingSeller OUT,服务导购绑定失败，出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }
                AppStore store = storeService.findById(seller.getStoreId());
                if (store == null) {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "该导购没有绑定有效的门店信息",
                            null);
                    logger.info("customerBindingSeller OUT,服务导购绑定失败，出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }
                customer.setSalesConsultId(seller.getEmpId());
                customer.setStoreId(store.getStoreId());
                customer.setCustomerType(AppCustomerType.MEMBER);
                customerService.update(customer);
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null,
                        new CustomerBindingSellerResponse(Boolean.TRUE, seller.getName(), store.getStoreName()));
                logger.info("customerBindingSeller OUT,服务导购绑定成功，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            } else {//未添加推荐导购电话
                AppStore store = storeService.findDefaultStoreByCityId(customer.getCityId());
                customer.setStoreId(store.getStoreId());
                customer.setSalesConsultId(0L);
                customer.setCustomerType(AppCustomerType.RETAIL);
                customer.setBindingTime(new Date());
                customerService.update(customer);
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null,
                        new CustomerBindingSellerResponse(Boolean.FALSE, null, store.getStoreName()));
                logger.info("customerBindingSeller OUT,服务导购绑定成功，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知异常,绑定导购失败", new CustomerBindingSellerResponse(Boolean.FALSE, null, null));
            logger.warn("customerBindingSeller EXCEPTION,服务导购绑定失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }


    /**
     * 顾客现金优惠券列表
     *
     * @param userId       顾客id
     * @param identityType 身份类型
     * @return ResultDTO
     */
    @PostMapping(value = "/cashCoupon/list", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> customerCashCoupon(Long userId, String identityType) {
        logger.info("customerCashCoupon CALLED,获取顾客可用优惠券，入参 userId {},identityType{}", userId, identityType);
        ResultDTO<Object> resultDTO;
        try {
            if (null == userId) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "userId不能为空！", null);
                logger.info("customerCashCoupon OUT,获取顾客可用优惠券失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            List<CashCouponResponse> cashCouponList = customerService.findCashCouponByCustomerId(userId);
            if (null != cashCouponList && cashCouponList.size() > 0) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, cashCouponList);

            } else {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
            }
            logger.info("customerCashCoupon OUT,获取顾客可用优惠券成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知异常,获取顾客可用优惠券失败", null);
            logger.warn("customerCashCoupon EXCEPTION,获取顾客可用优惠券失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    /**
     * 顾客产品券列表
     *
     * @param userId       顾客id
     * @param identityType 身份类型
     * @return ResultDTO
     */
    @PostMapping(value = "/productCoupon/list", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> customerProductCoupon(Long userId, String identityType) {
        logger.info("customerProductCoupon CALLED,获取顾客可用产品券，入参 userId {},identityType{}", userId, identityType);

        ResultDTO<Object> resultDTO;
        try {
            if (null == userId) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "userId不能为空！", null);
                logger.info("customerProductCoupon OUT,获取顾客可用产品券失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            List<ProductCouponResponse> productCouponList = customerService.findProductCouponByCustomerId(userId);
            if (null != productCouponList && productCouponList.size() > 0) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, productCouponList);

            } else {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
            }
            logger.info("customerProductCoupon OUT,获取顾客可用产品券成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知异常,获取顾客可用产品券失败", null);
            logger.warn("customerProductCoupon EXCEPTION,获取顾客可用产品券失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    /**
     * 顾客获取账户余额
     * @param userId
     * @param identityType
     * @return
     */
    @PostMapping(value = "/preDeposit/balance", produces = "application/json;charset=UTF-8")
    public ResultDTO getCustomerPreDepositBalance(Long userId, Integer identityType){

        logger.info("getCustomerPreDepositBalance CALLED,顾客获取账户余额，入参 userId {},identityType{}", userId, identityType);

        ResultDTO<Object> resultDTO;
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空", null);
            logger.info("getCustomerPreDepositBalance OUT,顾客获取账户余额失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空",
                    null);
            logger.info("getCustomerPreDepositBalance OUT,顾客获取账户余额失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            Double balance = customerService.findPreDepositBalanceByUserIdAndIdentityType(userId,identityType);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, balance);
            logger.info("getCustomerPreDepositBalance OUT,顾客获取账户余额成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，顾客获取账户余额失败", null);
            logger.warn("getCustomerPreDepositBalance EXCEPTION,顾客获取账户余额失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    /**
     * 顾客获取乐币数量
     * @param userId
     * @param identityType
     * @return
     */
    @PostMapping(value = "/lebi/quantity", produces = "application/json;charset=UTF-8")
    public ResultDTO getCustomerLeBiQuantity(Long userId, Integer identityType){

        logger.info("getCustomerLeBiQuantity CALLED,顾客获取乐币数量，入参 userId {},identityType{}", userId, identityType);

        ResultDTO<Object> resultDTO;
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空", null);
            logger.info("getCustomerLeBiQuantity OUT,顾客获取乐币数量失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空",
                    null);
            logger.info("getCustomerLeBiQuantity OUT,顾客获取乐币数量失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            Integer quantity = customerService.findLeBiQuantityByUserIdAndIdentityType(userId,identityType);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, quantity);
            logger.info("getCustomerLeBiQuantity OUT,顾客获取乐币数量成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，顾客获取乐币数量失败", null);
            logger.warn("getCustomerLeBiQuantity EXCEPTION,顾客获取乐币数量失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    /**
     * 顾客签到增加乐币
     * @param userId
     * @param identityType
     * @return
     */
    @PostMapping(value = "/sign", produces = "application/json;charset=UTF-8")
    public ResultDTO addCustomerLeBiQuantity(Long userId, Integer identityType){

        logger.info("addCustomerLeBiQuantity CALLED,顾客签到增加乐币，入参 userId {},identityType{}", userId, identityType);

        ResultDTO<Object> resultDTO;
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空", null);
            logger.info("addCustomerLeBiQuantity OUT,顾客签到增加乐币失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空",
                    null);
            logger.info("addCustomerLeBiQuantity OUT,顾客签到增加乐币失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            AppCustomer appCustomer = customerService.findById(userId);
            if(null != appCustomer.getLastSignTime() && DateUtils.isSameDay(appCustomer.getLastSignTime(),new Date())){
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "今天已签到，不能重复签到！", null);
                logger.info("addCustomerLeBiQuantity OUT,顾客签到增加乐币失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            commonService.updateCustomerSignTimeAndCustomerLeBiByUserId(userId,identityType);

            //记录变更明细日志
            CustomerLeBiVariationLog customerLeBiVariationLog = new CustomerLeBiVariationLog();
            customerLeBiVariationLog.setCusID(userId);
            customerLeBiVariationLog.setLeBiVariationType(LeBiVariationType.SIGN);
            customerLeBiVariationLog.setVariationQuantity(1);
            customerLeBiVariationLog.setVariationTime(new Date());
            customerLeBiVariationLog.setAfterVariationQuantity(customerService.findLeBiQuantityByUserIdAndIdentityType(userId,identityType));
            leBiVariationLogService.addCustomerLeBiVariationLog(customerLeBiVariationLog);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
            logger.info("addCustomerLeBiQuantity OUT,顾客签到增加乐币成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，顾客签到增加乐币失败", null);
            logger.warn("addCustomerLeBiQuantity EXCEPTION,顾客签到增加乐币失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }


    /**
     * @title   获取客户钱包充值记录
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2017/11/7
     */
    @PostMapping(value = "/PreDeposit/recharge/log", produces = "application/json;charset=UTF-8")
    public ResultDTO getCustomerRechargePreDepositLog(Long userId, Integer identityType){

        logger.info("getCustomerRechargePreDepositLog CALLED,获取客户钱包充值记录，入参 userId {},identityType{}", userId, identityType);

        ResultDTO<Object> resultDTO;
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空", null);
            logger.info("getCustomerRechargePreDepositLog OUT,获取客户钱包充值记录失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType || identityType != 6) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型错误！",
                    null);
            logger.info("getCustomerRechargePreDepositLog OUT,获取客户钱包充值记录失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            List<PreDepositChangeType> preDepositChangeTypeList = PreDepositChangeType.getRechargeType();
            List<PreDepositLogResponse> preDepositLogResponseList = this.cusPreDepositLogServiceImpl.findByUserIdAndType(userId, preDepositChangeTypeList);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, preDepositLogResponseList);
            logger.info("getCustomerRechargePreDepositLog OUT,获取客户钱包充值记录成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，获取客户钱包充值记录失败", null);
            logger.warn("getCustomerRechargePreDepositLog EXCEPTION,获取客户钱包充值记录失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    /**
     * @title   获取客户钱包消费记录
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2017/11/7
     */
    @PostMapping(value = "/PreDeposit/consumption/log", produces = "application/json;charset=UTF-8")
    public ResultDTO getCustomerConsumptionPreDepositLog(Long userId, Integer identityType){

        logger.info("getCustomerConsumptionPreDepositLog CALLED,获取客户钱包消费记录，入参 userId {},identityType{}", userId, identityType);

        ResultDTO<Object> resultDTO;
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空", null);
            logger.info("getCustomerConsumptionPreDepositLog OUT,获取客户钱包消费记录失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType || identityType != 6) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型错误！",
                    null);
            logger.info("getCustomerConsumptionPreDepositLog OUT,获取客户钱包消费记录失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            List<PreDepositChangeType> preDepositChangeTypeList = PreDepositChangeType.getConsumptionType();
            List<PreDepositLogResponse> preDepositLogResponseList = this.cusPreDepositLogServiceImpl.findByUserIdAndType(userId, preDepositChangeTypeList);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, preDepositLogResponseList);
            logger.info("getCustomerConsumptionPreDepositLog OUT,获取客户钱包消费记录成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，获取客户钱包消费记录失败", null);
            logger.warn("getCustomerConsumptionPreDepositLog EXCEPTION,获取客户钱包消费记录失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }
}


package cn.com.leyizhuang.app.web.controller.customer;

import cn.com.leyizhuang.app.core.bean.GridDataVO;
import cn.com.leyizhuang.app.core.constant.*;
import cn.com.leyizhuang.app.core.utils.DateUtil;
import cn.com.leyizhuang.app.core.utils.JwtUtils;
import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.pojo.AppStore;
import cn.com.leyizhuang.app.foundation.pojo.message.AppUserDevice;
import cn.com.leyizhuang.app.foundation.pojo.request.CustomerRegistryParam;
import cn.com.leyizhuang.app.foundation.pojo.response.*;
import cn.com.leyizhuang.app.foundation.pojo.user.*;
import cn.com.leyizhuang.app.foundation.service.*;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import cn.com.leyizhuang.common.util.AssertUtil;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Richard
 *
 * @author 顾客控制器
 * Created on 2017-09-21 12:59
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

    @Resource
    private CusPreDepositLogService cusPreDepositLogServiceImpl;

    @Resource
    private LeBiVariationLogService leBiVariationLogService;

    @Resource
    private AppUserDeviceService userDeviceService;

    /**
     * App 顾客登录
     *
     * @param openId   微信openId
     * @param response 请求响应
     * @return resultDTO
     */
    @ApiOperation(value = "顾客登录", notes = "微信授权登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "openId", value = "微信open_id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "systemType", value = "顾客设备类型", required = true, dataType = "String"),
            @ApiImplicitParam(name = "clientId", value = "个推客户端id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "deviceId", value = "设备序列号", required = true, dataType = "String"),
            @ApiImplicitParam(name = "response", value = "响应对象（后端生成，不用传值）", required = false, dataType = "Object")
    })
    @PostMapping(value = "/login", produces = "application/json;charset=UTF-8")
    public ResultDTO<CustomerLoginResponse> customerLogin(String openId, String systemType, String clientId, String deviceId, HttpServletResponse response) {
        //logger.info("customerLogin CALLED,顾客登录，入参 openId:{}", openId);
        ResultDTO<CustomerLoginResponse> resultDTO;
        try {
            if (null == openId || "".equalsIgnoreCase(openId)) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "openId为空！", null);
                logger.info("customerLogin OUT,顾客登录失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == clientId || "".equalsIgnoreCase(clientId)) {

                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "clientId为空！", null);
                logger.info("customerLogin OUT,顾客登录失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == deviceId || "".equalsIgnoreCase(deviceId)) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "deviceId为空！", null);
                logger.info("customerLogin OUT,顾客登录失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == systemType || "".equalsIgnoreCase(systemType)) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "systemType为空！", null);
                logger.info("customerLogin OUT,顾客登录失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }

            AppCustomer customer = customerService.findByOpenId(openId);
            if (customer == null) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "当前微信账号还没有注册，先来注册一个吧！",
                        new CustomerLoginResponse(Boolean.FALSE, null, null, null, null));
                logger.info("customerLogin OUT,顾客登录失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            AppUserDevice device = userDeviceService.findByClientIdAndDeviceIdAndUserIdAndIdentityType(clientId, deviceId, customer.getCusId(), AppIdentityType.CUSTOMER);
            if (null == device) {
                device = new AppUserDevice(null, customer.getCusId(), AppIdentityType.CUSTOMER, AppSystemType.getAppSystemTypeByValue(systemType),
                        clientId, deviceId, new Date(), new Date());
                userDeviceService.addUserDevice(device);
            } else {
                device.setLastLoginTime(new Date());
                userDeviceService.updateLastLoginTime(device);
            }
            //拼装accessToken
            String accessToken = JwtUtils.createJWT(String.valueOf(customer.getCusId()), String.valueOf(customer.getMobile()),
                    JwtConstant.EXPPIRES_SECOND * 1000);
            System.out.println(accessToken);
            response.setHeader("token", accessToken);
            //判断是否是专供会员
            CustomerRankInfoResponse rankInfo = this.customerService.findCusRankinfoByCusId(customer.getCusId());
            String rankCode = null;
            if (null != rankInfo && null != rankInfo.getRankCode() && !("".equals(rankInfo.getRankCode()))) {
                rankCode = rankInfo.getRankCode();
            }

            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null,
                    new CustomerLoginResponse(Boolean.TRUE, customer.getCusId(), customer.getMobile(), customer.getCityId(), rankCode));
            //logger.info("customerLogin OUT,顾客登录成功，出参 resultDTO:{}", resultDTO);
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
     * 顾客注册 - 绑定微信
     *
     * @param registryParam 注册参数
     * @param response      请求响应
     * @return resultDTO
     */
    @PostMapping(value = "/registry", produces = "application/json;charset=UTF-8")
    public ResultDTO<CustomerRegistResponse> customerRegistry(CustomerRegistryParam registryParam, HttpServletResponse response) {
        // logger.info("customerRegistry CALLED,顾客注册，入参 loginParam:{}", registryParam);
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
            if (null == registryParam.getProfession()) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "工种不能为空！", new CustomerRegistResponse(Boolean.FALSE, null));
                logger.info("customerRegistry OUT,顾客注册失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == registryParam.getName()) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "顾客姓名不能为空！", new CustomerRegistResponse(Boolean.FALSE, null));
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
            //如果电话号码已经存在
            if (phoneUser != null) {
                //open_id为空
                if (StringUtils.isBlank(phoneUser.getOpenId())) {
                    phoneUser.setOpenId(registryParam.getOpenId());
                    phoneUser.setNickName(registryParam.getNickName());
                    phoneUser.setPicUrl(registryParam.getPicUrl());
                    if (null != phoneUser.getName() && !"".equals(phoneUser.getName())) {
                        phoneUser.setName(registryParam.getName());
                    }
                    customerService.update(phoneUser);
                    String accessToken = JwtUtils.createJWT(String.valueOf(phoneUser.getCusId()), String.valueOf(phoneUser.getMobile()),
                            JwtConstant.EXPPIRES_SECOND * 1000);
                    System.out.println(accessToken);
                    response.setHeader("token", accessToken);
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null,
                            new CustomerRegistResponse(Boolean.FALSE, phoneUser.getCusId()));
                    logger.info("customerRegistry OUT,顾客注册成功，出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                } else {
                    //open_id不为空
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "该用户当前已绑定微信，请解绑当前微信后再绑定新微信！",
                            new CustomerRegistResponse(Boolean.TRUE, phoneUser.getCusId()));
                    logger.info("customerRegistry OUT,顾客注册失败，出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }

            } else {//如果电话号码不存在
                AppCustomer newUser = new AppCustomer();
                newUser.setCreateTime(LocalDateTime.now());
                newUser.setCreateType(AppCustomerCreateType.APP_REGISTRY);
                newUser.setOpenId(registryParam.getOpenId());
                newUser.setStatus(Boolean.TRUE);
                newUser.setSex((null != registryParam.getSex() && !registryParam.getSex()) ? SexType.FEMALE : SexType.MALE);
                newUser.setNickName(registryParam.getNickName());
                if (null == registryParam.getSex()) {
                    newUser.setSex(SexType.SECRET);
                } else {
                    newUser.setSex(registryParam.getSex() ? SexType.MALE : SexType.FEMALE);
                }
                newUser.setPicUrl(registryParam.getPicUrl());
                Long cityId = registryParam.getCityId();
                AppStore store = storeService.findDefaultStoreByCityId(cityId);
                newUser.setStoreId(store.getStoreId());
                newUser.setCityId(cityId);
                newUser.setMobile(registryParam.getPhone());
                newUser.setLight(AppCustomerLightStatus.NOT);
                newUser.setIsCashOnDelivery(Boolean.FALSE);
                newUser.setCustomerProfession(registryParam.getProfession());
                List<CustomerProfession> professions = customerService.getCustomerProfessionListByStatus(AppWhetherFlag.Y.toString());
                newUser.setCustomerProfessionDesc(null != professions ? professions.stream().filter(p -> p.getTitle().equals(registryParam.getProfession())).collect(Collectors.toList()).get(0).getDescription() : "");
                newUser.setName(registryParam.getName());
                AppCustomer returnUser = commonService.saveCustomerInfo(newUser, new CustomerLeBi(), new CustomerPreDeposit());
                //拼装accessToken
                String accessToken = JwtUtils.createJWT(String.valueOf(returnUser.getCusId()), String.valueOf(returnUser.getMobile()),
                        JwtConstant.EXPPIRES_SECOND * 1000);
                System.out.println(accessToken);
                response.setHeader("token", accessToken);
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null,
                        new CustomerRegistResponse(Boolean.FALSE, returnUser.getCusId()));
                //logger.info("customerRegistry OUT,顾客注册成功，出参 resultDTO:{}", resultDTO);
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
     *
     * @param userId     顾客id
     * @param guidePhone 导购电话
     * @return resultDTO
     */
    @PostMapping(value = "/binding/seller", produces = "application/json;charset=UTF-8")
    public ResultDTO<CustomerBindingSellerResponse> customerBindingSeller(Long userId, String guidePhone) {
        //logger.info("customerBindingSeller CALLED,顾客绑定服务导购，入参 userId {},guidePhone{}", userId, guidePhone);

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
            //如果填写了推荐导购电话
            if (null != guidePhone && !"".equalsIgnoreCase(guidePhone)) {
                AppEmployee seller = employeeService.findByMobile(guidePhone);
                if (seller == null) {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "导购不存在！",
                            new CustomerBindingSellerResponse(Boolean.FALSE, null, null));
                    logger.info("customerBindingSeller OUT,服务导购绑定失败，出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }
                if (!Objects.equals(seller.getCityId(), customer.getCityId())) {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "不能绑定其他城市的导购！", null);
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
                customer.setBindingTime(new Date());
                customerService.update(customer);
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null,
                        new CustomerBindingSellerResponse(Boolean.TRUE, seller.getName(), store.getStoreName()));
                logger.info("customerBindingSeller OUT,服务导购绑定成功，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            } else {//未添加推荐导购电话
                AppStore store = storeService.findDefaultStoreByCityId(customer.getCityId());
                if (null == store) {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "该城市下没有默认门店!", null);
                    logger.info("customerBindingSeller OUT,服务导购绑定失败，出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }
                customer.setStoreId(store.getStoreId());
                //customer.setSalesConsultId(0L);
                customer.setCustomerType(AppCustomerType.RETAIL);
                customer.setBindingTime(new Date());
                customerService.update(customer);
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null,
                        new CustomerBindingSellerResponse(Boolean.FALSE, null, store.getStoreName()));
                //logger.info("customerBindingSeller OUT,服务导购绑定成功，出参 resultDTO:{}", resultDTO);
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
     * 顾客解除微信绑定
     *
     * @param userId       顾客id
     * @param identityType 身份类型
     * @return resultDTO
     */
    @PostMapping(value = "/unbinding/weChat", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> customerUnbindingWeChat(Long userId, Integer identityType) {

        ResultDTO<Object> resultDTO;
        try {
            if (null == userId) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "userId不能为空！", null);
                logger.info("customerUnbindingWeChat OUT,顾客解除微信绑定失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == identityType || !(identityType == AppIdentityType.CUSTOMER.getValue())) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "身份类型不正确！", null);
                logger.info("customerUnbindingWeChat OUT,顾客解除微信绑定失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            AppCustomer customer = customerService.findById(userId);
            if (customer == null) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户不存在！", null);
                logger.info("customerUnbindingWeChat OUT,顾客解除微信绑定失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            } else {
                customerService.unbindingCustomerWeChat(userId);
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "顾客解除微信绑定成功！", null);
                logger.info("customerUnbindingWeChat OUT,顾客解除微信绑定成功，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知异常,顾客解除微信绑定失败", null);
            logger.warn("customerUnbindingWeChat EXCEPTION,顾客解除微信绑定失败，出参 resultDTO:{}", resultDTO);
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
        //logger.info("customerCashCoupon CALLED,获取顾客可用优惠券，入参 userId {},identityType{}", userId, identityType);
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
            //logger.info("customerCashCoupon OUT,获取顾客可用优惠券成功，出参 resultDTO:{}", resultDTO);
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
     * @param userId       用户id
     * @param identityType 用户身份类型
     * @param cusId        顾客id
     * @return 顾客产品券列表
     */
    @PostMapping(value = "/productCoupon/list", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> customerProductCoupon(Long userId, Integer identityType, Long cusId) {
        logger.info("customerProductCoupon CALLED,获取顾客可用产品券，入参 userId {},identityType{}", userId, identityType);

        ResultDTO<Object> resultDTO;
        try {
            if (null == userId) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "userId不能为空！", null);
                logger.info("customerProductCoupon OUT,获取顾客可用产品券失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == identityType) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "身份类型不能为空！", null);
                logger.info("customerProductCoupon OUT,获取顾客可用产品券失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (identityType == 0 && cusId == null) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "顾客id不能为空！", null);
                logger.info("customerProductCoupon OUT,获取顾客可用产品券失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (identityType == 6) {
                List<ProductCouponResponse> productCouponList = customerService.findProductCouponByCustomerId(userId);
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, productCouponList);
                //logger.info("customerProductCoupon OUT,获取顾客可用产品券成功，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            } else if (identityType == 0) {
                List<ProductCouponResponse> productCouponResponseList = customerService.
                        findProductCouponBySellerIdAndCustomerId(userId, cusId);
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, productCouponResponseList);
                //logger.info("customerProductCoupon OUT,获取顾客可用产品券成功，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            } else {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户身份类型不合法,获取产品券列表失败", null);
                logger.info("customerProductCoupon OUT,获取顾客可用产品券失败,出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
        } catch (Exception e) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知异常,获取顾客可用产品券失败", null);
            logger.warn("customerProductCoupon EXCEPTION,获取顾客可用产品券失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }


    /**
     * 顾客获取账户余额
     *
     * @param userId       用户id
     * @param identityType 用户身份
     * @return 用户预存款余额
     */
    @PostMapping(value = "/preDeposit/balance", produces = "application/json;charset=UTF-8")
    public ResultDTO getCustomerPreDepositBalance(Long userId, Integer identityType) {

        //logger.info("getCustomerPreDepositBalance CALLED,顾客获取账户余额，入参 userId {},identityType{}", userId, identityType);

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
            Double balance = customerService.findPreDepositBalanceByUserIdAndIdentityType(userId, identityType);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, balance);
            //logger.info("getCustomerPreDepositBalance OUT,顾客获取账户余额成功，出参 resultDTO:{}", resultDTO);
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
     *
     * @param userId       用户id
     * @param identityType 身份类型
     * @return 用户乐币数量
     */
    @PostMapping(value = "/lebi/quantity", produces = "application/json;charset=UTF-8")
    public ResultDTO getCustomerLeBiQuantity(Long userId, Integer identityType) {

        //logger.info("getCustomerLeBiQuantity CALLED,顾客获取乐币数量，入参 userId {},identityType{}", userId, identityType);

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
            Integer quantity = customerService.findLeBiQuantityByUserIdAndIdentityType(userId, identityType);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, quantity);
            //logger.info("getCustomerLeBiQuantity OUT,顾客获取乐币数量成功，出参 resultDTO:{}", resultDTO);
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
     *
     * @param userId       用户id
     * @param identityType 用户身份类型
     * @return 签到是否成功
     */
    @PostMapping(value = "/sign", produces = "application/json;charset=UTF-8")
    public ResultDTO addCustomerLeBiQuantity(Long userId, Integer identityType) {

        //logger.info("addCustomerLeBiQuantity CALLED,顾客签到增加乐币，入参 userId {},identityType{}", userId, identityType);

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
            if (null != appCustomer.getLastSignTime() && DateUtils.isSameDay(appCustomer.getLastSignTime(), new Date())) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "今天已签到，不能重复签到！", null);
                logger.info("addCustomerLeBiQuantity OUT,顾客签到增加乐币失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            commonService.customerSign(userId, identityType);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
            //logger.info("addCustomerLeBiQuantity OUT,顾客签到增加乐币成功，出参 resultDTO:{}", resultDTO);
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
     * @param
     * @return
     * @throws
     * @title 获取客户钱包充值记录
     * @descripe
     * @author GenerationRoad
     * @date 2017/11/7
     */
    @PostMapping(value = "/PreDeposit/recharge/log", produces = "application/json;charset=UTF-8")
    public ResultDTO getCustomerRechargePreDepositLog(Long userId, Integer identityType, Integer page, Integer size) {

        //logger.info("getCustomerRechargePreDepositLog CALLED,获取客户钱包充值记录，入参 userId {},identityType{},page:{},size:{}", userId, identityType,page,size);

        ResultDTO<Object> resultDTO;
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空", null);
            logger.info("getCustomerRechargePreDepositLog OUT,获取客户钱包充值记录失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == page) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "页码不能为空",
                    null);
            logger.info("getCustomerSignDetail OUT,获取客户钱包充值记录失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == size) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "单页显示条数不能为空",
                    null);
            logger.info("getCustomerSignDetail OUT,获取客户钱包充值记录失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType || identityType != 6) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型错误！",
                    null);
            logger.info("getCustomerRechargePreDepositLog OUT,获取客户钱包充值记录失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            List<CustomerPreDepositChangeType> preDepositChangeTypeList = CustomerPreDepositChangeType.getRechargeType();
            PageInfo<PreDepositLogResponse> preDepositLogResponseList = this.cusPreDepositLogServiceImpl.findByUserIdAndType(userId, preDepositChangeTypeList, page, size);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, new GridDataVO<PreDepositLogResponse>().transform(preDepositLogResponseList));
            //logger.info("getCustomerRechargePreDepositLog OUT,获取客户钱包充值记录成功，出参 resultDTO:{}", resultDTO);
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
     * @param userId       用户id
     * @param identityType 身份类型
     * @return 预存款消费日志
     */
    @PostMapping(value = "/PreDeposit/consumption/log", produces = "application/json;charset=UTF-8")
    public ResultDTO getCustomerConsumptionPreDepositLog(Long userId, Integer identityType, Integer page, Integer size) {

        // logger.info("getCustomerConsumptionPreDepositLog CALLED,获取客户钱包消费记录，入参 userId {},identityType{},page:{},size:{}", userId, identityType,page,size);

        ResultDTO<Object> resultDTO;
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空", null);
            logger.info("getCustomerConsumptionPreDepositLog OUT,获取客户钱包消费记录失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == page) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "页码不能为空",
                    null);
            logger.info("getCustomerSignDetail OUT,获取客户钱包消费记录失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == size) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "单页显示条数不能为空",
                    null);
            logger.info("getCustomerSignDetail OUT,获取客户钱包消费记录失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType || identityType != 6) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型错误！",
                    null);
            logger.info("getCustomerConsumptionPreDepositLog OUT,获取客户钱包消费记录失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            List<CustomerPreDepositChangeType> preDepositChangeTypeList = CustomerPreDepositChangeType.getConsumptionType();
            PageInfo<PreDepositLogResponse> preDepositLogResponseList = this.cusPreDepositLogServiceImpl.findByUserIdAndType(userId, preDepositChangeTypeList, page, size);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, new GridDataVO<PreDepositLogResponse>().transform(preDepositLogResponseList));
            //logger.info("getCustomerConsumptionPreDepositLog OUT,获取客户钱包消费记录成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，获取客户钱包消费记录失败", null);
            logger.warn("getCustomerConsumptionPreDepositLog EXCEPTION,获取客户钱包消费记录失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    /**
     * 获取顾客签到概况
     *
     * @param cusId        顾客id
     * @param identityType 身份类型
     * @return 顾客签到概况
     */
    @PostMapping(value = "/sign/overview", produces = "application/json;charset=UTF-8")
    public ResultDTO getCustomerSignOverview(Long cusId, Integer identityType) {
        ResultDTO<Object> resultDTO;
        if (null == cusId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空", null);
            logger.info("getCustomerSignOverview OUT,获取顾客签到信息失败,出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空",
                    null);
            logger.info("getCustomerSignOverview OUT,获取顾客签到信息失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (identityType != AppIdentityType.CUSTOMER.getValue()) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "非顾客身份不开放签到功能",
                    null);
            logger.info("getCustomerSignOverview OUT,获取顾客签到信息失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            AppCustomer appCustomer = customerService.findById(cusId);
            if (null != appCustomer) {
                CustomerSignOverviewResponse response = new CustomerSignOverviewResponse();
                if (null != appCustomer.getLastSignTime() && DateUtils.isSameDay(appCustomer.getLastSignTime(), new Date())) {
                    response.setCanSign(false);
                } else {
                    response.setCanSign(true);
                }
                if (null != appCustomer.getConsecutiveSignDays()) {
                    response.setConsecutiveSignDays(appCustomer.getConsecutiveSignDays());
                } else {
                    response.setConsecutiveSignDays(0);
                }
                Integer days = customerService.countSignDaysByCusId(cusId, DateUtil.getStartTimeOfThisMonth(), new Date());
                response.setMonthlySignDays(days);
                Integer totalDays = customerService.countTotalSignDaysByCusId(cusId);
                response.setTotalSignDays(totalDays);
                Integer totalLebiQty = customerService.countSignAwardLebiQtyByCusId(cusId);
                if (null != totalLebiQty) {
                    response.setTotalAwardsQty(totalLebiQty);
                } else {
                    response.setTotalAwardsQty(0);
                }
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, response);
                return resultDTO;
            } else {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "没有找到该顾客",
                        null);
                return resultDTO;
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，获取顾客签到信息失败", null);
            logger.warn("getCustomerSignOverview EXCEPTION,获取顾客签到信息失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }


    @PostMapping(value = "/sign/detail", produces = "application/json;charset=UTF-8")
    public ResultDTO getCustomerSignDetail(Long cusId, Integer identityType, Integer page, Integer size) {
        ResultDTO<Object> resultDTO;
        if (null == cusId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空", null);
            logger.info("getCustomerSignDetail OUT,获取顾客签到明细失败,出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空",
                    null);
            logger.info("getCustomerSignDetail OUT,获取顾客签到明细失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (identityType != AppIdentityType.CUSTOMER.getValue()) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "非顾客身份不开放签到功能",
                    null);
            logger.info("getCustomerSignDetail OUT,获取顾客签到明细失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == page) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "页码不能为空",
                    null);
            logger.info("getCustomerSignDetail OUT,获取顾客签到明细失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == size) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "单页显示条数不能为空",
                    null);
            logger.info("getCustomerSignDetail OUT,获取顾客签到明细失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            AppCustomer appCustomer = customerService.findById(cusId);
            if (null != appCustomer) {
                PageInfo<CustomerSignLogBrief> logPage = this.customerService.findCustomerSignDetailByCusIdWithPageable(cusId, page, size);
                CustomerSignDetailResponse response = new CustomerSignDetailResponse();
                response.setCount(logPage.getTotal());
                response.setNumsPerPage(logPage.getPageSize());
                response.setTotalPage(logPage.getPages());
                response.setCurrentPage(logPage.getPageNum());
                response.setData(logPage.getList());
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, response);
                return resultDTO;
            } else {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "没有找到该顾客",
                        null);
                return resultDTO;
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，获取顾客签到明细失败", null);
            logger.warn("getCustomerSignDetail EXCEPTION,获取顾客签到信息失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    /**
     * 获取APP顾客身份类型列表
     *
     * @return APP顾客身份类型列表
     */
    @PostMapping(value = "/get/customerProfession", produces = "application/json;charset=UTF-8")
    public ResultDTO getCustomerIdentityType() {
        ResultDTO<Object> resultDTO;
        try {
            List<CustomerProfession> customerIdentityTypeList = customerService.getCustomerProfessionListByStatus(AppWhetherFlag.Y.toString());
            if (AssertUtil.isNotEmpty(customerIdentityTypeList)) {
                Map<String, String> returnMap = new HashMap<>(20);
                customerIdentityTypeList.forEach(p -> returnMap.put(p.getTitle(), p.getDescription()));
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null,
                        returnMap);
            } else {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "没有获取到工种信息",
                        null);
                return resultDTO;
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，获取工种信息失败", null);
            logger.warn("getCustomerIdentityType EXCEPTION,获取工种信息失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }


    /**
     * 顾客获取咨询电话
     *
     * @param userId       用户id
     * @param identityType 身份类型
     * @return 咨询电话
     */
    @PostMapping(value = "/supportHotline", produces = "application/json;charset=UTF-8")
    public ResultDTO getCustomerSupportHotline(Long userId, Integer identityType) {
        logger.info("getCustomerSupportHotline CALLED,顾客获取咨询电话，入参 userId {},identityType{}", userId, identityType);
        ResultDTO<Object> resultDTO;
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空", null);
            logger.info("getCustomerSupportHotline OUT,顾客获取咨询电话失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType || 6 != identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型错误",
                    null);
            logger.info("getCustomerSupportHotline OUT,顾客获取咨询电话失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            List<SupportHotlineResponse> supportHotline = customerService.getCustomerSupportHotline(userId);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, supportHotline);
            logger.info("getCustomerSupportHotline OUT顾客获取咨询电话成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，顾客获取咨询电话失败", null);
            logger.warn("getCustomerSupportHotline EXCEPTION,顾客获取咨询电话失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    /**
     * 根据 顾客手机号返回顾客信息
     *
     * @param phone
     * @return
     */
    @PostMapping(value = "/get/customer/info", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> getCustomerInfoByPhone(String phone) {
        logger.info("getCustomerInfoByPhone CALLED,顾客信息获取，入参 phone {}", phone);
        ResultDTO<Object> resultDTO;

        if (phone == null || phone.equals("")) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户手机号为空", null);
            logger.info("getCustomerInfoByPhone OUT,顾客信息获取,出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }

        AppCustomer appCustomer = new AppCustomer();
        try {
            appCustomer = customerService.findByMobile(phone);

            // 设置默认导购电话
            Long sellerId = appCustomer.getSalesConsultId();
            if (sellerId != null) {
                AppEmployee appEmployee = employeeService.findById(sellerId);
                appCustomer.setSalesPhone(appEmployee.getMobile());
            }

            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, appCustomer);
        } catch (Exception e) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，顾客获取咨询电话失败", null);
            logger.warn("getCustomerInfoByPhone OUT,顾客信息获取,出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    /**
     * 修改顾客类型
     *
     * @param userId       用户id
     * @param identityType 用户类型
     * @return
     */
    @PostMapping(value = "/update/type", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> updateCustomerType(Long userId, Integer identityType) {
        logger.info("updateCustomerType CALLED,修改顾客类型，入参 userId {},identityType{}", userId, identityType);
        ResultDTO<Object> resultDTO;
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空", null);
            logger.info("updateCustomerType OUT,修改顾客类型失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType || 6 != identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型错误",
                    null);
            logger.info("updateCustomerType OUT,修改顾客类型失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            AppCustomer customer = customerService.findById(userId);
            if (customer.getCustomerType().equals(AppCustomerType.MEMBER)) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "此顾客已经是会员，修改顾客类型失败！", null);
                logger.info("updateCustomerType OUT,修改顾客类型失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            customerService.updateCustomerTypeByUserId(userId);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
            logger.info("updateCustomerType OUT修改顾客类型成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，修改顾客类型失败", null);
            logger.warn("updateCustomerType OUT,修改顾客类型失败,出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }
}


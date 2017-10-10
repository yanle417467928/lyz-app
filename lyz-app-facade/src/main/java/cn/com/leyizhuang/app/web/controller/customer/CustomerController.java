package cn.com.leyizhuang.app.web.controller.customer;

import cn.com.leyizhuang.app.core.constant.JwtConstant;
import cn.com.leyizhuang.app.core.constant.SexType;
import cn.com.leyizhuang.app.core.utils.JwtUtils;
import cn.com.leyizhuang.app.foundation.pojo.AppCustomer;
import cn.com.leyizhuang.app.foundation.pojo.AppEmployee;
import cn.com.leyizhuang.app.foundation.pojo.AppStore;
import cn.com.leyizhuang.app.foundation.pojo.request.CustomerRegistryParam;
import cn.com.leyizhuang.app.foundation.pojo.response.*;
import cn.com.leyizhuang.app.foundation.service.IAppCustomerService;
import cn.com.leyizhuang.app.foundation.service.IAppEmployeeService;
import cn.com.leyizhuang.app.foundation.service.IAppStoreService;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
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
    private IAppCustomerService customerService;

    @Resource
    private IAppEmployeeService employeeService;

    @Resource
    private IAppStoreService storeService;

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
                        new CustomerLoginResponse(Boolean.FALSE, null, null));
                logger.info("customerLogin OUT,顾客登录失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            //拼装accessToken
            String accessToken = JwtUtils.createJWT(String.valueOf(customer.getId()), String.valueOf(customer.getMobile()),
                    JwtConstant.EXPPIRES_SECOND * 1000);
            System.out.println(accessToken);
            response.setHeader("token", accessToken);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null,
                    new CustomerLoginResponse(Boolean.TRUE, customer.getId(), customer.getMobile()));
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
    public ResultDTO<CustomerRegistResponse> customerRegistry(CustomerRegistryParam registryParam, HttpServletResponse response) {
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
                        new CustomerRegistResponse(Boolean.TRUE, customer.getId()));
                logger.info("customerRegistry OUT,顾客注册失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            AppCustomer phoneUser = customerService.findByMobile(registryParam.getPhone());
            if (phoneUser != null) {
                phoneUser.setOpenId(registryParam.getOpenId());
                phoneUser.setNickName(registryParam.getNickName());
                phoneUser.setPicUrl(registryParam.getPicUrl());
                customerService.update(phoneUser);
                String accessToken = JwtUtils.createJWT(String.valueOf(phoneUser.getId()), String.valueOf(phoneUser.getMobile()),
                        JwtConstant.EXPPIRES_SECOND * 1000);
                System.out.println(accessToken);
                response.setHeader("token", accessToken);
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null,
                        new CustomerRegistResponse(Boolean.FALSE, phoneUser.getId()));
                logger.info("customerRegistry OUT,顾客注册成功，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            } else {
                AppCustomer newUser = new AppCustomer();
                newUser.setOpenId(registryParam.getOpenId());
                newUser.setStatus(Boolean.TRUE);
                newUser.setSex((null != registryParam.getSex() && !registryParam.getSex()) ? SexType.FEMALE : SexType.MALE);
                newUser.setNickName(registryParam.getNickName());
                newUser.setPicUrl(registryParam.getPicUrl());
                newUser.setCityId(registryParam.getCityId());
                newUser.setMobile(registryParam.getPhone());
                AppCustomer returnUser = customerService.save(newUser);
                //拼装accessToken
                String accessToken = JwtUtils.createJWT(String.valueOf(returnUser.getId()), String.valueOf(returnUser.getMobile()),
                        JwtConstant.EXPPIRES_SECOND * 1000);
                System.out.println(accessToken);
                response.setHeader("token", accessToken);
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null,
                        new CustomerRegistResponse(Boolean.FALSE, returnUser.getId()));
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
            if (null != guidePhone && !"".equalsIgnoreCase(guidePhone)) {
                AppEmployee seller = employeeService.findByMobile(guidePhone);
                if (seller == null) {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "导购不存在！",
                            new CustomerBindingSellerResponse(Boolean.FALSE, null, null));
                    logger.info("customerBindingSeller OUT,服务导购绑定失败，出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }
                AppStore store = storeService.findById(seller.getId());
                if (store == null) {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "该导购没有绑定有效的门店信息",
                            null);
                    logger.info("customerBindingSeller OUT,服务导购绑定失败，出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }
                customer.setSalesConsultId(seller.getId());
                customer.setStoreId(store.getId());
                customerService.update(customer);
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null,
                        new CustomerBindingSellerResponse(Boolean.TRUE, seller.getName(), store.getStoreName()));
                logger.info("customerBindingSeller OUT,服务导购绑定成功，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            } else {
                AppStore store = storeService.findDefaultStoreByCityId(customer.getCityId());
                customer.setStoreId(store.getId());
                customer.setSalesConsultId(0L);
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
        logger.info("customerCashCoupon CALLED,获取顾客可用产品现金券，入参 userId {},identityType{}", userId, identityType);
        ResultDTO<Object> resultDTO;
        try {
            if (null == userId) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "userId不能为空！", null);
                logger.info("customerCashCoupon OUT,获取顾客可用产品现金券失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            List<CashCouponResponse> cashCouponList = customerService.findCashCouponByCustomerId(userId);
            if (null != cashCouponList && cashCouponList.size() > 0) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, cashCouponList);

            } else {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
            }
            logger.info("customerCashCoupon OUT,获取顾客可用产品现金券成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知异常,绑定导购失败", null);
            logger.warn("customerCashCoupon EXCEPTION,获取顾客可用产品现金券失败，出参 resultDTO:{}", resultDTO);
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
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知异常,绑定导购失败", null);
            logger.warn("customerProductCoupon EXCEPTION,获取顾客可用产品券失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }
}


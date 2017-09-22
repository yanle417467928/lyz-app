package cn.com.leyizhuang.app.web.controller.customer;

import cn.com.leyizhuang.app.core.constant.AppUserType;
import cn.com.leyizhuang.app.core.constant.JwtConstant;
import cn.com.leyizhuang.app.core.constant.SexType;
import cn.com.leyizhuang.app.core.utils.JwtUtils;
import cn.com.leyizhuang.app.foundation.pojo.AppUser;
import cn.com.leyizhuang.app.foundation.pojo.LoginParam;
import cn.com.leyizhuang.app.foundation.pojo.rest.CustomerBindingSellerResponse;
import cn.com.leyizhuang.app.foundation.pojo.rest.CustomerLoginResponse;
import cn.com.leyizhuang.app.foundation.pojo.rest.CustomerRegistResponse;
import cn.com.leyizhuang.app.foundation.service.IAppUserService;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

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
    private IAppUserService appUserService;

    @PostMapping(value = "/login", produces = "application/json;charset=UTF-8")
    public ResultDTO<CustomerLoginResponse> customerLogin(String openId, HttpServletResponse response) {
        ResultDTO<CustomerLoginResponse> resultDTO;
        try {
            if (null == openId || "".equalsIgnoreCase(openId)) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "openId为空！", null);
                return resultDTO;
            }
            AppUser user = appUserService.findByOpenId(openId);
            if (user == null) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "没有找到该用户！",
                        new CustomerLoginResponse(Boolean.FALSE, null, null));
                return resultDTO;
            }
            //拼装accessToken
            String accessToken = JwtUtils.createJWT(String.valueOf(user.getId()), String.valueOf(user.getLoginName()),
                    JwtConstant.EXPPIRES_SECOND * 1000);
            System.out.println(accessToken);
            response.setHeader("token", accessToken);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null,
                    new CustomerLoginResponse(Boolean.TRUE, user.getId(), user.getMobile()));
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("登录出现异常");
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知异常", null);
            return resultDTO;
        }
    }

    @PostMapping(value = "/registry", produces = "application/json;charset=UTF-8")
    public ResultDTO<CustomerRegistResponse> customerRegist(LoginParam loginParam, HttpServletResponse response) {
        ResultDTO<CustomerRegistResponse> resultDTO;
        try {
            if (null == loginParam.getOpenId() || "".equalsIgnoreCase(loginParam.getOpenId())) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "openId不能为空！", new CustomerRegistResponse(Boolean.FALSE, null));
                return resultDTO;
            }
            if (null == loginParam.getPhone() || "".equalsIgnoreCase(loginParam.getPhone())) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "手机号码不能为空！", new CustomerRegistResponse(Boolean.FALSE, null));
                return resultDTO;
            }
            if (null == loginParam.getCityId()) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "城市id不能为空！", new CustomerRegistResponse(Boolean.FALSE, null));
                return resultDTO;
            }
            AppUser user = appUserService.findByOpenId(loginParam.getOpenId());
            if (user != null) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "openId已存在！",
                        new CustomerRegistResponse(Boolean.TRUE, user.getId()));
                return resultDTO;
            }
            AppUser phoneUser = appUserService.findByMobile(loginParam.getPhone());
            if (phoneUser != null) {
                phoneUser.setOpenId(loginParam.getOpenId());
                phoneUser.setNickName(loginParam.getNickName());
                phoneUser.setPicUrl(loginParam.getPicUrl());
                phoneUser.setLoginName(loginParam.getOpenId());
                appUserService.update(phoneUser);
                String accessToken = JwtUtils.createJWT(String.valueOf(phoneUser.getId()), String.valueOf(phoneUser.getLoginName()),
                        JwtConstant.EXPPIRES_SECOND * 1000);
                System.out.println(accessToken);
                response.setHeader("token", accessToken);
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null,
                        new CustomerRegistResponse(Boolean.FALSE, phoneUser.getId()));
                return resultDTO;
            } else {
                AppUser newUser = new AppUser();
                newUser.setOpenId(loginParam.getOpenId());
                newUser.setStatus(Boolean.TRUE);
                newUser.setUserType(AppUserType.MEMBER);
                newUser.setSex((null != loginParam.getSex() && !loginParam.getSex()) ? SexType.FEMALE : SexType.MALE);
                newUser.setNickName(loginParam.getNickName());
                newUser.setPicUrl(loginParam.getPicUrl());
                newUser.setCityId(loginParam.getCityId());
                newUser.setMobile(loginParam.getPhone());
                newUser.setLoginName(loginParam.getOpenId());
                AppUser returnUser = appUserService.save(newUser);
                //拼装accessToken
                String accessToken = JwtUtils.createJWT(String.valueOf(returnUser.getId()), String.valueOf(returnUser.getLoginName()),
                        JwtConstant.EXPPIRES_SECOND * 1000);
                System.out.println(accessToken);
                response.setHeader("token", accessToken);
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null,
                        new CustomerRegistResponse(Boolean.FALSE, returnUser.getId()));
                return resultDTO;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("注册出现异常");
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知异常,注册失败", new CustomerRegistResponse(Boolean.FALSE, null));
            return resultDTO;
        }
    }

    @PostMapping(value = "/binding/seller", produces = "application/json;charset=UTF-8")
    public ResultDTO<CustomerBindingSellerResponse> custonerBindingSeller(Long userId, String guidePhone) {
        ResultDTO<CustomerBindingSellerResponse> resultDTO;
        try {
            if (null == userId) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "userId不能为空！", new CustomerBindingSellerResponse(Boolean.FALSE, null, null));
                return resultDTO;
            }
            if (null == guidePhone || "".equalsIgnoreCase(guidePhone)) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "手机号码不能为空！", new CustomerBindingSellerResponse(Boolean.FALSE, null, null));
                return resultDTO;
            }
            AppUser user = appUserService.findById(userId);
            if (user == null) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户不存在！",
                        new CustomerBindingSellerResponse(Boolean.FALSE, null, null));
                return resultDTO;
            }
            AppUser seller = appUserService.findByMobile(guidePhone);
            if (seller == null) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "导购不存在！",
                        new CustomerBindingSellerResponse(Boolean.FALSE, null, null));
                return resultDTO;
            }
            user.setGuideId(seller.getId());
            user.setGuideName(seller.getName());
            appUserService.update(user);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null,
                    new CustomerBindingSellerResponse(Boolean.TRUE, seller.getName(), seller.getStoreName()));
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("会员绑定导购出现异常");
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知异常,绑定导购失败", new CustomerBindingSellerResponse(Boolean.FALSE, null, null));
            return resultDTO;
        }
    }


}


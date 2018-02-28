package cn.com.leyizhuang.app.web.controller.rest;

import cn.com.leyizhuang.app.core.constant.*;
import cn.com.leyizhuang.app.core.utils.SmsUtils;
import cn.com.leyizhuang.app.foundation.pojo.AppStore;
import cn.com.leyizhuang.app.foundation.pojo.user.AppCustomer;
import cn.com.leyizhuang.app.foundation.pojo.user.AppEmployee;
import cn.com.leyizhuang.app.foundation.pojo.user.CustomerLeBi;
import cn.com.leyizhuang.app.foundation.pojo.user.CustomerPreDeposit;
import cn.com.leyizhuang.app.foundation.service.AppCustomerService;
import cn.com.leyizhuang.app.foundation.service.AppEmployeeService;
import cn.com.leyizhuang.app.foundation.service.AppStoreService;
import cn.com.leyizhuang.app.foundation.service.CommonService;
import cn.com.leyizhuang.app.foundation.service.impl.SmsAccountServiceImpl;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.SmsAccount;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Random;

/**
 * 扫码注册
 * Created by panjie on 2018/2/22.
 */
@RestController
@RequestMapping(value = QrcodeRegisterRestController.PRE_URL, produces = "application/json;charset=utf-8")
public class QrcodeRegisterRestController extends BaseRestController {
    protected final static String PRE_URL = "/rest/qrcode/";

    private final Logger logger = LoggerFactory.getLogger(QrcodeRegisterRestController.class);

    @Autowired
    private CommonService commonService;

    @Autowired
    private AppCustomerService customerService;

    @Autowired
    private AppEmployeeService employeeService;

    @Resource
    private SmsAccountServiceImpl smsAccountService;

    @Autowired
    private AppStoreService appStoreService;

    @PostMapping(value = "/save")
    public ResultDTO<?> save(HttpServletRequest req, String name, String phone, String sellerPhone, String sellerName,
                             String storeName, String code, String workNumber) {

        String smsCode = (String) req.getSession().getAttribute("SMSCODE");
        String smsMobile = (String) req.getSession().getAttribute("SMSMOBILE");

        if (smsCode == null || smsMobile == null) {
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "验证码有误", null);
        }

        if (!smsCode.equals(code) || !phone.equals(smsMobile)) {
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "验证码有误", null);
        }

        AppCustomer appCustomer = null;
        try {
            appCustomer = customerService.findByMobile(phone);
            if (appCustomer != null) {
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "手机号已经注册", null);
            } else {
                AppEmployee appEmployee = employeeService.findByMobile(sellerPhone);

                // 排除分销仓库
                AppStore appStore = appStoreService.findById(appEmployee.getStoreId());

                if (appStore.getStoreType().equals(StoreType.FXCK)) {
                    return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "分销仓库下不能注册会员", null);
                }

                AppCustomer newCustomer = new AppCustomer();

                if (workNumber.equals(appEmployee.getLoginName())) {
                    // 有推荐码 设置为会员
                    newCustomer.setCustomerType(AppCustomerType.MEMBER);
                } else {
                    newCustomer.setCustomerType(AppCustomerType.RETAIL);
                }

                newCustomer.setCreateTime(LocalDateTime.now());
                newCustomer.setCreateType(AppCustomerCreateType.QRCODE_REGISTRY);
                newCustomer.setStatus(Boolean.TRUE);
                newCustomer.setName(name);
                newCustomer.setSex(SexType.SECRET);
                newCustomer.setPicUrl(null);
                newCustomer.setLight(AppCustomerLightStatus.NOT);
                newCustomer.setIsCashOnDelivery(Boolean.FALSE);
                newCustomer.setCityId(appEmployee.getCityId());
                newCustomer.setStoreId(appEmployee.getStoreId());
                newCustomer.setSalesConsultId(appEmployee.getEmpId());
                newCustomer.setMobile(phone);
                newCustomer.setBindingTime(new Date());

                // 保存
                commonService.saveCustomerInfo(newCustomer, new CustomerLeBi(), new CustomerPreDeposit());
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "昵称转码错误，注册失败!", null);
        }
        return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "注册成功！", null);
    }

    @PostMapping(value = "/send/code")
    public ResultDTO<?> sendSmsCode(HttpServletRequest req, String mobile) {
        ResultDTO<?> resultDTO;
        if (null == mobile || mobile.equalsIgnoreCase("") || mobile.trim().length() != 11) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "手机号码不合法！", null);
            logger.info("getQrCode OUT,验证码发送失败，出参 ResultDTO:{}", resultDTO);
            return resultDTO;
        }

        Random random = new Random();
        String smsCode = random.nextInt(900000) + 100000 + "";

        req.getSession().setAttribute("SMSCODE", smsCode);
        req.getSession().setAttribute("SMSMOBILE", mobile);

        String info = "您正在注册乐易装app帐号，验证码为" + smsCode + "，请在页面中输入以完成验证。";
        logger.info("生成的验证码为:{}", smsCode);
        String content;
        try {
            content = URLEncoder.encode(info, "GB2312");
            System.err.println(content);
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "验证码生成失败！", null);
            logger.info("getQrCode EXCEPTION，验证码发送失败，出参 ResultDTO:{}", resultDTO);
            return resultDTO;
        }

        SmsAccount account = smsAccountService.findOne();
        String returnCode;
        try {
            returnCode = SmsUtils.sendMessageQrCode(account.getEncode(), account.getEnpass(), account.getUserName(), mobile, content);
        } catch (IOException e) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "网络故障，短信验证码发送失败！", null);
            logger.info("getQrCode EXCEPTION，验证码发送失败，出参 ResultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        } catch (Exception e) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知错误，短信验证码发送失败！", null);
            logger.info("getQrCode EXCEPTION，验证码发送失败，出参 ResultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
        if (returnCode.equalsIgnoreCase("00")) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
            logger.info("getQrCode OUT，验证码发送成功，出参 ResultDTO:{}", resultDTO);
            return resultDTO;
        } else {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "短信平台故障，验证码发送失败！", null);
            logger.info("getQrCode OUT，验证码发送失败，出参 ResultDTO:{}", resultDTO);
            return resultDTO;
        }
    }
}

package cn.com.leyizhuang.app.web.controller.qrcode;

import cn.com.leyizhuang.app.core.utils.SmsUtils;
import cn.com.leyizhuang.app.foundation.pojo.rest.QrCodeResponse;
import cn.com.leyizhuang.app.foundation.service.Impl.SmsAccountService;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.SmsAccount;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Random;

/**
 * 短信发送
 *
 * @author Richard
 * Created on 2017-09-21 15:50
 **/
@RestController
@RequestMapping(value = "/app/qrcode")
public class QRCodeController {

    private static final Logger logger = LoggerFactory.getLogger(QRCodeController.class);

    @Autowired
    private SmsAccountService smsAccountService;

    @RequestMapping(value = "/send",method = RequestMethod.POST)
    public ResultDTO<QrCodeResponse> getQrCode(String mobile) {
        if (null == mobile || mobile.equalsIgnoreCase("") || mobile.trim().length() != 11) {
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "手机号码不合法！", new QrCodeResponse(null));
        }
        Random random = new Random();
        String smsCode = random.nextInt(900000) + 100000 + "";
        String info = "您的验证码为" + smsCode + "，请在页面中输入以完成验证。";
        logger.info("生成的验证码为:{}", smsCode);
        String content = null;
        try {
            content = URLEncoder.encode(info, "GB2312");
            System.err.println(content);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "验证码生成失败！", new QrCodeResponse(null));
        }
        SmsAccount account = smsAccountService.findOne();
        String returnCode;
        try {
            returnCode = SmsUtils.sendMessageQrCode(account.getEncode(), account.getEnpass(), account.getUserName(), mobile, content);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "网络故障，短信验证码发送失败！", new QrCodeResponse(null));
        }
        if (returnCode.equalsIgnoreCase("00")) {
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, new QrCodeResponse(smsCode));
        } else {
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "短信平台故障，验证码发送失败！", new QrCodeResponse(null));
        }
    }

}
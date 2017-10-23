package cn.com.leyizhuang.app.web.controller.qrcode;

import cn.com.leyizhuang.app.core.utils.RandomUtil;
import cn.com.leyizhuang.app.core.utils.SmsUtils;
import cn.com.leyizhuang.app.foundation.pojo.response.QrCodeResponse;
import cn.com.leyizhuang.app.foundation.service.impl.SmsAccountServiceImpl;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.SmsAccount;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Random;

/**
 * 短信发送
 *
 * @author Richard
 *         Created on 2017-09-21 15:50
 **/
@RestController
@RequestMapping(value = "/app/qrcode")
public class QRCodeController {

    private static final Logger logger = LoggerFactory.getLogger(QRCodeController.class);

    @Resource
    private SmsAccountServiceImpl smsAccountService;

    @RequestMapping(value = "/send", method = RequestMethod.POST)
    public ResultDTO<QrCodeResponse> getQrCode(String mobile) {
        ResultDTO<QrCodeResponse> resultDTO;
        logger.info("getQrCode CALLED,发送验证码，入参 mobile:{}", mobile);
        if (null == mobile || mobile.equalsIgnoreCase("") || mobile.trim().length() != 11) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "手机号码不合法！", new QrCodeResponse(null));
            logger.info("getQrCode OUT,验证码发送失败，出参 ResultDTO:{}", resultDTO);
            return resultDTO;
        }
        Random random = new Random();
        String smsCode = random.nextInt(900000) + 100000 + "";
        String info = "您的验证码为" + smsCode + "，请在页面中输入以完成验证。";
        logger.info("生成的验证码为:{}", smsCode);
        String content;
        try {
            content = URLEncoder.encode(info, "GB2312");
            System.err.println(content);
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "验证码生成失败！", new QrCodeResponse(null));
            logger.info("getQrCode EXCEPTION，验证码发送失败，出参 ResultDTO:{}", resultDTO);
            return resultDTO;
        }
        SmsAccount account = smsAccountService.findOne();
        String returnCode;
        try {
            returnCode = SmsUtils.sendMessageQrCode(account.getEncode(), account.getEnpass(), account.getUserName(), mobile, content);
        } catch (IOException e) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "网络故障，短信验证码发送失败！", new QrCodeResponse(null));
            logger.info("getQrCode EXCEPTION，验证码发送失败，出参 ResultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }catch (Exception e){
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知错误，短信验证码发送失败！", new QrCodeResponse(null));
            logger.info("getQrCode EXCEPTION，验证码发送失败，出参 ResultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
        if (returnCode.equalsIgnoreCase("00")) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, new QrCodeResponse(smsCode));
            logger.info("getQrCode OUT，验证码发送成功，出参 ResultDTO:{}", resultDTO);
            return resultDTO;
        } else {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "短信平台故障，验证码发送失败！", new QrCodeResponse(null));
            logger.info("getQrCode OUT，验证码发送失败，出参 ResultDTO:{}", resultDTO);
            return resultDTO;
        }
    }

    /**
     * 发送提货码 (Pick Up Code)
     * @param mobile
     * @return
     */
    @RequestMapping(value = "/send/pucode", method = RequestMethod.POST)
    public ResultDTO<QrCodeResponse> getPuCode(String mobile) {

        logger.info("getPCode CALLED,发送提货码，入参 mobile:{}", mobile);

        ResultDTO<QrCodeResponse> resultDTO;
        if (null == mobile || mobile.equalsIgnoreCase("") || mobile.trim().length() != 11) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "手机号码不合法！", new QrCodeResponse(null));
            logger.info("getPuCode OUT,发送提货码失败，出参 ResultDTO:{}", resultDTO);
            return resultDTO;
        }
        String smsCode = RandomUtil.randomStrCode(6);
        String info = "您的提货码为" + smsCode + "，请在【门店取货】时出示信息，在此之前请勿删除此信息。为了您的商品安全，请妥善保管提货码。";
        logger.info("生成的提货码为:{}", smsCode);
        String content;
        try {
            content = URLEncoder.encode(info, "GB2312");
            System.err.println(content);
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "提货码生成失败！", new QrCodeResponse(null));
            logger.info("getPuCode EXCEPTION，提货码发送失败，出参 ResultDTO:{}", resultDTO);
            return resultDTO;
        }

        SmsAccount account = smsAccountService.findOne();
        String returnCode;
        try {
            returnCode = SmsUtils.sendMessageQrCode(account.getEncode(), account.getEnpass(), account.getUserName(), mobile, content);
        } catch (IOException e) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "网络故障，短信提货码发送失败！", new QrCodeResponse(null));
            logger.info("getPuCode EXCEPTION，提货码发送失败，出参 ResultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }catch (Exception e){
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知错误，短信验证码发送失败！", new QrCodeResponse(null));
            logger.info("getPuCode EXCEPTION，提货码发送失败，出参 ResultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
        if (returnCode.equalsIgnoreCase("00")) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, new QrCodeResponse(smsCode));
            logger.info("getPuCode OUT，提货码发送成功，出参 ResultDTO:{}", resultDTO);
            return resultDTO;
        } else {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "短信平台故障，提货码发送失败！", new QrCodeResponse(null));
            logger.info("getPuCode OUT，提货码发送失败，出参 ResultDTO:{}", resultDTO);
            return resultDTO;
        }
    }
}

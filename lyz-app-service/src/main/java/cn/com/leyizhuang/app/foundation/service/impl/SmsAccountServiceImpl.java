package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.utils.SmsUtils;
import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.dao.SmsAccountDAO;
import cn.com.leyizhuang.common.foundation.pojo.SmsAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;

/**
 * lyz-app-facade用户服务实现类
 *
 * @author Richard
 * Created on 2017-09-19 11:23
 **/
@Service
public class SmsAccountServiceImpl implements cn.com.leyizhuang.app.foundation.service.SmsAccountService {

    @Autowired
    private SmsAccountDAO smsAccountDAO;

    @Override
    public SmsAccount findOne() {
        return smsAccountDAO.findOne();
    }

    @Override
    public void commonSendSms(String mobile, String content) {
        if (StringUtils.isBlank(mobile) || StringUtils.isBlank(content)) {
            return;
        }
        try {
            String info = URLEncoder.encode(content, "GB2312");

            SmsAccount account = smsAccountDAO.findOne();

            SmsUtils.sendMessageQrCode(account.getEncode(), account.getEnpass(), account.getUserName(), mobile, info);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

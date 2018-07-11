package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.common.foundation.pojo.SmsAccount;


/**
 * 短信账号
 *
 * @author Richard
 * Created on 2017-09-21 14:25
 **/
public interface SmsAccountService {

    /**
     * 获取短信账号
     *
     * @return
     */
    SmsAccount findOne();

    /**
     * 通用短信发送方法
     *
     * @param mobile  手机号
     * @param content 短信内容
     */
    void commonSendSms(String mobile, String content);

    /**
     * GBK短信发送方法
     *
     * @param mobile  手机号
     * @param content 短信内容
     */
    void commonSendGBKSms(String mobile, String content);
}

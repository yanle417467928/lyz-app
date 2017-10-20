package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.SmsAccountDAO;
import cn.com.leyizhuang.common.foundation.pojo.SmsAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
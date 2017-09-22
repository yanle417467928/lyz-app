package cn.com.leyizhuang.app.foundation.service.Impl;

import cn.com.leyizhuang.app.foundation.dao.AppUserDAO;
import cn.com.leyizhuang.app.foundation.dao.SmsAccountDAO;
import cn.com.leyizhuang.app.foundation.pojo.AppUser;
import cn.com.leyizhuang.app.foundation.service.IAppUserService;
import cn.com.leyizhuang.app.foundation.service.ISmsAccountService;
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
public class SmsAccountService implements ISmsAccountService {

    @Autowired
    private SmsAccountDAO smsAccountDAO;

    @Override
    public SmsAccount findOne() {
        return smsAccountDAO.findOne();
    }
}

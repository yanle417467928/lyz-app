package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.AppAdminAppUserDAO;
import cn.com.leyizhuang.app.foundation.pojo.AppUser;
import cn.com.leyizhuang.app.foundation.service.IAppAdminAppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * lyz-app-facade用户服务实现类
 *
 * @author Richard
 * Created on 2017-09-19 11:23
 **/
@Service
public class AppAdminAppUserService implements IAppAdminAppUserService {

    @Autowired
    private AppAdminAppUserDAO appUserDAO;

    @Override
    public void save(AppUser appUser) {
        if(null != appUser){
            appUserDAO.save(appUser);
        }
    }
}

package cn.com.leyizhuang.app.foundation.service.Impl;

import cn.com.leyizhuang.app.foundation.dao.AppUserDAO;
import cn.com.leyizhuang.app.foundation.pojo.AppUser;
import cn.com.leyizhuang.app.foundation.service.IAppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * lyz-app-facade用户服务实现类
 *
 * @author Richard
 * Created on 2017-09-19 11:23
 **/
@Service
public class AppUserService implements IAppUserService {

    @Autowired
    private AppUserDAO appUserDAO;

    @Override
    public AppUser findByLoginName(String loginName) {
        if(null != loginName){
            return appUserDAO.findByLoginName(loginName);
        }
        return null;
    }
}

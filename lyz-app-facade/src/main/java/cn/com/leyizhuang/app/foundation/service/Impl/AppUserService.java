package cn.com.leyizhuang.app.foundation.service.Impl;

import cn.com.leyizhuang.app.foundation.dao.AppUserDAO;
import cn.com.leyizhuang.app.foundation.pojo.AppUser;
import cn.com.leyizhuang.app.foundation.service.IAppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    public AppUser findByOpenId(String openId) {
        if(null != openId && !"".equalsIgnoreCase(openId)){
            return appUserDAO.findByOpenId(openId);
        }
        return null;
    }

    @Override
    public AppUser findByMobile(String mobile) {
        if(null != mobile && !"".equalsIgnoreCase(mobile)){
            return appUserDAO.findByMobile(mobile);
        }
        return null;
    }

    @Override
    @Transactional
    public void update(AppUser newUser) {
        if(null != newUser){
            appUserDAO.update(newUser);
        }
    }

    @Override
    @Transactional
    public AppUser save(AppUser newUser) {
        if(null != newUser){
            appUserDAO.save(newUser);
        }
        return newUser;
    }

    @Override
    public AppUser findById(Long userId) {
        if(null != userId){
            return appUserDAO.findById(userId);
        }
        return null;
    }
}

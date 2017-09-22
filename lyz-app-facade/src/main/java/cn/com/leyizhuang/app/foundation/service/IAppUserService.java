package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.AppUser;

/**
 * lyz-app-facade用户服务接口
 *
 * @author Richard
 * Created on 2017-09-19 11:18
 **/
public interface IAppUserService {

    AppUser findByLoginName(String loginName);

    AppUser findByOpenId(String openId);

    AppUser findByMobile(String mobile);

    void update(AppUser newUser);

    AppUser save(AppUser newUser);

    AppUser findById(Long userId);
}

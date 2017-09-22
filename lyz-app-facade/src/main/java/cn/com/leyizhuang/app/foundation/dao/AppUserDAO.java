package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.AppUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * lyz-app-facade用户数据仓库
 *
 * @author Richard
 * Created on 2017-09-19 11:26
 **/
@Repository
public interface AppUserDAO {
    AppUser findByLoginName(String loginName);

    AppUser findByOpenId(@Param(value = "openId") String openId);

    AppUser findByMobile(String mobile);

    void update(AppUser newUser);

    Long save(AppUser newUser);

    AppUser findById(Long userId);
}

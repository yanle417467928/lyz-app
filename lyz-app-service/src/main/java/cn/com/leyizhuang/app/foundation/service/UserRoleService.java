package cn.com.leyizhuang.app.foundation.service;


import cn.com.leyizhuang.app.foundation.pojo.management.UserRole;

import java.util.List;

/**
 * 用户-角色对应表
 *
 * @author Richard
 * Created on 2017-07-28 15:13
 **/
public interface UserRoleService {
    void save(UserRole userRole);

    List<Long> findRoleIdsByUserId(Long id);

    void deleteUserRoleByUserId(Long id);
}

package cn.com.leyizhuang.app.foundation.service;


import cn.com.leyizhuang.app.foundation.pojo.management.RoleResource;

import java.util.Set;

/**
 * @author Richard
 * Created on 2017-07-28 15:31
 **/
public interface RoleResourceService {
    Set<Long> findResouceIdSetByRoleId(Long roleId);

    void deleteByRoleId(Long id);

    void save(RoleResource roleResource);
}

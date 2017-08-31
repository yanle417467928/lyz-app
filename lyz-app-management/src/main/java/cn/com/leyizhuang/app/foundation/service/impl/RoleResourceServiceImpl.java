package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.RoleResourceDAO;
import cn.com.leyizhuang.app.foundation.service.RoleResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @author Richard
 * Created on 2017-07-28 15:32
 **/
@Service
public class RoleResourceServiceImpl implements RoleResourceService{

    @Autowired
    private RoleResourceDAO roleResourceDAO;

    @Override
    public Set<Long> findResouceIdSetByRoleId(Long roleId) {
        if (null != roleId){
            return roleResourceDAO.findResouceIdSetByRoleId(roleId);
        }
        return null;
    }
}

package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.RoleResourceDAO;
import cn.com.leyizhuang.app.foundation.pojo.RoleResource;
import cn.com.leyizhuang.app.foundation.service.RoleResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    @Transactional
    public void deleteByRoleId(Long id) {
        if(null != id){
            roleResourceDAO.deleteByRoleId(id);
        }
    }

    @Override
    @Transactional
    public void save(RoleResource roleResource) {
        if (null != roleResource){
            roleResourceDAO.save(roleResource);
        }
    }
}

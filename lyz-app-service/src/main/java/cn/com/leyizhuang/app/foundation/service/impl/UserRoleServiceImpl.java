package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.UserRoleDAO;
import cn.com.leyizhuang.app.foundation.pojo.management.UserRole;
import cn.com.leyizhuang.app.foundation.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户-角色对应表API实现
 *
 * @author Richard
 * Created on 2017-07-28 15:15
 **/
@Service
public class UserRoleServiceImpl  implements UserRoleService {

    @Autowired
    private UserRoleDAO userRoleDAO;

    @Override
    @Transactional
    public void save(UserRole userRole) {
        if(null != userRole){
            userRoleDAO.save(userRole);
        }
    }

    @Override
    public List<Long> findRoleIdsByUserId(Long id) {
        if(null != id){
            return userRoleDAO.findRoleIdsByUserId(id);
        }
        return null;
    }


    @Override
    @Transactional
    public void deleteUserRoleByUserId(Long id) {
        if(null != id){
            userRoleDAO.deleteUserRoleByUserId(id);
        }
    }
}

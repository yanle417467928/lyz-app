package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.utils.BeanUtils;
import cn.com.leyizhuang.app.foundation.pojo.User;
import cn.com.leyizhuang.app.foundation.pojo.UserRole;
import cn.com.leyizhuang.app.foundation.pojo.vo.UserVO;
import cn.com.leyizhuang.app.foundation.service.CommonService;
import cn.com.leyizhuang.app.foundation.service.UserRoleService;
import cn.com.leyizhuang.app.foundation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 通用方法实现
 *
 * @author Richard
 * Created on 2017-09-12 15:44
 **/
@Service
public class CommonServiceImpl implements CommonService {

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private UserService userService;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void saveUserAndUserRoleByUserVO(UserVO userVO) {
        User user = BeanUtils.copy(userVO, User.class);
        user.setCreateTime(new Date());
        userService.save(user);
        Long id = user.getId();
        Long[] roles = userVO.getRoleIds();
        if (null != roles && roles.length > 0) {
            UserRole userRole = new UserRole();
            for (Long role : roles) {
                userRole.setUserId(id);
                userRole.setRoleId(role);
                userRoleService.save(userRole);
            }
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateUserAndUserRoleByUserVO(UserVO userVO) {
        if (null != userVO) {
            User user = new User();
            user.setName(userVO.getName());
            user.setLoginName(userVO.getLoginName());
            user.setPassword(userVO.getPassword());
            user.setId(userVO.getId());
            user.setAge(userVO.getAge());
            user.setPhone(userVO.getPhone());
            user.setSex(userVO.getSex());
            user.setStatus(userVO.getStatus());
            user.setUserType(userVO.getUserType());
            userService.update(user);
            userRoleService.deleteUserRoleByUserId(userVO.getId());
            Long[] roles = userVO.getRoleIds();
            if (null != roles && roles.length > 0) {
                UserRole userRole = new UserRole();
                for (Long role : roles) {
                    userRole.setUserId(userVO.getId());
                    userRole.setRoleId(role);
                    userRoleService.save(userRole);
                }
            }
        }
    }


}

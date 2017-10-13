package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.UserDAO;
import cn.com.leyizhuang.app.foundation.dao.UserRoleDAO;
import cn.com.leyizhuang.app.foundation.pojo.management.User;
import cn.com.leyizhuang.app.foundation.vo.UserVO;
import cn.com.leyizhuang.app.foundation.service.UserRoleService;
import cn.com.leyizhuang.app.foundation.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 管理员服务实现类
 *
 * @author Richard
 * Created on 2017-07-28 14:54
 **/
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private UserRoleDAO userRoleDAO;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private UserService userService;

    @Override
    public List<User> selectByLoginName(UserVO userVO) {
        if (null != userVO.getLoginName()) {
            return userDAO.selectByLoginName(userVO);
        }
        return null;
    }

    @Override
    public PageInfo<User> queryPage(Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<User> userList = userDAO.queryList();
        return new PageInfo<>(userList);
    }

    @Override
    public PageInfo<UserVO> queryPageVO(Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<UserVO> userList = userDAO.queryListVO();
        return new PageInfo<>(userList);
    }

    @Override
    public User queryById(Long id) {
        if (null != id) {
            return userDAO.queryById(id);
        }
        return null;
    }

    @Override
    @Transactional
    public void save(User user) {
        if (null != user) {
            userDAO.save(user);
        }
    }

    @Override
    public void delete(Long id) {
        if (null != id) {
            userDAO.delete(id);
        }
    }

    @Override
    @Transactional
    public void update(User user) {
        if (null != user) {
            userDAO.update(user);
        }
    }

    @Override
    public Boolean existsByLoginName(String loginName) {
        if (null != loginName) {
            return userDAO.existsByLoginName(loginName);
        }
        return false;
    }

    @Override
    public User quereyByLoginName(UserVO userVO) {
        if (null != userVO.getLoginName()) {
            return userDAO.quereyByLoginName(userVO);
        }
        return null;
    }


}

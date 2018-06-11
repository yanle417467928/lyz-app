package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.management.User;
import cn.com.leyizhuang.app.foundation.vo.UserVO;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 管理员服务API
 *
 * @author Richard
 * Created on 2017-07-28 14:51
 **/
public interface UserService {
    List<User> selectByLoginName(UserVO userVO);

    PageInfo<User> queryPage(Integer page, Integer size);

    PageInfo<UserVO> queryPageVO(Integer page, Integer size);

    PageInfo<UserVO> queryPageVOWithKeywords(Integer page, Integer size, String keywords,String identityType,String enable);

    User queryById(Long id);

    void save(User user);

    void delete(Long id);

    void update(User user);

    Boolean existsByLoginName(String loginName);

    User queryByLoginName(UserVO userVO);

    User findByLoginName(String loginName);

}

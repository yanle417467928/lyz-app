package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.User;
import cn.com.leyizhuang.app.foundation.pojo.vo.UserVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 管理员DAO层
 *
 * @author Richard
 * Created on 2017-07-28 14:55
 **/
@Repository
public interface  UserDAO {
    List<User> selectByLoginName(UserVO userVO);

    List<User> queryList();

    List<UserVO> queryListVO();

    User queryById(@Param(value = "id") Long id);

    void save(User user);
}

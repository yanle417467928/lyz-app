package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.management.User;
import cn.com.leyizhuang.app.foundation.vo.UserVO;
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
public interface UserDAO {
    List<User> selectByLoginName(UserVO userVO);

    List<User> queryList();

    List<UserVO> queryListVO();

    User queryById(@Param(value = "id") Long id);

    void save(User user);

    void delete(@Param(value = "id") Long id);

    void update(User user);

    Boolean existsByLoginName(@Param(value = "loginName") String loginName);

    void saveUserVO(UserVO userVO);

    User queryByLoginName(UserVO userVO);

    User findByLoginName(@Param(value = "loginName") String loginName);

    List<UserVO> queryUserVOListWithKeywords(@Param(value = "keywords") String keywords,@Param(value = "identityType") String identityType, @Param(value = "enable") String enable);
}

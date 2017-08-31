package cn.com.leyizhuang.app.foundation.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 管理员-角色对应表DAO
 *
 * @author Richard
 * Created on 2017-07-28 15:18
 **/
@Repository
public interface UserRoleDAO {
    List<Long> selectRoleIdListByUserId(@Param("userId") Long userId);
}

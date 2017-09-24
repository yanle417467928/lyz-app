package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.RoleResource;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * @author Richard
 * Created on 2017-07-28 15:33
 **/
@Repository
public interface RoleResourceDAO {

    void deleteByRoleId(@Param(value = "id") Long id);

    void save(RoleResource roleResource);

    Set<Long> findResouceIdSetByRoleId(Long roleId);
}

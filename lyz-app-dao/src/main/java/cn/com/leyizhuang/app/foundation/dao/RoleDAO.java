package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.Resource;
import cn.com.leyizhuang.app.foundation.pojo.Role;
import cn.com.leyizhuang.app.foundation.vo.ResourceVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 角色DAO
 *
 * @author Richard
 * Created on 2017-07-28 15:27
 **/
@Repository
public interface RoleDAO {

    List<Map<Long, String>> selectResourceListByRoleId(@Param("id") Long id);

    Role selectById(Long roleId);

    List<Role> queryList();

    void save(Role role);

    List<ResourceVO> findAllResource();

    void batchRemove(List<Long> longs);

    Role queryById(Long id);

    void update(Role role);

    List<Role> findByStatus(@Param(value = "status") Boolean status);

    List<Resource> selectResourceListByRoleIdList(List<Long> roleIdList);
}

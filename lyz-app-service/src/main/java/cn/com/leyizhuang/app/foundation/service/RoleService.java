package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.management.Role;
import cn.com.leyizhuang.app.foundation.vo.ResourceVO;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 角色API
 *
 * @author Richard
 * Created on 2017-07-28 15:07
 **/
public interface RoleService {
    Map<String, Set<String>> selectResourceMapByUserId(Long userId);

    PageInfo<Role> queryPage(Integer page, Integer size, String keywords);

    void save(Role role);

    void updateRoleResource(Long id, String[] resourceIds);

    List<ResourceVO> findAllResource();

    void batchRemove(List<Long> longs);

    Role queryById(Long id);

    void update(Role role);

    List<Role> findByStatus(Boolean status);
}

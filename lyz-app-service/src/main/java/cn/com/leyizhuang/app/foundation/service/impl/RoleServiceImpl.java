package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.dao.RoleDAO;
import cn.com.leyizhuang.app.foundation.dao.UserRoleDAO;
import cn.com.leyizhuang.app.foundation.pojo.management.Role;
import cn.com.leyizhuang.app.foundation.pojo.management.RoleResource;
import cn.com.leyizhuang.app.foundation.service.ResourceService;
import cn.com.leyizhuang.app.foundation.service.RoleResourceService;
import cn.com.leyizhuang.app.foundation.service.RoleService;
import cn.com.leyizhuang.app.foundation.vo.ResourceVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 角色API实现类
 *
 * @author Richard
 * Created on 2017-07-28 15:09
 **/
@Service
public class RoleServiceImpl implements RoleService {

    private UserRoleDAO userRoleDAO;

    @Autowired
    public void setUserRoleDAO(UserRoleDAO userRoleDAO) {
        this.userRoleDAO = userRoleDAO;
    }

    private RoleDAO roleDAO;

    @Autowired
    public void setRoleDAO(RoleDAO roleDAO) {
        this.roleDAO = roleDAO;
    }

    @Autowired
    private RoleResourceService roleResourceService;

    @Autowired
    private ResourceService resourceService;

    @Override
    public Map<String, Set<String>> selectResourceMapByUserId(Long userId) {
        Map<String, Set<String>> resourceMap = new HashMap<>();
        List<Long> roleIdList = userRoleDAO.selectRoleIdListByUserId(userId);
        Set<String> urlSet = new HashSet<String>();
        Set<String> roles = new HashSet<>();
        for (Long roleId : roleIdList) {
            List<Map<Long, String>> resourceList = roleDAO.selectResourceListByRoleId(roleId);
            if (resourceList != null) {
                for (Map<Long, String> map : resourceList) {
                    if (map != null && map.size() > 0) {
                        if (StringUtils.isNotBlank(map.get("url"))) {
                            urlSet.add(map.get("url"));
                        }
                    }
                }
            }
            Role role = roleDAO.selectById(roleId);
            if (role != null) {
                roles.add(role.getName());
            }
        }
        resourceMap.put("urls", urlSet);
        resourceMap.put("roles", roles);
        return resourceMap;
    }

    @Override
    public PageInfo<Role> queryPage(Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<Role> roleList = roleDAO.queryList();
        return new PageInfo<>(roleList);
    }

    @Override
    public void save(Role role) {
        if (null != role) {
            roleDAO.save(role);
        }
    }

    @Override
    @Transactional
    public void updateRoleResource(Long id, String[] resourceIds) {
        RoleResource roleResource = new RoleResource();

        List<Long> parentIdList =resourceService.queryParentIdsByIds(resourceIds);
        roleResourceService.deleteByRoleId(id);
        for (String resourceId : resourceIds) {
            if (Long.parseLong(resourceId) > 0) {
                roleResource = new RoleResource();
                roleResource.setRoleId(id);
                roleResource.setResourceId(Long.parseLong(resourceId));
                roleResourceService.save(roleResource);
            }
        }
        for (Long resourceId : parentIdList) {
            if (resourceId > 0) {
                roleResource = new RoleResource();
                roleResource.setRoleId(id);
                roleResource.setResourceId(resourceId);
                roleResourceService.save(roleResource);
            }
        }
    }

    @Override
    public List<ResourceVO> findAllResource() {
        return roleDAO.findAllResource();
    }

    @Override
    public void batchRemove(List<Long> longs) {
        if (null != longs && longs.size() > 0) {
            roleDAO.batchRemove(longs);
        }
    }

    @Override
    public Role queryById(Long id) {
        if (null != id) {
            return roleDAO.queryById(id);
        }
        return null;
    }

    @Override
    public void update(Role role) {
        if (null != role.getId()) {
            roleDAO.update(role);
        }
    }

    @Override
    public List<Role> findByStatus(Boolean status) {
        if(null != status){
            return roleDAO.findByStatus(status);
        }
        return null;
    }
}

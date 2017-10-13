package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.ResourceDAO;
import cn.com.leyizhuang.app.foundation.dao.RoleDAO;
import cn.com.leyizhuang.app.foundation.dao.UserRoleDAO;
import cn.com.leyizhuang.app.foundation.pojo.management.Resource;
import cn.com.leyizhuang.app.foundation.vo.ResourceVO;
import cn.com.leyizhuang.app.core.config.shiro.ShiroUser;
import cn.com.leyizhuang.app.foundation.service.ResourceService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 资源API实现
 *
 * @author Richard
 * Created on 2017-08-02 14:17
 **/
@Service
public class ResourceServiceImpl implements ResourceService {

    private static final int RESOURCE_MENU = 0; // 菜单
    private static final int RESOURCE_BUTTON = 1; // 菜单

    @Autowired
    private ResourceDAO resourceDAO;

    @Autowired
    private UserRoleDAO userRoleDAO;

    @Autowired
    private RoleDAO roleDAO;

    @Override
    public PageInfo<Resource> queryPage(Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<Resource> resourceList = resourceDAO.queryList();
        return new PageInfo<>(resourceList);
    }

    @Override
    public List<Resource> queryByPid(Long i) {
        if (null != i) {
            return resourceDAO.queryByPid(i);
        }
        return null;
    }

    @Override
    public void save(Resource resource) {
        if (null != resource) {
            resourceDAO.save(resource);
        }
    }

    @Override
    public Resource queryById(Long id) {
        if (null != id) {
            return resourceDAO.queryById(id);
        }
        return null;
    }

    @Override
    public PageInfo<ResourceVO> queryPageVO(Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<ResourceVO> resourceList = resourceDAO.queryListVO();
        return new PageInfo<>(resourceList);
    }

    @Override
    public void update(ResourceVO resourceVO) {
        if (null != resourceVO) {
            resourceDAO.updateVO(resourceVO);
        }
    }

    @Override
    public void update(Resource resource) {
        if (null != resource) {
            resourceDAO.update(resource);
        }
    }

    @Override
    public Long countByPId(Long id) {
        if (null != id) {
            return resourceDAO.countByPId(id);
        }
        return null;
    }

    @Override
    public void batchRemove(List<Long> longs) {
        if (null != longs && longs.size() > 0) {
            resourceDAO.batchRemove(longs);
        }
    }

    /**
     * 加载首页左侧导航菜单树
     *
     * @param shiroUser
     * @return
     */
    @Override
    public List<ResourceVO> loadAdminMenuTree(ShiroUser shiroUser) {
        List<ResourceVO> appAdminMenuListVO = new ArrayList<>();
        List<Resource> resourceList = new ArrayList<>();
        // shiro中缓存的用户角色
        Set<String> roles = shiroUser.getRoles();
        if (roles != null && roles.size() > 0) {
            // 如果有超级管理员权限
            if (roles.contains("超级管理员")||roles.contains("admin")) {
                resourceList = this.selectByType(RESOURCE_MENU);
            } else {
                // 普通用户
                List<Long> roleIdList = userRoleDAO.selectRoleIdListByUserId(shiroUser.getId());
                if (roleIdList != null && roleIdList.size() > 0) {
                    resourceList = roleDAO.selectResourceListByRoleIdList(roleIdList);
                }
            }
            appAdminMenuListVO = parseResourceList2ResourceVOList(resourceList);
        }
        return appAdminMenuListVO;
    }

    public List<ResourceVO> parseResourceList2ResourceVOList(List<Resource> resourceList) {
        List<ResourceVO> resourceVOList = new ArrayList<>();
        if (resourceList == null) {
            return resourceVOList;
        } else {
            List<ResourceVO> allMenuVOList = new ArrayList<>();
            for (Resource resource : resourceList) {
                ResourceVO resourceVO = new ResourceVO();
                resourceVO.setId(resource.getRsId());
                resourceVO.setParentResourceId(resource.getPid());
                resourceVO.setResourceName(resource.getName());
                resourceVO.setIcon(resource.getIcon());
                resourceVO.setUrl(resource.getUrl());
                resourceVO.setSeq(resource.getSortId());
                allMenuVOList.add(resourceVO);
            }
            // 先筛选出所有的顶层菜单
            resourceVOList = allMenuVOList.stream().filter(menuVO -> 0 == menuVO.getParentResourceId())
                    .collect(Collectors.toList());
            // 再通过Stream的过滤器获取顶层菜单的子菜单
            resourceVOList.forEach(topMenuVO -> {
                List<ResourceVO> children = allMenuVOList.stream()
                        .filter(menuVO -> topMenuVO.getId().equals(menuVO.getParentResourceId()) )
                        .collect(Collectors.toList());
                topMenuVO.setChildren(children);
            });
            // 最后按照sortId进行排序
            resourceVOList.sort(Comparator.comparing(ResourceVO::getSeq));
            return resourceVOList;
        }
    }

    @Override
    public List<Resource> selectByType(int type) {
        return resourceDAO.selectByType(type);
    }

    @Override
    public ResourceVO queryVOById(Long id) {
        if (null != id){
            return resourceDAO.queryVOById(id);
        }
        return null;
    }

    @Override
    public List<Long> queryParentIdsByIds(String[] resourceIds) {
        if(null != resourceIds && resourceIds.length>0){
            return resourceDAO.queryParentIdsByIds(resourceIds);
        }
        return null;
    }
}

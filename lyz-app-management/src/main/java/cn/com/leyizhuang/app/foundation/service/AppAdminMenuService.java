package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.AppAdminMenuDO;
import cn.com.leyizhuang.app.foundation.pojo.dto.AppAdminMenuDTO;
import cn.com.leyizhuang.app.foundation.pojo.vo.AppAdminMenuVO;
import cn.com.leyizhuang.common.foundation.service.BaseService;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * App后台管理菜单服务
 *
 * @author Richard
 *         Created on 2017-05-08 14:23
 **/
public interface AppAdminMenuService extends BaseService<AppAdminMenuDO>{
    PageInfo<AppAdminMenuDO> queryPage(Integer page, Integer size);

    List<AppAdminMenuVO> loadAdminMenuTree();

    PageInfo<AppAdminMenuDO> loadTopMenu(Integer page, Integer size);

    void add(AppAdminMenuDTO menuDTO);

    AppAdminMenuDO queryMenuById(Long id);

    void update(AppAdminMenuDTO menuDTO);

    List<AppAdminMenuDO> queryByParentId(Long parentId);

    Boolean existsByTitleAndIdNot(String title, Long id);

    Long countByParentId(Long id);

}

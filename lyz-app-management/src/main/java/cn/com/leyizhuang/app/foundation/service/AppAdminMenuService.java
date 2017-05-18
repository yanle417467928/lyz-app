package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.AppAdminMenuDO;
import cn.com.leyizhuang.app.foundation.pojo.vo.AppAdminMenuListVO;
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

    List<AppAdminMenuListVO> loadAdminMenuTree();

    PageInfo<AppAdminMenuDO> loadTopMenu(Integer page, Integer size);

    void add(AppAdminMenuDO appAdminMenuDO);

    AppAdminMenuDO queryMenuById(Long id);

    void update(AppAdminMenuDO appAdminMenuDO);
}

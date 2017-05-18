package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.AppAdminMenuDAO;
import cn.com.leyizhuang.app.foundation.pojo.AppAdminMenuDO;
import cn.com.leyizhuang.app.foundation.pojo.vo.AppAdminMenuListVO;
import cn.com.leyizhuang.app.foundation.service.AppAdminMenuService;
import cn.com.leyizhuang.common.foundation.service.impl.BaseServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * AppMenuService实现类
 *
 * @author Richard
 *         Created on 2017-05-08 14:30
 **/
@Service
@Transactional
public class AppAdminMenuServiceImpl extends BaseServiceImpl<AppAdminMenuDO> implements AppAdminMenuService {

    private AppAdminMenuDAO menuDAO;

    @Autowired
    public AppAdminMenuServiceImpl(AppAdminMenuDAO menuDAO) {
        super(menuDAO);
        this.menuDAO = menuDAO;
    }

    @Override
    public PageInfo<AppAdminMenuDO> queryPage(Integer page, Integer size) {
        PageHelper.startPage(page,size);
        List<AppAdminMenuDO> menuDOList = menuDAO.queryList();
        return new PageInfo<>(menuDOList);
    }

    @Override
    public List<AppAdminMenuListVO> loadAdminMenuTree() {
        List<AppAdminMenuListVO> appAdminMenuListVOList = new ArrayList<>();
        //获取所有的菜单DO
        List<AppAdminMenuDO> appAdminMenuDOList = menuDAO.queryList();
        if (null != appAdminMenuDOList){
            List<AppAdminMenuListVO> allMenuVOList = AppAdminMenuListVO.transform(appAdminMenuDOList);
            // 先筛选出所有的顶层菜单
            appAdminMenuListVOList = allMenuVOList.stream().filter(menuVO -> 0 == menuVO.getParentId())
                    .collect(Collectors.toList());
            // 再通过Stream的过滤器获取顶层菜单的子菜单
            appAdminMenuListVOList.forEach(topMenuVO -> {
                List<AppAdminMenuListVO> children = allMenuVOList.stream()
                        .filter(menuVO -> topMenuVO.getId().equals(menuVO.getParentId()))
                        .collect(Collectors.toList());
                topMenuVO.setChildren(children);
            });

            // 最后按照sortId进行排序
            appAdminMenuListVOList.sort(Comparator.comparing(AppAdminMenuListVO::getSortId));
        }
        return appAdminMenuListVOList;
    }

    @Override
    public PageInfo<AppAdminMenuDO> loadTopMenu(Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<AppAdminMenuDO> topMenuDOList = menuDAO.queryByParentId(0L);
        return new PageInfo<>(topMenuDOList);
    }

    @Override
    public void add(AppAdminMenuDO appAdminMenuDO) {
        if(null != appAdminMenuDO){
            menuDAO.add(appAdminMenuDO);
        }
    }

    @Override
    public AppAdminMenuDO queryMenuById(Long id) {
        return menuDAO.queryMenuById(id);
    }

    @Override
    public void update(AppAdminMenuDO appAdminMenuDO) {
        menuDAO.update(appAdminMenuDO);
    }

}

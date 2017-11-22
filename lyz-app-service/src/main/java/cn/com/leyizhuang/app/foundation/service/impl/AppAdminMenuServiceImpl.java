package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.constant.AppAdminMenuType;
import cn.com.leyizhuang.app.core.exception.ChangeMenuTypeException;
import cn.com.leyizhuang.app.foundation.dao.AppAdminMenuDAO;
import cn.com.leyizhuang.app.foundation.dto.AppAdminMenuDTO;
import cn.com.leyizhuang.app.foundation.pojo.AppAdminMenuDO;
import cn.com.leyizhuang.app.foundation.service.AppAdminMenuService;
import cn.com.leyizhuang.app.foundation.vo.AppAdminMenuVO;
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
    public List<AppAdminMenuVO> loadAdminMenuTree() {
        List<AppAdminMenuVO> appAdminMenuListVO = new ArrayList<>();
        //获取所有的菜单DO
        List<AppAdminMenuDO> appAdminMenuDOList = menuDAO.queryList();
        if (null != appAdminMenuDOList){
            List<AppAdminMenuVO> allMenuVOList = AppAdminMenuVO.transform(appAdminMenuDOList);
            // 先筛选出所有的顶层菜单
            appAdminMenuListVO = allMenuVOList.stream().filter(menuVO -> 0 == menuVO.getParentId())
                    .collect(Collectors.toList());
            // 再通过Stream的过滤器获取顶层菜单的子菜单
            appAdminMenuListVO.forEach(topMenuVO -> {
                List<AppAdminMenuVO> children = allMenuVOList.stream()
                        .filter(menuVO -> topMenuVO.getId().equals(menuVO.getParentId()))
                        .collect(Collectors.toList());
                topMenuVO.setChildren(children);
            });

            // 最后按照sortId进行排序
            appAdminMenuListVO.sort(Comparator.comparing(AppAdminMenuVO::getSortId));
        }
        return appAdminMenuListVO;
    }

    @Override
    public PageInfo<AppAdminMenuDO> loadTopMenu(Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<AppAdminMenuDO> topMenuDOList = menuDAO.queryByParentId(0L);
        return new PageInfo<>(topMenuDOList);
    }

    @Override
    public void add(AppAdminMenuDTO menuDTO) {
        AppAdminMenuDO menuDO = transformDTO(menuDTO);
        menuDO.setCreatorInfoByManager(0L);
        menuDAO.add(menuDO);
    }


    @Override
    public AppAdminMenuDO queryMenuById(Long id) {
        return menuDAO.queryMenuById(id);
    }

    @Override
    public void update(AppAdminMenuDTO menuDTO) {
        AppAdminMenuDO menuDO = transformDTO(menuDTO);
        menuDO.setModifierInfoByManager(0L);
        if (0L != menuDO.getParentId()) {
            Long count = menuDAO.countByParentId(menuDO.getId());
            if (count > 0) {
                throw new ChangeMenuTypeException("该菜单下还存在其它子菜单，不能将其修改为子菜单挂载在另一父级菜单下");
            }
        }
        menuDAO.update(menuDO);
    }

    @Override
    public List<AppAdminMenuDO> queryByParentId(Long parentId) {
        if(null != parentId){
            return menuDAO.queryByParentId(parentId);
        }
        return null;
    }

    @Override
    public Boolean existsByTitleAndIdNot(String title, Long id) {
        return menuDAO.existsByTitleAndIdNot(title, id);
    }

    @Override
    public Long countByParentId(Long id) {
        if (null == id){
            return 0L;
        }
        return menuDAO.countByParentId(id);
    }


    private AppAdminMenuDO transformDTO(AppAdminMenuDTO menuDTO) {
        AppAdminMenuDO menuDO = new AppAdminMenuDO();
        menuDO.setId(menuDTO.getId());
        menuDO.setTitle(menuDTO.getTitle());
        menuDO.setIconStyle(menuDTO.getIconStyle());

        menuDO.setLinkUri(menuDTO.getLinkUri());
        menuDO.setSortId(menuDTO.getSortId());
        menuDO.setReferenceTable(menuDTO.getReferenceTable());

        Long parentId = menuDTO.getParentId();
        menuDO.setParentId(parentId);

        if (0L == parentId) {
            menuDO.setType(AppAdminMenuType.PARENT);
        } else {
            menuDO.setType(AppAdminMenuType.CHILD);
        }

        return menuDO;
    }

}

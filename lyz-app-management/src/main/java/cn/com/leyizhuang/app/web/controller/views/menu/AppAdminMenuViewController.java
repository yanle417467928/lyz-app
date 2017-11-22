package cn.com.leyizhuang.app.web.controller.views.menu;

import cn.com.leyizhuang.app.core.constant.AppAdminMenuType;
import cn.com.leyizhuang.app.foundation.pojo.AppAdminMenuDO;
import cn.com.leyizhuang.app.foundation.service.AppAdminMenuService;
import cn.com.leyizhuang.app.foundation.vo.AppAdminMenuVO;
import cn.com.leyizhuang.app.web.controller.BaseController;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author CrazyApeDX
 *         Created on 2017/5/6.
 */
@Controller
@RequestMapping(value = AppAdminMenuViewController.PRE_URL, produces = "text/html;charset=utf-8")
public class AppAdminMenuViewController extends BaseController {

    protected final static String PRE_URL = "/views/admin/menu";

    public static final Logger LOGGER = LoggerFactory.getLogger(AppAdminMenuViewController.class);

    @Autowired
    private AppAdminMenuService menuService;

    @GetMapping(value = "/page")
    public String menuList(HttpServletRequest request, ModelMap map, Integer page, Integer size) {
        page = (null == page) ? CommonGlobal.PAGEABLE_DEFAULT_PAGE : page;
        size = null == size ? CommonGlobal.PAGEABLE_DEFAULT_SIZE : size;
        PageInfo<AppAdminMenuDO> menuDOPage = menuService.loadTopMenu(page, size);
        map.addAttribute("menuDOPage", menuDOPage);
        return "/views/menu/menu_page";
    }

    /**
     * 跳转到添加菜单页面
     * @return
     */
    @GetMapping(value = "/add")
    public String add(Model model){
        List<AppAdminMenuDO> menuDOList = menuService.queryByParentId(0L);
        List<AppAdminMenuVO> menuVOList = AppAdminMenuVO.transform(menuDOList);
        model.addAttribute("menuVOList",menuVOList);
        return "/views/menu/menu_add";
    }

    /**
     * 根据ID查询菜单，返回修改页面
     * @param id    菜单ID
     * @param map
     * @return
     */
    @GetMapping(value = "/select/{id}")
    public String selectMenu(@PathVariable Long id,ModelMap map){
        AppAdminMenuDO appAdminMenuDO = menuService.queryMenuById(id);
        String type = "";
        if (appAdminMenuDO.getType().equals(AppAdminMenuType.PARENT)){
            type = "一级菜单";
        }else {
            type = "二级菜单";
        }

        map.addAttribute(appAdminMenuDO);
        map.addAttribute("type",type);
        return "/views/menu/menu_update";
    }


    /**
     * 查看菜单详情方法，返回详情页面
     * @param id    菜单ID
     * @param map
     * @return
     */
    @RequestMapping(value = "/details/{id}")
    public String menuDetails(@PathVariable Long id,ModelMap map){
        AppAdminMenuDO appAdminMenuDO = menuService.queryMenuById(id);
        String type = "";
        if (appAdminMenuDO.getType().equals(AppAdminMenuType.PARENT)){
            type = "一级菜单";
        }else {
            type = "二级菜单";
        }

        map.addAttribute(appAdminMenuDO);
        map.addAttribute("type",type);
        return "/views/menu/menu_details";
    }


    @GetMapping(value = "/info/{id}")
    public String menuInfo(HttpServletRequest request, ModelMap map, @PathVariable(value = "id")Long id) {
        AppAdminMenuDO menuDO = menuService.queryById(id);
        map.addAttribute("menuDO", menuDO);
        return "/views/menu_info";
    }

    @GetMapping(value = "/edit/{id}")
    public String menuEdit(ModelMap map, @PathVariable(value = "id")Long id) {
        if (!id.equals(0L)) {
            AppAdminMenuDO menuDO = menuService.queryById(id);
            if (null == menuDO) {
                LOGGER.warn("跳转到修改菜单的页面失败，MenuDO(id = {}) == null", id);
                error404();
                return "/error/404";
            } else {
                map.addAttribute("menuDO", menuDO);
            }
        }

        List<AppAdminMenuDO> menuDOList = menuService.queryByParentId(0L);
        List<AppAdminMenuVO> menuVOList = AppAdminMenuVO.transform(menuDOList);

        menuVOList = menuVOList
                .stream().
                        filter(menuVO -> !id.equals(menuVO.getId()))
                .collect(Collectors.toList());

        map.addAttribute("menuVOList", menuVOList);

        return "/views/menu/menu_edit";
    }
}

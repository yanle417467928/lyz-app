package cn.com.leyizhuang.app.web.controller.views;

import cn.com.leyizhuang.app.foundation.pojo.AppAdminMenuDO;
import cn.com.leyizhuang.app.foundation.service.AppAdminMenuService;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author CrazyApeDX
 *         Created on 2017/5/6.
 */
@Controller
@RequestMapping(value = AppAdminMenuViewController.PRE_URL, produces = "text/html;charset=utf-8")
public class AppAdminMenuViewController {

    protected final static String PRE_URL = "/views/admin/menu";

    @Autowired
    private AppAdminMenuService menuService;

    @GetMapping(value = "/page")
    public String menuList(HttpServletRequest request, ModelMap map, Integer page, Integer size) {
        page = null == page ? CommonGlobal.PAGEABLE_DEFAULT_PAGE : page;
        size = null == size ? CommonGlobal.PAGEABLE_DEFAULT_SIZE : size;
        PageInfo<AppAdminMenuDO> menuDOPage = menuService.loadTopMenu(page, size);
        map.addAttribute("menuDOPage", menuDOPage);
        return "/views/menu_page";
    }

    @GetMapping(value = "/info/{id}")
    public String menuInfo(HttpServletRequest request, ModelMap map, @PathVariable(value = "id")Long id) {
        AppAdminMenuDO menuDO = menuService.queryById(id);
        map.addAttribute("menuDO", menuDO);
        return "/views/menu_info";
    }

    @GetMapping(value = "/edit/{id}")
    public String menuEdit(HttpServletRequest request, ModelMap map, @PathVariable(value = "id")Long id) {
        AppAdminMenuDO menuDO = menuService.queryById(id);
        map.addAttribute("menuDO", menuDO);
        return "/views/menu_edit";
    }
}

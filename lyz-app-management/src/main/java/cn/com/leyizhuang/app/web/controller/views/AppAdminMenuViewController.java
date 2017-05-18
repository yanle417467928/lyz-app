package cn.com.leyizhuang.app.web.controller.views;

import cn.com.leyizhuang.app.core.constant.AppAdminMenuType;
import cn.com.leyizhuang.app.foundation.pojo.AppAdminMenuDO;
import cn.com.leyizhuang.app.foundation.service.AppAdminMenuService;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

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

    /**
     * 跳转到添加菜单页面
     * @return
     */
    @GetMapping(value = "/add")
    public String add(){
        return "/views/menu/menu_add";
    }

    /**
     * 添加菜单方法，返回菜单列表页面
     * @param title 菜单标题
     * @param iconStyle 图标样式
     * @param linkUri   链接地址
     * @param sortId    排序号
     * @param type  菜单类型
     * @param referenceTable    相关数据表
     * @param parentID  父节点ID
     * @param parentTitle   父节点标题
     * @return
     */
    @PostMapping(value = "/add")
    public String addMenu(String title,String iconStyle,String linkUri,Integer sortId,String type,String referenceTable,Long parentID,String parentTitle){
    AppAdminMenuDO menuDO =  new AppAdminMenuDO();
    menuDO.setCreatorId(0L);
    menuDO.setCreatorType("MANAGER");
    menuDO.setCreateTime(LocalDateTime.now());
    menuDO.setModifierId(0L);
    menuDO.setModifierType("MANAGER");
    menuDO.setModifyTime(LocalDateTime.now());


    menuDO.setTitle(title);
    menuDO.setIconStyle(iconStyle);
    menuDO.setLinkUri(linkUri);
    menuDO.setSortId(sortId);
    menuDO.setReferenceTable(referenceTable);
    AppAdminMenuDO.ParentNode parent = new AppAdminMenuDO.ParentNode();
    parent.setId(parentID);
    parent.setTitle(parentTitle);
    menuDO.setParent(parent);
    if(null != type){
        menuDO.setType(Enum.valueOf(AppAdminMenuType.class,type));
    }
    menuService.add(menuDO);
    return "redirect:/views/admin/menu/page";
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
     * 修改菜单信息方法，返回菜单列表
     * @param title 菜单标题
     * @param iconStyle 图标样式
     * @param linkUri   链接地址
     * @param sortId    排序号
     * @param type  菜单类型
     * @param referenceTable    相关数据表
     * @param parentID  父节点ID
     * @param parentTitle   父节点标题
     * @return
     */
    @PostMapping(value = "/update")
    public String updateMenu(Long id,String title,String iconStyle,String linkUri,Integer sortId,String type,String referenceTable,Long parentID,String parentTitle){
        AppAdminMenuDO appAdminMenuDO = menuService.queryMenuById(id);
        if (null != title){
            appAdminMenuDO.setTitle(title);
        }
        if (null != iconStyle){
            appAdminMenuDO.setIconStyle(iconStyle);
        }
        if (null != linkUri){
            appAdminMenuDO.setLinkUri(linkUri);
        }
        if (null != sortId){
            appAdminMenuDO.setSortId(sortId);
        }
        if (null != type){
            appAdminMenuDO.setType(Enum.valueOf(AppAdminMenuType.class,type));
        }
        if (null != referenceTable){
            appAdminMenuDO.setReferenceTable(referenceTable);
        }
        if (null != parentID){
            appAdminMenuDO.getParent().setId(parentID);
        }
        if (null != parentTitle){
            appAdminMenuDO.getParent().setTitle(parentTitle);
        }
        appAdminMenuDO.setModifyTime(LocalDateTime.now());

        menuService.update(appAdminMenuDO);
        return "redirect:/views/admin/menu/page";
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
    public String menuEdit(HttpServletRequest request, ModelMap map, @PathVariable(value = "id")Long id) {
        AppAdminMenuDO menuDO = menuService.queryById(id);
        map.addAttribute("menuDO", menuDO);
        return "/views/menu_edit";
    }
}

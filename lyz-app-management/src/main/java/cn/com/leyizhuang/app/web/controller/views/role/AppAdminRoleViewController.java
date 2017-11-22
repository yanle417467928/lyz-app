package cn.com.leyizhuang.app.web.controller.views.role;

import cn.com.leyizhuang.app.foundation.pojo.management.Role;
import cn.com.leyizhuang.app.foundation.service.RoleService;
import cn.com.leyizhuang.app.web.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 资源控制器
 *
 * @author Richard
 * Created on 2017-08-02 9:49
 **/
@Controller
@RequestMapping("/views/admin/role")
public class AppAdminRoleViewController extends BaseController{

    private static final Logger logger = LoggerFactory.getLogger(AppAdminRoleViewController.class);

    @Autowired
    private RoleService roleService;

    @GetMapping(value = "/list")
    public String roleList() {
        return "/views/role/role_page";
    }

    /**
     * 新增资源页面跳转
     * @return
     */
    @GetMapping(value = "/add")
    public String add(Model model){
        logger.info("新增角色");
        return "/views/role/role_add";
    }


    @GetMapping(value = "/edit/{id}")
    public String roleEdit(ModelMap map, @PathVariable(value = "id") Long id) {
        if (!id.equals(0L)) {
            Role role = roleService.queryById(id);
            if (null == role) {
                logger.warn("跳转修改资源页面失败，Resource(id = {}) == null", id);
                error404();
                return "/error/404";
            } else {
                map.addAttribute("role",role);
            }
        }
        return "/views/role/role_edit";
    }

}

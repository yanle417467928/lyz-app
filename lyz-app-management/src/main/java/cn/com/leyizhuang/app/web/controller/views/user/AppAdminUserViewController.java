package cn.com.leyizhuang.app.web.controller.views.user;

import cn.com.leyizhuang.app.core.constant.StoreType;
import cn.com.leyizhuang.app.foundation.pojo.management.Role;
import cn.com.leyizhuang.app.foundation.pojo.management.User;
import cn.com.leyizhuang.app.foundation.service.AdminUserStoreService;
import cn.com.leyizhuang.app.foundation.service.RoleService;
import cn.com.leyizhuang.app.foundation.service.UserRoleService;
import cn.com.leyizhuang.app.foundation.service.UserService;
import cn.com.leyizhuang.app.foundation.vo.management.AdminUserStoreVO;
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

import java.util.List;

/**
 * 管理员控制器
 *
 * @author Richard
 * Created on 2017-08-02 9:49
 **/
@Controller
@RequestMapping("/views/admin/user")
public class AppAdminUserViewController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(AppAdminUserViewController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private AdminUserStoreService adminUserStoreService;

    @GetMapping(value = "/list")
    public String userList() {
        return "/views/user/user_list";
    }

    /**
     * 跳转到新增用户页面
     *
     * @return
     */
    @GetMapping(value = "/add")
    public String add(Model model) {
        logger.info("新增用户");
        List<Role> roleList = roleService.findByStatus(Boolean.TRUE);
        model.addAttribute("roleList", roleList);
        model.addAttribute("storeTypes", StoreType.values());
        return "/views/user/user_add";
    }

    /**
     * 跳转到编辑用户页面
     *
     * @param map
     * @param id
     * @return
     */
    @GetMapping(value = "/edit/{id}")
    public String resourceEdit(ModelMap map, @PathVariable(value = "id") Long id) {
        if (!id.equals(0L)) {
            User user = userService.queryById(id);
            if (null == user) {
                logger.warn("跳转用户编辑页面失败，User(id = {}) == null", id);
                error404();
                return "/error/404";
            } else {
                List<Role> roleList = roleService.findByStatus(Boolean.TRUE);
                List<Long> roleIds = userRoleService.findRoleIdsByUserId(id);
                List<AdminUserStoreVO> adminUserStoreVOList = this.adminUserStoreService.findByUid(id);
                map.addAttribute("roleIds", roleIds);
                map.addAttribute("roleList", roleList);
                map.addAttribute("user", user);
                map.addAttribute("storeList", adminUserStoreVOList);
                map.addAttribute("storeTypes", StoreType.values());
            }
        }
        return "/views/user/user_edit";
    }

}

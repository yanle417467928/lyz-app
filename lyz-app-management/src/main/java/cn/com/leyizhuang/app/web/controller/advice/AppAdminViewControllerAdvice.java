package cn.com.leyizhuang.app.web.controller.advice;

import cn.com.leyizhuang.app.foundation.pojo.AppAdminMenuDO;
import cn.com.leyizhuang.app.foundation.pojo.vo.AppAdminMenuListVO;
import cn.com.leyizhuang.app.foundation.service.AppAdminMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

/**
 * @author CrazyApeDX
 *         Created on 2017/5/6.
 */
@ControllerAdvice(basePackages = "cn.com.leyizhuang.app.web.controller.views")
public class AppAdminViewControllerAdvice {

    @Autowired
    private AppAdminMenuService menuService;

    @ModelAttribute(value = "menuVOList")
    public List<AppAdminMenuListVO> menuVOList() {
        return menuService.loadAdminMenuTree();
    }

    @ModelAttribute(value = "selectedMenu")
    public AppAdminMenuDO selectedMenu(Long menuId) {
        if (null == menuId) {
            return null;
        } else {
            System.out.println(menuService.queryById(menuId).toString());
            return menuService.queryById(menuId);
        }
    }
}

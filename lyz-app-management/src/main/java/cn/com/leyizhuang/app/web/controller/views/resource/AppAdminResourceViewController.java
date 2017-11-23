package cn.com.leyizhuang.app.web.controller.views.resource;

import cn.com.leyizhuang.app.core.constant.AppAdminResourceType;
import cn.com.leyizhuang.app.foundation.pojo.management.Resource;
import cn.com.leyizhuang.app.foundation.service.ResourceService;
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
import java.util.stream.Collectors;

/**
 * 资源控制器
 *
 * @author Richard
 * Created on 2017-08-02 9:49
 **/
@Controller
@RequestMapping("/views/admin/resource")
public class AppAdminResourceViewController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(AppAdminResourceViewController.class);

    @Autowired
    private ResourceService resourceService;

    @GetMapping(value = "/list")
    public String resourceList() {
        return "/views/resource/resource_page";
    }

    /**
     * 新增资源页面跳转
     *
     * @return
     */
    @GetMapping(value = "/add")
    public String add(Model model) {
        logger.info("新增资源");
        List<Resource> resourceList = resourceService.queryByPid(0L);
        model.addAttribute("resourceList", resourceList);
        model.addAttribute("resourceTypeList", AppAdminResourceType.values());
        return "/views/resource/resource_add";
    }


    /**
     * 编辑资源页面
     *
     * @param map
     * @param id
     * @return
     */
    @GetMapping(value = "/edit/{id}")
    public String resourceEdit(ModelMap map, @PathVariable(value = "id") Long id) {
        if (!id.equals(0L)) {
            Resource resource = resourceService.queryById(id);
            if (null == resource) {
                logger.warn("跳转修改资源页面失败，Resource(id = {}) == null", id);
                error404();
                return "/error/404";
            } else {
                map.addAttribute("resource", resource);
            }
        }
        List<Resource> resourceList = resourceService.queryByPid(0L);
        resourceList = resourceList
                .stream().
                        filter(resource -> !id.equals(resource.getRsId()))
                .collect(Collectors.toList());
        map.addAttribute("resourceTypeList", AppAdminResourceType.values());
        map.addAttribute("resourceList", resourceList);
        return "/views/resource/resource_edit";
    }

}

package cn.com.leyizhuang.app.web.controller.views.decorativeCompany;


import cn.com.leyizhuang.app.foundation.pojo.management.decorativeCompany.DecorativeCompanyInfo;
import cn.com.leyizhuang.app.foundation.service.MaStoreService;
import cn.com.leyizhuang.app.web.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping(value = MaDecorativeCompanyCreditViewsController.PRE_URL, produces = "application/json;charset=utf-8")
public class MaDecorativeCompanyCreditViewsController extends BaseController {

    protected final static String PRE_URL = "/views/admin/decorativeCredit";

    private final Logger logger = LoggerFactory.getLogger(MaDecorativeCompanyCreditViewsController.class);

    @Autowired
    private MaStoreService maStoreService;

    /**
     * 装饰公司信用列表
     * @return
     */
    @RequestMapping(value = "/list")
    public String getDecorativeCreditList() {
        return "/views/decorativeCompany/decorativeCompanyCredit_page";
    }

    /**
     * 装饰公司信用金编辑页面
     * @param map
     * @param id
     * @return
     */
    @RequestMapping(value = "/edit/{id}")
    public String DecorativeCreditEdit(ModelMap map, @PathVariable(value = "id") Long id) {
        if (!id.equals(0L)) {
            DecorativeCompanyInfo decorativeCompanyInfo = this.maStoreService.queryDecorativeCompanyCreditById(id);
            if (null == decorativeCompanyInfo) {
                logger.warn("跳转修改资源页面失败，Resource(id = {}) == null", id);
                error404();
                return "/error/404";
            } else {
                map.addAttribute("decorativeCompanyVO",decorativeCompanyInfo);
            }
        }
        return "/views/decorativeCompany/decorativeCompanyCredit_edit";
    }
}


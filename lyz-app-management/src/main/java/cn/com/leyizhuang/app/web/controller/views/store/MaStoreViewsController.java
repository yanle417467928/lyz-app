package cn.com.leyizhuang.app.web.controller.views.store;

import cn.com.leyizhuang.app.foundation.service.MaStoreService;
import cn.com.leyizhuang.app.foundation.vo.management.store.StoreDetailVO;
import cn.com.leyizhuang.app.web.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/views/admin/stores")
public class MaStoreViewsController extends BaseController {

    @Autowired
    private MaStoreService maStoreService;

    private static final Logger logger = LoggerFactory.getLogger(MaStoreViewsController.class);

    @RequestMapping(value = "/list")
    public String getStoreList() {
        return "/views/store/store_page";
    }

    /**
     * 跳转门店编辑页面
     * @param model
     * @param storeId
     * @return
     */
    @GetMapping(value = "/edit/{id}")
    public String storeEdit(Model model, @PathVariable(value = "id") Long storeId) {
        if (!storeId.equals(0L)) {
            StoreDetailVO storeVO = maStoreService.queryStoreVOById(storeId);
            if (null == storeVO) {
                logger.warn("跳转修改资源页面失败，Resource(id = {}) == null", storeId);
                error404();
                return "/error/404";
            } else {
                model.addAttribute("store",storeVO);
            }
        }
        return "/views/store/store_edit";
    }

}

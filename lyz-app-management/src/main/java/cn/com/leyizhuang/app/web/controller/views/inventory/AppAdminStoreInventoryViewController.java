package cn.com.leyizhuang.app.web.controller.views.inventory;

import cn.com.leyizhuang.app.web.controller.BaseController;
import cn.com.leyizhuang.app.foundation.service.AppAdminStoreInventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * APP后端库存视图控制器
 *
 * @author Richard
 *         Created on 2017-07-12 14:26
 **/
@Controller
@RequestMapping(value = AppAdminStoreInventoryViewController.PRE_URL,produces = "text/html;charset=utf-8")
public class AppAdminStoreInventoryViewController extends BaseController{

    protected final static String PRE_URL = "/views/admin/inventory";

    public static final Logger LOGGER = LoggerFactory.getLogger(AppAdminStoreInventoryViewController.class);

    @Autowired
    private AppAdminStoreInventoryService storeInventoryService;

    @RequestMapping("/page")
    public String inventoryList(Model model,Integer page,Integer size){
        /*page = null == page ? CommonGlobal.PAGEABLE_DEFAULT_PAGE : page;
        size = null == size ? CommonGlobal.PAGEABLE_DEFAULT_SIZE : size;
        PageInfo<StoreInventoryDO> storeInventoryPage = storeInventoryService.queryPage(page,size);
        model.addAttribute("storeInventoryPage", storeInventoryPage);*/
        return "/views/inventory/store/store_inventory_page";
    }

}

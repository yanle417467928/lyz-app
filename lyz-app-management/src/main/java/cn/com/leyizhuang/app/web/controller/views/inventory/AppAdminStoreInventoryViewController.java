package cn.com.leyizhuang.app.web.controller.views.inventory;

import cn.com.leyizhuang.app.foundation.service.AppAdminStoreInventoryService;
import cn.com.leyizhuang.app.web.controller.BaseController;
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
 * Created on 2017-07-12 14:26
 **/
@Controller
@RequestMapping(value = AppAdminStoreInventoryViewController.PRE_URL, produces = "text/html;charset=utf-8")
public class AppAdminStoreInventoryViewController extends BaseController {

    public static final Logger LOGGER = LoggerFactory.getLogger(AppAdminStoreInventoryViewController.class);
    protected final static String PRE_URL = "/views/admin/inventory";
    @Autowired
    private AppAdminStoreInventoryService storeInventoryService;

    @RequestMapping("/page")
    public String inventoryList(Model model, Integer page, Integer size) {
        return "/views/inventory/store/store_inventory_page";
    }


    /**
     * 进销存
     *
     * @param model
     * @return
     */
    @RequestMapping("/invoicing")
    public String invoicingList(Model model) {
        return "/views/inventory/store/store_invoicing_page";
    }

    /**
     * 库存调拨
     *
     * @param model
     * @return
     */
    @RequestMapping("/allocation")
    public String allocationList(Model model) {
        return "/views/inventory/store/store_allocation_page";
    }

    @RequestMapping("/allocation/add")
    public String addAllocation(Model model) {
        return "/views/inventory/store/store_allocation_add";
    }
    /**
     * 库存要货
     *
     * @param model
     * @return
     */
    @RequestMapping("/requiring")
    public String requiringList(Model model) {
        return "/views/inventory/store/store_requiring_page";
    }

    /**
     * 库存退货
     *
     * @param model
     * @return
     */
    @RequestMapping("/returning")
    public String returningList(Model model) {
        return "/views/inventory/store/store_returning_page";
    }

    /**
     * 库存盘点
     *
     * @param model
     * @return
     */
    @RequestMapping("/checking")
    public String checkingList(Model model) {
        return "/views/inventory/store/store_checking_page";
    }

    /**
     * 未提货单
     *
     * @param model
     * @return
     */
    @RequestMapping("/nondelivery")
    public String nondeliveryList(Model model) {
        return "/views/inventory/store/store_nondelivery_page";
    }

    /**
     * 自提货单出货
     *
     * @param model
     * @return
     */
    @RequestMapping("/selfdelivery")
    public String selfdeliveryList(Model model) {
        return "/views/inventory/store/store_selfDelivery_page";
    }
}

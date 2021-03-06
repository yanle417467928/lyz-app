package cn.com.leyizhuang.app.web.controller.views.inventory;

import cn.com.leyizhuang.app.core.config.shiro.ShiroUser;
import cn.com.leyizhuang.app.foundation.pojo.AppStore;
import cn.com.leyizhuang.app.foundation.pojo.inventory.allocation.Allocation;
import cn.com.leyizhuang.app.foundation.pojo.management.store.SimpleStoreParam;
import cn.com.leyizhuang.app.foundation.pojo.management.structure.SimpaleGroupStructureParam;
import cn.com.leyizhuang.app.foundation.service.*;
import cn.com.leyizhuang.app.web.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

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

    @Autowired
    private ItyAllocationService allocationService;

    @Autowired
    private MaStoreService storeService;

    @Autowired
    private AdminUserStoreService adminUserStoreService;

    @Autowired
    private CityService cityService;

    @Autowired
    private MaGroupStructureService maGroupStructureService;

    @Autowired
    private MaStoreService maStoreService;


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
        List<String> structureCodeList = new ArrayList<>();
        structureCodeList.add("LYZ001|JZSYBM");
        List<SimpaleGroupStructureParam>  structureList = maGroupStructureService.querySimpaleStructureListByFilter(structureCodeList);
        List<Long> storeIds = this.adminUserStoreService.findStoreIdList();
        List<SimpleStoreParam> storesList = this.maStoreService.findStoresListByStoreId(storeIds);
        model.addAttribute("structureList", structureList);
        model.addAttribute("storeList", storesList);
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
        List<String> structureCodeList = new ArrayList<>();
        structureCodeList.add("LYZ001|JZSYBM");
        List<SimpaleGroupStructureParam>  structureList = maGroupStructureService.querySimpaleStructureListByFilter(structureCodeList);
        List<Long> storeIds = this.adminUserStoreService.findStoreIdList();
        List<SimpleStoreParam> storesList = this.maStoreService.findStoresListByStoreId(storeIds);
        model.addAttribute("structureList", structureList);
        model.addAttribute("storeList", storesList);
        return "/views/inventory/store/store_allocation_page";
    }


    /**
     * 库存调拨查询
     *
     * @param model
     * @return
     */
    @RequestMapping("/queryAllocation")
    public String queryAllocationList(Model model) {
        List<String> structureCodeList = new ArrayList<>();
        structureCodeList.add("LYZ001|JZSYBM");
        List<SimpaleGroupStructureParam>  structureList = maGroupStructureService.querySimpaleStructureListByFilter(structureCodeList);
        model.addAttribute("structureList", structureList);
        return "/views/inventory/store/store_queryAllocation_page";
    }



    @RequestMapping("/allocation/add")
    public String addAllocation(Model model) {

        // 获取当前登录帐号
        ShiroUser user = super.getShiroUser();

        List<Long> storeIds = adminUserStoreService.findStoreIdList();
        if (storeIds == null || storeIds.size() == 0){
            // 没有门店信息 重新登录
            return "/views/user/login";
        }

        AppStore appStore = storeService.findAppStoreByStoreId(storeIds.get(0));

        // 获取用户城市下门店信息
        List<SimpleStoreParam> storeList =  storeService.findStoresListByCityIdExcludeStoreId(appStore.getStoreId());

        model.addAttribute("stores",storeList);
        return "/views/inventory/store/store_allocation_add";
    }

    /**
     * 调拨单详情
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/allocation/detail/{id}")
    public String allocationDetail(@PathVariable Long id, Model model){

        List<Long> storeIds = adminUserStoreService.findStoreIdList();
        if (storeIds == null || storeIds.size() == 0){
            // 没有门店信息 重新登录
            return "/views/user/login";
        }

        AppStore appStore = storeService.findAppStoreByStoreId(storeIds.get(0));
        model.addAttribute("store",appStore);

        // 获取调拨单详情
        allocationService.queryAllocationDetail(id,model);
        return "/views/inventory/store/store_allocation_detail";
    }
    /**
     * 库存要货
     *
     * @param map
     * @return
     */
    @RequestMapping("/requiring")
    public String requiringList(ModelMap map) {
        List<String> structureCodeList = new ArrayList<>();
        structureCodeList.add("LYZ001|JZSYBM");
        List<SimpaleGroupStructureParam>  structureList = maGroupStructureService.querySimpaleStructureListByFilter(structureCodeList);
        map.addAttribute("structureList", structureList);
        return "/views/inventory/store/store_requiring_page";
    }

    /**
     * 库存退货
     *4
     * @param map
     * @return
     */
    @RequestMapping("/returning")
    public String returningList(ModelMap map) {
        List<String> structureCodeList = new ArrayList<>();
        structureCodeList.add("LYZ001|JZSYBM");
        List<SimpaleGroupStructureParam>  structureList = maGroupStructureService.querySimpaleStructureListByFilter(structureCodeList);
        map.addAttribute("structureList", structureList);
        return "/views/inventory/store/store_returning_page";
    }

    /**
     * 库存盘点
     *
     * @param
     * @return
     */
    @RequestMapping("/checking")
    public String checkingList(ModelMap map) {
        List<String> structureCodeList = new ArrayList<>();
        structureCodeList.add("LYZ001|JZSYBM");
        List<SimpaleGroupStructureParam>  structureList = maGroupStructureService.querySimpaleStructureListByFilter(structureCodeList);
        map.addAttribute("structureList", structureList);
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

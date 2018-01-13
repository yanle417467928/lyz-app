package cn.com.leyizhuang.app.web.controller.views.store;

import cn.com.leyizhuang.app.core.constant.StoreType;
import cn.com.leyizhuang.app.foundation.service.MaStoreService;
import cn.com.leyizhuang.app.foundation.vo.management.store.StorePreDepositVO;
import cn.com.leyizhuang.app.web.controller.BaseController;
import cn.com.leyizhuang.app.web.controller.views.customer.MaCustomerPreDepositViewsController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author GenerationRoad
 * @date 2018/1/12
 */
@Controller
@RequestMapping(value = MaStorePreDepositViewsController.PRE_URL, produces = "application/json;charset=utf-8")
public class MaStorePreDepositViewsController extends BaseController {
    protected final static String PRE_URL = "/views/admin/store/preDeposit";

    private final Logger logger = LoggerFactory.getLogger(MaStorePreDepositViewsController.class);

    @Autowired
    private MaStoreService maStoreService;

    @RequestMapping(value = "/list")
    public String getCustomerList(ModelMap map) {
        map.addAttribute("storeTypes", StoreType.values());
        return "/views/store/store_pre_deposit_page";
    }

    @GetMapping(value = "/edit/{storeId}")
    public String resourceEdit(ModelMap map, @PathVariable(value = "storeId") Long storeId) {
        StorePreDepositVO storePreDepositVO = this.maStoreService.queryStorePredepositByStoreId(storeId);
        if (null == storePreDepositVO) {
            logger.warn("跳转变更预存款页面失败，storePreDepositVO(storeId = {}) == null", storeId);
            error404();
            return "/error/404";
        } else {
            map.addAttribute("storePreDepositVO", storePreDepositVO);
        }
        return "/views/store/store_pre_deposit_edit";
    }


}

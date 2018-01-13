package cn.com.leyizhuang.app.web.controller.views.store;

import cn.com.leyizhuang.app.core.constant.StoreType;
import cn.com.leyizhuang.app.web.controller.BaseController;
import cn.com.leyizhuang.app.web.controller.views.customer.MaCustomerPreDepositLogViewsController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author GenerationRoad
 * @date 2018/1/12
 */
@Controller
@RequestMapping(value = MaStorePreDepositLogViewsController.PRE_URL, produces = "application/json;charset=utf-8")
public class MaStorePreDepositLogViewsController  extends BaseController {
    protected final static String PRE_URL = "/views/admin/store/preDeposit/log";

    private final Logger logger = LoggerFactory.getLogger(MaStorePreDepositLogViewsController.class);

    /**
     * @title   跳转预存款变更明细列表页面
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2018/1/11
     */
    @RequestMapping(value = "/list/{storeId}")
    public String getStorePreDepositLogList(ModelMap map, @PathVariable(value = "storeId") Long storeId) {
        map.addAttribute("storeId",storeId);
        map.addAttribute("storeTypes", StoreType.values());
        return "/views/store/store_pre_deposit_log_page";
    }


}

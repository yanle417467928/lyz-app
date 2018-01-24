package cn.com.leyizhuang.app.web.controller.views.customer;

import cn.com.leyizhuang.app.web.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author GenerationRoad
 * @date 2018/1/11
 */
@Controller
@RequestMapping(value = MaCustomerPreDepositLogViewsController.PRE_URL, produces = "application/json;charset=utf-8")
public class MaCustomerPreDepositLogViewsController extends BaseController {
    protected final static String PRE_URL = "/views/admin/customer/preDeposit/log";

    private final Logger logger = LoggerFactory.getLogger(MaCustomerPreDepositLogViewsController.class);

    /**
     * @title   跳转预存款变更明细列表页面
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2018/1/11
     */
    @RequestMapping(value = "/list/{cusId}")
    public String cusPreDepositLogListPage(ModelMap map, @PathVariable(value = "cusId") Long cusId) {
        map.addAttribute("cusId",cusId);
        return "/views/customer/customer_pre_deposit_log_page";
    }


}

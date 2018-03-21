package cn.com.leyizhuang.app.web.controller.views.customer;

import cn.com.leyizhuang.app.core.constant.OrderBillingPaymentType;
import cn.com.leyizhuang.app.foundation.service.BankService;
import cn.com.leyizhuang.app.foundation.service.MaCustomerService;
import cn.com.leyizhuang.app.foundation.vo.management.BankVO;
import cn.com.leyizhuang.app.foundation.vo.management.customer.CustomerPreDepositVO;
import cn.com.leyizhuang.app.web.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * @author GenerationRoad
 * @date 2018/1/8
 */
@Controller
@RequestMapping(value = MaCustomerPreDepositViewsController.PRE_URL, produces = "application/json;charset=utf-8")
public class MaCustomerPreDepositViewsController extends BaseController {

    protected final static String PRE_URL = "/views/admin/customer/preDeposit";

    private final Logger logger = LoggerFactory.getLogger(MaCustomerPreDepositViewsController.class);

    @Autowired
    private MaCustomerService maCustomerService;

    @Autowired
    private BankService bankService;

    /**
     * @title   跳转顾客预存款列表
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2018/1/15
     */
    @RequestMapping(value = "/list")
    public String customerListPage() {
        return "/views/customer/customer_pre_deposit_page";
    }

    /**
     * @title   跳转变更预存款页面
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2018/1/11
     */
    @GetMapping(value = "/edit/{cusId}")
    public String customerPreDepositEditPage(ModelMap map, @PathVariable(value = "cusId") Long cusId) {
        CustomerPreDepositVO customerPreDepositVO = this.maCustomerService.queryCusPredepositByCusId(cusId);
        if (null == customerPreDepositVO) {
            logger.warn("跳转变更预存款页面失败，customerPreDepositVO(cusId = {}) == null", cusId);
            error404();
            return "/error/404";
        } else {
            List<OrderBillingPaymentType> paymentTypes = new ArrayList<>();
            paymentTypes.add(OrderBillingPaymentType.ALIPAY);
            paymentTypes.add(OrderBillingPaymentType.WE_CHAT);
            paymentTypes.add(OrderBillingPaymentType.CASH);
            paymentTypes.add(OrderBillingPaymentType.POS);
            paymentTypes.add(OrderBillingPaymentType.TRANSFER_ACCOUNTS);
            map.addAttribute("paymentTypes", paymentTypes);
            map.addAttribute("customerPreDepositVO", customerPreDepositVO);
            List<BankVO> bankVOS = this.bankService.findBankByIsEnable();
            map.addAttribute("bankVOS", bankVOS);
        }
        return "/views/customer/customer_pre_deposit_edit";
    }

}

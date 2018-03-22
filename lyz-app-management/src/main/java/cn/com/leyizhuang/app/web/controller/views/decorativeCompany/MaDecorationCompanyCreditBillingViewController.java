package cn.com.leyizhuang.app.web.controller.views.decorativeCompany;

import cn.com.leyizhuang.app.core.constant.OrderBillingPaymentType;
import cn.com.leyizhuang.app.foundation.service.BankService;
import cn.com.leyizhuang.app.foundation.service.MaDecorationCompanyCreditBillingService;
import cn.com.leyizhuang.app.foundation.vo.management.BankVO;
import cn.com.leyizhuang.app.foundation.vo.management.decorativeCompany.DecorationCompanyCreditBillingDetailsVO;
import cn.com.leyizhuang.app.foundation.vo.management.decorativeCompany.DecorationCompanyCreditBillingVO;
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
 * @date 2018/3/15
 */
@Controller
@RequestMapping(value = MaDecorationCompanyCreditBillingViewController.PRE_URL, produces = "application/json;charset=utf-8")
public class MaDecorationCompanyCreditBillingViewController extends BaseController {

    protected static final String PRE_URL = "/views/decorationCompany/creditBilling";

    private final Logger logger = LoggerFactory.getLogger(MaDecorationCompanyCreditBillingViewController.class);

    @Autowired
    private MaDecorationCompanyCreditBillingService maDecorationCompanyCreditBillingService;

    @Autowired
    private BankService bankService;

    /**
     * @title   跳转装饰公司账单获取页面
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2018/3/19
     */
    @GetMapping(value = "/order/list")
    public String getCompanyCreditOrder(ModelMap map) {
        return "/views/creditBilling/credit_order_page";
    }

    /**
     * @title   跳转装饰公司账单列表页面
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2018/3/19
     */
    @GetMapping(value = "/list")
    public String getCompanyCreditBillingList(ModelMap map) {
        return "/views/creditBilling/credit_billing_page";
    }

    /**
     * @title 跳转查看账单详情页面
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2018/3/20
     */
    @GetMapping(value = "/{id}")
    public String showDetails(ModelMap map, @PathVariable(value = "id") Long id) {
        DecorationCompanyCreditBillingVO creditBillingVO = this.maDecorationCompanyCreditBillingService.getDecorationCompanyCreditBillingById(id);
        if (null == creditBillingVO) {
            logger.warn("跳转查看账单详情页面失败，showDetails(creditBillingVO = {}) == null", creditBillingVO);
            error404();
            return "/error/404";
        } else {
            List<DecorationCompanyCreditBillingDetailsVO> creditBillingDetailsVOS = this.maDecorationCompanyCreditBillingService.getDecorationCompanyCreditBillingDetailsByCreditBillingNo(creditBillingVO.getCreditBillingNo());
            map.addAttribute("creditBillingVO", creditBillingVO);
            map.addAttribute("creditBillingDetailsVOS", creditBillingDetailsVOS);
            List<OrderBillingPaymentType> paymentTypes = new ArrayList<>();
            paymentTypes.add(OrderBillingPaymentType.ALIPAY);
            paymentTypes.add(OrderBillingPaymentType.WE_CHAT);
            paymentTypes.add(OrderBillingPaymentType.CASH);
            paymentTypes.add(OrderBillingPaymentType.POS);
            map.addAttribute("paymentTypes", paymentTypes);
            List<BankVO> bankVOS = this.bankService.findBankByIsEnable();
            map.addAttribute("bankVOS", bankVOS);
        }
        return "/views/creditBilling/credit_billing_details";
    }

}

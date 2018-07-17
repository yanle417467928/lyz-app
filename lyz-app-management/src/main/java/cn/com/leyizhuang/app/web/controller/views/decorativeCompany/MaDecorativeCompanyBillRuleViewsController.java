package cn.com.leyizhuang.app.web.controller.views.decorativeCompany;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author GenerationRoad
 * @date 2018/7/14
 */
@Controller
@RequestMapping(value = MaDecorativeCompanyBillRuleViewsController.PRE_URL, produces = "application/json;charset=utf-8")
public class MaDecorativeCompanyBillRuleViewsController {

    protected static final String PRE_URL = "/views/decorationCompany/billRule";

    private final Logger logger = LoggerFactory.getLogger(MaDecorativeCompanyBillRuleViewsController.class);

    @GetMapping(value = "/page")
    public String getCompanyCreditOrder(ModelMap map) {
        return "/views/decorativeCompany/bill_rule_page";
    }
}

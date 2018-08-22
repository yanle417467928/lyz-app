package cn.com.leyizhuang.app.web.controller.views.decorativeCompany;

import cn.com.leyizhuang.app.core.constant.FitCompayType;
import cn.com.leyizhuang.app.foundation.pojo.bill.BillRuleDO;
import cn.com.leyizhuang.app.foundation.service.BillRuleService;
import cn.com.leyizhuang.app.foundation.vo.management.BillRuleVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

/**
 * @author GenerationRoad
 * @date 2018/7/14
 */
@Controller
@RequestMapping(value = MaDecorativeCompanyBillRuleViewsController.PRE_URL, produces = "application/json;charset=utf-8")
public class MaDecorativeCompanyBillRuleViewsController {

    @Resource
    private BillRuleService billRuleService;

    protected static final String PRE_URL = "/views/decorationCompany/billRule";

    private final Logger logger = LoggerFactory.getLogger(MaDecorativeCompanyBillRuleViewsController.class);

    @GetMapping(value = "/page")
    public String billRulePage(ModelMap map) {
        map.addAttribute("fitCompanyTyoes", FitCompayType.getFitCompayType());
        return "/views/decorativeCompany/bill_rule_page";
    }

    @GetMapping(value = "/add")
    public String addBillRule() {
        return "/views/decorativeCompany/bill_rule_add";
    }

    @GetMapping(value = "/edit/{id}")
    public String editBillRule(@PathVariable(value = "id") Long id,ModelMap map) {
        BillRuleVO billRule = this.billRuleService.getBillRuleById(id);
        map.addAttribute("billRule",billRule);
        return "/views/decorativeCompany/bill_rule_edit";
    }

    @GetMapping(value = "/log/list/{id}")
    public String getBillRuleChangLog(@PathVariable(value = "id") Long id,ModelMap map) {
        map.addAttribute("ruleId",id);
        return "/views/decorativeCompany/bill_rule_log";
    }
}

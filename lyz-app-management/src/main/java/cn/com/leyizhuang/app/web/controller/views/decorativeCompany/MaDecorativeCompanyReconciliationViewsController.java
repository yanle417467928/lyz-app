package cn.com.leyizhuang.app.web.controller.views.decorativeCompany;

import cn.com.leyizhuang.app.foundation.pojo.management.decorativeCompany.DecorativeCompanyContract;
import cn.com.leyizhuang.app.foundation.service.DecorativeCompanyContractService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

/**
 * Created by liuh on 2018/2/26.
 */
@Controller
@RequestMapping(value = MaDecorativeCompanyReconciliationViewsController.PRE_URL, produces = "application/json;charset=utf-8")
public class MaDecorativeCompanyReconciliationViewsController {
    protected final static String PRE_URL = "/views/admin/decorativeCredit/reconciliation";

    @Resource
    private DecorativeCompanyContractService decorativeCompanyContractService;


    /**
     * 装饰公司列表
     */
    @RequestMapping(value = "/list")
    public String decorativeCompanyListPage() {
        return "/views/decorativeCompany/decorativeCompanyReconciliation_page";
    }

    /**
     * 查看装饰公司合同
     *
     * @param map
     * @param companyId
     * @return
     */
    @GetMapping(value = "/detail/{id}")
    public String getCompanyContractController(ModelMap map, @PathVariable(value = "id") Long companyId) {
        //根据装饰公司id查询装饰公司账单
        DecorativeCompanyContract decorativeCompanyContract = decorativeCompanyContractService.findCompanyContractByCompanyId(companyId);

        map.addAttribute("decorativeCompanyContract", decorativeCompanyContract);
        return "/views/decorativeCompany/decorative_company_contract_detail";
    }
}

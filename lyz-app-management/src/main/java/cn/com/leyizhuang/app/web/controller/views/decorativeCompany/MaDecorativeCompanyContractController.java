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
 * Created by caiyu on 2018/1/30.
 */
@Controller
@RequestMapping(value = MaDecorativeCompanyContractController.PRE_URL, produces = "application/json;charset=utf-8")
public class MaDecorativeCompanyContractController {
    protected final static String PRE_URL = "/views/controller";

    @Resource
    private DecorativeCompanyContractService decorativeCompanyContractService;

    /**
     * 查看装饰公司合同
     *
     * @param map
     * @param companyId
     * @return
     */
    @GetMapping(value = "/detail/{id}")
    public String getCompanyContractController(ModelMap map, @PathVariable(value = "id") Long companyId) {
        //根据装饰公司id查询装饰公司合同
        DecorativeCompanyContract decorativeCompanyContract = decorativeCompanyContractService.findCompanyContractByCompanyId(companyId);

        map.addAttribute("decorativeCompanyContract", decorativeCompanyContract);
        return "/views/decorativeCompany/decorative_company_contract_detail";
    }
}

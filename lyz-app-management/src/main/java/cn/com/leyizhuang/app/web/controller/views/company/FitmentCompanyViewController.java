package cn.com.leyizhuang.app.web.controller.views.company;

import cn.com.leyizhuang.app.foundation.pojo.FitmentCompanyDO;
import cn.com.leyizhuang.app.foundation.service.FitmentCompanyService;
import cn.com.leyizhuang.app.foundation.vo.FitmentCompanyVO;
import cn.com.leyizhuang.app.web.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author GenerationRoad
 * @date 2017/9/19
 */
@Controller
@RequestMapping(value = FitmentCompanyViewController.PRE_URL, produces = "application/json;charset=utf-8")
public class FitmentCompanyViewController extends BaseController {
    protected final static String PRE_URL = "/view/company";
    private final Logger logger = LoggerFactory.getLogger(FitmentCompanyViewController.class);

    @Autowired
    private FitmentCompanyService fitmentCompanyServiceImpl;

    @GetMapping(value = "/page")
    public String companyPage(HttpServletRequest request, ModelMap map) {
        return "/views/company/company_page";
    }

    @GetMapping(value = "/edit/{id}")
    public String companyEdit(ModelMap map, @PathVariable(value = "id") Long id) {
        if (!id.equals(0L)) {
            FitmentCompanyDO fitmentCompanyDO = this.fitmentCompanyServiceImpl.queryById(id);
            if (null == fitmentCompanyDO) {
                logger.warn("跳转修改装饰公司页面失败，Resource(id = {}) == null", id);
                error404();
                return "/error/404";
            } else {
                FitmentCompanyVO decorationCompanyVO = FitmentCompanyVO.transform(fitmentCompanyDO);
                map.addAttribute("decorationCompanyVO", decorationCompanyVO);
            }
        }
        return "/views/company/dec_company_edit";
    }
}

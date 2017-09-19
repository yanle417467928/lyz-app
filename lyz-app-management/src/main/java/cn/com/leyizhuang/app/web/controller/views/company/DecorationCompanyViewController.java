package cn.com.leyizhuang.app.web.controller.views.company;

import cn.com.leyizhuang.app.foundation.service.DecorationCompanyService;
import cn.com.leyizhuang.app.web.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author GenerationRoad
 * @date 2017/9/19
 */
@Controller
@RequestMapping(value = DecorationCompanyViewController.PRE_URL,produces = "application/json;charset=utf-8")
public class DecorationCompanyViewController extends BaseController {
    protected final static String PRE_URL = "/view/company";
    private final Logger logger = LoggerFactory.getLogger(DecorationCompanyViewController.class);

    @Autowired
    private DecorationCompanyService decorationCompanyServiceImpl;

    @GetMapping(value = "/page")
    public String storePage(HttpServletRequest request, ModelMap map) {
        return "/views/company/company_page";
    }
}

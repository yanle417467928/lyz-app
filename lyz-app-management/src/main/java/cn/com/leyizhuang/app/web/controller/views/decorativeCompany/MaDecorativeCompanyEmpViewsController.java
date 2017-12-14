package cn.com.leyizhuang.app.web.controller.views.decorativeCompany;


import cn.com.leyizhuang.app.web.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping(value = MaDecorativeCompanyEmpViewsController.PRE_URL, produces = "application/json;charset=utf-8")
public class MaDecorativeCompanyEmpViewsController extends BaseController {

    protected final static String PRE_URL = "/views/admin/decorativeEmp";

    private final Logger logger = LoggerFactory.getLogger(MaDecorativeCompanyEmpViewsController.class);

    @RequestMapping(value = "/list")
    public String getDecorativeEmpList() {
        return "/views/decorativeCompany/decorativeCompanyEmp_page";
    }
}


package cn.com.leyizhuang.app.web.controller.views.decorativeCompanyInfo;


import cn.com.leyizhuang.app.web.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping(value = MaDecorativeCompanyInfoViewsController.PRE_URL, produces = "application/json;charset=utf-8")
public class MaDecorativeCompanyInfoViewsController extends BaseController {

    protected final static String PRE_URL = "/views/admin/decorativeInfo";

    private final Logger logger = LoggerFactory.getLogger(MaDecorativeCompanyInfoViewsController.class);


    @RequestMapping(value = "/list")
    public String getDecorativeInfoList() {
        return "/views/decorativeCompany/decorativeCompanyInfo_page";
    }
}


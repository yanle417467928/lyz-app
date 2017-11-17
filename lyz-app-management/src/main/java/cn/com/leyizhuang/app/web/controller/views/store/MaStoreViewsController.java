package cn.com.leyizhuang.app.web.controller.views.store;

import cn.com.leyizhuang.app.web.controller.BaseController;
import cn.com.leyizhuang.app.web.controller.views.user.AppAdminUserViewController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/views/admin/stores")
public class MaStoreViewsController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(AppAdminUserViewController.class);

    @RequestMapping(value = "/list")
    public String storeList() {
        return "/views/store/store_page";
    }
}

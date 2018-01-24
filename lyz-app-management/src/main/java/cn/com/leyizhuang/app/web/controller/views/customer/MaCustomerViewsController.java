package cn.com.leyizhuang.app.web.controller.views.customer;


import cn.com.leyizhuang.app.web.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping(value = MaCustomerViewsController.PRE_URL, produces = "application/json;charset=utf-8")
public class MaCustomerViewsController extends BaseController {

    protected final static String PRE_URL = "/views/admin/customers";

    private final Logger logger = LoggerFactory.getLogger(MaCustomerViewsController.class);

    /**
     * 跳转顾客列表
     * @return
     */
    @RequestMapping(value = "/list")
    public String customerListPage() {
        return "/views/customer/customer_page";
    }

    /**
     * 跳转顾客新增页面
     * @return
     */
    @RequestMapping(value = "/add")
    public String addPage() {
        logger.info("新增顾客");
        return "/views/customer/customer_add";
    }

}

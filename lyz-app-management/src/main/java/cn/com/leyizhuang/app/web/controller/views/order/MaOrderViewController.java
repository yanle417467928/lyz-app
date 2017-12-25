package cn.com.leyizhuang.app.web.controller.views.order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * 后台门店订单管理
 * Created by caiyu on 2017/12/16.
 */
@Controller
@RequestMapping(value = MaOrderViewController.PRE_URL, produces = "application/json;charset=utf-8")
public class MaOrderViewController {
    protected final static String PRE_URL = "/views/admin/order";
//    private final Logger logger = LoggerFactory.getLogger(MaOrderViewController.class);

    /**
     * 返回门店订单列表页
     *
     * @return 门店订单页面
     */
    @GetMapping(value = "/store/list")
    public String storePage() {
        return "/views/order/store_page";
    }

    /**
     * 返回装饰公司订单列表页面
     *
     * @return 装饰公司订单页面
     */
    @GetMapping(value = "/company/list")
    public String companyPage() {
        return "/views/order/company_page";
    }

    /**
     * 返回待发货订单列表页面
     *
     * @return 待发货订单页面
     */
    @GetMapping(value = "/pendingShipment/list")
    public String pendingShipmentOrderPage() {
        return "/views/order/pending_shipment_page";
    }
}

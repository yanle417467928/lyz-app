package cn.com.leyizhuang.app.web.controller.views.wmsInterface;

import cn.com.leyizhuang.app.core.constant.OrderBillingPaymentType;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.wms.WtaShippingOrderHeader;
import cn.com.leyizhuang.app.foundation.service.MaStoreService;
import cn.com.leyizhuang.app.foundation.service.WmsToAppOrderService;
import cn.com.leyizhuang.app.foundation.vo.management.BankVO;
import cn.com.leyizhuang.app.foundation.vo.management.store.StorePreDepositVO;
import cn.com.leyizhuang.app.web.controller.BaseController;
import cn.com.leyizhuang.app.web.controller.views.store.MaStorePreDepositViewsController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * @author GenerationRoad
 * @date 2018/4/23
 */
@Controller
@RequestMapping(value = WmsInterfaceViewController.PRE_URL, produces = "application/json;charset=utf-8")
public class WmsInterfaceViewController extends BaseController {
    protected final static String PRE_URL = "/views/admin/wmsInterface";

    private final Logger logger = LoggerFactory.getLogger(WmsInterfaceViewController.class);

    @Autowired
    private WmsToAppOrderService wmsToAppOrderService;


    @GetMapping(value = "/handle/deliveryStatus/{orderNumber}")
    public String handleDeliveryStatus(ModelMap map, @PathVariable(value = "orderNumber") String orderNumber) {
        this.wmsToAppOrderService.handleWtaOrderLogistics(orderNumber);
        return "sueesess";
    }

    @GetMapping(value = "/handle/orderShipping/{orderNumber}/{taskNo}")
    public String handleOrderShipping(ModelMap map, @PathVariable(value = "orderNumber") String orderNumber, @PathVariable(value = "taskNo") String taskNo) {
        this.wmsToAppOrderService.handleWtaShippingOrder(orderNumber, taskNo);
        return "sueesess";
    }

    @GetMapping(value = "/handle/orderShipping/all")
    public String handleOrderShippingALL() {
        List<WtaShippingOrderHeader> shippingOrderHeaders = this.wmsToAppOrderService.getAllWtaShippingOrderHeader();
        if (null != shippingOrderHeaders){
            for (WtaShippingOrderHeader shippingOrderHeader: shippingOrderHeaders) {
                this.wmsToAppOrderService.handleWtaShippingOrder(shippingOrderHeader.getOrderNo(), shippingOrderHeader.getTaskNo());
            }
        }

        return "sueesess";
    }
}

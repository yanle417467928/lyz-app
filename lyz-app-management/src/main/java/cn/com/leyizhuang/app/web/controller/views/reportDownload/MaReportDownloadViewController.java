package cn.com.leyizhuang.app.web.controller.views.reportDownload;

import cn.com.leyizhuang.app.core.constant.OrderBillingPaymentType;
import cn.com.leyizhuang.app.core.constant.StoreType;
import cn.com.leyizhuang.app.web.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * @author GenerationRoad
 * @date 2018/3/22
 */
@Controller
@RequestMapping(value = MaReportDownloadViewController.PRE_URL, produces = "application/json;charset=utf-8")
public class MaReportDownloadViewController extends BaseController {

    protected static final String PRE_URL = "/views/reportDownload";

    private final Logger logger = LoggerFactory.getLogger(MaReportDownloadViewController.class);

    @GetMapping(value = "/receipts/list")
    public String getReceiptsList(ModelMap map) {
        List<OrderBillingPaymentType> paymentTypes = new ArrayList<>();
        paymentTypes.add(OrderBillingPaymentType.ALIPAY);
        paymentTypes.add(OrderBillingPaymentType.WE_CHAT);
        paymentTypes.add(OrderBillingPaymentType.CASH);
        paymentTypes.add(OrderBillingPaymentType.POS);
        paymentTypes.add(OrderBillingPaymentType.UNION_PAY);
        paymentTypes.add(OrderBillingPaymentType.CUS_PREPAY);
        paymentTypes.add(OrderBillingPaymentType.ST_PREPAY);
        paymentTypes.add(OrderBillingPaymentType.OTHER);
        map.addAttribute("paymentTypes", paymentTypes);
        map.addAttribute("storeTypes", StoreType.getNotZsType());
        return "/views/reportDownload/receipts_page";
    }
}

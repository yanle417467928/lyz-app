package cn.com.leyizhuang.app.web.controller.views.reportDownload;

import cn.com.leyizhuang.app.core.constant.AppDeliveryType;
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

    /**
     * @title    收款报表
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2018/4/2
     */
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

    @GetMapping(value = "/not/pickGoods/list")
    public String getNotPickGoodsList(ModelMap map) {
        List<AppDeliveryType> deliveryTypes = new ArrayList<>();
        deliveryTypes.add(AppDeliveryType.HOUSE_DELIVERY);
        deliveryTypes.add(AppDeliveryType.PRODUCT_COUPON);
        deliveryTypes.add(AppDeliveryType.SELF_TAKE);
        map.addAttribute("pickTypes", deliveryTypes);
        map.addAttribute("storeTypes", StoreType.getStoreTypeList());
        return "/views/reportDownload/not_pick_goods";
    }

    @GetMapping(value = "/store/predeposit/list")
    public String getStorePredeposit(ModelMap map) {
        map.addAttribute("storeTypes", StoreType.getStoreTypeList());
        return "/views/reportDownload/store_predeposit_page";
    }

    /**
     * @title    对账商品明细报表
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2018/4/2
     */
    @GetMapping(value = "/accountGoodsItems/list")
    public String getAccountGoodsItemsList(ModelMap map) {
        map.addAttribute("storeTypes", StoreType.getStoreTypeList());
        return "/views/reportDownload/account_goods_items_page";
    }

    /**
     * @title   账单明细报表
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2018/4/3
     */
    @GetMapping(value = "/billingItems/list")
    public String getBillingItemsList(ModelMap map) {
        map.addAttribute("storeTypes", StoreType.getStoreTypeList());
        return "/views/reportDownload/billing_items_page";
    }

    /**
     * @title    配送员代收款报表
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2018/4/3
     */
    @GetMapping(value = "/agencyFund/list")
    public String getAgencyFundList(ModelMap map) {
        map.addAttribute("storeTypes", StoreType.getNotZsType());
        return "/views/reportDownload/agency_fund_page";
    }


    /**
     * @title    商品出退货报表
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2018/4/3
     */
    @GetMapping(value = "/goods/shipmentAndReturn/list")
    public String getGoodsShipmentAndReturnList(ModelMap map) {
        map.addAttribute("storeTypes", StoreType.getNotZsType());
        return "/views/reportDownload/goods_shipmentAndReturn_page";
    }

}

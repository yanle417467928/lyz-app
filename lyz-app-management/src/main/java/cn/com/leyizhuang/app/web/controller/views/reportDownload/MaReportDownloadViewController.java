package cn.com.leyizhuang.app.web.controller.views.reportDownload;

import cn.com.leyizhuang.app.core.constant.AppDeliveryType;
import cn.com.leyizhuang.app.core.constant.OrderBillingPaymentType;
import cn.com.leyizhuang.app.core.constant.StoreType;
import cn.com.leyizhuang.app.foundation.pojo.management.structure.SimpaleGroupStructureParam;
import cn.com.leyizhuang.app.foundation.service.MaGroupStructureService;
import cn.com.leyizhuang.app.web.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
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

    @Resource
    private MaGroupStructureService  maGroupStructureService;

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

    @GetMapping(value = "/employee/creditMoney/list")
    public String getEmployeeCreditMoney(ModelMap map) {
        map.addAttribute("storeTypes", StoreType.getStoreTypeList());
        return "/views/reportDownload/emp_creditMoney_page";
    }

    /**
     * @title    订单明细报表
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
     * @author liuh
     * @date 2018/4/3
     */
    @GetMapping(value = "/goods/shipmentAndReturn/list")
    public String getGoodsShipmentAndReturnList(ModelMap map) {
        
        map.addAttribute("storeTypes", StoreType.getNotZsType());
        return "/views/reportDownload/goods_shipmentAndReturn_page";
    }


    /**
     * @title    销量报表
     * @descripe
     * @param
     * @return
     * @throws
     * @author liuh
     * @date 2018/4/3
     */
    @GetMapping(value = "/salesVolume/list")
    public String getSalesVolumeList(ModelMap map) {
        List<SimpaleGroupStructureParam>  structureList = maGroupStructureService.querySimpaleStructureList();
        map.addAttribute("structureList", structureList);
        map.addAttribute("storeTypes", StoreType.getNotZsType());
        return "/views/reportDownload/sales_volume_page";
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
    @GetMapping(value = "/accountZGGoodsItems/list")
    public String getAccountZGGoodsItemsList(ModelMap map) {
        return "/views/reportDownload/account_ZG_goods_items_page";
    }


    /**
     * @title    欠款报表
     * @descripe
     * @param
     * @return
     * @throws
     * @author liuh
     * @date 2018/4/3
     */
    @GetMapping(value = "/arrears/list")
    public String getArrearsList(ModelMap map) {
        List<SimpaleGroupStructureParam>  structureList = maGroupStructureService.querySimpaleStructureList();
        map.addAttribute("structureList", structureList);
        map.addAttribute("storeTypes", StoreType.getNotZsType());
        return "/views/reportDownload/arrears_page";
    }

    @GetMapping(value = "/distribution/list")
    public String getDistributionList(ModelMap map) {
        return "/views/reportDownload/distribution_page";
    }

    /**
     * @title    郑州华润产品销顾终端出货销量报表
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2018/4/2
     */
    @GetMapping(value = "/account/hr/list")
    public String getAccountGoodsItemsHRList(ModelMap map) {
        map.addAttribute("storeTypes", StoreType.getStoreTypeList());
        return "/views/reportDownload/account_goods_items_hr_page";
    }

    @GetMapping(value = "/emp/credit")
    public String empCreditMoney(ModelMap map) {
        map.addAttribute("storeTypes", StoreType.getStoreTypeList());
        return "/views/reportDownload/emp_credit_money";
    }

    @GetMapping(value = "/st/credit")
    public String stCreditMoney(ModelMap map) {
        map.addAttribute("storeTypes", StoreType.getStoreTypeList());
        return "/views/reportDownload/st_credit_money";
    }

    @GetMapping(value = "/photoOrderCheck/list")
    public String getPhotoOrderCheckList(ModelMap map) {
        return "/views/reportDownload/photoOrderCheck_page";
    }


    @GetMapping(value = "/expiringSoonProduct/list")
    public String getExpiringSoonProductList(ModelMap map) {
        map.addAttribute("storeTypes", StoreType.getNotZsType());
        return "/views/reportDownload/expiringSoon_product_page";
    }

}

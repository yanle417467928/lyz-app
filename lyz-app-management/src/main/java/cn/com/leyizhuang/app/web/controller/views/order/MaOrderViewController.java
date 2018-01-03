package cn.com.leyizhuang.app.web.controller.views.order;

import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderBaseInfo;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderGoodsInfo;
import cn.com.leyizhuang.app.foundation.service.AppOrderService;
import cn.com.leyizhuang.app.foundation.service.MaOrderService;
import cn.com.leyizhuang.app.foundation.vo.management.order.MaOrderBillingDetailResponse;
import cn.com.leyizhuang.app.foundation.vo.management.order.MaOrderBillingPaymentDetailResponse;
import cn.com.leyizhuang.app.foundation.vo.management.order.MaOrderDetailResponse;
import cn.com.leyizhuang.app.foundation.vo.management.goodscategory.MaOrderGoodsDetailResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 后台门店订单管理
 * Created by caiyu on 2017/12/16.
 */
@Controller
@RequestMapping(value = MaOrderViewController.PRE_URL, produces = "application/json;charset=utf-8")
public class MaOrderViewController {
    protected final static String PRE_URL = "/views/admin/order";
    private final Logger logger = LoggerFactory.getLogger(MaOrderViewController.class);

    @Resource
    private AppOrderService appOrderService;
    @Resource
    private MaOrderService maOrderService;
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

    /**
     * 门店订单详情
     *
     * @return 待发货订单页面
     */
    @GetMapping(value = "/detail/{orderaNumber}")
    public String storeOrderDetail(ModelMap map, @PathVariable(value = "orderaNumber") String orderaNumber) {
        logger.info("storeOrderDetail CALLED,门店订单详情，入参 orderaNumber:{}", orderaNumber);
        if (!StringUtils.isBlank(orderaNumber)) {
            OrderBaseInfo orderBaseInfo = appOrderService.getOrderByOrderNumber(orderaNumber);
            if (orderBaseInfo != null){
                if (orderBaseInfo.getDeliveryType().getDescription().equals("送货上门")){
                    //查询订单基本信息
                    MaOrderDetailResponse maOrderDetailResponse = maOrderService.findMaOrderDetailByOrderNumber(orderaNumber);
                    //查询订单商品
                    List<OrderGoodsInfo> orderGoodsInfoList = appOrderService.getOrderGoodsInfoByOrderNumber(orderaNumber);
                    //创建商品返回list
                    List<MaOrderGoodsDetailResponse> maOrderGoodsDetailResponseList = new ArrayList<>();
                    for (OrderGoodsInfo orderGoodsInfo : orderGoodsInfoList){
                        //创建商品返回对象
                        MaOrderGoodsDetailResponse maOrderGoodsDetailResponse = new MaOrderGoodsDetailResponse();
                        maOrderGoodsDetailResponse.setSku(orderGoodsInfo.getSku());
                        maOrderGoodsDetailResponse.setQty(orderGoodsInfo.getOrderQuantity() == null ? 0:orderGoodsInfo.getOrderQuantity());
                        maOrderGoodsDetailResponse.setUnitPrice(orderGoodsInfo.getRetailPrice() == null ? 0.00:orderGoodsInfo.getRetailPrice());
                        //计算商品小计（零售）
                        Double subTotalPrice = (orderGoodsInfo.getOrderQuantity() == null ? 0:orderGoodsInfo.getOrderQuantity()) * (orderGoodsInfo.getRetailPrice() == null ? 0.00:orderGoodsInfo.getRetailPrice());
                        maOrderGoodsDetailResponse.setSubTotalPrice(subTotalPrice);
                        //计算商品实付金额（分摊）
                        Double reslPayment = (orderGoodsInfo.getOrderQuantity() == null ? 0:orderGoodsInfo.getOrderQuantity()) * (orderGoodsInfo.getReturnPrice() == null ? 0.00:orderGoodsInfo.getReturnPrice());
                        maOrderGoodsDetailResponse.setRealPayment(reslPayment);
                        if ("本品".equals(orderGoodsInfo.getGoodsLineType().getDescription())){
                            maOrderGoodsDetailResponse.setGoodsType("本品");
                        }else if ("赠品".equals(orderGoodsInfo.getGoodsLineType().getDescription())){
                            maOrderGoodsDetailResponse.setGoodsType("赠品");
                        }else if ("产品券".equals(orderGoodsInfo.getGoodsLineType().getDescription())){
                            maOrderGoodsDetailResponse.setGoodsType("产品券");
                        }
                        maOrderGoodsDetailResponseList.add(maOrderGoodsDetailResponse);
                    }
                    maOrderDetailResponse.setMaOrderGoodsDetailResponseList(maOrderGoodsDetailResponseList);
                    //获取订单账目明细
                    MaOrderBillingDetailResponse maOrderBillingDetailResponse = maOrderService.getMaOrderBillingDetailByOrderNumber(orderaNumber);
                    //获取订单支付明细列表
                    List<MaOrderBillingPaymentDetailResponse> maOrderBillingPaymentDetailResponseList = maOrderService.getMaOrderBillingPaymentDetailByOrderNumber(orderaNumber);
                    if (null != maOrderBillingDetailResponse){
                        map.addAttribute("orderBillingDetail",maOrderBillingDetailResponse);
                    }
                    if (null != maOrderBillingPaymentDetailResponseList){
                        map.addAttribute("paymentDetailList",maOrderBillingPaymentDetailResponseList);
                    }
                    map.addAttribute("maOrderDetail",maOrderDetailResponse);
                    return "/views/order/store_order_detail";
                }else if (orderBaseInfo.getDeliveryType().getDescription().equals("门店自提")){

                }
            }
        }
        return "/views/order/store_order_detail";
    }
}

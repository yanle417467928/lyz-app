package cn.com.leyizhuang.app.web.controller.views.order;

import cn.com.leyizhuang.app.core.constant.AppOrderStatus;
import cn.com.leyizhuang.app.core.constant.AppOrderStatus;
import cn.com.leyizhuang.app.core.constant.StoreType;
import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.pojo.management.order.MaOrderArrearsAudit;
import cn.com.leyizhuang.app.foundation.pojo.management.order.OrderFreightChange;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderBaseInfo;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderGoodsInfo;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderLogisticsInfo;
import cn.com.leyizhuang.app.foundation.service.*;
import cn.com.leyizhuang.app.foundation.vo.DetailFitOrderVO;
import cn.com.leyizhuang.app.foundation.vo.management.goodscategory.MaOrderGoodsDetailResponse;
import cn.com.leyizhuang.app.foundation.vo.management.order.*;
import cn.com.leyizhuang.common.util.CountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
    @Resource
    private MaEmpCreditMoneyService maEmpCreditMoneyService;
    @Resource
    private MaStoreInventoryService maStoreInventoryService;
    @Resource
    private MaStoreService maStoreService;
    @Resource
    private AppOrderService orderService;

    @Autowired
    private MaOrderFreightService freightService;

    /**
     * 返回门店订单列表页
     *
     * @return 门店订单页面
     */
    @GetMapping(value = "/store/list")
    public String storePage(ModelMap map) {
        map.addAttribute("orderStatusList", AppOrderStatus.getAppOrderStatusList());
        return "/views/order/store_page";
    }

    /**
     * 返回装饰公司订单列表页面
     *
     * @return 装饰公司订单页面
     */
    @GetMapping(value = "/company/list")
    public String companyPage(ModelMap map) {
        map.addAttribute("orderStatusList", AppOrderStatus.getAppOrderStatusList());
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
     * 订单详情
     *
     * @return 订单详情页面
     */
    @GetMapping(value = "/detail/{orderaNumber}")
    public String storeOrderDetail(ModelMap map, @PathVariable(value = "orderaNumber") String orderaNumber) {
        logger.info("storeOrderDetail CALLED,门店订单详情，入参 orderaNumber:{}", orderaNumber);
        if (!StringUtils.isBlank(orderaNumber)) {
            OrderBaseInfo orderBaseInfo = appOrderService.getOrderByOrderNumber(orderaNumber);

            if (orderBaseInfo != null && "门店".equals(orderBaseInfo.getOrderSubjectType().getDescription())) {
                //查询订单基本信息
                MaOrderDetailResponse maOrderDetailResponse = maOrderService.findMaOrderDetailByOrderNumber(orderaNumber);
                String time = maOrderService.getShippingTime(orderaNumber);
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date shippingDate = null;
                if (null != time) {
                    try {
                        shippingDate = formatter.parse(time);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                OrderLogisticsInfo orderLogisticsInfo = orderService.getOrderLogistice(orderaNumber);
                maOrderDetailResponse.setShipTime(shippingDate);
                if (null != orderLogisticsInfo && null != orderLogisticsInfo.getBookingStoreName()) {
                    maOrderDetailResponse.setShipStore(orderLogisticsInfo.getBookingStoreName());
                }
                //查询订单商品
                List<OrderGoodsInfo> orderGoodsInfoList = appOrderService.getOrderGoodsInfoByOrderNumber(orderaNumber);
                //创建商品返回list
                List<MaOrderGoodsDetailResponse> maOrderGoodsDetailResponseList = new ArrayList<>();
                for (OrderGoodsInfo orderGoodsInfo : orderGoodsInfoList) {
                    //创建商品返回对象
                    MaOrderGoodsDetailResponse maOrderGoodsDetailResponse = new MaOrderGoodsDetailResponse();
                    maOrderGoodsDetailResponse.setSku(orderGoodsInfo.getSku());
                    maOrderGoodsDetailResponse.setGoodsName(orderGoodsInfo.getSkuName());
                    maOrderGoodsDetailResponse.setQty(orderGoodsInfo.getOrderQuantity() == null ? 0 : orderGoodsInfo.getOrderQuantity());
                    maOrderGoodsDetailResponse.setUnitPrice(orderGoodsInfo.getRetailPrice() == null ? 0.00 : orderGoodsInfo.getRetailPrice());
                    //计算商品小计（零售）
                    Double subTotalPrice = (orderGoodsInfo.getOrderQuantity() == null ? 0 : orderGoodsInfo.getOrderQuantity()) * (orderGoodsInfo.getRetailPrice() == null ? 0.00 : orderGoodsInfo.getRetailPrice());
                    maOrderGoodsDetailResponse.setSubTotalPrice(subTotalPrice);
                    //计算商品实付金额（分摊）
                    Double reslPayment = (orderGoodsInfo.getOrderQuantity() == null ? 0 : orderGoodsInfo.getOrderQuantity()) * (orderGoodsInfo.getReturnPrice() == null ? 0.00 : orderGoodsInfo.getReturnPrice());
                    maOrderGoodsDetailResponse.setRealPayment(reslPayment);
                    if ("本品".equals(orderGoodsInfo.getGoodsLineType().getDescription())) {
                        maOrderGoodsDetailResponse.setGoodsType("本品");
                    } else if ("赠品".equals(orderGoodsInfo.getGoodsLineType().getDescription())) {
                        maOrderGoodsDetailResponse.setGoodsType("赠品");
                    } else if ("产品券".equals(orderGoodsInfo.getGoodsLineType().getDescription())) {
                        maOrderGoodsDetailResponse.setGoodsType("产品券");
                    }
                    maOrderGoodsDetailResponseList.add(maOrderGoodsDetailResponse);
                }
                maOrderDetailResponse.setMaOrderGoodsDetailResponseList(maOrderGoodsDetailResponseList);
                //获取订单账目明细
                MaOrderBillingDetailResponse maOrderBillingDetailResponse = maOrderService.getMaOrderBillingDetailByOrderNumber(orderaNumber);
                Double totalGoodsPrice = maOrderBillingDetailResponse.getTotalGoodsPrice() == null ? 0 : maOrderBillingDetailResponse.getTotalGoodsPrice();
                Double freight = maOrderBillingDetailResponse.getFreight() == null ? 0 : maOrderBillingDetailResponse.getFreight();

                //计算总价
                Double totailPrice = CountUtil.add(totalGoodsPrice, freight);

                Double cashCouponDiscount = maOrderBillingDetailResponse.getCashCouponDiscount() == null ? 0 : maOrderBillingDetailResponse.getCashCouponDiscount();
                Double productCouponDiscount = maOrderBillingDetailResponse.getProductCouponDiscount() == null ? 0 : maOrderBillingDetailResponse.getProductCouponDiscount();
                Double memberDiscount = maOrderBillingDetailResponse.getMemberDiscount() == null ? 0 : maOrderBillingDetailResponse.getMemberDiscount();
                Double lebiCashDiscount = maOrderBillingDetailResponse.getLebiCashDiscount() == null ? 0 : maOrderBillingDetailResponse.getLebiCashDiscount();
                Double promotionDiscount = maOrderBillingDetailResponse.getPromotionDiscount() == null ? 0 : maOrderBillingDetailResponse.getPromotionDiscount();

                //计算所有折扣
                Double totailDiscountPrice = CountUtil.add(cashCouponDiscount, productCouponDiscount, promotionDiscount,
                        memberDiscount, lebiCashDiscount);

                maOrderBillingDetailResponse.setAmountPayable(CountUtil.sub(totailPrice, totailDiscountPrice));

                //获取订单支付明细列表
                List<MaOrderBillingPaymentDetailResponse> maOrderBillingPaymentDetailResponseList = maOrderService.getMaOrderBillingPaymentDetailByOrderNumber(orderaNumber);
                if (null != maOrderBillingDetailResponse) {
                    map.addAttribute("orderBillingDetail", maOrderBillingDetailResponse);
                }
                if (null != maOrderBillingPaymentDetailResponseList) {
                    map.addAttribute("paymentDetailList", maOrderBillingPaymentDetailResponseList);
                }
                map.addAttribute("maOrderDetail", maOrderDetailResponse);
                //获取运费明细
                List<OrderFreightChange> freightChanges = this.freightService.queryOrderFreightChangeLogListByOid(orderBaseInfo.getId());
                map.addAttribute("freightChanges", freightChanges);
                //获取运费初始值
                OrderFreightChange freightChange = this.freightService.queryOrderFreightChangeLogFirstByOid(orderBaseInfo.getId());
                if (null == freightChange){
                    freightChange = new OrderFreightChange();
                    freightChange.setFreight(BigDecimal.valueOf(maOrderBillingDetailResponse.getFreight()));
                }
                map.addAttribute("freightChange", freightChange);
                return "/views/order/store_order_detail";
            } else if ("门店自提".equals(orderBaseInfo.getDeliveryType().getDescription())) {
                MaOrderDetailResponse maOrderDetailResponse = maOrderService.findMaOrderDetailByOrderNumber(orderaNumber);
                List<OrderGoodsInfo> orderGoodsInfoList = appOrderService.getOrderGoodsInfoByOrderNumber(orderaNumber);
                //创建商品返回list
                String time = maOrderService.getShippingTime(orderaNumber);
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date shippingDate = null;
                if (null != time) {
                    try {
                        shippingDate = formatter.parse(time);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                OrderLogisticsInfo orderLogisticsInfo = orderService.getOrderLogistice(orderaNumber);
                maOrderDetailResponse.setShipTime(shippingDate);
                if (null != orderLogisticsInfo && null != orderLogisticsInfo.getBookingStoreName()) {
                    maOrderDetailResponse.setShipStore(orderLogisticsInfo.getBookingStoreName());
                }
                List<MaOrderGoodsDetailResponse> maOrderGoodsDetailResponseList = new ArrayList<>();
                for (OrderGoodsInfo orderGoodsInfo : orderGoodsInfoList) {
                    //创建商品返回对象
                    MaOrderGoodsDetailResponse maOrderGoodsDetailResponse = new MaOrderGoodsDetailResponse();
                    maOrderGoodsDetailResponse.setSku(orderGoodsInfo.getSku());
                    maOrderGoodsDetailResponse.setGoodsName(orderGoodsInfo.getSkuName());
                    maOrderGoodsDetailResponse.setQty(orderGoodsInfo.getOrderQuantity() == null ? 0 : orderGoodsInfo.getOrderQuantity());
                    maOrderGoodsDetailResponse.setUnitPrice(orderGoodsInfo.getRetailPrice() == null ? 0.00 : orderGoodsInfo.getRetailPrice());
                    //计算商品小计（零售）
                    Double subTotalPrice = (orderGoodsInfo.getOrderQuantity() == null ? 0 : orderGoodsInfo.getOrderQuantity()) * (orderGoodsInfo.getRetailPrice() == null ? 0.00 : orderGoodsInfo.getRetailPrice());
                    maOrderGoodsDetailResponse.setSubTotalPrice(subTotalPrice);
                    //计算商品实付金额（分摊）
                    Double reslPayment = (orderGoodsInfo.getOrderQuantity() == null ? 0 : orderGoodsInfo.getOrderQuantity()) * (orderGoodsInfo.getReturnPrice() == null ? 0.00 : orderGoodsInfo.getReturnPrice());
                    maOrderGoodsDetailResponse.setRealPayment(reslPayment);
                    if ("本品".equals(orderGoodsInfo.getGoodsLineType().getDescription())) {
                        maOrderGoodsDetailResponse.setGoodsType("本品");
                    } else if ("赠品".equals(orderGoodsInfo.getGoodsLineType().getDescription())) {
                        maOrderGoodsDetailResponse.setGoodsType("赠品");
                    } else if ("产品券".equals(orderGoodsInfo.getGoodsLineType().getDescription())) {
                        maOrderGoodsDetailResponse.setGoodsType("产品券");
                    }
                    maOrderGoodsDetailResponseList.add(maOrderGoodsDetailResponse);
                }
                maOrderDetailResponse.setMaOrderGoodsDetailResponseList(maOrderGoodsDetailResponseList);
                //获取订单账目明细
                MaOrderBillingDetailResponse maOrderBillingDetailResponse = maOrderService.getMaOrderBillingDetailByOrderNumber(orderaNumber);
                //获取订单支付明细列表
                List<MaOrderBillingPaymentDetailResponse> maOrderBillingPaymentDetailResponseList = maOrderService.getMaOrderBillingPaymentDetailByOrderNumber(orderaNumber);
                if (null != maOrderBillingDetailResponse) {
                    map.addAttribute("orderBillingDetail", maOrderBillingDetailResponse);
                }
                if (null != maOrderBillingPaymentDetailResponseList) {
                    map.addAttribute("paymentDetailList", maOrderBillingPaymentDetailResponseList);
                }
                map.addAttribute("maOrderDetail", maOrderDetailResponse);
                return "/views/order/store_order_detail";
            } else if (orderBaseInfo != null && "装饰公司".equals(orderBaseInfo.getOrderSubjectType().getDescription())) {
                //查询订单基本信息
                MaCompanyOrderDetailResponse maCompanyOrderDetailResponse = maOrderService.findMaCompanyOrderDetailByOrderNumber(orderaNumber);
                //查询订单商品
                List<OrderGoodsInfo> orderGoodsInfoList = appOrderService.getOrderGoodsInfoByOrderNumber(orderaNumber);
                //创建商品返回list
                List<MaOrderGoodsDetailResponse> maCompanyOrderGoodsDetailResponseList = new ArrayList<>();
                for (OrderGoodsInfo orderGoodsInfo : orderGoodsInfoList) {
                    //创建商品返回对象
                    MaOrderGoodsDetailResponse maCompanyOrderGoodsDetailResponse = new MaOrderGoodsDetailResponse();
                    maCompanyOrderGoodsDetailResponse.setSku(orderGoodsInfo.getSku());
                    maCompanyOrderGoodsDetailResponse.setGoodsName(orderGoodsInfo.getSkuName());
                    maCompanyOrderGoodsDetailResponse.setQty(orderGoodsInfo.getOrderQuantity() == null ? 0 : orderGoodsInfo.getOrderQuantity());
                    maCompanyOrderGoodsDetailResponse.setUnitPrice(orderGoodsInfo.getRetailPrice() == null ? 0.00 : orderGoodsInfo.getRetailPrice());
                    //计算商品小计（零售）
                    Double subTotalPrice = (orderGoodsInfo.getOrderQuantity() == null ? 0 : orderGoodsInfo.getOrderQuantity()) * (orderGoodsInfo.getRetailPrice() == null ? 0.00 : orderGoodsInfo.getRetailPrice());
                    maCompanyOrderGoodsDetailResponse.setSubTotalPrice(subTotalPrice);
                    //计算商品实付金额（分摊）
                    Double reslPayment = (orderGoodsInfo.getOrderQuantity() == null ? 0 : orderGoodsInfo.getOrderQuantity()) * (orderGoodsInfo.getReturnPrice() == null ? 0.00 : orderGoodsInfo.getReturnPrice());
                    maCompanyOrderGoodsDetailResponse.setRealPayment(reslPayment);
                    if ("本品".equals(orderGoodsInfo.getGoodsLineType().getDescription())) {
                        maCompanyOrderGoodsDetailResponse.setGoodsType("本品");
                    } else if ("赠品".equals(orderGoodsInfo.getGoodsLineType().getDescription())) {
                        maCompanyOrderGoodsDetailResponse.setGoodsType("赠品");
                    } else if ("产品券".equals(orderGoodsInfo.getGoodsLineType().getDescription())) {
                        maCompanyOrderGoodsDetailResponse.setGoodsType("产品券");
                    }
                    maCompanyOrderGoodsDetailResponseList.add(maCompanyOrderGoodsDetailResponse);
                }
                maCompanyOrderDetailResponse.setMaOrderGoodsDetailResponseList(maCompanyOrderGoodsDetailResponseList);

                //获取订单账目明细
                MaOrderBillingDetailResponse maOrderBillingDetailResponse = maOrderService.getMaOrderBillingDetailByOrderNumber(orderaNumber);

                Double totalGoodsPrice = maOrderBillingDetailResponse.getTotalGoodsPrice() == null ? 0 : maOrderBillingDetailResponse.getTotalGoodsPrice();
                Double freight = maOrderBillingDetailResponse.getFreight() == null ? 0 : maOrderBillingDetailResponse.getFreight();
                //计算总价
                Double totailPrice = CountUtil.add(totalGoodsPrice, freight);

                Double cashCounponDiscount = maOrderBillingDetailResponse.getCashCouponDiscount() == null ? 0 : maOrderBillingDetailResponse.getCashCouponDiscount();
                Double promotionDiscount = maOrderBillingDetailResponse.getPromotionDiscount() == null ? 0 : maOrderBillingDetailResponse.getPromotionDiscount();
                Double memberDiscount = maOrderBillingDetailResponse.getMemberDiscount() == null ? 0 : maOrderBillingDetailResponse.getMemberDiscount();
                Double subvention = maOrderBillingDetailResponse.getSubvention() == null ? 0 : maOrderBillingDetailResponse.getSubvention();


                //计算所有折扣
                Double totailDiscountPrice = CountUtil.add(cashCounponDiscount, promotionDiscount, memberDiscount, subvention);

                maOrderBillingDetailResponse.setAmountPayable(CountUtil.sub(totailPrice, totailDiscountPrice));

                //获取订单支付明细列表
                List<MaOrderBillingPaymentDetailResponse> maOrderBillingPaymentDetailResponseList = maOrderService.getMaOrderBillingPaymentDetailByOrderNumber(orderaNumber);
                if (null != maOrderBillingDetailResponse) {
                    map.addAttribute("orderBillingDetail", maOrderBillingDetailResponse);
                }
                if (null != maOrderBillingPaymentDetailResponseList) {
                    map.addAttribute("paymentDetailList", maOrderBillingPaymentDetailResponseList);
                }

                map.addAttribute("maOrderDetail", maCompanyOrderDetailResponse);
                //获取运费明细
                List<OrderFreightChange> freightChanges = this.freightService.queryOrderFreightChangeLogListByOid(orderBaseInfo.getId());
                map.addAttribute("freightChanges", freightChanges);
                //获取运费初始值
                OrderFreightChange freightChange = this.freightService.queryOrderFreightChangeLogFirstByOid(orderBaseInfo.getId());
                if (null == freightChange){
                    freightChange = new OrderFreightChange();
                    freightChange.setFreight(BigDecimal.valueOf(maOrderBillingDetailResponse.getFreight()));
                }
                map.addAttribute("freightChange", freightChange);
                return "/views/order/company_order_detail";
            }
        }
        return "/views/order/store_order_detail";
    }

    /**
     * 购买产品券
     *
     * @return 购买产品券页面
     */
    @RequestMapping(value = "/buy/produtCoupon")
    public String buyProdutCoupon() {
        return "/views/order/produt_coupon";
    }

    /**
     * 购买专供产品券
     *
     * @return 购买产品券页面
     */
    @RequestMapping(value = "/buy/zg/produtCoupon")
    public String buyZGProdutCoupon() {
        return "/views/order/zg_produt_coupon";
    }

    /**
     * 跳转门店自提单页面
     *
     * @return
     */
    @RequestMapping(value = "/selfTakeOrder/list")
    public String selfTakeOrderShippingListPage(ModelMap map) {
        map.addAttribute("orderStatusList", AppOrderStatus.getSelfTakeOrderStatusList());
        return "/views/order/selfTakeOrder_page";
    }


    /**
     * 自提单订单详情
     *
     * @return 自提单订单详情页面
     */
    @RequestMapping(value = "/selfTakeOrderDetail/{orderNumber}")
    public String selfTakeOrderDetail(ModelMap map, @PathVariable(value = "orderNumber") String orderNumber) {
        logger.info("selfTakeOrderDetail CALLED,门店订单详情，入参 orderNumber:{}", orderNumber);
        if (!StringUtils.isBlank(orderNumber)) {
            //获取订单基本信息
            OrderBaseInfo orderBaseInfo = appOrderService.getOrderByOrderNumber(orderNumber);
            //查询出货时间
            String time = maOrderService.getShippingTime(orderNumber);
            map.addAttribute("shippingTime", time);
            if ("门店自提".equals(orderBaseInfo.getDeliveryType().getDescription())) {
                //查询订单详细信息
                MaOrderDetailResponse maOrderDetailResponse = maOrderService.findMaOrderDetailByOrderNumber(orderNumber);
                maOrderDetailResponse.setCreatorIdentityType(orderBaseInfo.getCreatorIdentityType());
                //查询订单商品信息
                List<MaOrderGoodsDetailResponse> maOrderGoodsDetailResponseList = maOrderService.getOrderGoodsDetailResponseList(orderNumber);
                //创建商品返回list
                maOrderDetailResponse.setMaOrderGoodsDetailResponseList(maOrderGoodsDetailResponseList);
                //获取订单账目明细
                MaOrderBillingDetailResponse maOrderBillingDetailResponse = maOrderService.getMaOrderBillingDetailByOrderNumber(orderNumber);
                //获取订单支付明细列表
                List<MaOrderBillingPaymentDetailResponse> maOrderBillingPaymentDetailResponseList = maOrderService.getMaOrderBillingPaymentDetailByOrderNumber(orderNumber);
                if (null != maOrderBillingDetailResponse) {
                    map.addAttribute("orderBillingDetail", maOrderBillingDetailResponse);
                }
                if (null != maOrderBillingPaymentDetailResponseList) {
                    map.addAttribute("paymentDetailList", maOrderBillingPaymentDetailResponseList);
                }
                map.addAttribute("maOrderDetail", maOrderDetailResponse);
                Boolean isPayUp = maOrderService.isPayUp(orderNumber);
                map.addAttribute("isPayUp", isPayUp);
                logger.info("selfTakeOrderDetail CALLED,门店订单详情成功");
                return "/views/order/selfTakeOrder_detail";
            } else {
                logger.warn("该订单不为门店自提");
            }
        } else {
            logger.warn("orderNumber为空");
        }
        return "/error/500";
    }


    /**
     * 跳转欠款还款订单页面
     *
     * @return
     */
    @RequestMapping(value = "/arrearsAndRepaymentsOrder/list")
    public String arrearsAndRepaymentsOrderListPage() {
        return "/views/order/arrearsAndRepaymentsOrder_page";
    }

    /**
     * 订单审核欠款还款详情
     *
     * @return 订单审核欠款还款详情页面
     */
    @GetMapping(value = "/arrearsAndRepaymentsOrderDetail")
    public String arrearsAndRepaymentsOrderDetail(ModelMap map, @RequestParam(value = "orderNumber") String orderNumber, @RequestParam(value = "id") Long id) {
        logger.info("arrearsAndRepaymentsOrderDetail CALLED,欠款还款订单详情，入参 orderaNumber:{}", orderNumber);
        if (!StringUtils.isBlank(orderNumber)) {
            //获取订单基本信息
            OrderBaseInfo orderBaseInfo = appOrderService.getOrderByOrderNumber(orderNumber);
            //查询订单是否还清
            Boolean isPayUp = maOrderService.isPayUp(orderNumber);
            //查询审核订单信息
            MaOrderArrearsAudit maOrderArrearsAudit = maOrderService.getArrearsAuditInfoById(id);
            //查询订单商品信息
            List<MaOrderGoodsDetailResponse> maOrderGoodsDetailResponseList = maOrderService.getOrderGoodsDetailResponseList(orderNumber);
            //获取订单账目明细
            MaOrderBillingDetailResponse maOrderBillingDetailResponse = maOrderService.getMaOrderBillingDetailByOrderNumber(orderNumber);
            if (orderBaseInfo != null && "门店".equals(orderBaseInfo.getOrderSubjectType().getDescription())) {
                //查询订单详细信息
                MaOrderDetailResponse maOrderDetailResponse = maOrderService.findMaOrderDetailByOrderNumber(orderNumber);
                maOrderDetailResponse.setMaOrderGoodsDetailResponseList(maOrderGoodsDetailResponseList);
                map.addAttribute("maOrderDetail", maOrderDetailResponse);
                map.addAttribute("type", 1);
            } else if (orderBaseInfo != null && "装饰公司".equals(orderBaseInfo.getOrderSubjectType().getDescription())) {
                //查询订单基本信息
                MaCompanyOrderDetailResponse maCompanyOrderDetailResponse = maOrderService.findMaCompanyOrderDetailByOrderNumber(orderNumber);
                maCompanyOrderDetailResponse.setMaOrderGoodsDetailResponseList(maOrderGoodsDetailResponseList);
                map.addAttribute("maOrderDetail", maCompanyOrderDetailResponse);
                map.addAttribute("type", 2);
            }
            //获取订单支付明细列表
            List<MaOrderBillingPaymentDetailResponse> maOrderBillingPaymentDetailResponseList = maOrderService.getMaOrderBillingPaymentDetailByOrderNumber(orderNumber);
            //获取应还款金额
            Double repaymentAmount = maOrderBillingDetailResponse.getArrearage();
            if (null != maOrderBillingDetailResponse) {
                map.addAttribute("orderBillingDetail", maOrderBillingDetailResponse);
            }
            if (null != maOrderBillingPaymentDetailResponseList) {
                map.addAttribute("paymentDetailList", maOrderBillingPaymentDetailResponseList);
            }
            map.addAttribute("repaymentAmount", repaymentAmount);
            map.addAttribute("isPayUp", isPayUp);
            map.addAttribute("auditStatus", maOrderArrearsAudit.getStatus());
            map.addAttribute("auditId", maOrderArrearsAudit.getId());
            return "/views/order/arrearsAndRepaymentsOrder_detail";
        }
        logger.warn("orderNumber为空");
        return "/error/500";
    }


    /**
     * 装饰公司订单详情
     *
     * @return 自提单订单详情页面
     */
    @RequestMapping(value = "/fitOrderDetail/{orderNumber}")
    public String fitOrderDetail(ModelMap map, @PathVariable(value = "orderNumber") String orderNumber) {
        logger.info("selfTakeOrderDetail CALLED,门店订单详情，入参 orderNumber:{}", orderNumber);
        if (!StringUtils.isBlank(orderNumber)) {
            //获取订单基本信息
            OrderBaseInfo orderBaseInfo = appOrderService.getOrderByOrderNumber(orderNumber);
            //装饰公司订单代付信息
            DetailFitOrderVO detailFitOrderVO = maOrderService.findFitOrderByOrderNumber(orderNumber);
            map.addAttribute("detailFitOrderVO", detailFitOrderVO);
            //物流信息
            MaOrderDeliveryInfoResponse maOrderDeliveryInfoResponse = maOrderService.getDeliveryInfoByOrderNumber(orderNumber);
            map.addAttribute("delivertInfo", maOrderDeliveryInfoResponse);
            //查询出货时间
            String time = maOrderService.getShippingTime(orderNumber);
            map.addAttribute("shippingTime", time);
            //查询订单详细信息
            MaOrderDetailResponse maOrderDetailResponse = maOrderService.findMaOrderDetailByOrderNumber(orderNumber);
            maOrderDetailResponse.setCreatorIdentityType(orderBaseInfo.getCreatorIdentityType());
            //查询订单商品信息
            List<MaOrderGoodsDetailResponse> maOrderGoodsDetailResponseList = maOrderService.getOrderGoodsDetailResponseList(orderNumber);
            //创建商品返回list
            maOrderDetailResponse.setMaOrderGoodsDetailResponseList(maOrderGoodsDetailResponseList);
            //获取订单账目明细
            MaOrderBillingDetailResponse maOrderBillingDetailResponse = maOrderService.getMaOrderBillingDetailByOrderNumber(orderNumber);
            //获取订单支付明细列表
            List<MaOrderBillingPaymentDetailResponse> maOrderBillingPaymentDetailResponseList = maOrderService.getMaOrderBillingPaymentDetailByOrderNumber(orderNumber);
            if (null != maOrderBillingDetailResponse) {
                map.addAttribute("orderBillingDetail", maOrderBillingDetailResponse);
            }
            if (null != maOrderBillingPaymentDetailResponseList) {
                map.addAttribute("paymentDetailList", maOrderBillingPaymentDetailResponseList);
            }
            map.addAttribute("maOrderDetail", maOrderDetailResponse);
            Boolean isPayUp = maOrderService.isPayUp(orderNumber);
            map.addAttribute("isPayUp", isPayUp);
            logger.info("selfTakeOrderDetail CALLED,门店订单详情成功");
            return "/views/decorativeCompany/fitOrder_detail";
        } else {
            logger.warn("orderNumber为空");
        }
        return "/error/500";
    }

}

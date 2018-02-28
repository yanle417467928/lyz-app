package cn.com.leyizhuang.app.web.controller.views.order;

import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.pojo.management.guide.GuideCreditMoney;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderBaseInfo;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderGoodsInfo;
import cn.com.leyizhuang.app.foundation.service.AppOrderService;
import cn.com.leyizhuang.app.foundation.service.MaEmpCreditMoneyService;
import cn.com.leyizhuang.app.foundation.service.MaOrderService;
import cn.com.leyizhuang.app.foundation.service.MaStoreInventoryService;
import cn.com.leyizhuang.app.foundation.vo.management.order.MaCompanyOrderDetailResponse;
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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    @Resource
    private MaEmpCreditMoneyService maEmpCreditMoneyService;
    @Resource
    private MaStoreInventoryService maStoreInventoryService;

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
            } else if ("门店自提".equals(orderBaseInfo.getDeliveryType().getDescription())) {
                MaOrderDetailResponse maOrderDetailResponse = maOrderService.findMaOrderDetailByOrderNumber(orderaNumber);
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
                //获取订单支付明细列表
                List<MaOrderBillingPaymentDetailResponse> maOrderBillingPaymentDetailResponseList = maOrderService.getMaOrderBillingPaymentDetailByOrderNumber(orderaNumber);
                if (null != maOrderBillingDetailResponse) {
                    map.addAttribute("orderBillingDetail", maOrderBillingDetailResponse);
                }
                if (null != maOrderBillingPaymentDetailResponseList) {
                    map.addAttribute("paymentDetailList", maOrderBillingPaymentDetailResponseList);
                }

                map.addAttribute("maOrderDetail", maCompanyOrderDetailResponse);
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
     * 跳转门店自提单页面
     *
     * @return
     */
    @RequestMapping(value = "/selfTakeOrder/list")
    public String selfTakeOrderShippingListPage() {
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
            String  time = maOrderService.getShippingTime(orderNumber);
            logger.info("selfTakeOrderDetail CALLED,门店订单详情，入参 time:{}", time);
            if(null!=time){
                try{
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date shippingTime  =sdf.parse(time);
                    map.addAttribute("shippingTime", shippingTime);
                } catch (ParseException e) {
                    logger.info("出货日期转换错误");
                }
            }
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
    @GetMapping(value = "/arrearsAndRepaymentsOrderDetail/{orderNumber}")
    public String arrearsAndRepaymentsOrderDetail(ModelMap map, @PathVariable(value = "orderNumber") String orderNumber) {
        logger.info("arrearsAndRepaymentsOrderDetail CALLED,欠款还款订单详情，入参 orderaNumber:{}", orderNumber);
        if (!StringUtils.isBlank(orderNumber)) {
            //获取订单基本信息
            OrderBaseInfo orderBaseInfo = appOrderService.getOrderByOrderNumber(orderNumber);
            //查询订单是否还清
            Boolean isPayUp = maOrderService.isPayUp(orderNumber);
            //查询审核状态
            String auditStatus = maOrderService.queryAuditStatus(orderNumber);
            //查询订单商品信息
            List<MaOrderGoodsDetailResponse> maOrderGoodsDetailResponseList = maOrderService.getOrderGoodsDetailResponseList(orderNumber);
            //获取订单账目明细
            MaOrderBillingDetailResponse maOrderBillingDetailResponse = maOrderService.getMaOrderBillingDetailByOrderNumber(orderNumber);
            //导购信用额度更新时间
            Long sellerId = maOrderService.querySellerIdByOrderNumber(orderNumber);
            GuideCreditMoney guideCreditMoney = maEmpCreditMoneyService.findGuideCreditMoneyAvailableByEmpId(sellerId);
            //获取订单支付明细列表
            List<MaOrderBillingPaymentDetailResponse> maOrderBillingPaymentDetailResponseList = maOrderService.getMaOrderBillingPaymentDetailByOrderNumber(orderNumber);
            //获取应还款金额
            String repaymentAmount =  maOrderService.queryRepaymentAmount(orderNumber);
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
            if (null != maOrderBillingDetailResponse) {
                map.addAttribute("orderBillingDetail", maOrderBillingDetailResponse);
            }
            if (null != maOrderBillingPaymentDetailResponseList) {
                map.addAttribute("paymentDetailList", maOrderBillingPaymentDetailResponseList);
            }
            map.addAttribute("repaymentAmount", repaymentAmount);
            map.addAttribute("isPayUp", isPayUp);
            map.addAttribute("auditStatus", auditStatus);
            map.addAttribute("lastUpdateTime", guideCreditMoney.getLastUpdateTime());
            return "/views/order/arrearsAndRepaymentsOrder_detail";
        }
        logger.warn("orderNumber为空");
        return "/error/500";
    }

}

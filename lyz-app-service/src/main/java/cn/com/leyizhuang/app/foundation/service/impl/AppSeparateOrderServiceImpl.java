package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.constant.*;
import cn.com.leyizhuang.app.core.remote.ebs.EbsSenderService;
import cn.com.leyizhuang.app.core.utils.order.OrderUtils;
import cn.com.leyizhuang.app.foundation.dao.AppSeparateOrderDAO;
import cn.com.leyizhuang.app.foundation.pojo.order.*;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.ebs.OrderBaseInf;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.ebs.OrderCouponInf;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.ebs.OrderGoodsInf;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.ebs.OrderReceiptInf;
import cn.com.leyizhuang.app.foundation.service.AppOrderService;
import cn.com.leyizhuang.app.foundation.service.AppSeparateOrderService;
import cn.com.leyizhuang.app.foundation.service.TransactionalSupportService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * 拆单服务实现
 *
 * @author Ricahrd
 * Created on 2018-01-04 10:49
 **/
@Service
public class AppSeparateOrderServiceImpl implements AppSeparateOrderService {

    @Resource
    private AppSeparateOrderDAO separateOrderDAO;

    @Resource
    private TransactionalSupportService supportService;

    @Resource
    private AppOrderService orderService;

    @Resource
    private EbsSenderService ebsSenderService;

    @Override
    public Boolean isOrderExist(String orderNumber) {
        if (null != orderNumber) {
            return separateOrderDAO.isOrderExist(orderNumber);
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrderBaseInf(OrderBaseInf baseInf) {
        if (null != baseInf && null != baseInf.getOrderNumber()) {
            separateOrderDAO.saveOrderBaseInf(baseInf);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrderGoodsInf(OrderGoodsInf goodsInf) {
        if (null != goodsInf) {
            separateOrderDAO.saveOrderGoodsInf(goodsInf);
        }
    }

    @Override
    public void separateOrder(String orderNumber) {
        OrderBaseInfo baseInfo = orderService.getOrderByOrderNumber(orderNumber);
        if (null != baseInfo) {
            OrderBillingDetails billingDetail = orderService.getOrderBillingDetail(orderNumber);
            List<OrderGoodsInfo> orderGoodsInfoList = orderService.getOrderGoodsInfoByOrderNumber(orderNumber);
            if (null != orderGoodsInfoList && orderGoodsInfoList.size() > 0) {
                //获取所有companyFlag并加入到set中
                Set<String> companyFlag = new HashSet<>();
                for (OrderGoodsInfo orderGoodsInfo : orderGoodsInfoList) {
                    if (null != orderGoodsInfo.getCompanyFlag()) {
                        companyFlag.add(orderGoodsInfo.getCompanyFlag());
                    } else {
                        //todo 记录拆单错误日志
                    }
                }
                //创建一个map存放按companyFlag分组的商品信息
                Map<String, List<OrderGoodsInfo>> goodsMap = new HashMap<>(5);
                //分单List
                List<OrderBaseInf> orderBaseInfList = new ArrayList<>(5);
                //分单商品List
                List<OrderGoodsInf> orderGoodsInfList = new ArrayList<>(10);
                //循环所有companyFlag,拿到各个分单的产品并创建分单
                for (String s : companyFlag) {
                    List<OrderGoodsInfo> orderGoodsInfoListTemp = new ArrayList<>();
                    for (OrderGoodsInfo orderGoodsInfo : orderGoodsInfoList) {
                        if (orderGoodsInfo.getCompanyFlag().equalsIgnoreCase(s)) {
                            orderGoodsInfoListTemp.add(orderGoodsInfo);
                        }
                    }
                    goodsMap.put(s, orderGoodsInfoListTemp);
                    //创建分单
                    OrderBaseInf orderBaseInf = new OrderBaseInf();
                    String separateOrderNumber = OrderUtils.generateSeparateOrderNumber(s, orderNumber);
                    if (null != separateOrderNumber) {
                        orderBaseInf.setMainOrderNumber(orderNumber);
                        orderBaseInf.setOrderNumber(separateOrderNumber);
                        orderBaseInf.setCreateTime(new Date());
                        orderBaseInf.setDeliveryTypeTitle(baseInfo.getDeliveryType());
                        orderBaseInf.setOrderSubjectType(baseInfo.getOrderSubjectType());

                        //************* 计算分单商品总金额及应付金额 *************

                        //分单商品零售价总金额
                        Double separateOrderGoodsTotalPrice = 0D;
                        //分单应付金额
                        Double separateOrderAmountPayable = 0D;
                        //分单会员折扣
                        Double separateOrderMemberDiscount = 0D;
                        //分单促销折扣
                        Double separateOrderPromotionDiscount = 0D;
                        //分单优惠券折扣
                        Double separateOrderCashCouponDiscount = 0D;
                        //分单产品券折扣
                        Double separateOrderProductCouponDiscount = 0D;
                        //分单乐币折扣
                        Double separateOrderLebiDiscount = 0D;
                        //分单现金返利折扣
                        Double separateOrderSubventionDiscount = 0D;

                        for (OrderGoodsInfo goodsInfo : orderGoodsInfoListTemp) {
                            if (goodsInfo.getGoodsLineType() == AppGoodsLineType.GOODS) {
                                separateOrderGoodsTotalPrice += goodsInfo.getRetailPrice() * goodsInfo.getOrderQuantity();
                                separateOrderMemberDiscount += (goodsInfo.getRetailPrice() - goodsInfo.getSettlementPrice()) * goodsInfo.getOrderQuantity();
                                separateOrderPromotionDiscount += goodsInfo.getPromotionSharePrice() * goodsInfo.getOrderQuantity();
                                separateOrderCashCouponDiscount += goodsInfo.getCashCouponSharePrice() * goodsInfo.getOrderQuantity();
                                separateOrderLebiDiscount += goodsInfo.getLbSharePrice() * goodsInfo.getOrderQuantity();
                                separateOrderSubventionDiscount += goodsInfo.getCashReturnSharePrice() * goodsInfo.getOrderQuantity();
                            } else if (goodsInfo.getGoodsLineType() == AppGoodsLineType.PRODUCT_COUPON) {
                                separateOrderGoodsTotalPrice += goodsInfo.getRetailPrice() * goodsInfo.getOrderQuantity();
                                separateOrderMemberDiscount += (goodsInfo.getRetailPrice() - goodsInfo.getSettlementPrice()) * goodsInfo.getOrderQuantity();
                                separateOrderProductCouponDiscount += goodsInfo.getSettlementPrice() * goodsInfo.getOrderQuantity();
                            }

                            OrderGoodsInf goodsInf = new OrderGoodsInf();
                            goodsInf.setCreateTime(orderBaseInf.getCreateTime());
                            goodsInf.setMainOrderNumber(orderNumber);
                            goodsInf.setOrderNumber(orderBaseInf.getOrderNumber());
                            goodsInf.setOrderLineId(goodsInfo.getId());
                            goodsInf.setCashCouponDiscount(goodsInfo.getCashCouponSharePrice());
                            goodsInf.setLebiDiscount(goodsInfo.getLbSharePrice());
                            goodsInf.setPromotionDiscount(goodsInfo.getPromotionSharePrice());
                            goodsInf.setSubventionDiscount(goodsInfo.getCashReturnSharePrice());
                            goodsInf.setDiscountTotalPrice(goodsInf.getCashCouponDiscount() + goodsInf.getLebiDiscount()
                                    + goodsInf.getSubventionDiscount() + goodsInf.getPromotionDiscount());
                            goodsInf.setGiftFlag(goodsInfo.getGoodsLineType() == AppGoodsLineType.PRESENT ? AppWhetherFlag.Y : AppWhetherFlag.N);
                            goodsInf.setSku(goodsInfo.getSku());
                            goodsInf.setGoodsTitle(goodsInfo.getSkuName());
                            goodsInf.setLsPrice(goodsInfo.getRetailPrice());
                            goodsInf.setHyPrice(goodsInfo.getVIPPrice());
                            goodsInf.setJxPrice(goodsInfo.getWholesalePrice());
                            goodsInf.setSettlementPrice(goodsInfo.getSettlementPrice());
                            goodsInf.setReturnPrice(goodsInfo.getReturnPrice());
                            goodsInf.setQuantity(goodsInfo.getOrderQuantity());
                            goodsInf.setPromotionId(goodsInfo.getPromotionId());
                            goodsInf.setProductType(ProductType.getProductTypeByValue(goodsInfo.getCompanyFlag()));
                            orderGoodsInfList.add(goodsInf);
                        }

                        separateOrderAmountPayable = separateOrderGoodsTotalPrice
                                - separateOrderMemberDiscount
                                - separateOrderPromotionDiscount
                                - separateOrderCashCouponDiscount
                                - separateOrderProductCouponDiscount
                                - separateOrderLebiDiscount
                                - separateOrderSubventionDiscount;
                        orderBaseInf.setOrderAmt(separateOrderGoodsTotalPrice);
                        orderBaseInf.setRecAmt(separateOrderAmountPayable);
                        orderBaseInf.setCashCouponDiscount(separateOrderCashCouponDiscount);
                        orderBaseInf.setStoreSubventionDiscount(separateOrderSubventionDiscount);
                        orderBaseInf.setLebiDiscount(separateOrderLebiDiscount);
                        orderBaseInf.setMemberDiscount(separateOrderMemberDiscount);
                        orderBaseInf.setPromotionDiscount(separateOrderPromotionDiscount);
                        orderBaseInf.setProductCouponDiscount(separateOrderProductCouponDiscount);
                        orderBaseInf.setSobId(baseInfo.getSobId());
                        orderBaseInf.setDiySiteCode(baseInfo.getStoreCode());
                        orderBaseInf.setStoreOrgId(baseInfo.getStoreOrgId());
                        orderBaseInf.setOrderDate(orderBaseInf.getCreateTime());
                        //订单类型
                        orderBaseInf.setOrderTypeId(4L);
                        orderBaseInf.setOrderSubjectType(baseInfo.getOrderSubjectType());
                        if (baseInfo.getOrderSubjectType() == AppOrderSubjectType.STORE) {
                            orderBaseInf.setUserId(baseInfo.getCustomerId());
                            orderBaseInf.setSalesConsultId(baseInfo.getSalesConsultId());
                        } else {
                            orderBaseInf.setDecorateManagerId(baseInfo.getCreatorId());
                        }
                        if (orderBaseInf.getCashCouponDiscount() > 0 || orderBaseInf.getProductCouponDiscount() > 0) {
                            orderBaseInf.setCouponFlag(AppWhetherFlag.Y);
                        } else {
                            orderBaseInf.setCouponFlag(AppWhetherFlag.N);
                        }
                        orderBaseInf.setProductType(ProductType.getProductTypeByValue(s));
                        orderBaseInf.setInvoiceFlag(AppWhetherFlag.N);
                        if (billingDetail.getEmpCreditMoney() > 0 || billingDetail.getStoreCreditMoney() > 0) {
                            orderBaseInf.setCreditFlag(AppWhetherFlag.Y);
                        } else {
                            orderBaseInf.setCreditFlag(AppWhetherFlag.N);
                        }
                        orderBaseInfList.add(orderBaseInf);
                    } else {
                        // todo 记录拆单错误日志
                    }
                }
                //生成订单券接口信息
                List<OrderCouponInf> couponInfList = new ArrayList<>(10);
                List<OrderCouponInfo> couponInfoList = orderService.getOrderCouponInfoByOrderNumber(orderNumber);
                if (null != couponInfoList && couponInfoList.size() > 0) {
                    for (OrderCouponInfo info : couponInfoList) {
                        OrderCouponInf couponInf = new OrderCouponInf();
                        couponInf.setMainOrderNumber(orderNumber);
                        couponInf.setCreateTime(new Date());
                        couponInf.setCouponType(info.getCouponType());
                        couponInf.setCouponId(info.getCouponId());
                        couponInf.setGetType(info.getGetType());
                        if (info.getCouponType() == OrderCouponType.PRODUCT_COUPON) {
                            if (info.getGetType() == CouponGetType.HISTORY_IMPORT) {
                                couponInf.setHistoryFlag(AppWhetherFlag.Y);
                            } else {
                                couponInf.setHistoryFlag(AppWhetherFlag.N);
                            }
                            for (OrderGoodsInf orderGoodsInf : orderGoodsInfList
                                    ) {
                                if (orderGoodsInf.getSku().equals(info.getSku())) {
                                    couponInf.setProductType(orderGoodsInf.getProductType());
                                    break;
                                }
                            }
                            couponInf.setSku(info.getSku());
                        }
                        couponInf.setPurchasePrice(info.getPurchasePrice());
                        couponInf.setCostPrice(info.getCostPrice());
                        couponInf.setQuantity(1);
                        couponInfList.add(couponInf);
                    }
                }
                //生成收款接口表信息
                List<OrderReceiptInf> receiptInfList = new ArrayList<>(5);
                List<OrderBillingPaymentDetails> billingPaymentDetailsList = orderService.getOrderBillingDetailListByOrderNo(orderNumber);
                if (null != billingPaymentDetailsList && billingPaymentDetailsList.size() > 0) {
                    for (OrderBillingPaymentDetails billing : billingPaymentDetailsList) {
                        OrderReceiptInf receiptInf = new OrderReceiptInf();
                        receiptInf.setMainOrderNumber(billing.getOrderNumber());
                        receiptInf.setDescription(billing.getPayTypeDesc());
                        receiptInf.setAmount(billing.getAmount());
                        receiptInf.setCreateTime(new Date());
                        receiptInf.setReceiptDate(billing.getPayTime());
                        receiptInf.setReceiptType(billing.getPayType());
                        receiptInf.setStoreOrgId(baseInfo.getStoreOrgId());
                        receiptInf.setStoreCode(baseInfo.getStoreCode());
                        receiptInf.setReceiptNumber(billing.getReceiptNumber());
                        receiptInfList.add(receiptInf);
                    }
                }
                //循环保存分单信息,分单商品信息及订单券信息
                supportService.saveSeparateOrderRelevatnInf(orderBaseInfList, orderGoodsInfList, couponInfList, receiptInfList);

            } else {
                //todo 记录拆单错误日志
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrderCouponInf(OrderCouponInf couponInf) {
        if (null != couponInf) {
            separateOrderDAO.saveOrderCouponInf(couponInf);
        }
    }

    @Override
    public void saveOrderReceiptInf(OrderReceiptInf receiptInf) {
        if (null != receiptInf) {
            separateOrderDAO.saveOrderReceiptInf(receiptInf);
        }
    }

    @Override
    public void updateOrderBaseInfoSendFlagAndErrorMessageAndSendTime(String orderNumber, AppWhetherFlag flag, String errorMsg, Date sendTime) {
        if (null != orderNumber) {
            separateOrderDAO.updateOrderBaseInfoSendFlagAndErrorMessageAndSendTime(orderNumber, flag, errorMsg, sendTime);
        }
    }

    @Override
    public void updateOrderGoodsInfoSendFlagAndErrorMessageAndSendTime(Long orderLineId, AppWhetherFlag flag, String errorMsg, Date sendTime) {
        if (null != orderLineId) {
            separateOrderDAO.updateOrderGoodsInfoSendFlagAndErrorMessageAndSendTime(orderLineId, flag, errorMsg, sendTime);
        }
    }

    @Override
    public void sendOrderBaseInfAndOrderGoodsInf(String orderNumber) {
        if (null != orderNumber) {
            List<OrderBaseInf> pendingSendOrderBaseInfs = separateOrderDAO.getPendingSendOrderBaseInf(orderNumber);
            for (OrderBaseInf baseInf : pendingSendOrderBaseInfs) {
                List<OrderGoodsInf> orderGoodsInfList = separateOrderDAO.getOrderGoodsInfByOrderNumber(baseInf.getOrderNumber());
                ebsSenderService.sendOrderAndGoodsToEbsAndRecord(baseInf, orderGoodsInfList);
            }
        }
    }
}

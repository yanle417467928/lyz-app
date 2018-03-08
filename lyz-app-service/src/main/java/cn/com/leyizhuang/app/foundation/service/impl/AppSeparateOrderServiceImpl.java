package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.constant.*;
import cn.com.leyizhuang.app.core.constant.remote.webservice.ebs.ChargeObjType;
import cn.com.leyizhuang.app.core.remote.ebs.EbsSenderService;
import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.core.utils.order.OrderUtils;
import cn.com.leyizhuang.app.foundation.dao.AppSeparateOrderDAO;
import cn.com.leyizhuang.app.foundation.dao.CusPreDepositWithdrawDAO;
import cn.com.leyizhuang.app.foundation.dao.StPreDepositWithdrawDAO;
import cn.com.leyizhuang.app.foundation.pojo.AppStore;
import cn.com.leyizhuang.app.foundation.pojo.StPreDepositWithdraw;
import cn.com.leyizhuang.app.foundation.pojo.WithdrawRefundInfo;
import cn.com.leyizhuang.app.foundation.pojo.order.*;
import cn.com.leyizhuang.app.foundation.pojo.recharge.RechargeOrder;
import cn.com.leyizhuang.app.foundation.pojo.recharge.RechargeReceiptInfo;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.ebs.*;
import cn.com.leyizhuang.app.foundation.pojo.returnorder.*;
import cn.com.leyizhuang.app.foundation.pojo.user.AppCustomer;
import cn.com.leyizhuang.app.foundation.pojo.user.AppCustomerFxStoreRelation;
import cn.com.leyizhuang.app.foundation.pojo.user.CusPreDepositWithdraw;
import cn.com.leyizhuang.app.foundation.service.*;
import cn.com.leyizhuang.common.util.AssertUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.stream.Collectors;

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

    @Resource
    private AppStoreService storeService;

    @Resource
    private RechargeService rechargeService;

    @Resource
    private ReturnOrderService returnOrderService;

    @Resource
    private WithdrawService withdrawService;

    @Resource
    private AppCustomerService customerService;

    @Resource
    private CusPreDepositWithdrawDAO cusPreDepositWithdrawDAO;

    @Resource
    private StPreDepositWithdrawDAO stPreDepositWithdrawDAO;

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
            //主单账单明细
            OrderBillingDetails billingDetail = orderService.getOrderBillingDetail(orderNumber);
            //主单商品明细
            List<OrderGoodsInfo> orderGoodsInfoList = orderService.getOrderGoodsInfoByOrderNumber(orderNumber);
            if (null != orderGoodsInfoList && orderGoodsInfoList.size() > 0) {
                //获取所有companyFlag并加入到set中
                Set<String> companyFlag = orderGoodsInfoList.stream().map(OrderGoodsInfo::getCompanyFlag).collect(Collectors.toSet());
                /*for (OrderGoodsInfo orderGoodsInfo : orderGoodsInfoList) {
                    if (null != orderGoodsInfo.getCompanyFlag()) {
                        companyFlag.add(orderGoodsInfo.getCompanyFlag());
                    } else {
                        //todo 记录拆单错误日志
                    }
                }*/
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
                        //分销仓库为分销门店代下单，设置分销门店编码至订单头 attribute3
                        String fxStoreCode = null;
                        if (baseInfo.getCreatorIdentityType() == AppIdentityType.CUSTOMER ||
                                baseInfo.getCreatorIdentityType() == AppIdentityType.SELLER) {
                            Long customerIdTemp;
                            if (baseInfo.getCreatorIdentityType() == AppIdentityType.SELLER) {
                                customerIdTemp = baseInfo.getCustomerId();
                            } else {
                                customerIdTemp = baseInfo.getCreatorId();
                            }
                            AppCustomerFxStoreRelation customerFxStoreRelation = separateOrderDAO.getCustomerFxStoreRelationByCusId(customerIdTemp);
                            if (null != customerFxStoreRelation) {
                                fxStoreCode = customerFxStoreRelation.getFxStoreCode();
                                orderBaseInf.setAttribute3(fxStoreCode);
                            }
                        }

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
                            if (goodsInfo.getGoodsLineType() == AppGoodsLineType.PRODUCT_COUPON) {
                                separateOrderGoodsTotalPrice += goodsInfo.getRetailPrice() * goodsInfo.getOrderQuantity();
                                separateOrderMemberDiscount += (goodsInfo.getRetailPrice() - goodsInfo.getSettlementPrice()) * goodsInfo.getOrderQuantity();
                                separateOrderProductCouponDiscount += goodsInfo.getSettlementPrice() * goodsInfo.getOrderQuantity();
                            } else {
                                separateOrderGoodsTotalPrice += goodsInfo.getRetailPrice() * goodsInfo.getOrderQuantity();
                                separateOrderMemberDiscount += (goodsInfo.getRetailPrice() - goodsInfo.getSettlementPrice()) * goodsInfo.getOrderQuantity();
                                separateOrderPromotionDiscount += goodsInfo.getPromotionSharePrice() * goodsInfo.getOrderQuantity();
                                separateOrderCashCouponDiscount += goodsInfo.getCashCouponSharePrice() * goodsInfo.getOrderQuantity();
                                separateOrderLebiDiscount += goodsInfo.getLbSharePrice() * goodsInfo.getOrderQuantity();
                                separateOrderSubventionDiscount += goodsInfo.getCashReturnSharePrice() * goodsInfo.getOrderQuantity();
                            }

                            OrderGoodsInf goodsInf = new OrderGoodsInf();
                            goodsInf.setCreateTime(orderBaseInf.getCreateTime());
                            goodsInf.setMainOrderNumber(orderNumber);
                            goodsInf.setOrderNumber(orderBaseInf.getOrderNumber());
                            goodsInf.setMainOrderLineId(goodsInfo.getId());
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
                            couponInf.setCouponTypeId(1L);
                        } else {
                            couponInf.setCouponTypeId(2L);
                        }
                        couponInf.setBuyPrice(info.getPurchasePrice());
                        couponInf.setCostPrice(info.getCostPrice());
                        couponInf.setQuantity(1);
                        couponInf.setSobId(baseInfo.getSobId());
                        couponInfList.add(couponInf);
                    }
                }
                //生成订单收款接口表信息
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
                        receiptInf.setDiySiteCode(baseInfo.getStoreCode());
                        receiptInf.setReceiptNumber(billing.getReceiptNumber());
                        receiptInf.setSobId(baseInfo.getSobId());
                        receiptInf.setUserId(null == baseInfo.getCustomerId() ? baseInfo.getCreatorId() : baseInfo.getCustomerId());
                        receiptInf.setUserPhone(null == baseInfo.getCustomerPhone() ? baseInfo.getCreatorPhone() : baseInfo.getCustomerPhone());
                        receiptInf.setUsername(null == baseInfo.getCustomerName() ? baseInfo.getCreatorName() : baseInfo.getCustomerName());
                        receiptInf.setGuideId(baseInfo.getSalesConsultId());
                        if (StringUtils.isNotBlank(orderBaseInfList.get(0).getAttribute3())) {
                            receiptInf.setAttribute3(orderBaseInfList.get(0).getAttribute3());
                        }
                        receiptInfList.add(receiptInf);
                    }
                }
                //订单经销差价返还 接口拆单
                List<OrderJxPriceDifferenceReturnDetails> returnDetails = orderService.getOrderJxPriceDifferenceReturnDetailsByOrderNumber(orderNumber);
                List<OrderJxPriceDifferenceReturnInf> jxPriceDifferenceReturnInfs = new ArrayList<>(20);
                if (null != returnDetails && returnDetails.size() > AppConstant.INTEGER_ZERO) {
                    for (OrderJxPriceDifferenceReturnDetails jxPriceDifferenceReturnDetails : returnDetails) {
                        OrderJxPriceDifferenceReturnInf returnInf = new OrderJxPriceDifferenceReturnInf();
                        returnInf.setAmount(jxPriceDifferenceReturnDetails.getAmount());
                        returnInf.setCreateTime(new Date());
                        returnInf.setMainOrderNumber(jxPriceDifferenceReturnDetails.getOrderNumber());
                        returnInf.setReceiptDate(jxPriceDifferenceReturnDetails.getCreateTime());
                        returnInf.setSku(jxPriceDifferenceReturnDetails.getSku());
                        returnInf.setSobId(baseInfo.getSobId());
                        returnInf.setStoreOrgCode(baseInfo.getStoreStructureCode());
                        returnInf.setDiySiteCode(jxPriceDifferenceReturnDetails.getStoreCode());
                        returnInf.setReceiptNumber(jxPriceDifferenceReturnDetails.getReceiptNumber());
                        jxPriceDifferenceReturnInfs.add(returnInf);
                    }
                }

                //订单关键信息接口
                OrderKeyInf orderKeyInf = new OrderKeyInf();
                orderKeyInf.setType(AppFreightOrderType.ORDER);
                orderKeyInf.setMainOrderNumber(baseInfo.getOrderNumber());
                orderKeyInf.setMainOrderNumber(baseInfo.getOrderNumber());
                orderKeyInf.setFreight(null == billingDetail.getFreight() ? 0D : billingDetail.getFreight());
                orderKeyInf.setOrderAmt(orderBaseInfList.stream().mapToDouble(OrderBaseInf::getOrderAmt).sum());
                orderKeyInf.setGoodsAmt(orderGoodsInfoList.stream().mapToDouble(p -> p.getOrderQuantity()
                        * p.getSettlementPrice()).sum());
                orderKeyInf.setArrearage(null == billingDetail.getArrearage() ? 0D : billingDetail.getArrearage());
                orderKeyInf.setDecCreditMoney(baseInfo.getOrderSubjectType() == AppOrderSubjectType.STORE ? 0D :
                        (null == billingDetail.getStoreCreditMoney() ? 0D : billingDetail.getStoreCreditMoney()));
                orderKeyInf.setDecPreDeposit(baseInfo.getOrderSubjectType() == AppOrderSubjectType.STORE ? 0D :
                        (null == billingDetail.getStPreDeposit() ? 0D : billingDetail.getStPreDeposit()));
                orderKeyInf.setDecSubvention(null == billingDetail.getStoreSubvention() ? 0D : billingDetail.getStoreSubvention());
                orderKeyInf.setEmpCreditMoney(null == billingDetail.getEmpCreditMoney() ? 0D : billingDetail.getEmpCreditMoney());
                //循环保存分单信息,分单商品信息及订单券信息
                supportService.saveSeparateOrderRelevantInf(orderBaseInfList, orderGoodsInfList, couponInfList,
                        receiptInfList, jxPriceDifferenceReturnInfs, orderKeyInf);

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
    public void updateOrderBaseInfSendFlagAndErrorMessageAndSendTime(String orderNumber, AppWhetherFlag flag, String errorMsg, Date sendTime) {
        if (null != orderNumber) {
            separateOrderDAO.updateOrderBaseInfSendFlagAndErrorMessageAndSendTime(orderNumber, flag, errorMsg, sendTime);
        }
    }


    @Override
    public void sendOrderBaseInfAndOrderGoodsInf(String orderNumber) {
        if (null != orderNumber) {
            //发送订单头、商品行
            List<OrderBaseInf> pendingSendOrderBaseInfs = separateOrderDAO.getPendingSendOrderBaseInf(orderNumber);
            for (OrderBaseInf baseInf : pendingSendOrderBaseInfs) {
                List<OrderGoodsInf> orderGoodsInfList = separateOrderDAO.getOrderGoodsInfByOrderNumber(baseInf.getOrderNumber());
                ebsSenderService.sendOrderAndGoodsToEbsAndRecord(baseInf, orderGoodsInfList);
            }

        }
    }

    @Override
    public void sendOrderReceiptInf(String orderNumber) {
        if (null != orderNumber) {
            List<OrderReceiptInf> receiptInfList = separateOrderDAO.getOrderReceiptInf(orderNumber);
            if (null != receiptInfList && receiptInfList.size() > 0) {
                ebsSenderService.sendOrderReceiptInfAndRecord(receiptInfList);
            }
        }
    }

    @Override
    public void sendOrderCouponInf(String orderNumber) {
        if (null != orderNumber) {
            List<OrderCouponInf> orderCouponInfList = separateOrderDAO.getOrderCouponInf(orderNumber);
            if (null != orderCouponInfList && orderCouponInfList.size() > 0) {
                ebsSenderService.sendOrderCouponInfAndRecord(orderCouponInfList);
            }
        }
    }

    @Override
    public void updateOrderCouponFlagAndSendTimeAndErrorMsg(List<Long> couponInfIds, String msg, Date sendTime, AppWhetherFlag flag) {
        if (null != couponInfIds && couponInfIds.size() > 0) {
            separateOrderDAO.updateOrderCouponFlagAndSendTimeAndErrorMsg(couponInfIds, msg, sendTime, flag);
        }
    }

    @Override
    public void updateOrderGoodsInfByOrderNumber(String orderNumber, AppWhetherFlag flag, String message, Date sendTime) {
        if (null != orderNumber) {
            separateOrderDAO.updateOrderGoodsInfByOrderNumber(orderNumber, flag, message, sendTime);
        }
    }

    @Override
    public void updateOrderReceiptFlagAndSendTimeAndErrorMsg(List<Long> receiptInfIds, String msg, Date sendTime, AppWhetherFlag flag) {
        if (null != receiptInfIds && receiptInfIds.size() > 0) {
            separateOrderDAO.updateOrderReceiptFlagAndSendTimeAndErrorMsg(receiptInfIds, msg, sendTime, flag);
        }
    }

    @Override
    public Boolean isRechargeReceiptExist(String rechargeNo) {
        if (null != rechargeNo) {
            return separateOrderDAO.isRechargeReceiptExist(rechargeNo);
        }
        return true;
    }

    @Override
    public void separateRechargeReceipt(String rechargeNo) {
        List<RechargeReceiptInfo> rechargeReceiptInfoList = rechargeService.findRechargeReceiptInfoByRechargeNo(rechargeNo);
        List<RechargeOrder> rechargeOrderList = rechargeService.findRechargeOrderByRechargeNo(rechargeNo);
        AppStore store = storeService.findById(rechargeOrderList.get(0).getStoreId());

        if (null != rechargeReceiptInfoList && rechargeReceiptInfoList.size() > 0) {
            for (RechargeReceiptInfo rechargeReceiptInfo : rechargeReceiptInfoList) {
                RechargeReceiptInf rechargeReceiptInf = new RechargeReceiptInf();
                rechargeReceiptInf.setAmount(rechargeReceiptInfo.getAmount());
                rechargeReceiptInf.setChargeNumber(rechargeReceiptInfo.getRechargeNo());
                rechargeReceiptInf.setReceiptNumber(rechargeReceiptInfo.getReceiptNumber());
                switch (rechargeReceiptInfo.getRechargeAccountType()) {
                    case CUS_PREPAY:
                        rechargeReceiptInf.setChargeObj(ChargeObjType.CUSTOMER);
                        break;
                    case ST_PREPAY:
                        rechargeReceiptInf.setChargeObj(ChargeObjType.STORE);
                        break;
                    case PRODUCT_COUPON:
                        rechargeReceiptInf.setChargeObj(ChargeObjType.PRODUCT_COUPON);
                        break;
                    case BOND:
                        rechargeReceiptInf.setChargeObj(ChargeObjType.BOND);
                        break;
                    default:
                        break;
                }
                rechargeReceiptInf.setChargeType(rechargeReceiptInfo.getChargeType());
                rechargeReceiptInf.setReceiptType(rechargeReceiptInfo.getPayType());
                rechargeReceiptInf.setDescription(rechargeReceiptInf.getReceiptType().getDescription());
                rechargeReceiptInf.setDiySiteCode(store.getStoreCode());
                rechargeReceiptInf.setStoreOrgCode(store.getStoreStructureCode());
                rechargeReceiptInf.setSobId(store.getSobId());
                rechargeReceiptInf.setReceiptDate(rechargeReceiptInfo.getPayTime());
                if (rechargeReceiptInfo.getRechargeAccountType() == RechargeAccountType.CUS_PREPAY || rechargeReceiptInfo.getRechargeAccountType() == RechargeAccountType.PRODUCT_COUPON) {
                    rechargeReceiptInf.setUserid(rechargeOrderList.get(0).getCustomerId());
                }

                separateOrderDAO.saveRechargeReceiptInf(rechargeReceiptInf);
            }
        } else {
            //todo 记录充值收款拆单错误日志
        }


    }

    @Override
    public void sendRechargeReceiptInf(String rechargeNo) {
        if (null != rechargeNo) {
            List<RechargeReceiptInf> receiptInfList = separateOrderDAO.getRechargeReceiptInfByRechargeNo(rechargeNo);
            if (null != receiptInfList && receiptInfList.size() > 0) {
                for (RechargeReceiptInf receiptInf : receiptInfList) {
                    ebsSenderService.sendRechargeReceiptInfAndRecord(receiptInf);
                }
            }
        }
    }

    @Override
    public void updateRechargeReceiptFlagAndSendTimeAndErrorMsg(Long receiptId, String msg, Date sendTime, AppWhetherFlag flag) {
        if (null != receiptId) {
            separateOrderDAO.updateRechargeReceiptFlagAndSendTimeAndErrorMsg(receiptId, msg, sendTime, flag);
        }
    }

    @Override
    public void saveOrderJxPriceDifferenceReturnInf(OrderJxPriceDifferenceReturnInf returnInf) {
        if (null != returnInf) {
            separateOrderDAO.saveOrderJxPriceDifferenceReturnInf(returnInf);
        }
    }

    @Override
    public void sendOrderJxPriceDifferenceReturnInf(String orderNumber) {
        if (StringUtils.isNotBlank(orderNumber)) {
            List<OrderJxPriceDifferenceReturnInf> jxPriceDifferenceReturnInfs = separateOrderDAO.getOrderJxPriceDifferenceReturnInf(orderNumber);
            if (null != jxPriceDifferenceReturnInfs && jxPriceDifferenceReturnInfs.size() > 0) {
                ebsSenderService.sendOrderJxPriceDifferenceReturnInfAndRecord(jxPriceDifferenceReturnInfs);
            }
        }
    }

  /*  @Override
    public void sendOrderJxPriceDifferenceRefundInf(String returnNumber) {
        if (StringUtils.isNotBlank(returnNumber)) {
            List<ReturnOrderJxPriceDifferenceRefundInf> jxPriceDifferenceRefundInfs = separateOrderDAO.getOrderJxPriceDifferenceRefundInf(returnNumber);
            if (null != jxPriceDifferenceRefundInfs && jxPriceDifferenceRefundInfs.size() > 0) {
                ebsSenderService.sendOrderJxPriceDifferenceRefundInfAndRecord(jxPriceDifferenceRefundInfs);
            }
        }
    }*/

    @Override
    public void updateOrderJxPriceDifferenceReturnInf(List<Long> returnInfIds, String msg, Date sendTime, AppWhetherFlag flag) {
        if (null != returnInfIds && returnInfIds.size() > 0) {
            separateOrderDAO.updateOrderJxPriceDifferenceReturnInf(returnInfIds, msg, sendTime, flag);
        }
    }

    @Override
    public Boolean isReturnOrderExist(String returnNumber) {
        if (null != returnNumber) {
            return separateOrderDAO.isReturnOrderExist(returnNumber);
        }
        return false;
    }


    @Override
    public void separateReturnOrder(String returnNumber) {
        //查找主退单信息
        ReturnOrderBaseInfo returnOrderBaseInfo = returnOrderService.queryByReturnNo(returnNumber);
        if (null != returnOrderBaseInfo) {
            //查找主退单对应的主订单信息
            OrderBaseInfo orderBaseInfo = orderService.getOrderByOrderNumber(returnOrderBaseInfo.getOrderNo());
            if (null != orderBaseInfo) {
                //根据主退单号获取主退单下所有商品
                List<ReturnOrderGoodsInfo> returnOrderGoodsInfoList = returnOrderService.findReturnOrderGoodsInfoByOrderNumber(returnNumber);
                //提取主退单下所有companyFlag
                Set<String> companyFlag = returnOrderGoodsInfoList.stream().map(ReturnOrderGoodsInfo::getCompanyFlag).collect(Collectors.toSet());
                //创建一个map,用来存储各个companyFlag下对应的商品信息
                Map<String, List<ReturnOrderGoodsInfo>> separateReturnOrderGoodsInfoMap = new HashMap<>(5);
                //创建一个map用于存储拆单后退单头和退单商品信息
                Map<ReturnOrderBaseInf, List<ReturnOrderGoodsInf>> returnOrderParamMap = new HashMap<>(5);

                //*********************************** 拆退单头及退单商品信息 begin *************************************
                String fxStoreCode = null;
                for (String flag : companyFlag) {
                    //查找退单该flag对应的原分单信息
                    OrderBaseInf orderBaseInf = separateOrderDAO.getOrderBaseInfByMainOrderNumberAndCompanFlag(orderBaseInfo.getOrderNumber(), flag);
                    //查找退单该flag对应的原分单商品信息
                    List<OrderGoodsInf> orderGoodsInfList;
                    if (null != orderBaseInf) {
                        orderGoodsInfList = separateOrderDAO.getOrderGoodsInfByOrderNumber(orderBaseInf.getOrderNumber());
                        List<ReturnOrderGoodsInfo> separateReturnOrderGoodsInfoList = returnOrderGoodsInfoList.parallelStream().
                                filter(p -> p.getCompanyFlag().equals(flag)).collect(Collectors.toList());
                        separateReturnOrderGoodsInfoMap.put(flag, separateReturnOrderGoodsInfoList);

                        //创建分退单头信息
                        ReturnOrderBaseInf returnOrderBaseInf = new ReturnOrderBaseInf();
                        returnOrderBaseInf.setCreateTime(new Date());
                        returnOrderBaseInf.setDeliverTypeTitle(orderBaseInfo.getDeliveryType());
                        returnOrderBaseInf.setDiySiteCode(orderBaseInfo.getStoreCode());
                        returnOrderBaseInf.setMainOrderNumber(orderBaseInfo.getOrderNumber());
                        returnOrderBaseInf.setOrderNumber(orderBaseInf.getOrderNumber());
                        returnOrderBaseInf.setMainReturnNumber(returnOrderBaseInfo.getReturnNo());
                        returnOrderBaseInf.setReturnNumber(OrderUtils.generateSeparateReturnOrderNumber(flag, returnOrderBaseInfo.getReturnNo()));
                        returnOrderBaseInf.setOrderTypeId(orderBaseInf.getOrderTypeId());
                        returnOrderBaseInf.setRefundAmount(returnOrderBaseInfo.getReturnPrice());
                        returnOrderBaseInf.setReturnDate(returnOrderBaseInfo.getReturnTime());
                        returnOrderBaseInf.setReturnType(returnOrderBaseInfo.getReturnType());
                        returnOrderBaseInf.setRtFullFlag(returnOrderBaseInfo.getReturnType() == ReturnOrderType.NORMAL_RETURN ? AppWhetherFlag.N : AppWhetherFlag.Y);
                        returnOrderBaseInf.setSellerId(orderBaseInf.getSalesConsultId());
                        returnOrderBaseInf.setUserId(orderBaseInf.getUserId());
                        returnOrderBaseInf.setSobId(orderBaseInf.getSobId());
                        returnOrderBaseInf.setStoreOrgCode(orderBaseInfo.getStoreStructureCode());
                        //分销仓库为分销门店代下单，设置分销门店编码至订单头 attribute3
                        if (StringUtils.isNotBlank(orderBaseInf.getAttribute3())) {
                            fxStoreCode = orderBaseInf.getAttribute3();
                            returnOrderBaseInf.setAttribute3(fxStoreCode);
                        }

                        //创建分退单产品信息
                        List<ReturnOrderGoodsInf> returnOrderGoodsInfList = new ArrayList<>(20);
                        if (null != separateReturnOrderGoodsInfoList && separateReturnOrderGoodsInfoList.size() > 0) {
                            for (ReturnOrderGoodsInfo returnGoodsInfo : separateReturnOrderGoodsInfoList) {
                                ReturnOrderGoodsInf returnGoodsInf = new ReturnOrderGoodsInf();
                                returnGoodsInf.setMainOrderNumber(returnOrderBaseInf.getMainOrderNumber());
                                returnGoodsInf.setOrderNumber(returnOrderBaseInf.getOrderNumber());
                                returnGoodsInf.setMainReturnNumber(returnOrderBaseInf.getMainReturnNumber());
                                returnGoodsInf.setReturnNumber(returnOrderBaseInf.getReturnNumber());
                                returnGoodsInf.setOrderLineId(orderGoodsInfList.stream().filter(p -> p.getMainOrderLineId().
                                        equals(returnGoodsInfo.getOrderGoodsId())).collect(Collectors.toList()).get(0).getOrderLineId());
                                returnGoodsInf.setCreateTime(returnOrderBaseInf.getCreateTime());
                                returnGoodsInf.setGoodsTitle(returnGoodsInfo.getSkuName());
                                returnGoodsInf.setHyPrice(returnGoodsInfo.getVipPrice());
                                returnGoodsInf.setJxPrice(returnGoodsInfo.getWholesalePrice());
                                returnGoodsInf.setLsPrice(returnGoodsInfo.getRetailPrice());
                                returnGoodsInf.setSettlementPrice(returnGoodsInfo.getSettlementPrice());
                                returnGoodsInf.setQuantity(returnGoodsInfo.getReturnQty());
                                returnGoodsInf.setReturnPrice(returnGoodsInfo.getReturnPrice());
                                returnGoodsInf.setSku(returnGoodsInfo.getSku());
                                returnOrderGoodsInfList.add(returnGoodsInf);
                            }
                        }
                        returnOrderParamMap.put(returnOrderBaseInf, returnOrderGoodsInfList);
                    } else {
                        throw new RuntimeException("未找到原分单信息,退单拆单失败!");
                    }
                }
                //*********************************** 拆退单头及退单商品信息 end ***************************************


                //************************************* 生成退单券信息 begin *****************************************

                List<ReturnOrderCouponInf> returnOrderCouponInfList = new ArrayList<>(20);
                //生成退单优惠券列表
                List<ReturnOrderCashCoupon> returnOrderCashCouponList = returnOrderService.
                        getReturnOrderCashCouponByRoid(returnOrderBaseInfo.getRoid());
                if (null != returnOrderCashCouponList && returnOrderCashCouponList.size() > 0) {
                    for (ReturnOrderCashCoupon cashCoupon : returnOrderCashCouponList) {
                        ReturnOrderCouponInf returnOrderCouponInf = new ReturnOrderCouponInf();
                        returnOrderCouponInf.setBuyPrice(cashCoupon.getPurchasePrice());
                        returnOrderCouponInf.setMainOrderNumber(orderBaseInfo.getOrderNumber());
                        returnOrderCouponInf.setMainReturnNumber(returnOrderBaseInfo.getReturnNo());
                        returnOrderCouponInf.setCouponTypeId(2L);
                        returnOrderCouponInf.setQuantity(1);
                        returnOrderCouponInfList.add(returnOrderCouponInf);
                    }
                }
                //生成退单产品券列表
                List<ReturnOrderProductCoupon> returnOrderProductCouponList = returnOrderService.getReturnOrderProductCouponByRoid(returnOrderBaseInfo.getRoid());
                if (null != returnOrderProductCouponList && returnOrderProductCouponList.size() > 0) {
                    for (ReturnOrderProductCoupon productCoupon : returnOrderProductCouponList) {
                        ReturnOrderCouponInf returnOrderCouponInf = new ReturnOrderCouponInf();
                        returnOrderCouponInf.setBuyPrice(productCoupon.getPurchasePrice());
                        returnOrderCouponInf.setMainOrderNumber(orderBaseInfo.getOrderNumber());
                        returnOrderCouponInf.setMainReturnNumber(returnOrderBaseInfo.getReturnNo());
                        returnOrderCouponInf.setCouponTypeId(1L);
                        returnOrderCouponInf.setQuantity(1);
                        returnOrderCouponInf.setSku(productCoupon.getSku());
                        returnOrderCouponInfList.add(returnOrderCouponInf);
                    }
                }
                //************************************* 生成退单券信息 end *******************************************


                //************************************ 生成退单退款信息 begin ******************************************

                List<ReturnOrderRefundInf> returnOrderRefundInfList = new ArrayList<>(10);
                List<ReturnOrderBillingDetail> returnOrderBillingDetailList = returnOrderService.
                        getReturnOrderBillingDetailByRoid(returnOrderBaseInfo.getRoid());
                if (null != returnOrderBillingDetailList && returnOrderBillingDetailList.size() > 0) {
                    for (ReturnOrderBillingDetail billingDetail : returnOrderBillingDetailList) {
                        ReturnOrderRefundInf refundInf = new ReturnOrderRefundInf();
                        refundInf.setAmount(billingDetail.getReturnMoney());
                        refundInf.setCreateTime(billingDetail.getCreateTime());
                        refundInf.setDiySiteCode(orderBaseInfo.getStoreCode());
                        refundInf.setMainOrderNumber(orderBaseInfo.getOrderNumber());
                        refundInf.setMainReturnNumber(returnOrderBaseInfo.getReturnNo());
                        refundInf.setStoreOrgCode(orderBaseInfo.getStoreStructureCode());
                        refundInf.setUserId(returnOrderBaseInfo.getCreatorIdentityType() == AppIdentityType.SELLER ?
                                returnOrderBaseInfo.getCustomerId() : returnOrderBaseInfo.getCreatorId());
                        refundInf.setRefundDate(billingDetail.getIntoAmountTime());
                        refundInf.setRefundNumber(OrderUtils.getRefundNumber());
                        refundInf.setRefundType(billingDetail.getReturnPayType());
                        refundInf.setSobId(orderBaseInfo.getSobId());
                        refundInf.setDescription(null != refundInf.getRefundType() ? refundInf.getRefundType().getDescription() : "");
                        //设置分销门店编码至attribute3
                        if (StringUtils.isNotBlank(fxStoreCode)) {
                            refundInf.setAttribute3(fxStoreCode);
                        }
                        returnOrderRefundInfList.add(refundInf);
                    }
                }

                //************************************ 生成退单退款信息 end ********************************************

                //******************************* 生成退订单退经销差价信息 begin ***************************************
                List<ReturnOrderJxPriceDifferenceRefundInf> jxPriceDifferenceRefundInfList = new ArrayList<>(20);
                List<ReturnOrderJxPriceDifferenceRefundDetails> detailsList = returnOrderService.
                        getReturnOrderJxPriceDifferenceRefundDetailsByReturnNumber(returnOrderBaseInfo.getReturnNo());
                if (AssertUtil.isNotEmpty(detailsList)) {
                    for (ReturnOrderJxPriceDifferenceRefundDetails details : detailsList) {
                        ReturnOrderJxPriceDifferenceRefundInf inf = new ReturnOrderJxPriceDifferenceRefundInf();
                        inf.setAmount(details.getAmount());
                        inf.setCreateTime(details.getCreateTime());
                        inf.setMainReturnNumber(returnOrderBaseInfo.getReturnNo());
                        inf.setMainOrderNumber(returnOrderBaseInfo.getOrderNo());
                        inf.setRefundDate(returnOrderBaseInfo.getReturnTime());
                        inf.setSku(details.getSku());
                        inf.setSobId(orderBaseInfo.getSobId());
                        inf.setStoreOrgCode(orderBaseInfo.getStoreStructureCode());
                        inf.setDiySiteCode(details.getStoreCode());
                        inf.setRefundNumber(details.getRefundNumber());
                        jxPriceDifferenceRefundInfList.add(inf);
                    }
                }
                //******************************* 生成退订单退经销差价信息 end ***************************************

                //保存退单拆单信息
                supportService.saveSeparateReturnOrderRelevantInf(returnOrderParamMap, returnOrderCouponInfList, returnOrderRefundInfList, jxPriceDifferenceRefundInfList);

            } else {
                throw new RuntimeException("为找到原主单信息,退单拆单失败!");
            }

        }
    }

    @Override
    public void updateOrderJxPriceDifferenceRefundInf(List<Long> refundInfIds, String msg, Date sendTime, AppWhetherFlag flag) {
        if (null != refundInfIds && refundInfIds.size() > 0) {
            separateOrderDAO.updateOrderJxPriceDifferenceRefundInf(refundInfIds, msg, sendTime, flag);
        }
    }

    @Override
    public void saveOrderJxPriceDifferenceRefundInf(ReturnOrderJxPriceDifferenceRefundInf refundInf) {
        if (null != refundInf) {
            separateOrderDAO.saveOrderJxPriceDifferenceRefundInf(refundInf);
        }
    }

    @Override
    public void updateOrderReceiveFlagAndSendTimeAndErrorMsg(Long receiveInfsId, String msg, Date sendTime, AppWhetherFlag flag) {
        if (null != receiveInfsId) {
            separateOrderDAO.updateOrderReceiveFlagAndSendTimeAndErrorMsg(receiveInfsId, msg, sendTime, flag);
        }
    }


    @Override
    public void updateReturnOrderFlagAndSendTimeAndErrorMsg(Long rtHeaderId, String msg, Date sendTime, AppWhetherFlag flag) {
        if (null != rtHeaderId) {
            separateOrderDAO.updateReturnOrderFlagAndSendTimeAndErrorMsg(rtHeaderId, msg, sendTime, flag);
        }
    }

    @Override
    public void saveReturnOrderBaseInf(ReturnOrderBaseInf returnOrderBaseInf) {
        if (null != returnOrderBaseInf) {
            separateOrderDAO.saveReturnOrderBaseInf(returnOrderBaseInf);
        }
    }

    @Override
    public void saveReturnOrderGoodsInf(ReturnOrderGoodsInf returnOrderGoodsInf) {
        if (null != returnOrderGoodsInf) {
            separateOrderDAO.saveReturnOrderGoodsInf(returnOrderGoodsInf);
        }
    }

    @Override
    public void saveReturnOrderCouponInf(ReturnOrderCouponInf returnOrderCouponInf) {
        if (null != returnOrderCouponInf) {
            separateOrderDAO.saveReturnOrderCouponInf(returnOrderCouponInf);
        }
    }

    @Override
    public void saveReturnOrderRefundInf(ReturnOrderRefundInf returnOrderRefundInf) {
        if (null != returnOrderRefundInf) {
            separateOrderDAO.saveReturnOrderRefundInf(returnOrderRefundInf);
        }
    }

    @Override
    public void sendReturnOrderBaseInfAndReturnOrderGoodsInf(String returnNumber) {
        if (null != returnNumber) {
            //发送退单头、商品行
            List<ReturnOrderBaseInf> returnOrderBaseInfList = separateOrderDAO.getReturnOrderBaseInfByReturnNumber(returnNumber);
            for (ReturnOrderBaseInf baseInf : returnOrderBaseInfList) {
                List<ReturnOrderGoodsInf> returnOrderGoodsInfList = separateOrderDAO.getReturnOrderGoodsInfByReturnNumber(baseInf.getReturnNumber());
                ebsSenderService.sendReturnOrderAndReturnGoodsToEbsAndRecord(baseInf, returnOrderGoodsInfList);
            }

        }
    }

    @Override
    public void updateReturnOrderBaseInf(String returnNumber, AppWhetherFlag flag, String errorMsg, Date sendTime) {
        if (null != returnNumber) {
            separateOrderDAO.updateReturnOrderBaseInf(returnNumber, flag, errorMsg, sendTime);
        }
    }

    @Override
    public void updateReturnOrderGoodsInf(String returnNumber, AppWhetherFlag flag, String errorMsg, Date sendTime) {
        if (null != returnNumber) {
            separateOrderDAO.updateReturnOrderGoodsInf(returnNumber, flag, errorMsg, sendTime);
        }
    }

    @Override
    public void sendReturnOrderCouponInf(String returnNumber) {
        if (null != returnNumber) {
            List<ReturnOrderCouponInf> returnOrderCouponInfList = separateOrderDAO.getReturnOrderCouponInf(returnNumber);
            if (AssertUtil.isNotEmpty(returnOrderCouponInfList)) {
                ebsSenderService.sendReturnOrderCouponInfAndRecord(returnOrderCouponInfList);
            }
        }
    }

    @Override
    public void updateReturnOrderCouponInf(List<Long> returnCouponInfLineId, String msg, Date sendTime, AppWhetherFlag flag) {
        if (AssertUtil.isNotEmpty(returnCouponInfLineId)) {
            separateOrderDAO.updateReturnOrderCouponInf(returnCouponInfLineId, msg, sendTime, flag);
        }
    }

    @Override
    public void sendReturnOrderRefundInf(String returnNumber) {
        if (null != returnNumber) {
            List<ReturnOrderRefundInf> returnOrderRefundInfList = separateOrderDAO.getReturnOrderRefundInf(returnNumber);
            if (AssertUtil.isNotEmpty(returnOrderRefundInfList)) {
                ebsSenderService.sendReturnOrderRefundInfAndRecord(returnOrderRefundInfList);
            }
        }
    }

    @Override
    public void updateReturnOrderRefundInf(List<Long> refundInfIds, String msg, Date sendTime, AppWhetherFlag flag) {
        if (AssertUtil.isNotEmpty(refundInfIds)) {
            separateOrderDAO.updateReturnOrderRefundInf(refundInfIds, msg, sendTime, flag);
        }
    }

    @Override
    public void sendReturnOrderJxPriceDifferenceRefundInf(String returnNumber) {
        if (StringUtils.isNotBlank(returnNumber)) {
            List<ReturnOrderJxPriceDifferenceRefundInf> jxPriceDifferenceRefundInfs = separateOrderDAO.getReturnOrderJxPriceDifferenceRefundInf(returnNumber);
            if (AssertUtil.isNotEmpty(jxPriceDifferenceRefundInfs)) {
                ebsSenderService.sendReturnOrderJxPriceDifferenceRefundInfAndRecord(jxPriceDifferenceRefundInfs);
            }
        }
    }

    @Override
    public void updateReturnOrderJxPriceDifferenceRefundInf(List<Long> refundInfIds, String msg, Date sendTime, AppWhetherFlag flag) {
        if (AssertUtil.isNotEmpty(refundInfIds)) {
            separateOrderDAO.updateReturnOrderJxPriceDifferenceRefundInf(refundInfIds, msg, sendTime, flag);
        }
    }

    @Override
    public void saveOrderKeyInf(OrderKeyInf orderKeyInf) {
        if (null != orderKeyInf) {
            separateOrderDAO.saveOrderKeyInf(orderKeyInf);
        }
    }

    @Override
    public void sendOrderKeyInf(String orderNumber) {
        if (StringUtils.isNotBlank(orderNumber)) {
            OrderKeyInf orderKeyInf = separateOrderDAO.getOrderKeyInfByMainOrderNumber(orderNumber);
            if (null != orderKeyInf) {
                ebsSenderService.sendOrderKeyInfAndRecord(orderKeyInf);
            }
        }
    }

    @Override
    public void updateOrderKeyInfFlagAndSendTimeAndErrorMsg(Long id, String msg, Date sendTime, AppWhetherFlag sendFlag) {
        if (null != id) {
            separateOrderDAO.updateOrderKeyInfFlagAndSendTimeAndErrorMsg(id, msg, sendTime, sendFlag);
        }
    }

    @Override
    public Boolean isWithdrawRefundExist(String refundNo) {
        if (null != refundNo) {
            return separateOrderDAO.isWithdrawRefundExist(refundNo);
        }
        return null;
    }

    @Override
    public void separateWithdrawRefund(String refundNo) throws UnsupportedEncodingException {
        if (null != refundNo) {
            WithdrawRefundInfo withdrawRefundInfo = withdrawService.getWithdrawRefundInfoByRefundNo(refundNo);
            WithdrawRefundInf withdrawRefundInf = new WithdrawRefundInf();
            withdrawRefundInf.setAmount(withdrawRefundInfo.getWithdrawAmount());
            withdrawRefundInf.setCreateTime(new Date());
            withdrawRefundInf.setWithdrawNumber(withdrawRefundInfo.getWithdrawNo());
            withdrawRefundInf.setRefundNumber(withdrawRefundInfo.getRefundNumber());
            switch (withdrawRefundInfo.getWithdrawSubjectType()) {
                case CUSTOMER:
                    withdrawRefundInf.setWithdrawObj(1);
                    break;
                case STORE:
                    withdrawRefundInf.setWithdrawObj(2);
                    break;
                default:
                    break;
            }
            //withdrawRefundInf.setWithdrawType(withdrawRefundInfo);
            switch (withdrawRefundInfo.getWithdrawChannel()) {
                case ALIPAY:
                    withdrawRefundInf.setRefundType(1);
                    break;
                case WE_CHAT:
                    withdrawRefundInf.setRefundType(2);
                    break;
                case UNION_PAY:
                    withdrawRefundInf.setRefundType(3);
                    break;
                default:
                    break;
            }
            withdrawRefundInf.setDescription(withdrawRefundInfo.getWithdrawChannel().getDescription());
            withdrawRefundInf.setRefundDate(withdrawRefundInfo.getCreateTime());

            //获取充值顾客信息
            if (withdrawRefundInfo.getWithdrawAccountType() == RechargeAccountType.CUS_PREPAY) {
                CusPreDepositWithdraw cusPreDepositWithdraw = cusPreDepositWithdrawDAO.findByApplyNo(withdrawRefundInfo.getWithdrawNo());
                AppCustomer customer = customerService.findById(cusPreDepositWithdraw.getApplyCusId());
                AppStore store = storeService.findById(customer.getStoreId());
                withdrawRefundInf.setUserid(cusPreDepositWithdraw.getApplyCusId());
                withdrawRefundInf.setSobId(store.getSobId());
                withdrawRefundInf.setStoreOrgCode(store.getStoreStructureCode());
                withdrawRefundInf.setDiySiteCode(store.getStoreCode());
            }
            //获取门店充值信息
            else if (withdrawRefundInfo.getWithdrawAccountType() == RechargeAccountType.ST_PREPAY) {
                StPreDepositWithdraw stPreDepositWithdraw = stPreDepositWithdrawDAO.findByApplyNo(withdrawRefundInfo.getWithdrawNo());
                AppStore store = storeService.findById(stPreDepositWithdraw.getApplyStId());
                withdrawRefundInf.setDiySiteCode(store.getStoreCode());
                withdrawRefundInf.setStoreOrgCode(store.getStoreStructureCode());
                withdrawRefundInf.setSobId(store.getSobId());
            }


            //保存提现退款接口信息
            separateOrderDAO.saveWithdrawRefundInf(withdrawRefundInf);
        }
    }

    @Override
    public void sendWithdrawRefundInf(String refundNo) {
        if (null != refundNo) {
            WithdrawRefundInf refundInf = separateOrderDAO.getWithdrawRefundInfByRefundNo(refundNo);
            if (null != refundInf) {
                ebsSenderService.sendWithdrawRefundInfAndRecord(refundInf);
            }
        }
    }

    @Override
    public void updateWithdrawRefundFlagAndSendTimeAndErrorMsg(Long refundId, String msg, Date sendTime, AppWhetherFlag flag) {
        if (null != refundId) {
            separateOrderDAO.updateWithdrawRefundFlagAndSendTimeAndErrorMsg(refundId, msg, sendTime, flag);
        }
    }

    @Override
    public Boolean isReceiptExist(String receiptNumber) {
        if (null != receiptNumber) {
            return separateOrderDAO.isReceiptExist(receiptNumber);
        }
        return null;
    }

    @Override
    public void separateOrderReceipt(String receiptNumber) {
        //生成订单收款接口表信息
        List<OrderBillingPaymentDetails> billingPaymentDetailsList = orderService.getOrderBillingDetailListByReceiptNumber(receiptNumber);
        if (null != billingPaymentDetailsList && billingPaymentDetailsList.size() == 1) {
            for (OrderBillingPaymentDetails billing : billingPaymentDetailsList) {
                OrderBaseInfo baseInfo = orderService.getOrderByOrderNumber(billing.getOrderNumber());
                if (null != baseInfo) {
                    OrderReceiptInf receiptInf = new OrderReceiptInf();
                    receiptInf.setMainOrderNumber(billing.getOrderNumber());
                    receiptInf.setDescription(billing.getPayTypeDesc());
                    receiptInf.setAmount(billing.getAmount());
                    receiptInf.setCreateTime(new Date());
                    receiptInf.setReceiptDate(billing.getPayTime());
                    receiptInf.setReceiptType(billing.getPayType());
                    receiptInf.setStoreOrgId(baseInfo.getStoreOrgId());
                    receiptInf.setDiySiteCode(baseInfo.getStoreCode());
                    receiptInf.setReceiptNumber(billing.getReceiptNumber());
                    receiptInf.setSobId(baseInfo.getSobId());
                    receiptInf.setUserId(null == baseInfo.getCustomerId() ? baseInfo.getCreatorId() : baseInfo.getCustomerId());
                    receiptInf.setUserPhone(null == baseInfo.getCustomerPhone() ? baseInfo.getCreatorPhone() : baseInfo.getCustomerPhone());
                    receiptInf.setUsername(null == baseInfo.getCustomerName() ? baseInfo.getCreatorName() : baseInfo.getCustomerName());
                    receiptInf.setGuideId(baseInfo.getSalesConsultId());

                    //分销仓库为分销门店代下单，设置分销门店编码至订单头 attribute3
                    String fxStoreCode = null;
                    if (baseInfo.getCreatorIdentityType() == AppIdentityType.CUSTOMER ||
                            baseInfo.getCreatorIdentityType() == AppIdentityType.SELLER) {
                        Long customerIdTemp;
                        if (baseInfo.getCreatorIdentityType() == AppIdentityType.SELLER) {
                            customerIdTemp = baseInfo.getCustomerId();
                        } else {
                            customerIdTemp = baseInfo.getCreatorId();
                        }
                        AppCustomerFxStoreRelation customerFxStoreRelation = separateOrderDAO.getCustomerFxStoreRelationByCusId(customerIdTemp);
                        if (null != customerFxStoreRelation) {
                            fxStoreCode = customerFxStoreRelation.getFxStoreCode();
                        }
                    }
                    if (StringUtils.isNotBlank(fxStoreCode)) {
                        receiptInf.setAttribute3(fxStoreCode);
                    }
                    this.supportService.saveOrderReceiptInf(receiptInf);
                }
            }
        }
    }

    @Override
    public void separateOrderRefund(String refundNumber) {
        if (null != refundNumber) {
            List<ReturnOrderRefundInf> returnOrderRefundInfList = new ArrayList<>(10);
            List<ReturnOrderBillingDetail> returnOrderBillingDetailList = returnOrderService.
                    getReturnOrderBillingDetailByRefundNumber(refundNumber);
            if (null != returnOrderBillingDetailList && returnOrderBillingDetailList.size() > 0) {
                for (ReturnOrderBillingDetail billingDetail : returnOrderBillingDetailList) {

                    ReturnOrderBaseInfo returnOrderBaseInfo = returnOrderService.queryByReturnNo(billingDetail.getReturnNo());
                    OrderBaseInfo orderBaseInfo = orderService.getOrderByOrderNumber(null != returnOrderBaseInfo ? returnOrderBaseInfo.getOrderNo() : null);
                    if (null != orderBaseInfo) {
                        ReturnOrderRefundInf refundInf = new ReturnOrderRefundInf();
                        refundInf.setAmount(billingDetail.getReturnMoney());
                        refundInf.setCreateTime(billingDetail.getCreateTime());
                        refundInf.setDiySiteCode(orderBaseInfo.getStoreCode());
                        refundInf.setMainOrderNumber(orderBaseInfo.getOrderNumber());
                        refundInf.setMainReturnNumber(null != returnOrderBaseInfo ? returnOrderBaseInfo.getReturnNo() : null);
                        refundInf.setStoreOrgCode(orderBaseInfo.getStoreStructureCode());
                        refundInf.setUserId(returnOrderBaseInfo.getCreatorIdentityType() == AppIdentityType.SELLER ?
                                returnOrderBaseInfo.getCustomerId() : returnOrderBaseInfo.getCreatorId());
                        refundInf.setRefundDate(billingDetail.getIntoAmountTime());
                        refundInf.setRefundNumber(OrderUtils.getRefundNumber());
                        refundInf.setRefundType(billingDetail.getReturnPayType());
                        refundInf.setSobId(orderBaseInfo.getSobId());
                        refundInf.setDescription(null != refundInf.getRefundType() ? refundInf.getRefundType().getDescription() : "");

                        //设置分销门店编码至attribute3
                        String fxStoreCode = null;
                        if (AppOrderSubjectType.STORE.equals(orderBaseInfo.getOrderSubjectType())) {
                            Long customerIdTemp;
                            if (AppIdentityType.SELLER.equals(orderBaseInfo.getCreatorIdentityType())) {
                                customerIdTemp = orderBaseInfo.getCustomerId();
                            } else {
                                customerIdTemp = orderBaseInfo.getCreatorId();
                            }
                            AppCustomerFxStoreRelation customerFxStoreRelation = separateOrderDAO.getCustomerFxStoreRelationByCusId(customerIdTemp);
                            if (null != customerFxStoreRelation) {
                                fxStoreCode = customerFxStoreRelation.getFxStoreCode();
                            }
                        }
                        if (StringUtils.isNotBlank(fxStoreCode)) {
                            refundInf.setAttribute3(fxStoreCode);
                        }
                        returnOrderRefundInfList.add(refundInf);
                    }
                }
            }
        }
    }

    @Override
    public void sendOrderReceiptInfByReceiptNumber(String receiptNumber) {
        if (null != receiptNumber) {
            List<OrderReceiptInf> receiptInfList = separateOrderDAO.getOrderReceiptInfByReceiptNumber(receiptNumber);
            if (null != receiptInfList && receiptInfList.size() == 1) {
                ebsSenderService.sendOrderReceiptInfAndRecord(receiptInfList);
            }
        }
    }

}

package cn.com.leyizhuang.app.core.remote.ebs.impl;

import cn.com.leyizhuang.app.core.constant.AppConstant;
import cn.com.leyizhuang.app.core.constant.AppWhetherFlag;
import cn.com.leyizhuang.app.core.remote.ebs.EbsSenderService;
import cn.com.leyizhuang.app.foundation.dao.ItyAllocationDAO;
import cn.com.leyizhuang.app.foundation.pojo.inventory.allocation.Allocation;
import cn.com.leyizhuang.app.foundation.pojo.inventory.allocation.AllocationInf;
import cn.com.leyizhuang.app.foundation.pojo.management.returnOrder.MaStoreReturnOrderAppToEbsBaseInfo;
import cn.com.leyizhuang.app.foundation.pojo.management.webservice.ebs.MaOrderReceiveInf;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.ebs.*;
import cn.com.leyizhuang.app.foundation.service.AppSeparateOrderService;
import cn.com.leyizhuang.app.foundation.service.ItyAllocationService;
import cn.com.leyizhuang.common.util.AssertUtil;
import cn.com.leyizhuang.ebs.entity.dto.second.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * EBS接口发送服务实现
 *
 * @author Richard
 * Created on 2018-01-13 14:25
 **/
@Service
@Slf4j
public class EbsSenderServiceImpl implements EbsSenderService {

    @Resource
    private AppSeparateOrderService separateOrderService;

    @Resource
    private ItyAllocationService ityAllocationService;

    @Resource
    private ItyAllocationDAO allocationDAO;

    /**
     * 配置请求的超时设置
     */
    private final RequestConfig REQUEST_CONFIG = RequestConfig.custom().setConnectionRequestTimeout(5000).setConnectTimeout(1000)
            .setSocketTimeout(5000).build();

    //************************************ 发送订单收款信息 begin *************************************

    /**
     * 发送订单收款到EBS 并记录发送结果
     *
     * @param receiptInfs 收款信息
     */
    @Override
    public void sendOrderReceiptInfAndRecord(List<OrderReceiptInf> receiptInfs) {
        Map<String, Object> result = sendOrderReceiptToEbs(receiptInfs);
        List<Long> receiptInfIds = new ArrayList<>(10);
        for (OrderReceiptInf receiptInf : receiptInfs) {
            receiptInfIds.add(receiptInf.getReceiptId());
        }
        if (!(Boolean) result.get("success")) {
            updateOrderReceiptFlagAndSendTimeAndErrorMsg(receiptInfIds, (String) result.get("msg"), null, AppWhetherFlag.N);
        } else {
            updateOrderReceiptFlagAndSendTimeAndErrorMsg(receiptInfIds, null, new Date(), AppWhetherFlag.Y);
        }
    }

    /**
     * 发送订单收款信息到EBS
     *
     * @param receiptInfs 订单收款信息
     * @return 发送结果
     */
    private Map<String, Object> sendOrderReceiptToEbs(List<OrderReceiptInf> receiptInfs) {
        log.info("sendOrderReceiptToEbs, receiptInfs=" + receiptInfs);
        List<OrderReceiptSecond> orderReceiptSeconds = new ArrayList<>();
        if (null != receiptInfs && receiptInfs.size() > 0) {
            for (OrderReceiptInf receiptInf : receiptInfs) {
                OrderReceiptSecond orderReceiptSecond = new OrderReceiptSecond();
                orderReceiptSecond.setAttribute1(toString(receiptInf.getAttribute1()));
                orderReceiptSecond.setAttribute2(toString(receiptInf.getAttribute2()));
                orderReceiptSecond.setAttribute3(toString(receiptInf.getAttribute3()));
                orderReceiptSecond.setAttribute4(toString(receiptInf.getAttribute4()));
                orderReceiptSecond.setAttribute5(toString(receiptInf.getAttribute5()));
                orderReceiptSecond.setAmount(toString(receiptInf.getAmount()));
                orderReceiptSecond.setDescription(toString(receiptInf.getDescription()));
                orderReceiptSecond.setDiySiteCode(toString(receiptInf.getDiySiteCode()));
                orderReceiptSecond.setGuideId(toString(receiptInf.getGuideId()));
                orderReceiptSecond.setMainOrderNumber(toString(receiptInf.getMainOrderNumber()));
                orderReceiptSecond.setReceiptDate(toString(DateFormatUtils.format(receiptInf.getReceiptDate(), "yyyy-MM-dd HH:mm:ss")));
                orderReceiptSecond.setReceiptId(toString(receiptInf.getReceiptId()));
                orderReceiptSecond.setReceiptNumber(toString(receiptInf.getReceiptNumber()));
                orderReceiptSecond.setReceiptType(toString(receiptInf.getReceiptType().getDescription()));
                orderReceiptSecond.setSobId(toString(receiptInf.getSobId()));
                orderReceiptSecond.setUserId(toString(receiptInf.getUserId()));
                orderReceiptSecond.setUsername(toString(receiptInf.getUsername()));
                orderReceiptSecond.setUserphone(toString(receiptInf.getUserPhone()));
                orderReceiptSeconds.add(orderReceiptSecond);
            }

        }
        String orderReceiptSecondJson = JSON.toJSONString(orderReceiptSeconds);
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        parameters.add(new BasicNameValuePair("orderReceiptJson", orderReceiptSecondJson));
        Map<String, Object> result = this.postToEbs(AppConstant.EBS_NEW_URL + "callOrderReceiptSecond", parameters);
        if (!(Boolean) result.get("success")) {
            JSONObject content = new JSONObject();
            content.put("orderReceiptSecondJson", orderReceiptSecondJson);
            result.put("content", JSON.toJSONString(content));
        }
        log.info("sendOrderReceiptToEbs, result=" + result);
        return result;
    }

    /**
     * 更新订单收款传输信息
     *
     * @param receiptInfIds 订单收款信息
     * @param msg           接口返回错误信息
     * @param sendTime      发送成功时间
     * @param flag          是否发送成功标识
     */
    private void updateOrderReceiptFlagAndSendTimeAndErrorMsg(List<Long> receiptInfIds, String msg, Date sendTime, AppWhetherFlag flag) {
        if (null != receiptInfIds && receiptInfIds.size() > 0) {
            separateOrderService.updateOrderReceiptFlagAndSendTimeAndErrorMsg(receiptInfIds, msg, sendTime, flag);
        }
    }

    //************************************ 发送订单收款信息 end *************************************


    //************************************ 发送订单券信息 begin *************************************

    /**
     * 发送订单券信息到EBS并记录返回结果
     *
     * @param orderCouponInfs 订单券信息
     */
    @Override
    public void sendOrderCouponInfAndRecord(List<OrderCouponInf> orderCouponInfs) {
        Map<String, Object> result = sendOrderCouponToEbs(orderCouponInfs);
        List<Long> couponInfIds = new ArrayList<>(10);
        for (OrderCouponInf couponInf : orderCouponInfs) {
            couponInfIds.add(couponInf.getId());
        }
        if (!(Boolean) result.get("success")) {
            updateOrderCouponFlagAndSendTimeAndErrorMsg(couponInfIds, (String) result.get("msg"), null, AppWhetherFlag.N);
        } else {
            updateOrderCouponFlagAndSendTimeAndErrorMsg(couponInfIds, null, new Date(), AppWhetherFlag.Y);
        }
    }


    /**
     * 发送订单券信息到EBS
     *
     * @param orderCouponInfs 订单券信息
     * @return
     */
    private Map<String, Object> sendOrderCouponToEbs(List<OrderCouponInf> orderCouponInfs) {
        log.info("sendOrderCouponToEbs, orderCouponInf=" + orderCouponInfs);
        List<OrderCouponSecond> orderCouponSeconds = new ArrayList<>();
        if (null != orderCouponInfs && orderCouponInfs.size() > 0) {
            for (OrderCouponInf couponInf : orderCouponInfs) {
                OrderCouponSecond orderCouponSecond = new OrderCouponSecond();
                orderCouponSecond.setLineId(toString(couponInf.getId()));
                orderCouponSecond.setPromotion(toString(couponInf.getPromotion()));
                orderCouponSecond.setAttribute1(toString(couponInf.getAttribute1()));
                orderCouponSecond.setAttribute2(toString(couponInf.getAttribute2()));
                orderCouponSecond.setAttribute3(toString(couponInf.getAttribute3()));
                orderCouponSecond.setAttribute4(toString(couponInf.getAttribute4()));
                orderCouponSecond.setBuyPrice(toString(couponInf.getBuyPrice()));
                orderCouponSecond.setCostPrice(toString(couponInf.getCostPrice()));
                orderCouponSecond.setCouponTypeId(toString(couponInf.getCouponTypeId()));
                orderCouponSecond.setHistoryFlag(toString(couponInf.getHistoryFlag()));
                orderCouponSecond.setMainOrderNumber(toString(couponInf.getMainOrderNumber()));
                orderCouponSecond.setProductType(toString(couponInf.getProductType()));
                orderCouponSecond.setQuantity(toString(couponInf.getQuantity()));
                orderCouponSecond.setSku(toString(couponInf.getSku()));
                orderCouponSecond.setSobId(toString(couponInf.getSobId()));
                orderCouponSeconds.add(orderCouponSecond);

            }

        }
        String orderCouponJson = JSON.toJSONString(orderCouponSeconds);
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        parameters.add(new BasicNameValuePair("orderCouponJson", orderCouponJson));
        Map<String, Object> result = this.postToEbs(AppConstant.EBS_NEW_URL + "callOrderCouponSecond", parameters);
        if (!(Boolean) result.get("success")) {
            JSONObject content = new JSONObject();
            content.put("orderCouponJson", orderCouponJson);
            result.put("content", JSON.toJSONString(content));
        }
        log.info("sendOrderCouponToEbs, result=" + result);
        return result;
    }


    /**
     * 更新订单券接口发送信息
     *
     * @param couponInfIds 订单券行id
     * @param msg          错误信息
     * @param sendTime     发送成功时间
     * @param flag         标识
     */
    private void updateOrderCouponFlagAndSendTimeAndErrorMsg(List<Long> couponInfIds, String msg, Date sendTime, AppWhetherFlag flag) {
        if (null != couponInfIds && couponInfIds.size() > 0) {
            separateOrderService.updateOrderCouponFlagAndSendTimeAndErrorMsg(couponInfIds, msg, sendTime, flag);
        }
    }


    //************************************ 发送订单券信息 end *************************************


    //************************************ 发送充值收款信息begin *************************************

    /**
     * 发送充值收款信息到EBS 并记录传输结果
     *
     * @param receiptInf 充值收款信息
     */
    @Override
    public void sendRechargeReceiptInfAndRecord(RechargeReceiptInf receiptInf) {
        Map<String, Object> result = sendRechargeReceiptToEbs(receiptInf);
        if (!(Boolean) result.get("success")) {
            updateRechargeReceiptFlagAndSendTimeAndErrorMsg(receiptInf.getReceiptId(), (String) result.get("msg"), null, AppWhetherFlag.N);
        } else {
            updateRechargeReceiptFlagAndSendTimeAndErrorMsg(receiptInf.getReceiptId(), null, new Date(), AppWhetherFlag.Y);
        }

    }

    /**
     * 发送充值收款信息到EBS
     *
     * @param receiptInf 充值收款信息
     * @return 传输结果
     */
    private Map<String, Object> sendRechargeReceiptToEbs(RechargeReceiptInf receiptInf) {
        log.info("sendRechargeReceiptToEbs, receiptInf=" + receiptInf);
        RechargeReceiptSecond receiptSecond = new RechargeReceiptSecond();
        receiptSecond.setAmount(toString(receiptInf.getAmount()));
        receiptSecond.setAttribute1(toString(receiptInf.getAttribute1()));
        receiptSecond.setAttribute2(toString(receiptInf.getAttribute2()));
        receiptSecond.setAttribute3(toString(receiptInf.getAttribute3()));
        receiptSecond.setAttribute4(toString(receiptInf.getAttribute4()));
        receiptSecond.setAttribute5(toString(receiptInf.getAttribute5()));
        receiptSecond.setChargeNumber(toString(receiptInf.getChargeNumber()));
        receiptSecond.setChargeObj(toString(receiptInf.getChargeObj()));
        receiptSecond.setChargeType(toString(receiptInf.getChargeType()));
        receiptSecond.setDescription(toString(receiptInf.getDescription()));
        receiptSecond.setDiySiteCode(toString(receiptInf.getDiySiteCode()));
        receiptSecond.setReceiptDate(toString(DateFormatUtils.format(receiptInf.getReceiptDate(), "yyyy-MM-dd HH:mm:ss")));
        receiptSecond.setReceiptId(toString(receiptInf.getReceiptId()));
        receiptSecond.setReceiptNumber(toString(receiptInf.getReceiptNumber()));
        receiptSecond.setReceiptType(toString(receiptInf.getReceiptType()));
        receiptSecond.setSobId(toString(receiptInf.getSobId()));
        receiptSecond.setStoreOrgCode(toString(receiptInf.getStoreOrgCode()));
        receiptSecond.setUserid(toString(receiptInf.getUserid()));
        String rechargeReceiptSecondJson = JSON.toJSONString(receiptSecond);
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        parameters.add(new BasicNameValuePair("rechargeReceiptJson", rechargeReceiptSecondJson));
        Map<String, Object> result = this.postToEbs(AppConstant.EBS_NEW_URL + "callChargeReceiptSecond", parameters);
        if (!(Boolean) result.get("success")) {
            JSONObject content = new JSONObject();
            content.put("rechargeReceiptSecondJson", rechargeReceiptSecondJson);
            result.put("content", JSON.toJSONString(content));
        }
        log.info("sendRechargeReceiptToEbs, result=" + result);
        return result;
    }

    /**
     * 更新充值收款接口传输信息
     *
     * @param receiptId 收款id
     * @param msg       接口返回错误信息
     * @param sendTime  成功发送时间
     * @param flag      是否发送成功标识
     */
    private void updateRechargeReceiptFlagAndSendTimeAndErrorMsg(Long receiptId, String msg, Date sendTime, AppWhetherFlag flag) {
        if (null != receiptId) {
            separateOrderService.updateRechargeReceiptFlagAndSendTimeAndErrorMsg(receiptId, msg, sendTime, flag);
        }
    }

    //************************************ 发送充值收款信息 end *************************************


    //************************************ 发送订单（订单头、商品信息）信息 begin *******************

    /**
     * 发送订单（订单头、商品信息）到EBS并记录返回结果
     *
     * @param orderInf  订单头信息
     * @param goodsInfs 订单商品信息
     */
    @Override
    public void sendOrderAndGoodsToEbsAndRecord(final OrderBaseInf orderInf, final List<OrderGoodsInf> goodsInfs) {
        Map<String, Object> result = sendOrderAndGoodsToEbs(orderInf, goodsInfs);
        if (!(Boolean) result.get("success")) {
            orderInf.setErrorMsg((String) result.get("msg"));
            updateOrderFlag(orderInf, goodsInfs, AppWhetherFlag.N);
        } else {
            updateOrderFlag(orderInf, goodsInfs, AppWhetherFlag.Y);
        }

    }

    /**
     * 发送【订单（订单头、订单商品）】信息到EBS
     *
     * @param orderInf  订单头
     * @param goodsInfs 订单商品
     * @return 返回结果
     */
    public Map<String, Object> sendOrderAndGoodsToEbs(OrderBaseInf orderInf, List<OrderGoodsInf> goodsInfs) {
        log.info("sendOrderAndGoodsToEbs, orderInf=" + orderInf);
        log.info("sendOrderAndGoodsToEbs,goodsInfs=" + goodsInfs);

        OrderSecond orderSecond = new OrderSecond();
        orderSecond.setMainOrderNumber(toString(orderInf.getMainOrderNumber()));
        orderSecond.setOrderNumber(toString(orderInf.getOrderNumber()));
        orderSecond.setOrderDate(toString(DateFormatUtils.format(orderInf.getOrderDate(), "yyyy-MM-dd HH:mm:ss")));
        orderSecond.setSobId(toString(orderInf.getSobId()));
        orderSecond.setCouponFlag(toString(orderInf.getCouponFlag()));
        orderSecond.setCreditFlag(toString(orderInf.getCreditFlag()));
        orderSecond.setDecorateManagerId(toString(orderInf.getDecorateManagerId()));
        orderSecond.setSalesConsultId(toString(orderInf.getSalesConsultId()));
        orderSecond.setStoreOrgId(toString(orderInf.getStoreOrgId()));
        orderSecond.setUserid(toString(orderInf.getUserId()));
        orderSecond.setDeliverTypeTitle(toString(orderInf.getDeliveryTypeTitle()));
        orderSecond.setDiySiteCode(toString(orderInf.getDiySiteCode()));
        orderSecond.setOrderAmt(toString(orderInf.getOrderAmt()));
        orderSecond.setOrderHeaderId(toString(orderInf.getOrderHeaderId()));
        orderSecond.setOrderSubjectType(toString(orderInf.getOrderSubjectType()));
        orderSecond.setOrderTypeId(toString(orderInf.getOrderTypeId()));
        orderSecond.setProductCouponDiscount(toString(orderInf.getProductCouponDiscount()));
        orderSecond.setCashCouponDiscount(toString(orderInf.getCashCouponDiscount()));
        orderSecond.setLebiDiscount(toString(orderInf.getLebiDiscount()));
        orderSecond.setMemberDiscount(toString(orderInf.getMemberDiscount()));
        orderSecond.setPromotionDiscount(toString(orderInf.getPromotionDiscount()));
        orderSecond.setStoreSubventionDiscount(toString(orderInf.getStoreSubventionDiscount()));
        orderSecond.setProductType(toString(orderInf.getProductType()));
        orderSecond.setRecAmt(toString(orderInf.getRecAmt()));
        orderSecond.setAttribute1(toString(orderInf.getAttribute1()));
        orderSecond.setAttribute2(toString(orderInf.getAttribute2()));
        orderSecond.setAttribute3(toString(orderInf.getAttribute3()));
        orderSecond.setAttribute4(toString(orderInf.getAttribute4()));
        orderSecond.setAttribute5(toString(orderInf.getAttribute5()));
        orderSecond.setAttribute6(toString(orderInf.getAttribute6()));
        orderSecond.setAttribute7(toString(orderInf.getAttribute7()));


        List<OrderGoodSecond> orderGoodSeconds = new ArrayList<>();
        if (null != goodsInfs && goodsInfs.size() > 0) {
            for (OrderGoodsInf goodsInf : goodsInfs) {
                OrderGoodSecond orderGoodSecond = new OrderGoodSecond();
                orderGoodSecond.setAttribute1(toString(goodsInf.getAttribute1()));
                orderGoodSecond.setAttribute2(toString(goodsInf.getAttribute2()));
                orderGoodSecond.setAttribute3(toString(goodsInf.getAttribute3()));
                orderGoodSecond.setAttribute4(toString(goodsInf.getAttribute4()));
                orderGoodSecond.setCashCounponDiscount(toString(goodsInf.getCashCouponDiscount()));
                orderGoodSecond.setDiscountTotalPrice(toString(goodsInf.getDiscountTotalPrice()));
                orderGoodSecond.setGiftFlag(toString(goodsInf.getGiftFlag()));
                orderGoodSecond.setGoodsTitle(toString(goodsInf.getGoodsTitle()));
                orderGoodSecond.setHyPrice(toString(goodsInf.getHyPrice()));
                orderGoodSecond.setJxPrice(toString(goodsInf.getJxPrice()));
                orderGoodSecond.setLebiDiscount(toString(goodsInf.getLebiDiscount()));
                orderGoodSecond.setLsPrice(toString(goodsInf.getLsPrice()));
                orderGoodSecond.setMainOrderNumber(toString(goodsInf.getMainOrderNumber()));
                orderGoodSecond.setOrderHeaderId(toString(goodsInf.getOrderHeaderId()));
                orderGoodSecond.setOrderNumber(toString(goodsInf.getOrderNumber()));
                orderGoodSecond.setProductType(toString(goodsInf.getProductType()));
                orderGoodSecond.setOrderLineId(toString(goodsInf.getOrderLineId()));
                orderGoodSecond.setPromotionDiscount(toString(goodsInf.getPromotionDiscount()));
                orderGoodSecond.setPromotionId(toString(goodsInf.getPromotionId()));
                orderGoodSecond.setQuantity(toString(goodsInf.getQuantity()));
                orderGoodSecond.setReturnPrice(toString(goodsInf.getReturnPrice()));
                orderGoodSecond.setSettlementPrice(toString(goodsInf.getSettlementPrice()));
                orderGoodSecond.setSku(toString(goodsInf.getSku()));
                orderGoodSecond.setSubventionDiscount(toString(goodsInf.getSubventionDiscount()));
                orderGoodSecond.setAttribute1(toString(goodsInf.getAttribute1()));
                orderGoodSecond.setAttribute2(toString(goodsInf.getAttribute2()));
                orderGoodSecond.setAttribute3(toString(goodsInf.getAttribute3()));
                orderGoodSecond.setAttribute4(toString(goodsInf.getAttribute4()));
                orderGoodSeconds.add(orderGoodSecond);
            }
        }

        String orderJson = JSON.toJSONString(orderSecond);
        String orderGoodsJson = JSON.toJSONString(orderGoodSeconds);
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        parameters.add(new BasicNameValuePair("orderJson", orderJson));
        parameters.add(new BasicNameValuePair("orderGoodsJson", orderGoodsJson));

        Map<String, Object> result = this.postToEbs(AppConstant.EBS_NEW_URL + "callOrderSecond", parameters);

        if (!(Boolean) result.get("success")) {
            JSONObject content = new JSONObject();
            content.put("orderJson", orderJson);
            content.put("orderGoodsJson", orderGoodsJson);
            result.put("content", JSON.toJSONString(content));
        }

        log.info("sendOrderToEbs, result=" + result);
        return result;
    }

    /**
     * 更新订单接口传输状态
     *
     * @param orderInf  订单头信息
     * @param goodsInfs 订单商品信息
     */
    public void updateOrderFlag(final OrderBaseInf orderInf, final List<OrderGoodsInf> goodsInfs, AppWhetherFlag flag) {
        if (AppWhetherFlag.Y == flag) {
            separateOrderService.updateOrderBaseInfSendFlagAndErrorMessageAndSendTime(orderInf.getOrderNumber(), flag, null, new Date());
            separateOrderService.updateOrderGoodsInfByOrderNumber(orderInf.getOrderNumber(), flag, null, new Date());
        } else {
            separateOrderService.updateOrderBaseInfSendFlagAndErrorMessageAndSendTime(orderInf.getOrderNumber(), flag, orderInf.getErrorMsg(), null);
            separateOrderService.updateOrderGoodsInfByOrderNumber(orderInf.getOrderNumber(), flag, orderInf.getErrorMsg(), null);
        }

    }

    //************************************ 发送订单（订单头、商品信息）信息 end *******************


    //************************************ 发送订单经销差价返还信息 begin *************************

    @Override
    public void sendOrderJxPriceDifferenceReturnInfAndRecord(List<OrderJxPriceDifferenceReturnInf> jxPriceDifferenceReturnInfs) {
        Map<String, Object> result = sendOrderJxPriceDifferenceReturnToEbs(jxPriceDifferenceReturnInfs);
        List<Long> returnInfIds = new ArrayList<>(10);
        for (OrderJxPriceDifferenceReturnInf returnInf : jxPriceDifferenceReturnInfs) {
            returnInfIds.add(returnInf.getReceiptId());
        }
        if (!(Boolean) result.get("success")) {
            updateOrderJxPriceDifferenceReturnInf(returnInfIds, (String) result.get("msg"), null, AppWhetherFlag.N);
        } else {
            updateOrderJxPriceDifferenceReturnInf(returnInfIds, null, new Date(), AppWhetherFlag.Y);
        }

    }

    private void updateOrderJxPriceDifferenceReturnInf(List<Long> returnInfIds, String msg, Date sendTime, AppWhetherFlag flag) {
        if (null != returnInfIds && returnInfIds.size() > 0) {
            separateOrderService.updateOrderJxPriceDifferenceReturnInf(returnInfIds, msg, sendTime, flag);
        }
    }

    private Map<String, Object> sendOrderJxPriceDifferenceReturnToEbs(List<OrderJxPriceDifferenceReturnInf> jxPriceDifferenceReturnInfs) {
        log.info("sendJxPriceDifferenceReturnToEbs, jxPriceDifferenceReturnInfs=" + jxPriceDifferenceReturnInfs);
        List<OrderJxPriceDifferenceReturnSecond> orderJxPriceDifferenceReturnSeconds = new ArrayList<>(20);
        if (null != jxPriceDifferenceReturnInfs && jxPriceDifferenceReturnInfs.size() > 0) {
            for (OrderJxPriceDifferenceReturnInf returnInf : jxPriceDifferenceReturnInfs) {
                OrderJxPriceDifferenceReturnSecond returnSecond = new OrderJxPriceDifferenceReturnSecond();
                returnSecond.setAmount(toString(returnInf.getAmount()));
                returnSecond.setAttribute1(toString(returnInf.getAttribute1()));
                returnSecond.setAttribute2(toString(returnInf.getAttribute2()));
                returnSecond.setAttribute3(toString(returnInf.getAttribute3()));
                returnSecond.setAttribute4(toString(returnInf.getAttribute4()));
                returnSecond.setAttribute5(toString(returnInf.getAttribute5()));
                returnSecond.setDescription(toString(returnInf.getDescription()));
                returnSecond.setDiySiteCode(toString(returnInf.getDiySiteCode()));
                returnSecond.setMainOrderNumber(toString(returnInf.getMainOrderNumber()));
                returnSecond.setStoreOrgCode(toString(returnInf.getStoreOrgCode()));
                returnSecond.setSobId(toString(returnInf.getSobId()));
                returnSecond.setSku(toString(returnInf.getSku()));
                returnSecond.setReceiptNumber(toString(returnInf.getReceiptNumber()));
                returnSecond.setReceiptId(toString(returnInf.getReceiptId()));
                returnSecond.setReceiptDate(toString(DateFormatUtils.format(returnInf.getReceiptDate(), "yyyy-MM-dd HH:mm:ss")));
                orderJxPriceDifferenceReturnSeconds.add(returnSecond);
            }
        }
        String orderJxPriceDifferenceReturnJson = JSON.toJSONString(orderJxPriceDifferenceReturnSeconds);
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        parameters.add(new BasicNameValuePair("orderJxPriceDifferenceReturnJson", orderJxPriceDifferenceReturnJson));
        Map<String, Object> result = this.postToEbs(AppConstant.EBS_NEW_URL + "callOrderJxPriceDifferenceReturnSecond", parameters);
        if (!(Boolean) result.get("success")) {
            JSONObject content = new JSONObject();
            content.put("orderJxPriceDifferenceReturnJson", orderJxPriceDifferenceReturnJson);
            result.put("content", JSON.toJSONString(content));
        }
        log.info("sendJxPriceDifferenceReturnToEbs, result=" + result);
        return result;
    }


    //************************************ 发送订单经销差价返还信息 end ***************************

    /**
     * 发送【调拨单(出库)】信息到EBS，并保存发送结果
     *
     * @param allocation
     */
    @Override
    public void sendAllocationToEBSAndRecord(final Allocation allocation) {
        Map<String, Object> result = sendAllocationToEBS(allocation);
        if (!(Boolean) result.get("success")) {
            AllocationInf allocationInf = new AllocationInf();
            Date now = new Date();
            allocationInf.setAllocationId(allocation.getId());
            allocationInf.setContent((String) result.get("content"));
            allocationInf.setCreatedTime(now);
            allocationInf.setMsg((String) result.get("msg"));
            allocationInf.setNumber(allocation.getNumber());
            allocationInf.setStatus(1);
            allocationInf.setTimes(1);
            allocationInf.setType(1);
            allocationInf.setUpdatedTime(now);
            allocationDAO.insertAllocationInf(allocationInf);
        }
    }

    /**
     * 发送【调拨单(出库)】信息到EBS
     *
     * @param allocation
     * @return
     */
    public Map<String, Object> sendAllocationToEBS(Allocation allocation) {
        log.info("sendAllocationToEBS, allocation = " + allocation);

        String header = ityAllocationService.genHeaderJson(allocation);
        String details = ityAllocationService.genDetailJson(allocation);
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        parameters.add(new BasicNameValuePair("allcationHeaderJson", header));
        parameters.add(new BasicNameValuePair("allocationDetailsJson", details));
        Map<String, Object> result = this.postToEbs(AppConstant.EBS_NEW_URL + "callAllocationSecond", parameters);

        if (!(Boolean) result.get("success")) {
            JSONObject content = new JSONObject();
            content.put("allcationHeaderJson", header);
            content.put("allocationDetailsJson", details);
            result.put("content", JSON.toJSONString(content));
        }

        log.info("sendAllocationToEBS, result=" + result);
        return result;
    }

    /**
     * 发送【调拨单(入库)】信息到EBS
     *
     * @param
     */
    @Override
    public void sendAllocationReceivedToEBSAndRecord(final Allocation allocation) {
        Map<String, Object> result = sendAllocationReceivedToEBS(allocation);
        if (!(Boolean) result.get("success")) {
            AllocationInf allocationInf = new AllocationInf();
            Date now = new Date();
            allocationInf.setAllocationId(allocation.getId());
            allocationInf.setContent((String) result.get("content"));
            allocationInf.setCreatedTime(now);
            allocationInf.setMsg((String) result.get("msg"));
            allocationInf.setNumber(allocation.getNumber());
            allocationInf.setStatus(1);
            allocationInf.setTimes(1);
            allocationInf.setType(3);
            allocationInf.setUpdatedTime(now);
            allocationDAO.insertAllocationInf(allocationInf);
        }
    }

    /**
     * 发送【调拨单(入库)】信息到EBS
     *
     * @param
     */
    public Map<String, Object> sendAllocationReceivedToEBS(Allocation allocation) {
        log.info("sendAllocationReceivedToEBS, allocation=" + allocation);

        String allocationReceiveJson = ityAllocationService.genReceiveJson(allocation);
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        parameters.add(new BasicNameValuePair("allocationReceiveJson", allocationReceiveJson));
        Map<String, Object> result = this.postToEbs(AppConstant.EBS_NEW_URL + "callAllocationReceiveSecond", parameters);

        if (!(Boolean) result.get("success")) {
            JSONObject content = new JSONObject();
            content.put("allocationReceiveJson", allocationReceiveJson);
            result.put("content", JSON.toJSONString(content));
        }

        log.info("sendAllocationReceivedToEBS, result=" + result);
        return result;
    }

    /**
     * 发送门店自提单发货到EBS
     *
     * @param receiveInfs 门店自提单信息
     */
    @Override
    public void sendOrderReceiveInfAndRecord(MaOrderReceiveInf receiveInfs) {
        Map<String, Object> result = sendOrderReceiveToEbs(receiveInfs);
        if (!(Boolean) result.get("success")) {
            updateOrderReceiveFlagAndSendTimeAndErrorMsg(receiveInfs.getId(), (String) result.get("msg"), null, AppWhetherFlag.N);
        } else {
            updateOrderReceiveFlagAndSendTimeAndErrorMsg(receiveInfs.getId(), null, new Date(), AppWhetherFlag.Y);
        }
    }


    @Override
    public void sendReturnOrderReceiptInfAndRecord(MaStoreReturnOrderAppToEbsBaseInfo maStoreReturnOrderAppToEbsBaseInfo) {
        Map<String, Object> result = sendReturnOrderReceiptToEbs(maStoreReturnOrderAppToEbsBaseInfo);
        if (!(Boolean) result.get("success")) {
            updateReturnOrderFlagAndSendTimeAndErrorMsg(maStoreReturnOrderAppToEbsBaseInfo.getId(), (String) result.get("msg"), null, AppWhetherFlag.N);
        } else {
            updateReturnOrderFlagAndSendTimeAndErrorMsg(maStoreReturnOrderAppToEbsBaseInfo.getId(), null, new Date(), AppWhetherFlag.Y);
        }
    }

    /**
     * 更新门店退货接口信息
     *
     * @param id
     * @param msg      错误信息
     * @param sendTime 发送成功时间
     * @param flag     标识
     */
    private void updateReturnOrderFlagAndSendTimeAndErrorMsg(Long id, String msg, Date sendTime, AppWhetherFlag flag) {
        if (null != id) {
            separateOrderService.updateReturnOrderFlagAndSendTimeAndErrorMsg(id, msg, sendTime, flag);
        }
    }

    /**
     * 更新门店自提单接口信息
     *
     * @param receiveInfsId 订单券行id
     * @param msg           错误信息
     * @param sendTime      发送成功时间
     * @param flag          标识
     */
    private void updateOrderReceiveFlagAndSendTimeAndErrorMsg(Long receiveInfsId, String msg, Date sendTime, AppWhetherFlag flag) {
        if (null != receiveInfsId) {
            separateOrderService.updateOrderReceiveFlagAndSendTimeAndErrorMsg(receiveInfsId, msg, sendTime, flag);
        }
    }

    private Map<String, Object> sendOrderReceiveToEbs(MaOrderReceiveInf receiveInfs) {
        log.info("sendOrderReceiveToEbs, receiveInfs=" + receiveInfs);
        StorePickUpSecond storePickUpSecond = new StorePickUpSecond();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (null != receiveInfs) {
            storePickUpSecond.setAttribute1(toString(receiveInfs.getAttribute1()));
            storePickUpSecond.setAttribute2(toString(receiveInfs.getAttribute2()));
            storePickUpSecond.setAttribute3(toString(receiveInfs.getAttribute3()));
            storePickUpSecond.setAttribute4(toString(receiveInfs.getAttribute4()));
            storePickUpSecond.setAttribute5(toString(receiveInfs.getAttribute5()));
            storePickUpSecond.setSobId(toString(receiveInfs.getSobId()));
            storePickUpSecond.setMainOrderNumber(receiveInfs.getOrderNumber());
            storePickUpSecond.setReceiveDate(toString(sdf.format(receiveInfs.getReceiveDate())));
        }
        String storePickUpSecondJson = JSON.toJSONString(storePickUpSecond);
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        parameters.add(new BasicNameValuePair("storePickUpJsonSecond", storePickUpSecondJson));
        String url = AppConstant.EBS_NEW_URL + "callStorePickUpSecond";
        Map<String, Object> result = this.postToEbs(url, parameters);
        if (!(Boolean) result.get("success")) {
            JSONObject content = new JSONObject();
            content.put("storePickUpSecondJson", storePickUpSecondJson);
            result.put("content", JSON.toJSONString(content));
        }
        log.info("sendOrderReceiveToEbs, result=" + result);
        return result;
    }


    private Map<String, Object> sendReturnOrderReceiptToEbs(MaStoreReturnOrderAppToEbsBaseInfo maStoreReturnOrderAppToEbsBaseInfo) {
        log.info("sendReturnOrderReceiptToEbs, maReturnOrderReceiptInf=" + maStoreReturnOrderAppToEbsBaseInfo);
        StoreReturnSecond storeReturnSecond = new StoreReturnSecond();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (null != maStoreReturnOrderAppToEbsBaseInfo) {
            storeReturnSecond.setAttribute1(toString(maStoreReturnOrderAppToEbsBaseInfo.getAttribute1()));
            storeReturnSecond.setAttribute2(toString(maStoreReturnOrderAppToEbsBaseInfo.getAttribute2()));
            storeReturnSecond.setAttribute3(toString(maStoreReturnOrderAppToEbsBaseInfo.getAttribute3()));
            storeReturnSecond.setAttribute4(toString(maStoreReturnOrderAppToEbsBaseInfo.getAttribute4()));
            storeReturnSecond.setAttribute5(toString(maStoreReturnOrderAppToEbsBaseInfo.getAttribute5()));
            storeReturnSecond.setSobId(toString(maStoreReturnOrderAppToEbsBaseInfo.getSobId()));
            storeReturnSecond.setReturnDate(toString(sdf.format(maStoreReturnOrderAppToEbsBaseInfo.getReturnDate())));
            storeReturnSecond.setMainOrderNumber(toString(maStoreReturnOrderAppToEbsBaseInfo.getMainOrderNumber()));
            storeReturnSecond.setReturnNumber(toString(maStoreReturnOrderAppToEbsBaseInfo.getReturnNumber()));
        }
        String storeReturnSecondJson = JSON.toJSONString(storeReturnSecond);
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        parameters.add(new BasicNameValuePair("storeReturnSecondJson", storeReturnSecondJson));
        String url = AppConstant.EBS_NEW_URL + "callStoreReturnSecond";
        Map<String, Object> result = this.postToEbs(url, parameters);
        if (!(Boolean) result.get("success")) {
            JSONObject content = new JSONObject();
            content.put("storeReturnSecondJson", storeReturnSecondJson);
            result.put("content", JSON.toJSONString(content));
        }
        log.info("sendReturnOrderReceiptToEbs, result=" + result);
        return result;
    }

    //************************************ 发送退单头及商品信息 begin *************************

    /**
     * 发送退单头及商品信息到EBS并记录
     *
     * @param baseInf                 退单头
     * @param returnOrderGoodsInfList 退单商品
     */
    @Override
    public void sendReturnOrderAndReturnGoodsToEbsAndRecord(ReturnOrderBaseInf baseInf, List<ReturnOrderGoodsInf> returnOrderGoodsInfList) {
        Map<String, Object> result = sendReturnOrderAndReturnGoodsToEbs(baseInf, returnOrderGoodsInfList);
        if (!(Boolean) result.get("success")) {
            baseInf.setErrorMsg((String) result.get("msg"));
            updateReturnOrderFlag(baseInf, returnOrderGoodsInfList, AppWhetherFlag.N);
        } else {
            updateReturnOrderFlag(baseInf, returnOrderGoodsInfList, AppWhetherFlag.Y);
        }
    }


    private Map<String, Object> sendReturnOrderAndReturnGoodsToEbs(ReturnOrderBaseInf baseInf, List<ReturnOrderGoodsInf> returnOrderGoodsInfList) {
        log.info("sendReturnOrderAndReturnGoodsToEbs, returnOrderInf=" + baseInf);
        log.info("sendReturnOrderAndReturnGoodsToEbs,returnOrderGoodsInfList=" + returnOrderGoodsInfList);

        ReturnOrderSecond returnOrderSecond = new ReturnOrderSecond();
        returnOrderSecond.setDeliverTypeTitle(toString(baseInf.getDeliverTypeTitle()));
        returnOrderSecond.setDiySiteCode(toString(baseInf.getDiySiteCode()));
        returnOrderSecond.setMainOrderNumber(toString(baseInf.getMainOrderNumber()));
        returnOrderSecond.setMainReturnNumber(toString(baseInf.getMainReturnNumber()));
        returnOrderSecond.setOrderNumber(toString(baseInf.getOrderNumber()));
        returnOrderSecond.setOrderTypeId(toString(baseInf.getOrderTypeId()));
        returnOrderSecond.setRefundAmount(toString(baseInf.getRefundAmount()));
        returnOrderSecond.setReturnDate(toString(DateFormatUtils.format(baseInf.getReturnDate(), "yyyy-MM-dd HH:mm:ss")));
        returnOrderSecond.setReturnNumber(toString(baseInf.getReturnNumber()));
        returnOrderSecond.setReturnType(toString(baseInf.getReturnType()));
        returnOrderSecond.setRtFullFlag(toString(baseInf.getRtFullFlag()));
        returnOrderSecond.setRtHeaderId(toString(baseInf.getRtHeaderId()));
        returnOrderSecond.setSellerId(toString(baseInf.getSellerId()));
        returnOrderSecond.setSobId(toString(baseInf.getSobId()));
        returnOrderSecond.setStoreOrgCode(toString(baseInf.getStoreOrgCode()));
        returnOrderSecond.setUserId(toString(baseInf.getUserId()));
        returnOrderSecond.setAttribute1(toString(baseInf.getAttribute1()));
        returnOrderSecond.setAttribute2(toString(baseInf.getAttribute2()));
        returnOrderSecond.setAttribute3(toString(baseInf.getAttribute3()));
        returnOrderSecond.setAttribute4(toString(baseInf.getAttribute4()));
        returnOrderSecond.setAttribute5(toString(baseInf.getAttribute5()));


        List<ReturnOrderGoodsSecond> returnOrderGoodsSecondList = new ArrayList<>();
        if (null != returnOrderGoodsInfList && returnOrderGoodsInfList.size() > 0) {
            for (ReturnOrderGoodsInf returnOrderGoodsInf : returnOrderGoodsInfList) {
                ReturnOrderGoodsSecond returnOrderGoodsSecond = new ReturnOrderGoodsSecond();
                returnOrderGoodsSecond.setAttribute1(toString(returnOrderGoodsInf.getAttribute1()));
                returnOrderGoodsSecond.setAttribute2(toString(returnOrderGoodsInf.getAttribute2()));
                returnOrderGoodsSecond.setAttribute3(toString(returnOrderGoodsInf.getAttribute3()));
                returnOrderGoodsSecond.setAttribute4(toString(returnOrderGoodsInf.getAttribute4()));
                returnOrderGoodsSecond.setAttribute5(toString(returnOrderGoodsInf.getAttribute5()));
                returnOrderGoodsSecond.setGoodsTitle(toString(returnOrderGoodsInf.getGoodsTitle()));
                returnOrderGoodsSecond.setHyPrice(toString(returnOrderGoodsInf.getHyPrice()));
                returnOrderGoodsSecond.setJxPrice(toString(returnOrderGoodsInf.getJxPrice()));
                returnOrderGoodsSecond.setLsPrice(toString(returnOrderGoodsInf.getLsPrice()));
                returnOrderGoodsSecond.setSettlementPrice(toString(returnOrderGoodsInf.getSettlementPrice()));
                returnOrderGoodsSecond.setMainOrderNumber(toString(returnOrderGoodsInf.getMainOrderNumber()));
                returnOrderGoodsSecond.setMainReturnNumber(toString(returnOrderGoodsInf.getMainReturnNumber()));
                returnOrderGoodsSecond.setOrderLineId(toString(returnOrderGoodsInf.getOrderLineId()));
                returnOrderGoodsSecond.setOrderNumber(toString(returnOrderGoodsInf.getOrderNumber()));
                returnOrderGoodsSecond.setQuantity(toString(returnOrderGoodsInf.getQuantity()));
                returnOrderGoodsSecond.setReturnNumber(toString(returnOrderGoodsInf.getReturnNumber()));
                returnOrderGoodsSecond.setReturnPrice(toString(returnOrderGoodsInf.getReturnPrice()));
                returnOrderGoodsSecond.setRtHeaderId(toString(returnOrderGoodsInf.getRtHeaderId()));
                returnOrderGoodsSecond.setRtLineId(toString(returnOrderGoodsInf.getRtLineId()));
                returnOrderGoodsSecond.setSku(toString(returnOrderGoodsInf.getSku()));

                returnOrderGoodsSecondList.add(returnOrderGoodsSecond);
            }
        }

        String returnOrderJson = JSON.toJSONString(returnOrderSecond);
        String returnOrderGoodsJson = JSON.toJSONString(returnOrderGoodsSecondList);
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        parameters.add(new BasicNameValuePair("returnOrderJson", returnOrderJson));
        parameters.add(new BasicNameValuePair("returnOrderGoodsJson", returnOrderGoodsJson));

        Map<String, Object> result = this.postToEbs(AppConstant.EBS_NEW_URL + "callReturnOrderSecond", parameters);

        if (!(Boolean) result.get("success")) {
            JSONObject content = new JSONObject();
            content.put("returnOrderJson", returnOrderJson);
            content.put("returnOrderGoodsJson", returnOrderGoodsJson);
            result.put("content", JSON.toJSONString(content));
        }

        log.info("sendReturnOrderAndReturnGoodsToEbs, result=" + result);
        return result;
    }

    private void updateReturnOrderFlag(ReturnOrderBaseInf baseInf, List<ReturnOrderGoodsInf> returnOrderGoodsInfList, AppWhetherFlag flag) {
        if (AppWhetherFlag.Y == flag) {
            separateOrderService.updateReturnOrderBaseInf(baseInf.getReturnNumber(), flag, null, new Date());
            separateOrderService.updateReturnOrderGoodsInf(baseInf.getReturnNumber(), flag, null, new Date());
        } else {
            separateOrderService.updateReturnOrderBaseInf(baseInf.getReturnNumber(), flag, baseInf.getErrorMsg(), null);
            separateOrderService.updateReturnOrderGoodsInf(baseInf.getReturnNumber(), flag, baseInf.getErrorMsg(), null);
        }
    }


    //************************************ 发送退单头及商品信息 end *************************


    //************************************ 发送退单券信息 begin *************************


    /**
     * 发送退单券信息到EBS并记录发送结果
     *
     * @param returnOrderCouponInfList 退单退券信息
     */
    @Override
    public void sendReturnOrderCouponInfAndRecord(List<ReturnOrderCouponInf> returnOrderCouponInfList) {
        Map<String, Object> result = sendReturnOrderCouponsToEbs(returnOrderCouponInfList);
        List<Long> returnCouponInfLineId = returnOrderCouponInfList.stream().map(ReturnOrderCouponInf::getLineId).collect(Collectors.toList());
        if (!(Boolean) result.get("success")) {
            updateReturnOrderCouponInf(returnCouponInfLineId, (String) result.get("msg"), null, AppWhetherFlag.N);
        } else {
            updateReturnOrderCouponInf(returnCouponInfLineId, null, new Date(), AppWhetherFlag.Y);
        }
    }


    private Map<String, Object> sendReturnOrderCouponsToEbs(List<ReturnOrderCouponInf> returnOrderCouponInfList) {
        log.info("sendReturnOrderCouponsToEbs, returnOrderCouponInfList=" + returnOrderCouponInfList);
        List<ReturnOrderCouponSecond> returnOrderCouponSeconds = new ArrayList<>();
        if (null != returnOrderCouponInfList && returnOrderCouponInfList.size() > 0) {
            for (ReturnOrderCouponInf couponInf : returnOrderCouponInfList) {
                ReturnOrderCouponSecond returnOrderCouponSecond = new ReturnOrderCouponSecond();
                returnOrderCouponSecond.setAttribute1(toString(couponInf.getAttribute1()));
                returnOrderCouponSecond.setAttribute2(toString(couponInf.getAttribute2()));
                returnOrderCouponSecond.setAttribute3(toString(couponInf.getAttribute3()));
                returnOrderCouponSecond.setAttribute4(toString(couponInf.getAttribute4()));
                returnOrderCouponSecond.setAttribute5(toString(couponInf.getAttribute5()));
                returnOrderCouponSecond.setBuyPrice(toString(couponInf.getBuyPrice()));
                returnOrderCouponSecond.setCouponTypeId(toString(couponInf.getCouponTypeId()));
                returnOrderCouponSecond.setLineId(toString(couponInf.getLineId()));
                returnOrderCouponSecond.setMainOrderNumber(toString(couponInf.getMainOrderNumber()));
                returnOrderCouponSecond.setMainReturnNumber(toString(couponInf.getMainReturnNumber()));
                returnOrderCouponSecond.setQuantity(toString(couponInf.getQuantity()));
                returnOrderCouponSecond.setSku(toString(couponInf.getSku()));
                returnOrderCouponSeconds.add(returnOrderCouponSecond);
            }

        }
        String returnOrderCouponJson = JSON.toJSONString(returnOrderCouponSeconds);
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        parameters.add(new BasicNameValuePair("returnOrderCouponJson", returnOrderCouponJson));
        Map<String, Object> result = this.postToEbs(AppConstant.EBS_NEW_URL + "callReturnOrderCouponSecond", parameters);
        if (!(Boolean) result.get("success")) {
            JSONObject content = new JSONObject();
            content.put("returnOrderCouponJson", returnOrderCouponJson);
            result.put("content", JSON.toJSONString(content));
        }
        log.info("sendReturnOrderCouponsToEbs, result=" + result);
        return result;
    }

    private void updateReturnOrderCouponInf(List<Long> returnCouponInfLineId, String msg, Date sendTime, AppWhetherFlag flag) {
        if (AssertUtil.isNotEmpty(returnCouponInfLineId)) {
            separateOrderService.updateReturnOrderCouponInf(returnCouponInfLineId, msg, sendTime, flag);
        }
    }


    //************************************ 发送退单券信息 begin *******************************

    //************************************ 发送退单退款信息 begin *****************************


    /**
     * 发送退单退款信息到EBS并记录发送结果
     *
     * @param returnOrderRefundInfList 退单退款信息
     */
    @Override
    public void sendReturnOrderRefundInfAndRecord(List<ReturnOrderRefundInf> returnOrderRefundInfList) {
        Map<String, Object> result = sendReturnOrderRefundToEbs(returnOrderRefundInfList);
        List<Long> refundInfIds = returnOrderRefundInfList.stream().map(ReturnOrderRefundInf::getRefundId).collect(Collectors.toList());
        if (!(Boolean) result.get("success")) {
            updateReturnOrderRefundInf(refundInfIds, (String) result.get("msg"), null, AppWhetherFlag.N);
        } else {
            updateReturnOrderRefundInf(refundInfIds, null, new Date(), AppWhetherFlag.Y);
        }
    }


    private Map<String, Object> sendReturnOrderRefundToEbs(List<ReturnOrderRefundInf> returnOrderRefundInfList) {
        log.info("sendReturnOrderRefundToEbs, returnOrderRefundInfList=" + returnOrderRefundInfList);
        List<ReturnOrderRefundSecond> returnOrderRefundSeconds = new ArrayList<>();
        if (AssertUtil.isNotEmpty(returnOrderRefundInfList)) {
            for (ReturnOrderRefundInf refundInf : returnOrderRefundInfList) {
                ReturnOrderRefundSecond returnOrderRefundSecond = new ReturnOrderRefundSecond();
                returnOrderRefundSecond.setAttribute1(toString(refundInf.getAttribute1()));
                returnOrderRefundSecond.setAttribute2(toString(refundInf.getAttribute2()));
                returnOrderRefundSecond.setAttribute3(toString(refundInf.getAttribute3()));
                returnOrderRefundSecond.setAttribute4(toString(refundInf.getAttribute4()));
                returnOrderRefundSecond.setAttribute5(toString(refundInf.getAttribute5()));
                returnOrderRefundSecond.setAmount(toString(refundInf.getAmount()));
                returnOrderRefundSecond.setDescription(toString(refundInf.getDescription()));
                returnOrderRefundSecond.setDiySiteCode(toString(refundInf.getDiySiteCode()));
                returnOrderRefundSecond.setMainOrderNumber(toString(refundInf.getMainOrderNumber()));
                returnOrderRefundSecond.setMainReturnNumber(toString(refundInf.getMainReturnNumber()));
                returnOrderRefundSecond.setRefundDate(toString(DateFormatUtils.format(refundInf.getRefundDate(), "yyyy-MM-dd HH:mm:ss")));
                returnOrderRefundSecond.setRefundId(toString(refundInf.getRefundId()));
                returnOrderRefundSecond.setRefundNumber(toString(refundInf.getRefundNumber()));
                returnOrderRefundSecond.setRefundType(toString(refundInf.getRefundType()));
                returnOrderRefundSecond.setSobId(toString(refundInf.getSobId()));
                returnOrderRefundSecond.setStoreOrgCode(toString(refundInf.getStoreOrgCode()));
                returnOrderRefundSecond.setUserId(toString(refundInf.getUserId()));
                returnOrderRefundSeconds.add(returnOrderRefundSecond);
            }
        }
        String returnOrderRefundJson = JSON.toJSONString(returnOrderRefundSeconds);
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        parameters.add(new BasicNameValuePair("returnOrderRefundJson", returnOrderRefundJson));
        Map<String, Object> result = this.postToEbs(AppConstant.EBS_NEW_URL + "callReturnOrderRefundSecond", parameters);
        if (!(Boolean) result.get("success")) {
            JSONObject content = new JSONObject();
            content.put("returnOrderRefundJson", returnOrderRefundJson);
            result.put("content", JSON.toJSONString(content));
        }
        log.info("sendReturnOrderRefundToEbs, result=" + result);
        return result;
    }

    private void updateReturnOrderRefundInf(List<Long> refundInfIds, String msg, Date sendTime, AppWhetherFlag flag) {
        if (AssertUtil.isNotEmpty(refundInfIds)) {
            separateOrderService.updateReturnOrderRefundInf(refundInfIds, msg, sendTime, flag);
        }
    }

    //************************************ 发送退单退款信息 end *****************************


    //************************************ 发送退单经销差价退款信息 begin *****************************

    /**
     * 发送退单经销差价退款信息到EBS并记录发送结果
     *
     * @param jxPriceDifferenceRefundInfs 退单经销差价退款信息
     */
    @Override
    public void sendReturnOrderJxPriceDifferenceRefundInfAndRecord(List<ReturnOrderJxPriceDifferenceRefundInf> jxPriceDifferenceRefundInfs) {
        Map<String, Object> result = sendReturnOrderJxPriceDifferenceRefundToEbs(jxPriceDifferenceRefundInfs);
        List<Long> refundInfIds = jxPriceDifferenceRefundInfs.stream().map(ReturnOrderJxPriceDifferenceRefundInf::getRefundId).collect(Collectors.toList());
        if (!(Boolean) result.get("success")) {
            updateReturnOrderJxPriceDifferenceRefundInf(refundInfIds, (String) result.get("msg"), null, AppWhetherFlag.N);
        } else {
            updateReturnOrderJxPriceDifferenceRefundInf(refundInfIds, null, new Date(), AppWhetherFlag.Y);
        }
    }

    private void updateReturnOrderJxPriceDifferenceRefundInf(List<Long> refundInfIds, String msg, Date sendTime, AppWhetherFlag flag) {
        if (AssertUtil.isNotEmpty(refundInfIds)) {
            separateOrderService.updateReturnOrderJxPriceDifferenceRefundInf(refundInfIds, msg, sendTime, flag);
        }
    }

    private Map<String, Object> sendReturnOrderJxPriceDifferenceRefundToEbs(List<ReturnOrderJxPriceDifferenceRefundInf> jxPriceDifferenceRefundInfs) {
        log.info("sendReturnOrderJxPriceDifferenceRefundToEbs, jxPriceDifferenceRefundInfs=" + jxPriceDifferenceRefundInfs);
        List<ReturnOrderJxPriceDifferenceRefundSecond> returnOrderJxPriceDifferenceRefundSeconds = new ArrayList<>(20);
        if (AssertUtil.isNotEmpty(jxPriceDifferenceRefundInfs)) {
            for (ReturnOrderJxPriceDifferenceRefundInf refundInf : jxPriceDifferenceRefundInfs) {
                ReturnOrderJxPriceDifferenceRefundSecond returnOrderJxPriceDifferenceRefundSecond = new ReturnOrderJxPriceDifferenceRefundSecond();
                returnOrderJxPriceDifferenceRefundSecond.setAmount(toString(refundInf.getAmount()));
                returnOrderJxPriceDifferenceRefundSecond.setAttribute1(toString(refundInf.getAttribute1()));
                returnOrderJxPriceDifferenceRefundSecond.setAttribute2(toString(refundInf.getAttribute2()));
                returnOrderJxPriceDifferenceRefundSecond.setAttribute3(toString(refundInf.getAttribute3()));
                returnOrderJxPriceDifferenceRefundSecond.setAttribute4(toString(refundInf.getAttribute4()));
                returnOrderJxPriceDifferenceRefundSecond.setAttribute5(toString(refundInf.getAttribute5()));
                returnOrderJxPriceDifferenceRefundSecond.setRefundDate(toString(DateFormatUtils.format(refundInf.getRefundDate(), "yyyy-MM-dd HH:mm:ss")));
                returnOrderJxPriceDifferenceRefundSecond.setDescription(toString(refundInf.getDescription()));
                returnOrderJxPriceDifferenceRefundSecond.setDiySiteCode(toString(refundInf.getDiySiteCode()));
                returnOrderJxPriceDifferenceRefundSecond.setMainOrderNumber(toString(refundInf.getMainOrderNumber()));
                returnOrderJxPriceDifferenceRefundSecond.setMainReturnNumber(toString(refundInf.getMainReturnNumber()));
                returnOrderJxPriceDifferenceRefundSecond.setRefundId(toString(refundInf.getRefundId()));
                returnOrderJxPriceDifferenceRefundSecond.setRefundNumber(toString(refundInf.getRefundNumber()));
                returnOrderJxPriceDifferenceRefundSecond.setSku(toString(refundInf.getSku()));
                returnOrderJxPriceDifferenceRefundSecond.setStoreOrgCode(toString(refundInf.getStoreOrgCode()));
                returnOrderJxPriceDifferenceRefundSecond.setSobId(toString(refundInf.getSobId()));
                returnOrderJxPriceDifferenceRefundSeconds.add(returnOrderJxPriceDifferenceRefundSecond);
            }
        }
        String returnOrderJxPriceDifferenceRefundJson = JSON.toJSONString(returnOrderJxPriceDifferenceRefundSeconds);
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        parameters.add(new BasicNameValuePair("returnOrderJxPriceDifferenceRefundJson", returnOrderJxPriceDifferenceRefundJson));
        Map<String, Object> result = this.postToEbs(AppConstant.EBS_NEW_URL + "callReturnOrderJxPriceDifferenceRefundSecond", parameters);
        if (!(Boolean) result.get("success")) {
            JSONObject content = new JSONObject();
            content.put("returnOrderJxPriceDifferenceRefundJson", returnOrderJxPriceDifferenceRefundJson);
            result.put("content", JSON.toJSONString(content));
        }
        log.info("sendReturnOrderJxPriceDifferenceRefundToEbs, result=" + result);
        return result;

    }

    //************************************ 发送退单经销差价退款信息 end *****************************


    //************************************ 发送订单运费信息 begin **********************************

    /**
     * 发送订单关键信息到EBS
     *
     * @param orderKeyInf 订单关键信息
     */
    @Override
    public void sendOrderKeyInfAndRecord(OrderKeyInf orderKeyInf) {
        Map<String, Object> result = sendOrderKeyInfToEbs(orderKeyInf);
        if (!(Boolean) result.get("success")) {
            updateOrderKeyInfFlagAndSendTimeAndErrorMsg(orderKeyInf.getId(), (String) result.get("msg"), null, AppWhetherFlag.N);
        } else {
            updateOrderKeyInfFlagAndSendTimeAndErrorMsg(orderKeyInf.getId(), null, new Date(), AppWhetherFlag.Y);
        }

    }

    @Override
    public void sendWithdrawRefundInfAndRecord(WithdrawRefundInf refundInf) {
        Map<String, Object> result = sendWithdrawRefundToEbs(refundInf);
        if (!(Boolean) result.get("success")) {
            updateWithdrawRefundFlagAndSendTimeAndErrorMsg(refundInf.getRefundId(), (String) result.get("msg"), null, AppWhetherFlag.N);
        } else {
            updateWithdrawRefundFlagAndSendTimeAndErrorMsg(refundInf.getRefundId(), null, new Date(), AppWhetherFlag.Y);
        }
    }

    private Map<String, Object> sendWithdrawRefundToEbs(WithdrawRefundInf refundInf) {
        log.info("sendWithdrawRefundToEbs, refundInf=" + refundInf);
        WithdrawRefundSecond refundSecond = new WithdrawRefundSecond();
        refundSecond.setAmount(toString(refundInf.getAmount()));
        refundSecond.setAttribute1(toString(refundInf.getAttribute1()));
        refundSecond.setAttribute2(toString(refundInf.getAttribute2()));
        refundSecond.setAttribute3(toString(refundInf.getAttribute3()));
        refundSecond.setAttribute4(toString(refundInf.getAttribute4()));
        refundSecond.setAttribute5(toString(refundInf.getAttribute5()));
        refundSecond.setDescription(toString(refundInf.getDescription()));
        refundSecond.setDiySiteCode(toString(refundInf.getDiySiteCode()));
        refundSecond.setRefundDate(toString(DateFormatUtils.format(refundInf.getRefundDate(), "yyyy-MM-dd HH:mm:ss")));
        refundSecond.setRefundId(toString(refundInf.getRefundId()));
        refundSecond.setRefundNumber(toString(refundInf.getRefundNumber()));
        refundSecond.setRefundType(toString(refundInf.getRefundType()));
        refundSecond.setSobId(toString(refundInf.getSobId()));
        refundSecond.setStoreOrgCode(toString(refundInf.getStoreOrgCode()));
        refundSecond.setUserid(toString(refundInf.getUserid()));
        refundSecond.setWithdrawNumber(toString(refundInf.getWithdrawNumber()));
        refundSecond.setWithdrawObj(toString(refundInf.getWithdrawObj()));
        refundSecond.setWithdrawType(toString(refundInf.getWithdrawType()));
        String withdrawRefundSecondJson = JSON.toJSONString(refundSecond);
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        parameters.add(new BasicNameValuePair("withdrawRefundJson", withdrawRefundSecondJson));
        Map<String, Object> result = this.postToEbs(AppConstant.EBS_NEW_URL + "callWithdrawRefundSecond", parameters);
        if (!(Boolean) result.get("success")) {
            JSONObject content = new JSONObject();
            content.put("withdrawRefundSecondJson", withdrawRefundSecondJson);
            result.put("content", JSON.toJSONString(content));
        }
        log.info("sendWithdrawRefundToEbs, result=" + result);
        return result;
    }

    private void updateWithdrawRefundFlagAndSendTimeAndErrorMsg(Long refundId, String msg, Date sendTime, AppWhetherFlag flag) {
        if (null != refundId) {
            separateOrderService.updateWithdrawRefundFlagAndSendTimeAndErrorMsg(refundId, msg, sendTime, flag);
        }
    }

    private Map<String, Object> sendOrderKeyInfToEbs(OrderKeyInf orderKeyInf) {
        log.info("sendOrderKeyInfToEbs, orderKeyInf=" + orderKeyInf);
        OrderKeySecond orderKeySecond = new OrderKeySecond();
        orderKeySecond.setFreight(toString(orderKeyInf.getFreight()));
        orderKeySecond.setId(toString(orderKeyInf.getId()));
        orderKeySecond.setAttribute1(toString(orderKeyInf.getAttribute1()));
        orderKeySecond.setAttribute2(toString(orderKeyInf.getAttribute2()));
        orderKeySecond.setAttribute3(toString(orderKeyInf.getAttribute3()));
        orderKeySecond.setAttribute4(toString(orderKeyInf.getAttribute4()));
        orderKeySecond.setAttribute5(toString(orderKeyInf.getAttribute5()));
        orderKeySecond.setMainOrderNumber(toString(orderKeyInf.getMainOrderNumber()));
        orderKeySecond.setOrderAmt(toString(orderKeyInf.getOrderAmt()));
        orderKeySecond.setGoodsAmt(toString(orderKeyInf.getGoodsAmt()));
        orderKeySecond.setDecCreditMoney(toString(orderKeyInf.getDecCreditMoney()));
        orderKeySecond.setDecPreDeposit(toString(orderKeyInf.getDecPreDeposit()));
        orderKeySecond.setDecSubvention(toString(orderKeyInf.getDecSubvention()));
        orderKeySecond.setEmpCreditMoney(toString(orderKeyInf.getEmpCreditMoney()));
        orderKeySecond.setArrearage(toString(orderKeyInf.getArrearage()));
        orderKeySecond.setType(toString(orderKeyInf.getType()));
        String orderKeySecondJson = JSON.toJSONString(orderKeySecond);
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        parameters.add(new BasicNameValuePair("orderKeyJson", orderKeySecondJson));
        Map<String, Object> result = this.postToEbs(AppConstant.EBS_NEW_URL + "callOrderKeySecond", parameters);
        if (!(Boolean) result.get("success")) {
            JSONObject content = new JSONObject();
            content.put("orderKeySecondJson", orderKeySecondJson);
            result.put("content", JSON.toJSONString(content));
        }
        log.info("sendOrderKeyInfToEbs, result=" + result);
        return result;
    }

    private void updateOrderKeyInfFlagAndSendTimeAndErrorMsg(Long id, String msg, Date sendTime, AppWhetherFlag sendFlag) {
        if (null != id) {
            separateOrderService.updateOrderKeyInfFlagAndSendTimeAndErrorMsg(id, msg, sendTime, sendFlag);
        }
    }


    //************************************ 发送订单运费信息 end **********************************

    private Map<String, Object> postToEbs(String url, List<NameValuePair> parameters) {
        Map<String, Object> result = Maps.newHashMap();
        HttpPost httppost = new HttpPost(url);
        httppost.setConfig(REQUEST_CONFIG);
        CloseableHttpClient httpclient = HttpClientBuilder.create().build();

        try {
            httppost.setEntity(new UrlEncodedFormEntity(parameters, "UTF-8"));
            CloseableHttpResponse response = httpclient.execute(httppost);
            log.info("postToEbs, response=" + response.toString());
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                String jsonResult = EntityUtils.toString(entity, "utf-8");
                log.info("postToEbs, jsonResult=" + jsonResult);
                JSONObject ebsResult = JSON.parseObject(jsonResult);
                if ("0".equals(ebsResult.getString("code"))) {
                    result.put("success", true);
                } else {
                    result.put("success", false);
                    result.put("msg", ebsResult.getString("message"));
                }
            } else {
                result.put("success", false);
                result.put("msg", "Http code:" + response.getStatusLine().getStatusCode());
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("msg", e.getMessage());
        }
        return result;
    }

    private String toString(Object obj) {
        if (obj == null) {
            return "";
        } else {
            return String.valueOf(obj);
        }
    }
}

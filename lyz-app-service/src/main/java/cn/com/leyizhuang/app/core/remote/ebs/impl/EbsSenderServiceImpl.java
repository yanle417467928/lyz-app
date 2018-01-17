package cn.com.leyizhuang.app.core.remote.ebs.impl;

import cn.com.leyizhuang.app.core.constant.AppConstant;
import cn.com.leyizhuang.app.core.constant.AppWhetherFlag;
import cn.com.leyizhuang.app.core.remote.ebs.EbsSenderService;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.ebs.OrderBaseInf;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.ebs.OrderCouponInf;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.ebs.OrderGoodsInf;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.ebs.OrderReceiptInf;
import cn.com.leyizhuang.app.foundation.service.AppSeparateOrderService;
import cn.com.leyizhuang.ebs.entity.dto.second.OrderCouponSecond;
import cn.com.leyizhuang.ebs.entity.dto.second.OrderGoodSecond;
import cn.com.leyizhuang.ebs.entity.dto.second.OrderReceiptSecond;
import cn.com.leyizhuang.ebs.entity.dto.second.OrderSecond;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

    /**
     * 配置请求的超时设置
     */
    private final RequestConfig REQUEST_CONFIG = RequestConfig.custom().setConnectionRequestTimeout(5000).setConnectTimeout(1000)
            .setSocketTimeout(5000).build();


    /**
     * 发送订单收款到EBS
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

    private void updateOrderReceiptFlagAndSendTimeAndErrorMsg(List<Long> receiptInfIds, String msg, Date sendTime, AppWhetherFlag flag) {
        if (null != receiptInfIds && receiptInfIds.size() > 0) {
            separateOrderService.updateOrderReceiptFlagAndSendTimeAndErrorMsg(receiptInfIds, msg, sendTime, flag);
        }
    }

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
     * 更新订单券接口信息
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
     * 更新订单状态
     *
     * @param orderInf  订单头信息
     * @param goodsInfs 订单商品信息
     */
    public void updateOrderFlag(final OrderBaseInf orderInf, final List<OrderGoodsInf> goodsInfs, AppWhetherFlag flag) {
        if (AppWhetherFlag.Y == flag) {
            separateOrderService.updateOrderBaseInfoSendFlagAndErrorMessageAndSendTime(orderInf.getOrderNumber(), flag, null, new Date());
            separateOrderService.updateOrderGoodsInfByOrderNumber(orderInf.getOrderNumber(), flag, null, new Date());
            /*for (OrderGoodsInf orderGoodsInf : goodsInfs) {
                separateOrderService.updateOrderGoodsInfoSendFlagAndErrorMessageAndSendTime(orderGoodsInf.getOrderLineId(), flag, null, new Date());
            }*/
        } else {
            separateOrderService.updateOrderBaseInfoSendFlagAndErrorMessageAndSendTime(orderInf.getOrderNumber(), flag, orderInf.getErrorMsg(), null);
            separateOrderService.updateOrderGoodsInfByOrderNumber(orderInf.getOrderNumber(), flag, orderInf.getErrorMsg(), null);
            /*for (OrderGoodsInf orderGoodsInf : goodsInfs) {
                separateOrderService.updateOrderGoodsInfoSendFlagAndErrorMessageAndSendTime(orderGoodsInf.getOrderLineId(), flag, orderInf.getErrorMsg(), null);
            }*/
        }

    }

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

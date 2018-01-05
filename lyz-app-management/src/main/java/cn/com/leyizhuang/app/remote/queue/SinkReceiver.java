package cn.com.leyizhuang.app.remote.queue;

import cn.com.leyizhuang.app.core.constant.AppGoodsLineType;
import cn.com.leyizhuang.app.core.constant.AppOrderSubjectType;
import cn.com.leyizhuang.app.core.utils.JsonUtils;
import cn.com.leyizhuang.app.core.utils.order.OrderUtils;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderBaseInfo;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderGoodsInfo;
import cn.com.leyizhuang.app.foundation.pojo.remote.queue.MqMessage;
import cn.com.leyizhuang.app.foundation.pojo.remote.queue.MqOrderChannel;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.ebs.OrderBaseInf;
import cn.com.leyizhuang.app.foundation.service.AppOrderService;
import cn.com.leyizhuang.app.foundation.service.AppSeparateOrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

/**
 * @author Richard
 * @create 2017/12/27.
 */
@EnableBinding(value = {MqOrderChannel.class})
@Slf4j
@Component
public class SinkReceiver {


    @Resource
    private AppOrderService orderService;

    @Resource
    private AppSeparateOrderService separateOrderService;


    ObjectMapper objectMapper = new ObjectMapper();

    @StreamListener(value = MqOrderChannel.RECEIVE_ORDER)
    public void receiveOrder(MqMessage message) {
        log.info("消费拆单消息队列中的消息 Begin");
        log.info("消息类型:{}", message.getType());
        log.info("消息内容:\n{}", JsonUtils.formatJson(message.getContent()));
        switch (message.getType()) {
            case ORDER:
                try {
                    String orderNumber = objectMapper.readValue(message.getContent(), String.class);
                    Boolean isExist = separateOrderService.isOrderExist(orderNumber);
                    if (isExist) {
                        log.info("该订单已拆单，不能重复拆单!");
                    } else {
                        OrderBaseInfo baseInfo = orderService.getOrderByOrderNumber(orderNumber);
                        if (null != baseInfo) {
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
                                List<OrderBaseInf> orderBaseInfList = new ArrayList<>(10);
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
                                        orderBaseInf.setSobId(baseInfo.getSobId());
                                        orderBaseInf.setStoreCode(baseInfo.getStoreCode());
                                        //orderBaseInf.setStoreOrgId(baseInfo.getStoreStructureCode());
                                        orderBaseInf.setOrderDate(baseInfo.getCreateTime());
                                        //订单类型
                                        orderBaseInf.setOrderTypeId(4L);
                                        orderBaseInf.setOrderSubjectType(baseInfo.getOrderSubjectType());
                                        if (baseInfo.getOrderSubjectType() == AppOrderSubjectType.STORE) {
                                            orderBaseInf.setUserId(baseInfo.getCustomerId());
                                            orderBaseInf.setSalesConsultId(baseInfo.getSalesConsultId());
                                        } else {
                                            orderBaseInf.setDecorateManagerId(baseInfo.getCreatorId());
                                        }
                                        //orderBaseInf.set


                                        orderBaseInfList.add(orderBaseInf);
                                    } else {
                                        // todo 记录拆单错误日志
                                    }
                                }

                                //循环保存分单基础信息
                                for (OrderBaseInf baseInf : orderBaseInfList) {
                                    separateOrderService.saveOrderBaseInf(baseInf);
                                }
                            } else {
                                //todo 记录拆单错误日志
                            }
                        }
                    }

                } catch (IOException e) {

                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
        log.info("消费拆单消息队列中的消息 End");

    }

}

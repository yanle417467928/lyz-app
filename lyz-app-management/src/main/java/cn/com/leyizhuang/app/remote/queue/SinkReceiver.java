package cn.com.leyizhuang.app.remote.queue;

import cn.com.leyizhuang.app.core.utils.JsonUtils;
import cn.com.leyizhuang.app.foundation.pojo.remote.queue.MqMessage;
import cn.com.leyizhuang.app.foundation.pojo.remote.queue.MqOrderChannel;
import cn.com.leyizhuang.app.foundation.service.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

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

    @Resource
    private ItyAllocationService ityAllocationService;
    @Resource
    private MaOrderService maOrderService;

    @Resource
    private MaReturnOrderService maReturnOrderService;

    private ObjectMapper objectMapper = new ObjectMapper();

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
                        //拆单
                        separateOrderService.separateOrder(orderNumber);
                        //拆单完成之后发送订单和订单商品信息到EBS
                        separateOrderService.sendOrderBaseInfAndOrderGoodsInf(orderNumber);
                        //发送订单券儿信息到EBS
                        separateOrderService.sendOrderCouponInf(orderNumber);
                        //发送订单收款信息到EBS
                        separateOrderService.sendOrderReceiptInf(orderNumber);
                        //发送经销差价返还信息到EBS
                        separateOrderService.sendOrderJxPriceDifferenceReturnInf(orderNumber);
                        //发送订单关键信息到EBS
                        separateOrderService.sendOrderKeyInf(orderNumber);
                    }
                } catch (IOException e) {
                    log.warn("消息格式错误!");
                    e.printStackTrace();
                } catch (Exception e) {
                    log.warn("{}", e);
                    e.printStackTrace();
                }
                break;
            case RECHARGE_RECEIPT:
                try {
                    String rechargeNo = objectMapper.readValue(message.getContent(), String.class);
                    Boolean isExist = separateOrderService.isRechargeReceiptExist(rechargeNo);
                    if (isExist) {
                        log.info("该充值单已拆单，不能重复拆单!");
                    } else {
                        //拆单
                        separateOrderService.separateRechargeReceipt(rechargeNo);

                        //发送充值收款信息
                        separateOrderService.sendRechargeReceiptInf(rechargeNo);
                    }
                } catch (IOException e) {
                    log.warn("消息格式错误!");
                    e.printStackTrace();
                } catch (Exception e) {
                    log.warn("{}", e);
                    e.printStackTrace();
                }
                break;
            case ALLOCATION_OUTBOUND:
                try {
                    String number = objectMapper.readValue(message.getContent(), String.class);
                    // 调拨出库
                    log.info("调拨单出库队列消费开始");
                    ityAllocationService.sendAllocationToEBSAndRecord(number);
                } catch (IOException e) {
                    log.warn("消息格式错误!");
                    e.printStackTrace();
                } catch (Exception e) {
                    log.warn("{}", e);
                    e.printStackTrace();
                }
                break;
            case ALLOCATION_INBOUND:
                try {
                    String number = objectMapper.readValue(message.getContent(), String.class);
                    // 调拨出库
                    log.info("调拨单入库队列消费开始");
                    ityAllocationService.sendAllocationReceivedToEBSAndRecord(number);
                } catch (IOException e) {
                    log.warn("消息格式错误!");
                    e.printStackTrace();
                } catch (Exception e) {
                    log.warn("{}", e);
                    e.printStackTrace();
                }
                break;
            case RETURN_ORDER:
                try {
                    String returnNumber = objectMapper.readValue(message.getContent(), String.class);
                    Boolean isExist = separateOrderService.isReturnOrderExist(returnNumber);
                    if (isExist) {
                        log.info("该退单已拆单，不能重复拆单!");
                    } else {
                        //拆退单
                        separateOrderService.separateReturnOrder(returnNumber);
                        //拆单完成之后发送退单和退单商品信息到EBS
                        separateOrderService.sendReturnOrderBaseInfAndReturnOrderGoodsInf(returnNumber);
                        //发送退单券儿信息
                        separateOrderService.sendReturnOrderCouponInf(returnNumber);
                        //发送退单退款信息
                        separateOrderService.sendReturnOrderRefundInf(returnNumber);
                        //发送经退单销差价扣除信息
                        separateOrderService.sendReturnOrderJxPriceDifferenceRefundInf(returnNumber);
                    }
                } catch (IOException e) {
                    log.warn("消息格式错误!");
                    e.printStackTrace();
                } catch (Exception e) {
                    log.warn("{}", e);
                    e.printStackTrace();
                }
                break;
            case ORDER_RECEIVE:
                try {
                    String orderNumber = objectMapper.readValue(message.getContent(), String.class);
                    // 门店自提单发货
                    log.info("门店自提单发货队列消费开始");
                    maOrderService.sendOrderReceiveInfAndRecord(orderNumber);
                } catch (IOException e) {
                    log.warn("消息格式错误!");
                    e.printStackTrace();
                } catch (Exception e) {
                    log.warn("{}", e);
                    e.printStackTrace();
                }
                break;
            case RETURN_ORDER_RECEIPT:
                try {
                    String returnNumber = objectMapper.readValue(message.getContent(), String.class);
                    // 门店退货单收货(自提单)
                    log.info("门店自提单收货发货队列消费开始");
                    maReturnOrderService.sendReturnOrderReceiptInfAndRecord(returnNumber);
                } catch (IOException e) {
                    log.warn("消息格式错误!");
                    e.printStackTrace();
                } catch (Exception e) {
                    log.warn("{}", e);
                    e.printStackTrace();
                }
                break;
            case WITHDRAW_REFUND:
                try {
                    String refundNo = objectMapper.readValue(message.getContent(), String.class);
                    Boolean isExist = separateOrderService.isWithdrawRefundExist(refundNo);
                    if (isExist) {
                        log.info("该充值单已拆单，不能重复拆单!");
                    } else {
                        //拆单
                        separateOrderService.separateWithdrawRefund(refundNo);

                        //发送提现收款信息
                        separateOrderService.sendWithdrawRefundInf(refundNo);
                    }
                } catch (IOException e) {
                    log.warn("消息格式错误!");
                    e.printStackTrace();
                } catch (Exception e) {
                    log.warn("{}", e);
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
        log.info("消费拆单消息队列中的消息 End");
    }

}

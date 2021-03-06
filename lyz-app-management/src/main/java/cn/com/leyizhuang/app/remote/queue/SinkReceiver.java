package cn.com.leyizhuang.app.remote.queue;

import cn.com.leyizhuang.app.core.utils.JsonUtils;
import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.pojo.remote.queue.MqMessage;
import cn.com.leyizhuang.app.foundation.pojo.remote.queue.MqOrderChannel;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.ebs.KdSell;
import cn.com.leyizhuang.app.foundation.service.*;
import cn.com.leyizhuang.app.remote.queue.stategy.KdSellGenerator;
import cn.com.leyizhuang.app.remote.queue.stategy.KdSellStrategy;
import cn.com.leyizhuang.app.remote.queue.stategy.OrderKdSellStrategy;
import cn.com.leyizhuang.app.remote.queue.stategy.ReturnOrderKdSellStrategy;
import cn.com.leyizhuang.common.util.AssertUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.List;

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

    @Resource
    private OrderKdSellStrategy orderKdSellStrategy;

    @Resource
    private ReturnOrderKdSellStrategy returnOrderKdSellStrategy;

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
                        //发送订单应收金额信息
                        separateOrderService.sendOrderReceivableInf(orderNumber);
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
                        log.info("订单二次传ebs");
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
                        //发送退单应退金额信息
                        separateOrderService.sendReturnOrderReceivableInf(returnNumber);

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
                    log.info("门店自提单收货队列消费开始");
                    //拆退单 log.info("门店自提单收货队列消费开始");
                    separateOrderService.separateReturnOrder(returnNumber);
                    //拆单完成之后发送退单和退单商品信息到EBS
                    separateOrderService.sendReturnOrderBaseInfAndReturnOrderGoodsInf(returnNumber);
                    //发送退单券儿信息
                    separateOrderService.sendReturnOrderCouponInf(returnNumber);
                    //发送退单退款信息
                    separateOrderService.sendReturnOrderRefundInf(returnNumber);
                    //发送经退单销差价扣除信息
                    separateOrderService.sendReturnOrderJxPriceDifferenceRefundInf(returnNumber);
                    //发送退单应退金额信息
                    separateOrderService.sendReturnOrderReceivableInf(returnNumber);
                     //发送门店退货基本信息
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
            case ORDER_RECEIPT:
                try {
                    String receiptNumber = objectMapper.readValue(message.getContent(), String.class);
                    Boolean isExist = separateOrderService.isReceiptExist(receiptNumber);
                    if (isExist) {
                        log.info("该订单收款已拆单，不能重复拆单!");
                    } else {
                        //拆单
                        separateOrderService.separateOrderReceipt(receiptNumber);
                        //发送订单收款信息到EBS
                        separateOrderService.sendOrderReceiptInfByReceiptNumber(receiptNumber);
                    }
                } catch (IOException e) {
                    log.warn("消息格式错误!");
                    e.printStackTrace();
                } catch (Exception e) {
                    log.warn("{}", e);
                    e.printStackTrace();
                }
                break;
            case ORDER_REFUND:
                try {
                    String returnNumber = objectMapper.readValue(message.getContent(), String.class);
                    //拆退款信息和经销差价
                    separateOrderService.separateOrderRefund(returnNumber);
                    //发送订单退款信息到EBS
                    separateOrderService.sendReturnOrderRefundInf(returnNumber);
                    //发送经退单销差价扣除信息
                    separateOrderService.sendReturnOrderJxPriceDifferenceRefundInf(returnNumber);
                } catch (IOException e) {
                    log.warn("消息格式错误!");
                    e.printStackTrace();
                } catch (Exception e) {
                    log.warn("{}", e);
                    e.printStackTrace();
                }
                break;
            case CREDIT_RECHARGE_RECEIPT:
                try {
                    String receiptNumber = objectMapper.readValue(message.getContent(), String.class);
                    Boolean isExist = separateOrderService.isCreditRechargeReceiptExist(receiptNumber);
                    if (isExist) {
                        log.info("该订单收款已拆单，不能重复拆单!");
                    } else {
                        //拆单
                        separateOrderService.separateCreditRechargeReceipt(receiptNumber);

                        //发送充值收款信息
                        log.info("收款二次传ebs");
                        separateOrderService.sendCreditRechargeReceiptInf(receiptNumber);
                    }
                } catch (IOException e) {
                    log.warn("消息格式错误!");
                    e.printStackTrace();
                } catch (Exception e) {
                    log.warn("{}", e);
                    e.printStackTrace();
                }
                break;
            case KD_SELL_SEND:
                try {
                    String mainOrderNumber = objectMapper.readValue(message.getContent(), String.class);
                    //生成金蝶销量明细
                    if (StringUtils.isNotBlank(mainOrderNumber)) {
                        //选择生成策略
                        KdSellStrategy sellStrategy;
                        if (mainOrderNumber.contains("XN")) {
                            sellStrategy = this.orderKdSellStrategy;
                        } else {
                            sellStrategy = this.returnOrderKdSellStrategy;
                        }
                        //创建策略执行器
                        KdSellGenerator kdSellGenerator = new KdSellGenerator(sellStrategy);

                        //按策略生成 金蝶销退明细
                        List<KdSell> kdSellList = kdSellGenerator.generate(mainOrderNumber);
                        if (AssertUtil.isNotEmpty(kdSellList)) {
                            kdSellList.forEach(p -> {
                                p.setCreateTime(new Date());
                            });
                            separateOrderService.saveKdSellList(kdSellList);
                        }

                    } else {
                        throw new RuntimeException("金蝶销退表主单号为空，消息内容:" + message.getContent());
                    }
                    //发送金蝶销量明细
                    separateOrderService.sendKdSell(mainOrderNumber);
                } catch (IOException e) {
                    e.printStackTrace();
                    log.warn("金蝶销退表主单号读取异常，消息内容:{}", message.getContent());
                }
                break;
            default:
                break;
        }
        log.info("消费拆单消息队列中的消息 End");
    }

}

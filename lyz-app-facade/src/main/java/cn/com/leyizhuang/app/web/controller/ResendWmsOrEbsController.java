package cn.com.leyizhuang.app.web.controller;

import cn.com.leyizhuang.app.core.constant.WmsInterfaceMethodType;
import cn.com.leyizhuang.app.foundation.pojo.AppStore;
import cn.com.leyizhuang.app.foundation.pojo.SalesConsult;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderBaseInfo;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderBillingDetails;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderGoodsInfo;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderLogisticsInfo;
import cn.com.leyizhuang.app.foundation.pojo.remote.queue.MqMessageType;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.wms.*;
import cn.com.leyizhuang.app.foundation.pojo.returnorder.ReturnOrderBaseInfo;
import cn.com.leyizhuang.app.foundation.pojo.returnorder.ReturnOrderGoodsInfo;
import cn.com.leyizhuang.app.foundation.pojo.returnorder.ReturnOrderLogisticInfo;
import cn.com.leyizhuang.app.foundation.service.*;
import cn.com.leyizhuang.app.remote.webservice.ICallWms;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import cn.com.leyizhuang.common.util.AssertUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jerry.Ren
 * create 2018-02-26 11:45
 * desc: 重传WMS或者EBS的控制器,后面需要移至后台
 **/
@Slf4j
@RestController
@RequestMapping(value = ResendWmsOrEbsController.PRE_URL)
public class ResendWmsOrEbsController {

    protected final static String PRE_URL = "/app/resend";

    private static final Logger logger = LoggerFactory.getLogger(ResendWmsOrEbsController.class);

    @Resource
    private ICallWms iCallWms;
    @Resource
    private AppToWmsOrderService appToWmsOrderService;
    @Resource
    private AppOrderService appOrderService;
    @Resource
    private ReturnOrderService returnOrderService;
    @Resource
    private AppStoreService appStoreService;
    @Resource
    private AppEmployeeService appEmployeeService;
    @Resource
    private AppSeparateOrderService separateOrderService;
    @Resource
    private ItyAllocationService ityAllocationService;
    /**
     * 重传订单到wms,// TODO 此方法后面需要移至后台
     *
     * @param method      传输类型
     * @param orderNumber 单号
     * @return 结果
     */
    @RequestMapping(value = "/wms/{method}/{orderNumber}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> retransmissionToWms(@PathVariable String method, @PathVariable String orderNumber) {
        ResultDTO<Object> resultDTO;
        logger.info("retransmissionToWms CALLED,重传订单到wms，入参 method:{}, orderNumber:{}", method, orderNumber);
        if (null == method) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "传输方式不能为空!", WmsInterfaceMethodType.values());
            logger.info("retransmissionToWms OUT,重传订单到wms，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == orderNumber) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "单号不能为空!", null);
            logger.info("retransmissionToWms OUT,重传订单到wms，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            if (WmsInterfaceMethodType.ORDER.getValue().equals(method)) {
                List<AtwRequisitionOrderGoods> requisitionOrderGoodsList = appToWmsOrderService.findAtwRequisitionOrderGoodsByOrderNo(orderNumber);
                if (AssertUtil.isEmpty(requisitionOrderGoodsList)) {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "传wms配送单的接口表无商品信息!请检查是否之前报错或者联系管理员.", null);
                    logger.info("retransmissionToWms OUT,重传订单到wms，出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }
                iCallWms.sendToWmsRequisitionOrderAndGoods(orderNumber);
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "重传成功!", null);
                logger.info("retransmissionToWms OUT,重传订单到wms，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            } else if (WmsInterfaceMethodType.RE_ORDER.getValue().equals(method)) {
                List<AtwRequisitionOrderGoods> requisitionOrderGoodsList = appToWmsOrderService.findAtwRequisitionOrderGoodsByOrderNo(orderNumber);
                if (AssertUtil.isEmpty(requisitionOrderGoodsList)) {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "传wms配送单的接口表无商品信息!请检查是否之前报错或者联系管理员.", null);
                    logger.info("retransmissionToWms OUT,重传订单到wms，出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }
                iCallWms.sendToWmsReturnOrderAndGoods(orderNumber);
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "重传成功!", null);
                logger.info("retransmissionToWms OUT,重传订单到wms，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            } else if (WmsInterfaceMethodType.C_ORDER.getValue().equals(method)) {
                AtwCancelOrderRequest atwCancelOrderRequest = appToWmsOrderService.findAtwCancelOrderByOrderNo(orderNumber);
                if (null != atwCancelOrderRequest.getSendFlag()) {
                    if (atwCancelOrderRequest.getSendFlag()) {
                        resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "已发送取消订单到wms,请勿重复发送!", null);
                        logger.info("retransmissionToWms OUT,重传订单到wms，出参 resultDTO:{}", resultDTO);
                        return resultDTO;
                    }
                }
                iCallWms.sendToWmsCancelOrder(orderNumber);
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "重传成功!", null);
                logger.info("retransmissionToWms OUT,重传订单到wms，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            } else if (WmsInterfaceMethodType.C_RE_ORDER.getValue().equals(method)) {
                AtwCancelReturnOrderRequest cancelReturnOrderRequest = appToWmsOrderService.findAtwCancelReturnOrderByReturnNo(orderNumber);
                if (null != cancelReturnOrderRequest.getSendFlag()) {
                    if (cancelReturnOrderRequest.getSendFlag()) {
                        resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "已发送取消退货单到wms,请勿重复发送!", null);
                        logger.info("retransmissionToWms OUT,重传订单到wms，出参 resultDTO:{}", resultDTO);
                        return resultDTO;
                    }
                }
                iCallWms.sendToWmsCancelReturnOrder(orderNumber);
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "重传成功!", null);
                logger.info("retransmissionToWms OUT,重传订单到wms，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            } else if (WmsInterfaceMethodType.RE_ORDER_ENTER.getValue().equals(method)) {
                AtwReturnOrderCheckEnter atwReturnOrderCheckEnter = appToWmsOrderService.findAtwReturnOrderCheckEnterByReturnNo(orderNumber);
                if (null != atwReturnOrderCheckEnter.getSendFlag()) {
                    if (atwReturnOrderCheckEnter.getSendFlag()) {
                        resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "已发送退货单确认到wms,请勿重复发送!", null);
                        logger.info("retransmissionToWms OUT,重传订单到wms，出参 resultDTO:{}", resultDTO);
                        return resultDTO;
                    }
                }
                iCallWms.sendToWmsReturnOrderCheck(orderNumber);
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "重传成功!", null);
                logger.info("retransmissionToWms OUT,重传订单到wms，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            List<String> typeList = new ArrayList<>();
            typeList.add(WmsInterfaceMethodType.ORDER.toString());
            typeList.add(WmsInterfaceMethodType.RE_ORDER.toString());
            typeList.add(WmsInterfaceMethodType.C_ORDER.toString());
            typeList.add(WmsInterfaceMethodType.C_RE_ORDER.toString());
            typeList.add(WmsInterfaceMethodType.RE_ORDER_ENTER.toString());
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "传输方式不正确!请选择:", typeList);
            logger.info("retransmissionToWms OUT,重传订单到wms，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "重传失败!发生未知异常!", null);
            logger.info("retransmissionToWms OUT,重传订单到wms，出参 resultDTO:{}", resultDTO);
            logger.debug("Exception:{}", e);
            return resultDTO;
        }
    }


    /**
     * WMS重新生成接口表订单数据,// TODO 此方法后面需要移至后台
     *
     * @param orderNumber
     * @return
     */
    @RequestMapping(value = "/wms/regenerate/{method}/{orderNumber}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> regenerateWmsInfo(@PathVariable String method, @PathVariable String orderNumber) {
        ResultDTO<Object> resultDTO;
        logger.info("retransmissionToWms CALLED,重新生成接口表订单数据，入参 method:{}, orderNumber:{}", method, orderNumber);
        if (null == method) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "传输方式不能为空!", WmsInterfaceMethodType.values());
            logger.info("retransmissionToWms OUT,重新生成接口表订单数据失败！出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == orderNumber) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "单号不能为空!", null);
            logger.info("retransmissionToWms OUT,重新生成接口表订单数据失败！出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }

        try {
            if (WmsInterfaceMethodType.ORDER.getValue().equals(method)) {
                //查主单
                OrderBaseInfo baseInfo = appOrderService.getOrderByOrderNumber(orderNumber);
                if (baseInfo == null) {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "App没有查询到该单！检查是否单号错误！", null);
                    logger.info("retransmissionToWms OUT,重新生成接口表订单数据失败！出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }
                AppStore store = appStoreService.findStoreByUserIdAndIdentityType(baseInfo.getCreatorId(),
                        baseInfo.getCreatorIdentityType().getValue());
                OrderBillingDetails billingDetails = appOrderService.getOrderBillingDetail(orderNumber);
                List<OrderGoodsInfo> orderGoodsInfoList = appOrderService.getOrderGoodsInfoByOrderNumber(baseInfo.getOrderNumber());
                int orderGoodsSize = orderGoodsInfoList.size();
                OrderLogisticsInfo orderLogisticsInfo = appOrderService.getOrderLogistice(baseInfo.getOrderNumber());
                AtwRequisitionOrder requisitionOrder = AtwRequisitionOrder.transform(baseInfo, orderLogisticsInfo,
                        store, billingDetails, orderGoodsSize);
                appToWmsOrderService.saveAtwRequisitionOrder(requisitionOrder);
                //保存传wms配送单商品信息
                if (orderGoodsSize > 0) {
                    for (OrderGoodsInfo goodsInfo : orderGoodsInfoList) {
                        AtwRequisitionOrderGoods requisitionOrderGoods = AtwRequisitionOrderGoods.transform(goodsInfo.getOrderNumber(),
                                goodsInfo.getSku(), goodsInfo.getSkuName(), goodsInfo.getRetailPrice(), goodsInfo.getOrderQuantity(), goodsInfo.getCompanyFlag());
                        appToWmsOrderService.saveAtwRequisitionOrderGoods(requisitionOrderGoods);
                    }
                }
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "重新生成成功!", null);
                logger.info("retransmissionToWms OUT,重新生成接口表订单数据失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            } else if (WmsInterfaceMethodType.RE_ORDER.getValue().equals(method)) {

                //查主退单
                ReturnOrderBaseInfo returnOrderBaseInfo = returnOrderService.queryByReturnNo(orderNumber);
                if (returnOrderBaseInfo == null) {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "App没有查询到该单！检查是否单号错误！", null);
                    logger.info("retransmissionToWms OUT,重新生成接口表订单数据失败！出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }
                OrderBaseInfo orderBaseInfo = appOrderService.getOrderByOrderNumber(returnOrderBaseInfo.getOrderNo());
                AppStore appStore = appStoreService.findStoreByUserIdAndIdentityType(returnOrderBaseInfo.getCreatorId(),
                        returnOrderBaseInfo.getCreatorIdentityType().getValue());
                List<ReturnOrderGoodsInfo> returnOrderGoodsInfoList = returnOrderService.findReturnOrderGoodsInfoByOrderNumber(orderNumber);
                int size = returnOrderGoodsInfoList.size();
                ReturnOrderLogisticInfo returnOrderLogisticInfo = returnOrderService.getReturnOrderLogisticeInfo(orderNumber);
                SalesConsult salesConsult = appEmployeeService.findSellerByUserIdAndIdentityType(returnOrderBaseInfo.getCreatorId(),
                        returnOrderBaseInfo.getCreatorIdentityType().getValue());
                AtwReturnOrder atwReturnOrder = AtwReturnOrder.transform(returnOrderBaseInfo, returnOrderLogisticInfo, appStore, orderBaseInfo, size, salesConsult);
                appToWmsOrderService.saveAtwReturnOrder(atwReturnOrder);
                //保存传wms退货单商品信息
                if (size > 0) {
                    for (ReturnOrderGoodsInfo returnOrderGoodsInfo : returnOrderGoodsInfoList) {
                        AtwRequisitionOrderGoods requisitionOrderGoods = AtwRequisitionOrderGoods.transform(returnOrderGoodsInfo.getReturnNo(),
                                returnOrderGoodsInfo.getSku(), returnOrderGoodsInfo.getSkuName(), returnOrderGoodsInfo.getRetailPrice(), returnOrderGoodsInfo.getReturnQty(), returnOrderGoodsInfo.getCompanyFlag());
                        appToWmsOrderService.saveAtwRequisitionOrderGoods(requisitionOrderGoods);
                    }
                }
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "重新生成成功!", null);
                logger.info("retransmissionToWms OUT,重新生成接口表订单数据失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            } else if (WmsInterfaceMethodType.C_ORDER.getValue().equals(method)) {
                //查主单
                OrderBaseInfo baseInfo = appOrderService.getOrderByOrderNumber(orderNumber);
                if (baseInfo == null) {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "App没有查询到该单！检查是否单号错误！", null);
                    logger.info("retransmissionToWms OUT,重新生成接口表订单数据失败！出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }
                AtwCancelOrderRequest atwCancelOrderRequest = AtwCancelOrderRequest.transform("", baseInfo);
                appToWmsOrderService.saveAtwCancelOrderRequest(atwCancelOrderRequest);
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "重新生成成功!", null);
                logger.info("retransmissionToWms OUT,重新生成接口表订单数据失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            } else if (WmsInterfaceMethodType.C_RE_ORDER.getValue().equals(method)) {
                //查主退单
                ReturnOrderBaseInfo returnOrderBaseInfo = returnOrderService.queryByReturnNo(orderNumber);
                if (returnOrderBaseInfo == null) {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "App没有查询到该单！检查是否单号错误！", null);
                    logger.info("retransmissionToWms OUT,重新生成接口表订单数据失败！出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }
                AtwCancelReturnOrderRequest atwCancelOrderRequest = AtwCancelReturnOrderRequest.transform(returnOrderBaseInfo);
                appToWmsOrderService.saveAtwCancelReturnOrderRequest(atwCancelOrderRequest);
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "重新生成成功!", null);
                logger.info("retransmissionToWms OUT,重新生成接口表订单数据失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            } else if (WmsInterfaceMethodType.RE_ORDER_ENTER.getValue().equals(method)) {
                //查主退单
                ReturnOrderBaseInfo returnOrderBaseInfo = returnOrderService.queryByReturnNo(orderNumber);
                if (returnOrderBaseInfo == null) {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "App没有查询到该单！检查是否单号错误！", null);
                    logger.info("retransmissionToWms OUT,重新生成接口表订单数据失败！出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }
                AtwReturnOrderCheckEnter checkEnter = AtwReturnOrderCheckEnter.transform(returnOrderBaseInfo);
                appToWmsOrderService.saveAtwReturnOrderCheckEnter(checkEnter);
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "重新生成成功!", null);
                logger.info("retransmissionToWms OUT,重新生成接口表订单数据失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            List<String> typeList = new ArrayList<>();
            typeList.add(WmsInterfaceMethodType.ORDER.toString());
            typeList.add(WmsInterfaceMethodType.RE_ORDER.toString());
            typeList.add(WmsInterfaceMethodType.C_ORDER.toString());
            typeList.add(WmsInterfaceMethodType.C_RE_ORDER.toString());
            typeList.add(WmsInterfaceMethodType.RE_ORDER_ENTER.toString());
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "传输方式不正确!请选择:", typeList);
            logger.info("retransmissionToWms OUT,重新生成接口表订单数据失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "生成失败!发生未知异常!", null);
            logger.info("retransmissionToWms OUT,重新生成接口表订单数据失败，出参 resultDTO:{}", resultDTO);
            logger.debug("Exception:{}", e);
            return resultDTO;
        }
    }


    /**
     * EBS订单重传
     *
     * @param orderNumber
     * @return
     */
    @RequestMapping(value = "/EBS/{method}/{orderNumber}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResultDTO retransmissionToEBS(@PathVariable String method, @PathVariable String orderNumber) {
        ResultDTO<Object> resultDTO;
        logger.info("retransmissionToEBS CALLED,EBS订单重传，入参 method:{},orderNumber:{}", method, orderNumber);
        MqMessageType type = MqMessageType.getMqMessageTypeByValue(method);
        if (null == type) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "传输方式不正确!", null);
            logger.info("retransmissionToEBS OUT,EBS订单重传失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == orderNumber) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "单号不能为空!", null);
            logger.info("retransmissionToEBS OUT,EBS订单重传失败！出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }

        try {
            switch (type) {
                case ORDER:
                    try {
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
                    } catch (Exception e) {
                        log.warn("{}", e);
                        e.printStackTrace();
                    }
                    break;
                case RECHARGE_RECEIPT:
                    try {
                        Boolean isExist = separateOrderService.isRechargeReceiptExist(orderNumber);
                        if (isExist) {
                            log.info("该充值单已拆单，不能重复拆单!");
                        } else {
                            //拆单
                            separateOrderService.separateRechargeReceipt(orderNumber);

                            //发送充值收款信息
                            log.info("订单二次传ebs");
                            separateOrderService.sendRechargeReceiptInf(orderNumber);
                        }
                    } catch (Exception e) {
                        log.warn("{}", e);
                        e.printStackTrace();
                    }
                    break;
                case ALLOCATION_OUTBOUND:
                    try {
                        // 调拨出库
                        log.info("调拨单出库队列消费开始");
                        ityAllocationService.sendAllocationToEBSAndRecord(orderNumber);
                    } catch (Exception e) {
                        log.warn("{}", e);
                        e.printStackTrace();
                    }
                    break;
                case ALLOCATION_INBOUND:
                    try {
                        // 调拨出库
                        log.info("调拨单入库队列消费开始");
                        ityAllocationService.sendAllocationReceivedToEBSAndRecord(orderNumber);
                    } catch (Exception e) {
                        log.warn("{}", e);
                        e.printStackTrace();
                    }
                    break;
                case RETURN_ORDER:
                    try {
                        Boolean isExist = separateOrderService.isReturnOrderExist(orderNumber);
                        if (isExist) {
                            log.info("该退单已拆单，不能重复拆单!");
                        } else {
                            //拆退单
                            separateOrderService.separateReturnOrder(orderNumber);
                            //拆单完成之后发送退单和退单商品信息到EBS
                            separateOrderService.sendReturnOrderBaseInfAndReturnOrderGoodsInf(orderNumber);
                            //发送退单券儿信息
                            separateOrderService.sendReturnOrderCouponInf(orderNumber);
                            //发送退单退款信息
                            separateOrderService.sendReturnOrderRefundInf(orderNumber);
                            //发送经退单销差价扣除信息
                            separateOrderService.sendReturnOrderJxPriceDifferenceRefundInf(orderNumber);
                        }
                    } catch (Exception e) {
                        log.warn("{}", e);
                        e.printStackTrace();
                    }
                    break;
                case ORDER_RECEIVE:
                    try {
                        // 门店自提单发货
                        log.info("门店自提单发货队列消费开始");
//                        maOrderService.sendOrderReceiveInfAndRecord(orderNumber);
                    } catch (Exception e) {
                        log.warn("{}", e);
                        e.printStackTrace();
                    }
                    break;
                case RETURN_ORDER_RECEIPT:
                    try {
                        // 门店退货单收货(自提单)
                        log.info("门店自提单收货队列消费开始");
                        //拆退单 log.info("门店自提单收货队列消费开始");
                        separateOrderService.separateReturnOrder(orderNumber);
                        //拆单完成之后发送退单和退单商品信息到EBS
                        separateOrderService.sendReturnOrderBaseInfAndReturnOrderGoodsInf(orderNumber);
                        //发送退单券儿信息
                        separateOrderService.sendReturnOrderCouponInf(orderNumber);
                        //发送退单退款信息
                        separateOrderService.sendReturnOrderRefundInf(orderNumber);
                        //发送经退单销差价扣除信息
                        separateOrderService.sendReturnOrderJxPriceDifferenceRefundInf(orderNumber);
                        //发送门店退货基本信息
//                        maReturnOrderService.sendReturnOrderReceiptInfAndRecord(orderNumber);
                    } catch (Exception e) {
                        log.warn("{}", e);
                        e.printStackTrace();
                    }
                    break;
                case WITHDRAW_REFUND:
                    try {
                        Boolean isExist = separateOrderService.isWithdrawRefundExist(orderNumber);
                        if (isExist) {
                            log.info("该充值单已拆单，不能重复拆单!");
                        } else {
                            //拆单
                            separateOrderService.separateWithdrawRefund(orderNumber);

                            //发送提现收款信息
                            separateOrderService.sendWithdrawRefundInf(orderNumber);
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
                        Boolean isExist = separateOrderService.isReceiptExist(orderNumber);
                        if (isExist) {
                            log.info("该订单收款已拆单，不能重复拆单!");
                        } else {
                            //拆单
                            separateOrderService.separateOrderReceipt(orderNumber);
                            //发送订单收款信息到EBS
                            separateOrderService.sendOrderReceiptInfByReceiptNumber(orderNumber);
                        }
                    } catch (Exception e) {
                        log.warn("{}", e);
                        e.printStackTrace();
                    }
                    break;
                case ORDER_REFUND:
                    try {
                        //拆退款信息和经销差价
                        separateOrderService.separateOrderRefund(orderNumber);
                        //发送订单退款信息到EBS
                        separateOrderService.sendReturnOrderRefundInf(orderNumber);
                        //发送经退单销差价扣除信息
                        separateOrderService.sendReturnOrderJxPriceDifferenceRefundInf(orderNumber);
                    } catch (Exception e) {
                        log.warn("{}", e);
                        e.printStackTrace();
                    }
                    break;
                case CREDIT_RECHARGE_RECEIPT:
                    try {
                        Boolean isExist = separateOrderService.isCreditRechargeReceiptExist(orderNumber);
                        if (isExist) {
                            log.info("该订单收款已拆单，不能重复拆单!");
                        } else {
                            //拆单
                            separateOrderService.separateCreditRechargeReceipt(orderNumber);

                            //发送充值收款信息
                            log.info("收款二次传ebs");
                            separateOrderService.sendCreditRechargeReceiptInf(orderNumber);
                        }
                    } catch (Exception e) {
                        log.warn("{}", e);
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "重传成功!", null);
            logger.info("retransmissionToEBS OUT,EBS订单重传成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "传输失败!发生未知异常!", null);
            logger.info("retransmissionToEBS OUT,EBS订单重传失败，出参 resultDTO:{}", resultDTO);
            logger.debug("Exception:{}", e);
            return resultDTO;
        }
    }


    //*****************************下面是重传EBS拆分重传***********************************

    /**
     * EBS重传商品
     *
     * @param orderNumber
     * @return
     */
    @RequestMapping(value = "/EBS/order/goods/{orderNumber}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResultDTO retransmissionOrderAndGoodsToEBS(@PathVariable String orderNumber) {
        ResultDTO<Object> resultDTO;
        logger.info("retransmissionToWms CALLED,EBS重传商品，入参 orderNumber:{}", orderNumber);
        if (null == orderNumber) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "单号不能为空!", null);
            logger.info("retransmissionToWms OUT,EBS重传商品失败！出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            separateOrderService.sendOrderBaseInfAndOrderGoodsInf(orderNumber);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "重传成功!", null);
            logger.info("retransmissionToWms OUT,EBS重传商品失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "传输失败!发生未知异常!", null);
            logger.info("retransmissionToWms OUT,EBS重传商品失败，出参 resultDTO:{}", resultDTO);
            logger.debug("Exception:{}", e);
            return resultDTO;
        }
    }

    /**
     * EBS重传券
     *
     * @param orderNumber
     * @return
     */
    @RequestMapping(value = "/EBS/order/coupon/{orderNumber}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResultDTO retransmissionOrderCouponToEBS(@PathVariable String orderNumber) {
        ResultDTO<Object> resultDTO;
        logger.info("retransmissionOrderCouponToEBS CALLED,EBS重传券，入参 orderNumber:{}", orderNumber);
        if (null == orderNumber) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "单号不能为空!", null);
            logger.info("retransmissionOrderCouponToEBS OUT,EBS重传券失败！出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }

        try {
            separateOrderService.sendOrderCouponInf(orderNumber);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "重传成功!", null);
            logger.info("retransmissionOrderCouponToEBS OUT,EBS重传券失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "传输失败!发生未知异常!", null);
            logger.info("retransmissionOrderCouponToEBS OUT,EBS重传券失败，出参 resultDTO:{}", resultDTO);
            logger.debug("Exception:{}", e);
            return resultDTO;
        }
    }

    /**
     * EBS重传收款
     *
     * @param orderNumber
     * @return
     */
    @RequestMapping(value = "/EBS/order/receipt/{orderNumber}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResultDTO retransmissionOrderReceiptToEBS(@PathVariable String orderNumber) {
        ResultDTO<Object> resultDTO;
        logger.info("retransmissionOrderReceiptToEBS CALLED,EBS重传收款，入参 orderNumber:{}", orderNumber);
        if (null == orderNumber) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "单号不能为空!", null);
            logger.info("retransmissionOrderReceiptToEBS OUT,EBS重传收款失败！出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }

        try {
            separateOrderService.sendOrderReceiptInf(orderNumber);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "重传成功!", null);
            logger.info("retransmissionOrderReceiptToEBS OUT,EBS重传收款失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "传输失败!发生未知异常!", null);
            logger.info("retransmissionOrderReceiptToEBS OUT,EBS重传收款失败，出参 resultDTO:{}", resultDTO);
            logger.debug("Exception:{}", e);
            return resultDTO;
        }
    }

    /**
     * EBS重传经销差价
     *
     * @param orderNumber
     * @return
     */
    @RequestMapping(value = "/EBS/order/jxPrice/{orderNumber}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResultDTO retransmissionOrderJxPriceToEBS(@PathVariable String orderNumber) {
        ResultDTO<Object> resultDTO;
        logger.info("retransmissionOrderJxPriceToEBS CALLED,EBS重传经销差价，入参 orderNumber:{}", orderNumber);
        if (null == orderNumber) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "单号不能为空!", null);
            logger.info("retransmissionOrderJxPriceToEBS OUT,EBS重传经销差价失败！出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }

        try {
            separateOrderService.sendOrderJxPriceDifferenceReturnInf(orderNumber);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "重传成功!", null);
            logger.info("retransmissionOrderJxPriceToEBS OUT,EBS重传经销差价失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "传输失败!发生未知异常!", null);
            logger.info("retransmissionOrderJxPriceToEBS OUT,EBS重传经销差价失败，出参 resultDTO:{}", resultDTO);
            logger.debug("Exception:{}", e);
            return resultDTO;
        }
    }

    /**
     * EBS重传充值收款
     *
     * @param rechargeNo
     * @return
     */
    @RequestMapping(value = "/EBS/recharge/{rechargeNo}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResultDTO retransmissionRechargeReceiptToEBS(@PathVariable String rechargeNo) {
        ResultDTO<Object> resultDTO;
        logger.info("retransmissionRechargeReceiptToEBS CALLED,重传充值收款，入参 orderNumber:{}", rechargeNo);
        if (null == rechargeNo) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "单号不能为空!", null);
            logger.info("retransmissionRechargeReceiptToEBS OUT,重传充值收款失败！出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }

        try {
            separateOrderService.sendRechargeReceiptInf(rechargeNo);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "重传成功!", null);
            logger.info("retransmissionRechargeReceiptToEBS OUT,重传充值收款失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "传输失败!发生未知异常!", null);
            logger.info("retransmissionRechargeReceiptToEBS OUT,重传充值收款失败，出参 resultDTO:{}", resultDTO);
            logger.debug("Exception:{}", e);
            return resultDTO;
        }
    }

    /**
     * EBS重传库存调拨出库
     *
     * @param number
     * @return
     */
    @RequestMapping(value = "/EBS/allocation/out/{number}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResultDTO retransmissionAllocationOutToEBS(@PathVariable String number) {
        ResultDTO<Object> resultDTO;
        logger.info("retransmissionAllocationOutToEBS CALLED,EBS重传库存调拨出库，入参 orderNumber:{}", number);
        if (null == number) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "单号不能为空!", null);
            logger.info("retransmissionAllocationOutToEBS OUT,EBS重传库存调拨出库失败！出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }

        try {
            ityAllocationService.sendAllocationToEBSAndRecord(number);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "重传成功!", null);
            logger.info("retransmissionAllocationOutToEBS OUT,EBS重传库存调拨出库失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "传输失败!发生未知异常!", null);
            logger.info("retransmissionAllocationOutToEBS OUT,EBS重传库存调拨出库失败，出参 resultDTO:{}", resultDTO);
            logger.debug("Exception:{}", e);
            return resultDTO;
        }
    }

    /**
     * EBS重传库存调拨入库
     *
     * @param number
     * @return
     */
    @RequestMapping(value = "/EBS/allocation/in/{number}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResultDTO retransmissionAllocationInToEBS(@PathVariable String number) {
        ResultDTO<Object> resultDTO;
        logger.info("retransmissionAllocationInToEBS CALLED,EBS重传库存调拨入库，入参 orderNumber:{}", number);
        if (null == number) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "单号不能为空!", null);
            logger.info("retransmissionAllocationInToEBS OUT,EBS重传库存调拨入库失败！出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }

        try {
            ityAllocationService.sendAllocationReceivedToEBSAndRecord(number);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "重传成功!", null);
            logger.info("retransmissionAllocationInToEBS OUT,EBS重传库存调拨入库失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "传输失败!发生未知异常!", null);
            logger.info("retransmissionAllocationInToEBS OUT,EBS重传库存调拨入库失败，出参 resultDTO:{}", resultDTO);
            logger.debug("Exception:{}", e);
            return resultDTO;
        }
    }

    /**
     * EBS重传退单商品
     *
     * @param returnNumber
     * @return
     */
    @RequestMapping(value = "/EBS/return/goods/{returnNumber}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResultDTO retransmissionReturnOrderAndGoodsToEBS(@PathVariable String returnNumber) {
        ResultDTO<Object> resultDTO;
        logger.info("retransmissionReturnOrderAndGoodsToEBS CALLED,EBS重传退单商品，入参 orderNumber:{}", returnNumber);
        if (null == returnNumber) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "单号不能为空!", null);
            logger.info("retransmissionReturnOrderAndGoodsToEBS OUT,EBS重传退单商品失败！出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }

        try {
            separateOrderService.sendReturnOrderBaseInfAndReturnOrderGoodsInf(returnNumber);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "重传成功!", null);
            logger.info("retransmissionReturnOrderAndGoodsToEBS OUT,EBS重传退单商品失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "传输失败!发生未知异常!", null);
            logger.info("retransmissionReturnOrderAndGoodsToEBS OUT,EBS重传退单商品失败，出参 resultDTO:{}", resultDTO);
            logger.debug("Exception:{}", e);
            return resultDTO;
        }
    }

    /**
     * EBS重传退单券
     *
     * @param returnNumber
     * @return
     */
    @RequestMapping(value = "/EBS/return/coupon/{returnNumber}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResultDTO retransmissionReturnOrderAndCouponToEBS(@PathVariable String returnNumber) {
        ResultDTO<Object> resultDTO;
        logger.info("retransmissionReturnOrderAndCouponToEBS CALLED,EBS重传退单券，入参 returnNumber:{}", returnNumber);
        if (null == returnNumber) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "单号不能为空!", null);
            logger.info("retransmissionReturnOrderAndCouponToEBS OUT,EBS重传退单券失败！出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }

        try {
            separateOrderService.sendReturnOrderCouponInf(returnNumber);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "重传成功!", null);
            logger.info("retransmissionReturnOrderAndCouponToEBS OUT,EBS重传退单券失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "传输失败!发生未知异常!", null);
            logger.info("retransmissionReturnOrderAndCouponToEBS OUT,EBS重传退单券失败，出参 resultDTO:{}", resultDTO);
            logger.debug("Exception:{}", e);
            return resultDTO;
        }
    }

    /**
     * EBS重传退单退款信息
     *
     * @param returnNumber
     * @return
     */
    @RequestMapping(value = "/EBS/return/refund/{returnNumber}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResultDTO retransmissionReturnOrderRefundToEBS(@PathVariable String returnNumber) {
        ResultDTO<Object> resultDTO;
        logger.info("retransmissionReturnOrderRefundToEBS CALLED,EBS重传退单退款信息，入参 returnNumber:{}", returnNumber);
        if (null == returnNumber) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "单号不能为空!", null);
            logger.info("retransmissionReturnOrderRefundToEBS OUT,EBS重传退单退款信息失败！出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }

        try {
            separateOrderService.sendReturnOrderRefundInf(returnNumber);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "重传成功!", null);
            logger.info("retransmissionReturnOrderRefundToEBS OUT,EBS重传退单退款信息失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "传输失败!发生未知异常!", null);
            logger.info("retransmissionReturnOrderRefundToEBS OUT,EBS重传退单退款信息失败，出参 resultDTO:{}", resultDTO);
            logger.debug("Exception:{}", e);
            return resultDTO;
        }
    }

    /**
     * EBS重传退单经销差价信息
     *
     * @param returnNumber
     * @return
     */
    @RequestMapping(value = "/EBS/return/jxPrice/{returnNumber}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResultDTO retransmissionReturnOrderJxPriceToEBS(@PathVariable String returnNumber) {
        ResultDTO<Object> resultDTO;
        logger.info("retransmissionReturnOrderJxPriceToEBS CALLED,EBS重传退单经销差价信息，入参 returnNumber:{}", returnNumber);
        if (null == returnNumber) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "单号不能为空!", null);
            logger.info("retransmissionReturnOrderJxPriceToEBS OUT,EBS重传退单经销差价信息失败！出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }

        try {
            separateOrderService.sendReturnOrderJxPriceDifferenceRefundInf(returnNumber);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "重传成功!", null);
            logger.info("retransmissionReturnOrderJxPriceToEBS OUT,EBS重传退单经销差价信息失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "传输失败!发生未知异常!", null);
            logger.info("retransmissionReturnOrderJxPriceToEBS OUT,EBS重传退单经销差价信息失败，出参 resultDTO:{}", resultDTO);
            logger.debug("Exception:{}", e);
            return resultDTO;
        }
    }

    /**
     * EBS重传门店自提收款信息
     *
     * @param orderNumber
     * @return
     */
    @RequestMapping(value = "/EBS/order/store/receive/{orderNumber}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResultDTO retransmissionOrderReceiveInfAndRecordToEBS(@PathVariable String orderNumber) {
        ResultDTO<Object> resultDTO;
        logger.info("retransmissionOrderReceiveInfAndRecordToEBS CALLED,EBS重传门店自提收款信息，入参 orderNumber:{}", orderNumber);
        if (null == orderNumber) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "单号不能为空!", null);
            logger.info("retransmissionOrderReceiveInfAndRecordToEBS OUT,EBS重传门店自提收款信息失败！出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }

        try {
//            maOrderService.sendOrderReceiveInfAndRecord(orderNumber);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "重传成功!", null);
            logger.info("retransmissionOrderReceiveInfAndRecordToEBS OUT,EBS重传门店自提收款信息失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "传输失败!发生未知异常!", null);
            logger.info("retransmissionOrderReceiveInfAndRecordToEBS OUT,EBS重传门店自提收款信息失败，出参 resultDTO:{}", resultDTO);
            logger.debug("Exception:{}", e);
            return resultDTO;
        }
    }

    /**
     * EBS重传提现收款
     *
     * @param refundNo
     * @return
     */
    @RequestMapping(value = "/EBS/withdraw/refund/{refundNo}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResultDTO retransmissionWithdrawRefundToEBS(@PathVariable String refundNo) {
        ResultDTO<Object> resultDTO;
        logger.info("retransmissionWithdrawRefundToEBS CALLED,EBS重传提现收款，入参 refundNo:{}", refundNo);
        if (null == refundNo) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "单号不能为空!", null);
            logger.info("retransmissionWithdrawRefundToEBS OUT,EBS重传提现收款失败！出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }

        try {
            separateOrderService.sendWithdrawRefundInf(refundNo);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "重传成功!", null);
            logger.info("retransmissionWithdrawRefundToEBS OUT,EBS重传提现收款失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "传输失败!发生未知异常!", null);
            logger.info("retransmissionWithdrawRefundToEBS OUT,EBS重传提现收款失败，出参 resultDTO:{}", resultDTO);
            logger.debug("Exception:{}", e);
            return resultDTO;
        }
    }

    /**
     * EBS重传订单收款信息
     *
     * @param receiptNumber
     * @return
     */
    @RequestMapping(value = "/EBS/order/store/receipt/{receiptNumber}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResultDTO retransmissionOrderStoreReceiptToEBS(@PathVariable String receiptNumber) {
        ResultDTO<Object> resultDTO;
        logger.info("retransmissionOrderStoreReceiptToEBS CALLED,EBS重传订单收款信息，入参 receiptNumber:{}", receiptNumber);
        if (null == receiptNumber) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "单号不能为空!", null);
            logger.info("retransmissionOrderStoreReceiptToEBS OUT,EBS重传订单收款信息失败！出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }

        try {
            separateOrderService.sendOrderReceiptInfByReceiptNumber(receiptNumber);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "重传成功!", null);
            logger.info("retransmissionOrderStoreReceiptToEBS OUT,EBS重传订单收款信息失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "传输失败!发生未知异常!", null);
            logger.info("retransmissionOrderStoreReceiptToEBS OUT,EBS重传订单收款信息失败，出参 resultDTO:{}", resultDTO);
            logger.debug("Exception:{}", e);
            return resultDTO;
        }
    }

    /**
     * EBS重传信用金充值收款信息
     *
     * @param receiptNumber
     * @return
     */
    @RequestMapping(value = "/EBS/order/store/recharge/{receiptNumber}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResultDTO retransmissionCreditRechargeReceiptToEBS(@PathVariable String receiptNumber) {
        ResultDTO<Object> resultDTO;
        logger.info("retransmissionCreditRechargeReceiptToEBS CALLED,EBS重传充值收款信息，入参 receiptNumber:{}", receiptNumber);
        if (null == receiptNumber) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "单号不能为空!", null);
            logger.info("retransmissionCreditRechargeReceiptToEBS OUT,EBS重传充值收款信息失败！出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }

        try {
            separateOrderService.sendCreditRechargeReceiptInf(receiptNumber);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "重传成功!", null);
            logger.info("retransmissionCreditRechargeReceiptToEBS OUT,EBS重传充值收款信息失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "传输失败!发生未知异常!", null);
            logger.info("retransmissionCreditRechargeReceiptToEBS OUT,EBS重传充值收款信息失败，出参 resultDTO:{}", resultDTO);
            logger.debug("Exception:{}", e);
            return resultDTO;
        }
    }

    /**
     * EBS生成订单头和商品数据
     *
     * @param orderNumber
     * @return
     */
    @RequestMapping(value = "/EBS/order/generate/goods/{orderNumber}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResultDTO retransmissionOrderGenerateGoodsToEBS(@PathVariable String orderNumber) {
        ResultDTO<Object> resultDTO;
        logger.info("retransmissionOrderGenerateGoodsToEBS CALLED,EBS生成订单头和商品数据，入参 orderNumber:{}", orderNumber);
        if (null == orderNumber) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "单号不能为空!", null);
            logger.info("retransmissionOrderGenerateGoodsToEBS OUT,EBS生成订单头和商品数据失败！出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }

        try {
            separateOrderService.separateOrderAndGoodsInf(orderNumber);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "重传成功!", null);
            logger.info("retransmissionOrderGenerateGoodsToEBS OUT,EBS生成订单头和商品数据失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "传输失败!发生未知异常!", null);
            logger.info("retransmissionOrderGenerateGoodsToEBS OUT,EBS生成订单头和商品数据失败，出参 resultDTO:{}", resultDTO);
            logger.debug("Exception:{}", e);
            return resultDTO;
        }
    }

    /**
     * EBS生成订单券信息
     *
     * @param orderNumber
     * @return
     */
    @RequestMapping(value = "/EBS/order/generate/coupon/{orderNumber}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResultDTO retransmissionOrderGenerateCouponToEBS(@PathVariable String orderNumber) {
        ResultDTO<Object> resultDTO;
        logger.info("retransmissionOrderGenerateCouponToEBS CALLED,EBS生成订单券信息，入参 orderNumber:{}", orderNumber);
        if (null == orderNumber) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "单号不能为空!", null);
            logger.info("retransmissionOrderGenerateCouponToEBS OUT,EBS生成订单券信息失败！出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }

        try {

            separateOrderService.separateOrderCouponInf(orderNumber);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "重传成功!", null);
            logger.info("retransmissionOrderGenerateCouponToEBS OUT,EBS生成订单券信息失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "传输失败!发生未知异常!", null);
            logger.info("retransmissionOrderGenerateCouponToEBS OUT,EBS生成订单券信息失败，出参 resultDTO:{}", resultDTO);
            logger.debug("Exception:{}", e);
            return resultDTO;
        }
    }

    /**
     * EBS生成订单收款信息
     *
     * @param orderNumber
     * @return
     */
    @RequestMapping(value = "/EBS/order/generate/receipt/{orderNumber}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResultDTO retransmissionOrderGenerateReceiptToEBS(@PathVariable String orderNumber) {
        ResultDTO<Object> resultDTO;
        logger.info("retransmissionOrderGenerateReceiptToEBS CALLED,EBS生成订单收款信息，入参 orderNumber:{}", orderNumber);
        if (null == orderNumber) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "单号不能为空!", null);
            logger.info("retransmissionOrderGenerateReceiptToEBS OUT,EBS生成订单收款信息失败！出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }

        try {
            separateOrderService.separateOrderReceiptInf(orderNumber);

            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "重传成功!", null);
            logger.info("retransmissionOrderGenerateReceiptToEBS OUT,EBS生成订单收款信息失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "传输失败!发生未知异常!", null);
            logger.info("retransmissionOrderGenerateReceiptToEBS OUT,EBS生成订单收款信息失败，出参 resultDTO:{}", resultDTO);
            logger.debug("Exception:{}", e);
            return resultDTO;
        }
    }

    /**
     * EBS生成订单经销差价信息
     *
     * @param orderNumber
     * @return
     */
    @RequestMapping(value = "/EBS/order/generate/jxPrice/{orderNumber}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResultDTO retransmissionOrderGenerateJxPriceToEBS(@PathVariable String orderNumber) {
        ResultDTO<Object> resultDTO;
        logger.info("retransmissionOrderGenerateJxPriceToEBS CALLED,EBS生成订单经销差价信息，入参 orderNumber:{}", orderNumber);
        if (null == orderNumber) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "单号不能为空!", null);
            logger.info("retransmissionOrderGenerateJxPriceToEBS OUT,EBS生成订单经销差价信息失败！出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }

        try {

            separateOrderService.separateOrderJxPriceInf(orderNumber);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "重传成功!", null);
            logger.info("retransmissionOrderGenerateJxPriceToEBS OUT,EBS生成订单经销差价信息失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "传输失败!发生未知异常!", null);
            logger.info("retransmissionOrderGenerateJxPriceToEBS OUT,EBS生成订单经销差价信息失败，出参 resultDTO:{}", resultDTO);
            logger.debug("Exception:{}", e);
            return resultDTO;
        }
    }

    /**
     * EBS生成订单运费信息
     *
     * @param orderNumber
     * @return
     */
    @RequestMapping(value = "/EBS/order/generate/keyInf/{orderNumber}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResultDTO retransmissionOrderGenerateOrderKeyInfToEBS(@PathVariable String orderNumber) {
        ResultDTO<Object> resultDTO;
        logger.info("retransmissionOrderGenerateOrderKeyInfToEBS CALLED,EBS生成订单运费信息，入参 orderNumber:{}", orderNumber);
        if (null == orderNumber) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "单号不能为空!", null);
            logger.info("retransmissionOrderGenerateOrderKeyInfToEBS OUT,EBS生成订单运费信息失败！出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }

        try {

            separateOrderService.separateOrderKeyInfInf(orderNumber);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "重传成功!", null);
            logger.info("retransmissionOrderGenerateOrderKeyInfToEBS OUT,EBS生成订单运费信息失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "传输失败!发生未知异常!", null);
            logger.info("retransmissionOrderGenerateOrderKeyInfToEBS OUT,EBS生成订单运费信息失败，出参 resultDTO:{}", resultDTO);
            logger.debug("Exception:{}", e);
            return resultDTO;
        }
    }


}

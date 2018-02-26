package cn.com.leyizhuang.app.web.controller;

import cn.com.leyizhuang.app.core.constant.WmsInterfaceMethodType;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.wms.AtwCancelOrderRequest;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.wms.AtwCancelReturnOrderRequest;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.wms.AtwRequisitionOrderGoods;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.wms.AtwReturnOrderCheckEnter;
import cn.com.leyizhuang.app.foundation.service.AppToWmsOrderService;
import cn.com.leyizhuang.app.remote.webservice.ICallWms;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import cn.com.leyizhuang.common.util.AssertUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Jerry.Ren
 * create 2018-02-26 11:45
 * desc: 重传WMS或者EBS的控制器
 **/
@RestController
@RequestMapping(value = ResendWmsOrEbsController.PRE_URL)
public class ResendWmsOrEbsController {

    protected final static String PRE_URL = "/app/resend";

    private static final Logger logger = LoggerFactory.getLogger(ResendWmsOrEbsController.class);

    @Resource
    private ICallWms iCallWms;

    @Resource
    private AppToWmsOrderService appToWmsOrderService;

    /**
     * 重传订单到wms
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
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "传输方式不能为空!", null);
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
            } else if (WmsInterfaceMethodType.RE_ORDER.getValue().equals(method)) {
                List<AtwRequisitionOrderGoods> requisitionOrderGoodsList = appToWmsOrderService.findAtwRequisitionOrderGoodsByOrderNo(orderNumber);
                if (AssertUtil.isEmpty(requisitionOrderGoodsList)) {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "传wms配送单的接口表无商品信息!请检查是否之前报错或者联系管理员.", null);
                    logger.info("retransmissionToWms OUT,重传订单到wms，出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }
                iCallWms.sendToWmsReturnOrderAndGoods(orderNumber);
            } else if (WmsInterfaceMethodType.C_ORDER.getValue().equals(method)) {
                AtwCancelOrderRequest atwCancelOrderRequest = appToWmsOrderService.findAtwCancelOrderByOrderNo(orderNumber);
                if (atwCancelOrderRequest.getSendFlag()) {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "已发送取消订单到wms,请勿重复发送!", null);
                    logger.info("retransmissionToWms OUT,重传订单到wms，出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }
                iCallWms.sendToWmsCancelOrder(orderNumber);
            } else if (WmsInterfaceMethodType.C_RE_ORDER.getValue().equals(method)) {
                AtwCancelReturnOrderRequest cancelReturnOrderRequest = appToWmsOrderService.findAtwCancelReturnOrderByReturnNo(orderNumber);
                if (cancelReturnOrderRequest.getSendFlag()) {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "已发送取消退货单到wms,请勿重复发送!", null);
                    logger.info("retransmissionToWms OUT,重传订单到wms，出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }
                iCallWms.sendToWmsCancelReturnOrder(orderNumber);
            } else if (WmsInterfaceMethodType.RE_ORDER_ENTER.getValue().equals(method)) {
                AtwReturnOrderCheckEnter atwReturnOrderCheckEnter = appToWmsOrderService.findAtwReturnOrderCheckEnterByReturnNo(orderNumber);
                if (atwReturnOrderCheckEnter.getSendFlag()) {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "已发送退货单确认到wms,请勿重复发送!", null);
                    logger.info("retransmissionToWms OUT,重传订单到wms，出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }
                iCallWms.sendToWmsReturnOrderCheck(orderNumber);
            }
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "重传成功!", null);
            logger.info("retransmissionToWms OUT,重传订单到wms，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "重传失败!发生未知异常!", null);
            logger.info("retransmissionToWms OUT,重传订单到wms，出参 resultDTO:{}", resultDTO);
            logger.debug(e.getMessage());
            return resultDTO;
        }
    }
}

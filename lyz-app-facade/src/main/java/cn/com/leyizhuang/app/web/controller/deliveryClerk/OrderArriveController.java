package cn.com.leyizhuang.app.web.controller.deliveryClerk;

import cn.com.leyizhuang.app.core.constant.AppOrderStatus;
import cn.com.leyizhuang.app.core.constant.LogisticStatus;
import cn.com.leyizhuang.app.core.constant.OrderBillingPaymentType;
import cn.com.leyizhuang.app.core.utils.oss.FileUploadOSSUtils;
import cn.com.leyizhuang.app.foundation.pojo.OrderDeliveryInfoDetails;
import cn.com.leyizhuang.app.foundation.pojo.order.*;
import cn.com.leyizhuang.app.foundation.pojo.response.ArrearsAuditResponse;
import cn.com.leyizhuang.app.foundation.service.*;
import cn.com.leyizhuang.common.core.constant.ArrearsAuditStatus;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/11/23
 */
@RestController
@RequestMapping(value = "/app/orderArrive")
public class OrderArriveController {

    private static final Logger logger = LoggerFactory.getLogger(OrderArriveController.class);

    @Autowired
    private AppOrderService appOrderServiceImpl;

    @Autowired
    private ArrearsAuditService arrearsAuditServiceImpl;

    @Autowired
    private OrderAgencyFundService orderAgencyFundServiceImpl;

    @Autowired
    private OrderDeliveryInfoDetailsService orderDeliveryInfoDetailsServiceImpl;

    /**
     * @title   配送员确认订单送达
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2017/11/23
     */
    @PostMapping(value = "/confirm", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> confirmOrderArrive(Long userId, Integer identityType, String orderNo, String pickUpCode, Double collectionAmount, String paymentMethod, String remarks, @RequestParam(value = "files", required = false) MultipartFile[] files) {
        logger.info("confirmOrderArrive CALLED,配送员确认订单送达，入参 userId:{} identityType:{} " +
                        "orderNo:{} pickUpCode:{} collectionAmount:{} paymentMethod:{}, remarks:{} files:{}",
                userId, identityType, orderNo, pickUpCode, collectionAmount, paymentMethod, remarks, files);
        ResultDTO<Object> resultDTO;
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "userId不能为空！", null);
            logger.info("confirmOrderArrive OUT,配送员确认订单送达失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType && identityType != 1) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型错误！", null);
            logger.info("confirmOrderArrive OUT,配送员确认订单送达失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == orderNo) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "订单号不能为空！", null);
            logger.info("confirmOrderArrive OUT,配送员确认订单送达失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == pickUpCode) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "取货码不能为空！", null);
            logger.info("confirmOrderArrive OUT,配送员确认订单送达失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try{
            //根据订单号查询订单信息
            OrderTempInfo orderTempInfo = this.appOrderServiceImpl.getOrderInfoByOrderNo(orderNo);
            if (null == orderTempInfo || !(orderTempInfo.getOrderStatus().equals(AppOrderStatus.PENDING_RECEIVE.getValue()))){
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "订单信息错误！", null);
                logger.info("confirmOrderArrive OUT,配送员确认订单送达失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            //查询是否有欠款待审核
            List<ArrearsAuditStatus> arrearsAuditStatusList = new ArrayList<ArrearsAuditStatus>();
            arrearsAuditStatusList.add(ArrearsAuditStatus.AUDITING);
            List<ArrearsAuditResponse> arrearsAuditResponseList = this.arrearsAuditServiceImpl.findByUserIdAndOrderNoAndStatus(userId, orderNo, arrearsAuditStatusList);
            if (null != arrearsAuditResponseList && arrearsAuditResponseList.size() > 0){
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "你有欠款审核还在审核中，请不要重复提交！", null);
                logger.info("confirmOrderArrive OUT,配送员确认订单送达失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }

            //验证取货码是否正确
            String pickCode = orderTempInfo.getPickUpCode();
            if (null == pickCode || !(pickCode.equalsIgnoreCase(pickUpCode))){
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "取货码输入错误！", null);
                logger.info("confirmOrderArrive OUT,配送员确认订单送达失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }

            collectionAmount = null == collectionAmount ? 0D : collectionAmount;
            Double collectionAmountOrder = null == orderTempInfo.getCollectionAmount() ? 0D : orderTempInfo.getCollectionAmount();
            Double ownManey = null == orderTempInfo.getOwnMoney() ? 0D : orderTempInfo.getOwnMoney();
            //判断配送员代收金额是否大于导购输入的代收金额
            if (collectionAmount > collectionAmountOrder){
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "收款金额不能大于代收金额！", null);
                logger.info("confirmOrderArrive OUT,配送员确认订单送达失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (collectionAmount < 0){
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "收款金额不能小于0！", null);
                logger.info("confirmOrderArrive OUT,配送员确认订单送达失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            //上传图片
            String picture = "";
            for (int i = 0; i < files.length; i++) {
                picture += FileUploadOSSUtils.uploadProfilePhoto(files[i], "logistics/photo");
                picture += ",";
            }
            //判断订单是否有欠款
            if (ownManey > 0){
                if (ownManey > collectionAmount){//欠款金额 > 收款金额
                    //创建欠款审核
                    OrderArrearsAuditDO orderArrearsAuditDO = new OrderArrearsAuditDO();
                    orderArrearsAuditDO.setOrderInfo(userId, orderNo, collectionAmountOrder, ownManey);
                    orderArrearsAuditDO.setCustomerAndSeller(orderTempInfo.getCustomerName(), orderTempInfo.getCustomerPhone(),orderTempInfo.getSellerId(),
                            orderTempInfo.getSellerName(), orderTempInfo.getSellerPhone());
                    orderArrearsAuditDO.setDistributionInfo(orderTempInfo.getShippingAddress(), LocalDateTime.now());
                    orderArrearsAuditDO.setArrearsAuditInfo(paymentMethod, collectionAmount, remarks, ArrearsAuditStatus.AUDITING);
                    orderArrearsAuditDO.setPicture(picture);
                    this.arrearsAuditServiceImpl.save(orderArrearsAuditDO);

                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "欠款审核提交成功,正在审核中!", null);
                    logger.info("confirmOrderArrive OUT,配送员确认订单送达申请欠款审核，出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                } else { //欠款金额 <= 收款金额
                    if (ownManey > 0) {
                        //创建收款记录
                        OrderBillingPaymentDetails paymentDetails = new OrderBillingPaymentDetails(null, Calendar.getInstance().getTime(),orderTempInfo.getOrderId(),
                                Calendar.getInstance().getTime(), OrderBillingPaymentType.getOrderBillingPaymentTypeByValue(paymentMethod), orderNo, ownManey, "","");

                        this.appOrderServiceImpl.savePaymentDetails(paymentDetails);

                        //修改订单欠款为0
                        OrderBillingDetails orderBillingDetails = new OrderBillingDetails();
                        orderBillingDetails.setOrderNumber(orderNo);
                        orderBillingDetails.setArrearage(0D);
                        this.appOrderServiceImpl.updateOwnMoneyByOrderNo(orderBillingDetails);
                    }
                }
            }

            //判断是否有代收款
            if (collectionAmount > 0){
                //生成代收款记录
                OrderAgencyFundDO orderAgencyFundDO = new OrderAgencyFundDO();
                orderAgencyFundDO.setOrderInfo(userId, orderNo, collectionAmountOrder);
                orderAgencyFundDO.setCustomerAndSeller(orderTempInfo.getCustomerName(), orderTempInfo.getCustomerPhone(),
                        orderTempInfo.getSellerId(), orderTempInfo.getSellerName(), orderTempInfo.getSellerPhone());
                orderAgencyFundDO.setAgencyFundInfo(paymentMethod, collectionAmount, collectionAmount - ownManey, remarks);
                this.orderAgencyFundServiceImpl.save(orderAgencyFundDO);
            }

            //生成订单物流详情
           /* AppEmployee appEmployee = this.appEmployeeServiceImpl.findDeliveryClerkNoByUserId(userId);
            String deliveryClerkNo = "";
            if (null != appEmployee && null != appEmployee.getDeliveryClerkNo()){
                deliveryClerkNo = appEmployee.getDeliveryClerkNo();
            }*/
            OrderDeliveryInfoDetails orderDeliveryInfoDetails = new OrderDeliveryInfoDetails();
            orderDeliveryInfoDetails.setDeliveryInfo(orderNo,LogisticStatus.CONFIRM_ARRIVAL, "确认到货！","送达",orderTempInfo.getOperatorNo(),picture,"","");
            this.orderDeliveryInfoDetailsServiceImpl.addOrderDeliveryInfoDetails(orderDeliveryInfoDetails);

            //修改订单状态
            OrderBaseInfo orderBaseInfo = new OrderBaseInfo();
            orderBaseInfo.setOrderNumber(orderNo);
            orderBaseInfo.setStatus(AppOrderStatus.FINISHED);
            orderBaseInfo.setDeliveryStatus(LogisticStatus.CONFIRM_ARRIVAL);
            this.appOrderServiceImpl.updateOrderStatusByOrderNo(orderBaseInfo);

            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
            logger.info("confirmOrderArrive OUT,配送员确认订单送达成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }catch (Exception e){
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，配送员确认订单送达失败", null);
            logger.warn("confirmOrderArrive EXCEPTION,配送员确认订单送达失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }


}
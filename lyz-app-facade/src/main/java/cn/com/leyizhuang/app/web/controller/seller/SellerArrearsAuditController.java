package cn.com.leyizhuang.app.web.controller.seller;

import cn.com.leyizhuang.app.core.constant.AppOrderStatus;
import cn.com.leyizhuang.app.core.constant.LogisticStatus;
import cn.com.leyizhuang.app.foundation.pojo.OrderDeliveryInfoDetails;
import cn.com.leyizhuang.app.foundation.pojo.order.*;
import cn.com.leyizhuang.app.foundation.pojo.response.ArrearageListResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.SellerArrearsAuditResponse;
import cn.com.leyizhuang.app.foundation.service.AppOrderService;
import cn.com.leyizhuang.app.foundation.service.ArrearsAuditService;
import cn.com.leyizhuang.app.foundation.service.OrderAgencyFundService;
import cn.com.leyizhuang.app.foundation.service.OrderDeliveryInfoDetailsService;
import cn.com.leyizhuang.common.core.constant.ArrearsAuditStatus;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import cn.com.leyizhuang.common.util.CountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/11/24
 */
@RestController
@RequestMapping(value = "/app/seller/arrearsAudit")
public class SellerArrearsAuditController {

    private static final Logger logger = LoggerFactory.getLogger(cn.com.leyizhuang.app.web.controller.deliveryClerk.ArrearsAuditController.class);

    @Autowired
    private ArrearsAuditService arrearsAuditServiceImpl;

    @Autowired
    private AppOrderService appOrderServiceImpl;

    @Autowired
    private OrderAgencyFundService orderAgencyFundServiceImpl;

    @Autowired
    private OrderDeliveryInfoDetailsService orderDeliveryInfoDetailsServiceImpl;


    /**
     * @title   导购获取待审核欠款申请列表
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2017/11/24
     */
    @PostMapping(value = "/auditing/list", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> getArrearsAuditListBySeller(Long userId, Integer identityType) {
        logger.info("getArrearsAuditListBySeller CALLED,导购获取待审核欠款申请列表，入参 userId:{} identityType:{}", userId, identityType);
        ResultDTO<Object> resultDTO;
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "userId不能为空！", null);
            logger.info("getArrearsAuditListBySeller OUT,导购获取待审核欠款申请列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType && identityType != 0) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型错误！", null);
            logger.info("getArrearsAuditListBySeller OUT,导购获取待审核欠款申请列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        List<ArrearsAuditStatus> arrearsAuditStatusList = new ArrayList<ArrearsAuditStatus>();
        arrearsAuditStatusList.add(ArrearsAuditStatus.AUDITING);
        List<SellerArrearsAuditResponse> arrearsAuditResponseList = this.arrearsAuditServiceImpl.findBySellerIdAndStatus(userId, arrearsAuditStatusList);
        resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, arrearsAuditResponseList);
        logger.info("getArrearsAuditListBySeller OUT,导购获取待审核欠款申请列表成功，出参 resultDTO:{}", resultDTO);
        return resultDTO;
    }

    /**
     * @title   导购获取已审核欠款申请列表
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2017/11/24
     */
    @PostMapping(value = "/audited/list", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> findArrearsAuditedListBySeller(Long userId, Integer identityType) {
        logger.info("findArrearsAuditedListBySeller CALLED,导购获取已审核欠款申请列表，入参 userId:{} identityType:{}", userId, identityType);
        ResultDTO<Object> resultDTO;
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "userId不能为空！", null);
            logger.info("findArrearsAuditedListBySeller OUT,导购获取已审核欠款申请列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType && identityType != 0) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型错误！", null);
            logger.info("findArrearsAuditedListBySeller OUT,导购获取已审核欠款申请列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        List<ArrearsAuditStatus> arrearsAuditStatusList = new ArrayList<ArrearsAuditStatus>();
        arrearsAuditStatusList.add(ArrearsAuditStatus.AUDIT_NO);
        arrearsAuditStatusList.add(ArrearsAuditStatus.AUDIT_PASSED);
        List<SellerArrearsAuditResponse> arrearsAuditResponseList = this.arrearsAuditServiceImpl.findBySellerIdAndStatus(userId, arrearsAuditStatusList);
        resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, arrearsAuditResponseList);
        logger.info("findArrearsAuditedListBySeller OUT,导购获取已审核欠款申请列表成功，出参 resultDTO:{}", resultDTO);
        return resultDTO;
    }

    /**  
     * @title   导购审批欠款申请
     * @descripe
     * @param 
     * @return 
     * @throws 
     * @author GenerationRoad
     * @date 2017/11/27
     */
    @PostMapping(value = "/audit", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> audit(Long userId, Integer identityType, Long arrearsAuditId, Boolean result) {
        logger.info("audit CALLED,导购审批欠款申请，入参 userId:{} identityType:{} arrearsAuditId:{} result:{}", userId, identityType, arrearsAuditId, result);
        ResultDTO<Object> resultDTO;
        try {
            if (null == userId) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "userId不能为空！", null);
                logger.info("audit OUT,导购审批欠款申请失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == identityType && identityType != 0) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型错误！",
                        null);
                logger.info("audit OUT,导购审批欠款申请失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }

            if (null == arrearsAuditId) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "审核单id不能为空！",
                        null);
                logger.info("audit OUT,导购审批欠款申请失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == result) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "审核结果不能为空！",
                        null);
                logger.info("audit OUT,导购审批欠款申请失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            OrderArrearsAuditDO orderArrearsAuditDO = this.arrearsAuditServiceImpl.findById(arrearsAuditId);
            if (null == orderArrearsAuditDO || !(orderArrearsAuditDO.getStatus().equals(ArrearsAuditStatus.AUDITING))){
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "欠款审核单信息错误！",
                        null);
                logger.info("audit OUT,导购审批欠款申请失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            String orderNo = orderArrearsAuditDO.getOrderNumber();
            OrderTempInfo orderTempInfo = this.appOrderServiceImpl.getOrderInfoByOrderNo(orderNo);
            if (null == orderTempInfo){
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "订单信息错误！",
                        null);
                logger.info("audit OUT,导购审批欠款申请失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (result) {

                Double collectionAmount = null == orderArrearsAuditDO.getRealMoney() ? 0D : orderArrearsAuditDO.getRealMoney();
                Double collectionAmountOrder = null == orderTempInfo.getCollectionAmount() ? 0D : orderTempInfo.getCollectionAmount();
                //生成代收款记录
                OrderAgencyFundDO orderAgencyFundDO = new OrderAgencyFundDO();
                orderAgencyFundDO.setOrderInfo(orderArrearsAuditDO.getUserId(), orderNo, collectionAmountOrder);
                orderAgencyFundDO.setCustomerAndSeller(orderTempInfo.getCustomerName(), orderTempInfo.getCustomerPhone(),
                        orderTempInfo.getSellerId(), orderTempInfo.getSellerName(), orderTempInfo.getSellerPhone());
                orderAgencyFundDO.setAgencyFundInfo(orderArrearsAuditDO.getPaymentMethod(), collectionAmount, 0D, orderArrearsAuditDO.getRemarks());
                this.orderAgencyFundServiceImpl.save(orderAgencyFundDO);

                //创建收款记录
                OrderBillingPaymentDetails paymentDetails = new OrderBillingPaymentDetails();
                paymentDetails.setConstructor(orderTempInfo.getOrderId(), "实际货币", orderArrearsAuditDO.getPaymentMethod(), orderNo, collectionAmount, "");
                this.appOrderServiceImpl.savePaymentDetails(paymentDetails);

                //修改订单欠款
                OrderBillingDetails orderBillingDetails = new OrderBillingDetails();
                orderBillingDetails.setOrderNumber(orderNo);
                orderBillingDetails.setArrearage(CountUtil.sub(orderTempInfo.getOwnMoney(), collectionAmount));
                this.appOrderServiceImpl.updateOwnMoneyByOrderNo(orderBillingDetails);

                //生成订单物流详情
                OrderDeliveryInfoDetails orderDeliveryInfoDetails = new OrderDeliveryInfoDetails();
                orderDeliveryInfoDetails.setDeliveryInfo(orderNo, LogisticStatus.CONFIRM_ARRIVAL, "确认到货！","送达",orderTempInfo.getOperatorNo(),orderArrearsAuditDO.getPicture(),"","");
                this.orderDeliveryInfoDetailsServiceImpl.addOrderDeliveryInfoDetails(orderDeliveryInfoDetails);

                //修改订单状态
                OrderBaseInfo orderBaseInfo = new OrderBaseInfo();
                orderBaseInfo.setOrderNumber(orderNo);
                orderBaseInfo.setStatus(AppOrderStatus.FINISHED);
                orderBaseInfo.setDeliveryStatus(LogisticStatus.CONFIRM_ARRIVAL);
                this.appOrderServiceImpl.updateOrderStatusByOrderNo(orderBaseInfo);

                orderArrearsAuditDO.setStatus(ArrearsAuditStatus.AUDIT_PASSED);
                orderArrearsAuditDO.setUpdateTime(LocalDateTime.now());
                this.arrearsAuditServiceImpl.updateStatusById(orderArrearsAuditDO);
            }else {
                orderArrearsAuditDO.setStatus(ArrearsAuditStatus.AUDIT_NO);
                orderArrearsAuditDO.setUpdateTime(LocalDateTime.now());
                this.arrearsAuditServiceImpl.updateStatusById(orderArrearsAuditDO);
            }

            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
            logger.info("audit OUT,导购审批欠款申请成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知异常,导购审批欠款申请失败!", null);
            logger.warn("audit EXCEPTION,导购审批欠款申请失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    /**
     * 导购欠款查询
     * @param userID    用户id
     * @param identityType  用户类型
     * @return  欠款列表
     */
    @PostMapping(value = "/list", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> findArrearsListByUserId(Long userID,Integer identityType){
        logger.info("findArrearsListByUserId CALLED,导购欠款查询，入参 userId:{} identityType:{}", userID, identityType);
        ResultDTO<Object> resultDTO;

            if (null == userID) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "userId不能为空！", null);
                logger.info("findArrearsListByUserId OUT,导购欠款查询失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == identityType) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空！",
                        null);
                logger.info("findArrearsListByUserId OUT,导购欠款查询失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (identityType != 0){
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型错误！",
                        null);
                logger.info("findArrearsListByUserId OUT,导购欠款查询失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            try {
                List<ArrearageListResponse> arrearageListResponseList = arrearsAuditServiceImpl.findArrearsListByUserId(userID);
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, arrearageListResponseList);
                logger.info("findArrearsListByUserId OUT,导购欠款查询成功，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }catch (Exception e){
                e.printStackTrace();
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知异常,导购欠款查询失败!", null);
                logger.warn("findArrearsListByUserId EXCEPTION,导购欠款查询失败，出参 resultDTO:{}", resultDTO);
                logger.warn("{}", e);
                return resultDTO;
            }

    }
}

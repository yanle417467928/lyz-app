package cn.com.leyizhuang.app.web.controller;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.core.constant.LogisticStatus;
import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.pojo.OrderDeliveryInfoDetails;
import cn.com.leyizhuang.app.foundation.pojo.WareHouseDO;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderBaseInfo;
import cn.com.leyizhuang.app.foundation.pojo.response.LogisticsDetailResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.LogisticsInformationResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.LogisticsMessageResponse;
import cn.com.leyizhuang.app.foundation.pojo.user.AppEmployee;
import cn.com.leyizhuang.app.foundation.service.AppEmployeeService;
import cn.com.leyizhuang.app.foundation.service.AppOrderService;
import cn.com.leyizhuang.app.foundation.service.OrderDeliveryInfoDetailsService;
import cn.com.leyizhuang.app.foundation.service.WareHouseService;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 物流详情
 * Created by caiyu on 2017/11/20.
 */
@RestController
@RequestMapping(value = "/app/delivery")
public class OrderDeliveryInfoDetailsController {
    private static final Logger logger = LoggerFactory.getLogger(OrderDeliveryInfoDetailsController.class);

    @Resource
    private OrderDeliveryInfoDetailsService orderDeliveryInfoDetailsService;

    @Resource
    private AppEmployeeService appEmployeeService;

    @Resource
    private AppOrderService appOrderService;

    @Resource
    private WareHouseService wareHouseService;

    /**
     * 查看物流详情
     *
     * @param orderNumber 订单号
     * @return 订单详情
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> getOrderDelivery(String orderNumber, Long userId, Integer identityType) {
        ResultDTO<Object> resultDTO;
        logger.info("getOrderDelicery CALLED,获取物流详情，入参 orderNumber:{}", orderNumber);
        if (StringUtils.isBlank(orderNumber)) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "订单号不能为空", null);
            logger.info("getOrderDelicery OUT,获取物流详情失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //查询订单基础信息
            OrderBaseInfo orderBaseInfo = appOrderService.getOrderByOrderNumber(orderNumber);
            if (AppIdentityType.CUSTOMER.equals(AppIdentityType.getAppIdentityTypeByValue(identityType))) {
                if (!orderBaseInfo.getCustomerId().equals(userId)) {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "只能查看自己订单物流详情", null);
                    logger.info("getOrderDelicery OUT,获取物流详情失败，出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }
            } else if (AppIdentityType.SELLER.equals(AppIdentityType.getAppIdentityTypeByValue(identityType))) {
                if (!orderBaseInfo.getSalesConsultId().equals(userId)) {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "只能查看自己订单物流详情", null);
                    logger.info("getOrderDelicery OUT,获取物流详情失败，出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }
            }
            //查询该订单所有物流状态
            List<OrderDeliveryInfoDetails> orderDeliveryInfoDetailsList = orderDeliveryInfoDetailsService.queryListByOrderNumber(orderNumber);
            //配送员编号
            String deliveryNumber = null;
            if (null != orderDeliveryInfoDetailsList && orderDeliveryInfoDetailsList.size() > 0) {
                deliveryNumber = orderDeliveryInfoDetailsList.get(0).getOperatorNo();
            } else {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "没有物流记录", null);
                logger.info("getOrderDelicery OUT,获取物流详情失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            //创建物流详情list
            List<LogisticsDetailResponse> logisticsDetailResponseList = new ArrayList<>();
            for (OrderDeliveryInfoDetails orderDeliveryInfoDetails : orderDeliveryInfoDetailsList) {
                LogisticsDetailResponse logisticsDetailResponse = new LogisticsDetailResponse();
                logisticsDetailResponse.setCreateTime(sdf.format(orderDeliveryInfoDetails.getCreateTime()));
                logisticsDetailResponse.setDescribe(orderDeliveryInfoDetails.getDescription());
                logisticsDetailResponse.setLogisticsType(orderDeliveryInfoDetails.getLogisticStatus().getDescription());

                logisticsDetailResponseList.add(logisticsDetailResponse);
            }
            LogisticsInformationResponse logisticsInformationResponse1;
            logisticsInformationResponse1 = orderDeliveryInfoDetailsService.getDeliveryByOperatorNoAndOrderNumber(deliveryNumber, orderNumber);
            if (null != logisticsInformationResponse1) {
                //数据库查询来的WareHouseName是仓库编码,这里再查一次仓库名称为页面显示
                WareHouseDO wareHouseDO = wareHouseService.findByWareHouseNo(logisticsInformationResponse1.getWarehouseName());
                if (wareHouseDO != null) {
                    logisticsInformationResponse1.setWarehouseName(wareHouseDO.getWareHouseName());
                }
                logisticsInformationResponse1.setLogisticsDetail(logisticsDetailResponseList);
            } else {
                logisticsInformationResponse1 = new LogisticsInformationResponse();
                logisticsInformationResponse1.setOrderNumber(orderNumber);
                logisticsInformationResponse1.setLogisticsDetail(logisticsDetailResponseList);
            }

            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, logisticsInformationResponse1);
            logger.info("getOrderDelicery OUT,获取物流详情成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，获取物流详情失败", null);
            logger.warn("getOrderDelicery EXCEPTION,获取物流详情失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    /**
     * 用户获取物流推送信息
     *
     * @param userID       用户id
     * @param identityType 用户类型
     * @return 返回信息列表
     */
    @RequestMapping(value = "/message", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> getOrderLogisticsMessage(Long userID, Integer identityType) {
        ResultDTO<Object> resultDTO;
        logger.info("getOrderLogisticsMessage CALLED,用户获取物流推送信息，入参 userID:{}, identityType:{}", userID, identityType);

        if (null == userID) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空", null);
            logger.info("getOrderLogisticsMessage OUT,用户获取物流推送信息失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空", null);
            logger.info("getOrderLogisticsMessage OUT,用户获取物流推送信息失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }

        try {
            //定义时间格式
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar c = Calendar.getInstance();
            //计算7天前时间
            c.add(Calendar.DATE, -7);
            //获取7天前的时间
            Date monday = c.getTime();
            //查询推送的物流细信息
            List<OrderDeliveryInfoDetails> orderDeliveryInfoDetailsList = orderDeliveryInfoDetailsService.
                    getLogisticsMessageByUserId(userID, monday, identityType);
            //创建返回list
            List<LogisticsMessageResponse> logisticsMessageResponseList = new ArrayList<>();
            //未读消息排序在前
            orderDeliveryInfoDetailsList.sort(Comparator.comparing(OrderDeliveryInfoDetails::getIsRead).
                    thenComparing(OrderDeliveryInfoDetails::getCreateTime).reversed());
            for (OrderDeliveryInfoDetails orderDeliveryInfoDetails : orderDeliveryInfoDetailsList) {
                //修改已读状态
                orderDeliveryInfoDetails.setIsRead(true);
                orderDeliveryInfoDetailsService.modifyOrderDeliveryInfoDetails(orderDeliveryInfoDetails);
                //创建返回类
                LogisticsMessageResponse logisticsMessageResponse = new LogisticsMessageResponse();
                //设置
                logisticsMessageResponse.setCreateTime(sdf.format(orderDeliveryInfoDetails.getCreateTime()));
                logisticsMessageResponse.setOrderNumber(orderDeliveryInfoDetails.getOrderNo());
                if ("已封车".equals(orderDeliveryInfoDetails.getLogisticStatus().getDescription())) {
                    logisticsMessageResponse.setMessage("您的订单 " + orderDeliveryInfoDetails.getOrderNo() + "  商家已发货！");
                } else if ("已签收".equals(orderDeliveryInfoDetails.getLogisticStatus().getDescription())) {
                    logisticsMessageResponse.setMessage("您的订单 " + orderDeliveryInfoDetails.getOrderNo() + "  商家已确认到货！");
                }
                //讲返回类加入返回list中
                logisticsMessageResponseList.add(logisticsMessageResponse);
            }
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, logisticsMessageResponseList);
            logger.info("getOrderLogisticsMessage OUT,用户获取物流推送信息成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，用户获取物流推送信息失败", null);
            logger.warn("getOrderLogisticsMessage EXCEPTION,用户获取物流推送信息失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    /**
     * 配送员修改物流状态
     *
     * @param userID       用户id
     * @param identityType 用户类型
     * @param orderNumber  订单号
     * @param description  描述
     * @return 成功或失败
     */
    @RequestMapping(value = "/update/status", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> updateLogisticsStatus(Long userID, Integer identityType, String orderNumber, String description) {
        ResultDTO<Object> resultDTO;
        logger.info("updateLogisticsStatus CALLED,配送员修改物流状态，入参 userID:{}, identityType:{}, orderNumber:{}, description:{}", userID, identityType, orderNumber, description);

        if (null == userID) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空", null);
            logger.info("updateLogisticsStatus OUT,配送员修改物流状态失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空", null);
            logger.info("updateLogisticsStatus OUT,配送员修改物流状态失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (StringUtils.isBlank(orderNumber)) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "订单号不能为空", null);
            logger.info("updateLogisticsStatus OUT,配送员修改物流状态失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (StringUtils.isBlank(description)) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "状态描述不能为空", null);
            logger.info("updateLogisticsStatus OUT,配送员修改物流状态失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (identityType != 1) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "只有配送员能进行此操作", null);
            logger.info("updateLogisticsStatus OUT,配送员修改物流状态失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            AppEmployee appEmployee = appEmployeeService.findById(userID);
            OrderDeliveryInfoDetails orderDeliveryInfoDetails = orderDeliveryInfoDetailsService.queryByOrderNumberAndOperatorNumber(orderNumber, appEmployee.getDeliveryClerkNo());
            OrderDeliveryInfoDetails deliveryInfoDetails = new OrderDeliveryInfoDetails();
            deliveryInfoDetails.setCreateTime(new Date());
            deliveryInfoDetails.setLogisticStatus(LogisticStatus.SENDING);
            deliveryInfoDetails.setOrderNo(orderNumber);
            deliveryInfoDetails.setDescription(description);
            deliveryInfoDetails.setOperatorNo(orderDeliveryInfoDetails.getOperatorNo());
            deliveryInfoDetails.setTaskNo(orderDeliveryInfoDetails.getTaskNo());
            deliveryInfoDetails.setWarehouseNo(orderDeliveryInfoDetails.getWarehouseNo());
            deliveryInfoDetails.setPicture("");
            orderDeliveryInfoDetailsService.addOrderDeliveryInfoDetails(deliveryInfoDetails);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
            logger.info("getOrderLogisticsResponse OUT,配送员修改物流状态成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，配送员修改物流状态失败", null);
            logger.warn("updateLogisticsStatus EXCEPTION,配送员修改物流状态失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }
}

package cn.com.leyizhuang.app.web.controller;

import cn.com.leyizhuang.app.core.constant.LogisticStatus;
import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.pojo.OrderDeliveryInfoDetails;
import cn.com.leyizhuang.app.foundation.pojo.response.LogisticsDetailResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.LogisticsInformationResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.LogisticsMessageResponse;
import cn.com.leyizhuang.app.foundation.pojo.user.AppEmployee;
import cn.com.leyizhuang.app.foundation.service.AppEmployeeService;
import cn.com.leyizhuang.app.foundation.service.OrderDeliveryInfoDetailsService;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

    /**
     * 查看物流详情
     *
     * @param orderNumber 订单号
     * @return 订单详情
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> getOrderDelicery(String orderNumber) {
        ResultDTO<Object> resultDTO;
        logger.info("getOrderDelicery CALLED,获取物流详情，入参 orderNumber:{}", orderNumber);
        if (StringUtils.isBlank(orderNumber)) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "订单号不能为空", null);
            logger.info("getOrderDelicery OUT,获取物流详情失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //查询该订单所有物流状态
            List<OrderDeliveryInfoDetails> orderDeliveryInfoDetailsList = orderDeliveryInfoDetailsService.queryListByOrderNumber(orderNumber);
            //配送员编号
            String deliveryNumber = orderDeliveryInfoDetailsList.get(1).getOperatorNo();
            //创建物流详情list
            List<LogisticsDetailResponse> logisticsDetailResponseList = new ArrayList<>();
            for (OrderDeliveryInfoDetails orderDeliveryInfoDetails : orderDeliveryInfoDetailsList) {
                LogisticsDetailResponse logisticsDetailResponse = new LogisticsDetailResponse();
                logisticsDetailResponse.setCreateTime(sdf.format(orderDeliveryInfoDetails.getCreateTime()));
                logisticsDetailResponse.setDescribe(orderDeliveryInfoDetails.getDescription());
                logisticsDetailResponse.setLogisticsType(orderDeliveryInfoDetails.getLogisticStatus().getDescription());

                logisticsDetailResponseList.add(logisticsDetailResponse);
            }
            LogisticsInformationResponse logisticsInformationResponse1 = orderDeliveryInfoDetailsService.getDeliveryByOperatorNoAndOrderNumber(deliveryNumber, orderNumber);
            logisticsInformationResponse1.setLogisticsDetail(logisticsDetailResponseList);

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
        logger.info("getOrderLogisticsResponse CALLED,用户获取物流推送信息，入参 userID:{}, identityType:{}", userID, identityType);

        if (null == userID) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空", null);
            logger.info("getOrderLogisticsResponse OUT,用户获取物流推送信息失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空", null);
            logger.info("getOrderLogisticsResponse OUT,用户获取物流推送信息失败，出参 resultDTO:{}", resultDTO);
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
            List<OrderDeliveryInfoDetails> orderDeliveryInfoDetailsList = orderDeliveryInfoDetailsService.getLogisticsMessageByUserId(userID, monday, identityType);
            //创建返回list
            List<LogisticsMessageResponse> logisticsMessageResponseList = new ArrayList<>();
            for (OrderDeliveryInfoDetails orderDeliveryInfoDetails : orderDeliveryInfoDetailsList) {
                //创建返回类
                LogisticsMessageResponse logisticsMessageResponse = new LogisticsMessageResponse();
                //设置
                logisticsMessageResponse.setCreateTime(sdf.format(orderDeliveryInfoDetails.getCreateTime()));
                logisticsMessageResponse.setOrderNumber(orderDeliveryInfoDetails.getOrderNo());
                if ("已封车".equals(orderDeliveryInfoDetails.getLogisticStatus().getDescription())) {
                    logisticsMessageResponse.setMessage("您的订单 " + orderDeliveryInfoDetails.getOrderNo() + "  商家已发货！");
                } else if ("确认到货".equals(orderDeliveryInfoDetails.getLogisticStatus().getDescription())) {
                    logisticsMessageResponse.setMessage("您的订单 " + orderDeliveryInfoDetails.getOrderNo() + "  商家已确认到货！");
                }
                //讲返回类加入返回list中
                logisticsMessageResponseList.add(logisticsMessageResponse);
            }
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, logisticsMessageResponseList);
            logger.info("getOrderLogisticsResponse OUT,用户获取物流推送信息成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，用户获取物流推送信息失败", null);
            logger.warn("getOrderLogisticsResponse EXCEPTION,用户获取物流推送信息失败，出参 resultDTO:{}", resultDTO);
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
     * @return  成功或失败
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
        if (identityType != 1){
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "只有配送员能进行此操作", null);
            logger.info("updateLogisticsStatus OUT,配送员修改物流状态失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            AppEmployee appEmployee = appEmployeeService.findById(userID);
            OrderDeliveryInfoDetails orderDeliveryInfoDetails = orderDeliveryInfoDetailsService.queryByOrderNumberAndOperatorNumber(orderNumber,appEmployee.getDeliveryClerkNo());
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

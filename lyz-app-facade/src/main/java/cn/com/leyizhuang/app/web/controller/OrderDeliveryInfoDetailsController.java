package cn.com.leyizhuang.app.web.controller;

import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.pojo.OrderDeliveryInfoDetails;
import cn.com.leyizhuang.app.foundation.pojo.response.LogisticsDetailResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.LogisticsInformationResponse;
import cn.com.leyizhuang.app.foundation.pojo.user.AppEmployee;
import cn.com.leyizhuang.app.foundation.service.OrderDeliveryInfoDetailsService;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

    /**
     * 查看物流详情
     *
     * @param orderNumber 订单号
     * @return 订单详情
     */
    public ResultDTO<Object> getOrderDelicery(String orderNumber) {
        ResultDTO<Object> resultDTO;
        logger.info("getOrderDelicery CALLED,获取物流详情，入参 orderNumber:{}", orderNumber);
        if (StringUtils.isBlank(orderNumber)) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "订单号不能为空", null);
            logger.info("getOrderDelicery OUT,获取物流详情失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
                logisticsDetailResponse.setLogisticsType(orderDeliveryInfoDetails.getOperationType());

                logisticsDetailResponseList.add(logisticsDetailResponse);
            }
            LogisticsInformationResponse logisticsInformationResponse1 = orderDeliveryInfoDetailsService.getDeliveryBydeliveryClerkNo(deliveryNumber);
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
}

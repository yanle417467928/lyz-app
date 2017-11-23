package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.foundation.pojo.OrderDeliveryInfoDetails;
import cn.com.leyizhuang.app.foundation.pojo.response.LogisticsInformationResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.ShipperDetailResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.WaitDeliveryResponse;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by caiyu on 2017/11/20.
 */
@Repository
public interface OrderDeliveryInfoDetailsDAO {

    void addOrderDeliveryInfoDetails(OrderDeliveryInfoDetails orderDeliveryInfoDetails);

    List<OrderDeliveryInfoDetails> queryListByOrderNumber(@Param("orderNumber") String orderNumber);

    LogisticsInformationResponse getDeliveryByOperatorNoAndOrderNumber(@Param("operatorNo") String operatorNo,@Param("orderNumber") String orderNumber);

    //获取配送员待配送列表
    List<WaitDeliveryResponse> getOrderBeasInfoByOperatorNo(@Param("operatorNo") String operatorNo);
    //获取出货单详情
    ShipperDetailResponse getOrderDeliveryInfoDetailsByOperatorNoAndOrderNumber(@Param("operatorNo") String operatorNo, @Param("orderNumber") String orderNumber);
    //获取推送的物流消息
    List<OrderDeliveryInfoDetails> getLogisticsMessageByUserId(@Param("userID") Long userID, @Param("createTime") Date createTime, @Param("identityType")AppIdentityType identityType);
}

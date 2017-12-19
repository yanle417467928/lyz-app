package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.core.constant.LogisticStatus;
import cn.com.leyizhuang.app.foundation.pojo.OrderDeliveryInfoDetails;
import cn.com.leyizhuang.app.foundation.pojo.response.*;

import java.util.Date;
import java.util.List;

/**
 * Created by caiyu on 2017/11/20.
 */
public interface OrderDeliveryInfoDetailsService {
    void addOrderDeliveryInfoDetails(OrderDeliveryInfoDetails orderDeliveryInfoDetails);

    List<OrderDeliveryInfoDetails> queryListByOrderNumber(String orderNumber);

    LogisticsInformationResponse getDeliveryByOperatorNoAndOrderNumber(String operatorNo, String orderNumber);

    OrderDeliveryInfoDetails queryByOrderNumberAndOperatorNumber(String orderNumber, String operatorNumber);

    //获取配送员待配送列表
    List<WaitDeliveryResponse> getOrderBeasInfoByOperatorNo(String operatorNo);

    //获取出货单详情
    ShipperDetailResponse getOrderDeliveryInfoDetailsByOperatorNoAndOrderNumber(String operatorNo, String orderNumber);

    //获取推送的物流消息
    List<OrderDeliveryInfoDetails> getLogisticsMessageByUserId(Long userID, Date createTime, Integer identityType);

    /**
     * 配送员获取待取货列表
     *
     * @param operatorNo 配送员编码
     * @return 待取货列表
     */
    List<WaitPickUpResponse> getWaitPickUpListByOperatorNo(String operatorNo);

    /**
     * 配送员获取待取货详情
     *
     * @param operatorNo   配送员编码
     * @param returnNumber 退单号
     * @return 待取货详情
     */
    PickUpDetailResponse getPickUpDetailByOperatorNoAndReturnNo(String operatorNo, String returnNumber);

    /**
     * 获取退货单商品详情
     * @param returnNumber  退单号
     * @return  商品详情
     */
    List<GiftListResponseGoods> getReturnGoods(String returnNumber);


    /**
     * 查找订单物流明细
     * @param orderNumber  订单号
     * @param logisticStatus    物流状态
     * @return  物流明细
     */
    OrderDeliveryInfoDetails findByOrderNumberAndLogisticStatus(String orderNumber,LogisticStatus logisticStatus);

    /**
     * 配送员获取已完成单列表
     * @param userId    配送员编号
     * @return  已完成单列表
     */
    List<AuditFinishResponse> getAuditFinishOrderByOperatorNo(Long userId);
}

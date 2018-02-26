package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.core.constant.LogisticStatus;
import cn.com.leyizhuang.app.foundation.pojo.OrderDeliveryInfoDetails;
import cn.com.leyizhuang.app.foundation.pojo.response.*;
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

    LogisticsInformationResponse getDeliveryByOperatorNoAndOrderNumber(@Param("operatorNo") String operatorNo, @Param("orderNumber") String orderNumber);

    OrderDeliveryInfoDetails queryByOrderNumberAndOperatorNumber(@Param("orderNumber") String orderNumber,@Param("operatorNumber") String operatorNumber);

    //获取配送员待配送列表
    List<WaitDeliveryResponse> getOrderBeasInfoByOperatorNo(@Param("operatorNo") String operatorNo);

    /**
     * 配送员获取已完成单列表
     * @return  已完成单列表
     */
    List<AuditFinishResponse> getAuditFinishOrderByOperatorNo(@Param("operatorNo") String operatorNo);

    //获取出货单详情
    ShipperDetailResponse getOrderDeliveryInfoDetailsByOperatorNoAndOrderNumber(@Param("operatorNo") String operatorNo, @Param("orderNumber") String orderNumber);

    //获取推送的物流消息
    List<OrderDeliveryInfoDetails> getLogisticsMessageByUserId(@Param("userID") Long userID, @Param("createTime") Date createTime, @Param("identityType")AppIdentityType identityType);

    /**
     * 配送员获取待取货列表
     * @param operatorNo    配送员编码
     * @return  待取货列表
     */
    List<WaitPickUpResponse> getWaitPickUpListByOperatorNo(@Param("operatorNo")String operatorNo);

    /**
     * 配送员获取待取货详情
     * @param operatorNo    配送员编码
     * @param returnNumber  退单号
     * @return  待取货详情
     */
    PickUpDetailResponse getPickUpDetailByOperatorNoAndReturnNo(@Param("operatorNo")String operatorNo,@Param("returnNumber")String returnNumber);

    /**
     * 获取退货单商品详情
     * @param returnNumber  退单号
     * @return  商品详情
     */
    List<GiftListResponseGoods> getReturnGoods(@Param("returnNumber")String returnNumber);

    /**
     * 查找订单物流明细
     * @param orderNumber  订单号
     * @param logisticStatus    物流状态
     * @return  物流明细
     */
    OrderDeliveryInfoDetails findByOrderNumberAndLogisticStatus(@Param("orderNumber")String orderNumber,
                                                                @Param("logisticStatus")LogisticStatus logisticStatus);

    /**
     * 获取未读的物流信息条数
     *
     * @param userId       id
     * @param identityType 身份
     * @return 未读数
     */
    int countUnreadLogisticsMessage(@Param("userId") Long userId, @Param("identityType") AppIdentityType identityType);

    /**
     * 修改物流信息
     *
     * @param orderDeliveryInfoDetails 物流信息
     */
    void modifyOrderDeliveryInfoDetails(OrderDeliveryInfoDetails orderDeliveryInfoDetails);

    /**
     * 查询配送员已完成的数量
     *
     * @param operatorNo
     * @return
     */
    int countAuditFinishOrderByOperatorNo(String operatorNo);

    /**
     * 根据物流操作单号获取物流配送信息
     *
     * @param taskNo
     * @return
     */
    OrderDeliveryInfoDetails findByTaskNo(String taskNo);
}

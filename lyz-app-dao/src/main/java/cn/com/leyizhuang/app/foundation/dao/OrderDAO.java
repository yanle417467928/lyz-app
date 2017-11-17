package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.foundation.pojo.MaterialListDO;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderBaseInfo;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderGoodsInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/11/10
 */
@Repository
public interface OrderDAO {

    List<MaterialListDO> getGoodsInfoByOrderNumber(String orderNumber);
    //用户获取我的订单列表
    List<OrderBaseInfo> getOrderListByUserIDAndIdentityType(@Param("userID") Long userID, @Param("identityType") AppIdentityType identityType);
    //获取订单所有商品
    List<OrderGoodsInfo> getOrderGoodsInfoByOrderNumber(@Param("orderNumber") String orderNumber);
    //获取订单应付/实付金额
    Double getAmountPayableByOrderNumber(@Param("orderNumber") String orderNumber);
    //计算获取订单所有商品数量
    Integer querySumQtyByOrderNumber(@Param("orderNumber") String orderNumber);
    //模糊查询订单
    List<OrderBaseInfo> getFuzzyQuery(@Param("userID") Long userID, @Param("identityType") AppIdentityType identityType,@Param("condition") String condition);

    OrderBaseInfo findByOrderName(@Param("orderNumber") String outTradeNo);
}

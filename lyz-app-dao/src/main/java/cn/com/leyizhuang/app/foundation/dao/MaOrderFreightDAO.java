package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.management.order.OrderFreightChange;
import cn.com.leyizhuang.app.foundation.pojo.management.order.SimpleOrderBillingDetails;
import cn.com.leyizhuang.app.foundation.vo.management.freight.OrderFreightChangeVO;
import cn.com.leyizhuang.app.foundation.vo.management.freight.OrderFreightDetailVO;
import cn.com.leyizhuang.app.foundation.vo.management.freight.OrderFreightVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface MaOrderFreightDAO {
    List<OrderFreightVO> findAllOrderFreight();

    List<OrderFreightVO> queryOrderFreightVOByStoreId(Long storeId);

    List<OrderFreightVO> queryOrderFreightVOByCityId(Long cityId);

    List<OrderFreightVO> queryOrderFreightVOByInfo(String queryOrderInfo);

    OrderFreightDetailVO queryOrderFreightDetailVOById(Long id);

    OrderFreightVO queryOrderFreightVOById(Long id);

    void  update(SimpleOrderBillingDetails simpleOrderBillingDetails);

    List<OrderFreightChangeVO> queryOrderFreightChangeList();

    void saveOrderFreightChange(OrderFreightChange orderFreightChange);

    void updateOrderPrice(@Param(value ="orderId" ) Long orderId,@Param(value ="freight" )  BigDecimal freight);
}

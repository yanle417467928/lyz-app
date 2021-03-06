package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.management.order.OrderFreightChange;
import cn.com.leyizhuang.app.foundation.vo.management.freight.OrderFreightChangeVO;
import cn.com.leyizhuang.app.foundation.vo.management.freight.OrderFreightDetailVO;
import cn.com.leyizhuang.app.foundation.vo.management.freight.OrderFreightVO;
import com.github.pagehelper.PageInfo;

import java.math.BigDecimal;
import java.util.List;

public interface MaOrderFreightService {

    PageInfo<OrderFreightVO> queryPageVO(Integer page, Integer size, List<Long> storeIds);

    PageInfo<OrderFreightVO> queryOrderFreightVOByStoreId(Integer page, Integer size,Long storeId);

    PageInfo<OrderFreightVO> queryOrderFreightVOByCityId(Integer page, Integer size,Long cityId);

    PageInfo<OrderFreightVO> queryOrderFreightVOByInfo(Integer page, Integer size,String queryOrderInfo);

    OrderFreightDetailVO queryOrderFreightDetailVOById(Long id);

    OrderFreightVO queryOrderFreightVOById(Long id);

    void update(OrderFreightChange orderFreightChange);

    void updateOrderBillingPrice(Long orderId, BigDecimal freight,BigDecimal changAmount);

    PageInfo<OrderFreightChangeVO> queryOrderFreightChangeList(Integer page, Integer size, String keywords);

    void saveOrderFreightChange(OrderFreightChange orderFreightChange);

    List<OrderFreightChange> queryOrderFreightChangeLogListByOid(Long oid);

    OrderFreightChange queryOrderFreightChangeLogFirstByOid(Long oid);

}

package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.order.OrderAgencyFundDO;
import cn.com.leyizhuang.app.foundation.pojo.response.DeliveryAgencyFundResponse;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/11/24
 */
public interface OrderAgencyFundService {

    OrderAgencyFundDO save(OrderAgencyFundDO orderAgencyFundDO);

    List<DeliveryAgencyFundResponse> findByUserId(Long userId);

}

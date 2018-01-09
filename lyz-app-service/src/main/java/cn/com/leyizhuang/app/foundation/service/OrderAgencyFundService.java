package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.order.OrderAgencyFundDO;
import cn.com.leyizhuang.app.foundation.pojo.response.DeliveryAgencyFundResponse;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/11/24
 */
public interface OrderAgencyFundService {

    OrderAgencyFundDO save(OrderAgencyFundDO orderAgencyFundDO);

    List<DeliveryAgencyFundResponse> findByUserId(Long userId);

    PageInfo<DeliveryAgencyFundResponse> findByUserIdAndCreateTime(Long userId, String startDate, String endDate, Integer page, Integer size);

}

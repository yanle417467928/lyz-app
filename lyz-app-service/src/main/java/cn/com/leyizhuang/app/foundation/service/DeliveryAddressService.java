package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.DeliveryAddressDO;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/9/28
 */
public interface DeliveryAddressService {

    List<DeliveryAddressDO> queryList(Long customerId);
}

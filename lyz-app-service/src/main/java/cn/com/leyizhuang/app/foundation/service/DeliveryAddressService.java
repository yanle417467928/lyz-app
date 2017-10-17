package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.foundation.pojo.DeliveryAddressDO;
import cn.com.leyizhuang.app.foundation.pojo.request.DeliveryAddressRequest;
import cn.com.leyizhuang.app.foundation.pojo.response.DeliveryAddressResponse;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/9/28
 */
public interface DeliveryAddressService {

    List<DeliveryAddressResponse> queryListByUserIdAndStatusIsTrue(Long customerId, AppIdentityType identityType);

    DeliveryAddressRequest addDeliveryAddress(Long userId, DeliveryAddressRequest deliveryAddress);

    DeliveryAddressRequest modifyDeliveryAddress(Long userId, DeliveryAddressRequest deliveryAddress);

    void deleteDeliveryAddress(Long deliveryAddressId);
}

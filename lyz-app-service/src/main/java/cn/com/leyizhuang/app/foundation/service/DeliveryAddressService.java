package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.foundation.pojo.AreaManagementDO;
import cn.com.leyizhuang.app.foundation.pojo.request.DeliveryAddressRequest;
import cn.com.leyizhuang.app.foundation.pojo.response.DeliveryAddressResponse;
import cn.com.leyizhuang.app.foundation.pojo.user.DeliveryAddressDO;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/9/28
 */
public interface DeliveryAddressService {

    PageInfo<DeliveryAddressResponse> queryListByUserIdAndStatusIsTrue(Long customerId, AppIdentityType identityType,Integer page);

    DeliveryAddressRequest addDeliveryAddress(Long userId, AppIdentityType identityType, DeliveryAddressRequest deliveryAddress);

    DeliveryAddressRequest modifyDeliveryAddress(Long userId, AppIdentityType identityType, DeliveryAddressRequest deliveryAddress);

    void deleteDeliveryAddress(Long deliveryAddressId);

    DeliveryAddressResponse getDefaultDeliveryAddressByUserIdAndIdentityType(Long userId, AppIdentityType identityType);

    DeliveryAddressResponse getDefaultDeliveryAddressByUserIdAndIdentityTypeAndDeliveryId(Long userId, AppIdentityType identityType,Long deliveryId);

    void clearDefaultAddressByUserIdAndIdentityType(Long userId, AppIdentityType appIdentityTypeByValue);

    DeliveryAddressResponse getTopDeliveryAddressByUserIdAndIdentityType(Long userId, AppIdentityType appIdentityTypeByValue);

    List<DeliveryAddressResponse> queryListByUserIdAndIdentityTypeAndStatusIsTrueAndKeywords(Long userId, AppIdentityType identityType, String keywords);

    void maAddDeliveryAddress(DeliveryAddressDO deliveryAddress);

    List<AreaManagementDO> findAllAreaManagement();

    List<AreaManagementDO> findAreaManagementByProvinceCode(String provinceCode,Long type);

    String findAreaNameByCode(String provinceCode);
}

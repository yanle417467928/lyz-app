package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.foundation.pojo.AreaManagementDO;
import cn.com.leyizhuang.app.foundation.pojo.response.DeliveryAddressResponse;
import cn.com.leyizhuang.app.foundation.pojo.user.DeliveryAddressDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/9/28
 */
@Repository
public interface DeliveryAddressDAO {

    List<DeliveryAddressResponse> queryListByUserIdAndStatusIsTure(Long customerId);

    void addDeliveryAddress(DeliveryAddressDO deliveryAddressDO);

    void modifyDeliveryAddress(DeliveryAddressDO deliveryAddressDO);

    List<DeliveryAddressResponse> queryListByCustomerIdAndStatusIsTrue(@Param(value = "userId") Long userId);

    List<DeliveryAddressResponse> queryListByEmployeeIdAndIdentityTypeAndStatusIsTrue(@Param(value = "userId") Long userId,
                                                                                      @Param(value = "identityType") AppIdentityType identityType);

    DeliveryAddressResponse getDefaultDeliveryAddressByUserIdAndIdentityType(@Param(value = "userId") Long userId,
                                                                             @Param(value = "identityType") AppIdentityType identityType);

    DeliveryAddressResponse getDefaultDeliveryAddressByUserIdAndIdentityTypeAndDeliveryId(@Param(value = "userId") Long userId,
                                                                             @Param(value = "identityType") AppIdentityType identityType,
                                                                             @Param(value = "deliveryId") Long deliveryId);
    DeliveryAddressResponse getDefaultDeliveryAddressByDeliveryId(@Param(value = "deliveryId") Long deliveryId);

    void clearDefaultAddressByUserIdAndIdentityType(@Param(value = "userId") Long userId,
                                                    @Param(value = "identityType") AppIdentityType identityType);

    DeliveryAddressResponse getTopDeliveryAddressByUserIdAndIdentityType(@Param(value = "userId") Long userId,
                                                                         @Param(value = "identityType") AppIdentityType identityType);

    List<DeliveryAddressResponse> queryListByUserIdAndIdentityTypeAndStatusIsTrueAndKeywords(@Param(value = "userId") Long userId,
                                                                                             @Param(value = "identityType") AppIdentityType identityType,
                                                                                             @Param(value = "keywords")String keywords);

    List<AreaManagementDO> findAllAreaManagement();

    List<AreaManagementDO> findAreaManagementByProvinceCode(@Param(value = "provinceCode") String provinceCode,@Param(value = "type") Long type);


    String findAreaNameByCode(@Param(value = "provinceCode") String provinceCode);

    List<DeliveryAddressResponse> getDefaultDeliveryAddressListByUserIdAndIdentityType(@Param(value = "userId") Long userId,
                                                                                       @Param(value = "identityType") AppIdentityType identityType,
                                                                                       @Param(value = "keywords")String keywords);


}

package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.foundation.dao.DeliveryAddressDAO;
import cn.com.leyizhuang.app.foundation.pojo.DeliveryAddressDO;
import cn.com.leyizhuang.app.foundation.pojo.request.DeliveryAddressRequest;
import cn.com.leyizhuang.app.foundation.pojo.response.DeliveryAddressResponse;
import cn.com.leyizhuang.app.foundation.service.DeliveryAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/9/28
 */
@Service
@Transactional
public class DeliveryAddressServiceImpl implements DeliveryAddressService {

    @Autowired
    private DeliveryAddressDAO deliveryAddressDAO;

    public DeliveryAddressServiceImpl(DeliveryAddressDAO deliveryAddressDAO) {
        this.deliveryAddressDAO = deliveryAddressDAO;
    }

    @Override
    public List<DeliveryAddressResponse> queryListByUserIdAndStatusIsTrue(Long userId, AppIdentityType identityType) {
        if (identityType.getValue() == 6 ){
            return this.deliveryAddressDAO.queryListByCustomerIdAndStatusIsTrue(userId);
        }else{
            return this.deliveryAddressDAO.queryListByEmployeeIdAndIdentityTypeAndStatusIsTrue(userId,identityType);
        }

    }

    @Override
    public DeliveryAddressRequest addDeliveryAddress(Long userId, DeliveryAddressRequest deliveryAddress) {
        DeliveryAddressDO deliveryAddressDO = transform(userId, deliveryAddress);
        deliveryAddressDO.setCreatorInfoByBusiness("DeliveryAddressServiceImpl", "addDeliveryAddress");
        this.deliveryAddressDAO.addDeliveryAddress(deliveryAddressDO);
        return deliveryAddress;
    }

    @Override
    public DeliveryAddressRequest modifyDeliveryAddress(Long userId, DeliveryAddressRequest deliveryAddress) {
        DeliveryAddressDO deliveryAddressDO = transform(userId, deliveryAddress);
        deliveryAddressDO.setModifierInfoByBusiness("DeliveryAddressServiceImpl", "modifyDeliveryAddress");
        this.deliveryAddressDAO.modifyDeliveryAddress(deliveryAddressDO);
        return deliveryAddress;
    }

    @Override
    public void deleteDeliveryAddress(Long deliveryAddressId) {
        DeliveryAddressDO deliveryAddressDO = new DeliveryAddressDO();
        deliveryAddressDO.setId(deliveryAddressId);
        deliveryAddressDO.setStatus(false);
        deliveryAddressDO.setModifierInfoByBusiness("DeliveryAddressServiceImpl", "deleteDeliveryAddress");
        this.deliveryAddressDAO.modifyDeliveryAddress(deliveryAddressDO);
    }

    private DeliveryAddressDO transform(Long userId, DeliveryAddressRequest deliveryAddress){
        if (null != userId && null != deliveryAddress) {
            DeliveryAddressDO deliveryAddressDO = new DeliveryAddressDO();
            if (null != deliveryAddress.getId()) {
                deliveryAddressDO.setId(deliveryAddress.getId());
            }
            deliveryAddressDO.setReceiver(deliveryAddress.getDeliveryName());
            deliveryAddressDO.setReceiverPhone(deliveryAddress.getDeliveryPhone());
            deliveryAddressDO.setDeliveryCity(deliveryAddress.getDeliveryCity());
            deliveryAddressDO.setDeliveryCounty(deliveryAddress.getDeliveryCounty());
            deliveryAddressDO.setDeliveryStreet(deliveryAddress.getDeliveryStreet());
            deliveryAddressDO.setDetailedAddress(deliveryAddress.getDetailedAddress());
            if (null != deliveryAddress.getVillageName()) {
                deliveryAddressDO.setResidenceName(deliveryAddress.getVillageName());
            }
            deliveryAddressDO.setUserId(userId);
            deliveryAddressDO.setStatus(true);
            return deliveryAddressDO;
        } else {
            return null;
        }
    }
}

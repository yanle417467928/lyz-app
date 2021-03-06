package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.foundation.dao.DeliveryAddressDAO;
import cn.com.leyizhuang.app.foundation.pojo.AreaManagementDO;
import cn.com.leyizhuang.app.foundation.pojo.request.DeliveryAddressRequest;
import cn.com.leyizhuang.app.foundation.pojo.response.DeliveryAddressResponse;
import cn.com.leyizhuang.app.foundation.pojo.user.DeliveryAddressDO;
import cn.com.leyizhuang.app.foundation.service.DeliveryAddressService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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
    public PageInfo<DeliveryAddressResponse> queryListByUserIdAndStatusIsTrue(Long userId, AppIdentityType identityType, Integer page, Integer size) {
        List deliveryAddressResponseList;
        PageHelper.startPage(page, size);
        if (identityType.getValue() == 6) {
            deliveryAddressResponseList = this.deliveryAddressDAO.queryListByCustomerIdAndStatusIsTrue(userId);
        } else {
            deliveryAddressResponseList = this.deliveryAddressDAO.queryListByEmployeeIdAndIdentityTypeAndStatusIsTrue(userId, identityType);
        }
        return new PageInfo<>(deliveryAddressResponseList);
    }

    @Override
    public DeliveryAddressRequest addDeliveryAddress(Long userId, AppIdentityType identityType, DeliveryAddressRequest deliveryAddress) {
        DeliveryAddressDO deliveryAddressDO = transform(userId, identityType, deliveryAddress);
        deliveryAddressDO.setCreatorInfoByBusiness("DeliveryAddressServiceImpl", "addDeliveryAddress");
        this.deliveryAddressDAO.addDeliveryAddress(deliveryAddressDO);
        return deliveryAddress;
    }

    @Override
    public DeliveryAddressRequest modifyDeliveryAddress(Long userId, AppIdentityType identityType, DeliveryAddressRequest deliveryAddress) {
        DeliveryAddressDO deliveryAddressDO = transform(userId, identityType, deliveryAddress);
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

    @Override
    public DeliveryAddressResponse getDefaultDeliveryAddressByUserIdAndIdentityType(Long userId, AppIdentityType
            identityType) {
        if (null != userId && null != identityType) {
            return deliveryAddressDAO.getDefaultDeliveryAddressByUserIdAndIdentityType(userId, identityType);
        }
        return null;
    }

    @Override
    public PageInfo<DeliveryAddressResponse> getDefaultDeliveryAddressListByUserIdAndIdentityType(Integer page, Integer size,Long userId, AppIdentityType
            identityType,String keywords) {
        if (null != userId && null != identityType) {
            PageHelper.startPage(page, size);
            List<DeliveryAddressResponse> deliveryAddressResponseList = deliveryAddressDAO.getDefaultDeliveryAddressListByUserIdAndIdentityType(userId, identityType,keywords);
            return new PageInfo<>(deliveryAddressResponseList);
        }
        return null;
    }

    @Override
    public DeliveryAddressResponse getDefaultDeliveryAddressByUserIdAndIdentityTypeAndDeliveryId(Long userId, AppIdentityType identityType, Long deliveryId) {
        if (null != userId && null != identityType && null != deliveryId) {
            return deliveryAddressDAO.getDefaultDeliveryAddressByUserIdAndIdentityTypeAndDeliveryId(userId, identityType,deliveryId);
        }
        return null;
    }

    @Override
    public DeliveryAddressResponse getDefaultDeliveryAddressByDeliveryId(Long deliveryId) {
            return deliveryAddressDAO.getDefaultDeliveryAddressByDeliveryId(deliveryId);
    }

    @Override
    public void clearDefaultAddressByUserIdAndIdentityType(Long userId, AppIdentityType identityType) {
        if (null != userId && null != identityType) {
            deliveryAddressDAO.clearDefaultAddressByUserIdAndIdentityType(userId, identityType);
        }
    }

    @Override
    public DeliveryAddressResponse getTopDeliveryAddressByUserIdAndIdentityType(Long userId, AppIdentityType identityType) {
        if (null != userId && null != identityType) {
            return deliveryAddressDAO.getTopDeliveryAddressByUserIdAndIdentityType(userId, identityType);
        }
        return null;
    }

    @Override
    public List<DeliveryAddressResponse> queryListByUserIdAndIdentityTypeAndStatusIsTrueAndKeywords(Long userId, AppIdentityType identityType, String keywords) {
        if (null != userId && null != identityType) {
            return deliveryAddressDAO.queryListByUserIdAndIdentityTypeAndStatusIsTrueAndKeywords(userId, identityType, keywords);
        }
        return null;
    }

    @Override
    public void maAddDeliveryAddress(DeliveryAddressDO deliveryAddress) {
        deliveryAddressDAO.addDeliveryAddress(deliveryAddress);
    }

    @Override
    public List<AreaManagementDO> findAllAreaManagement() {
        return deliveryAddressDAO.findAllAreaManagement();
    }

    @Override
    public List<AreaManagementDO> findAreaManagementByProvinceCode(String provinceCode,Long type) {
        return deliveryAddressDAO.findAreaManagementByProvinceCode(provinceCode,type);
    }

    @Override
    public String findAreaNameByCode(String provinceCode) {
        return deliveryAddressDAO.findAreaNameByCode(provinceCode);
    }

    private DeliveryAddressDO transform(Long userId, AppIdentityType identityType, DeliveryAddressRequest deliveryAddress) {
        if (null != userId && null != deliveryAddress && null != identityType) {
            DeliveryAddressDO deliveryAddressDO = new DeliveryAddressDO();
            if (null != deliveryAddress.getId()) {
                deliveryAddressDO.setId(deliveryAddress.getId());
            }
            deliveryAddressDO.setIdentityType(identityType);
            deliveryAddressDO.setReceiver(deliveryAddress.getDeliveryName());
            deliveryAddressDO.setReceiverPhone(deliveryAddress.getDeliveryPhone());
            deliveryAddressDO.setDeliveryProvince(deliveryAddress.getDeliveryProvince());
            deliveryAddressDO.setDeliveryCity(deliveryAddress.getDeliveryCity());
            deliveryAddressDO.setDeliveryCounty(deliveryAddress.getDeliveryCounty());
            deliveryAddressDO.setDeliveryStreet(deliveryAddress.getDeliveryStreet());
            deliveryAddressDO.setDetailedAddress(deliveryAddress.getDetailedAddress());
            if (null != deliveryAddress.getVillageName()) {
                deliveryAddressDO.setResidenceName(deliveryAddress.getVillageName());
            }
            deliveryAddressDO.setUserId(userId);
            deliveryAddressDO.setIsDefault(deliveryAddress.getIsDefault());
            deliveryAddressDO.setStatus(true);
            deliveryAddressDO.setEstateInfo(deliveryAddress.getEstateInfo());
            return deliveryAddressDO;
        } else {
            return null;
        }
    }
}

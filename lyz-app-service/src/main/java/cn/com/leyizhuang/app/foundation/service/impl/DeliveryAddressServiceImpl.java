package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.DeliveryAddressDAO;
import cn.com.leyizhuang.app.foundation.pojo.DeliveryAddressDO;
import cn.com.leyizhuang.app.foundation.pojo.response.DeliveryAddressResponse;
import cn.com.leyizhuang.app.foundation.service.DeliveryAddressService;
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
    private DeliveryAddressDAO deliveryAddressDAO;
    public DeliveryAddressServiceImpl(DeliveryAddressDAO deliveryAddressDAO) {
        this.deliveryAddressDAO = deliveryAddressDAO;
    }

    @Override
    public List<DeliveryAddressResponse> queryListByUserIdAndStatusIsTure(Long customerId) {
        return this.deliveryAddressDAO.queryListByUserIdAndStatusIsTure(customerId);
    }
}

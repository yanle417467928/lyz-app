package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.foundation.dao.PaymentMethodDAO;
import cn.com.leyizhuang.app.foundation.service.PaymentMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/11/15
 */
@Service
@Transactional
public class PaymentMethodServiceImpl implements PaymentMethodService {

    @Autowired
    private PaymentMethodDAO paymentMethodDAO;

    @Override
    public List<String> findByTypeAndCityId(AppIdentityType type, Long cityId) {
        return this.paymentMethodDAO.findByTypeAndCityId(type, cityId);
    }
}

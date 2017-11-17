package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.foundation.pojo.response.PaymentMethodResponse;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/11/15
 */
public interface PaymentMethodService {
    List<PaymentMethodResponse> findByTypeAndCityId(AppIdentityType type, Long cityId);
}

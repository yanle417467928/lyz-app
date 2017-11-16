package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/11/15
 */
public interface PaymentMethodService {
    List<String> findByTypeAndCityId(AppIdentityType type, Long cityId);
}

package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/11/15
 */
@Repository
public interface PaymentMethodDAO {

    List<String> findByTypeAndCityId(@Param("type") AppIdentityType type, @Param("cityId") Long cityId);
}

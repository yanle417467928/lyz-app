package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.DeliveryAddressDO;
import cn.com.leyizhuang.app.foundation.pojo.response.DeliveryAddressResponse;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/9/28
 */
@Repository
public interface DeliveryAddressDAO {

    List<DeliveryAddressResponse> queryListByUserIdAndStatusIsTure(Long customerId);

}

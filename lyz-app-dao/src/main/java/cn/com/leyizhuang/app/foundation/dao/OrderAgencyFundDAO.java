package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.order.OrderAgencyFundDO;
import cn.com.leyizhuang.app.foundation.pojo.response.DeliveryAgencyFundResponse;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/11/24
 */
@Repository
public interface OrderAgencyFundDAO {

    void save(OrderAgencyFundDO orderAgencyFundDO);

    List<DeliveryAgencyFundResponse> findByUserId(@Param("userId") Long userId);

}

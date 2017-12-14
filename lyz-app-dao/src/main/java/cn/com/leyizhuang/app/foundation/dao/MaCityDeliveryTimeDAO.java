package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.city.CityDeliveryTime;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaCityDeliveryTimeDAO {
        List<CityDeliveryTime> queryPage(Long cityId);

        List<CityDeliveryTime> judgmentTime(@Param("cityId") long cityId,@Param("id") long id);

        void save(CityDeliveryTime cityDeliveryTime);

        CityDeliveryTime queryById(Long id);

         void  update(CityDeliveryTime cityDeliveryTime);

}

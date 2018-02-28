package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.CustomerLeBiVariationLog;
import cn.com.leyizhuang.app.foundation.vo.management.customer.CusLebiLogVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2018/1/13
 */
@Repository
public interface MaCusLebiLogDAO {
    void save(CustomerLeBiVariationLog customerLeBiVariationLog);

    List<CusLebiLogVO> findAllCusLebiLog(@Param("cusId") Long cusId, @Param("cityId")Long cityId, @Param("storeId")Long storeId,
                                         @Param("keywords")String keywords, @Param("list") List<Long> storeIds);

    CusLebiLogVO findCusLebiLogById(Long id);
}

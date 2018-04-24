package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.StPreDepositLogDO;
import cn.com.leyizhuang.app.foundation.vo.management.store.StorePreDepositLogVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2018/1/12
 */
@Repository
public interface MaStorePreDepositLogDAO {

    void save(StPreDepositLogDO stPreDepositLogDO);

    List<StorePreDepositLogVO> findAllStorePredepositLog(@Param("storeId")Long storeId, @Param("cityId")Long cityId,
                                                         @Param("storeType")String storeType, @Param("keywords")String keywords,
                                                         @Param("list") List<Long> storeIds, @Param("changeType")String changeType);

    StorePreDepositLogVO findStorePredepositLogById(Long id);

}

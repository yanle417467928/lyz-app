package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.dto.StorePreDepositDTO;
import cn.com.leyizhuang.app.foundation.pojo.StPreDepositLogDO;
import cn.com.leyizhuang.app.foundation.vo.management.store.StorePreDepositLogVO;
import com.github.pagehelper.PageInfo;

/**
 * @author GenerationRoad
 * @date 2018/1/12
 */
public interface MaStorePreDepositLogService {

    StPreDepositLogDO save(StorePreDepositDTO storePreDepositDTO);


    PageInfo<StorePreDepositLogVO> findAllStorePredepositLog(Integer page, Integer size, Long storeId, Long cityId, String storeType, String keywords);

    StorePreDepositLogVO findStorePredepositLogById(Long id);
}

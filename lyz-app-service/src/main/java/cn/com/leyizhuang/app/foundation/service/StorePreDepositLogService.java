package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.StPreDepositLogDO;
import cn.com.leyizhuang.app.foundation.pojo.response.PreDepositLogResponse;
import cn.com.leyizhuang.common.core.constant.PreDepositChangeType;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/11/8
 */
public interface StorePreDepositLogService {

    List<PreDepositLogResponse> findByUserIdAndType(Long userId, List<PreDepositChangeType> typeList);

    StPreDepositLogDO save(StPreDepositLogDO stPreDepositLogDO);
}

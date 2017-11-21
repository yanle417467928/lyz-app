package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.CusPreDepositLogDO;
import cn.com.leyizhuang.common.core.constant.PreDepositChangeType;
import cn.com.leyizhuang.app.foundation.pojo.response.PreDepositLogResponse;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/11/7
 */
public interface CusPreDepositLogService {

    List<PreDepositLogResponse> findByUserIdAndType(Long userId, List<PreDepositChangeType> typeList);

    CusPreDepositLogDO save(CusPreDepositLogDO cusPreDepositLogDO);
}

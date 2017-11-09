package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.core.constant.PreDepositChangeType;
import cn.com.leyizhuang.app.foundation.pojo.response.CusPreDepositLogResponse;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/11/7
 */
public interface CusPreDepositLogService {

    List<CusPreDepositLogResponse> findByUserIdAndType(Long userId, List<PreDepositChangeType> typeList);
}

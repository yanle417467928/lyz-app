package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.core.constant.CustomerPreDepositChangeType;
import cn.com.leyizhuang.app.foundation.pojo.CusPreDepositLogDO;
import cn.com.leyizhuang.app.foundation.pojo.response.PreDepositLogResponse;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/11/7
 */
public interface CusPreDepositLogService {

    PageInfo<PreDepositLogResponse> findByUserIdAndType(Long userId, List<CustomerPreDepositChangeType> typeList, Integer page, Integer size);

    CusPreDepositLogDO save(CusPreDepositLogDO cusPreDepositLogDO);
}

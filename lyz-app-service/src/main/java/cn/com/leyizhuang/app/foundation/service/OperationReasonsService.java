package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.response.OperationReasonsResponse;
import cn.com.leyizhuang.common.core.constant.OperationReasonType;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/11/13
 */
public interface OperationReasonsService {

    List<OperationReasonsResponse> findAllByType(OperationReasonType type);
}

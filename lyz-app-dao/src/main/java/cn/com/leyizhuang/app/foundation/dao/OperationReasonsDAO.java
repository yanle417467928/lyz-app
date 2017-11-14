package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.response.OperationReasonsResponse;
import cn.com.leyizhuang.common.core.constant.OperationReasonType;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/11/13
 */
@Repository
public interface OperationReasonsDAO {

    List<OperationReasonsResponse> findAllByType(OperationReasonType type);
}

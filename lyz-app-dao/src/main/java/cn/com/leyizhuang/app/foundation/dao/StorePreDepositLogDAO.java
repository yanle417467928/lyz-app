package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.StPreDepositLogDO;
import cn.com.leyizhuang.app.foundation.pojo.response.PreDepositLogResponse;
import cn.com.leyizhuang.common.core.constant.PreDepositChangeType;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/11/8
 */
@Repository
public interface StorePreDepositLogDAO {

    List<PreDepositLogResponse> findByUserIdAndType(@Param("userId") Long userId, @Param("list") List<PreDepositChangeType> typeList);

    void save(StPreDepositLogDO stPreDepositLogDO);

}

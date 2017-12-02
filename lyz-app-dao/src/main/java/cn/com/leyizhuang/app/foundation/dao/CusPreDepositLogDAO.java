package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.core.constant.CustomerPreDepositChangeType;
import cn.com.leyizhuang.app.foundation.pojo.CusPreDepositLogDO;
import cn.com.leyizhuang.app.foundation.pojo.response.PreDepositLogResponse;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/11/7
 */
@Repository
public interface CusPreDepositLogDAO {

    List<PreDepositLogResponse> findByUserIdAndType(@Param("userId") Long userId, @Param("list") List<CustomerPreDepositChangeType> typeList);

    void save(CusPreDepositLogDO cusPreDepositLogDO);

}

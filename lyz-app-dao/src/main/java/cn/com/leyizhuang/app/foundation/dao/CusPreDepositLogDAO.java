package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.core.constant.PreDepositChangeType;
import cn.com.leyizhuang.app.foundation.pojo.response.CusPreDepositLogResponse;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/11/7
 */
@Repository
public interface CusPreDepositLogDAO {

    List<CusPreDepositLogResponse> findByUserIdAndType(@Param("userId") Long userId, @Param("list") List<PreDepositChangeType> typeList);

}

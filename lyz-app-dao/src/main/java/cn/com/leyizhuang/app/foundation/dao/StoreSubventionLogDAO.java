package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.response.StoreSubventionLogResponse;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/11/27
 */
@Repository
public interface StoreSubventionLogDAO {
    List<StoreSubventionLogResponse> findByUserId(@Param("userId") Long userId);
}

package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.response.StoreCreditMoneyLogResponse;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/11/27
 */
@Repository
public interface StoreCreditMoneyLogDAO {
    List<StoreCreditMoneyLogResponse> findByUserId(@Param("userId") Long userId);
}

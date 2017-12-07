package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.StoreCreditMoney;
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

    /**
     * 根据用户id查询门店信用金
     * @param userId    用户id
     * @return  返回门店信用金类
     */
   StoreCreditMoney findStoreCreditMoneyByUserId(@Param("userId") Long userId);
}

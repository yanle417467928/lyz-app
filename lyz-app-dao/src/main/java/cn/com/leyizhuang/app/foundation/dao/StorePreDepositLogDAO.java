package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.core.constant.StorePreDepositChangeType;
import cn.com.leyizhuang.app.foundation.pojo.StPreDepositLogDO;
import cn.com.leyizhuang.app.foundation.pojo.StorePreDeposit;
import cn.com.leyizhuang.app.foundation.pojo.response.PreDepositLogResponse;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/11/8
 */
@Repository
public interface StorePreDepositLogDAO {

    List<PreDepositLogResponse> findByUserIdAndType(@Param("userId") Long userId, @Param("list") List<StorePreDepositChangeType> typeList);

    void save(StPreDepositLogDO stPreDepositLogDO);

    /**
     * 根据导购id查询门店预存款信息
     * @param userId    用户id
     * @return  门店预存款信息
     */
    StorePreDeposit findStoreByUserId(@Param("userId") Long userId);

    /**
     * 修改门店预存款
     * @param money 修改后金额
     * @param userId    用户id
     */
    void updateStPreDepositByUserId(@Param("money") Double money,@Param("userId")Long userId);
}

package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.AppStore;
import cn.com.leyizhuang.common.foundation.dao.BaseDAO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * 门店服务Dao层
 *
 * @author Richard
 * Created on 2017-07-24 9:12
 **/
@Repository
public interface AppStoreDAO {

    List<AppStore> findAll();

    AppStore findById(Long id);

    AppStore findDefaultStoreByCityId(Long cityId);

    Double findSubventionBalanceByUserId(Long userId);

    Double findCreditMoneyBalanceByUserId(Long userId);

    Double findPreDepositBalanceByUserId(Long userId);

    int updateStoreDepositByUserIdAndStoreDeposit(@Param("userId") Long userId,@Param("deposit") Double storeDeposit);

    int updateStoreCreditByUserIdAndCredit(@Param("userId") Long userId,@Param("credit") Double storeCredit);

    int updateStoreSubventionByUserIdAndSubvention(@Param("userId") Long userId,@Param("subvention") Double storeSubvention);
}

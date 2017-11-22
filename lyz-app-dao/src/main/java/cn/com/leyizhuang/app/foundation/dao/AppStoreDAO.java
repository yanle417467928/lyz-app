package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.core.constant.StoreType;
import cn.com.leyizhuang.app.foundation.pojo.AppStore;
import cn.com.leyizhuang.app.foundation.pojo.StorePreDeposit;
import cn.com.leyizhuang.app.foundation.pojo.response.SelfTakeStore;
import cn.com.leyizhuang.app.foundation.pojo.response.StoreResponse;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

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

    int updateStoreInventoryByEmployeeIdAndGoodsIdAndInventory(
            @Param("userId") Long userId,@Param("gid") Long index,@Param("qty") Integer integer);

    int updateStoreInventoryByCustomerIdAndGoodsIdAndInventory(
            @Param("userId") Long userId,@Param("gid") Long index,@Param("qty") Integer integer);

    void updateStoreDepositByUserId(@Param("userId") Long userId,@Param("deposit") Double storeDeposit);

    void updateStoreCreditByUserId(@Param("userId") Long userId,@Param("credit") Double storeCredit);

    void updateStoreSubventionByUserId(@Param("userId") Long userId,@Param("subvention") Double storeSubvention);

    void updateStoreInventoryByEmployeeIdAndGoodsId(
            @Param("userId") Long userId,@Param("gid") Long index,@Param("qty") Integer integer);

    void updateStoreInventoryByCustomerIdAndGoodsId(
            @Param("userId") Long userId,@Param("gid") Long index,@Param("qty") Integer integer);

    Boolean existGoodsStoreInventory(@Param("storeId") Long storeId,@Param("gid") Long gid,@Param("qty") Integer qty);

    List<StoreResponse> findStoreByCityId(Long cityId);

    void saveStore(AppStore appStore);

    void modifyStore(AppStore appStore);

    void deleteStoreByStoreCode(@Param("storeCode") String storeCode);

    AppStore findByStoreCode(@Param("storeCode") String storeCode);

    StorePreDeposit findStorePreDepositByEmpId(Long userId);

    AppStore findAppStoreByEmpId(Long userId);

    void saveStorePreDeposit(StorePreDeposit storePreDeposit);

    List<AppStore> findStoreListByCityId(Long cityId);

    List<StoreResponse> findStoreByCityIdAndNotStoreType(@Param("cityId") Long cityId, @Param("storeType") StoreType storeType);

    AppStore findAppStoreCusId(@Param(value = "userId") Long userId);

    List<SelfTakeStore> findSelfTakePermittedStoreByCityId(Long cityId);
}

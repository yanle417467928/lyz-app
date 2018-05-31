package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.core.constant.StoreType;
import cn.com.leyizhuang.app.foundation.pojo.*;
import cn.com.leyizhuang.app.foundation.pojo.inventory.StoreInventory;
import cn.com.leyizhuang.app.foundation.pojo.inventory.StoreInventoryAvailableQtyChangeLog;
import cn.com.leyizhuang.app.foundation.pojo.response.SelfTakeStore;
import cn.com.leyizhuang.app.foundation.pojo.response.StoreResponse;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Date;
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

    int updateStoreDepositByUserIdAndStoreDeposit(@Param("userId") Long userId, @Param("deposit") Double storeDeposit,
                                                  @Param(value = "version") Timestamp version);

    int updateStoreCreditByUserIdAndCredit(@Param("userId") Long userId, @Param("credit") Double storeCredit,
                                           @Param(value = "version") Timestamp version);

    int updateStoreSubventionByUserIdAndSubvention(@Param("userId") Long userId, @Param("subvention") Double storeSubvention,
                                                   @Param(value = "version") Timestamp version);

    int updateStoreInventoryByEmployeeIdAndGoodsIdAndInventory(
            @Param("userId") Long userId, @Param("gid") Long index, @Param("qty") Integer integer);

    int updateStoreInventoryByCustomerIdAndGoodsIdAndInventory(
            @Param("userId") Long userId, @Param("gid") Long index, @Param("qty") Integer integer);

    int updateStoreDepositByUserId(@Param("userId") Long userId, @Param("deposit") Double storeDeposit, @Param("lastUpdateTime") Timestamp lastUpdateTime);

    void updateStoreCreditByUserId(@Param("userId") Long userId, @Param("credit") Double storeCredit);

    Integer updateStoreCreditByUserIdAndVersion(@Param("userId") Long userId, @Param("credit") Double storeCredit, @Param("version") Date version);

    void updateStoreSubventionByUserId(@Param("userId") Long userId, @Param("subvention") Double storeSubvention);

    void updateStoreInventoryByEmployeeIdAndGoodsId(
            @Param("userId") Long userId, @Param("gid") Long index, @Param("qty") Integer integer);

    void updateStoreInventoryByCustomerIdAndGoodsId(
            @Param("userId") Long userId, @Param("gid") Long index, @Param("qty") Integer integer);

    Boolean existGoodsStoreInventory(@Param("storeId") Long storeId, @Param("gid") Long gid, @Param("qty") Integer qty);

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

    /**
     * 变更指定门店下指定产品的库存
     *
     * @param storeId   门店id
     * @param goodsId   商品id
     * @param inventory 变更量
     * @return 影响行数
     */
    Integer updateStoreInventoryByStoreIdAndGoodsIdAndInventory(@Param(value = "storeId") Long storeId,
                                                                @Param(value = "goodsId") Long goodsId,
                                                                @Param(value = "inventory") Integer inventory,
                                                                @Param(value = "version") Date version);

    StoreInventory findStoreInventoryByStoreIdAndGoodsId(@Param(value = "storeId") Long storeId,
                                                         @Param(value = "goodsId") Long goodsId);

    StoreInventory findStoreInventoryByStoreCodeAndGoodsSku(@Param(value = "storeCode") String storeCode,
                                                         @Param(value = "goodsSku") String goodsSku);


    StoreInventory findStoreInventoryByStoreCodeAndGoodsId(@Param(value = "storeCode") String storeCode,
                                                           @Param(value = "goodsId") Long goodsId);

    void addStoreInventoryAvailableQtyChangeLog(StoreInventoryAvailableQtyChangeLog log);

    StoreCreditMoney findStoreCreditMoneyByEmpId(Long empId);

    StoreSubvention findStoreSubventionByEmpId(Long userId);

    void addStPreDepositLog(StPreDepositLogDO log);

    void addStoreCreditMoneyChangeLog(StoreCreditMoneyChangeLog log);

    void addStoreSubventionChangeLog(StoreSubventionChangeLog log);

    void updateStoreInventoryByStoreCodeAndGoodsId(@Param("storeCode") String storeCode, @Param("gid") Long gid, @Param("qty") Integer qty);

    Integer updateStoreInventoryByStoreCodeAndGoodsIdAndVersion(@Param("storeCode") String storeCode, @Param("gid") Long gid,
                                                                @Param("qty") Integer qty, @Param(value = "version") Date version);

    Integer updateStoreSubventionByUserIdAndVersion(@Param("subvention") Double subvention, @Param("userId") Long userId, @Param("version") Timestamp version);

    StorePreDeposit findStorePreDepositByUserIdAndIdentityType(@Param(value = "userId") Long userId,
                                                               @Param(value = "identityType") Integer identityType);

    /*int updateStoreDepositByStoreIdAndStoreDeposit(@Param(value = "storeId") Long storeId,
                                                    @Param(value = "stPreDeposit") Double stPreDeposit,
                                                    @Param(value = "lastUpdateTime") Timestamp lastUpdateTime);*/
    StorePreDeposit findStorePreDepositByStoreId(@Param("storeId")Long storeId);

    Integer updateStoreDepositByStoreIdAndStoreDeposit(@Param("storeId") Long storeId, @Param("deposit") Double storeDeposit,
                                                  @Param(value = "version") Timestamp version);

    List<AppStore> findStoreListByLoginAdministrator(Long id);

    List<AppStore> findFitStoreListByLoginAdministrator(Long id);
}

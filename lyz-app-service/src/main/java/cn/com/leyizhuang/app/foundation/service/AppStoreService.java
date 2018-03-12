package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.core.constant.StorePreDepositChangeType;
import cn.com.leyizhuang.app.core.constant.StoreType;
import cn.com.leyizhuang.app.foundation.pojo.*;
import cn.com.leyizhuang.app.foundation.pojo.inventory.StoreInventory;
import cn.com.leyizhuang.app.foundation.pojo.inventory.StoreInventoryAvailableQtyChangeLog;
import cn.com.leyizhuang.app.foundation.pojo.response.SelfTakeStore;
import cn.com.leyizhuang.app.foundation.pojo.response.StoreResponse;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 门店服务层
 *
 * @author Richard
 * Created on 2017-07-20 10:40
 **/
public interface AppStoreService {

    List<AppStore> findAll();

    AppStore findById(Long id);

    AppStore findDefaultStoreByCityId(Long cityId);

    Double findSubventionBalanceByUserId(Long userId);

    Double findCreditMoneyBalanceByUserId(Long userId);

    Double findPreDepositBalanceByUserId(Long userId);

    int lockStoreDepositByUserIdAndStoreDeposit(Long userId, Double storeDeposit, Timestamp version);

    int lockStoreCreditByUserIdAndCredit(Long userId, Double storeCredit, Timestamp version);

    int lockStoreSubventionByUserIdAndSubvention(Long userId, Double storeSubvention, Timestamp version);

    int lockStoreInventoryByUserIdAndIdentityTypeAndInventory(Long userId, Integer identityType, Map<Long, Integer> storeInventory);

    void unlockStoreDepositByUserIdAndStoreDeposit(Long userId, Double storeDeposit);

    void unlockStoreCreditByUserIdAndCredit(Long userId, Double storeCredit);

    void unlockStoreSubventionByUserIdAndSubvention(Long userId, Double storeSubvention);

    void unlockStoreInventoryByUserIdAndIdentityTypeAndInventory(Long userId, Integer identityType, Map<Long, Integer> storeInventory);

    List<StoreResponse> findStoreByCityId(Long cityId);

    List<AppStore> findStoreListByCityId(Long cityId);

    void saveStore(AppStore appStore);

    void modifyStore(AppStore appStore);

    void deleteStoreByStoreCode(String storeCode);

    AppStore findByStoreCode(String storeCode);

    void preDepositRecharge(PaymentDataDO paymentDataDO, StorePreDepositChangeType type);

    List<StoreResponse> findStoreByCityIdAndNotStoreType(Long cityId, StoreType storeType);


    AppStore findStoreByUserIdAndIdentityType(Long userId, Integer identityType);

    List<SelfTakeStore> findSelfTakePermittedStoreByCityId(Long cityId);

    Integer lockStoreInventoryByStoreIdAndGoodsIdAndInventory(Long storeId, Long goodsId, Integer inventory, Timestamp version);

    StoreInventory findStoreInventoryByStoreIdAndGoodsId(Long bookingStoreId, Long goodsId);

    StoreInventory findStoreInventoryByStoreCodeAndGoodsSku(String storeCode, String goodsSku);

    void addStoreInventoryAvailableQtyChangeLog(StoreInventoryAvailableQtyChangeLog log);

    StorePreDeposit findStorePreDepositByEmpId(Long userId);

    StoreCreditMoney findStoreCreditMoneyByEmpId(Long empId);

    StoreSubvention findStoreSubventionByEmpId(Long userId);

    void addStPreDepositLog(StPreDepositLogDO log);

    void addStoreCreditMoneyChangeLog(StoreCreditMoneyChangeLog log);

    void addStoreSubventionChangeLog(StoreSubventionChangeLog log);

    StoreInventory findStoreInventoryByStoreCodeAndGoodsId(String storeCode, Long goodsId);

    void updateStoreInventoryByStoreCodeAndGoodsId(String storeCode, Long gid, Integer qty);

    Integer updateStoreInventoryByStoreCodeAndGoodsIdAndVersion(String storeCode, Long gid, Integer qty, Date version);

    Integer updateStoreCreditByUserIdAndVersion(Long userId, Double storeCredit, Date version);

    Integer updateStoreSubventionByUserIdAndVersion(Double subvention, Long userId, Timestamp version);

    StorePreDeposit findStorePreDepositByStoreId(Long storeId);

    StorePreDeposit findStorePreDepositByUserIdAndIdentityType(Long userId, Integer identityType);

    Integer updateStoreDepositByStoreIdAndStoreDeposit(Long storeId, Double storeDeposit, Timestamp version);

}

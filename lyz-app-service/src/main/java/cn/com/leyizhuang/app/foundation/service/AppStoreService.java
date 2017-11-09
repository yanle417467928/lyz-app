package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.AppStore;
import cn.com.leyizhuang.app.foundation.pojo.response.StoreResponse;

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

    int lockStoreDepositByUserIdAndStoreDeposit(Long userId, Double storeDeposit);

    int lockStoreCreditByUserIdAndCredit(Long userId, Double storeCredit);

    int lockStoreSubventionByUserIdAndSubvention(Long userId, Double storeSubvention);

    int lockStoreInventoryByUserIdAndIdentityTypeAndInventory(Long userId, Integer identityType, Map<Long,Integer> storeInventory);

    void unlockStoreDepositByUserIdAndStoreDeposit(Long userId, Double storeDeposit);

    void unlockStoreCreditByUserIdAndCredit(Long userId, Double storeCredit);

    void unlockStoreSubventionByUserIdAndSubvention(Long userId, Double storeSubvention);

    void unlockStoreInventoryByUserIdAndIdentityTypeAndInventory(Long userId, Integer identityType, Map<Long,Integer> storeInventory);

    List<StoreResponse> findStoreByCityId(Long cityId);

    void saveStore(AppStore appStore);

    void modifyStore(AppStore appStore);

    void deleteStoreByStoreCode(String storeCode);

    AppStore findByStoreCode(String storeCode);

}

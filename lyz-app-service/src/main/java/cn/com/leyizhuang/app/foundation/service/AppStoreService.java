package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.AppStore;
import cn.com.leyizhuang.common.foundation.service.BaseService;

import java.util.List;

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

    int lockStoreInventoryByUserIdAndIdentityTypeAndInventory(Long userId, Integer identityType, Integer storeInventory);

    int lockCityInventoryByUserIdAndIdentityTypeAndInventory(Long userId, Integer identityType, Integer cityInventory);
}

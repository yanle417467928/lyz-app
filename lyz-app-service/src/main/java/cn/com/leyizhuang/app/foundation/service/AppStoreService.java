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
public interface AppStoreService extends BaseService<AppStore> {

    List<AppStore> findAll();

    AppStore findById(Long id);

    AppStore findDefaultStoreByCityId(Long cityId);

    Double findSubventionBalanceByStoreId(Long storeId);

    Double findCreditMoneyBalanceByStoreId(Long storeId);

    Double findPreDepositBalanceByStoreId(Long storeId);
}

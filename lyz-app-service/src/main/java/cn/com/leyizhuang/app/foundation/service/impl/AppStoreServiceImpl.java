package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.core.constant.StoreType;
import cn.com.leyizhuang.app.foundation.dao.AppStoreDAO;
import cn.com.leyizhuang.app.foundation.pojo.AppStore;
import cn.com.leyizhuang.app.foundation.pojo.PaymentDataDO;
import cn.com.leyizhuang.app.foundation.pojo.StPreDepositLogDO;
import cn.com.leyizhuang.app.foundation.pojo.StorePreDeposit;
import cn.com.leyizhuang.app.foundation.pojo.response.SelfTakeStore;
import cn.com.leyizhuang.app.foundation.pojo.AppStore;
import cn.com.leyizhuang.app.foundation.pojo.PaymentDataDO;
import cn.com.leyizhuang.app.foundation.pojo.StPreDepositLogDO;
import cn.com.leyizhuang.app.foundation.pojo.StorePreDeposit;
import cn.com.leyizhuang.app.foundation.pojo.response.StoreResponse;
import cn.com.leyizhuang.app.foundation.service.AppStoreService;
import cn.com.leyizhuang.app.foundation.service.StorePreDepositLogService;
import cn.com.leyizhuang.common.core.constant.PreDepositChangeType;
import cn.com.leyizhuang.common.util.CountUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 门店服务实现类
 *
 * @author Richard
 * Created on 2017-07-20 10:42
 **/
@Service
public class AppStoreServiceImpl implements AppStoreService {

    private AppStoreDAO storeDAO;

    @Autowired
    public void setStoreDAO(AppStoreDAO storeDAO) {
        this.storeDAO = storeDAO;
    }

    @Autowired
    private StorePreDepositLogService storePreDepositLogServiceImpl;

    @Override
    public List<AppStore> findAll() {
        return storeDAO.findAll();
    }

    @Override
    public AppStore findById(Long id) {
        if (null != id) {
            return storeDAO.findById(id);
        }
        return null;
    }

    @Override
    public AppStore findDefaultStoreByCityId(Long cityId) {
        if (null != cityId) {
            return storeDAO.findDefaultStoreByCityId(cityId);
        }
        return null;
    }

    @Override
    public Double findSubventionBalanceByUserId(Long userId) {
        if (null != userId) {
            return storeDAO.findSubventionBalanceByUserId(userId);
        }
        return null;
    }

    @Override
    public Double findCreditMoneyBalanceByUserId(Long userId) {
        if (null != userId) {
            return storeDAO.findCreditMoneyBalanceByUserId(userId);
        }
        return null;
    }

    @Override
    public Double findPreDepositBalanceByUserId(Long userId) {
        if (null != userId) {
            return storeDAO.findPreDepositBalanceByUserId(userId);
        }
        return null;
    }

    @Override
    @Transactional
    public int lockStoreDepositByUserIdAndStoreDeposit(Long userId, Double storeDeposit) {
        if (null != userId && null != storeDeposit) {
            return storeDAO.updateStoreDepositByUserIdAndStoreDeposit(userId, storeDeposit);
        }
        return 0;
    }

    @Override
    @Transactional
    public int lockStoreCreditByUserIdAndCredit(Long userId, Double storeCredit) {
        if (null != userId && null != storeCredit) {
            return storeDAO.updateStoreCreditByUserIdAndCredit(userId, storeCredit);
        }
        return 0;
    }

    @Override
    @Transactional
    public int lockStoreSubventionByUserIdAndSubvention(Long userId, Double storeSubvention) {
        if (null != userId && null != storeSubvention) {
            return storeDAO.updateStoreSubventionByUserIdAndSubvention(userId, storeSubvention);
        }
        return 0;
    }

    @Override
    @Transactional
    public int lockStoreInventoryByUserIdAndIdentityTypeAndInventory(Long userId, Integer identityType, Map<Long, Integer> storeInventory) {
        if (null != userId && !storeInventory.isEmpty()) {
            if (identityType == 6) {
                for (Long index : storeInventory.keySet()) {
                    int result = storeDAO.updateStoreInventoryByCustomerIdAndGoodsIdAndInventory(userId, index, storeInventory.get(index));
                    if (result == 0) {
                        return 0;
                    }
                }
            } else {
                for (Long index : storeInventory.keySet()) {
                    int result = storeDAO.updateStoreInventoryByEmployeeIdAndGoodsIdAndInventory(userId, index, storeInventory.get(index));
                    if (result == 0) {
                        return 0;
                    }
                }
            }
        }
        return 1;
    }

    @Override
    @Transactional
    public void unlockStoreDepositByUserIdAndStoreDeposit(Long userId, Double storeDeposit) {
        if (null != userId && null != storeDeposit) {
            storeDAO.updateStoreDepositByUserId(userId, storeDeposit);
        }
    }

    @Override
    @Transactional
    public void unlockStoreCreditByUserIdAndCredit(Long userId, Double storeCredit) {
        if (null != userId && null != storeCredit) {
            storeDAO.updateStoreCreditByUserId(userId, storeCredit);
        }
    }

    @Override
    @Transactional
    public void unlockStoreSubventionByUserIdAndSubvention(Long userId, Double storeSubvention) {
        if (null != userId && null != storeSubvention) {
            storeDAO.updateStoreSubventionByUserId(userId, storeSubvention);
        }
    }

    @Override
    @Transactional
    public void unlockStoreInventoryByUserIdAndIdentityTypeAndInventory(Long userId, Integer identityType, Map<Long, Integer> storeInventory) {
        if (null != userId && !storeInventory.isEmpty()) {
            if (identityType == 6) {
                for (Long index : storeInventory.keySet()) {
                    storeDAO.updateStoreInventoryByCustomerIdAndGoodsId(userId, index, storeInventory.get(index));
                }
            } else {
                for (Long index : storeInventory.keySet()) {
                    storeDAO.updateStoreInventoryByEmployeeIdAndGoodsId(userId, index, storeInventory.get(index));
                }
            }
        }
    }

    @Override
    public List<StoreResponse> findStoreByCityId(Long cityId) {
        return this.storeDAO.findStoreByCityId(cityId);
    }

    @Override
    public List<AppStore> findStoreListByCityId(Long cityId) {
        return this.storeDAO.findStoreListByCityId(cityId);
    }

    @Override
    public void saveStore(AppStore appStore) {
        this.storeDAO.saveStore(appStore);
    }

    @Override
    public void modifyStore(AppStore appStore) {
        this.storeDAO.modifyStore(appStore);
    }

    @Override
    public void deleteStoreByStoreCode(String storeCode) {
        this.storeDAO.deleteStoreByStoreCode(storeCode);
    }

    @Override
    public AppStore findByStoreCode(String storeCode) {
        return this.storeDAO.findByStoreCode(storeCode);
    }

    @Override
    public void preDepositRecharge(PaymentDataDO paymentDataDO, PreDepositChangeType type) {
        Long userId = paymentDataDO.getUserId();
        Double money = paymentDataDO.getTotalFee();
        StorePreDeposit storePreDeposit = this.storeDAO.findStorePreDepositByEmpId(userId);
        if (null == storePreDeposit) {
            storePreDeposit = new StorePreDeposit();
            storePreDeposit.setBalance(money);
            AppStore store = this.storeDAO.findAppStoreByEmpId(userId);
            storePreDeposit.setStoreId(store.getStoreId());
            this.storeDAO.saveStorePreDeposit(storePreDeposit);
        } else {
            this.storeDAO.updateStoreDepositByUserId(userId, money);
        }
        StPreDepositLogDO log = new StPreDepositLogDO();
        log.setCreateTimeAndChangeMoneyAndType(LocalDateTime.now(), money, type);
        log.setUserIdAndOperatorinfo(storePreDeposit.getStoreId(), userId, paymentDataDO.getAppIdentityType(), "");
        log.setOrderNumber(paymentDataDO.getOutTradeNo());
        log.setUserOrderNumber(paymentDataDO.getTradeNo());
        log.setBalance(CountUtil.add(storePreDeposit.getBalance(), money));
        this.storePreDepositLogServiceImpl.save(log);
    }

    @Override
    public List<StoreResponse> findStoreByCityIdAndNotStoreType(Long cityId, StoreType storeType) {
        return this.storeDAO.findStoreByCityIdAndNotStoreType(cityId, storeType);
    }

    @Override
    public AppStore findStoreByUserIdAndIdentityType(Long userId, Integer identityType) {
        if (null != userId && null != identityType) {
            new AppStore();
            AppStore store;
            if (identityType == AppIdentityType.CUSTOMER.getValue()) {
                store = storeDAO.findAppStoreCusId(userId);
            } else {
                store = storeDAO.findAppStoreByEmpId(userId);
            }
            return store;
        }
        return null;
    }

    @Override
    public List<SelfTakeStore> findSelfTakePermittedStoreByCityId(Long cityId) {
        if (null != cityId){
            return storeDAO.findSelfTakePermittedStoreByCityId(cityId);
        }
        return null;
    }
}

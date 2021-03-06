package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.config.shiro.ShiroUser;
import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.core.constant.StorePreDepositChangeType;
import cn.com.leyizhuang.app.core.constant.StoreType;
import cn.com.leyizhuang.app.foundation.dao.AppStoreDAO;
import cn.com.leyizhuang.app.foundation.pojo.*;
import cn.com.leyizhuang.app.foundation.pojo.inventory.StoreInventory;
import cn.com.leyizhuang.app.foundation.pojo.inventory.StoreInventoryAvailableQtyChangeLog;
import cn.com.leyizhuang.app.foundation.pojo.response.SelfTakeStore;
import cn.com.leyizhuang.app.foundation.pojo.response.StoreResponse;
import cn.com.leyizhuang.app.foundation.service.AppStoreService;
import cn.com.leyizhuang.app.foundation.service.StorePreDepositLogService;
import cn.com.leyizhuang.common.core.exception.AppConcurrentExcp;
import cn.com.leyizhuang.common.util.CountUtil;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
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
    private StorePreDepositLogService storePreDepositLogServiceImpl;

    @Autowired
    public void setStoreDAO(AppStoreDAO storeDAO) {
        this.storeDAO = storeDAO;
    }

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
    @Transactional(rollbackFor = Exception.class)
    public int lockStoreDepositByUserIdAndStoreDeposit(Long userId, Double storeDeposit, Timestamp version) {
        if (null != userId && null != storeDeposit) {
            return storeDAO.updateStoreDepositByUserIdAndStoreDeposit(userId, storeDeposit, version);
        }
        return 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int lockStoreCreditByUserIdAndCredit(Long userId, Double storeCredit, Timestamp version) {
        if (null != userId && null != storeCredit) {
            return storeDAO.updateStoreCreditByUserIdAndCredit(userId, storeCredit, version);
        }
        return 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int lockStoreSubventionByUserIdAndSubvention(Long userId, Double storeSubvention, Timestamp version) {
        if (null != userId && null != storeSubvention) {
            return storeDAO.updateStoreSubventionByUserIdAndSubvention(userId, storeSubvention, version);
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
    @Transactional(rollbackFor = Exception.class)
    public void unlockStoreCreditByUserIdAndCredit(Long userId, Double storeCredit) {
        if (null != userId && null != storeCredit) {
            storeDAO.updateStoreCreditByUserId(userId, storeCredit);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
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
    @Transactional(rollbackFor = Exception.class)
    public void preDepositRecharge(PaymentDataDO paymentDataDO, StorePreDepositChangeType type) {
        Long userId = paymentDataDO.getUserId();
        Double money = paymentDataDO.getTotalFee();
        StorePreDeposit storePreDeposit = this.storeDAO.findStorePreDepositByEmpId(userId);
        if (null == storePreDeposit) {
            storePreDeposit = new StorePreDeposit();
            storePreDeposit.setBalance(money);
            AppStore store = this.storeDAO.findAppStoreByEmpId(userId);
            storePreDeposit.setStoreId(store.getStoreId());
            storePreDeposit.setCreateTime(new Date());
            storePreDeposit.setLastUpdateTime(new Timestamp(System.currentTimeMillis()));
            this.storeDAO.saveStorePreDeposit(storePreDeposit);
        } else {
            int row = this.storeDAO.updateStoreDepositByUserId(userId, money, storePreDeposit.getLastUpdateTime());
            if (1 != row) {
                throw new AppConcurrentExcp("账号余额信息过期！");
            }
        }
        StPreDepositLogDO log = new StPreDepositLogDO();
        log.setCreateTimeAndChangeMoneyAndType(LocalDateTime.now(), money, type);
        log.setUserIdAndOperatorinfo(storePreDeposit.getStoreId(), userId, paymentDataDO.getAppIdentityType(), "");
        log.setOrderNumber(paymentDataDO.getOutTradeNo());
        log.setMerchantOrderNumber(paymentDataDO.getTradeNo());
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
        if (null != cityId) {
            return storeDAO.findSelfTakePermittedStoreByCityId(cityId);
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer lockStoreInventoryByStoreIdAndGoodsIdAndInventory(Long storeId, Long goodsId, Integer inventory, Timestamp version) {
        if (null != storeId && null != goodsId && null != inventory) {
            return storeDAO.updateStoreInventoryByStoreIdAndGoodsIdAndInventory(storeId, goodsId, inventory, version);
        }
        return null;
    }

    @Override
    public StoreInventory findStoreInventoryByStoreIdAndGoodsId(Long bookingStoreId, Long goodsId) {
        if (null != bookingStoreId && null != goodsId) {
            return storeDAO.findStoreInventoryByStoreIdAndGoodsId(bookingStoreId, goodsId);
        }
        return null;
    }

    @Override
    public StoreInventory findStoreInventoryByStoreCodeAndGoodsSku(String storeCode, String goodsSku) {
        if (null != storeCode && null != goodsSku) {
            return storeDAO.findStoreInventoryByStoreCodeAndGoodsSku(storeCode, goodsSku);
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addStoreInventoryAvailableQtyChangeLog(StoreInventoryAvailableQtyChangeLog log) {
        if (null != log) {
            storeDAO.addStoreInventoryAvailableQtyChangeLog(log);
        }
    }

    @Override
    public StorePreDeposit findStorePreDepositByEmpId(Long userId) {
        if (null != userId) {
            return storeDAO.findStorePreDepositByEmpId(userId);
        }
        return null;
    }

    @Override
    public StoreCreditMoney findStoreCreditMoneyByEmpId(Long empId) {
        if (null != empId) {
            return storeDAO.findStoreCreditMoneyByEmpId(empId);
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StoreSubvention findStoreSubventionByEmpId(Long userId) {
        if (null != userId) {
            return storeDAO.findStoreSubventionByEmpId(userId);
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addStPreDepositLog(StPreDepositLogDO log) {
        if (null != log) {
            storeDAO.addStPreDepositLog(log);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addStoreCreditMoneyChangeLog(StoreCreditMoneyChangeLog log) {
        if (null != log) {
            storeDAO.addStoreCreditMoneyChangeLog(log);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addStoreSubventionChangeLog(StoreSubventionChangeLog log) {
        if (null != log) {
            storeDAO.addStoreSubventionChangeLog(log);
        }
    }

    @Override
    public StoreInventory findStoreInventoryByStoreCodeAndGoodsId(String storeCode, Long goodsId) {
        return storeDAO.findStoreInventoryByStoreCodeAndGoodsId(storeCode, goodsId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStoreInventoryByStoreCodeAndGoodsId(String storeCode, Long gid, Integer qty) {
        storeDAO.updateStoreInventoryByStoreCodeAndGoodsId(storeCode, gid, qty);
    }

    @Override
    public Integer updateStoreInventoryByStoreCodeAndGoodsIdAndVersion(String storeCode, Long gid, Integer qty, Date version) {
        if (null != storeCode && null != gid && null != qty) {
            return storeDAO.updateStoreInventoryByStoreCodeAndGoodsIdAndVersion(storeCode, gid, qty, version);
        }
        return null;
    }

    @Override
    public Integer updateStoreCreditByUserIdAndVersion(Long userId, Double storeCredit, Date version) {
        if (null != userId && null != storeCredit) {
            return storeDAO.updateStoreCreditByUserIdAndVersion(userId, storeCredit, version);
        }
        return null;
    }

    @Override
    public Integer updateStoreSubventionByUserIdAndVersion(Double subvention, Long userId, Timestamp version) {
        if (null != subvention && null != userId) {
            return storeDAO.updateStoreSubventionByUserIdAndVersion(subvention, userId, version);
        }
        return null;
    }

    @Override
    public StorePreDeposit findStorePreDepositByStoreId(Long storeId) {
        if (null != storeId) {
            return storeDAO.findStorePreDepositByStoreId(storeId);
        }
        return null;
    }

    @Override
    public Integer updateStoreDepositByStoreIdAndStoreDeposit(Long storeId, Double storeDeposit, Timestamp version) {
        if (null != storeId && null != storeDeposit && null != version) {
            return storeDAO.updateStoreDepositByStoreIdAndStoreDeposit(storeId, storeDeposit, version);
        }
        return 0;
    }

    @Override
    public List<AppStore> findStoreListByLoginAdministrator() {
        ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        if (null != shiroUser) {
            return storeDAO.findStoreListByLoginAdministrator(shiroUser.getId());
        }
        return null;
    }

    @Override
    public List<AppStore> findFitStoreListByLoginAdministrator() {
        ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        if (null != shiroUser) {
            return storeDAO.findFitStoreListByLoginAdministrator(shiroUser.getId());
        }
        return null;
    }

    @Override
    public StoreCreditMoney findStoreCreditMoneyByStoreId(Long storeId) {
        if (null != storeId){
            return this.storeDAO.findStoreCreditMoneyByStoreId(storeId);
        }
        return null;
    }

    @Override
    public Integer updateStoreCreditByStoreIdAndVersion(Long storeId, Double storeCredit, Timestamp version) {
        if (null != storeCredit && null != storeId) {
            return storeDAO.updateStoreCreditByStoreIdAndVersion(storeId, storeCredit, version);
        }
        return null;
    }

    @Override
    public StorePreDeposit findStorePreDepositByUserIdAndIdentityType(Long userId, Integer identityType) {
        if (null != userId && null != identityType) {
            return storeDAO.findStorePreDepositByUserIdAndIdentityType(userId, identityType);
        }
        return null;
    }


}

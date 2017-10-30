package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.AppStoreDAO;
import cn.com.leyizhuang.app.foundation.pojo.AppStore;
import cn.com.leyizhuang.app.foundation.service.AppStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Override
    public List<AppStore> findAll() {
        return storeDAO.findAll();
    }

    @Override
    public AppStore findById(Long id) {
        if (null != id){
            return storeDAO.findById(id);
        }
        return null;
    }

    @Override
    public AppStore findDefaultStoreByCityId(Long cityId) {
        if (null != cityId){
            return storeDAO.findDefaultStoreByCityId(cityId);
        }
        return null;
    }

    @Override
    public Double findSubventionBalanceByUserId(Long userId) {
        if (null != userId){
            return storeDAO.findSubventionBalanceByUserId(userId);
        }
        return null;
    }

    @Override
    public Double findCreditMoneyBalanceByUserId(Long userId) {
        if (null != userId){
            return storeDAO.findCreditMoneyBalanceByUserId(userId);
        }
        return null;
    }

    @Override
    public Double findPreDepositBalanceByUserId(Long userId) {
        if (null != userId){
            return storeDAO.findPreDepositBalanceByUserId(userId);
        }
        return null;
    }

    @Override
    @Transactional
    public int lockStoreDepositByUserIdAndStoreDeposit(Long userId, Double storeDeposit) {
        if (null != userId && null != storeDeposit){
            return storeDAO.updateStoreDepositByUserIdAndStoreDeposit(userId,storeDeposit);
        }
        return 0;
    }

    @Override
    @Transactional
    public int lockStoreCreditByUserIdAndCredit(Long userId, Double storeCredit) {
        if (null != userId && null != storeCredit){
            return storeDAO.updateStoreCreditByUserIdAndCredit(userId,storeCredit);
        }
        return 0;
    }

    @Override
    @Transactional
    public int lockStoreSubventionByUserIdAndSubvention(Long userId, Double storeSubvention) {
        if (null != userId && null != storeSubvention){
            return storeDAO.updateStoreSubventionByUserIdAndSubvention(userId,storeSubvention);
        }
        return 0;
    }

    @Override
    @Transactional
    public int lockStoreInventoryByUserIdAndIdentityTypeAndInventory(Long userId, Integer identityType, Integer storeInventory) {
        return 0;
    }

    @Override
    @Transactional
    public int lockCityInventoryByUserIdAndIdentityTypeAndInventory(Long userId, Integer identityType, Integer cityInventory) {
        return 0;
    }
}

package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.AppStoreDAO;
import cn.com.leyizhuang.app.foundation.pojo.AppStore;
import cn.com.leyizhuang.common.foundation.dao.BaseDAO;
import cn.com.leyizhuang.common.foundation.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 门店服务实现类
 *
 * @author Richard
 * Created on 2017-07-20 10:42
 **/
@Service
public class AppStoreServiceImpl extends BaseServiceImpl<AppStore> implements cn.com.leyizhuang.app.foundation.service.AppStoreService {
    public AppStoreServiceImpl(BaseDAO<AppStore> baseDAO) {
        super(baseDAO);
    }

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
    public Double findSubventionBalanceByStoreId(Long storeId) {
        if(null != storeId){
            return storeDAO.findSubventionBalanceByStoreId(storeId);
        }
        return null;
    }

    @Override
    public Double findCreditMoneyBalanceByStoreId(Long storeId) {
        if(null != storeId){
            return storeDAO.findCreditMoneyBalanceByStoreId(storeId);
        }
        return null;
    }

    @Override
    public Double findPreDepositBalanceByStoreId(Long storeId) {
        if(null != storeId){
            return storeDAO.findPreDepositBalanceByStoreId(storeId);
        }
        return null;
    }
}

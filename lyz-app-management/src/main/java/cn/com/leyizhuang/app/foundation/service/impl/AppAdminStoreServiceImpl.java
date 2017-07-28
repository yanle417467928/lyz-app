package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.AppAdminStoreDAO;
import cn.com.leyizhuang.app.foundation.pojo.Store;
import cn.com.leyizhuang.app.foundation.service.AppAdminStoreService;
import cn.com.leyizhuang.common.foundation.dao.BaseDAO;
import cn.com.leyizhuang.common.foundation.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 门店服务实现类
 *
 * @author Richard
 * Created on 2017-07-20 10:42
 **/
@Service
public class AppAdminStoreServiceImpl extends BaseServiceImpl<Store> implements AppAdminStoreService {
    public AppAdminStoreServiceImpl(BaseDAO<Store> baseDAO) {
        super(baseDAO);
    }

    private AppAdminStoreDAO storeDAO;
    @Autowired
    public void setStoreDAO(AppAdminStoreDAO storeDAO) {
        this.storeDAO = storeDAO;
    }

    @Override
    public List<Store> findAll() {
        return storeDAO.findAll();
    }
}

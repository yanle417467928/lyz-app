package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.AppStoreDAO;
import cn.com.leyizhuang.app.foundation.pojo.AppStore;
import cn.com.leyizhuang.app.foundation.service.IAppStoreService;
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
public class AppStoreService extends BaseServiceImpl<AppStore> implements IAppStoreService {
    public AppStoreService(BaseDAO<AppStore> baseDAO) {
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
}

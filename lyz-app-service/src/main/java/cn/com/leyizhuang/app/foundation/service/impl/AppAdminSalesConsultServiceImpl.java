package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.AppAdminSalesConsultDAO;
import cn.com.leyizhuang.app.foundation.pojo.SalesConsult;
import cn.com.leyizhuang.app.foundation.service.AppAdminSalesConsultService;
import cn.com.leyizhuang.common.foundation.dao.BaseDAO;
import cn.com.leyizhuang.common.foundation.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 销售顾问服务实现类
 *
 * @author Richard
 * Created on 2017-07-24 10:35
 **/
@Service
public class AppAdminSalesConsultServiceImpl extends BaseServiceImpl<SalesConsult> implements AppAdminSalesConsultService {
    private AppAdminSalesConsultDAO salesConsultDAO;

    public AppAdminSalesConsultServiceImpl(BaseDAO<SalesConsult> baseDAO) {
        super(baseDAO);
    }

    @Autowired
    public void setSalesConsultDAO(AppAdminSalesConsultDAO salesConsultDAO) {
        this.salesConsultDAO = salesConsultDAO;
    }

    @Override
    public List<SalesConsult> findAll() {
        return salesConsultDAO.findAll();
    }

    @Override
    public List<SalesConsult> findByStoreId(Long storeId) {
        if (null != storeId) {
            return salesConsultDAO.findByStoreId(storeId);
        }
        return null;
    }

    @Override
    public SalesConsult findByConsultId(Long consultId) {
        if (null != consultId) {
            return salesConsultDAO.findByConsultId(consultId);
        }
        return null;
    }


}

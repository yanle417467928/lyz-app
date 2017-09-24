package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.SalesConsult;
import cn.com.leyizhuang.common.foundation.dao.BaseDAO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 销售顾问服务Dao层
 *
 * @author Richard
 * Created on 2017-07-24 9:12
 **/
@Repository
public interface AppAdminSalesConsultDAO extends BaseDAO<SalesConsult> {

    List<SalesConsult> findAll();

    List<SalesConsult> findByStoreId(Long storeId);

    SalesConsult findByConsultId(Long consultId);
}

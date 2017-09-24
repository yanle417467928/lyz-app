package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.SalesConsult;
import cn.com.leyizhuang.common.foundation.service.BaseService;

import java.util.List;

/**
 * 销售顾问服务层
 *
 * @author Richard
 * Created on 2017-07-20 10:37
 **/
public interface AppAdminSalesConsultService extends BaseService<SalesConsult> {

    List<SalesConsult> findAll();

    List<SalesConsult> findByStoreId(Long storeId);

    SalesConsult findByConsultId(Long consultId);
}

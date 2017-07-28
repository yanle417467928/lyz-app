package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.SalesConsult;
import cn.com.leyizhuang.common.foundation.service.BaseService;

import java.util.List;

/**
 * 销售顾问服务
 *
 * @author Richard
 * Created on 2017-07-24 10:33
 **/
public interface AppAdminConsultService extends BaseService<SalesConsult> {
    List<SalesConsult> findAll();
}

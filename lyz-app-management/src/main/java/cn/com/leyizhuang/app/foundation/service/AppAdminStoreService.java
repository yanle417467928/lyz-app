package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.Store;
import cn.com.leyizhuang.common.foundation.service.BaseService;

import java.util.List;

/**
 * 门店服务层
 *
 * @author Richard
 * Created on 2017-07-20 10:40
 **/
public interface AppAdminStoreService extends BaseService<Store> {

    List<Store> findAll();
}

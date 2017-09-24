package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.AppStore;
import cn.com.leyizhuang.common.foundation.dao.BaseDAO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 门店服务Dao层
 *
 * @author Richard
 * Created on 2017-07-24 9:12
 **/
@Repository
public interface AppStoreDAO extends BaseDAO<AppStore> {

    List<AppStore> findAll();

    AppStore findById(Long id);
}

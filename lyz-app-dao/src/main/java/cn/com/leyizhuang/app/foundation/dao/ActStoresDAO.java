package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.activity.ActStoreDO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * table: act_stores
 * Created by panjie on 2017/11/22.
 */
@Repository
public interface ActStoresDAO {

    void save(ActStoreDO actBaseDO);

    void update(ActStoreDO actBaseDO);

    List<ActStoreDO> queryList();

    ActStoreDO queryById();
}

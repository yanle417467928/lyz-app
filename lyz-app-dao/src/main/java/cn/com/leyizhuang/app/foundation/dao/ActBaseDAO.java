package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.activity.ActBaseDO;
import cn.com.leyizhuang.common.foundation.dao.BaseDAO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * table: act_base
 * Created by panjie on 2017/11/22.
 */
@Repository
public interface ActBaseDAO extends BaseDAO<ActBaseDO>{

    List<ActBaseDO> queryList();

}

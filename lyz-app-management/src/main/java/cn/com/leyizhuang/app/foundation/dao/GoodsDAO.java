package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.GoodsDO;
import cn.com.leyizhuang.common.foundation.dao.BaseDAO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/9/5
 */
@Repository
public interface GoodsDAO extends BaseDAO<GoodsDO> {
    List<GoodsDO> queryList();
}

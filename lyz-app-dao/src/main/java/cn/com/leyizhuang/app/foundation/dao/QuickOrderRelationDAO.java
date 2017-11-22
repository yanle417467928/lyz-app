package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.goods.GoodsDO;
import org.springframework.stereotype.Repository;

/**
 * @author GenerationRoad
 * @date 2017/11/21
 */
@Repository
public interface QuickOrderRelationDAO {

    GoodsDO findByNumber(String number);
}

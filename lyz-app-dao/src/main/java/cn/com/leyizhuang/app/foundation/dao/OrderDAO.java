package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.MaterialListDO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/11/10
 */
@Repository
public interface OrderDAO {

    List<MaterialListDO> getGoodsInfoByOrderNumber(String orderNumber);
}

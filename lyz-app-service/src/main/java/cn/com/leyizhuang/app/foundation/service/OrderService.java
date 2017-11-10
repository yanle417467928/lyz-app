package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.MaterialListDO;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/11/10
 */
public interface OrderService {

    List<MaterialListDO> getGoodsInfoByOrderNumber(String orderNumber);
}

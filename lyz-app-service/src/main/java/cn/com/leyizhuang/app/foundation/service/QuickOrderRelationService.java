package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.goods.GoodsDO;

/**
 * @author GenerationRoad
 * @date 2017/11/21
 */
public interface QuickOrderRelationService {

    GoodsDO findByNumber(String number);

}
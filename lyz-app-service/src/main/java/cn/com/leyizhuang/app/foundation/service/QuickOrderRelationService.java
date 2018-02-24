package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.foundation.pojo.QuickOrderRelationDO;
import cn.com.leyizhuang.app.foundation.pojo.goods.GoodsDO;

/**
 * @author GenerationRoad
 * @date 2017/11/21
 */
public interface QuickOrderRelationService {

    GoodsDO findByNumber(Long userId, AppIdentityType identityType, String number);

    QuickOrderRelationDO findQuickOrderRelationDOByNumber(String number);

}

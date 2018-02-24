package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.foundation.pojo.QuickOrderRelationDO;
import cn.com.leyizhuang.app.foundation.pojo.goods.GoodsDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author GenerationRoad
 * @date 2017/11/21
 */
@Repository
public interface QuickOrderRelationDAO {

    GoodsDO findByNumber(@Param("userId")Long userId, @Param("identityType")AppIdentityType identityType, @Param("number")String number);

    QuickOrderRelationDO findQuickOrderRelationDOByNumber(String number);
}

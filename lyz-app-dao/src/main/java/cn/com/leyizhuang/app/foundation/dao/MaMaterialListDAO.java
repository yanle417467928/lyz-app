package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.foundation.pojo.MaterialListDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2018/1/31
 */
@Repository
public interface MaMaterialListDAO {

    MaterialListDO findByUserIdAndIdentityTypeAndGoodsId(@Param("userId") Long userId, @Param(value = "identityType")
            AppIdentityType identityType, @Param(value = "goodsId") Long goodsId);

    void batchSave(List<MaterialListDO> materialLists);

    void modifyQty(@Param("id") Long id, @Param("qty") Integer qty);

}

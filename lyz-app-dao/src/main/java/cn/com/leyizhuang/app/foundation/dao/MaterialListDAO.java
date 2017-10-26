package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.foundation.pojo.MaterialListDO;
import cn.com.leyizhuang.app.foundation.pojo.response.MaterialListResponse;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/10/17
 */
@Repository
public interface MaterialListDAO {

    void save(MaterialListDO materialListDO);

    void batchSave(List<MaterialListDO> list);

    void modifyQty(@Param("id") Long id, @Param("qty") Integer qty);

    void batchDelete(List<Long> ids);

    List<MaterialListResponse> findByUserIdAndIdentityType(@Param("userId") Long userId, @Param("identityType") AppIdentityType identityType);

    MaterialListDO findByUserIdAndIdentityTypeAndGoodsId(@Param("userId") Long userId, @Param(value = "identityType")
             AppIdentityType identityType, @Param(value = "goodsId" )Long goodsId);
}

package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.foundation.pojo.MaterialListDO;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2018/1/31
 */
public interface MaMaterialListService {

    MaterialListDO findByUserIdAndIdentityTypeAndGoodsId(Long userId, AppIdentityType identityType, Long goodsId);

    void saveAndUpdateMaterialList(List<MaterialListDO> materialListSave, List<MaterialListDO> materialListUpdate);

}

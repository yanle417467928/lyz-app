package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.foundation.pojo.MaterialListDO;
import cn.com.leyizhuang.app.foundation.pojo.response.MaterialListResponse;

import java.util.List;
import java.util.Map;

/**
 * @author GenerationRoad
 * @date 2017/10/17
 */
public interface MaterialListService {
    void batchSave(List<MaterialListDO> materialLists);

    void modifyQty(Long id, Integer qty);

    void deleteMaterialList(List<Long> ids);

    List<MaterialListResponse> findByUserIdAndIdentityType(Long userId, Integer identityType);

    MaterialListDO findByUserIdAndIdentityTypeAndGoodsId(Long userId, AppIdentityType identityType, Long goodsId);

    List<MaterialListDO> findMaterialListByUserIdAndIdentityType(Long userId, AppIdentityType identityType);

    List<Long> findMaterialListGoodsIdsByUserIdAndIdentityType(Long userId, AppIdentityType identityType);

    void deleteMaterialListByUserIdAndIdentityTypeAndGoodsId(Long empId, AppIdentityType identityType, List<Long> deleteGoodsIds);

    Map<Long,Integer> findGoodsQtyByUserIdAndIdentityTypeAndGoodsId(Long userId, AppIdentityType identityType, Long goodsId);

}

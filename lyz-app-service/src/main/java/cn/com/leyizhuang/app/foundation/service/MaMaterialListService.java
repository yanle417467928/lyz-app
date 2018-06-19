package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.foundation.pojo.MaterialListDO;
import cn.com.leyizhuang.app.foundation.pojo.management.MaterialChangeDetailLog;
import cn.com.leyizhuang.app.foundation.pojo.management.MaterialChangeHeadLog;
import cn.com.leyizhuang.app.foundation.pojo.response.materialList.MaUpdateMaterialResponse;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2018/1/31
 */
public interface MaMaterialListService {

    MaterialListDO findByUserIdAndIdentityTypeAndGoodsId(Long userId, AppIdentityType identityType, Long goodsId);

    void saveAndUpdateMaterialList(List<MaterialListDO> materialListSave, List<MaterialListDO> materialListUpdate);

    void updateRemarkAndDeliveryId(String remark,Long deliveryId,Long userId,AppIdentityType identityType);

    List<MaterialListDO> findMaPhotoOrderMaterialListByPhotoNumber(String photoNo);

    List<MaUpdateMaterialResponse> findMaAllMaterialListByPhotoNumber(String photoNo, String identityType);

    void deleteMaterialListByUserIdAndIdentityTypeAndGoodsSku(Long userId,String identityType,String sku);

    void saveMaterialChangeHeadLogAndDetailLog(MaterialChangeHeadLog materialChangeHeadLog,List<MaterialChangeDetailLog> materialChangeDetailLog);

}

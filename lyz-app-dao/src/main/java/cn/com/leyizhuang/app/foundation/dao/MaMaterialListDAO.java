package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.foundation.pojo.MaterialListDO;
import cn.com.leyizhuang.app.foundation.pojo.management.MaterialChangeDetailLog;
import cn.com.leyizhuang.app.foundation.pojo.management.MaterialChangeHeadLog;
import cn.com.leyizhuang.app.foundation.pojo.response.materialList.MaUpdateMaterialResponse;
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

    void modifyQty(@Param("id") Long id, @Param("qty") Integer qty, @Param("deliveryId")Long deliveryId,
                   @Param("isGenerateOrder")String isGenerateOrder,@Param("userId")Long userId,@Param("identityType")AppIdentityType identityType);

    void updateRemarkAndDeliveryId(@Param("remark")String remark,@Param("deliveryId")Long deliveryId,
                                   @Param("userId")Long userId,@Param("identityType")AppIdentityType identityType);

    List<MaterialListDO> findMaPhotoOrderMaterialListByPhotoNumber(@Param("photoNo")String photoNo);

    List<MaUpdateMaterialResponse> findMaAllMaterialListByPhotoNumber(@Param("photoNo")String photoNo, @Param("identityType")String identityType);

    void deleteMaterialListByUserIdAndIdentityTypeAndGoodsSku(@Param(value = "userId") Long userId,
                                                              @Param(value = "identityType") String identityType,
                                                              @Param(value = "sku")String sku);

    void saveMaterialChangeHeadLog(MaterialChangeHeadLog materialChangeHeadLog);

    void saveMaterialChangeDetailLog(MaterialChangeDetailLog materialChangeDetailLog);

    List<MaUpdateMaterialResponse> findProxyMaterialListByPhotoNumber(@Param("userid")Long userid, @Param("identityType")AppIdentityType identityType);



}

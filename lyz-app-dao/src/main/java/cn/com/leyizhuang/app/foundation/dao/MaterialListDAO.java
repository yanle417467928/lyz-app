package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.foundation.pojo.MaterialListDO;
import cn.com.leyizhuang.app.foundation.pojo.response.materialList.CouponMaterialListResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.materialList.NormalMaterialListResponse;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

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

    List<NormalMaterialListResponse> findByUserIdAndIdentityType(@Param("userId") Long userId, @Param("identityType") AppIdentityType identityType);

    MaterialListDO findByUserIdAndIdentityTypeAndGoodsId(@Param("userId") Long userId, @Param(value = "identityType")
            AppIdentityType identityType, @Param(value = "goodsId") Long goodsId);

    List<MaterialListDO> findMaterialListByUserIdAndIdentityType(Long userId, AppIdentityType identityType);

    List<Long> findMaterialListGoodsIdsByUserIdAndIdentityType(Long userId, AppIdentityType identityType);

    void deleteMaterialListByUserIdAndIdentityTypeAndGoodsId(@Param(value = "userId") Long empId,
                                                             @Param(value = "identityType") AppIdentityType identityType,
                                                             @Param(value = "deleteGoodsIds") List<Long> deleteGoodsIds);

    Map<Long, Integer> findGoodsQtyByUserIdAndIdentityTypeAndGoodsId(@Param(value = "userId") Long userId,
                                                                     @Param(value = "identityType") AppIdentityType identityType,
                                                                     @Param(value = "goodsId") Long goodsId);

    Boolean existOtherMaterialListByUserIdAndIdentityType(@Param("userId") Long userID,
                                                          @Param("identityType") AppIdentityType appIdentityTypeByValue);

    List<NormalMaterialListResponse> findMaterialListByUserIdAndTypeAndAuditIsNotNull(@Param("userId") Long userId,
                                                                                      @Param("identityType") AppIdentityType identityType);

    List<NormalMaterialListResponse> findMaterialListByUserIdAndTypeAndIsCouponId(@Param("userId") Long userId,
                                                                                  @Param("identityType") AppIdentityType identityType);

    MaterialListDO findCouponTransformByUserIdAndIdentityTypeAndGoodsId(@Param("userId") Long userId,
                                                                        @Param("identityType") AppIdentityType appIdentityTypeByValue,
                                                                        @Param("goodsId") Long goodsId);

    MaterialListDO findCouponTransformByUserIdAndCusIdAndIdentityTypeAndGoodsId(@Param("userId") Long userId,
                                                                                @Param("cusId") Long cusId,
                                                                                @Param("identityType") AppIdentityType appIdentityTypeByValue,
                                                                                @Param("goodsId") Long goodsId);

    List<CouponMaterialListResponse> findGuideMaterialListByUserIdAndCusIdAndIdentityType(@Param("userId") Long userId,
                                                                             @Param("cusId") Long cusId,
                                                                             @Param("identityType") AppIdentityType identityType);

    List<CouponMaterialListResponse> findCoutomerMaterialListByUserIdAndIdentityType(@Param("userId") Long userId,
                                                                                     @Param("identityType") AppIdentityType identityType);
}

package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.core.constant.MaterialListType;
import cn.com.leyizhuang.app.foundation.pojo.MaterialListDO;
import cn.com.leyizhuang.app.foundation.pojo.request.GoodsIdQtyParam;
import cn.com.leyizhuang.app.foundation.pojo.response.materialList.CouponMaterialListResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.materialList.NormalMaterialListResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.materialList.PhotoOrderMaterialListResponse;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

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

    MaterialListDO findAuditListByUserIdAndIdentityTypeAndGoodsId(@Param("userId") Long userId, @Param(value = "identityType")
            AppIdentityType identityType, @Param(value = "goodsId") Long goodsId);


    List<Long> findMaterialListGoodsIdsByUserIdAndIdentityType(Long userId, AppIdentityType identityType);

    void deleteMaterialListByUserIdAndIdentityTypeAndGoodsId(@Param(value = "userId") Long empId,
                                                             @Param(value = "identityType") AppIdentityType identityType,
                                                             @Param(value = "deleteGoodsIds") List<Long> deleteGoodsIds);

    GoodsIdQtyParam findGoodsQtyByUserIdAndIdentityTypeAndGoodsId(@Param(value = "userId") Long userId,
                                                                  @Param(value = "identityType") AppIdentityType identityType,
                                                                  @Param(value = "goodsId") Long goodsId);

    Boolean existOtherMaterialListByUserIdAndIdentityType(@Param("userId") Long userID,
                                                          @Param("identityType") AppIdentityType appIdentityTypeByValue);

    List<NormalMaterialListResponse> findMaterialListByUserIdAndTypeAndAuditIsNotNull(@Param("userId") Long userId,
                                                                                      @Param("identityType") AppIdentityType identityType);

    MaterialListDO findCouponTransformByUserIdAndIdentityTypeAndGoodsId(@Param("userId") Long userId,
                                                                        @Param("identityType") AppIdentityType appIdentityTypeByValue,
                                                                        @Param("goodsId") Long goodsId);

    MaterialListDO findCouponTransformByUserIdAndCusIdAndIdentityTypeAndGoodsId(@Param("userId") Long userId,
                                                                                @Param("cusId") Long cusId,
                                                                                @Param("identityType") AppIdentityType appIdentityTypeByValue,
                                                                                @Param("goodsId") Long goodsId);

    List<CouponMaterialListResponse> findGuideMaterialListByUserIdAndCusIdAndIdentityType(@Param("userId") Long userId,
                                                                                          @Param("identityType") AppIdentityType identityType);

    List<CouponMaterialListResponse> findCustomerMaterialListByUserIdAndIdentityType(@Param("userId") Long userId,
                                                                                     @Param("identityType") AppIdentityType identityType);

    Boolean existOtherMaterialCouponByUserIdAndIdentityType(@Param("userId") Long userId,
                                                            @Param("cusId") Long cusId,
                                                            @Param("identityType") AppIdentityType identityType);

    void deleteMaterialListByUserIdAndIdentityTypeAndGoodsIds(@Param(value = "userId") Long userId,
                                                              @Param(value = "identityType") AppIdentityType identityType,
                                                              @Param(value = "goodsIds") Set<Long> goodsIds);

    void deleteMaMaterialListByUserIdAndIdentityTypeAndGoodsSkus(@Param(value = "userId") Long userId,
                                                              @Param(value = "identityType") AppIdentityType identityType,
                                                              @Param(value = "goodsSkus") Set<String> goodsSkus);

    void deleteMaterialListProductCouponGoodsByUserIdAndIdentityTypeAndGoodsIds(@Param(value = "userId") Long userId,
                                                                                @Param(value = "identityType") AppIdentityType identityType,
                                                                                @Param(value = "couponGoodsIds") Set<Long> couponGoodsIds);

    List<PhotoOrderMaterialListResponse> findByUserIdAndIdentityTypeAndMaterialListType(@Param(value = "userId") Long userId,
                                                                                        @Param(value = "identityType") AppIdentityType identityType,
                                                                                        @Param(value = "list")List<MaterialListType> materialListTypes);
}

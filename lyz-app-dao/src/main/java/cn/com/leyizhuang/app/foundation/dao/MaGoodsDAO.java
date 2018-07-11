package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.goods.GoodsDO;
import cn.com.leyizhuang.app.foundation.pojo.management.goods.GoodsShippingInfo;
import cn.com.leyizhuang.app.foundation.pojo.management.order.MaOrderGoodsInfo;
import cn.com.leyizhuang.app.foundation.pojo.response.GoodsBrandResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.GoodsSpecificationResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.GoodsTypeResponse;
import cn.com.leyizhuang.app.foundation.vo.management.goods.GoodsResponseVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaGoodsDAO {

    void saveGoodsShippingInfo(GoodsShippingInfo goodsShippingInfo);

    List<GoodsResponseVO> findGoodsByCidAndCusId(@Param("cusId") Long cusId, @Param("list") List<Long> cids);

    List<GoodsResponseVO> findGoodsByCidAndEmpId(@Param("empId") Long cusId, @Param("list") List<Long> cids);

    List<GoodsResponseVO> findGoodsByMultiConditionQueryAndCusId(@Param("cusId") Long cusId, @Param("cid") Long cids,@Param("firstCategoryCode")String categoryType,
                                                 @Param("brandId")Long brandString,@Param("specification")String specificationString, @Param("typeId")Long goodsTypeString);

    List<GoodsResponseVO> findGoodsByMultiConditionQueryAndEmpId(@Param("empId") Long cusId, @Param("cid") Long cids,@Param("firstCategoryCode")String categoryType,
                                                 @Param("brandId")Long brandString,@Param("specification")String specificationString, @Param("typeId")Long goodsTypeString);

    GoodsDO findGoodsById(Long id);

    List<GoodsResponseVO> findGoodsByCidAndCusIdAndUserRank(@Param("cusId") Long cusId);

    List<GoodsBrandResponse> findGoodsBrandListByCategoryCodeAndCustomerIdAndIdentityType(
            @Param(value = "categoryCode") String categoryCode, @Param(value = "userId") Long userId, @Param(value = "categorySecond") String categorySecond, @Param(value = "specification") String specification, @Param(value = "goodsType") String goodsType);

    List<GoodsBrandResponse> findGoodsBrandListByCategoryCodeAndEmployeeIdAndIdentityType(
            @Param(value = "categoryCode") String categoryCode, @Param(value = "userId") Long userId, @Param(value = "categorySecond") String categorySecond, @Param(value = "specification") String specification, @Param(value = "goodsType") String goodsType);

    List<GoodsTypeResponse> findGoodsTypeListByCategoryCodeAndCustomerIdAndIdentityType(
            @Param(value = "categoryCode") String categoryCode, @Param(value = "userId") Long userId, @Param(value = "categorySecond") String categorySecond, @Param(value = "specification") String specification, @Param(value = "goodsBrand") String goodsBrand);

    List<GoodsTypeResponse> findGoodsTypeListByCategoryCodeAndEmployeeIdAndIdentityType(
            @Param(value = "categoryCode") String categoryCode, @Param(value = "userId") Long userId, @Param(value = "categorySecond") String categorySecond, @Param(value = "specification") String specification, @Param(value = "goodsBrand") String goodsBrand);

    List<GoodsSpecificationResponse> findGoodsSpecificationListByCategoryCodeAndCustomerIdAndIdentityType(
            @Param(value = "categoryCode") String categoryCode, @Param(value = "userId") Long userId, @Param(value = "categorySecond") String categorySecond , @Param(value = "goodsBrand") String goodsBrand, @Param(value = "goodsType") String goodsType );

    List<GoodsSpecificationResponse> findGoodsSpecificationListByCategoryCodeAndEmployeeIdAndIdentityType(
            @Param(value = "categoryCode") String categoryCode, @Param(value = "userId") Long userId, @Param(value = "categorySecond")  String categorySecond , @Param(value = "goodsBrand") String goodsBrand, @Param(value = "goodsType") String goodsType );

    List<GoodsDO> findGoodsListByGidList(@Param(value = "internalGidList") List<Long> internalGidList);

}


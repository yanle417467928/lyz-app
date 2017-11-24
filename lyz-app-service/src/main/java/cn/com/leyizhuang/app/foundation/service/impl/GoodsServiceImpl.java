package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.dao.GoodsDAO;
import cn.com.leyizhuang.app.foundation.dto.GoodsDTO;
import cn.com.leyizhuang.app.foundation.pojo.GoodsPrice;
import cn.com.leyizhuang.app.foundation.pojo.goods.GoodsDO;
import cn.com.leyizhuang.app.foundation.pojo.response.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/9/6
 */
@Service
@Transactional
public class GoodsServiceImpl implements cn.com.leyizhuang.app.foundation.service.GoodsService {

    private GoodsDAO goodsDAO;

    public GoodsServiceImpl(GoodsDAO goodsDAO) {
        this.goodsDAO = goodsDAO;
    }

    /**
     * @param page
     * @param size
     * @return
     * @throws
     * @title 商品分页查询
     * @descripe
     * @author GenerationRoad
     * @date 2017/9/6
     */
    @Override
    public PageInfo<GoodsDO> queryPage(Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<GoodsDO> goodsDOList = goodsDAO.queryList();
        return new PageInfo<>(goodsDOList);
    }

    /**
     * @param goodsDTO
     * @return
     * @throws
     * @title 保存编辑后的商品信息
     * @descripe
     * @author GenerationRoad
     * @date 2017/9/9
     */
    @Override
    public GoodsDO managerSaveGoods(GoodsDTO goodsDTO) {
        GoodsDO goodsDO = transform(goodsDTO);
        // goodsDAO.modify(goodsDO);
        return goodsDO;
    }

    @Override
    public List<UserGoodsResponse> findGoodsListByCategoryCodeAndUserIdAndIdentityType(String categoryCode, Long userId, Integer identityType) {
        if (null != categoryCode && null != userId && null != identityType) {
            if (identityType == 6) {
                return goodsDAO.findGoodsListByCategoryCodeAndCustomerIdAndIdentityType(categoryCode, userId);
            } else {
                return goodsDAO.findGoodsListByCategoryCodeAndEmployeeIdAndIdentityType(categoryCode, userId);
            }
        }
        return null;
    }

    @Override
    public GoodsDO queryById(Long id) {
        if (null != id) {
            return goodsDAO.queryById(id);
        }
        return null;
    }

    @Override
    public void batchRemove(List<Long> longs) {
        if (null != longs && longs.size() > 0) {
            goodsDAO.batchRemove(longs);
        }
    }

    @Override
    public List<GoodsCategoryResponse> findGoodsCategoryListByCategoryCodeAndUserIdAndIdentityType(String categoryCode, Long userId, Integer identityType) {
        if (null != categoryCode && null != userId && null != identityType) {
            if (identityType == 6) {
                return goodsDAO.findGoodsCategoryListByCategoryCodeAndCustomerIdAndIdentityType(categoryCode, userId);
            } else {
                return goodsDAO.findGoodsCategoryListByCategoryCodeAndEmployeeIdAndIdentityType(categoryCode, userId);
            }
        }
        return null;
    }

    @Override
    public List<GoodsBrandResponse> findGoodsBrandListByCategoryCodeAndUserIdAndIdentityType(String categoryCode, Long userId, Integer identityType) {
        if (null != categoryCode && null != userId && null != identityType) {
            if (identityType == 6) {
                return goodsDAO.findGoodsBrandListByCategoryCodeAndCustomerIdAndIdentityType(categoryCode, userId);
            } else {
                return goodsDAO.findGoodsBrandListByCategoryCodeAndEmployeeIdAndIdentityType(categoryCode, userId);
            }
        }
        return null;
    }

    @Override
    public List<GoodsTypeResponse> findGoodsTypeListByCategoryCodeAndUserIdAndIdentityType(String categoryCode, Long userId, Integer identityType) {
        if (null != categoryCode && null != userId && null != identityType) {
            if (identityType == 6) {
                return goodsDAO.findGoodsTypeListByCategoryCodeAndCustomerIdAndIdentityType(categoryCode, userId);
            } else {
                return goodsDAO.findGoodsTypeListByCategoryCodeAndEmployeeIdAndIdentityType(categoryCode, userId);
            }
        }
        return null;
    }

    @Override
    public List<UserGoodsResponse> findGoodsCollectListByUserIdAndIdentityType(Long userId, Integer identityType) {
        if (null != userId && null != identityType) {
            AppIdentityType appIdentityType = AppIdentityType.getAppIdentityTypeByValue(identityType);
            return goodsDAO.findGoodsCollectListByUserIdAndIdentityType(userId, appIdentityType);
        }
        return null;
    }

    @Override
    public List<UserGoodsResponse> findGoodsListByIsHotAndUserIdAndIdentityType(Long userId, Integer identityType) {
        if (null != userId && null != identityType) {
            if (identityType == 6) {
                return goodsDAO.findGoodsListByIsHotAndCustomerIdAndIdentityType(userId);
            } else {
                return goodsDAO.findGoodsListByIsHotAndEmployeeIdAndIdentityType(userId);
            }
        }
        return null;
    }

    @Override
    public List<UserGoodsResponse> findGoodsOftenListByUserIdAndIdentityType(Long userId, Integer identityType) {
        if (null != userId && null != identityType) {
            AppIdentityType appIdentityType = AppIdentityType.getAppIdentityTypeByValue(identityType);
            return goodsDAO.findGoodsOftenListByUserIdAndIdentityType(userId, appIdentityType);
        }
        return null;
    }

    @Override
    public List<UserGoodsResponse> searchByUserIdAndKeywordsAndIdentityType(Long userId, String keywords, Integer identityType) {
        List<UserGoodsResponse> goodsResponseList;
        if (StringUtils.isNotBlank(keywords) && null != userId && null != identityType) {
            if (identityType == AppIdentityType.CUSTOMER.getValue()) {
                goodsResponseList = goodsDAO.searchByCustomerIdAndKeywords(userId, keywords);
            } else {
                goodsResponseList = goodsDAO.searchByEmployeeIdAndKeywords(userId, keywords);
            }
            return goodsResponseList;
        }
        return null;
    }

    @Override
    @Transactional
    public void addCollectGoodsByUserIdAndGoodsIdAndIdentityType(Long userId, Long goodsId, Integer identityType) {
        if (null != userId && null != identityType && null != goodsId) {
            AppIdentityType appIdentityType = AppIdentityType.getAppIdentityTypeByValue(identityType);
            goodsDAO.saveCollectGoodsByUserIdAndGoodsIdAndIdentityType(userId, goodsId, appIdentityType);

        }

    }

    @Override
    @Transactional
    public void removeCollectGoodsByUserIdAndGoodsIdAndIdentityType(Long userId, Long goodsId, Integer identityType) {
        if (null != userId && null != identityType && null != goodsId) {
            AppIdentityType appIdentityType = AppIdentityType.getAppIdentityTypeByValue(identityType);
            goodsDAO.deleteCollectGoodsByUserIdAndGoodsIdAndIdentityType(userId, goodsId, appIdentityType);

        }
    }

    @Override
    public GoodsDO findGoodsById(Long id) {
        if (null != id) {
            return goodsDAO.findGoodsById(id);
        }
        return null;
    }

    @Override
    public List<GoodsPrice> getGoodsPriceByCustomerAndGoodsId(Long userId, List<Long> goodsIds) {
        return this.goodsDAO.getGoodsPriceByCustomerAndGoodsId(userId, goodsIds);
    }

    @Override
    public List<GoodsPrice> getGoodsPriceByEmployeeAndGoodsId(Long userId, List<Long> goodsIds) {
        return this.goodsDAO.getGoodsPriceByEmployeeAndGoodsId(userId, goodsIds);
    }

    @Override
    public List<UserGoodsResponse> filterGoods(Long userId, AppIdentityType type, String firstCategoryCode, Long secondCategoryId, Long brandId, Long typeId,
                                               String specification) {
        if ((null != firstCategoryCode || null != secondCategoryId || null != brandId || null != typeId ||
                null != specification) && null != userId && null != type) {
            if (type.equals(AppIdentityType.CUSTOMER)) {
                return goodsDAO.filterGoodsCustomer(userId, firstCategoryCode, secondCategoryId, brandId, typeId, specification);
            } else {
                return goodsDAO.filterGoodsEmployee(userId, firstCategoryCode, secondCategoryId, brandId, typeId, specification);
            }
        }
        return null;
    }

    @Override
    public List<OrderGoodsSimpleResponse> findGoodsListByEmployeeIdAndGoodsIdList(Long userId, List<Long> goodsIds) {
        if (null != userId && !goodsIds.isEmpty()) {
            return goodsDAO.findGoodsListByEmployeeIdAndGoodsIdList(userId, goodsIds);

        }
        return null;
    }

    @Override
    public List<OrderGoodsSimpleResponse> findGoodsListByCustomerIdAndGoodsIdList(Long userId, List<Long> goodsIds) {
        if (null != userId && !goodsIds.isEmpty()) {
            return goodsDAO.findGoodsListByCustomerIdAndGoodsIdList(userId, goodsIds);

        }
        return null;
    }

    @Override
    public GoodsPrice findGoodsPriceByProductCouponIdAndUserId(Long id, Long cusId, Integer qty) {
        if (null != id && null != cusId) {
            return goodsDAO.findGoodsPriceByProductCouponIdAndUserId(id, cusId, qty);
        }
        return null;
    }

    @Override
    public GoodsDO queryBySku(String sku) {
        return goodsDAO.queryBySku(sku);
    }

    @Override
    public Boolean existGoodsBrandByGoodsIdAndBrandName(Long id, String brandName) {
        if (id != null && StringUtils.isNotBlank(brandName)) {
            return goodsDAO.existGoodsBrandByGoodsIdAndBrandName(id, brandName);
        }
        return null;
    }

    @Override
    public List<String> findCompanyFlagListById(List<Long> goodsIdList) {
        if (null != goodsIdList && !goodsIdList.isEmpty()) {
            return goodsDAO.findCompanyFlagListById(goodsIdList);
        }
        return null;
    }

    @Override
    public void saveSynchronize(GoodsDO goodsDO) {
        if (goodsDO != null){
            goodsDAO.saveSynchronize(goodsDO);
        }
    }

    @Override
    public void modifySynchronize(GoodsDO goodsDO) {
        if (goodsDO != null){
            goodsDAO.modifySynchronize(goodsDO);
        }
    }

    @Override
    public void deleteSynchronize(String sku) {
       goodsDAO.deleteSynchronize(sku);
    }

    /**
     * @param
     * @return
     * @throws
     * @title 获取商品详情
     * @descripe
     * @author GenerationRoad
     * @date 2017/9/29
     */
    @Override
    public GoodsDetailResponse findGoodsDetailByGoodsId(Long userId, Long goodsId, Integer identityType) {
        AppIdentityType appIdentityType = AppIdentityType.getAppIdentityTypeByValue(identityType);
        GoodsDetailResponse goodsDetailResponse = this.goodsDAO.findGoodsDetailByGoodsId(userId, goodsId, appIdentityType);
        if (null != goodsDetailResponse) {
            GoodsDO goodsDO = this.goodsDAO.findGoodsImageUriByGoodsId(goodsId);
            goodsDetailResponse = GoodsDetailResponse.transform(goodsDetailResponse, goodsDO);
        }
        return goodsDetailResponse;
    }

    /**
     * @param goodsDTO
     * @return
     * @throws
     * @title GoodsDTO转GoodsDO
     * @descripe
     * @author GenerationRoad
     * @date 2017/9/9
     */
    private GoodsDO transform(GoodsDTO goodsDTO) {
        GoodsDO goodsDO = this.goodsDAO.queryById(goodsDTO.getId());
        goodsDO.setSkuName(goodsDTO.getGoodsName());
        goodsDO.setSku(goodsDTO.getGoodsCode());
        goodsDO.setBrdId(goodsDTO.getBrandId());
        goodsDO.setBrdName(goodsDTO.getBrandTitle());
        goodsDO.setCoverImageUri(goodsDTO.getCoverImageUri());
        String onSaleTime = goodsDTO.getOnSaleTime();
        return goodsDO;
    }


}

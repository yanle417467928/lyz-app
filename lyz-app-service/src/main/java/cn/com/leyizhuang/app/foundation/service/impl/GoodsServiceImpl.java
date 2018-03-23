package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.dao.GoodsDAO;
import cn.com.leyizhuang.app.foundation.dto.GoodsDTO;
import cn.com.leyizhuang.app.foundation.pojo.GoodsPrice;
import cn.com.leyizhuang.app.foundation.pojo.goods.GoodsDO;
import cn.com.leyizhuang.app.foundation.pojo.management.goods.GoodsBrand;
import cn.com.leyizhuang.app.foundation.pojo.response.*;
import cn.com.leyizhuang.app.foundation.service.GoodsService;
import cn.com.leyizhuang.app.foundation.service.MaGoodsBrandService;
import cn.com.leyizhuang.app.foundation.vo.OrderGoodsVO;
import cn.com.leyizhuang.app.foundation.vo.management.MaBuyProductCouponGoodsResponse;
import cn.com.leyizhuang.app.foundation.vo.management.goods.MaGoodsVO;
import cn.com.leyizhuang.common.util.AssertUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * @author GenerationRoad
 * @date 2017/9/6
 */
@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private GoodsDAO goodsDAO;

    @Autowired
    private MaGoodsBrandService maGoodsBrandService;


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
    @Transactional(rollbackFor = Exception.class)
    public GoodsDO managerSaveGoods(GoodsDTO goodsDTO) {
        GoodsDO goodsDO = transform(goodsDTO);
        // goodsDAO.modify(goodsDO);
        return goodsDO;
    }

    @Override
    public PageInfo<UserGoodsResponse> findGoodsListByCategoryCodeAndUserIdAndIdentityType(String categoryCode, Long userId, Integer identityType, Integer page, Integer size) {
        if (null != categoryCode && null != userId && null != identityType) {
            List<UserGoodsResponse> userGoodsResponseList;
            if (identityType == 6) {
                PageHelper.startPage(page, size);
                userGoodsResponseList = goodsDAO.findGoodsListByCategoryCodeAndCustomerIdAndIdentityType(categoryCode, userId);
                return new PageInfo<>(userGoodsResponseList);
            } else {
                PageHelper.startPage(page, size);
                userGoodsResponseList = goodsDAO.findGoodsListByCategoryCodeAndEmployeeIdAndIdentityType(categoryCode, userId);
                return new PageInfo<>(userGoodsResponseList);
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
    @Transactional(rollbackFor = Exception.class)
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
    public PageInfo<UserGoodsResponse> findGoodsListByIsHotAndUserIdAndIdentityType(Long userId, Integer identityType, Integer page, Integer size) {
        if (null != userId && null != identityType) {
            if (identityType == 6) {
                PageHelper.startPage(page, size);
                List<UserGoodsResponse> userGoodsResponseList = goodsDAO.findGoodsListByIsHotAndCustomerIdAndIdentityType(userId);
                return new PageInfo<>(userGoodsResponseList);
            } else {
                PageHelper.startPage(page, size);
                List<UserGoodsResponse> userGoodsResponseList = goodsDAO.findGoodsListByIsHotAndEmployeeIdAndIdentityType(userId);
                return new PageInfo<>(userGoodsResponseList);
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
    @Transactional(rollbackFor = Exception.class)
    public void addCollectGoodsByUserIdAndGoodsIdAndIdentityType(Long userId, Long goodsId, Integer identityType) {
        if (null != userId && null != identityType && null != goodsId) {
            AppIdentityType appIdentityType = AppIdentityType.getAppIdentityTypeByValue(identityType);
            goodsDAO.saveCollectGoodsByUserIdAndGoodsIdAndIdentityType(userId, goodsId, appIdentityType);

        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
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
    public PageInfo<UserGoodsResponse> filterGoods(Long userId, AppIdentityType type, String firstCategoryCode, Long secondCategoryId, Long brandId, Long typeId,
                                                   String specification, Integer page, Integer size) {
        if ((null != firstCategoryCode || null != secondCategoryId || null != brandId || null != typeId ||
                null != specification) && null != userId && null != type) {
            if (type.equals(AppIdentityType.CUSTOMER)) {
                PageHelper.startPage(page, size);
                List<UserGoodsResponse> list = goodsDAO.filterGoodsCustomer(userId, firstCategoryCode, secondCategoryId, brandId, typeId, specification);
                return new PageInfo<>(list);
            } else {
                PageHelper.startPage(page, size);
                List<UserGoodsResponse> list = goodsDAO.filterGoodsEmployee(userId, firstCategoryCode, secondCategoryId, brandId, typeId, specification);
                return new PageInfo<>(list);
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
        if (goodsDO != null) {
            goodsDAO.saveSynchronize(goodsDO);
        }
    }

    @Override
    public void modifySynchronize(GoodsDO goodsDO) {
        if (goodsDO != null) {
            goodsDAO.modifySynchronize(goodsDO);
        }
    }

    @Override
    public void deleteSynchronize(String sku) {
        goodsDAO.deleteSynchronize(sku);
    }

    @Override
    public List<OrderGoodsVO> findOrderGoodsVOListByUserIdAndIdentityTypeAndGoodsIds(Long userId, Integer identityType, Set<Long> goodsIdSet) {
        if (null != userId && null != identityType && null != goodsIdSet && goodsIdSet.size() > 0) {
            if (identityType == AppIdentityType.CUSTOMER.getValue()) {
                return goodsDAO.findOrderGoodsVOListByCustomerIdAndGoodsIds(userId, goodsIdSet);
            } else if (identityType == AppIdentityType.SELLER.getValue() || identityType == AppIdentityType.DECORATE_MANAGER.getValue()) {
                return goodsDAO.findOrderGoodsVOListByEmpIdAndGoodsIds(userId, goodsIdSet);
            }
        }
        return null;
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
        goodsDO.setBrandName(goodsDTO.getBrandTitle());
        goodsDO.setCoverImageUri(goodsDTO.getCoverImageUri());
        String onSaleTime = goodsDTO.getOnSaleTime();
        return goodsDO;
    }

    /**
     * 根据信息查询商品
     *
     * @param page
     * @param size
     * @param queryGoodsInfo
     * @return
     */
    @Override
    public PageInfo<GoodsDO> queryGoodsPageByInfo(Integer page, Integer size, String queryGoodsInfo) {
        PageHelper.startPage(page, size);
        List<GoodsDO> goodsDOList = goodsDAO.queryGoodsPageByInfo(queryGoodsInfo);
        return new PageInfo<>(goodsDOList);
    }

    /**
     * 根据条件筛选商品
     *
     * @param page
     * @param size
     * @param brandCode
     * @param categoryCode
     * @param companyCode
     * @return
     */
    @Override
    public PageInfo<GoodsDO> screenGoodsGrid(Integer page, Integer size, Long brandCode, String categoryCode, String companyCode) {
        PageHelper.startPage(page, size);
        if ("-1".equals(categoryCode)) {
            categoryCode = null;
        }
        if ("-1".equals(companyCode)) {
            companyCode = null;
        }
        if (-1 == brandCode) {
            brandCode = null;
        }
        List<GoodsDO> goodsDOList = goodsDAO.screenGoodsGrid(brandCode, categoryCode, companyCode);
        return new PageInfo<>(goodsDOList);
    }

    /**
     * 更新商品信息
     *
     * @param goodsVO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateGoods(MaGoodsVO goodsVO) {
        Long brdId = goodsVO.getBrdId();
        if (null != brdId) {
            GoodsBrand goodsBrand = maGoodsBrandService.queryGoodsBrandVOById(brdId);
            goodsVO.setBrdName(goodsBrand.getBrandName());
        }
        GoodsDO goodsDO = GoodsDO.transform(goodsVO);
        goodsDAO.updateGoods(goodsDO);
    }

    /**
     * 判断是否存在商品名称
     *
     * @param skuName
     * @param id
     * @return
     */
    @Override
    public Boolean isExistSkuName(String skuName, Long id) {
        return goodsDAO.isExistSkuName(skuName, id);
    }

    /**
     * 判断是否存在排序id
     *
     * @param sortId
     * @param id
     * @return
     */
    @Override
    public Boolean isExistSortId(Long sortId, Long id) {
        return goodsDAO.isExistSortId(sortId, id);
    }

    @Override
    public List<MaBuyProductCouponGoodsResponse> findMaStoreGoodsByStoreId(Long storeId) {
        return goodsDAO.findMaStoreGoodsByStoreId(storeId);
    }

    @Override
    public List<MaBuyProductCouponGoodsResponse> screenMaGoodsGrid(Long storeId, Long brandCode, String categoryCode, String companyCode) {
        return goodsDAO.screenMaGoodsGrid(storeId, brandCode, categoryCode, companyCode);
    }

    @Override
    public List<MaBuyProductCouponGoodsResponse> queryGoodsPageByStoreIdAndInfo(Long storeId, String queryGoodsInfo) {
        return goodsDAO.queryGoodsPageByStoreIdAndInfo(storeId, queryGoodsInfo);
    }

    @Override
    public PageInfo<UserGoodsResponse> findGoodsListByCustomerIdAndIdentityTypeAndUserRank(Long userId, AppIdentityType identityType, Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<UserGoodsResponse> list = goodsDAO.findGoodsListByCustomerIdAndIdentityTypeAndUserRank(userId, identityType);
        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<GoodsDO> getGoodsBykeywordsAndCompanyAndBrandCodeAndCategoryCodeAndStoreId(Integer page, Integer size, String keywords, String companyCode, Long brandCode,
                                                                                               String categoryCode, Long storeId) {
        PageHelper.startPage(page, size);
        List<GoodsDO> list = goodsDAO.getGoodsBykeywordsAndCompanyAndBrandCodeAndCategoryCodeAndStoreId(keywords, companyCode, brandCode, categoryCode, storeId);
        return new PageInfo<>(list);
    }

    @Override
    public List<String> getGoodsSkuNameListByGoodsIdList(List<Long> noPriceGoodsIdList) {
        if (AssertUtil.isNotEmpty(noPriceGoodsIdList)) {
            return goodsDAO.getGoodsSkuNameListByGoodsIdList(noPriceGoodsIdList);
        }
        return null;
    }


}

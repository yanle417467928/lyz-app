package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.constant.AppUserType;
import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.dao.GoodsDAO;
import cn.com.leyizhuang.app.foundation.pojo.GoodsDO;
import cn.com.leyizhuang.app.foundation.dto.GoodsDTO;
import cn.com.leyizhuang.app.foundation.pojo.response.GoodsBrandResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.GoodsCategoryResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.GoodsTypeResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.UserGoodsResponse;
import cn.com.leyizhuang.app.foundation.vo.GoodsVO;
import cn.com.leyizhuang.app.foundation.service.IGoodsService;
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
public class GoodsService implements IGoodsService {

    private GoodsDAO goodsDAO;

    public GoodsService(GoodsDAO goodsDAO) {
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
    public List<GoodsVO> findGoodsListByCategoryCodeAndUserIdAndIdentityType(String categoryCode, Long userId, Integer identityType) {
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
        if (identityType == 6) {
            return goodsDAO.findGoodsCollectListByCustomerIdAndIdentityType(userId);
        } else {
            return goodsDAO.findGoodsCollectListByEmployeeIdAndIdentityType(userId);
        }
    }

    @Override
    public List<UserGoodsResponse> findGoodsListByIsHotAndUserIdAndIdentityType(Long userId, Integer identityType) {
        if (identityType == 6) {
            return goodsDAO.findGoodsListByIsHotAndCustomerIdAndIdentityType(userId);
        } else {
            return goodsDAO.findGoodsListByIsHotAndEmployeeIdAndIdentityType(userId);
        }
    }

    @Override
    public List<UserGoodsResponse> findGoodsOftenListByUserIdAndIdentityType(Long userId, Integer identityType) {
        if (identityType == 6) {
            return goodsDAO.findGoodsOftenListByCustomerIdAndIdentityType(userId);
        } else {
            return goodsDAO.findGoodsOftenListByEmployeeIdAndIdentityType(userId);
        }
    }

    @Override
    public List<UserGoodsResponse> searchByUserIdAndKeywordsAndIdentityType(Long userId, String keywords, Integer identityType) {
        List<UserGoodsResponse> goodsResponseList;
        if (StringUtils.isNotBlank(keywords) && null != userId && null != identityType) {
            if (identityType == AppUserType.CUSTOMER.getValue()) {
                goodsResponseList = goodsDAO.searchByCustomerIdAndKeywords(userId, keywords);
            } else {
                goodsResponseList = goodsDAO.searchByEmployeeIdAndKeywords(userId, keywords);
            }
            return goodsResponseList;
        }
        return null;
    }

    /**
     * @param goodsCode
     * @return
     * @throws
     * @title 根据商品编码获取商品图片
     * @descripe
     * @author GenerationRoad
     * @date 2017/9/29
     */
    @Override
    public GoodsDO findGoodsImageUriByGoodsCode(String goodsCode) {
        return this.goodsDAO.findGoodsImageUriByGoodsCode(goodsCode);
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
        goodsDO.setGoodsName(goodsDTO.getGoodsName());
        goodsDO.setGoodsCode(goodsDTO.getGoodsCode());
        goodsDO.setBrandId(goodsDTO.getBrandId());
        goodsDO.setCategoryId(goodsDTO.getCategoryId());
        goodsDO.setCoverImageUri(goodsDTO.getCoverImageUri());
        String onSaleTime = goodsDTO.getOnSaleTime();
        return goodsDO;
    }


}

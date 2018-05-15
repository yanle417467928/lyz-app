package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.MaGoodsDAO;
import cn.com.leyizhuang.app.foundation.pojo.goods.GoodsDO;
import cn.com.leyizhuang.app.foundation.pojo.response.GoodsBrandResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.GoodsSpecificationResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.GoodsTypeResponse;
import cn.com.leyizhuang.app.foundation.service.MaGoodsService;
import cn.com.leyizhuang.app.foundation.vo.management.goods.GoodsResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2018/1/30
 */
@Service
@Transactional
public class MaGoodsServiceImpl implements MaGoodsService {

    @Autowired
    private MaGoodsDAO maGoodsDAO;

    @Override
    public List<GoodsResponseVO> findGoodsByCidAndCusId(Long cusId, List<Long> cids) {
        return this.maGoodsDAO.findGoodsByCidAndCusId(cusId, cids);
    }

    @Override
    public List<GoodsResponseVO> findGoodsByCidAndEmpId(Long empId, List<Long> cids) {
        return this.maGoodsDAO.findGoodsByCidAndEmpId(empId, cids);
    }

    @Override
    public List<GoodsResponseVO> findGoodsByMultiConditionQueryAndCusId(Long cusId, Long cids, String categoryType, Long brandString, String specificationString, Long goodsTypeString) {
        return this.maGoodsDAO.findGoodsByMultiConditionQueryAndCusId(cusId, cids, categoryType, brandString, specificationString, goodsTypeString);
    }

    @Override
    public List<GoodsResponseVO> findGoodsByMultiConditionQueryAndEmpId(Long empId, Long cids, String categoryType, Long brandString, String specificationString, Long goodsTypeString) {
        return this.maGoodsDAO.findGoodsByMultiConditionQueryAndEmpId(empId, cids, categoryType, brandString, specificationString, goodsTypeString);
    }

    @Override
    public GoodsDO findGoodsById(Long id) {
        if (null != id) {
            return maGoodsDAO.findGoodsById(id);
        }
        return null;
    }

    @Override
    public List<GoodsResponseVO> findGoodsByCidAndCusIdAndUserRank(Long cusId) {
        return this.maGoodsDAO.findGoodsByCidAndCusIdAndUserRank(cusId);
    }

    @Override
    public List<GoodsBrandResponse> findGoodsBrandListByCategoryCodeAndUserIdAndIdentityType(String categoryCode, Long userId, Integer identityType, String categorySecond, String specification, String goodType) {
        if (null != categoryCode && null != userId && null != identityType) {
            if (identityType == 6) {
                return maGoodsDAO.findGoodsBrandListByCategoryCodeAndCustomerIdAndIdentityType(categoryCode, userId, categorySecond, specification, goodType);
            } else {
                return maGoodsDAO.findGoodsBrandListByCategoryCodeAndEmployeeIdAndIdentityType(categoryCode, userId, categorySecond, specification, goodType);
            }
        }
        return null;
    }

    @Override
    public List<GoodsTypeResponse> findGoodsTypeListByCategoryCodeAndUserIdAndIdentityType(String categoryCode, Long userId, Integer identityType, String categorySecond, String specification, String goodsBrand) {
        if (null != categoryCode && null != userId && null != identityType) {
            if (identityType == 6) {
                return maGoodsDAO.findGoodsTypeListByCategoryCodeAndCustomerIdAndIdentityType(categoryCode, userId, categorySecond, specification, goodsBrand);
            } else {
                return maGoodsDAO.findGoodsTypeListByCategoryCodeAndEmployeeIdAndIdentityType(categoryCode, userId, categorySecond, specification, goodsBrand);
            }
        }
        return null;
    }

    @Override
    public List<GoodsSpecificationResponse> findGoodsSpecificationListByCategoryCodeAndUserIdAndIdentityType(String categoryCode, Long userId, Integer identityType, String categorySecond, String goodsBrand, String goodType) {
        if (null != categoryCode && null != userId && null != identityType) {
            if (identityType == 6) {
                return maGoodsDAO.findGoodsSpecificationListByCategoryCodeAndCustomerIdAndIdentityType(categoryCode, userId, categorySecond, goodsBrand, goodType);
            } else {
                return maGoodsDAO.findGoodsSpecificationListByCategoryCodeAndEmployeeIdAndIdentityType(categoryCode, userId, categorySecond, goodsBrand, goodType);
            }
        }
        return null;
    }
}

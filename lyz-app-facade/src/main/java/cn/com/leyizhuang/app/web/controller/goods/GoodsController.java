package cn.com.leyizhuang.app.web.controller.goods;

import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.pojo.GoodsCategory;
import cn.com.leyizhuang.app.foundation.pojo.response.GoodsBrandResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.GoodsCategoryResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.GoodsTypeResponse;
import cn.com.leyizhuang.app.foundation.pojo.vo.GoodsVO;
import cn.com.leyizhuang.app.foundation.service.IGoodsService;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 商品接口
 *
 * @author Richard
 * Created on 2017-09-25 10:17
 **/
@RestController
@RequestMapping(value = "/app/goods/")
public class GoodsController {

    private static final Logger logger = LoggerFactory.getLogger(GoodsController.class);

    @Autowired
    private IGoodsService goodsService;

    /**
     * @param categoryCode 一级分类id
     * @param userId 用户id
     * @param identityType 用户身份类型
     * @return resultDTO
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ResultDTO<Object> getGoodsListByUserIdAndIdentityType(String categoryCode, Long userId, Integer identityType) {
        ResultDTO<Object> resultDTO;
        logger.info("getGoodsListByUserIdAndIdentityType CALLED,获取商品列表，入参 categoryCode:{},userId:{},identityType:{}", categoryCode, userId, identityType);
        if (StringUtils.isBlank(categoryCode)) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "一级分类编码不能为空!", null);
            logger.info("getGoodsListByUserIdAndIdentityType OUT,获取商品列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空", null);
            logger.info("getGoodsListByUserIdAndIdentityType OUT,获取商品列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户身份不能为空", null);
            logger.info("getGoodsListByUserIdAndIdentityType OUT,获取商品列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            List<GoodsVO> goodsVOList = goodsService.findGoodsListByCategoryCodeAndUserIdAndIdentityType(categoryCode,userId, identityType);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, goodsVOList);
            logger.info("getGoodsListByUserIdAndIdentityType OUT,获取商品列表成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，商品列表获取失败", null);
            logger.warn("getGoodsListByUserIdAndIdentityType EXCEPTION,获取商品列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
    }

    /**
     * @param categoryCode 一级分类id
     * @param userId 用户id
     * @param identityType 用户身份类型
     * @return resultDTO
     */
    @RequestMapping(value = "/category/list", method = RequestMethod.POST)
    public ResultDTO<Object> getGoodsCategoryListByUserIdAndIdentityType(String categoryCode, Long userId, Integer identityType) {
        ResultDTO<Object> resultDTO;
        logger.info("getGoodsCategoryListByUserIdAndIdentityType CALLED,获取商品分类列表，入参 categoryCode:{},userId:{},identityType:{}", categoryCode, userId, identityType);
        if (StringUtils.isBlank(categoryCode)) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "一级分类编码不能为空!", null);
            logger.info("getGoodsCategoryListByUserIdAndIdentityType OUT,获取商品分类列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空", null);
            logger.info("getGoodsCategoryListByUserIdAndIdentityType OUT,获取商品分类列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户身份不能为空", null);
            logger.info("getGoodsCategoryListByUserIdAndIdentityType OUT,获取商品分类列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            List<GoodsCategoryResponse> categoryList = goodsService.findGoodsCategoryListByCategoryCodeAndUserIdAndIdentityType(categoryCode,userId, identityType);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, categoryList);
            logger.info("getGoodsCategoryListByUserIdAndIdentityType OUT,获取商品分类列表成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("{}",e);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，商品分类列表获取失败", null);
            logger.warn("getGoodsCategoryListByUserIdAndIdentityType EXCEPTION,获取商品分类列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
    }

    /**
     * @param categoryCode 一级分类id
     * @param userId 用户id
     * @param identityType 用户身份类型
     * @return resultDTO
     */
    @RequestMapping(value = "/brand/list", method = RequestMethod.POST)
    public ResultDTO<Object> getGoodsBrandListByUserIdAndIdentityType(String categoryCode, Long userId, Integer identityType) {
        ResultDTO<Object> resultDTO;
        logger.info("getGoodsBrandListByUserIdAndIdentityType CALLED,获取商品品牌列表，入参 categoryCode:{},userId:{},identityType:{}", categoryCode, userId, identityType);
        if (StringUtils.isBlank(categoryCode)) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "一级分类编码不能为空!", null);
            logger.info("getGoodsBrandListByUserIdAndIdentityType OUT,获取商品品牌列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空", null);
            logger.info("getGoodsBrandListByUserIdAndIdentityType OUT,获取商品品牌列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户身份不能为空", null);
            logger.info("getGoodsBrandListByUserIdAndIdentityType OUT,获取商品品牌列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            List<GoodsBrandResponse> brandList = goodsService.findGoodsBrandListByCategoryCodeAndUserIdAndIdentityType(categoryCode,userId, identityType);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, brandList);
            logger.info("getGoodsBrandListByUserIdAndIdentityType OUT,获取商品品牌列表成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，商品品牌列表获取失败", null);
            logger.warn("getGoodsBrandListByUserIdAndIdentityType EXCEPTION,获取商品品牌列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
    }

    /**
     * @param categoryCode 一级分类id
     * @param userId 用户id
     * @param identityType 用户身份类型
     * @return resultDTO
     */
    @RequestMapping(value = "/type/list", method = RequestMethod.POST)
    public ResultDTO<Object> getGoodsTypeListByUserIdAndIdentityType(String categoryCode, Long userId, Integer identityType) {
        ResultDTO<Object> resultDTO;
        logger.info("getGoodsTypeListByUserIdAndIdentityType CALLED,获取商品类型列表，入参 categoryCode:{},userId:{},identityType:{}", categoryCode, userId, identityType);
        if (StringUtils.isBlank(categoryCode)) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "一级分类编码不能为空!", null);
            logger.info("getGoodsTypeListByUserIdAndIdentityType OUT,获取商品类型列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空", null);
            logger.info("getGoodsTypeListByUserIdAndIdentityType OUT,获取商品类型列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户身份不能为空", null);
            logger.info("getGoodsTypeListByUserIdAndIdentityType OUT,获取商品类型列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            List<GoodsTypeResponse> brandList = goodsService.findGoodsTypeListByCategoryCodeAndUserIdAndIdentityType(categoryCode,userId, identityType);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, brandList);
            logger.info("getGoodsTypeListByUserIdAndIdentityType OUT,获取商品类型列表成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，商品分类列表获取失败", null);
            logger.warn("getGoodsTypeListByUserIdAndIdentityType EXCEPTION,获取商品类型列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
    }
}
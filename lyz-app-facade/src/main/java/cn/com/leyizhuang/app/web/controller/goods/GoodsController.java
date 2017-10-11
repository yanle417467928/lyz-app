package cn.com.leyizhuang.app.web.controller.goods;

import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.pojo.GoodsDO;
import cn.com.leyizhuang.app.foundation.pojo.response.*;
import cn.com.leyizhuang.app.foundation.service.IGoodsService;
import cn.com.leyizhuang.app.foundation.vo.GoodsVO;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 商品接口
 *
 * @author Richard
 *         Created on 2017-09-25 10:17
 **/
@RestController
@RequestMapping(value = "/app/goods")
public class GoodsController {

    private static final Logger logger = LoggerFactory.getLogger(GoodsController.class);

    @Resource
    private IGoodsService goodsService;

    /**
     * 获取商品列表
     *
     * @param categoryCode 一级分类id
     * @param userId       用户id
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
            List<GoodsVO> goodsVOList = goodsService.findGoodsListByCategoryCodeAndUserIdAndIdentityType(categoryCode, userId, identityType);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, goodsVOList);
            logger.info("getGoodsListByUserIdAndIdentityType OUT,获取商品列表成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，商品列表获取失败", null);
            logger.warn("getGoodsListByUserIdAndIdentityType EXCEPTION,获取商品列表失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    /**
     * 获取商品分类列表
     *
     * @param categoryCode 一级分类id
     * @param userId       用户id
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
            List<GoodsCategoryResponse> categoryList = goodsService.findGoodsCategoryListByCategoryCodeAndUserIdAndIdentityType(categoryCode, userId, identityType);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, categoryList);
            logger.info("getGoodsCategoryListByUserIdAndIdentityType OUT,获取商品分类列表成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("{}", e);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，商品分类列表获取失败", null);
            logger.warn("getGoodsCategoryListByUserIdAndIdentityType EXCEPTION,获取商品分类列表失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    /**
     * 获取商品品牌列表
     *
     * @param categoryCode 一级分类id
     * @param userId       用户id
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
            List<GoodsBrandResponse> brandList = goodsService.findGoodsBrandListByCategoryCodeAndUserIdAndIdentityType(categoryCode, userId, identityType);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, brandList);
            logger.info("getGoodsBrandListByUserIdAndIdentityType OUT,获取商品品牌列表成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，商品品牌列表获取失败", null);
            logger.warn("getGoodsBrandListByUserIdAndIdentityType EXCEPTION,获取商品品牌列表失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    /**
     * 获取商品类型列表
     *
     * @param categoryCode 一级分类id
     * @param userId       用户id
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
            List<GoodsTypeResponse> brandList = goodsService.findGoodsTypeListByCategoryCodeAndUserIdAndIdentityType(categoryCode, userId, identityType);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, brandList);
            logger.info("getGoodsTypeListByUserIdAndIdentityType OUT,获取商品类型列表成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，商品分类列表获取失败", null);
            logger.warn("getGoodsTypeListByUserIdAndIdentityType EXCEPTION,获取商品类型列表失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    /**
     * 获取热门商品信息
     *
     * @param userId       用户id
     * @param identityType 用户身份类型
     * @return ResultDTO
     */
    @RequestMapping(value = "/hot/list", method = RequestMethod.POST)
    public ResultDTO<Object> getGoodsHotListByUserIdAndIdentityType(Long userId, Integer identityType) {
        ResultDTO<Object> resultDTO;
        logger.info("getGoodsHotListByUserIdAndIdentityType CALLED,获取热门商品列表，入参 userId:{},identityType:{}", userId, identityType);
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空", null);
            logger.info("getGoodsHotListByUserIdAndIdentityType OUT,获取热门商品列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户身份不能为空", null);
            logger.info("getGoodsHotListByUserIdAndIdentityType OUT,获取热门商品列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            List<UserGoodsResponse> userGoodsResponseList = goodsService.findGoodsListByIsHotAndUserIdAndIdentityType(userId, identityType);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, userGoodsResponseList);
            logger.warn("getGoodsHotListByUserIdAndIdentityType OUT,获取热门商品列表成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;

        } catch (Exception e) {
            e.getStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，获取热门商品列表失败", null);
            logger.warn("getGoodsHotListByUserIdAndIdentityType EXCEPTION,获取热门商品列表失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    /**
     * 获取常购商品列表
     *
     * @param userId       用户ID
     * @param identityType 用户类型
     * @return ResultDTO
     */
    @RequestMapping(value = "/often/list", method = RequestMethod.POST)
    public ResultDTO<Object> getGoodsOftenBuyListByUserIdAndIdentityType(Long userId, Integer identityType) {

        logger.info("getGoodsOftenBuyListByUserIdAndIdentityType CALLED,获取收藏商品列表，入参 userId {},identityType{}", userId, identityType);

        ResultDTO<Object> resultDTO;

        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空", null);
            logger.info("getGoodsOftenBuyListByUserIdAndIdentityType OUT,获取常购商品列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户身份不能为空", null);
            logger.info("getGoodsOftenBuyListByUserIdAndIdentityType OUT,获取常购商品列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if(identityType == 1){
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "该用户类型没有此功能", null);
            logger.info("getGoodsOftenBuyListByUserIdAndIdentityType OUT,获取常购商品列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            List<UserGoodsResponse> userGoodsResponseList = goodsService.findGoodsOftenListByUserIdAndIdentityType(userId, identityType);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, userGoodsResponseList);
            logger.info("getGoodsOftenBuyListByUserIdAndIdentityType OUT,获取常购商品列表成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，获取常购商品列表失败", null);
            logger.warn("getGoodsOftenBuyListByUserIdAndIdentityType EXCEPTION,获取常购商品列表失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }


    /**
     * @param goodsCode 商品编码
     * @return
     * @throws
     * @title 获取商品详情图片和轮播图片
     * @descripe
     * @author GenerationRoad
     * @date 2017/9/29
     */
    @PostMapping(value = "/get/goodsImageUri", produces = "application/json;charset=UTF-8")
    public ResultDTO<GoodsImageUriResponse> getGoodsImageUri(String goodsCode) {
        logger.info("getGoodsImageUri CALLED,获取商品详情图片和轮播图片，入参 goodsCode {},type{}", goodsCode);

        ResultDTO<GoodsImageUriResponse> resultDTO;
        if (null == goodsCode) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "goodsCode！", null);
            logger.info("getGoodsImageUri OUT,获取商品详情图片和轮播图片，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        GoodsDO goodsDO = this.goodsService.findGoodsImageUriByGoodsCode(goodsCode);
        GoodsImageUriResponse goodsImageUriResponse = GoodsImageUriResponse.transform(goodsDO);
        resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, goodsImageUriResponse);
        logger.info("getGoodsImageUri OUT,获取商品详情图片和轮播图片，出参 resultDTO:{}", resultDTO);
        return resultDTO;
    }

    @PostMapping(value = "/search", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> searchGoodsList(String keywords, Long userId, Integer identityType) {
        logger.info("searchGoodsList CALLED,搜索商品，入参 keywords {},userId{}", keywords, userId);
        ResultDTO<Object> resultDTO;
        try {
            if (StringUtils.isBlank(keywords)) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "商品编码{goodsCode}不能为空！", null);
                logger.info("searchGoodsList OUT,搜索商品失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == userId) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id{userId}不能为空！", null);
                logger.info("searchGoodsList OUT,搜索商品失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            List<UserGoodsResponse> goodsResponseList = goodsService.searchByUserIdAndKeywordsAndIdentityType(userId, keywords, identityType);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null,
                    (goodsResponseList != null && goodsResponseList.size() > 0) ? goodsResponseList : null);
            logger.info("searchGoodsList OUT,搜索商品成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，搜索商品失败", null);
            logger.warn("searchGoodsList EXCEPTION,搜索商品失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }

    }
}

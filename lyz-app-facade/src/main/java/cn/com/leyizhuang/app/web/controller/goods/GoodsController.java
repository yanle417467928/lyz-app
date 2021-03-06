package cn.com.leyizhuang.app.web.controller.goods;

import cn.com.leyizhuang.app.core.bean.GridDataVO;
import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.pojo.response.*;
import cn.com.leyizhuang.app.foundation.service.AppCustomerService;
import cn.com.leyizhuang.app.foundation.service.GoodsService;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import com.github.pagehelper.PageInfo;
import com.sun.xml.bind.v2.schemagen.xmlschema.Appinfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
 * Created on 2017-09-25 10:17
 **/
@RestController
@RequestMapping(value = "/app/goods")
public class GoodsController {

    private static final Logger logger = LoggerFactory.getLogger(GoodsController.class);

    @Resource
    private GoodsService goodsService;

    @Autowired
    private AppCustomerService appCustomerService;

    /**
     * 获取商品列表
     *
     * @param categoryCode 一级分类id
     * @param userId       用户id
     * @param identityType 用户身份类型
     * @return resultDTO
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ResultDTO<Object> getGoodsListByUserIdAndIdentityType(String categoryCode, Long userId, Integer identityType,Integer page, Integer size) {
        ResultDTO<Object> resultDTO;
        logger.info("getGoodsListByUserIdAndIdentityType CALLED,获取商品列表，入参 categoryCode:{},userId:{},identityType:{},page:{},size:{}", categoryCode, userId, identityType,page,size);
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
        if (null == page) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "页码不能为空",
                    null);
            logger.info("getGoodsListByUserIdAndIdentityType OUT,获取商品列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == size) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "单页显示条数不能为空",
                    null);
            logger.info("getGoodsListByUserIdAndIdentityType OUT,获取商品列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            PageInfo<UserGoodsResponse> goodsVOList = goodsService.findGoodsListByCategoryCodeAndUserIdAndIdentityType(categoryCode, userId, identityType, page, size);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null,new GridDataVO<UserGoodsResponse>().transform(goodsVOList));
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
    public ResultDTO<Object> getGoodsCategoryListByUserIdAndIdentityType(String categoryCode, Long userId, Integer identityType,String goodsBrand,String specification,String goodType) {
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
            List<GoodsCategoryResponse> categoryList = goodsService.findGoodsCategoryListByCategoryCodeAndUserIdAndIdentityType(categoryCode, userId, identityType,goodsBrand,specification,goodType);
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
    public ResultDTO<Object> getGoodsBrandListByUserIdAndIdentityType(String categoryCode, Long userId, Integer identityType,String categorySecond,String specification,String goodType) {
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
            List<GoodsBrandResponse> brandList = goodsService.findGoodsBrandListByCategoryCodeAndUserIdAndIdentityType(categoryCode, userId, identityType,categorySecond,specification,goodType);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null,
                    (brandList != null && brandList.size() > 0) ? brandList : null);
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
     * 获取商品规格列表
     *
     * @param categoryCode 一级分类id
     * @param userId       用户id
     * @param identityType 用户身份类型
     * @return resultDTO
     */
    @RequestMapping(value = "/specification/list", method = RequestMethod.POST)
    public ResultDTO<Object> getGoodsSpecificationListByUserIdAndIdentityType(String categoryCode, Long userId, Integer identityType, String categorySecond, String goodsBrand, String goodType) {
        ResultDTO<Object> resultDTO;
        logger.info("getGoodsSpecificationListByUserIdAndIdentityType CALLED,获取商品规格列表，入参 categoryCode:{},userId:{},identityType:{}", categoryCode, userId, identityType);
        if (StringUtils.isBlank(categoryCode)) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "一级分类编码不能为空!", null);
            logger.info("getGoodsSpecificationListByUserIdAndIdentityType OUT,获取商品规格列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空", null);
            logger.info("getGoodsSpecificationListByUserIdAndIdentityType OUT,获取商品规格列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户身份不能为空", null);
            logger.info("getGoodsSpecificationListByUserIdAndIdentityType OUT,获取商品规格列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            List<GoodsSpecificationResponse> specificationList = goodsService.findGoodsSpecificationListByCategoryCodeAndUserIdAndIdentityType(categoryCode, userId, identityType,categorySecond,goodsBrand,goodType);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null,
                    (specificationList != null && specificationList.size() > 0) ? specificationList : null);
            logger.info("getGoodsSpecificationListByUserIdAndIdentityType OUT,获取商品规格列表成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，商品品牌列表获取失败", null);
            logger.warn("getGoodsSpecificationListByUserIdAndIdentityType EXCEPTION,获取商品规格列表失败，出参 resultDTO:{}", resultDTO);
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
    public ResultDTO<Object> getGoodsTypeListByUserIdAndIdentityType(String categoryCode, Long userId, Integer identityType, String categorySecond, String specification, String goodsBrand) {
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
            List<GoodsTypeResponse> brandList = goodsService.findGoodsTypeListByCategoryCodeAndUserIdAndIdentityType(categoryCode, userId, identityType,categorySecond,specification,goodsBrand);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null,
                    (brandList != null && brandList.size() > 0) ? brandList : null);
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
    public ResultDTO<Object> getGoodsHotListByUserIdAndIdentityType(Long userId, Integer identityType,Integer page, Integer size) {
        ResultDTO<Object> resultDTO;
        logger.info("getGoodsHotListByUserIdAndIdentityType CALLED,获取热门商品列表，入参 userId:{},identityType:{},page:{},size:{}", userId, identityType,page,size);
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
        if (null == page) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "页码不能为空",
                    null);
            logger.info("getGoodsHotListByUserIdAndIdentityType OUT,获取热门商品列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == size) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "单页显示条数不能为空",
                    null);
            logger.info("getGoodsHotListByUserIdAndIdentityType OUT,获取热门商品列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            PageInfo<UserGoodsResponse> userGoodsResponseList = goodsService.findGoodsListByIsHotAndUserIdAndIdentityType(userId, identityType, page, size);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null,
                    (null!=userGoodsResponseList&&null!=userGoodsResponseList.getList()&&userGoodsResponseList.getList().size() > 0) ? new GridDataVO<UserGoodsResponse>().transform(userGoodsResponseList) : null);
            logger.info("getGoodsHotListByUserIdAndIdentityType OUT,获取热门商品列表成功，出参 resultDTO:{}", resultDTO);
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
        if (identityType == 1) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "该用户类型没有此功能", null);
            logger.info("getGoodsOftenBuyListByUserIdAndIdentityType OUT,获取常购商品列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            List<UserGoodsResponse> userGoodsResponseList = goodsService.findGoodsOftenListByUserIdAndIdentityType(userId, identityType);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null,
                    (userGoodsResponseList != null && userGoodsResponseList.size() > 0) ? userGoodsResponseList : null);
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
     * @param
     * @return
     * @throws
     * @title 获取商品详情
     * @descripe
     * @author GenerationRoad
     * @date 2017/9/29
     */
    @PostMapping(value = "/get/goodsDetail", produces = "application/json;charset=UTF-8")
    public ResultDTO<GoodsDetailResponse> getGoodsDetail(Long userId, Integer identityType, Long goodsId) {
        logger.info("getGoodsDetail CALLED,获取商品详情，入参 userId{}, goodsId {},identityType{}", userId, goodsId, identityType);

        ResultDTO<GoodsDetailResponse> resultDTO;
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空", null);
            logger.info("getGoodsDetail OUT,获取常购商品列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户身份不能为空", null);
            logger.info("getGoodsDetail OUT,获取常购商品列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == goodsId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "goodsId不能为null！", null);
            logger.info("getGoodsDetail OUT,获取商品详情，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        GoodsDetailResponse goodsDetailResponse = this.goodsService.findGoodsDetailByGoodsId(userId, goodsId, identityType);
        resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, goodsDetailResponse);
        logger.info("getGoodsDetail OUT,获取商品详情成功，出参 resultDTO:{}", resultDTO);
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

    /**
     * @param userId            用户ID
     * @param identityType      身份类型
     * @param firstCategoryCode 一级分类编码
     * @param categoryId        二级分类ID
     * @param brandId           品牌ID
     * @param typeId            类型ID
     * @param specification     规格
     * @return 商品列表
     */
    @PostMapping(value = "/filter", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> filterGoodsList(Long userId, Integer identityType, String firstCategoryCode, Long categoryId,
                                             Long brandId, Long typeId, String specification, Integer page, Integer size) {
        logger.info("filterGoodsList CALLED,筛选商品，入参 userId:{},identityType:{},firstCategoryCode: {},categoryId:{}," +
                        "brandId: {},typeId:{},specification:{},page:{},size:{}", userId, identityType, firstCategoryCode, categoryId, brandId,
                typeId, specification, page, size);
        ResultDTO<Object> resultDTO;
        try {
            PageInfo<UserGoodsResponse> goodsResponseList = goodsService.filterGoods(userId,
                    AppIdentityType.getAppIdentityTypeByValue(identityType), firstCategoryCode, categoryId,
                    brandId, typeId, specification, page, size);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null,
                    (goodsResponseList != null && goodsResponseList.getList().size() > 0) ? goodsResponseList : null);
            logger.info("filterGoodsList OUT,筛选商品列表成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，筛选商品失败", null);
            logger.warn("filterGoodsList EXCEPTION,筛选商品失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    /**
     * @title   获取会员专供商品列表
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2018/3/6
     */
    @RequestMapping(value = "/rank/list", method = RequestMethod.POST)
    public ResultDTO<Object> getCustomerRankGoodsListByUserIdAndIdentityType(Long userId, Integer identityType, String keywords,
                                                                             String firstCategoryCode, Long categoryId, Long brandId, Long typeId,
                                                                             String specification,Integer page, Integer size) {
        ResultDTO<Object> resultDTO;
        logger.info("getCustomerRankGoodsListByUserIdAndIdentityType CALLED,获取会员专供商品列表，入参userId:{},identityType:{},page:{},size:{}", userId, identityType,page,size);

        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空", null);
            logger.info("getCustomerRankGoodsListByUserIdAndIdentityType OUT,获取会员专供商品列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        //只有顾客身份才能购买专供商品
        if (null == identityType || identityType != 6) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "此用户身份不支持此功能！", null);
            logger.info("getCustomerRankGoodsListByUserIdAndIdentityType OUT,获取会员专供商品列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == page) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "页码不能为空",
                    null);
            logger.info("getCustomerRankGoodsListByUserIdAndIdentityType OUT,获取会员专供商品列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == size) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "单页显示条数不能为空",
                    null);
            logger.info("getCustomerRankGoodsListByUserIdAndIdentityType OUT,获取会员专供商品列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {

            CustomerRankInfoResponse rankInfo = this.appCustomerService.findCusRankinfoByCusId(userId);
            if (null == rankInfo){
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "此用户身份不支持此功能！", null);
                logger.info("getCustomerRankGoodsListByUserIdAndIdentityType OUT,获取会员专供商品列表失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }

            PageInfo<UserGoodsResponse> goodsVOList = goodsService.findGoodsListByCustomerIdAndIdentityTypeAndUserRank(userId,
                    AppIdentityType.getAppIdentityTypeByValue(identityType), firstCategoryCode, categoryId, brandId, typeId, specification,keywords, page, size);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null,new GridDataVO<UserGoodsResponse>().transform(goodsVOList));
            logger.info("getCustomerRankGoodsListByUserIdAndIdentityType OUT,获取会员专供商品列表成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，获取会员专供商品列表失败", null);
            logger.warn("getCustomerRankGoodsListByUserIdAndIdentityType EXCEPTION,获取会员专供商品列表失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }
    /**
     * @title   获取装饰公司商品专供商品列表
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2018/3/6
     */
    @RequestMapping(value = "/zs/rank/list", method = RequestMethod.POST)
    public ResultDTO<Object> getZsRankGoodsListByUserIdAndIdentityType(Long userId, Integer identityType, String keywords,
                                                                             String firstCategoryCode, Long categoryId, Long brandId, Long typeId,
                                                                             String specification,Integer page, Integer size) {
        ResultDTO<Object> resultDTO;
        logger.info("getZsRankGoodsListByUserIdAndIdentityType CALLED,获取装饰公司商品专供商品列表，入参userId:{},identityType:{},page:{},size:{}", userId, identityType,page,size);

        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空", null);
            logger.info("getZsRankGoodsListByUserIdAndIdentityType OUT,获取装饰公司商品专供商品列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        //只有顾客身份才能购买专供商品
        if (null == identityType ) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "此用户身份不支持此功能！", null);
            logger.info("getZsRankGoodsListByUserIdAndIdentityType OUT,获取装饰公司商品专供商品列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == page) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "页码不能为空",
                    null);
            logger.info("getZsRankGoodsListByUserIdAndIdentityType OUT,获取装饰公司商品专供商品列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == size) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "单页显示条数不能为空",
                    null);
            logger.info("getZsRankGoodsListByUserIdAndIdentityType OUT,获取装饰公司商品专供商品列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            if (identityType.equals(AppIdentityType.DECORATE_MANAGER.getValue()) || identityType.equals(AppIdentityType.DECORATE_EMPLOYEE.getValue())){
                // 装饰公司查看专供产品
            }else {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "此用户身份不支持此功能！", null);
                logger.info("getZsRankGoodsListByUserIdAndIdentityType OUT,获取装饰公司商品列表失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }

            PageInfo<UserGoodsResponse> goodsVOList = goodsService.findGoodsListByCustomerIdAndIdentityTypeAndUserRank(userId,
                    AppIdentityType.getAppIdentityTypeByValue(identityType), firstCategoryCode, categoryId, brandId, typeId, specification,keywords, page, size);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null,new GridDataVO<UserGoodsResponse>().transform(goodsVOList));
            logger.info("getZsRankGoodsListByUserIdAndIdentityType OUT,获取装饰公司专供商品列表成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，获取会员专供商品列表失败", null);
            logger.warn("getZsRankGoodsListByUserIdAndIdentityType EXCEPTION,获取装饰公司专供商品列表失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    /**
     * @title   导购查看专供商品列表
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2018/5/2
     */
    @RequestMapping(value = "/seller/rank/list", method = RequestMethod.POST)
    public ResultDTO<Object> getSellerRankGoodsListByUserIdAndIdentityType(Long userId, Integer identityType, String rankCode, String keywords,
                                                                           String firstCategoryCode, Long categoryId, Long brandId, Long typeId,
                                                                           String specification, Integer page, Integer size) {
        ResultDTO<Object> resultDTO;
        logger.info("getSellerRankGoodsListByUserIdAndIdentityType CALLED,获取专供商品列表，入参userId:{},identityType:{},rankCode{},page:{},size:{}", userId, identityType,rankCode,page,size);

        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空", null);
            logger.info("getSellerRankGoodsListByUserIdAndIdentityType OUT,获取专供商品列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        //只有导购才能查看专供商品
        if (null == identityType || identityType != 0) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "此用户身份不支持此功能！", null);
            logger.info("getSellerRankGoodsListByUserIdAndIdentityType OUT,获取专供商品列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == rankCode || "".equals(rankCode)) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "专供类型不能为空！", null);
            logger.info("getSellerRankGoodsListByUserIdAndIdentityType OUT,获取专供商品列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == page) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "页码不能为空",
                    null);
            logger.info("getSellerRankGoodsListByUserIdAndIdentityType OUT,获取专供商品列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == size) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "单页显示条数不能为空",
                    null);
            logger.info("getSellerRankGoodsListByUserIdAndIdentityType OUT,获取专供商品列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            PageInfo<UserGoodsResponse> goodsVOList = goodsService.findGoodsListBySellerIdAndIdentityTypeAndRankCode(userId,
                    AppIdentityType.getAppIdentityTypeByValue(identityType), rankCode, keywords, firstCategoryCode, categoryId,
                    brandId, typeId, specification, page, size);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null,
            (null!=goodsVOList&&null!=goodsVOList.getList()&&goodsVOList.getList().size() > 0) ? new GridDataVO<UserGoodsResponse>().transform(goodsVOList) : null);
            logger.info("getSellerRankGoodsListByUserIdAndIdentityType OUT,获取专供商品列表成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，获取专供商品列表失败", null);
            logger.warn("getSellerRankGoodsListByUserIdAndIdentityType EXCEPTION,获取专供商品列表失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    /**
     * @title   导购获取专供商品详情
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2018/5/5
     */
    @PostMapping(value = "/seller/zg/goodsDetail", produces = "application/json;charset=UTF-8")
    public ResultDTO<GoodsDetailResponse> getSellerZGGoodsDetail(Long userId, Integer identityType, Long goodsId, String rankCode) {
        logger.info("getSellerZGGoodsDetail CALLED,导购获取专供商品详情，入参 userId{}, goodsId {},identityType{}, rankCode{}", userId, goodsId, identityType, rankCode);

        ResultDTO<GoodsDetailResponse> resultDTO;
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空", null);
            logger.info("getSellerZGGoodsDetail OUT,导购获取专供商品详情失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户身份不能为空", null);
            logger.info("getSellerZGGoodsDetail OUT,导购获取专供商品详情失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == goodsId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "goodsId不能为null！", null);
            logger.info("getSellerZGGoodsDetail OUT,导购获取专供商品详情失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        GoodsDetailResponse goodsDetailResponse = this.goodsService.findSellerZGGoodsDetailByGoodsId(userId, goodsId, identityType, rankCode);
        resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, goodsDetailResponse);
        logger.info("getSellerZGGoodsDetail OUT,导购获取专供商品详情成功，出参 resultDTO:{}", resultDTO);
        return resultDTO;
    }


    /**
     * @title   查询专供商品品牌列表
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2018/6/20
     */
    @RequestMapping(value = "/rank/brand/list", method = RequestMethod.POST)
    public ResultDTO<Object> getGoodsBrandListByUserIdAndIdentityTypeAndUserRank(String categoryCode, Long userId, Integer identityType, String rankCode, String categorySecond,String specification,String goodType) {
        ResultDTO<Object> resultDTO;
        logger.info("getGoodsBrandListByUserIdAndIdentityTypeAndUserRank CALLED,查询专供商品品牌列表，入参 categoryCode:{},userId:{},identityType:{}", categoryCode, userId, identityType);
        if (StringUtils.isBlank(categoryCode)) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "一级分类编码不能为空!", null);
            logger.info("getGoodsBrandListByUserIdAndIdentityTypeAndUserRank OUT,查询专供商品品牌列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空", null);
            logger.info("getGoodsBrandListByUserIdAndIdentityTypeAndUserRank OUT,查询专供商品品牌列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户身份不能为空", null);
            logger.info("getGoodsBrandListByUserIdAndIdentityTypeAndUserRank OUT,查询专供商品品牌列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            List<GoodsBrandResponse> brandList = goodsService.findGoodsBrandListByCategoryCodeAndUserIdAndIdentityTypeAndUserRank(categoryCode, userId, identityType,categorySecond,specification,goodType,rankCode);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null,
                    (brandList != null && brandList.size() > 0) ? brandList : null);
            logger.info("getGoodsBrandListByUserIdAndIdentityTypeAndUserRank OUT,查询专供商品品牌列表成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，查询专供商品品牌列表失败", null);
            logger.warn("getGoodsBrandListByUserIdAndIdentityTypeAndUserRank EXCEPTION,查询专供商品品牌列表失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }


    /**
     * @title   获取专供商品规格列表
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2018/6/22
     */
    @RequestMapping(value = "/rank/specification/list", method = RequestMethod.POST)
    public ResultDTO<Object> getGoodsSpecificationListByUserIdAndUserRank(String categoryCode, Long userId, Integer identityType, String categorySecond,
                                                                          String goodsBrand, String goodType, String rankCode) {
        ResultDTO<Object> resultDTO;
        logger.info("getGoodsSpecificationListByUserIdAndUserRank CALLED,获取专供商品规格列表，入参 categoryCode:{},userId:{},identityType:{}", categoryCode, userId, identityType);
        if (StringUtils.isBlank(categoryCode)) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "一级分类编码不能为空!", null);
            logger.info("getGoodsSpecificationListByUserIdAndUserRank OUT,获取专供商品规格列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空", null);
            logger.info("getGoodsSpecificationListByUserIdAndUserRank OUT,获取专供商品规格列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户身份不能为空", null);
            logger.info("getGoodsSpecificationListByUserIdAndUserRank OUT,获取专供商品规格列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            List<GoodsSpecificationResponse> specificationList = goodsService.findGoodsSpecificationListByCategoryCodeAndUserIdAndUserRank(categoryCode, userId, identityType,categorySecond,goodsBrand,goodType,rankCode);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null,
                    (specificationList != null && specificationList.size() > 0) ? specificationList : null);
            logger.info("getGoodsSpecificationListByUserIdAndUserRank OUT,获取专供商品规格列表成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，专供商品品牌列表获取失败", null);
            logger.warn("getGoodsSpecificationListByUserIdAndUserRank EXCEPTION,获取专供商品规格列表失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    /**
     * @title   获取专供商品分类列表
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2018/6/22
     */
    @RequestMapping(value = "/rank/category/list", method = RequestMethod.POST)
    public ResultDTO<Object> getGoodsCategoryListByUserIdAndUserRank(String categoryCode, Long userId, Integer identityType,String goodsBrand,
                                                                     String specification,String goodType, String rankCode) {
        ResultDTO<Object> resultDTO;
        logger.info("getGoodsCategoryListByUserIdAndUserRank CALLED,获取专供商品分类列表，入参 categoryCode:{},userId:{},identityType:{}", categoryCode, userId, identityType);
        if (StringUtils.isBlank(categoryCode)) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "一级分类编码不能为空!", null);
            logger.info("getGoodsCategoryListByUserIdAndUserRank OUT,获取专供商品分类列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空", null);
            logger.info("getGoodsCategoryListByUserIdAndUserRank OUT,获取专供商品分类列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户身份不能为空", null);
            logger.info("getGoodsCategoryListByUserIdAndUserRank OUT,获取专供商品分类列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            List<GoodsCategoryResponse> categoryList = goodsService.findGoodsCategoryListByCategoryCodeAndUserIdAndUserRank(categoryCode, userId, identityType,goodsBrand,specification,goodType,rankCode);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, categoryList);
            logger.info("getGoodsCategoryListByUserIdAndUserRank OUT,获取专供商品分类列表成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("{}", e);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，专供商品分类列表获取失败", null);
            logger.warn("getGoodsCategoryListByUserIdAndUserRank EXCEPTION,获取专供商品分类列表失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    /**
     * @title   获取专供商品类型列表
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2018/6/22
     */
    @RequestMapping(value = "/rank/type/list", method = RequestMethod.POST)
    public ResultDTO<Object> getGoodsTypeListByUserIdAndUserRank(String categoryCode, Long userId, Integer identityType, String categorySecond,
                                                                 String specification, String goodsBrand, String rankCode) {
        ResultDTO<Object> resultDTO;
        logger.info("getGoodsTypeListByUserIdAndUserRank CALLED,获取专供商品类型列表，入参 categoryCode:{},userId:{},identityType:{}", categoryCode, userId, identityType);
        if (StringUtils.isBlank(categoryCode)) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "一级分类编码不能为空!", null);
            logger.info("getGoodsTypeListByUserIdAndUserRank OUT,获取专供商品类型列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空", null);
            logger.info("getGoodsTypeListByUserIdAndUserRank OUT,获取专供商品类型列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户身份不能为空", null);
            logger.info("getGoodsTypeListByUserIdAndUserRank OUT,获取专供商品类型列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            List<GoodsTypeResponse> brandList = goodsService.findGoodsTypeListByCategoryCodeAndUserIdAndUserRank(categoryCode, userId, identityType,categorySecond,specification,goodsBrand, rankCode);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null,
                    (brandList != null && brandList.size() > 0) ? brandList : null);
            logger.info("getGoodsTypeListByUserIdAndUserRank OUT,获取专供商品类型列表成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，专供商品分类列表获取失败", null);
            logger.warn("getGoodsTypeListByUserIdAndUserRank EXCEPTION,获取专供商品类型列表失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

}

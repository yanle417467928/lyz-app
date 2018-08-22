package cn.com.leyizhuang.app.web.controller.rest;

import cn.com.leyizhuang.app.core.config.shiro.ShiroUser;
import cn.com.leyizhuang.app.core.constant.AppDeliveryType;
import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.core.constant.MaterialListType;
import cn.com.leyizhuang.app.core.constant.*;
import cn.com.leyizhuang.app.core.exception.*;
import cn.com.leyizhuang.app.core.utils.ArrayListUtils;
import cn.com.leyizhuang.app.core.utils.DateUtil;
import cn.com.leyizhuang.app.core.utils.SmsUtils;
import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.core.utils.order.OrderUtils;
import cn.com.leyizhuang.app.foundation.dto.PhotoOrderDTO;
import cn.com.leyizhuang.app.foundation.pojo.*;
import cn.com.leyizhuang.app.foundation.pojo.goods.GoodsDO;
import cn.com.leyizhuang.app.foundation.pojo.inventory.CityInventory;
import cn.com.leyizhuang.app.foundation.pojo.management.MaterialChangeDetailLog;
import cn.com.leyizhuang.app.foundation.pojo.management.MaterialChangeHeadLog;
import cn.com.leyizhuang.app.foundation.pojo.management.city.CityDeliveryTime;
import cn.com.leyizhuang.app.foundation.pojo.management.goods.GoodsCategoryDO;
import cn.com.leyizhuang.app.foundation.pojo.management.order.MaActGoodsMapping;
import cn.com.leyizhuang.app.foundation.pojo.order.*;
import cn.com.leyizhuang.app.foundation.pojo.request.GoodsIdQtyParam;
import cn.com.leyizhuang.app.foundation.pojo.request.management.MaCreateOrderRequest;
import cn.com.leyizhuang.app.foundation.pojo.request.settlement.*;
import cn.com.leyizhuang.app.foundation.pojo.order.MaterialAuditGoodsInfo;
import cn.com.leyizhuang.app.foundation.pojo.order.MaterialAuditSheet;
import cn.com.leyizhuang.app.foundation.pojo.request.GoodsIdQtyParam;
import cn.com.leyizhuang.app.foundation.pojo.response.*;
import cn.com.leyizhuang.app.foundation.pojo.response.materialList.MaUpdateMaterialResponse;
import cn.com.leyizhuang.app.foundation.pojo.user.AppCustomer;
import cn.com.leyizhuang.app.foundation.pojo.user.AppEmployee;
import cn.com.leyizhuang.app.foundation.pojo.user.DeliveryAddressDO;
import cn.com.leyizhuang.app.foundation.service.*;
import cn.com.leyizhuang.app.foundation.service.impl.SmsAccountServiceImpl;
import cn.com.leyizhuang.app.foundation.vo.management.goods.GoodsResponseVO;
import cn.com.leyizhuang.app.foundation.vo.management.goodscategory.GoodsCategoryVO;
import cn.com.leyizhuang.app.foundation.vo.management.order.PhotoOrderVO;
import cn.com.leyizhuang.app.remote.queue.MaSinkSender;
import cn.com.leyizhuang.app.remote.wms.MaICallWms;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.core.constant.PhotoOrderStatus;
import cn.com.leyizhuang.common.core.constant.PhotoOrderType;
import cn.com.leyizhuang.common.core.exception.data.InvalidDataException;
import cn.com.leyizhuang.common.foundation.pojo.SmsAccount;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import cn.com.leyizhuang.common.util.AssertUtil;
import cn.com.leyizhuang.common.util.CountUtil;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageInfo;
import com.mchange.v1.util.ArrayUtils;
import com.sun.jdi.LongValue;
import org.apache.http.HttpResponse;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


/**
 * @author GenerationRoad
 * @date 2018/1/22
 */
@RestController
@RequestMapping(value = MaPhotoOrderRestController.PRE_URL, produces = "application/json;charset=utf-8")
public class MaPhotoOrderRestController extends BaseRestController {

    protected static final String PRE_URL = "/rest/order/photo";

    private final Logger logger = LoggerFactory.getLogger(MaPhotoOrderRestController.class);

    @Autowired
    private MaPhotoOrderService maPhotoOrderService;

    @Autowired
    private MaGoodsCategoryService maGoodsCategoryService;

    @Autowired
    private MaGoodsService maGoodsService;

    @Autowired
    private MaMaterialListService maMaterialListService;

    @Autowired
    private SmsAccountServiceImpl smsAccountService;

    @Autowired
    private MaPhotoOrderGoodsService maPhotoOrderGoodsService;

    @Autowired
    private AdminUserStoreService adminUserStoreService;

    @Resource
    private DeliveryAddressService deliveryAddressService;

    @Resource
    private AppEmployeeService employeeService;

    @Resource
    private AppCustomerService customerService;

    @Resource
    private MaCustomerService maCustomerService;

    @Resource
    private GoodsService goodsService;

    @Resource
    private PhotoOrderService photoOrderServiceImpl;

    @Resource
    private GoodsPriceService goodsPriceService;

    @Resource
    private AppStoreService storeService;

    @Resource
    private MaCityInventoryService cityInventoryService;

    @Autowired
    private AppActService appActService;

    @Resource
    private AppOrderService appOrderService;

    @Resource
    private CommonService commonService;

    @Resource
    private AppCashCouponDutchService cashCouponDutchService;

    @Resource
    private AppActDutchService dutchService;

    @Resource
    private AppCashReturnDutchService cashReturnDutchService;

    @Resource
    private TransactionalSupportService transactionalSupportService;

    @Resource
    private MaOrderService maOrderService;

    @Resource
    private MaICallWms iCallWms;

    @Resource
    private MaSinkSender maSinkSender;

    @Resource
    private AppActService actService;

    @Resource
    private DeliveryFeeRuleService deliveryFeeRuleService;

    @Resource
    private CityService cityService;

    /**
     * @param
     * @return
     * @throws
     * @title 获取拍照下单列表
     * @descripe
     * @author GenerationRoad
     * @date 2018/1/11
     */
    @GetMapping(value = "/page/grid")
    public GridDataVO<PhotoOrderVO> restPhotoOrderPageGird(Integer offset, Integer size, String keywords, Long cityId, String status, Long storeId) {
        size = getSize(size);
        Integer page = getPage(offset, size);
        //查询登录用户门店权限的门店ID
        List<Long> storeIds = this.adminUserStoreService.findStoreIdList();
        PageInfo<PhotoOrderVO> photoOrderVOPageInfo = this.maPhotoOrderService.findAllByCityIdAndStoreId(page, size, cityId, storeId, keywords, status, storeIds);
        return new GridDataVO<PhotoOrderVO>().transform(photoOrderVOPageInfo.getList(), photoOrderVOPageInfo.getTotal());
    }

    /**
     * @param
     * @return
     * @throws
     * @title 获取拍照下单明细
     * @descripe
     * @author GenerationRoad
     * @date 2018/1/22
     */
    @GetMapping(value = "/{id}")
    public ResultDTO<PhotoOrderVO> restCusPreDepositLogGet(@PathVariable(value = "id") Long id) {
        PhotoOrderVO photoOrderVO = this.maPhotoOrderService.findById(id);
        if (null == photoOrderVO) {
            logger.warn("查找拍照下单明细失败：Role(id = {}) == null", id);
            return new ResultDTO<>(CommonGlobal.COMMON_NOT_FOUND_CODE,
                    "指定数据不存在，请联系管理员", null);
        } else {
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, photoOrderVO);
        }
    }


    /**
     * @param
     * @return
     * @throws
     * @title 获取拍照下单商品信息
     * @descripe
     * @author
     */
    @GetMapping(value = "/find/photo/goods")
    public GridDataVO<PhotoOrderGoodsDO> restPhotoGoodsList(ModelMap map, Integer offset, Integer size, String photoNo) {
        if (StringUtils.isBlank(photoNo)) {
            logger.warn("拍照下单单号为空，获取商品信息失败！");
            return null;
        }
        size = getSize(size);
        Integer page = getPage(offset, size);


        PageInfo<PhotoOrderGoodsDO> photoOrderGoodsDOPageInfo = this.maPhotoOrderGoodsService.findPhotoOrderGoodsByPhotoOrderNo(page, size, photoNo);
        List<MaterialListDO> materialListDOList = this.maMaterialListService.findMaPhotoOrderMaterialListByPhotoNumber(photoNo);
        List<PhotoOrderGoodsDO> photoOrderGoodsDOList = photoOrderGoodsDOPageInfo.getList();
        if (null != materialListDOList && materialListDOList.size() > 0) {
            for (PhotoOrderGoodsDO photoOrderGoodsDO : photoOrderGoodsDOList) {
                photoOrderGoodsDO.setIsGenerateOrder("N");
            }
        } else {
            for (PhotoOrderGoodsDO photoOrderGoodsDO : photoOrderGoodsDOList) {
                photoOrderGoodsDO.setIsGenerateOrder("Y");
            }
        }

        if (null == photoOrderGoodsDOList) {
            logger.warn("获取拍照下单商品信息失败：photoNo {}", photoNo);
            return null;
        } else {
            return new GridDataVO<PhotoOrderGoodsDO>().transform(photoOrderGoodsDOList, photoOrderGoodsDOPageInfo.getTotal());
        }
    }


    /**
     * @param
     * @return
     * @throws
     * @title 获取商品分类
     * @descripe
     * @author GenerationRoad
     * @date 2018/2/22
     */
    @GetMapping(value = "/findCategory")
    public ResultDTO<Object> findCategory(String categoryCode, Long id, Long guideId) {
        logger.info("findCategory,获取商品分类，入参 categoryCode:{} id:{} guideId:{}", categoryCode, id, guideId);

        if (null == guideId) {
            logger.warn("选择导购id为空：guideId {}", guideId);
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE,
                    "选择导购id为空，请联系管理员", null);
        }
        Map<String, Object> returnMap = new HashMap(2);
        List<GoodsCategoryDO> goodsCategoryDOList = this.maGoodsCategoryService.findGoodsCategoryByPCategoryCode(categoryCode);
        if (null == goodsCategoryDOList) {
            logger.warn("查找商品分类：Role(categoryCode = {}) == null", categoryCode);
            return new ResultDTO<>(CommonGlobal.COMMON_NOT_FOUND_CODE,
                    "指定数据不存在，请联系管理员", null);
        } else {
            returnMap.put("goodsCategory", goodsCategoryDOList);
            PhotoOrderVO photoOrderVO = this.maPhotoOrderService.findById(id);
            List<GoodsResponseVO> goodsList = null;
            List<GoodsBrandResponse> brandList = null;
            List<GoodsSpecificationResponse> specificationList = null;
            List<GoodsTypeResponse> goodsTypeList = null;
            if (null != photoOrderVO) {
                List<Long> cids = new ArrayList<>();
                for (GoodsCategoryDO goodsCategoryDO : goodsCategoryDOList) {
                    cids.add(goodsCategoryDO.getCid());
                }
                if (AppIdentityType.CUSTOMER.equals(photoOrderVO.getIdentityTypeValue())) {
                    goodsList = this.maGoodsService.findGoodsByCidAndCusId(photoOrderVO.getUserId(), cids);

                    brandList = maGoodsService.findGoodsBrandListByCategoryCodeAndUserIdAndIdentityType(categoryCode, photoOrderVO.getUserId(), 6, null, null, null);
                    specificationList = maGoodsService.findGoodsSpecificationListByCategoryCodeAndUserIdAndIdentityType(categoryCode, photoOrderVO.getUserId(), 6, null, null, null);
                    goodsTypeList = maGoodsService.findGoodsTypeListByCategoryCodeAndUserIdAndIdentityType(categoryCode, photoOrderVO.getUserId(), 6, null, null, null);
                } else {
                    if (-1 == guideId) {
                        goodsList = this.maGoodsService.findGoodsByCidAndEmpId(photoOrderVO.getUserId(), cids);
                        brandList = maGoodsService.findGoodsBrandListByCategoryCodeAndUserIdAndIdentityType(categoryCode, photoOrderVO.getUserId(), 0, null, null, null);
                        specificationList = maGoodsService.findGoodsSpecificationListByCategoryCodeAndUserIdAndIdentityType(categoryCode, photoOrderVO.getUserId(), 0, null, null, null);
                        goodsTypeList = maGoodsService.findGoodsTypeListByCategoryCodeAndUserIdAndIdentityType(categoryCode, photoOrderVO.getUserId(), 0, null, null, null);
                    } else {
                        goodsList = this.maGoodsService.findGoodsByCidAndEmpId(guideId, cids);
                        brandList = maGoodsService.findGoodsBrandListByCategoryCodeAndUserIdAndIdentityType(categoryCode, guideId, 0, null, null, null);
                        specificationList = maGoodsService.findGoodsSpecificationListByCategoryCodeAndUserIdAndIdentityType(categoryCode, guideId, 0, null, null, null);
                        goodsTypeList = maGoodsService.findGoodsTypeListByCategoryCodeAndUserIdAndIdentityType(categoryCode, guideId, 0, null, null, null);
                    }
                }
            }
            returnMap.put("goods", goodsList);
            returnMap.put("brandList", brandList);
            returnMap.put("specificationList", specificationList);
            returnMap.put("goodsTypeList", goodsTypeList);

            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, returnMap);
        }
    }


    /**
     * @param
     * @return
     * @throws
     * @title 获取商品分类
     * @descripe
     * @author GenerationRoad
     * @date 2018/2/22
     */
    @GetMapping(value = "/findCategory/goods")
    public ResultDTO<Object> findCategoryGoods(String categoryCode, String identityType, Long guideId) {
        logger.info("findCategoryGoods,获取商品分类，入参 categoryCode:{} identityType:{} guideId:{}", categoryCode, identityType, guideId);

        if (null == guideId) {
            logger.warn("选择导购id为空：guideId {}", guideId);
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE,
                    "选择导购id为空，请联系管理员", null);
        }
        Map<String, Object> returnMap = new HashMap(2);
        List<GoodsCategoryDO> goodsCategoryDOList = this.maGoodsCategoryService.findGoodsCategoryByPCategoryCode(categoryCode);
        if (null == goodsCategoryDOList) {
            logger.warn("查找商品分类：Role(categoryCode = {}) == null", categoryCode);
            return new ResultDTO<>(CommonGlobal.COMMON_NOT_FOUND_CODE,
                    "指定数据不存在，请联系管理员", null);
        } else {
            returnMap.put("goodsCategory", goodsCategoryDOList);

            List<GoodsResponseVO> goodsList = null;
            List<GoodsBrandResponse> brandList = null;
            List<GoodsSpecificationResponse> specificationList = null;
            List<GoodsTypeResponse> goodsTypeList = null;
            List<Long> cids = new ArrayList<>();
            for (GoodsCategoryDO goodsCategoryDO : goodsCategoryDOList) {
                cids.add(goodsCategoryDO.getCid());
            }
            if (identityType.equals(AppIdentityType.CUSTOMER.getDescription())) {
                goodsList = this.maGoodsService.findGoodsByCidAndCusId(guideId, cids);
                brandList = maGoodsService.findGoodsBrandListByCategoryCodeAndUserIdAndIdentityType(categoryCode, guideId, 6, null, null, null);
                specificationList = maGoodsService.findGoodsSpecificationListByCategoryCodeAndUserIdAndIdentityType(categoryCode, guideId, 6, null, null, null);
                goodsTypeList = maGoodsService.findGoodsTypeListByCategoryCodeAndUserIdAndIdentityType(categoryCode, guideId, 6, null, null, null);
            } else {
                goodsList = this.maGoodsService.findGoodsByCidAndEmpId(guideId, cids);
                brandList = maGoodsService.findGoodsBrandListByCategoryCodeAndUserIdAndIdentityType(categoryCode, guideId, 0, null, null, null);
                specificationList = maGoodsService.findGoodsSpecificationListByCategoryCodeAndUserIdAndIdentityType(categoryCode, guideId, 0, null, null, null);
                goodsTypeList = maGoodsService.findGoodsTypeListByCategoryCodeAndUserIdAndIdentityType(categoryCode, guideId, 0, null, null, null);
            }
            returnMap.put("goods", goodsList);
            returnMap.put("brandList", brandList);
            returnMap.put("specificationList", specificationList);
            returnMap.put("goodsTypeList", goodsTypeList);

            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, returnMap);
        }
    }


    /**
     * @param
     * @return
     * @throws
     * @title 获取专供商品分类
     * @descripe
     * @author GenerationRoad
     * @date 2018/2/22
     */
    @GetMapping(value = "/findZGCategory/goods")
    public ResultDTO<Object> findZGCategoryGoods(String categoryCode, Long guideId, String identityType, String categorySecond, String specification, String goodType, String rankCode, String goodsBrand) {
        ResultDTO<Object> resultDTO;
        logger.info("findZGCategoryGoods,获取商品分类，入参 categoryCode:{} identityType:{} guideId:{} categorySecond:{} specification:{} goodType:{}", categoryCode, identityType, guideId, categorySecond, specification, goodType);

        if (null == guideId) {
            logger.warn("选择下单人id为空：guideId {}", guideId);
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE,
                    "选择下单人id为空，请联系管理员", null);
        }
        if (StringUtils.isBlank(categoryCode)) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "一级分类编码不能为空!", null);
            logger.info("findZGCategoryGoods OUT,获取专供商品分类，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == guideId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空", null);
            logger.info("findZGCategoryGoods OUT,获取专供商品分类，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户身份不能为空", null);
            logger.info("findZGCategoryGoods OUT,获取专供商品分类，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        Integer appIdentityType = AppIdentityType.getAppIdentityTypeByDescription(identityType).getValue();
        if (null == appIdentityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户身份错误", null);
            logger.info("findZGCategoryGoods OUT,获取专供商品分类，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        Map<String, Object> returnMap = new HashMap(3);
        List<GoodsBrandResponse> brandList = goodsService.findGoodsBrandListByCategoryCodeAndUserIdAndIdentityTypeAndUserRank(categoryCode, guideId, appIdentityType, categorySecond, specification, goodType, rankCode);
        List<GoodsSpecificationResponse> specificationList = goodsService.findGoodsSpecificationListByCategoryCodeAndUserIdAndUserRank(categoryCode, guideId, appIdentityType, categorySecond, goodsBrand, goodType, rankCode);
        List<UserGoodsResponse> goodsList = null;
        if(6==appIdentityType||2==appIdentityType || 3==appIdentityType){
            goodsList = goodsService.findGoodsListByCustomerIdAndIdentityTypeAndUserRankListMa(guideId,AppIdentityType.getAppIdentityTypeByDescription(identityType) , categoryCode,null, null,
                    null, null, null);
        }
        returnMap.put("specificationList", specificationList);
        returnMap.put("brandList", brandList);
        returnMap.put("goods", goodsList);
        return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, returnMap);
    }


    /**
     * @param
     * @return
     * @throws
     * @title 获取商品
     * @descripe
     * @author GenerationRoad
     * @date 2018/2/22
     */
    @GetMapping(value = "/findGoods")
    public ResultDTO<Object> findGoods(Long categoryId, Long id, Long guideId, String categoryType, Long brandString, String specificationString, Long goodsTypeString) {
        logger.info("findGoods,获取商品，入参 categoryId:{} id:{} guideId:{} categoryType:{} brandString:{} specificationString:{} goodsTypeString:{}",
                categoryId, id, guideId, categoryType, brandString, specificationString, goodsTypeString);
        if (StringUtils.isBlank(specificationString)) {
            specificationString = null;
        }
        if (StringUtils.isBlank(categoryType)) {
            categoryType = null;
        }

        List<Long> cids = new ArrayList<>();
        cids.add(categoryId);
        PhotoOrderVO photoOrderVO = this.maPhotoOrderService.findById(id);
        List<GoodsResponseVO> goodsList = null;
        if (null != photoOrderVO) {
            if (null != categoryId && categoryId.equals(0L)) {
                if (AppIdentityType.CUSTOMER.equals(photoOrderVO.getIdentityTypeValue())) {
                    goodsList = this.maGoodsService.findGoodsByCidAndCusIdAndUserRank(photoOrderVO.getUserId());
                }
            } else {
                if (AppIdentityType.CUSTOMER.equals(photoOrderVO.getIdentityTypeValue())) {
                    goodsList = this.maGoodsService.findGoodsByMultiConditionQueryAndCusId(photoOrderVO.getUserId(), categoryId, categoryType, brandString, specificationString, goodsTypeString);
                } else {
                    if (null == guideId || -1 == guideId) {
                        goodsList = this.maGoodsService.findGoodsByMultiConditionQueryAndEmpId(photoOrderVO.getUserId(), categoryId, categoryType, brandString, specificationString, goodsTypeString);
                    } else {
                        goodsList = this.maGoodsService.findGoodsByMultiConditionQueryAndEmpId(guideId, categoryId, categoryType, brandString, specificationString, goodsTypeString);
                    }
                }
            }
        }
        if (null == goodsList) {
            logger.warn("查找商品：Role(categoryId = {}, id = {}) == null", categoryId, id);
            return new ResultDTO<>(CommonGlobal.COMMON_NOT_FOUND_CODE,
                    "指定数据不存在，请联系管理员", null);
        } else {
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, goodsList);
        }
    }

    /**
     * @param
     * @return
     * @throws
     * @title 获取商品
     * @descripe
     * @author GenerationRoad
     * @date 2018/2/22
     */
    @GetMapping(value = "/findGoods/guideId")
    public ResultDTO<Object> findGoodsByGuideId(Long categoryId, Long guideId, String categoryType,
                                                Long brandString, String specificationString, Long goodsTypeString, String identityType) {
        logger.info("findGoods,获取商品，入参 categoryId:{} guideId:{} categoryType:{} brandString:{} specificationString:{} goodsTypeString:{} identityType:{}",
                categoryId, guideId, categoryType, brandString, specificationString, goodsTypeString, identityType);
        if (StringUtils.isBlank(specificationString)) {
            specificationString = null;
        }
        if (StringUtils.isBlank(categoryType)) {
            categoryType = null;
        }

        List<Long> cids = new ArrayList<>();
        cids.add(categoryId);
        List<GoodsResponseVO> goodsList = null;
        if (null != categoryId && categoryId.equals(0L)) {
            if ("顾客".equals(identityType)) {
                goodsList = this.maGoodsService.findGoodsByCidAndCusIdAndUserRank(guideId);
            }
        } else {
            if ("顾客".equals(identityType)) {
                goodsList = this.maGoodsService.findGoodsByMultiConditionQueryAndCusId(guideId, categoryId, categoryType, brandString, specificationString, goodsTypeString);
            } else {
                goodsList = this.maGoodsService.findGoodsByMultiConditionQueryAndEmpId(guideId, categoryId, categoryType, brandString, specificationString, goodsTypeString);
            }
        }
        if (null == goodsList) {
            logger.warn("查找商品：Role(categoryId = {}, guideId = {}) == null", categoryId, guideId);
            return new ResultDTO<>(CommonGlobal.COMMON_NOT_FOUND_CODE,
                    "指定数据不存在，请联系管理员", null);
        } else {
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, goodsList);
        }
    }


    /**
     * @param
     * @return
     * @throws
     * @title 获取专供商品
     * @descripe
     * @author GenerationRoad
     * @date 2018/2/22
     */
    @GetMapping(value = "/findZGGoods/guideId")
    public ResultDTO<Object> findZGGoodsByGuideId(Long categoryId, Long guideId, String categoryType,
                                                Long brandString, String specificationString, Long goodsTypeString, String identityType) {
        logger.info("findGoods,获取专供商品，入参 categoryId:{} guideId:{} categoryType:{} brandString:{} specificationString:{} goodsTypeString:{} identityType:{}",
                categoryId, guideId, categoryType, brandString, specificationString, goodsTypeString, identityType);
        if (StringUtils.isBlank(specificationString)) {
            specificationString = null;
        }
        if (StringUtils.isBlank(categoryType)) {
            categoryType = null;
        }
        Integer appIdentityType = AppIdentityType.getAppIdentityTypeByDescription(identityType).getValue();
        List<UserGoodsResponse> goodsList = null;
        if(6==appIdentityType||2==appIdentityType || 3==appIdentityType){
            goodsList = goodsService.findGoodsListByCustomerIdAndIdentityTypeAndUserRankListMa(guideId,AppIdentityType.getAppIdentityTypeByDescription(identityType) ,categoryType,null, brandString,
                    null, specificationString, null);
        }else {
            goodsList = null;
        }
        if (null == goodsList) {
            logger.warn("查找商品：Role(categoryId = {}, guideId = {}) == null", categoryId, guideId);
            return new ResultDTO<>(CommonGlobal.COMMON_NOT_FOUND_CODE,
                    "指定数据不存在，请联系管理员", null);
        } else {
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, goodsList);
        }
    }

    /**
     * @param
     * @return
     * @throws
     * @title 保存拍照下单
     * @descripe
     * @author GenerationRoad
     * @date 2018/2/22
     */
    @PostMapping(value = "/save")
    public ResultDTO<Object> savePhotoOrder(@Valid PhotoOrderDTO photoOrderDTO, BindingResult result) {
        logger.info("savePhotoOrder,保存拍照下单，入参 categoryId:{}", photoOrderDTO);
        if (!result.hasErrors()) {
            if (0 == photoOrderDTO.getGoAddDeliveryAddressType() && (null == photoOrderDTO.getDeliveryId() || -1 == photoOrderDTO.getDeliveryId())) {
                if (StringUtils.isBlank(photoOrderDTO.getReceiverName())) {
                    ResultDTO<Object> resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "收货人姓名不能为空，保存拍照下单失败！", null);
                    logger.info("savePhotoOrder EXCEPTION，收货人姓名不能为空，保存拍照下单失败，出参 ResultDTO:{}", resultDTO);
                    return resultDTO;
                }
                if (StringUtils.isBlank(photoOrderDTO.getReceiverPhone())) {
                    ResultDTO<Object> resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "收货人电话号码不能为空，保存拍照下单失败！", null);
                    logger.info("savePhotoOrder EXCEPTION，收货人姓名不能为空，保存拍照下单失败，出参 ResultDTO:{}", resultDTO);
                    return resultDTO;
                }
                if (StringUtils.isBlank(photoOrderDTO.getProvince())) {
                    ResultDTO<Object> resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "省不能为空，保存拍照下单失败！", null);
                    logger.info("savePhotoOrder EXCEPTION，省不能为空，保存拍照下单失败，出参 ResultDTO:{}", resultDTO);
                    return resultDTO;
                }
                if (StringUtils.isBlank(photoOrderDTO.getCity())) {
                    ResultDTO<Object> resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "市不能为空，保存拍照下单失败！", null);
                    logger.info("savePhotoOrder EXCEPTION，市不能为空，保存拍照下单失败，出参 ResultDTO:{}", resultDTO);
                    return resultDTO;
                }
                if (StringUtils.isBlank(photoOrderDTO.getCounty())) {
                    ResultDTO<Object> resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "区不能为空，保存拍照下单失败！", null);
                    logger.info("savePhotoOrder EXCEPTION，区不能为空，保存拍照下单失败，出参 ResultDTO:{}", resultDTO);
                    return resultDTO;
                }
                if (StringUtils.isBlank(photoOrderDTO.getStreet())) {
                    ResultDTO<Object> resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "街道不能为空，保存拍照下单失败！", null);
                    logger.info("savePhotoOrder EXCEPTION，街道不能为空，保存拍照下单失败，出参 ResultDTO:{}", resultDTO);
                    return resultDTO;
                }
                if (StringUtils.isBlank(photoOrderDTO.getResidenceName())) {
                    ResultDTO<Object> resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "小区名不能为空，保存拍照下单失败！", null);
                    logger.info("savePhotoOrder EXCEPTION，小区名不能为空，保存拍照下单失败，出参 ResultDTO:{}", resultDTO);
                    return resultDTO;
                }
                if (StringUtils.isBlank(photoOrderDTO.getDetailedAddress())) {
                    ResultDTO<Object> resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "详细地址不能为空，保存拍照下单失败！", null);
                    logger.info("savePhotoOrder EXCEPTION，详细地址不能为空，保存拍照下单失败，出参 ResultDTO:{}", resultDTO);
                    return resultDTO;
                }
            }

            if (null == photoOrderDTO.getCombList() || photoOrderDTO.getCombList().size() <= 0) {
                ResultDTO<Object> resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "商品信息不能为空，请选择商品！", null);
                logger.info("savePhotoOrder EXCEPTION，商品信息不能为空，保存拍照下单失败，出参 ResultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null != photoOrderDTO && null != photoOrderDTO.getPhotoId() && null != photoOrderDTO.getCombList() && photoOrderDTO.getCombList().size() > 0) {

                //查询拍照订单信息
                List<PhotoOrderStatus> status = new ArrayList<>();
                status.add(PhotoOrderStatus.PENDING);
                status.add(PhotoOrderStatus.PROCESSING);
                PhotoOrderVO photoOrderVO = this.maPhotoOrderService.findByIdAndStatus(photoOrderDTO.getPhotoId(), status);
                AppEmployee employee = null;
                AppEmployee proxyEmployee = null;
                if (null != photoOrderDTO.getGuideId() && -1 != photoOrderDTO.getGuideId()) {
                    employee = employeeService.findById(photoOrderDTO.getGuideId());
                    if (null == employee) {
                        ResultDTO<Object> resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "未查询到此导购或员工信息，导购id：" + photoOrderDTO.getGuideId() + "，保存拍照下单失败！", null);
                        logger.info("savePhotoOrder EXCEPTION，未查询到此导购或员工信息，导购id：" + photoOrderDTO.getGuideId() + "，保存拍照下单失败！出参 ResultDTO:{}", resultDTO);
                        return resultDTO;
                    }
                }
                if (null != photoOrderDTO.getProxyId() && -1 != photoOrderDTO.getProxyId()) {
                    proxyEmployee = employeeService.findById(photoOrderDTO.getProxyId());
                    if (null == proxyEmployee) {
                        ResultDTO<Object> resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "未查询到此代下单人信息，代下单id：" + photoOrderDTO.getProxyId() + "，保存新拍照下单失败！", null);
                        logger.info("saveNewPhotoOrder EXCEPTION，未查询到此代下单人信息，代下单id：" + photoOrderDTO.getProxyId() + "，保存拍照下单失败！出参 ResultDTO:{}", resultDTO);
                        return resultDTO;
                    }
                }
                if (null != photoOrderVO) {


                    if (AppIdentityType.DECORATE_EMPLOYEE == photoOrderVO.getIdentityTypeValue()) {
                        String provinceName = deliveryAddressService.findAreaNameByCode(photoOrderDTO.getProvince());
                        String cityName = deliveryAddressService.findAreaNameByCode(photoOrderDTO.getCity());
                        String countyName = deliveryAddressService.findAreaNameByCode(photoOrderDTO.getCounty());
                        AppEmployee decorateEmployee = employeeService.findById(photoOrderVO.getUserId());
                        List<MaterialListDO> combList = photoOrderDTO.getCombList();
                        MaterialAuditSheet materialAuditSheet = new MaterialAuditSheet();
                        if (0 == photoOrderDTO.getGoAddDeliveryAddressType()) {
                            materialAuditSheet.setEmployeeID(decorateEmployee.getEmpId());
                            materialAuditSheet.setReceiver(photoOrderDTO.getReceiverName());
                            materialAuditSheet.setReceiverPhone(photoOrderDTO.getReceiverPhone());
                            materialAuditSheet.setDeliveryCity(cityName);
                            materialAuditSheet.setDeliveryCounty(countyName);
                            materialAuditSheet.setDeliveryStreet(photoOrderDTO.getStreet());
                            materialAuditSheet.setResidenceName(photoOrderDTO.getResidenceName());
                            materialAuditSheet.setDetailedAddress(photoOrderDTO.getDetailedAddress());
                            materialAuditSheet.setRemark(photoOrderDTO.getRemark());

                            DeliveryAddressDO deliveryAddressDO = new DeliveryAddressDO();
                            deliveryAddressDO.setReceiver(photoOrderDTO.getReceiverName());
                            deliveryAddressDO.setReceiverPhone(photoOrderDTO.getReceiverPhone());
                            deliveryAddressDO.setDeliveryProvince(provinceName);
                            deliveryAddressDO.setDeliveryCity(cityName);
                            deliveryAddressDO.setDeliveryCounty(countyName);
                            deliveryAddressDO.setDeliveryStreet(photoOrderDTO.getStreet());
                            deliveryAddressDO.setDetailedAddress(photoOrderDTO.getDetailedAddress());
                            deliveryAddressDO.setResidenceName(photoOrderDTO.getResidenceName());
                            deliveryAddressDO.setUserId(decorateEmployee.getManagerId());
                            deliveryAddressDO.setIdentityType(AppIdentityType.DECORATE_MANAGER);
                            deliveryAddressDO.setStatus(Boolean.TRUE);
                            deliveryAddressDO.setIsDefault(Boolean.FALSE);
                            deliveryAddressDO.setEstateInfo(photoOrderDTO.getEstateInfo());

                            deliveryAddressService.maAddDeliveryAddress(deliveryAddressDO);
                            //地址id
                            Long deliveryId = deliveryAddressDO.getId();
                            materialAuditSheet.setDeliveryId(deliveryId);
                        }

                        materialAuditSheet.setEmployeeName(decorateEmployee.getName());
                        materialAuditSheet.setDeliveryType(AppDeliveryType.HOUSE_DELIVERY);
                        materialAuditSheet.setStoreID(decorateEmployee.getStoreId());
                        materialAuditSheet.setStatus(1);
                        materialAuditSheet.setIsAudited(false);
                        String auditNumber = this.createNumber();
                        materialAuditSheet.setAuditNo(auditNumber);
                        materialAuditSheet.setCreateTime(LocalDateTime.now());
                        List<MaterialAuditGoodsInfo> materialAuditGoodsInfoList = new ArrayList<>();
                        for (MaterialListDO materialListDO : combList) {
                            if (null != materialListDO && null != materialListDO.getGid()) {
                                //根据商品id查找对应的商品
                                GoodsDO goodsDO = goodsService.queryById(materialListDO.getGid());
                                MaterialAuditGoodsInfo materialAuditGoodsInfo = new MaterialAuditGoodsInfo();
                                //对物料审核单商品详情请进行赋值
                                materialAuditGoodsInfo.setGid(materialListDO.getGid());
                                materialAuditGoodsInfo.setCoverImageUri(goodsDO.getCoverImageUri());
                                materialAuditGoodsInfo.setQty(materialListDO.getQty());
                                materialAuditGoodsInfo.setGoodsSpecification(goodsDO.getGoodsSpecification());
                                materialAuditGoodsInfo.setGoodsUnit(goodsDO.getGoodsUnit());
                                materialAuditGoodsInfo.setSku(goodsDO.getSku());
                                materialAuditGoodsInfo.setSkuName(goodsDO.getSkuName());
                                //获取商品零售价
                                Double goodsPrice = goodsPriceService.findGoodsRetailPriceByGoodsIDAndStoreID(materialListDO.getGid(), decorateEmployee.getStoreId());
                                materialAuditGoodsInfo.setRetailPrice(goodsPrice);
                                materialAuditGoodsInfoList.add(materialAuditGoodsInfo);
                            }
                        }
                        Boolean b = maPhotoOrderService.saveMaterialAuditSheet(materialAuditSheet, materialAuditGoodsInfoList);
                        if (b) {
                            this.maPhotoOrderService.updateStatusAndsaveAndUpdateMaterialStatus(photoOrderDTO.getPhotoId(), PhotoOrderStatus.FINISH);

                            ResultDTO<Object> resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "保存新拍照下单成功！", null);
                            logger.info("保存新拍照下单成功!");
                            return resultDTO;
                        } else {
                            ResultDTO<Object> resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "信息缺失，保存新拍照下单失败！", null);
                            logger.info("saveNewPhotoOrder EXCEPTION，未知异常，保存新拍照下单失败！出参 ResultDTO:{}", resultDTO);
                            return resultDTO;
                        }
                    }

                    List<MaterialListDO> combList = photoOrderDTO.getCombList();
                    List<MaterialListDO> materialListSave = new ArrayList<>();
                    List<MaterialListDO> materialListUpdate = new ArrayList<>();
                    List<PhotoOrderGoodsDO> photoOrderGoodsDOList = new ArrayList<>();
                    Long deliveryId = null;
                    if (null != photoOrderDTO.getDeliveryId() && -1 != photoOrderDTO.getDeliveryId()) {
                        deliveryId = photoOrderDTO.getDeliveryId();
                    }
                    //*************************************增加地址信息**************************************
                    if (0 == photoOrderDTO.getGoAddDeliveryAddressType() && (null == photoOrderDTO.getDeliveryId() || -1 == photoOrderDTO.getDeliveryId())) {
                        String provinceName = deliveryAddressService.findAreaNameByCode(photoOrderDTO.getProvince());
                        String cityName = deliveryAddressService.findAreaNameByCode(photoOrderDTO.getCity());
                        String countyName = deliveryAddressService.findAreaNameByCode(photoOrderDTO.getCounty());

                        DeliveryAddressDO deliveryAddressDO = new DeliveryAddressDO();
                        deliveryAddressDO.setReceiver(photoOrderDTO.getReceiverName());
                        deliveryAddressDO.setReceiverPhone(photoOrderDTO.getReceiverPhone());
                        deliveryAddressDO.setDeliveryProvince(provinceName);
                        deliveryAddressDO.setDeliveryCity(cityName);
                        deliveryAddressDO.setDeliveryCounty(countyName);
                        deliveryAddressDO.setDeliveryStreet(photoOrderDTO.getStreet());
                        deliveryAddressDO.setDetailedAddress(photoOrderDTO.getDetailedAddress());
                        deliveryAddressDO.setResidenceName(photoOrderDTO.getResidenceName());
                        if (null != proxyEmployee) {
                            deliveryAddressDO.setUserId(proxyEmployee.getEmpId());
                            deliveryAddressDO.setIdentityType(proxyEmployee.getIdentityType());
                        } else if (-1 != photoOrderDTO.getGuideId()) {
                            deliveryAddressDO.setUserId(employee.getEmpId());
                            deliveryAddressDO.setIdentityType(employee.getIdentityType());
                        } else {
                            deliveryAddressDO.setUserId(photoOrderVO.getUserId());
                            deliveryAddressDO.setIdentityType(photoOrderVO.getIdentityTypeValue());
                        }
                        deliveryAddressDO.setStatus(Boolean.TRUE);
                        deliveryAddressDO.setIsDefault(Boolean.FALSE);
                        deliveryAddressDO.setEstateInfo(photoOrderDTO.getEstateInfo());

                        deliveryAddressService.maAddDeliveryAddress(deliveryAddressDO);
                        //地址id
                        deliveryId = deliveryAddressDO.getId();
                    }
                    //*******************************************************************************************


                    for (MaterialListDO materialListDO : combList) {
                        GoodsDO goodsDO = maGoodsService.findGoodsById(materialListDO.getGid());
                        if (null != goodsDO) {
                            MaterialListDO materialList = maMaterialListService.findByUserIdAndIdentityTypeAndGoodsId(photoOrderVO.getUserId(),
                                    photoOrderVO.getIdentityTypeValue(), materialListDO.getGid());
                            if (null == materialList) {
                                MaterialListDO materialListDOTemp = transformRepeat(goodsDO);
                                if (null != proxyEmployee) {
                                    materialListDOTemp.setUserId(proxyEmployee.getEmpId());
                                    materialListDOTemp.setIdentityType(proxyEmployee.getIdentityType());
                                } else if (null != photoOrderDTO.getGuideId() && -1 != photoOrderDTO.getGuideId()) {
                                    materialListDOTemp.setUserId(employee.getEmpId());
                                    materialListDOTemp.setIdentityType(employee.getIdentityType());
                                } else {
                                    materialListDOTemp.setUserId(photoOrderVO.getUserId());
                                    materialListDOTemp.setIdentityType(photoOrderVO.getIdentityTypeValue());
                                }
                                materialListDOTemp.setDeliveryId(deliveryId);
                                materialListDOTemp.setQty(materialListDO.getQty());
                                materialListDOTemp.setIsGenerateOrder("N");
                                materialListDOTemp.setMaterialListType(MaterialListType.NORMAL);
                                materialListSave.add(materialListDOTemp);
                            } else {
                                if (null != proxyEmployee) {
                                    materialList.setUserId(proxyEmployee.getEmpId());
                                    materialList.setIdentityType(proxyEmployee.getIdentityType());
                                }
                                materialList.setIsGenerateOrder("N");
                                materialList.setDeliveryId(deliveryId);
                                materialList.setQty(materialList.getQty() + materialListDO.getQty());
                                materialListUpdate.add(materialList);
                            }

                            PhotoOrderGoodsDO photoOrderGoodsDO = new PhotoOrderGoodsDO();
                            photoOrderGoodsDO.setGid(goodsDO.getGid());
                            photoOrderGoodsDO.setSkuName(goodsDO.getSkuName());
                            photoOrderGoodsDO.setGoodsQty(materialListDO.getQty());
                            photoOrderGoodsDO.setPhotoOrderNo(photoOrderVO.getPhotoOrderNo());
                            photoOrderGoodsDOList.add(photoOrderGoodsDO);
                        }
                    }
                    if (null != photoOrderDTO.getGuideId() && -1 != photoOrderDTO.getGuideId()) {
                        this.maPhotoOrderService.updateRemarkAndDeliveryId(photoOrderDTO.getRemark(), deliveryId, employee.getEmpId(), employee.getIdentityType());
                    } else {
                        this.maPhotoOrderService.updateRemarkAndDeliveryId(photoOrderDTO.getRemark(), deliveryId, photoOrderVO.getUserId(), photoOrderVO.getIdentityTypeValue());
                    }

                    if (null != photoOrderDTO.getProxyId() && -1 != photoOrderDTO.getProxyId()) {
                        this.maPhotoOrderService.updatePhotoOrderProxyId(photoOrderDTO.getProxyId(), photoOrderVO.getPhotoOrderNo());
                    }

                    this.maPhotoOrderGoodsService.batchSave(photoOrderGoodsDOList);
                    this.maPhotoOrderService.updateStatusAndsaveAndUpdateMaterialList(photoOrderDTO.getPhotoId(), PhotoOrderStatus.FINISH, materialListSave, materialListUpdate);

                    //短信提醒
//                    String info = "您的拍照下单订单(" + photoOrderVO.getPhotoOrderNo() + ")已处理，请登录APP查看。";
                    String info = "您的拍照下单已完成，请到 APP 下料清单中继续完成支付！";
                    String content;
                    try {
                        content = URLEncoder.encode(info, "GB2312");
                        System.err.println(content);
                    } catch (Exception e) {
                        e.printStackTrace();
                        ResultDTO<Object> resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知错误，短信验证码发送失败！", null);
                        logger.info("savePhotoOrder EXCEPTION，提醒短信发送失败，出参 ResultDTO:{}", resultDTO);
                        logger.warn("{}", e);
                        return resultDTO;
                    }
                    SmsAccount account = smsAccountService.findOne();
                    String returnCode;
                    try {
                        returnCode = SmsUtils.sendMessageQrCode(account.getEncode(), account.getEnpass(), account.getUserName(), photoOrderVO.getUserMobile(), content);
                    } catch (IOException e) {
                        ResultDTO<Object> resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "网络故障，提醒短信发送失败！", null);
                        logger.info("savePhotoOrder EXCEPTION，提醒短信发送失败，出参 ResultDTO:{}", resultDTO);
                        logger.warn("{}", e);
                    } catch (Exception e) {
                        ResultDTO<Object> resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知错误，短信验证码发送失败！", null);
                        logger.info("savePhotoOrder EXCEPTION，提醒短信发送失败，出参 ResultDTO:{}", resultDTO);
                        logger.warn("{}", e);
                    }

                    return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
                }
            }
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "信息错误！", null);
        } else {
            List<ObjectError> allErrors = result.getAllErrors();
            logger.warn("页面提交的数据有错误：errors = {}", errorMsgToHtml(allErrors));
            return new ResultDTO<>(CommonGlobal.COMMON_ERROR_PARAM_CODE,
                    errorMsgToHtml(allErrors), null);
        }
    }

    /**
     * @param
     * @return
     * @throws
     * @title 批量取消拍照下单
     * @descripe
     * @author GenerationRoad
     * @date 2018/2/22
     */
    @DeleteMapping
    public ResultDTO<?> dataResourceDelete(Long[] ids) {
        logger.info("dataResourceDelete,批量取消拍照下单，入参 ids:{}", ids);
        try {
            int num = this.maPhotoOrderService.batchDelete(ids);
            if (num > 0) {
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "拍照下单已成功取消", null);
            } else {
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "没有可取消的拍照下单", null);
            }
        } catch (InvalidDataException e) {
            logger.error("批量取消拍照下单发生错误");
            logger.error(e.getMessage());
            return new ResultDTO<>(CommonGlobal.COMMON_ERROR_PARAM_CODE,
                    "批量取消拍照下单发生错误，请稍后重试或联系管理员", null);
        } catch (Exception e) {
            logger.error("批量取消拍照下单发生错误");
            logger.error(e.getMessage());
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE,
                    "出现未知错误，请稍后重试或联系管理员", null);
        }
    }

    /**
     * @param
     * @return
     * @throws
     * @title 取消拍照下单
     * @descripe
     * @author GenerationRoad
     * @date 2018/2/22
     */
    @PostMapping(value = "/delete")
    public ResultDTO<?> dataResourceDeleteOne(Long photoId) {
        logger.info("dataResourceDeleteOne,取消拍照下单，入参 photoId:{}", photoId);
        try {
            Long[] ids = {photoId};
            int num = this.maPhotoOrderService.batchDelete(ids);
            if (num > 0) {
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "拍照下单已成功取消", null);
            } else {
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "没有可取消的拍照下单", null);
            }
        } catch (InvalidDataException e) {
            logger.error("取消拍照下单发生错误");
            logger.error(e.getMessage());
            return new ResultDTO<>(CommonGlobal.COMMON_ERROR_PARAM_CODE,
                    "取消拍照下单发生错误，请稍后重试或联系管理员", null);
        } catch (Exception e) {
            logger.error("取消拍照下单发生错误");
            logger.error(e.getMessage());
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE,
                    "出现未知错误，请稍后重试或联系管理员", null);
        }
    }

    private MaterialListDO transformRepeat(GoodsDO goodsDO) {
        MaterialListDO materialListDOTemp = new MaterialListDO();
        materialListDOTemp.setGid(goodsDO.getGid());
        materialListDOTemp.setSku(goodsDO.getSku());
        materialListDOTemp.setSkuName(goodsDO.getSkuName());
        materialListDOTemp.setGoodsSpecification(goodsDO.getGoodsSpecification());
        materialListDOTemp.setGoodsUnit(goodsDO.getGoodsUnit());
        if (null != goodsDO.getCoverImageUri()) {
            String uri[] = goodsDO.getCoverImageUri().split(",");
            materialListDOTemp.setCoverImageUri(uri[0]);
        }
        return materialListDOTemp;
    }

    /**
     * 下载拍照下单图片
     *
     * @param response
     * @param request
     * @param photoIds
     */
    @GetMapping("/download/photo")
    public void downloadPhoto(HttpServletResponse response, HttpServletRequest request, String photoIds) {
        logger.info("downloadPhoto,下载拍照下单图片，入参 photoIds:{}", photoIds);
        String[] str1 = photoIds.split(",");
        Long[] ids = new Long[str1.length];
        for (int i = 0; i < str1.length; i++) {
            ids[i] = Long.valueOf(str1[i]);
        }
        try {
            List<String> photos = this.maPhotoOrderService.findPhotosById(ids);
            String downloadFilename = "拍照下单图片包.zip";//文件的名称
            downloadFilename = URLEncoder.encode(downloadFilename, "UTF-8");//转换中文否则可能会产生乱码
            response.setContentType("application/octet-stream");// 指明response的返回对象是文件流
            response.setHeader("Content-Disposition", "attachment;filename=" + downloadFilename);// 设置在下载框默认显示的文件名
            ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());
            int i = 1;
            for (String photo : photos) {
                if (photo.contains(",")) {
                    String[] photoss = photo.split(",");
                    for (String p : photoss) {
                        URL url = new URL(p);
                        zos.putNextEntry(new ZipEntry(i + ".jpg"));
                        InputStream fis = url.openConnection().getInputStream();
                        byte[] buffer = new byte[1024];
                        int r = 0;
                        while ((r = fis.read(buffer)) != -1) {
                            zos.write(buffer, 0, r);
                        }
                        fis.close();
                        i++;
                    }
                } else {
                    URL url = new URL(photo);
                    zos.putNextEntry(new ZipEntry(i + ".jpg"));
                    InputStream fis = url.openConnection().getInputStream();
                    byte[] buffer = new byte[1024];
                    int r = 0;
                    while ((r = fis.read(buffer)) != -1) {
                        zos.write(buffer, 0, r);
                    }
                    fis.close();
                    i++;
                }
            }
            zos.flush();
            zos.close();

            logger.info("批量下载拍照下单图片成功!");
//        //****************************************************************************
//            String downloadFilename = "拍照下单图片包.zip";//文件的名称
//            downloadFilename = URLEncoder.encode(downloadFilename, "UTF-8");//转换中文否则可能会产生乱码
//            response.setContentType("application/octet-stream");// 指明response的返回对象是文件流
//            response.setHeader("Content-Disposition", "attachment;filename=" + downloadFilename);// 设置在下载框默认显示的文件名
//            ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());
//            String[] files = new String[]{"http://xxxx/xx.jpg","http://xxx/xx.jpg"};
//            for (int i=0;i<files.length;i++) {
//                URL url = new URL(files[i]);
//                zos.putNextEntry(new ZipEntry(i+".jpg"));
//                //FileInputStream fis = new FileInputStream(new File(files[i]));
//                InputStream fis = url.openConnection().getInputStream();
//                byte[] buffer = new byte[1024];
//                int r = 0;
//                while ((r = fis.read(buffer)) != -1) {
//                    zos.write(buffer, 0, r);
//                }
//                fis.close();
//            }
//            zos.flush();
//            zos.close();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    /**
     * @param
     * @return
     * @throws
     * @title 获取下单人地址库
     * @descripe
     */
    @GetMapping(value = "/find/address")
    public GridDataVO<DeliveryAddressResponse> findAddressByUserMobile(Integer offset, Integer size, String keywords,
                                                                       String userMobile, String identityType, Long guideId, String sellerAddressConditions) {
        logger.info("findAddressByUserMobile 获取下单人地址库,入参 offset:{},size:{},keywords:{},cityId:{},storeId:{}," +
                "userMobile:{},identityType:{},guideId:{}", offset, size, keywords, userMobile, identityType, guideId);
        try {

            size = getSize(size);
            Integer page = getPage(offset, size);
            PageInfo<DeliveryAddressResponse> deliveryAddressResponseList = null;
            List<DeliveryAddressResponse> deliveryAddressResponses = null;

            if (StringUtils.isBlank(sellerAddressConditions)) {
                sellerAddressConditions = null;
            }

            if (null != guideId && -1 != guideId) {
                if ("顾客".equals(identityType)) {
                    AppCustomer customer = customerService.findById(guideId);
                    deliveryAddressResponseList = deliveryAddressService.getDefaultDeliveryAddressListByUserIdAndIdentityType(page, size, customer.getCusId(), AppIdentityType.CUSTOMER, sellerAddressConditions);
                    deliveryAddressResponses = deliveryAddressResponseList.getList();
                } else {
                    AppEmployee employee = employeeService.findById(guideId);
                    deliveryAddressResponseList = deliveryAddressService.getDefaultDeliveryAddressListByUserIdAndIdentityType(page, size, employee.getEmpId(), employee.getIdentityType(), sellerAddressConditions);
                    deliveryAddressResponses = deliveryAddressResponseList.getList();
                }
                logger.warn("findAddressByUserMobile ,获取下单人地址库成功", deliveryAddressResponses.size());
                return new GridDataVO<DeliveryAddressResponse>().transform(deliveryAddressResponses, deliveryAddressResponseList.getTotal());
            }

            if ("顾客".equals(identityType)) {
                AppCustomer customer = customerService.findByMobile(userMobile);
                deliveryAddressResponseList = deliveryAddressService.getDefaultDeliveryAddressListByUserIdAndIdentityType(page, size, customer.getCusId(), AppIdentityType.CUSTOMER, sellerAddressConditions);
                deliveryAddressResponses = deliveryAddressResponseList.getList();
            } else if ("装饰公司经理".equals(identityType) || "装饰公司".equals(identityType)) {
                AppEmployee employee = employeeService.findByMobile(userMobile);
                deliveryAddressResponseList = deliveryAddressService.getDefaultDeliveryAddressListByUserIdAndIdentityType(page, size, employee.getEmpId(), AppIdentityType.DECORATE_MANAGER, sellerAddressConditions);
                deliveryAddressResponses = deliveryAddressResponseList.getList();
            } else if ("导购".equals(identityType)) {
                AppEmployee employee = employeeService.findByMobile(userMobile);
                deliveryAddressResponseList = deliveryAddressService.getDefaultDeliveryAddressListByUserIdAndIdentityType(page, size, employee.getEmpId(), AppIdentityType.SELLER, sellerAddressConditions);
                deliveryAddressResponses = deliveryAddressResponseList.getList();
            }
            logger.warn("findAddressByUserMobile ,获取下单人地址库成功", deliveryAddressResponses.size());
            return new GridDataVO<DeliveryAddressResponse>().transform(deliveryAddressResponses, deliveryAddressResponseList.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("findAddressByUserMobile EXCEPTION,发生未知错误，获取下单人地址库失败");
            logger.warn("{}", e);
            return null;
        }
    }


    /**
     * @param
     * @return
     * @throws
     * @title 获取下单人列表
     * @descripe
     */
    @GetMapping(value = "/find/people")
    public GridDataVO<MaCreateOrderPeopleResponse> findCreateOrderPeople(Integer offset, Integer size, String keywords,
                                                                         String peopleType, String selectCreateOrderPeopleConditions, Long storeId) {
        logger.info("findCreateOrderPeople 获取下单人列表,入参 offset:{},size:{},keywords:{},peopleType:{}," +
                "selectCreateOrderPeopleConditions:{}", offset, size, keywords, peopleType, selectCreateOrderPeopleConditions);
        if (StringUtils.isBlank(peopleType) || "-1".equals(peopleType)) {
            logger.warn("findCreateOrderPeople OUT,未选择下单人类型，获取下单人列表失败");
            return null;
        }
        try {

            size = getSize(size);
            Integer page = getPage(offset, size);
            PageInfo<MaCreateOrderPeopleResponse> maCreateOrderPeopleResponsePageInfo = null;
            List<MaCreateOrderPeopleResponse> maCreateOrderPeopleResponseList = null;

            if (StringUtils.isBlank(selectCreateOrderPeopleConditions)) {
                selectCreateOrderPeopleConditions = null;
            }
            if (null == storeId || -1 == storeId) {
                storeId = null;
            }
            maCreateOrderPeopleResponsePageInfo = maCustomerService.maFindCreatePeople(page, size, selectCreateOrderPeopleConditions, peopleType, storeId);
            maCreateOrderPeopleResponseList = maCreateOrderPeopleResponsePageInfo.getList();

            logger.warn("findCreateOrderPeople ,获取下单人列表成功", maCreateOrderPeopleResponseList.size());
            return new GridDataVO<MaCreateOrderPeopleResponse>().transform(maCreateOrderPeopleResponseList, maCreateOrderPeopleResponsePageInfo.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("findCreateOrderPeople EXCEPTION,发生未知错误，获取下单人列表失败");
            logger.warn("{}", e);
            return null;
        }
    }

    /**
     * @param
     * @return
     * @throws
     * @title 获取下单人列表
     * @descripe
     */
    @GetMapping(value = "/find/people/storeId")
    public GridDataVO<MaCreateOrderPeopleResponse> findCreateOrderPeopleByStoreId(Integer offset, Integer size, String peopleType, Long storeId) {
        logger.info("findCreateOrderPeople 获取下单人列表,入参 offset:{},size:{},peopleType:{}," +
                "storeId:{}", offset, size, peopleType, storeId);
        if (StringUtils.isBlank(peopleType) || "-1".equals(peopleType)) {
            logger.warn("findCreateOrderPeople OUT,未选择下单人类型，获取下单人列表失败");
            return null;
        }
        try {

            size = getSize(size);
            Integer page = getPage(offset, size);
            PageInfo<MaCreateOrderPeopleResponse> maCreateOrderPeopleResponsePageInfo = null;
            List<MaCreateOrderPeopleResponse> maCreateOrderPeopleResponseList = null;

            maCreateOrderPeopleResponsePageInfo = maCustomerService.maFindCreatePeopleByStoreId(page, size, storeId);
            maCreateOrderPeopleResponseList = maCreateOrderPeopleResponsePageInfo.getList();

            logger.warn("findCreateOrderPeople ,获取下单人列表成功", maCreateOrderPeopleResponseList.size());
            return new GridDataVO<MaCreateOrderPeopleResponse>().transform(maCreateOrderPeopleResponseList, maCreateOrderPeopleResponsePageInfo.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("findCreateOrderPeople EXCEPTION,发生未知错误，获取下单人列表失败");
            logger.warn("{}", e);
            return null;
        }
    }


    /**
     * @param
     * @return
     * @throws
     * @title 保存新拍照下单
     * @descripe
     * @author GenerationRoad
     * @date 2018/2/22
     */
    @PostMapping(value = "/save/new")
    public ResultDTO<Object> saveNewPhotoOrder(@Valid PhotoOrderDTO photoOrderDTO, BindingResult result) {
        logger.info("saveNewPhotoOrder,保存新拍照下单，入参 categoryId:{}", photoOrderDTO);
        if (!result.hasErrors()) {
            if (0 == photoOrderDTO.getGoAddDeliveryAddressType() && (null == photoOrderDTO.getDeliveryId() || -1 == photoOrderDTO.getDeliveryId())) {
                if (StringUtils.isBlank(photoOrderDTO.getReceiverName())) {
                    ResultDTO<Object> resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "收货人姓名不能为空，保存新拍照下单失败！", null);
                    logger.info("saveNewPhotoOrder EXCEPTION，收货人姓名不能为空，保存新拍照下单失败，出参 ResultDTO:{}", resultDTO);
                    return resultDTO;
                }
                if (StringUtils.isBlank(photoOrderDTO.getReceiverPhone())) {
                    ResultDTO<Object> resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "收货人电话号码不能为空，保存新拍照下单失败！", null);
                    logger.info("saveNewPhotoOrder EXCEPTION，收货人姓名不能为空，保存新拍照下单失败，出参 ResultDTO:{}", resultDTO);
                    return resultDTO;
                }
                if (StringUtils.isBlank(photoOrderDTO.getProvince())) {
                    ResultDTO<Object> resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "省不能为空，保存新拍照下单失败！", null);
                    logger.info("saveNewPhotoOrder EXCEPTION，省不能为空，保存新拍照下单失败，出参 ResultDTO:{}", resultDTO);
                    return resultDTO;
                }
                if (StringUtils.isBlank(photoOrderDTO.getCity())) {
                    ResultDTO<Object> resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "市不能为空，保存新拍照下单失败！", null);
                    logger.info("saveNewPhotoOrder EXCEPTION，市不能为空，保存新拍照下单失败，出参 ResultDTO:{}", resultDTO);
                    return resultDTO;
                }
                if (StringUtils.isBlank(photoOrderDTO.getCounty())) {
                    ResultDTO<Object> resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "区不能为空，保存新拍照下单失败！", null);
                    logger.info("saveNewPhotoOrder EXCEPTION，区不能为空，保存新拍照下单失败，出参 ResultDTO:{}", resultDTO);
                    return resultDTO;
                }
                if (StringUtils.isBlank(photoOrderDTO.getStreet())) {
                    ResultDTO<Object> resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "街道不能为空，保存新拍照下单失败！", null);
                    logger.info("saveNewPhotoOrder EXCEPTION，街道不能为空，保存新拍照下单失败，出参 ResultDTO:{}", resultDTO);
                    return resultDTO;
                }
                if (StringUtils.isBlank(photoOrderDTO.getResidenceName())) {
                    ResultDTO<Object> resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "小区名不能为空，保存新拍照下单失败！", null);
                    logger.info("saveNewPhotoOrder EXCEPTION，小区名不能为空，保存新拍照下单失败，出参 ResultDTO:{}", resultDTO);
                    return resultDTO;
                }
                if (StringUtils.isBlank(photoOrderDTO.getDetailedAddress())) {
                    ResultDTO<Object> resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "详细地址不能为空，保存新拍照下单失败！", null);
                    logger.info("saveNewPhotoOrder EXCEPTION，详细地址不能为空，保存新拍照下单失败，出参 ResultDTO:{}", resultDTO);
                    return resultDTO;
                }
            }

            if (StringUtils.isBlank(photoOrderDTO.getPeopleIdentityType())) {
                ResultDTO<Object> resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "下单人身份类型不能为空！", null);
                logger.info("saveNewPhotoOrder EXCEPTION，下单人身份类型不能为空，保存新拍照下单失败，出参 ResultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == photoOrderDTO.getGuideId()) {
                ResultDTO<Object> resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "下单人id不能为空！", null);
                logger.info("saveNewPhotoOrder EXCEPTION，下单人id不能为空，保存新拍照下单失败，出参 ResultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == photoOrderDTO.getCombList() || photoOrderDTO.getCombList().size() <= 0) {
                ResultDTO<Object> resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "商品信息不能为空，请选择商品！", null);
                logger.info("saveNewPhotoOrder EXCEPTION，商品信息不能为空，保存新拍照下单失败，出参 ResultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null != photoOrderDTO && null != photoOrderDTO.getPhotoId() && null != photoOrderDTO.getCombList() && photoOrderDTO.getCombList().size() > 0) {
                List<PhotoOrderGoodsDO> photoOrderGoodsDOList = new ArrayList<>();
                List<PhotoOrderStatus> status = new ArrayList<>();
                status.add(PhotoOrderStatus.PENDING);
                status.add(PhotoOrderStatus.PROCESSING);
//                PhotoOrderVO photoOrderVO = this.maPhotoOrderService.findByIdAndStatus(photoOrderDTO.getPhotoId(), status);
                Long userId = null;
                AppIdentityType appIdentityType = null;
                String mobile = null;
                Long cityId = 1L;
                AppEmployee proxyEmployee = null;
                if (null != photoOrderDTO.getProxyId() && -1 != photoOrderDTO.getProxyId()) {
                    proxyEmployee = employeeService.findById(photoOrderDTO.getProxyId());
                    if (null == proxyEmployee) {
                        ResultDTO<Object> resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "未查询到此代下单人信息，代下单id：" + photoOrderDTO.getProxyId() + "，保存新拍照下单失败！", null);
                        logger.info("saveNewPhotoOrder EXCEPTION，未查询到此代下单人信息，代下单id：" + photoOrderDTO.getProxyId() + "，保存新拍照下单失败！出参 ResultDTO:{}", resultDTO);
                        return resultDTO;
                    }
                }

                if ("装饰公司员工".equals(photoOrderDTO.getPeopleIdentityType())) {
                    String provinceName = deliveryAddressService.findAreaNameByCode(photoOrderDTO.getProvince());
                    String cityName = deliveryAddressService.findAreaNameByCode(photoOrderDTO.getCity());
                    String countyName = deliveryAddressService.findAreaNameByCode(photoOrderDTO.getCounty());
                    AppEmployee decorateEmployee = employeeService.findById(photoOrderDTO.getGuideId());
                    cityId = decorateEmployee.getCityId();
                    List<MaterialListDO> combList = photoOrderDTO.getCombList();
                    MaterialAuditSheet materialAuditSheet = new MaterialAuditSheet();
                    Long deliveryId = null;
                    if (0 == photoOrderDTO.getGoAddDeliveryAddressType()) {
                        materialAuditSheet.setEmployeeID(decorateEmployee.getEmpId());
                        materialAuditSheet.setReceiver(photoOrderDTO.getReceiverName());
                        materialAuditSheet.setReceiverPhone(photoOrderDTO.getReceiverPhone());
                        materialAuditSheet.setDeliveryCity(cityName);
                        materialAuditSheet.setDeliveryCounty(countyName);
                        materialAuditSheet.setDeliveryStreet(photoOrderDTO.getStreet());
                        materialAuditSheet.setResidenceName(photoOrderDTO.getResidenceName());
                        materialAuditSheet.setDetailedAddress(photoOrderDTO.getDetailedAddress());
                        materialAuditSheet.setRemark(photoOrderDTO.getRemark());


                        DeliveryAddressDO deliveryAddressDO = new DeliveryAddressDO();
                        deliveryAddressDO.setReceiver(photoOrderDTO.getReceiverName());
                        deliveryAddressDO.setReceiverPhone(photoOrderDTO.getReceiverPhone());
                        deliveryAddressDO.setDeliveryProvince(provinceName);
                        deliveryAddressDO.setDeliveryCity(cityName);
                        deliveryAddressDO.setDeliveryCounty(countyName);
                        deliveryAddressDO.setDeliveryStreet(photoOrderDTO.getStreet());
                        deliveryAddressDO.setDetailedAddress(photoOrderDTO.getDetailedAddress());
                        deliveryAddressDO.setResidenceName(photoOrderDTO.getResidenceName());
                        deliveryAddressDO.setUserId(decorateEmployee.getManagerId());
                        deliveryAddressDO.setIdentityType(AppIdentityType.DECORATE_MANAGER);
                        deliveryAddressDO.setStatus(Boolean.TRUE);
                        deliveryAddressDO.setIsDefault(Boolean.FALSE);
                        deliveryAddressDO.setEstateInfo(photoOrderDTO.getEstateInfo());

                        deliveryAddressService.maAddDeliveryAddress(deliveryAddressDO);
                        //地址id
                        deliveryId = deliveryAddressDO.getId();
                        materialAuditSheet.setDeliveryId(deliveryId);
                    }
                    materialAuditSheet.setEmployeeName(decorateEmployee.getName());
                    materialAuditSheet.setDeliveryType(AppDeliveryType.HOUSE_DELIVERY);
                    materialAuditSheet.setStoreID(decorateEmployee.getStoreId());
                    materialAuditSheet.setStatus(1);
                    materialAuditSheet.setIsAudited(false);
                    String auditNumber = this.createNumber();
                    materialAuditSheet.setAuditNo(auditNumber);
                    materialAuditSheet.setCreateTime(LocalDateTime.now());
                    List<MaterialAuditGoodsInfo> materialAuditGoodsInfoList = new ArrayList<>();
                    for (MaterialListDO materialListDO : combList) {
                        if (null != materialListDO && null != materialListDO.getGid()) {
                            //根据商品id查找对应的商品
                            GoodsDO goodsDO = goodsService.queryById(materialListDO.getGid());
                            MaterialAuditGoodsInfo materialAuditGoodsInfo = new MaterialAuditGoodsInfo();
                            //对物料审核单商品详情请进行赋值
                            materialAuditGoodsInfo.setGid(materialListDO.getGid());
                            materialAuditGoodsInfo.setCoverImageUri(goodsDO.getCoverImageUri());
                            materialAuditGoodsInfo.setQty(materialListDO.getQty());
                            materialAuditGoodsInfo.setGoodsSpecification(goodsDO.getGoodsSpecification());
                            materialAuditGoodsInfo.setGoodsUnit(goodsDO.getGoodsUnit());
                            materialAuditGoodsInfo.setSku(goodsDO.getSku());
                            materialAuditGoodsInfo.setSkuName(goodsDO.getSkuName());
                            //获取商品零售价
                            Double goodsPrice = goodsPriceService.findGoodsRetailPriceByGoodsIDAndStoreID(materialListDO.getGid(), decorateEmployee.getStoreId());
                            materialAuditGoodsInfo.setRetailPrice(goodsPrice);
                            materialAuditGoodsInfoList.add(materialAuditGoodsInfo);
                        }
                    }
                    //******************************************保存拍照下单实体********************************
                    String orderNumber = OrderUtils.generatePhotoOrderNumber(cityId);
                    PhotoOrderDO photoOrderDO = new PhotoOrderDO();
                    photoOrderDO.setCreateTime(LocalDateTime.now());
                    photoOrderDO.setIdentityType(decorateEmployee.getIdentityType());
                    photoOrderDO.setContactPhone(photoOrderDTO.getContactPhone());
                    photoOrderDO.setContactName(photoOrderDTO.getContactName());
                    photoOrderDO.setPhotos(photoOrderDTO.getPhotoImgs());
                    photoOrderDO.setRemark(photoOrderDTO.getRemark());
                    photoOrderDO.setStatus(PhotoOrderStatus.FINISH);
                    photoOrderDO.setUserId(decorateEmployee.getEmpId());
                    photoOrderDO.setPhotoOrderNo(orderNumber);
                    photoOrderDO.setDeliveryId(deliveryId);
                    photoOrderDO.setOrderType(PhotoOrderType.UNDERLINE);
                    photoOrderDO.setProxyId(photoOrderDTO.getProxyId());
                    this.photoOrderServiceImpl.save(photoOrderDO);

                    for (MaterialListDO materialListDO : combList) {
                        if (null != materialListDO && null != materialListDO.getGid()) {
                            GoodsDO goodsDO = maGoodsService.findGoodsById(materialListDO.getGid());
                            if (null != goodsDO) {
                                PhotoOrderGoodsDO photoOrderGoodsDO = new PhotoOrderGoodsDO();
                                photoOrderGoodsDO.setGid(goodsDO.getGid());
                                photoOrderGoodsDO.setSkuName(goodsDO.getSkuName());
                                photoOrderGoodsDO.setGoodsQty(materialListDO.getQty());
                                photoOrderGoodsDO.setPhotoOrderNo(orderNumber);
                                photoOrderGoodsDOList.add(photoOrderGoodsDO);
                            }
                        }
                    }
                    this.maPhotoOrderGoodsService.batchSave(photoOrderGoodsDOList);
                    Boolean b = maPhotoOrderService.saveMaterialAuditSheet(materialAuditSheet, materialAuditGoodsInfoList);
                    if (b) {
                        ResultDTO<Object> resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "保存新拍照下单成功！", null);
                        logger.info("保存新拍照下单成功!");
                        return resultDTO;
                    } else {
                        ResultDTO<Object> resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "信息缺失，保存新拍照下单失败！", null);
                        logger.info("saveNewPhotoOrder EXCEPTION，未知异常，保存新拍照下单失败！出参 ResultDTO:{}", resultDTO);
                        return resultDTO;
                    }
                }

                if ("顾客".equals(photoOrderDTO.getPeopleIdentityType())) {
                    try {
                        AppCustomer customer = customerService.findById(photoOrderDTO.getGuideId());
                        if (null == customer) {
                            ResultDTO<Object> resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "未查询到此顾客信息，顾客id：" + photoOrderDTO.getGuideId() + "，保存新拍照下单失败！", null);
                            logger.info("saveNewPhotoOrder EXCEPTION，未查询到此顾客信息，顾客id：" + photoOrderDTO.getGuideId() + "，保存新拍照下单失败！出参 ResultDTO:{}", resultDTO);
                            return resultDTO;
                        }
                        userId = customer.getCusId();
                        appIdentityType = AppIdentityType.CUSTOMER;
                        mobile = customer.getMobile();
                        cityId = customer.getCityId();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        ResultDTO<Object> resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "查询此顾客信息发生异常，请联系管理员，顾客id：" + photoOrderDTO.getGuideId() + "，保存新拍照下单失败！", null);
                        logger.info("saveNewPhotoOrder EXCEPTION，查询此顾客信息发生异常，请联系管理员，顾客id：" + photoOrderDTO.getGuideId() + "，保存新拍照下单失败！出参 ResultDTO:{}", resultDTO);
                        return resultDTO;
                    }
                } else {
                    AppEmployee employee = employeeService.findById(photoOrderDTO.getGuideId());
                    if (null == employee) {
                        ResultDTO<Object> resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "未查询到此导购或员工信息，导购id：" + photoOrderDTO.getGuideId() + "，保存新拍照下单失败！", null);
                        logger.info("saveNewPhotoOrder EXCEPTION，未查询到此导购或员工信息，导购id：" + photoOrderDTO.getGuideId() + "，保存新拍照下单失败！出参 ResultDTO:{}", resultDTO);
                        return resultDTO;
                    }
                    userId = employee.getEmpId();
                    appIdentityType = employee.getIdentityType();
                    mobile = employee.getMobile();
                    cityId = employee.getCityId();
                }
                List<MaterialListDO> combList = photoOrderDTO.getCombList();
                List<MaterialListDO> materialListSave = new ArrayList<>();
                List<MaterialListDO> materialListUpdate = new ArrayList<>();

                Long deliveryId = null;
                if (null != photoOrderDTO.getDeliveryId() && -1 != photoOrderDTO.getDeliveryId()) {
                    deliveryId = photoOrderDTO.getDeliveryId();
                }
                //*************************************增加地址信息**************************************
                if (0 == photoOrderDTO.getGoAddDeliveryAddressType() && (null == photoOrderDTO.getDeliveryId() || -1 == photoOrderDTO.getDeliveryId())) {
                    String provinceName = deliveryAddressService.findAreaNameByCode(photoOrderDTO.getProvince());
                    String cityName = deliveryAddressService.findAreaNameByCode(photoOrderDTO.getCity());
                    String countyName = deliveryAddressService.findAreaNameByCode(photoOrderDTO.getCounty());

                    DeliveryAddressDO deliveryAddressDO = new DeliveryAddressDO();
                    deliveryAddressDO.setReceiver(photoOrderDTO.getReceiverName());
                    deliveryAddressDO.setReceiverPhone(photoOrderDTO.getReceiverPhone());
                    deliveryAddressDO.setDeliveryProvince(provinceName);
                    deliveryAddressDO.setDeliveryCity(cityName);
                    deliveryAddressDO.setDeliveryCounty(countyName);
                    deliveryAddressDO.setDeliveryStreet(photoOrderDTO.getStreet());
                    deliveryAddressDO.setDetailedAddress(photoOrderDTO.getDetailedAddress());
                    deliveryAddressDO.setResidenceName(photoOrderDTO.getResidenceName());
                    if (null != proxyEmployee) {
                        deliveryAddressDO.setUserId(proxyEmployee.getEmpId());
                        deliveryAddressDO.setIdentityType(proxyEmployee.getIdentityType());
                    } else {
                        deliveryAddressDO.setUserId(userId);
                        deliveryAddressDO.setIdentityType(appIdentityType);
                    }
                    deliveryAddressDO.setStatus(Boolean.TRUE);
                    deliveryAddressDO.setIsDefault(Boolean.FALSE);
                    deliveryAddressDO.setEstateInfo(photoOrderDTO.getEstateInfo());

                    deliveryAddressService.maAddDeliveryAddress(deliveryAddressDO);
                    //地址id
                    deliveryId = deliveryAddressDO.getId();
                }
                //*******************************************************************************************

                //******************************************保存拍照下单实体********************************
                String orderNumber = OrderUtils.generatePhotoOrderNumber(cityId);
                PhotoOrderDO photoOrderDO = new PhotoOrderDO();
                photoOrderDO.setCreateTime(LocalDateTime.now());
                photoOrderDO.setIdentityType(appIdentityType);
                photoOrderDO.setContactPhone(photoOrderDTO.getContactPhone());
                photoOrderDO.setContactName(photoOrderDTO.getContactName());
                photoOrderDO.setPhotos(photoOrderDTO.getPhotoImgs());
                photoOrderDO.setRemark(photoOrderDTO.getRemark());
                photoOrderDO.setStatus(PhotoOrderStatus.PROCESSING);
                photoOrderDO.setUserId(userId);
                photoOrderDO.setPhotoOrderNo(orderNumber);
                photoOrderDO.setDeliveryId(deliveryId);
                photoOrderDO.setOrderType(PhotoOrderType.UNDERLINE);
                photoOrderDO.setProxyId(photoOrderDTO.getProxyId());
                this.photoOrderServiceImpl.save(photoOrderDO);

                //*****************************************************************************************

                for (MaterialListDO materialListDO : combList) {
                    if (null != materialListDO && null != materialListDO.getGid()) {
                        GoodsDO goodsDO = maGoodsService.findGoodsById(materialListDO.getGid());
                        if (null != goodsDO) {
                            MaterialListDO materialList = null;
                            if (null != proxyEmployee) {
                                materialList = maMaterialListService.findByUserIdAndIdentityTypeAndGoodsId(proxyEmployee.getEmpId(),
                                        proxyEmployee.getIdentityType(), materialListDO.getGid());
                            } else {
                                materialList = maMaterialListService.findByUserIdAndIdentityTypeAndGoodsId(userId,
                                        appIdentityType, materialListDO.getGid());
                            }
                            if (null == materialList) {
                                MaterialListDO materialListDOTemp = transformRepeat(goodsDO);
                                if (null != proxyEmployee) {
                                    materialListDOTemp.setUserId(proxyEmployee.getEmpId());
                                    materialListDOTemp.setIdentityType(proxyEmployee.getIdentityType());
                                } else {
                                    materialListDOTemp.setUserId(userId);
                                    materialListDOTemp.setIdentityType(appIdentityType);
                                }
                                materialListDOTemp.setDeliveryId(deliveryId);
                                materialListDOTemp.setQty(materialListDO.getQty());
                                materialListDOTemp.setRemark(photoOrderDTO.getRemark());
                                materialListDOTemp.setIsGenerateOrder("N");
                                materialListDOTemp.setMaterialListType(MaterialListType.NORMAL);
                                materialListSave.add(materialListDOTemp);
                            } else {
                                if (null != proxyEmployee) {
                                    materialList.setUserId(proxyEmployee.getEmpId());
                                    materialList.setIdentityType(proxyEmployee.getIdentityType());
                                }
                                materialList.setDeliveryId(deliveryId);
                                materialList.setRemark(photoOrderDTO.getRemark());
                                materialList.setIsGenerateOrder("N");
                                materialList.setQty(materialList.getQty() + materialListDO.getQty());
                                materialListUpdate.add(materialList);
                            }

                            PhotoOrderGoodsDO photoOrderGoodsDO = new PhotoOrderGoodsDO();
                            photoOrderGoodsDO.setGid(goodsDO.getGid());
                            photoOrderGoodsDO.setSkuName(goodsDO.getSkuName());
                            photoOrderGoodsDO.setGoodsQty(materialListDO.getQty());
                            photoOrderGoodsDO.setPhotoOrderNo(orderNumber);
                            photoOrderGoodsDOList.add(photoOrderGoodsDO);
                        }
                    }
                }
                PhotoOrderVO photoOrderVO = this.maPhotoOrderService.findByPhotoOrderNo(orderNumber);
                this.maPhotoOrderGoodsService.batchSave(photoOrderGoodsDOList);
                this.maPhotoOrderService.updateStatusAndsaveAndUpdateMaterialList(photoOrderVO.getId(), PhotoOrderStatus.FINISH, materialListSave, materialListUpdate);
                this.maPhotoOrderService.updateRemarkAndDeliveryId(photoOrderDTO.getRemark(), deliveryId, userId, appIdentityType);
                //短信提醒
//                    String info = "您的拍照下单订单(" + photoOrderVO.getPhotoOrderNo() + ")已处理，请登录APP查看。";
                String info = "您的拍照下单已完成，请到 APP 下料清单中继续完成支付！";
                String content;
                try {
                    content = URLEncoder.encode(info, "GB2312");
                    System.err.println(content);
                } catch (Exception e) {
                    e.printStackTrace();
                    ResultDTO<Object> resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知错误，短信验证码发送失败！", null);
                    logger.info("saveNewPhotoOrder EXCEPTION，提醒短信发送失败，出参 ResultDTO:{}", resultDTO);
                    logger.warn("{}", e);
                    return resultDTO;
                }
                SmsAccount account = smsAccountService.findOne();
                String returnCode;
                try {
                    returnCode = SmsUtils.sendMessageQrCode(account.getEncode(), account.getEnpass(), account.getUserName(), mobile, content);
                } catch (IOException e) {
                    ResultDTO<Object> resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "网络故障，提醒短信发送失败！", null);
                    logger.info("saveNewPhotoOrder EXCEPTION，提醒短信发送失败，出参 ResultDTO:{}", resultDTO);
                    logger.warn("{}", e);
                } catch (Exception e) {
                    ResultDTO<Object> resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知错误，短信验证码发送失败！", null);
                    logger.info("saveNewPhotoOrder EXCEPTION，提醒短信发送失败，出参 ResultDTO:{}", resultDTO);
                    logger.warn("{}", e);
                }

                return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
            }
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "信息错误！", null);
        } else {
            List<ObjectError> allErrors = result.getAllErrors();
            logger.warn("页面提交的数据有错误：errors = {}", errorMsgToHtml(allErrors));
            return new ResultDTO<>(CommonGlobal.COMMON_ERROR_PARAM_CODE,
                    errorMsgToHtml(allErrors), null);
        }
    }


    /**
     * @param
     * @return
     * @throws
     * @title 获取下料清单商品
     * @descripe
     */
    @GetMapping(value = "/material/goods")
    public ResultDTO<Object> findMaterialGoodsList(Long userId, String identityTypeValue, String updatePhotoOrderNo,String rankCode) {
        logger.info("findMaterialGoodsList,获取下料清单商品，入参 userId:{} identityTypeValue:{} updatePhotoOrderNo:{}", userId, identityTypeValue, updatePhotoOrderNo);

        if (null == userId) {
            logger.info("findMaterialGoodsList,获取下料清单商品，未获取到下单人ID，获取下料清单商品失败！userId：{}", userId);
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "未获取到下单人ID", null);
        }

        if (StringUtils.isBlank(identityTypeValue)) {
            logger.info("findMaterialGoodsList,获取下料清单商品，未获取到下单人身份类型，获取下料清单商品失败！identityTypeValue：{}", identityTypeValue);
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "未获取到下单人身份类型，修改下料清单失败！", null);
        }

        if (StringUtils.isBlank(updatePhotoOrderNo)) {
            logger.info("findMaterialGoodsList,获取下料清单商品，未获取到下单单号，获取下料清单商品失败！updatePhotoOrderNo：{}", updatePhotoOrderNo);
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "未获取到下单单号，修改下料清单失败！", null);
        }

        List<MaUpdateMaterialResponse> materialListDOList = null;
        PhotoOrderVO photoOrderVO = this.maPhotoOrderService.findByPhotoOrderNo(updatePhotoOrderNo);
        if (null != photoOrderVO) {

            if (null != photoOrderVO.getProxyId() && -1 != photoOrderVO.getProxyId()) {
                AppEmployee proxyEmployee = employeeService.findById(photoOrderVO.getProxyId());
                if (null == proxyEmployee) {
                    logger.warn("未查询到代下单人信息！带下单人id：{}", photoOrderVO.getProxyId());
                    return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "未查询到代下单人信息", null);
                }
                materialListDOList = maMaterialListService.findProxyMaterialListByPhotoNumber(proxyEmployee.getEmpId(), proxyEmployee.getIdentityType());
                if (null != materialListDOList && materialListDOList.size() > 0) {
                    for (MaUpdateMaterialResponse maUpdateMaterialResponse : materialListDOList) {
                        maUpdateMaterialResponse.setProxyId(photoOrderVO.getProxyId());
                    }
                }
            } else {
                if (!"CUSTOMER".equals(identityTypeValue)) {
                    identityTypeValue = "OTHER";
                }
                materialListDOList = this.maMaterialListService.findMaAllMaterialListByPhotoNumber(updatePhotoOrderNo, identityTypeValue,rankCode);
            }
            if (null == materialListDOList || materialListDOList.size() <= 0) {
                logger.warn("下料清单中未查询到商品信息！materialListDOList：{}", materialListDOList);
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "下料清单中未查询到商品信息", null);
            } else {
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, materialListDOList);
            }
        } else {
            logger.warn("未查询到该拍照下单信息！拍照单号：{}", updatePhotoOrderNo);
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "未查询到该拍照下单信息！", null);
        }
    }


    /**
     * @param
     * @return
     * @throws
     * @title 保存修改拍照下单
     * @descripe
     * @author GenerationRoad
     * @date 2018/2/22
     */
    @PostMapping(value = "/save/update")
    public ResultDTO<Object> saveUpdatePhotoOrder(@Valid PhotoOrderDTO photoOrderDTO, BindingResult result) {
        logger.info("savePhotoOrder,保存修改拍照下单，入参 categoryId:{}", photoOrderDTO);
        if (!result.hasErrors()) {
            if (0 == photoOrderDTO.getGoAddDeliveryAddressType() && (null == photoOrderDTO.getDeliveryId() || -1 == photoOrderDTO.getDeliveryId())) {
                if (StringUtils.isBlank(photoOrderDTO.getReceiverName())) {
                    ResultDTO<Object> resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "收货人姓名不能为空，保存拍照下单失败！", null);
                    logger.info("saveUpdatePhotoOrder EXCEPTION，收货人姓名不能为空，保存修改拍照下单失败，出参 ResultDTO:{}", resultDTO);
                    return resultDTO;
                }
                if (StringUtils.isBlank(photoOrderDTO.getReceiverPhone())) {
                    ResultDTO<Object> resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "收货人电话号码不能为空，保存拍照下单失败！", null);
                    logger.info("saveUpdatePhotoOrder EXCEPTION，收货人姓名不能为空，保存修改拍照下单失败，出参 ResultDTO:{}", resultDTO);
                    return resultDTO;
                }
                if (StringUtils.isBlank(photoOrderDTO.getProvince())) {
                    ResultDTO<Object> resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "省不能为空，保存拍照下单失败！", null);
                    logger.info("saveUpdatePhotoOrder EXCEPTION，省不能为空，保存修改拍照下单失败，出参 ResultDTO:{}", resultDTO);
                    return resultDTO;
                }
                if (StringUtils.isBlank(photoOrderDTO.getCity())) {
                    ResultDTO<Object> resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "市不能为空，保存拍照下单失败！", null);
                    logger.info("saveUpdatePhotoOrder EXCEPTION，市不能为空，保存修改拍照下单失败，出参 ResultDTO:{}", resultDTO);
                    return resultDTO;
                }
                if (StringUtils.isBlank(photoOrderDTO.getCounty())) {
                    ResultDTO<Object> resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "区不能为空，保存拍照下单失败！", null);
                    logger.info("saveUpdatePhotoOrder EXCEPTION，区不能为空，保存修改拍照下单失败，出参 ResultDTO:{}", resultDTO);
                    return resultDTO;
                }
                if (StringUtils.isBlank(photoOrderDTO.getStreet())) {
                    ResultDTO<Object> resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "街道不能为空，保存拍照下单失败！", null);
                    logger.info("saveUpdatePhotoOrder EXCEPTION，街道不能为空，保存修改拍照下单失败，出参 ResultDTO:{}", resultDTO);
                    return resultDTO;
                }
                if (StringUtils.isBlank(photoOrderDTO.getResidenceName())) {
                    ResultDTO<Object> resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "小区名不能为空，保存拍照下单失败！", null);
                    logger.info("saveUpdatePhotoOrder EXCEPTION，小区名不能为空，保存修改拍照下单失败，出参 ResultDTO:{}", resultDTO);
                    return resultDTO;
                }
                if (StringUtils.isBlank(photoOrderDTO.getDetailedAddress())) {
                    ResultDTO<Object> resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "详细地址不能为空，保存拍照下单失败！", null);
                    logger.info("saveUpdatePhotoOrder EXCEPTION，详细地址不能为空，保存修改拍照下单失败，出参 ResultDTO:{}", resultDTO);
                    return resultDTO;
                }
            }

            if (null == photoOrderDTO.getCombList() || photoOrderDTO.getCombList().size() <= 0) {
                ResultDTO<Object> resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "商品信息不能为空，请选择商品！", null);
                logger.info("saveUpdatePhotoOrder EXCEPTION，商品信息不能为空，保存修改拍照下单失败，出参 ResultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null != photoOrderDTO && null != photoOrderDTO.getPhotoId() && null != photoOrderDTO.getCombList() && photoOrderDTO.getCombList().size() > 0) {

                //查询拍照订单信息
                List<PhotoOrderStatus> status = new ArrayList<>();
                status.add(PhotoOrderStatus.PENDING);
                status.add(PhotoOrderStatus.PROCESSING);
                status.add(PhotoOrderStatus.FINISH);
                status.add(PhotoOrderStatus.CANCEL);
                PhotoOrderVO photoOrderVO = this.maPhotoOrderService.findByIdAndStatus(photoOrderDTO.getPhotoId(), status);
                AppEmployee employee = null;
                if (null != photoOrderDTO.getGuideId() && -1 != photoOrderDTO.getGuideId()) {
                    employee = employeeService.findById(photoOrderDTO.getGuideId());
                    if (null == employee) {
                        ResultDTO<Object> resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "未查询到此导购或员工信息，导购id：" + photoOrderDTO.getGuideId() + "，保存拍照下单失败！", null);
                        logger.info("saveUpdatePhotoOrder EXCEPTION，未查询到此导购或员工信息，导购id：" + photoOrderDTO.getGuideId() + "，保存修改拍照下单失败！出参 ResultDTO:{}", resultDTO);
                        return resultDTO;
                    }
                }
                if (null != photoOrderVO) {
                    AppEmployee proxyEmployee = null;
                    if (null != photoOrderVO.getProxyId() && photoOrderVO.getProxyId() != -1) {
                        proxyEmployee = employeeService.findById(photoOrderVO.getProxyId());
                        if (null == proxyEmployee) {
                            ResultDTO<Object> resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "未查询到此代下单人信息，代下单id：" + photoOrderDTO.getProxyId() + "，保存新拍照下单失败！", null);
                            logger.info("saveUpdatePhotoOrder EXCEPTION，未查询到此代下单人信息，代下单id：" + photoOrderDTO.getProxyId() + "，保存新拍照下单失败！出参 ResultDTO:{}", resultDTO);
                            return resultDTO;
                        }
                    }

                    List<MaterialListDO> combList = photoOrderDTO.getCombList();
                    List<MaterialListDO> materialListSave = new ArrayList<>();
                    List<MaterialListDO> materialListUpdate = new ArrayList<>();
                    List<MaterialChangeDetailLog> materialChangeDetailLogList = new ArrayList<>();
                    Long deliveryId = null;
                    if (null != photoOrderDTO.getDeliveryId() && -1 != photoOrderDTO.getDeliveryId()) {
                        deliveryId = photoOrderDTO.getDeliveryId();
                    }
                    //*************************************增加地址信息**************************************
                    if (0 == photoOrderDTO.getGoAddDeliveryAddressType() && (null == photoOrderDTO.getDeliveryId() || -1 == photoOrderDTO.getDeliveryId())) {
                        String provinceName = deliveryAddressService.findAreaNameByCode(photoOrderDTO.getProvince());
                        String cityName = deliveryAddressService.findAreaNameByCode(photoOrderDTO.getCity());
                        String countyName = deliveryAddressService.findAreaNameByCode(photoOrderDTO.getCounty());

                        DeliveryAddressDO deliveryAddressDO = new DeliveryAddressDO();
                        deliveryAddressDO.setReceiver(photoOrderDTO.getReceiverName());
                        deliveryAddressDO.setReceiverPhone(photoOrderDTO.getReceiverPhone());
                        deliveryAddressDO.setDeliveryProvince(provinceName);
                        deliveryAddressDO.setDeliveryCity(cityName);
                        deliveryAddressDO.setDeliveryCounty(countyName);
                        deliveryAddressDO.setDeliveryStreet(photoOrderDTO.getStreet());
                        deliveryAddressDO.setDetailedAddress(photoOrderDTO.getDetailedAddress());
                        deliveryAddressDO.setResidenceName(photoOrderDTO.getResidenceName());
                        if (null != proxyEmployee) {
                            deliveryAddressDO.setUserId(proxyEmployee.getEmpId());
                            deliveryAddressDO.setIdentityType(proxyEmployee.getIdentityType());
                        } else if (-1 != photoOrderDTO.getGuideId()) {
                            deliveryAddressDO.setUserId(employee.getEmpId());
                            deliveryAddressDO.setIdentityType(employee.getIdentityType());
                        } else {
                            deliveryAddressDO.setUserId(photoOrderVO.getUserId());
                            deliveryAddressDO.setIdentityType(photoOrderVO.getIdentityTypeValue());
                        }
                        deliveryAddressDO.setStatus(Boolean.TRUE);
                        deliveryAddressDO.setIsDefault(Boolean.FALSE);
                        deliveryAddressDO.setEstateInfo(photoOrderDTO.getEstateInfo());

                        deliveryAddressService.maAddDeliveryAddress(deliveryAddressDO);
                        //地址id
                        deliveryId = deliveryAddressDO.getId();
                    }
                    //*******************************************************************************************


                    for (MaterialListDO materialListDO : combList) {
                        if (null != materialListDO && null != materialListDO.getGid()) {
                            GoodsDO goodsDO = maGoodsService.findGoodsById(materialListDO.getGid());
                            if (null != goodsDO) {
                                MaterialListDO materialList = null;
                                if (null != proxyEmployee) {
                                    materialList = maMaterialListService.findByUserIdAndIdentityTypeAndGoodsId(proxyEmployee.getEmpId(),
                                            proxyEmployee.getIdentityType(), materialListDO.getGid());
                                } else {
                                    materialList = maMaterialListService.findByUserIdAndIdentityTypeAndGoodsId(photoOrderVO.getUserId(),
                                            photoOrderVO.getIdentityTypeValue(), materialListDO.getGid());
                                }
                                if (null == materialList) {
                                    MaterialListDO materialListDOTemp = transformRepeat(goodsDO);
                                    if (null != proxyEmployee) {
                                        materialListDOTemp.setUserId(proxyEmployee.getEmpId());
                                        materialListDOTemp.setIdentityType(proxyEmployee.getIdentityType());
                                    } else if (null != photoOrderDTO.getGuideId() && -1 != photoOrderDTO.getGuideId()) {
                                        materialListDOTemp.setUserId(employee.getEmpId());
                                        materialListDOTemp.setIdentityType(employee.getIdentityType());
                                    } else {
                                        materialListDOTemp.setUserId(photoOrderVO.getUserId());
                                        materialListDOTemp.setIdentityType(photoOrderVO.getIdentityTypeValue());
                                    }
                                    materialListDOTemp.setDeliveryId(deliveryId);
                                    materialListDOTemp.setQty(materialListDO.getQty());
                                    materialListDOTemp.setIsGenerateOrder("N");
                                    materialListDOTemp.setMaterialListType(MaterialListType.NORMAL);
                                    materialListSave.add(materialListDOTemp);
                                } else {
                                    if (null != proxyEmployee) {
                                        materialList.setUserId(proxyEmployee.getEmpId());
                                        materialList.setIdentityType(proxyEmployee.getIdentityType());
                                    }
                                    materialList.setIsGenerateOrder("N");
                                    materialList.setDeliveryId(deliveryId);
                                    materialList.setQty(materialListDO.getQty());
                                    materialListUpdate.add(materialList);
                                }

                                //创建修改明细日志
                                MaterialChangeDetailLog materialChangeDetailLog = new MaterialChangeDetailLog();
                                materialChangeDetailLog.setGid(goodsDO.getGid());
                                materialChangeDetailLog.setSkuName(goodsDO.getSkuName());
                                materialChangeDetailLog.setQty(materialListDO.getQty());
                                materialChangeDetailLogList.add(materialChangeDetailLog);
                            }
                        }
                    }
                    //修改处理人ID
                    ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
                    Long userId = null;
                    if (null != shiroUser) {
                        userId = shiroUser.getId();
                    }
                    //创建修改头信息日志
                    MaterialChangeHeadLog materialChangeHeadLog = new MaterialChangeHeadLog();
                    materialChangeHeadLog.setUserId(photoOrderVO.getUserId());
                    materialChangeHeadLog.setIdentityType(photoOrderVO.getIdentityTypeValue());
                    materialChangeHeadLog.setCreateTime(new Date());
                    materialChangeHeadLog.setUpdatePeopleId(userId);

                    if (null != photoOrderDTO.getGuideId() && -1 != photoOrderDTO.getGuideId()) {
                        this.maPhotoOrderService.updateRemarkAndDeliveryId(photoOrderDTO.getRemark(), deliveryId, employee.getEmpId(), employee.getIdentityType());
                    } else {
                        this.maPhotoOrderService.updateRemarkAndDeliveryId(photoOrderDTO.getRemark(), deliveryId, photoOrderVO.getUserId(), photoOrderVO.getIdentityTypeValue());
                    }
                    this.maMaterialListService.saveMaterialChangeHeadLogAndDetailLog(materialChangeHeadLog, materialChangeDetailLogList);

                    this.maPhotoOrderService.updateStatusAndsaveAndUpdateMaterialList(materialListSave, materialListUpdate);

                    //短信提醒
//                    String info = "您的拍照下单订单(" + photoOrderVO.getPhotoOrderNo() + ")已处理，请登录APP查看。";
                    String info = "您的拍照下单已完成，请到 APP 下料清单中继续完成支付！";
                    String content;
                    try {
                        content = URLEncoder.encode(info, "GB2312");
                        System.err.println(content);
                    } catch (Exception e) {
                        e.printStackTrace();
                        ResultDTO<Object> resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知错误，短信验证码发送失败！", null);
                        logger.info("saveUpdatePhotoOrder EXCEPTION，提醒短信发送失败，出参 ResultDTO:{}", resultDTO);
                        logger.warn("{}", e);
                        return resultDTO;
                    }
                    SmsAccount account = smsAccountService.findOne();
                    String returnCode;
                    try {
                        returnCode = SmsUtils.sendMessageQrCode(account.getEncode(), account.getEnpass(), account.getUserName(), photoOrderVO.getUserMobile(), content);
                    } catch (IOException e) {
                        ResultDTO<Object> resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "网络故障，提醒短信发送失败！", null);
                        logger.info("saveUpdatePhotoOrder EXCEPTION，提醒短信发送失败，出参 ResultDTO:{}", resultDTO);
                        logger.warn("{}", e);
                    } catch (Exception e) {
                        ResultDTO<Object> resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知错误，短信验证码发送失败！", null);
                        logger.info("saveUpdatePhotoOrder EXCEPTION，提醒短信发送失败，出参 ResultDTO:{}", resultDTO);
                        logger.warn("{}", e);
                    }

                    return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
                }
            }
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "信息错误！", null);
        } else {
            List<ObjectError> allErrors = result.getAllErrors();
            logger.warn("页面提交的数据有错误：errors = {}", errorMsgToHtml(allErrors));
            return new ResultDTO<>(CommonGlobal.COMMON_ERROR_PARAM_CODE,
                    errorMsgToHtml(allErrors), null);
        }
    }

    /**
     * 删除下料清单商品
     *
     * @param userId
     * @param identityTypeValue
     * @param sku
     * @return
     */
    @GetMapping(value = "/delete/material/goods")
    public ResultDTO<Object> deleteMaterialGoods(Long userId, String identityTypeValue, String sku, Long proxyId) {
        logger.info("deleteMaterialGoods 删除下料清单商品入参，userId:{},identityType:{},sku:{}", userId, identityTypeValue, sku);
        if (null == userId) {
            ResultDTO<Object> resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "userId不能为空，删除下料清单商品失败！", null);
            logger.info("deleteMaterialGoods EXCEPTION，userId不能为空，删除下料清单商品失败，出参 ResultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (StringUtils.isBlank(sku)) {
            ResultDTO<Object> resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "sku不能为空，删除下料清单商品失败！", null);
            logger.info("deleteMaterialGoods EXCEPTION，sku不能为空，删除下料清单商品失败，出参 ResultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (StringUtils.isBlank(identityTypeValue)) {
            ResultDTO<Object> resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "identityTypeValue不能为空，删除下料清单商品失败！", null);
            logger.info("deleteMaterialGoods EXCEPTION，identityTypeValue不能为空，删除下料清单商品失败，出参 ResultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null != proxyId && -1 != proxyId) {
            AppEmployee proxyEmployee = employeeService.findById(proxyId);
            if (null == proxyEmployee) {
                ResultDTO<Object> resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "未查询到代下单人信息，删除下料清单商品失败！", null);
                logger.info("deleteMaterialGoods EXCEPTION，未查询到代下单人信息，删除下料清单商品失败，出参 ResultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (proxyEmployee.getIdentityType() == AppIdentityType.SELLER) {
                identityTypeValue = "SELLER";
            } else if (proxyEmployee.getIdentityType() == AppIdentityType.DECORATE_MANAGER) {
                identityTypeValue = "DECORATE_MANAGER";
            }
            maMaterialListService.deleteMaterialListByUserIdAndIdentityTypeAndGoodsSku(proxyId, identityTypeValue, sku);
        } else {
            maMaterialListService.deleteMaterialListByUserIdAndIdentityTypeAndGoodsSku(userId, identityTypeValue, sku);
        }
        return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
    }

    /**
     * @param
     * @return
     * @throws
     * @title 获取代下单人列表
     * @descripe
     */
    @GetMapping(value = "/find/proxy")
    public GridDataVO<MaCreateOrderPeopleResponse> findProxyCreateOrderPeople(Integer offset, Integer size,
                                                                              String keywords, String selectProxyCreateOrderPeopleConditions) {
        logger.info("findProxyCreateOrderPeople 获取代下单人列表,入参 offset:{},size:{},keywords:{},peopleType:{}," +
                "selectProxyCreateOrderPeopleConditions:{}", offset, size, keywords, selectProxyCreateOrderPeopleConditions);
        try {

            size = getSize(size);
            Integer page = getPage(offset, size);
            PageInfo<MaCreateOrderPeopleResponse> maCreateOrderPeopleResponsePageInfo = null;
            List<MaCreateOrderPeopleResponse> maCreateOrderPeopleResponseList = null;

            if (StringUtils.isBlank(selectProxyCreateOrderPeopleConditions)) {
                selectProxyCreateOrderPeopleConditions = null;
            }
            maCreateOrderPeopleResponsePageInfo = maCustomerService.maFindProxyCreatePeople(page, size, selectProxyCreateOrderPeopleConditions);
            maCreateOrderPeopleResponseList = maCreateOrderPeopleResponsePageInfo.getList();

            logger.warn("findCreateOrderPeople ,获取下单人列表成功", maCreateOrderPeopleResponseList.size());
            return new GridDataVO<MaCreateOrderPeopleResponse>().transform(maCreateOrderPeopleResponseList, maCreateOrderPeopleResponsePageInfo.getTotal());

        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("findCreateOrderPeople EXCEPTION,发生未知错误，获取下单人列表失败");
            logger.warn("{}", e);
            return null;
        }
    }


    /**
     * 生成物料审核单编号
     *
     * @return 返回编号
     */
    public String createNumber() {
        //定义时间格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        //获取当前时间
        Date d = new Date();
        //转换为String
        String str = sdf.format(d);
        //获取6位随机数并转换为String
        String st = String.valueOf((int) ((Math.random() * 9 + 1) * 1000));
        return "SH" + str + st;
    }


    /**
     * 拍照下单检验库存
     *
     * @param photoOrderDTO
     * @param
     * @return
     */
    @RequestMapping(value = "/inspection/stock", method = RequestMethod.POST)
    public Map<String, Object> inspectionStock(@Valid PhotoOrderDTO photoOrderDTO) {
        logger.info("inspectionStock 拍照下单检验库存，入参  photoOrderDTO:{}", photoOrderDTO);
        Map<String, Object> map = new HashMap<>(5);
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
            JavaType productCouponSimpleInfo = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, ProductCouponSimpleInfo.class);
            //产品券信息
            List<ProductCouponSimpleInfo> productCouponList = new ArrayList<>();
            if (StringUtils.isNotBlank(photoOrderDTO.getProductCouponGoodss())) {
                productCouponList = objectMapper.readValue(photoOrderDTO.getProductCouponGoodss(), productCouponSimpleInfo);
            }
            if ((null == photoOrderDTO.getCombList() || photoOrderDTO.getCombList().size() <= 0) && (null == productCouponList || productCouponList.size() <= 0)){
                map.put("code", -1);
                map.put("message", "商品或产品券不能为空，请选择!");
                return map;
            }
        Long storeId = null;
        Long userId = null;

            if ("appPhotoOrder".equalsIgnoreCase(photoOrderDTO.getSource()) || "updatePhotoOrder".equals(photoOrderDTO.getSource())) {
                //查询拍照订单信息
                List<PhotoOrderStatus> status = new ArrayList<>();
                status.add(PhotoOrderStatus.PENDING);
                status.add(PhotoOrderStatus.PROCESSING);
                status.add(PhotoOrderStatus.FINISH);
                status.add(PhotoOrderStatus.CANCEL);
                PhotoOrderVO photoOrderVO = this.maPhotoOrderService.findByIdAndStatus(photoOrderDTO.getPhotoId(), status);

                if (null != photoOrderVO && 6 == photoOrderVO.getIdentityTypeValue().getValue()) {
                    AppCustomer customer = customerService.findById(photoOrderVO.getUserId());
                    storeId = customer.getStoreId();
                } else if (null != photoOrderVO && 2 == photoOrderVO.getIdentityTypeValue().getValue()) {
                    AppEmployee appEmployee = employeeService.findById(photoOrderVO.getUserId());
                    storeId = appEmployee.getStoreId();
                } else if (null != photoOrderVO && 0 == photoOrderVO.getIdentityTypeValue().getValue()) {
                    map.put("code", -1);
                    map.put("message", "导购下单请在App端进行支付！");
                    return map;
                }

                AppEmployee employee = null;
                if (null != photoOrderDTO.getGuideId() && -1 != photoOrderDTO.getGuideId()) {
                    employee = employeeService.findById(photoOrderDTO.getGuideId());
                    if (null == employee) {
                        logger.info("savePhotoOrder EXCEPTION，未查询到此下单人信息!");
                        map.put("code", -1);
                        map.put("message", "未查询到此下单人信息!");
                        return map;
                    } else {
                        storeId = employee.getStoreId();
                    }
                }
            } else if ("addPhotoOrder".equalsIgnoreCase(photoOrderDTO.getSource())) {
                if ("顾客".equals(photoOrderDTO.getPeopleIdentityType())) {
                    AppCustomer customer = customerService.findById(photoOrderDTO.getGuideId());
                    userId = customer.getCusId();
                    storeId = customer.getStoreId();
                } else if ("装饰公司经理".equals(photoOrderDTO.getPeopleIdentityType())) {
                    AppEmployee employee = employeeService.findById(photoOrderDTO.getGuideId());
                    userId = employee.getEmpId();
                    storeId = employee.getStoreId();
                }
            }





        //是否可以提交到购物车标志
        final boolean[] submitFlag = {true};
        AppStore store = storeService.findById(storeId);
        List<MaterialListDO> combList =null;
        if (null == photoOrderDTO.getCombList()){
            combList = new ArrayList<>();
        }else{
            combList = photoOrderDTO.getCombList();
        }
        if (null != productCouponList && productCouponList.size() > 0){
            for (ProductCouponSimpleInfo promotionSimpleInfo : productCouponList){
                Boolean flag = true;
                for (MaterialListDO materialListDO : combList){
                    if (materialListDO.getGid().equals(promotionSimpleInfo.getId())){
                        materialListDO.setQty(materialListDO.getQty() + promotionSimpleInfo.getQty());
                        flag = false;
                    }
                }
                if (flag){
                    MaterialListDO newMaterialListDO = new MaterialListDO();
                    newMaterialListDO.setGid(promotionSimpleInfo.getId());
                    newMaterialListDO.setQty(promotionSimpleInfo.getQty());
                    combList.add(newMaterialListDO);
                }
            }
        }
        List<Long> internalGidList = new ArrayList<>();
        List<String> internalSkuList = new ArrayList<>();
        for (MaterialListDO materialListDO : combList){
            if (null != materialListDO && null != materialListDO.getGid() && materialListDO.getQty() > 0) {
                internalGidList.add(materialListDO.getGid());
            }
        }
//        if (true) {
//            map.put("code", -1);
//            map.put("message", "发生未知异常，请联系管理员！");
//            return map;
//        }
        List<GoodsDO> goodsList = maGoodsService.findGoodsListByGidList(internalGidList);

        for (GoodsDO goodsDO : goodsList){

            for (MaterialListDO materialListDO : combList){
                if (goodsDO.getGid().equals(materialListDO.getGid())){
                    materialListDO.setSku(goodsDO.getSku());
                    materialListDO.setSkuName(goodsDO.getSkuName());
                }
            }
            internalSkuList.add(goodsDO.getSku());
        }
        List<CityInventory> cityInventoryList = new ArrayList<>(300);
        List<GoodsPrice> goodsPriceList = new ArrayList<>(300);
        if (null != store) {
            //查询全部内部编码对应的城市可用量
            cityInventoryList = cityInventoryService.findCityInventoryListByCityIdAndSkuList(store.getCityId(),
                    internalSkuList);
            //查询全部内部编码对应的门店价目表
            goodsPriceList = goodsPriceService.findGoodsPriceListByStoreIdAndSkuList(store.getStoreId(), internalSkuList);
        } else {
            //todo
            map.put("code", -1);
            map.put("message", "找不到该门店信息");
            return map;
        }

        //页面返回对象
        List<FitOrderExcelPageVO> pageVOList = new ArrayList<>();
        List<CityInventory> finalCityInventoryList = cityInventoryList;
        List<GoodsPrice> finalGoodsPriceList = goodsPriceList;
        combList.forEach(p -> {
            if (null != p.getGid()) {
                FitOrderExcelPageVO pageVO = new FitOrderExcelPageVO();
                pageVO.setQty(p.getQty());
                pageVO.setInternalCode(p.getSku());
                pageVO.setInternalName(p.getSkuName());
                //设置内部商品是否存在
                if (StringUtils.isBlank(p.getSku())) {
                    pageVO.setIsInternalCodeExists(false);
                } else {
                    if (goodsList.stream().map(GoodsDO::getSku).collect(Collectors.toList()).contains(p.getSku())) {
                        pageVO.setIsInternalCodeExists(true);
                    } else {
                        pageVO.setIsInternalCodeExists(false);
                    }
                }
                if (pageVO.getIsInternalCodeExists()) {
                    //设置库存相关信息
                    List<CityInventory> cityInventoryListTemp = finalCityInventoryList.stream().filter(q -> q.getSku().equals(p.getSku())).collect(Collectors.toList());
                    if (AssertUtil.isNotEmpty(cityInventoryListTemp)) {
                        pageVO.setInventory(cityInventoryListTemp.get(0).getAvailableIty() == null ? 0 : cityInventoryListTemp.get(0).getAvailableIty());
                    } else {
                        pageVO.setInventory(0);
                    }
                    if (pageVO.getInventory() >= pageVO.getQty()) {
                        pageVO.setIsInvEnough(true);
                        pageVO.setInvDifference(0);
                    } else {
                        pageVO.setIsInvEnough(false);
                        pageVO.setInvDifference(pageVO.getInventory() - pageVO.getQty());
                    }

                        //设置价目表是否存在
                        List<GoodsPrice> goodsPriceListTemp = finalGoodsPriceList.stream().filter(t -> t.getSku().equals(p.getSku())).collect(Collectors.toList());
                        if (AssertUtil.isNotEmpty(goodsPriceListTemp)) {
                            pageVO.setIsPriceItemExists(true);
                        } else {
                            pageVO.setIsPriceItemExists(false);
                        }
                    }
                    if (!pageVO.getIsInternalCodeExists()) {
                        pageVO.setErrorType(FitExcelImportGoodsErrorType.GOODS_NOT_EXISTS);
                    } else if (!pageVO.getIsInvEnough()) {
                        pageVO.setErrorType(FitExcelImportGoodsErrorType.INV_NOT_ENOUGH);
                    } else if (!pageVO.getIsPriceItemExists()) {
                        pageVO.setErrorType(FitExcelImportGoodsErrorType.PRICE_NOT_EXISTS);
                    }
                    if (!pageVO.getIsInternalCodeExists() || !pageVO.getIsInvEnough() || !pageVO.getIsPriceItemExists()) {
                        submitFlag[0] = false;
                    }
                    pageVOList.add(pageVO);
                }
            });
            map.put("submitFlag", submitFlag[0]);
            map.put("code", 0);
            map.put("content", pageVOList);
            return map;
        } catch (Exception e) {
            map.put("code", -1);
            map.put("message", "发生未知异常，请联系管理员！");
            e.printStackTrace();
            return map;
        }
    }

    /**
     * 根据导购id查询门店赠品列表
     *
     * @return
     */
    @RequestMapping(value = "/page/gifts",method = RequestMethod.POST)
    public ResultDTO<PromotionsListResponse> restGiftsPageBySellerId(@Valid PhotoOrderDTO photoOrderDTO){
        logger.info("restGiftsPageBySellerId 根据导购id查询门店赠品列表,入参 photoOrderDTO:{}",photoOrderDTO);
        if ((null == photoOrderDTO.getCombList() || photoOrderDTO.getCombList().size() <= 0) && (StringUtils.isBlank(photoOrderDTO.getProductCouponGoodss()))){
                logger.warn("商品信息为空");
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "商品信息为空", null);
        }
        if ((null == photoOrderDTO.getCombList() || photoOrderDTO.getCombList().size() <= 0) && (StringUtils.isNotBlank(photoOrderDTO.getProductCouponGoodss()))){
            logger.warn("产品券提货无促销");
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "", null);
        }

        try{
            AppCustomer appCustomer = null;
            PhotoOrderVO photoOrderVO = null;
            Long userId = null;
            Long customerId = null;
            Long storeId = null;
            AppIdentityType appIdentityType = null;
            if ("appPhotoOrder".equals(photoOrderDTO.getSource()) || "updatePhotoOrder".equals(photoOrderDTO.getSource())) {
                //查询拍照订单信息
                List<PhotoOrderStatus> status = new ArrayList<>();
                status.add(PhotoOrderStatus.PENDING);
                status.add(PhotoOrderStatus.PROCESSING);
                status.add(PhotoOrderStatus.FINISH);
                status.add(PhotoOrderStatus.CANCEL);
                photoOrderVO = this.maPhotoOrderService.findByIdAndStatus(photoOrderDTO.getPhotoId(), status);
                if (null != photoOrderVO && null != photoOrderVO.getIdentityTypeValue()) {
                    if (photoOrderVO.getIdentityTypeValue() == AppIdentityType.CUSTOMER) {
                        appCustomer = customerService.findById(photoOrderVO.getUserId());
                        if (null == appCustomer) {
                            logger.info("未查询到顾客信息，顾客id{}：", photoOrderVO.getUserId());
                            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "未查询到下单顾客信息，获取赠品信息失败！", null);
                        }
                        userId = appCustomer.getCusId();
                        customerId = appCustomer.getCusId();
                        storeId = appCustomer.getStoreId();
                        appIdentityType = AppIdentityType.CUSTOMER;
                    } else if (photoOrderVO.getIdentityTypeValue() == AppIdentityType.DECORATE_MANAGER) {
                        AppEmployee employee = employeeService.findById(photoOrderVO.getUserId());
                        userId = employee.getEmpId();
                        storeId = employee.getStoreId();
                        appIdentityType = employee.getIdentityType();
                    }
                } else {
                    return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "未查询到拍照下单信息或下单人身份类型为空！", null);
                }
            } else if ("addPhotoOrder".equals(photoOrderDTO.getSource())) {
                if ("顾客".equals(photoOrderDTO.getPeopleIdentityType())) {
                    AppCustomer customer = customerService.findById(photoOrderDTO.getGuideId());
                    userId = customer.getCusId();
                    customerId = customer.getCusId();
                    storeId = customer.getStoreId();
                    appIdentityType = AppIdentityType.CUSTOMER;
                } else if ("装饰公司经理".equals(photoOrderDTO.getPeopleIdentityType())) {
                    AppEmployee employee = employeeService.findById(photoOrderDTO.getGuideId());
                    userId = employee.getEmpId();
                    storeId = employee.getStoreId();
                    appIdentityType = employee.getIdentityType();
                }
            }
            //查询导购
//            AppEmployee appEmployee = employeeService.findById(appCustomer.getSalesConsultId());

            //创建促销查询所需商品参数list
            List<OrderGoodsSimpleResponse> orderGoodsSimpleResponseList = new ArrayList<>();
            for (MaterialListDO materialListDO : photoOrderDTO.getCombList()) {
                if (null != materialListDO && null != materialListDO.getGid()) {
                    OrderGoodsSimpleResponse orderGoodsSimpleResponse = new OrderGoodsSimpleResponse();
                    GoodsPrice goodsPrice = null;
                    if (appIdentityType == AppIdentityType.CUSTOMER) {
                        //根据商品id和门店id查询商品价格+ 顾客id
                        goodsPrice = goodsPriceService.findGoodsPriceByGoodsIDAndStoreID(materialListDO.getGid(), storeId, userId);
                    } else {
                        //根据商品id和门店id查询商品价格+ 员工id
                        goodsPrice = goodsPriceService.findGoodsPriceByGoodsIDAndStoreIDAndEmpId(materialListDO.getGid(), storeId, userId);
                    }

                    orderGoodsSimpleResponse.setId(materialListDO.getGid());
                    orderGoodsSimpleResponse.setSku(goodsPrice.getSku());
                    orderGoodsSimpleResponse.setGoodsQty(materialListDO.getQty());
                    orderGoodsSimpleResponse.setVipPrice(goodsPrice.getVIPPrice());
                    orderGoodsSimpleResponse.setRetailPrice(goodsPrice.getRetailPrice());
                    //将参数添加到list中
                    orderGoodsSimpleResponseList.add(orderGoodsSimpleResponse);
                }
            }
            //查询所有符合条件的促销
            PromotionsListResponse promotionsListResponse = appActService.countAct(userId, appIdentityType, orderGoodsSimpleResponseList, customerId, "GOODS");
            if (promotionsListResponse == null) {
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "无促销活动可参加！", null);
            }
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "促销查询成功", promotionsListResponse);
        } catch (Exception e) {
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，请联系管理员！", null);
        }
    }


    /**
     * 后台拍照下单创建订单方法
     *
     * @param photoOrderDTO 前台提交的订单相关参数
     * @param request       request对象
     * @return 订单创建结果
     */
    @RequestMapping(value = "/ma/photo/create", method = RequestMethod.POST)
    public ResultDTO<Object> maCreatePhotoOrder(@Valid PhotoOrderDTO photoOrderDTO, HttpServletRequest request) {
        logger.info("maCreatePhotoOrder CALLED,后台拍照下单创建订单方法,入参:{}", photoOrderDTO);

        ResultDTO<Object> resultDTO;
        //获取客户端ip地址

        if (null == photoOrderDTO.getDeliveryId() || -1 == photoOrderDTO.getDeliveryId()) {
            if (StringUtils.isBlank(photoOrderDTO.getReceiverName())) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "收货人姓名不能为空，保存拍照下单失败！", null);
                logger.info("saveUpdatePhotoOrder EXCEPTION，收货人姓名不能为空，保存修改拍照下单失败，出参 ResultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (StringUtils.isBlank(photoOrderDTO.getReceiverPhone())) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "收货人电话号码不能为空，保存拍照下单失败！", null);
                logger.info("saveUpdatePhotoOrder EXCEPTION，收货人姓名不能为空，保存修改拍照下单失败，出参 ResultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (StringUtils.isBlank(photoOrderDTO.getProvince())) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "省不能为空，保存拍照下单失败！", null);
                logger.info("saveUpdatePhotoOrder EXCEPTION，省不能为空，保存修改拍照下单失败，出参 ResultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (StringUtils.isBlank(photoOrderDTO.getCity())) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "市不能为空，保存拍照下单失败！", null);
                logger.info("saveUpdatePhotoOrder EXCEPTION，市不能为空，保存修改拍照下单失败，出参 ResultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (StringUtils.isBlank(photoOrderDTO.getCounty())) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "区不能为空，保存拍照下单失败！", null);
                logger.info("saveUpdatePhotoOrder EXCEPTION，区不能为空，保存修改拍照下单失败，出参 ResultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (StringUtils.isBlank(photoOrderDTO.getStreet())) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "街道不能为空，保存拍照下单失败！", null);
                logger.info("saveUpdatePhotoOrder EXCEPTION，街道不能为空，保存修改拍照下单失败，出参 ResultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (StringUtils.isBlank(photoOrderDTO.getResidenceName())) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "小区名不能为空，保存拍照下单失败！", null);
                logger.info("saveUpdatePhotoOrder EXCEPTION，小区名不能为空，保存修改拍照下单失败，出参 ResultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (StringUtils.isBlank(photoOrderDTO.getDetailedAddress())) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "详细地址不能为空，保存拍照下单失败！", null);
                logger.info("saveUpdatePhotoOrder EXCEPTION，详细地址不能为空，保存修改拍照下单失败，出参 ResultDTO:{}", resultDTO);
                return resultDTO;
            }
        }

        try {
            AppCustomer appCustomer = null;
            AppEmployee employee = null;
            PhotoOrderVO photoOrderVO = null;
            Long customerId = null;
            Long userId = null;
            Integer identityType = null;
            Long deliveryId = null;
            Long cityId = null;
            if ("appPhotoOrder".equals(photoOrderDTO.getSource()) || "updatePhotoOrder".equals(photoOrderDTO.getSource())){
            //查询拍照订单信息
            List<PhotoOrderStatus> status = new ArrayList<>();
            status.add(PhotoOrderStatus.PENDING);
            status.add(PhotoOrderStatus.PROCESSING);
            status.add(PhotoOrderStatus.FINISH);
            status.add(PhotoOrderStatus.CANCEL);
            photoOrderVO = this.maPhotoOrderService.findByIdAndStatus(photoOrderDTO.getPhotoId(), status);
            if (null != photoOrderVO){
                if (null == photoOrderVO.getIdentityTypeValue() || photoOrderVO.getIdentityTypeValue() == AppIdentityType.SELLER
                        || photoOrderVO.getIdentityTypeValue() == AppIdentityType.DELIVERY_CLERK
                        || photoOrderVO.getIdentityTypeValue() == AppIdentityType.DECORATE_EMPLOYEE
                        || photoOrderVO.getIdentityTypeValue() == AppIdentityType.ADMINISTRATOR   ){
                    logger.info("此下单人身份不支持后台创建订单，identity{}：",photoOrderVO.getIdentityTypeValue());
                    return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE,"此下单人身份不支持后台创建订单，后台拍照下单创建订单方法失败！",null);

                }
                if (null != photoOrderVO.getIdentityTypeValue() && photoOrderVO.getIdentityTypeValue() == AppIdentityType.CUSTOMER){
                    appCustomer = customerService.findById(photoOrderVO.getUserId());
                    if (null == appCustomer){
                        logger.info("未查询到顾客信息，顾客id{}：",photoOrderVO.getUserId());
                        return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE,"未查询到下单顾客信息，后台拍照下单创建订单方法失败！",null);
                    }
                    customerId = appCustomer.getCusId();
                    employee = employeeService.findById(appCustomer.getSalesConsultId());
                    userId = appCustomer.getCusId();
                    cityId = appCustomer.getCityId();
                    identityType = photoOrderVO.getIdentityTypeValue().getValue();
                }else if (null != photoOrderVO.getIdentityTypeValue() && photoOrderVO.getIdentityTypeValue() == AppIdentityType.DECORATE_MANAGER){
                    employee = employeeService.findById(photoOrderVO.getUserId());
                    userId = employee.getEmpId();
                    cityId = employee.getCityId();
                    identityType = photoOrderVO.getIdentityTypeValue().getValue();
                }else{
                    logger.info("下单人身份类型为空！：",photoOrderVO.getUserId());
                    return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE,"下单人身份类型为空，后台拍照下单创建订单方法失败！",null);

                    }
                } else {
                    return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "未查询到拍照下单信息！", null);
                }
            } else if ("addPhotoOrder".equals(photoOrderDTO.getSource())) {
                if ("装饰公司经理".equals(photoOrderDTO.getPeopleIdentityType())) {
                    employee = employeeService.findById(photoOrderDTO.getGuideId());
                    cityId = employee.getCityId();
                    userId = photoOrderDTO.getGuideId();
                    identityType = 2;
                } else if ("顾客".equals(photoOrderDTO.getPeopleIdentityType())) {
                    appCustomer = customerService.findById(photoOrderDTO.getGuideId());
                    employee = employeeService.findById(appCustomer.getSalesConsultId());
                    userId = photoOrderDTO.getGuideId();
                    identityType = 6;
                    cityId = appCustomer.getCityId();
                }
            }
//        Long userId = photoOrderVO.getUserId();
            if (null == userId) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不允许为空!", "");
                logger.warn("maCreatePhotoOrder OUT,后台拍照下单创建订单方法失败,出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }

        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户身份类型不允许为空!", "");
            logger.warn("maCreatePhotoOrder OUT,后台拍照下单创建订单方法失败,出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if ((null == photoOrderDTO.getCombList() || photoOrderDTO.getCombList().size() <= 0) && StringUtils.isBlank(photoOrderDTO.getProductCouponGoodss())) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "商品信息不允许为空!", "");
            logger.warn("maCreatePhotoOrder OUT,后台拍照下单创建订单方法失败,出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }


//        //判断创单人身份是否合法
//        if (orderParam.getIdentityType() != 2) {
//            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "创单人身份不合法!", "");
//            logger.warn("maCreatePhotoOrder OUT,后台拍照下单创建订单方法失败,出参 resultDTO:{}", resultDTO);
//            return resultDTO;
//        }


            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
//            JavaType goodsSimpleInfo = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, GoodsSimpleInfo.class);
//            JavaType cashCouponSimpleInfo = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, Long.class);
            JavaType productCouponSimpleInfo = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, ProductCouponSimpleInfo.class);
            JavaType promotionSimpleInfo = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, PromotionSimpleInfo.class);

            //*************************** 转化前台提交过来的json类型参数 ************************

            //商品信息
            List<GoodsSimpleInfo> goodsList = new ArrayList<>();

            if (null != photoOrderDTO.getCombList() && photoOrderDTO.getCombList().size() > 0) {
                for (MaterialListDO materialListDO : photoOrderDTO.getCombList()) {
                    if (null != materialListDO && null != materialListDO.getGid()) {
                        GoodsSimpleInfo goodsSimpleInfo = new GoodsSimpleInfo();
                        goodsSimpleInfo.setId(materialListDO.getGid());
                        goodsSimpleInfo.setQty(materialListDO.getQty());
                        goodsSimpleInfo.setGoodsLineType("GOODS");
                        goodsList.add(goodsSimpleInfo);
                    }
                }
            }
            String provinceName = null;
            String cityName = null;
            String countyName = null;
            String street = null;

            if (null == photoOrderDTO.getDeliveryId() || -1 == photoOrderDTO.getDeliveryId()) {
                provinceName = deliveryAddressService.findAreaNameByCode(photoOrderDTO.getProvince());
                cityName = deliveryAddressService.findAreaNameByCode(photoOrderDTO.getCity());
                countyName = deliveryAddressService.findAreaNameByCode(photoOrderDTO.getCounty());
                street = photoOrderDTO.getStreet();

            } else {
                DeliveryAddressResponse deliveryAddressResponse = deliveryAddressService.getDefaultDeliveryAddressByDeliveryId(photoOrderDTO.getDeliveryId());
                if (null == deliveryAddressResponse) {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "未查询到该地址信息！", null);
                    logger.info("maCreatePhotoOrder EXCEPTION，未查询到该地址信息，跳转确认订单失败，出参 ResultDTO:{}", resultDTO);
                    return resultDTO;
                }
                provinceName = deliveryAddressResponse.getDeliveryProvince();
                cityName = deliveryAddressResponse.getDeliveryCity();
                countyName = deliveryAddressResponse.getDeliveryCounty();
                street = deliveryAddressResponse.getDeliveryStreet();
            }

            //获取默认配送时间


            //配送信息
            DeliverySimpleInfo deliverySimpleInfo = new DeliverySimpleInfo();
            deliverySimpleInfo.setReceiver(photoOrderDTO.getReceiverName());
            deliverySimpleInfo.setReceiverPhone(photoOrderDTO.getReceiverPhone());
            deliverySimpleInfo.setDeliveryProvince(provinceName);
            deliverySimpleInfo.setDeliveryCity(cityName);
            deliverySimpleInfo.setDeliveryCounty(countyName);
            deliverySimpleInfo.setDeliveryStreet(street);
            deliverySimpleInfo.setResidenceName(photoOrderDTO.getResidenceName());
            deliverySimpleInfo.setEstateInfo(photoOrderDTO.getEstateInfo());
            deliverySimpleInfo.setDetailedAddress(photoOrderDTO.getDetailedAddress());
            deliverySimpleInfo.setDeliveryTime(photoOrderDTO.getPointDistributionTime());
            deliverySimpleInfo.setDeliveryType("HOUSE_DELIVERY");

            if (null == photoOrderDTO.getDeliveryId() || -1 == photoOrderDTO.getDeliveryId()) {
                DeliveryAddressDO deliveryAddressDO = new DeliveryAddressDO();
                deliveryAddressDO.setReceiver(photoOrderDTO.getReceiverName());
                deliveryAddressDO.setReceiverPhone(photoOrderDTO.getReceiverPhone());
                deliveryAddressDO.setDeliveryProvince(provinceName);
                deliveryAddressDO.setDeliveryCity(cityName);
                deliveryAddressDO.setDeliveryCounty(countyName);
                deliveryAddressDO.setDeliveryStreet(street);
                deliveryAddressDO.setDetailedAddress(photoOrderDTO.getDetailedAddress());
                deliveryAddressDO.setResidenceName(photoOrderDTO.getResidenceName());
                deliveryAddressDO.setUserId(userId);
                deliveryAddressDO.setIdentityType(AppIdentityType.getAppIdentityTypeByValue(identityType));
                deliveryAddressDO.setStatus(Boolean.TRUE);
                deliveryAddressDO.setIsDefault(Boolean.FALSE);
                deliveryAddressDO.setEstateInfo(photoOrderDTO.getEstateInfo());
                deliveryAddressService.maAddDeliveryAddress(deliveryAddressDO);
                deliveryId = deliveryAddressDO.getId();
            } else {
                deliveryId = photoOrderDTO.getDeliveryId();
            }


//            //优惠券信息
            List<Long> cashCouponList = new ArrayList<>();
//            if (StringUtils.isNotBlank(orderParam.getCashCouponIds())) {
//                cashCouponList = objectMapper.readValue(orderParam.getCashCouponIds(), cashCouponSimpleInfo);
//            }
            //产品券信息
            List<ProductCouponSimpleInfo> productCouponList = new ArrayList<>();
            //产品券信息
            List<ProductCouponSimpleInfo> couponList = new ArrayList<>();
            if (StringUtils.isNotBlank(photoOrderDTO.getProductCouponGoodss())) {
                couponList = objectMapper.readValue(photoOrderDTO.getProductCouponGoodss(), productCouponSimpleInfo);
                for (ProductCouponSimpleInfo coupon : couponList){
                    Boolean flag = true;
                    for (ProductCouponSimpleInfo materialListDO : productCouponList){
                        if (materialListDO.getId().equals(coupon.getId())){
                            materialListDO.setQty(materialListDO.getQty() + coupon.getQty());
                            flag = false;
                        }
                    }
                    if (flag){
                        ProductCouponSimpleInfo newMaterialListDO = new ProductCouponSimpleInfo();
                        newMaterialListDO.setId(coupon.getId());
                        newMaterialListDO.setQty(coupon.getQty());
                        productCouponList.add(newMaterialListDO);
                    }
                }
            }
//            //促销信息
            List<PromotionSimpleInfo> promotionSimpleInfoList = new ArrayList<>();
            if (6 == identityType) {
                if (StringUtils.isNotBlank(photoOrderDTO.getGiftDetails())) {
                    promotionSimpleInfoList = objectMapper.readValue(photoOrderDTO.getGiftDetails(), promotionSimpleInfo);
                }
            }
            // 检查促销是否过期
            List<Long> promotionIds = new ArrayList<>();
            if (null != promotionSimpleInfoList && promotionSimpleInfoList.size() > 0) {
                for (PromotionSimpleInfo promotion : promotionSimpleInfoList) {
                    promotionIds.add(promotion.getPromotionId());
                }
            }
            if (promotionIds.size() > 0) {
                Boolean outTime = actService.checkActOutTime(promotionIds);
                if (!outTime) {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "存在过期促销，请重新下单！", "");
                    logger.warn("createOrder OUT,创建订单失败,出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }
            }

            //账单信息
            BillingSimpleInfo billing = objectMapper.readValue(photoOrderDTO.getBillingMsg(), BillingSimpleInfo.class);

            if (null == billing) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "账单信息为空，请重新下单！", "");
                logger.warn("createOrder OUT,账单信息为空，创建订单失败,出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }


//            //如果是导购下单并且是四川直营门店，判断销售纸质单号是否为空
//            if (orderParam.getIdentityType() == AppIdentityType.SELLER.getValue()) {
//                AppEmployee employee = appEmployeeService.findById(orderParam.getUserId());
//                City city = cityService.findById(orderParam.getCityId());
//                AppStore appStore = appStoreService.findById(employee.getStoreId());
//                if ("ZY".equals(appStore.getStoreType().getValue()) && ("FZY009".equals(appStore.getStoreCode()) || "HLC004".equals(appStore.getStoreCode()) || "ML001".equals(appStore.getStoreCode()) || "QCMJ008".equals(appStore.getStoreCode()) ||
//                        "SB010".equals(appStore.getStoreCode()) || "YC002".equals(appStore.getStoreCode()) || "ZC002".equals(appStore.getStoreCode()) || "RC005".equals(appStore.getStoreCode()) ||
//                        "FZM007".equals(appStore.getStoreCode()) || "SH001".equals(appStore.getStoreCode()) || "YJ001".equals(appStore.getStoreCode()) || "HS001".equals(appStore.getStoreCode()) ||
//                        "XC001".equals(appStore.getStoreCode()))) {
//                    if (StringUtils.isBlank(orderParam.getSalesNumber())) {
//                        resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "四川直营门店销售纸质单号不能为空！", "");
//                        logger.warn("createOrder OUT,创建订单失败,出参 resultDTO:{}", resultDTO);
//                        return resultDTO;
//                    }
//                }
//            }

            //**********************************开始创建订单 **************************
            //******************* 根据商品确定订单表单号为 XN 或者 XNFW ********************
            List<Long> allGoodsList = new ArrayList<>();
            goodsList.forEach(g -> allGoodsList.add(g.getId()));
            productCouponList.forEach(p -> allGoodsList.add(p.getId()));
            String orderNumberType = appOrderService.returnType(allGoodsList, userId, identityType);

            //******************* 创建订单基础信息 *****************
            OrderBaseInfo orderBaseInfo = appOrderService.createOrderBaseInfo(cityId, userId,
                    identityType, customerId, deliverySimpleInfo.getDeliveryType(), photoOrderDTO.getRemark(), null);
            String oldOrderNumber = orderBaseInfo.getOrderNumber();
            oldOrderNumber = oldOrderNumber.replace("XN", orderNumberType);
            orderBaseInfo.setOrderNumber(oldOrderNumber);

            //****************** 创建订单物流信息 ******************
            OrderLogisticsInfo orderLogisticsInfo = appOrderService.createOrderLogisticInfo(deliverySimpleInfo);
            orderLogisticsInfo.setOrdNo(orderBaseInfo.getOrderNumber());

            //****************** 创建订单商品信息 ******************
            CreateOrderGoodsSupport support = commonService.createOrderGoodsInfo(goodsList, userId, identityType,
                    customerId, productCouponList, orderBaseInfo.getOrderNumber());

            //****************** 创建订单券信息 *********************
            List<OrderCouponInfo> orderCouponInfoList = new ArrayList<>();

            //****************** 创建订单优惠券信息 *****************
//            List<OrderCouponInfo> orderCashCouponInfoList = commonService.createOrderCashCouponInfo(orderBaseInfo, null);
//            if (null != orderCashCouponInfoList && orderCashCouponInfoList.size() > 0) {
//                orderCouponInfoList.addAll(orderCashCouponInfoList);
//            }
            //****************** 创建订单产品券信息 *****************
            List<OrderCouponInfo> orderProductCouponInfoList = commonService.createOrderProductCouponInfo(orderBaseInfo, support.getProductCouponGoodsList());
            if (null != orderProductCouponInfoList && orderProductCouponInfoList.size() > 0) {
                orderCouponInfoList.addAll(orderProductCouponInfoList);
            }

            //****************** 处理订单账单相关信息 ***************
            OrderBillingDetails orderBillingDetails = new OrderBillingDetails();
            orderBillingDetails.setOrderNumber(orderBaseInfo.getOrderNumber());
            orderBillingDetails.setIsOwnerReceiving(orderLogisticsInfo.getIsOwnerReceiving());
            orderBillingDetails.setTotalGoodsPrice(support.getGoodsTotalPrice());
            orderBillingDetails.setMemberDiscount(support.getMemberDiscount());
            orderBillingDetails.setPromotionDiscount(billing.getOrderDiscount());

            orderBillingDetails = appOrderService.createOrderBillingDetails(orderBillingDetails, userId, identityType,
                    billing, cashCouponList, support.getProductCouponGoodsList());

            orderBaseInfo.setTotalGoodsPrice(orderBillingDetails.getTotalGoodsPrice());

            //****************** 处理订单账单支付明细信息 ************
            List<OrderBillingPaymentDetails> paymentDetails = commonService.createOrderBillingPaymentDetails(orderBaseInfo, orderBillingDetails);

            /********* 开始计算分摊 促销分摊可能产生新的行记录 所以优先分摊 ******************/
            List<OrderGoodsInfo> orderGoodsInfoList;
            orderGoodsInfoList = dutchService.addGoodsDetailsAndDutch(userId, AppIdentityType.getAppIdentityTypeByValue(identityType), promotionSimpleInfoList, support.getPureOrderGoodsInfo(), customerId);

            //******** 分摊现乐币 策略：每个商品 按单价占比 分摊 *********************
            // 乐币暂时不分摊
//            Integer leBiQty = billing.getLeBiQuantity();
//            orderGoodsInfoList = leBiDutchService.LeBiDutch(leBiQty, orderGoodsInfoList);

            //******** 分摊现现金返利 策略：每个商品 按单价占比 分摊 *********************
            Double cashReturnAmount = billing.getStoreSubvention();
            orderGoodsInfoList = cashReturnDutchService.cashReturnDutch(cashReturnAmount, orderGoodsInfoList);

            //******** 分摊现金券 策略：使用范围商品 按单价占比 分摊 *********************
            orderGoodsInfoList = cashCouponDutchService.cashCouponDutch(cashCouponList, orderGoodsInfoList);

            //******** 分摊完毕 计算退货 单价 ***************************
            orderGoodsInfoList = dutchService.countReturnPrice(orderGoodsInfoList, cashReturnAmount, CountUtil.div(0, 10D), billing.getOrderDiscount());
            //orderGoodsInfoList = dutchService.countReturnPrice(orderGoodsInfoList);

            //将产品券商品加入 分摊完毕的商品列表中
            orderGoodsInfoList.addAll(support.getProductCouponGoodsList());
            support.setOrderGoodsInfoList(orderGoodsInfoList);

            //****************** 创建订单经销差价返还明细 ***********
            List<OrderJxPriceDifferenceReturnDetails> jxPriceDifferenceReturnDetailsList = commonService.createOrderJxPriceDifferenceReturnDetails(orderBaseInfo, support.getOrderGoodsInfoList(), promotionSimpleInfoList,orderProductCouponInfoList);
            if (null != jxPriceDifferenceReturnDetailsList && jxPriceDifferenceReturnDetailsList.size() > 0) {
                orderBillingDetails.setJxPriceDifferenceAmount(jxPriceDifferenceReturnDetailsList.stream().mapToDouble(OrderJxPriceDifferenceReturnDetails::getAmount).sum());
            }

            //**************** 创建要检核库存的商品和商品数量的Map ***********
            Map<Long, Integer> inventoryCheckMap = commonService.createInventoryCheckMap(orderGoodsInfoList);
            support.setInventoryCheckMap(inventoryCheckMap);

            //添加商品专供标志
            orderGoodsInfoList = this.commonService.addGoodsSign(orderGoodsInfoList, orderBaseInfo);
            //**************** 1、检查库存和与账单支付金额是否充足,如果充足就扣减相应的数量 ***********
            //**************** 2、持久化订单相关实体信息 ****************
            transactionalSupportService.createOrderBusiness(deliverySimpleInfo, support.getInventoryCheckMap(), cityId, identityType,
                    userId, customerId, cashCouponList, orderProductCouponInfoList, orderBillingDetails, orderBaseInfo,
                    orderLogisticsInfo, orderGoodsInfoList, orderCouponInfoList, paymentDetails, jxPriceDifferenceReturnDetailsList, null, promotionSimpleInfoList);

//            //****** 清空当单购物车商品 ******
//            maOrderService.clearOrderGoodsInMaterialList(orderParam.getUserId(), orderParam.getIdentityType(), goodsList, productCouponList);

            if ("appPhotoOrder".equals(photoOrderDTO.getSource())) {
                List<PhotoOrderGoodsDO> orderGoodsDOList = new ArrayList<>();
                for (OrderGoodsInfo goodsInfo : orderGoodsInfoList) {
                    PhotoOrderGoodsDO photoOrderGoodsDO = new PhotoOrderGoodsDO();
                    photoOrderGoodsDO.setGid(goodsInfo.getGid());
                    photoOrderGoodsDO.setSkuName(goodsInfo.getSkuName());
                    photoOrderGoodsDO.setGoodsQty(goodsInfo.getOrderQuantity());
                    photoOrderGoodsDO.setPhotoOrderNo(photoOrderVO.getPhotoOrderNo());
                    orderGoodsDOList.add(photoOrderGoodsDO);
                }
                this.maPhotoOrderGoodsService.batchSave(orderGoodsDOList);
                this.maPhotoOrderService.updateStatusAndsaveAndUpdateMaterialStatus(photoOrderDTO.getPhotoId(), PhotoOrderStatus.FINISH);
            } else if ("addPhotoOrder".equals(photoOrderDTO.getSource())) {
                //******************************************保存拍照下单实体********************************
                String orderNumber = OrderUtils.generatePhotoOrderNumber(orderBaseInfo.getCityId());
                PhotoOrderDO photoOrderDO = new PhotoOrderDO();
                photoOrderDO.setCreateTime(LocalDateTime.now());
                photoOrderDO.setIdentityType(AppIdentityType.getAppIdentityTypeByValue(identityType));
                photoOrderDO.setContactPhone(photoOrderDTO.getContactPhone());
                photoOrderDO.setContactName(photoOrderDTO.getContactName());
                photoOrderDO.setPhotos(photoOrderDTO.getPhotoImgs());
                photoOrderDO.setRemark(photoOrderDTO.getRemark());
                photoOrderDO.setStatus(PhotoOrderStatus.FINISH);
                photoOrderDO.setUserId(userId);
                photoOrderDO.setPhotoOrderNo(orderNumber);
                photoOrderDO.setDeliveryId(deliveryId);
                photoOrderDO.setOrderType(PhotoOrderType.UNDERLINE);
                photoOrderDO.setProxyId(photoOrderDTO.getProxyId());
                this.photoOrderServiceImpl.save(photoOrderDO);
                List<PhotoOrderGoodsDO> photoOrderGoodsDOList = new ArrayList<>();
                if (null != photoOrderDTO.getCombList() && photoOrderDTO.getCombList().size() > 0) {
                    for (MaterialListDO materialListDO : photoOrderDTO.getCombList()) {
                        if (null != materialListDO && null != materialListDO.getGid()) {
                            GoodsDO goodsDO = maGoodsService.findGoodsById(materialListDO.getGid());
                            if (null != goodsDO) {
                                PhotoOrderGoodsDO photoOrderGoodsDO = new PhotoOrderGoodsDO();
                                photoOrderGoodsDO.setGid(goodsDO.getGid());
                                photoOrderGoodsDO.setSkuName(goodsDO.getSkuName());
                                photoOrderGoodsDO.setGoodsQty(materialListDO.getQty());
                                photoOrderGoodsDO.setPhotoOrderNo(orderNumber);
                                photoOrderGoodsDO.setGoodsType("本品");
                                photoOrderGoodsDOList.add(photoOrderGoodsDO);
                            }
                        }
                    }
                }
                if (null != productCouponList && productCouponList.size() > 0){
                    for (ProductCouponSimpleInfo simpleInfo : productCouponList){
                        GoodsDO goodsDO = maGoodsService.findGoodsById(simpleInfo.getId());
                        if (null != goodsDO) {
                            PhotoOrderGoodsDO photoOrderGoodsDO = new PhotoOrderGoodsDO();
                            photoOrderGoodsDO.setGid(goodsDO.getGid());
                            photoOrderGoodsDO.setSkuName(goodsDO.getSkuName());
                            photoOrderGoodsDO.setGoodsQty(simpleInfo.getQty());
                            photoOrderGoodsDO.setPhotoOrderNo(orderNumber);
                            photoOrderGoodsDO.setGoodsType("产品券");
                            photoOrderGoodsDOList.add(photoOrderGoodsDO);
                        }
                    }
                }
                if (null != promotionSimpleInfoList && promotionSimpleInfoList.size() > 0){
                    for (PromotionSimpleInfo simpleInfo : promotionSimpleInfoList){
                        if (null != simpleInfo.getPresentInfo() && simpleInfo.getPresentInfo().size() > 0){
                            for (GoodsIdQtyParam goodsIdQtyParam : simpleInfo.getPresentInfo()){
                                GoodsDO goodsDO = maGoodsService.findGoodsById(goodsIdQtyParam.getId());
                                if (null != goodsDO) {
                                    PhotoOrderGoodsDO photoOrderGoodsDO = new PhotoOrderGoodsDO();
                                    photoOrderGoodsDO.setGid(goodsDO.getGid());
                                    photoOrderGoodsDO.setSkuName(goodsDO.getSkuName());
                                    photoOrderGoodsDO.setGoodsQty(goodsIdQtyParam.getQty());
                                    photoOrderGoodsDO.setPhotoOrderNo(orderNumber);
                                    photoOrderGoodsDO.setGoodsType("赠品");
                                    photoOrderGoodsDOList.add(photoOrderGoodsDO);
                                }
                            }
                        }
                    }
                }
                this.maPhotoOrderGoodsService.batchSave(photoOrderGoodsDOList);
            }


            if (orderBillingDetails.getAmountPayable() <= AppConstant.PAY_UP_LIMIT) {
                //如果预存款或信用金已支付完成直接发送到WMS出货单
                if (orderBaseInfo.getDeliveryType() == AppDeliveryType.HOUSE_DELIVERY) {
                    iCallWms.sendToWmsRequisitionOrderAndGoods(orderBaseInfo.getOrderNumber());
                }
                //将该订单入拆单消息队列
                maSinkSender.sendOrder(orderBaseInfo.getOrderNumber());
                //添加订单生命周期
                appOrderService.addOrderLifecycle(OrderLifecycleType.PAYED, orderBaseInfo.getOrderNumber());


                // 激活订单赠送的产品券
                // productCouponService.activateCusProductCoupon(orderBaseInfo.getOrderNumber());

                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "后台拍照下单创建订单方法成功",
                        new CreateOrderResponse(orderBaseInfo.getOrderNumber(), Double.parseDouble(CountUtil.retainTwoDecimalPlaces(orderBillingDetails.getAmountPayable())), true, false));
                logger.info("maCreatePhotoOrder OUT,后台拍照下单创建订单方法成功,出参 resultDTO:{}", resultDTO);
            } else {
                //判断是否可选择货到付款
//                Boolean isCashDelivery = this.commonService.checkCashDelivery(orderGoodsInfoList, userId, AppIdentityType.getAppIdentityTypeByValue(identityType));
                Boolean isCashDelivery = Boolean.FALSE;
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "后台拍照下单创建订单方法成功",
                        new CreateOrderResponse(orderBaseInfo.getOrderNumber(), Double.parseDouble(CountUtil.retainTwoDecimalPlaces(orderBillingDetails.getAmountPayable())), false, isCashDelivery));
                logger.info("maCreatePhotoOrder OUT,后台拍照下单创建订单方法成功,出参 resultDTO:{}", resultDTO);
            }
            return resultDTO;
        } catch (LockStoreInventoryException | LockStorePreDepositException | LockCityInventoryException | LockCustomerCashCouponException |
                LockCustomerLebiException | LockCustomerPreDepositException | LockEmpCreditMoneyException | LockStoreCreditMoneyException |
                LockStoreSubventionException | SystemBusyException | LockCustomerProductCouponException | GoodsMultipartPriceException | GoodsNoPriceException |
                OrderPayableAmountException | DutchException | OrderCreditMoneyException | OrderDiscountException | GoodsQtyErrorException e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, e.getMessage(), null);
            logger.warn("maCreatePhotoOrder OUT,后台拍照下单创建订单方法失败,出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        } catch (IOException e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "订单参数转换异常!", null);
            logger.warn("maCreatePhotoOrder EXCEPTION,后台拍照下单创建订单方法失败,出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        } catch (OrderSaveException e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "订单创建异常!", null);
            logger.warn("maCreatePhotoOrder EXCEPTION,后台拍照下单创建订单方法失败,出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常,下单失败!", null);
            logger.warn("maCreatePhotoOrder EXCEPTION,后台拍照下单创建订单方法失败,出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    /**
     * 确认订单页面
     *
     * @param photoOrderDTO
     * @return
     */
    @RequestMapping(value = "/order/detail", method = RequestMethod.POST)
    public ResultDTO<Object> orderDetail(@Valid PhotoOrderDTO photoOrderDTO) {
        logger.info("orderDetail  跳转确认订单入参  photoOrderDTO:{}", photoOrderDTO);
        ResultDTO<Object> resultDTO;
        try {
            String provinceName = null;
            String cityName = null;
            String countyName = null;
            String street = null;

        if ((null == photoOrderDTO.getCombList() || photoOrderDTO.getCombList().size() <= 0) && StringUtils.isBlank(photoOrderDTO.getProductCouponGoodss())){
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "商品信息不能为空，跳转确认订单失败！", null);
            logger.info("orderDetail EXCEPTION，商品信息不能为空，跳转确认订单失败，出参 ResultDTO:{}", resultDTO);
            return resultDTO;
        }

            if (null == photoOrderDTO.getDeliveryId() || -1 == photoOrderDTO.getDeliveryId()) {
                if (StringUtils.isBlank(photoOrderDTO.getReceiverName())) {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "收货人姓名不能为空，跳转确认订单失败！", null);
                    logger.info("orderDetail EXCEPTION，收货人姓名不能为空，跳转确认订单失败，出参 ResultDTO:{}", resultDTO);
                    return resultDTO;
                }
                if (StringUtils.isBlank(photoOrderDTO.getReceiverPhone())) {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "收货人电话号码不能为空，跳转确认订单失败！", null);
                    logger.info("orderDetail EXCEPTION，收货人姓名不能为空，跳转确认订单失败，出参 ResultDTO:{}", resultDTO);
                    return resultDTO;
                }
                if (StringUtils.isBlank(photoOrderDTO.getProvince())) {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "省不能为空，跳转确认订单失败！", null);
                    logger.info("orderDetail EXCEPTION，省不能为空，跳转确认订单失败，出参 ResultDTO:{}", resultDTO);
                    return resultDTO;
                }
                if (StringUtils.isBlank(photoOrderDTO.getCity())) {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "市不能为空，跳转确认订单失败！", null);
                    logger.info("orderDetail EXCEPTION，市不能为空，跳转确认订单失败，出参 ResultDTO:{}", resultDTO);
                    return resultDTO;
                }
                if (StringUtils.isBlank(photoOrderDTO.getCounty())) {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "区不能为空，跳转确认订单失败！", null);
                    logger.info("orderDetail EXCEPTION，区不能为空，跳转确认订单失败，出参 ResultDTO:{}", resultDTO);
                    return resultDTO;
                }
                if (StringUtils.isBlank(photoOrderDTO.getStreet())) {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "街道不能为空，跳转确认订单失败！", null);
                    logger.info("orderDetail EXCEPTION，街道不能为空，跳转确认订单失败，出参 ResultDTO:{}", resultDTO);
                    return resultDTO;
                }
                if (StringUtils.isBlank(photoOrderDTO.getResidenceName())) {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "小区名不能为空，跳转确认订单失败！", null);
                    logger.info("orderDetail EXCEPTION，小区名不能为空，跳转确认订单失败，出参 ResultDTO:{}", resultDTO);
                    return resultDTO;
                }
                if (StringUtils.isBlank(photoOrderDTO.getDetailedAddress())) {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "详细地址不能为空，跳转确认订单失败！", null);
                    logger.info("orderDetail EXCEPTION，详细地址不能为空，跳转确认订单失败，出参 ResultDTO:{}", resultDTO);
                    return resultDTO;
                }
                provinceName = deliveryAddressService.findAreaNameByCode(photoOrderDTO.getProvince());
                cityName = deliveryAddressService.findAreaNameByCode(photoOrderDTO.getCity());
                countyName = deliveryAddressService.findAreaNameByCode(photoOrderDTO.getCounty());
                street = photoOrderDTO.getStreet();
            } else {
                DeliveryAddressResponse deliveryAddressResponse = deliveryAddressService.getDefaultDeliveryAddressByDeliveryId(photoOrderDTO.getDeliveryId());
                if (null == deliveryAddressResponse) {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "未查询到该地址信息！", null);
                    logger.info("orderDetail EXCEPTION，未查询到该地址信息，跳转确认订单失败，出参 ResultDTO:{}", resultDTO);
                    return resultDTO;
                }
                provinceName = deliveryAddressResponse.getDeliveryProvince();
                cityName = deliveryAddressResponse.getDeliveryCity();
                countyName = deliveryAddressResponse.getDeliveryCounty();
                street = deliveryAddressResponse.getDeliveryStreet();
            }

            //拼接地址
            StringBuffer detailedAddress = new StringBuffer();
            detailedAddress.append(provinceName);
            detailedAddress.append(cityName);
            detailedAddress.append(countyName);
            detailedAddress.append(street);
            detailedAddress.append(photoOrderDTO.getResidenceName());
            detailedAddress.append(photoOrderDTO.getDetailedAddress());


            AppCustomer appCustomer = null;
            PhotoOrderVO photoOrderVO = null;
            Long userId = null;
            Integer identityType = null;

            List<GoodsIdQtyParam> goodsList = new ArrayList<>();
            if (AssertUtil.isNotEmpty(photoOrderDTO.getCombList())){
                for (MaterialListDO materialListDO : photoOrderDTO.getCombList()) {
                    if (null != materialListDO && null != materialListDO.getGid()) {
                        GoodsIdQtyParam goodsIdQtyParam = new GoodsIdQtyParam();
                        goodsIdQtyParam.setId(materialListDO.getGid());
                        goodsIdQtyParam.setQty(materialListDO.getQty());
                        goodsList.add(goodsIdQtyParam);
                    }
             }
            }
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
            JavaType goodsSimpleInfo = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, PromotionSimpleInfo.class);
            JavaType productCouponJavaType = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, GoodsIdQtyParam.class);

            List<PromotionSimpleInfo> giftList = objectMapper.readValue(photoOrderDTO.getGiftDetails(), goodsSimpleInfo);
//            List<PromotionSimpleInfo> giftList = photoOrderDTO.getGiftDetails();
            if ("appPhotoOrder".equals(photoOrderDTO.getSource()) || "updatePhotoOrder".equals(photoOrderDTO.getSource())) {
                //查询拍照订单信息
                List<PhotoOrderStatus> status = new ArrayList<>();
                status.add(PhotoOrderStatus.PENDING);
                status.add(PhotoOrderStatus.PROCESSING);
                status.add(PhotoOrderStatus.FINISH);
                status.add(PhotoOrderStatus.CANCEL);
                photoOrderVO = this.maPhotoOrderService.findByIdAndStatus(photoOrderDTO.getPhotoId(), status);
                if (null != photoOrderVO && null != photoOrderVO.getIdentityTypeValue()) {
                    if (photoOrderVO.getIdentityTypeValue() == AppIdentityType.CUSTOMER) {
                        appCustomer = customerService.findById(photoOrderVO.getUserId());
                        if (null == appCustomer) {
                            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "未查询到拍照下单人信息！", null);
                        }
                    }
                    userId = photoOrderVO.getUserId();
                    identityType = photoOrderVO.getIdentityTypeValue().getValue();
                } else {
                    return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "未查询到拍照下单信息或下单人身份类型为空！", null);
                }
            } else if ("addPhotoOrder".equals(photoOrderDTO.getSource())) {
                if ("装饰公司经理".equals(photoOrderDTO.getPeopleIdentityType())) {
                    userId = photoOrderDTO.getGuideId();
                    identityType = 2;
                } else if ("顾客".equals(photoOrderDTO.getPeopleIdentityType())) {
                    appCustomer = customerService.findById(photoOrderDTO.getGuideId());
                    userId = photoOrderDTO.getGuideId();
                    identityType = 6;
                }
            } else {
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "此下单人身份类型不支持后台支付功能！", null);
            }


//        List<GoodsIdQtyParam> couponList = goodsSimpleRequest.getProductCouponList();



        List<GoodsIdQtyParam> couponList = objectMapper.readValue(photoOrderDTO.getProductCouponGoodss(), productCouponJavaType);
//                List<GoodsSkuQtyParam> maGoodsList = objectMapper.readValue(maOrderCalulatedAmountRequest.getGoodsList(), goodsSimpleInfo);
//            List<GoodsSkuQtyParam> maGoodsList = maOrderCalulatedAmountRequest.getGoodsList();
            int goodsQty = 0;
            int giftQty = 0;
            int couponQty = 0;
                Double totalPrice = 0.00;
                Double memberDiscount = 0.00;
                Double orderDiscount = 0.00;
            Double proCouponDiscount = 0D;
                //运费暂时还没出算法
                Double freight = 0.00;
                Double totalOrderAmount = 0.00;
            List<Long> goodsIds = new ArrayList<Long>();
//                List<String> goodsSkus = new ArrayList<String>();
            List<Long> giftIds = new ArrayList<Long>();
            List<Long> couponIds = new ArrayList<Long>();
            List<GoodsIdQtyParam> giftsList = new ArrayList<>();
            List<OrderGoodsSimpleResponse> goodsInfo = null;
            List<OrderGoodsSimpleResponse> giftsInfo = null;
            List<OrderGoodsSimpleResponse> productCouponInfo = null;
//            List<CashCouponResponse> cashCouponResponseList = null;
//            Map<String, Object> goodsSettlement = new HashMap<>();
            Long cityId = 0L;
            AppStore appStore = null;
//            boolean isShowSalesNumber = false;
            if (identityType == 6) {
//
                cityId = appCustomer.getCityId();
                appStore = storeService.findById(appCustomer.getStoreId());
            } else if (identityType == 2) {
                AppEmployee employee = employeeService.findById(userId);
                cityId = employee.getCityId();
            }

            //取出所有本品的id和计算本品数量
            if (AssertUtil.isNotEmpty(goodsList)) {
                for (GoodsIdQtyParam aGoodsList : goodsList) {
                    goodsIds.add(aGoodsList.getId());
                    goodsQty = goodsQty + aGoodsList.getQty();
                }
            }
//                if (AssertUtil.isNotEmpty(maGoodsList)) {
//                    for (GoodsSkuQtyParam aGoodsList : maGoodsList) {
//                        goodsSkus.add(aGoodsList.getSku());
//                        goodsQty = goodsQty + aGoodsList.getQty();
//                    }
//                }
//            //取出所有赠品的id和计算赠品数量
            if (AssertUtil.isNotEmpty(giftList)) {
                for (PromotionSimpleInfo promotionSimpleInfo : giftList) {

                    if (null != promotionSimpleInfo.getPresentInfo()) {
                        giftsList.addAll(promotionSimpleInfo.getPresentInfo());
                        for (GoodsIdQtyParam goodsIdQtyParam : promotionSimpleInfo.getPresentInfo()) {
                            giftIds.add(goodsIdQtyParam.getId());
                            giftQty = giftQty + goodsIdQtyParam.getQty();
                        }
                    }
                }
            }
            //取出所有产品券商品的id和计算产品券商品数量
            if (AssertUtil.isNotEmpty(couponList)) {
                for (GoodsIdQtyParam couponSimpleInfo : couponList) {
                    couponIds.add(couponSimpleInfo.getId());
                    couponQty = couponQty + couponSimpleInfo.getQty();
                }
            }

            if (identityType == 6) {
                //获取商品信息
                goodsInfo = goodsService.findGoodsListByCustomerIdAndGoodsIdList(userId, goodsIds);
                //获取赠品信息
                giftsInfo = goodsService.findGoodsListByCustomerIdAndGoodsIdList(userId, giftIds);
                //获取产品券信息
                productCouponInfo = goodsService.findGoodsListByCustomerIdAndGoodsIdList(userId, couponIds);
            } else {
                //获取商品信息
                goodsInfo = goodsService.findGoodsListByEmployeeIdAndGoodsIdList(userId, goodsIds);
                //获取赠品信息
                giftsInfo = goodsService.findGoodsListByEmployeeIdAndGoodsIdList(userId, giftIds);
                //获取产品券信息
                productCouponInfo = goodsService.findGoodsListByEmployeeIdAndGoodsIdList(userId, couponIds);
            }

//                if (null == goodsInfo){
//                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "商品信息不能为空", null);
//                    logger.info("orderDetail OUT,跳转确认订单失败，出参 resultDTO:{}", resultDTO);
//                    return resultDTO;
//                }

               List<MaPhotoOrderGoodsDetailResponse> maPhotoOrderGoodsDetailResponseList = new ArrayList<>();
                if (null != goodsInfo && goodsInfo.size() >0) {
                    for (OrderGoodsSimpleResponse orderGoodsSimpleResponse : goodsInfo) {
                        if (null != orderGoodsSimpleResponse && null != orderGoodsSimpleResponse.getId()) {
                            for (GoodsIdQtyParam goodsIdQtyParam : goodsList) {
                                if (goodsIdQtyParam.getId().equals(orderGoodsSimpleResponse.getId())) {
                                    orderGoodsSimpleResponse.setGoodsQty(goodsIdQtyParam.getQty());
                                }
                            }
                            MaPhotoOrderGoodsDetailResponse maPhotoOrderGoodsDetailResponse = new MaPhotoOrderGoodsDetailResponse();
                            maPhotoOrderGoodsDetailResponse.setGid(orderGoodsSimpleResponse.getId());
                            maPhotoOrderGoodsDetailResponse.setSku(orderGoodsSimpleResponse.getSku());
                            maPhotoOrderGoodsDetailResponse.setSkuName(orderGoodsSimpleResponse.getGoodsName());
                            maPhotoOrderGoodsDetailResponse.setQty(orderGoodsSimpleResponse.getGoodsQty());
                            maPhotoOrderGoodsDetailResponse.setRetailPrice(orderGoodsSimpleResponse.getRetailPrice());
                            maPhotoOrderGoodsDetailResponse.setVipPrice(orderGoodsSimpleResponse.getVipPrice());
                            maPhotoOrderGoodsDetailResponse.setGoodsType("本品");
                            maPhotoOrderGoodsDetailResponseList.add(maPhotoOrderGoodsDetailResponse);
                        }
                    }
                }
                if (null != giftsInfo && giftsInfo.size() > 0) {
                    for (OrderGoodsSimpleResponse orderGoodsSimpleResponse : giftsInfo) {
                        Integer giftNum = 0;
                        if (null != giftList && giftList.size() > 0){
                            for (PromotionSimpleInfo promotionSimpleInfo : giftList){
                                if (null != promotionSimpleInfo.getPresentInfo() && promotionSimpleInfo.getPresentInfo().size() > 0){
                                    for (GoodsIdQtyParam goodsIdQtyParam : promotionSimpleInfo.getPresentInfo()){
                                        if (goodsIdQtyParam.getId().equals(orderGoodsSimpleResponse.getId())){
                                            giftNum += goodsIdQtyParam.getQty();
                                            orderGoodsSimpleResponse.setGoodsQty(giftNum);
                                        }
                                    }
                                }
                            }
                        }
                        MaPhotoOrderGoodsDetailResponse maPhotoOrderGoodsDetailResponse = new MaPhotoOrderGoodsDetailResponse();
                        maPhotoOrderGoodsDetailResponse.setGid(orderGoodsSimpleResponse.getId());
                        maPhotoOrderGoodsDetailResponse.setSku(orderGoodsSimpleResponse.getSku());
                        maPhotoOrderGoodsDetailResponse.setSkuName(orderGoodsSimpleResponse.getGoodsName());
                        maPhotoOrderGoodsDetailResponse.setQty(orderGoodsSimpleResponse.getGoodsQty());
                        maPhotoOrderGoodsDetailResponse.setRetailPrice(orderGoodsSimpleResponse.getRetailPrice());
                        maPhotoOrderGoodsDetailResponse.setVipPrice(orderGoodsSimpleResponse.getVipPrice());
                        maPhotoOrderGoodsDetailResponse.setGoodsType("赠品");
                        maPhotoOrderGoodsDetailResponseList.add(maPhotoOrderGoodsDetailResponse);
                    }
                }

            if (null != productCouponInfo && productCouponInfo.size() > 0) {
                for (OrderGoodsSimpleResponse orderGoodsSimpleResponse : productCouponInfo) {
                    Integer giftNum = 0;
                    if (null != couponList && couponList.size() > 0){
                                for (GoodsIdQtyParam goodsIdQtyParam : couponList){
                                    if (goodsIdQtyParam.getId().equals(orderGoodsSimpleResponse.getId())){
                                        giftNum += goodsIdQtyParam.getQty();
                                        orderGoodsSimpleResponse.setGoodsQty(giftNum);
                                    }
                                }
                    }
                    MaPhotoOrderGoodsDetailResponse maPhotoOrderGoodsDetailResponse = new MaPhotoOrderGoodsDetailResponse();
                    maPhotoOrderGoodsDetailResponse.setGid(orderGoodsSimpleResponse.getId());
                    maPhotoOrderGoodsDetailResponse.setSku(orderGoodsSimpleResponse.getSku());
                    maPhotoOrderGoodsDetailResponse.setSkuName(orderGoodsSimpleResponse.getGoodsName());
                    maPhotoOrderGoodsDetailResponse.setQty(orderGoodsSimpleResponse.getGoodsQty());
                    maPhotoOrderGoodsDetailResponse.setRetailPrice(orderGoodsSimpleResponse.getRetailPrice());
                    maPhotoOrderGoodsDetailResponse.setVipPrice(orderGoodsSimpleResponse.getVipPrice());
                    maPhotoOrderGoodsDetailResponse.setGoodsType("产品券");
                    maPhotoOrderGoodsDetailResponseList.add(maPhotoOrderGoodsDetailResponse);
                }
            }
                //加本品标识
                if (AssertUtil.isNotEmpty(goodsInfo)) {
                    for (OrderGoodsSimpleResponse simpleResponse : goodsInfo) {
                        for (GoodsIdQtyParam goodsIdQtyParam : goodsList) {
                            if (simpleResponse.getId().equals(goodsIdQtyParam.getId())) {
                                simpleResponse.setGoodsQty(goodsIdQtyParam.getQty());
                                break;
                            }
                        }
                        simpleResponse.setGoodsLineType(AppGoodsLineType.GOODS.getValue());
                        //算总金额
                        totalPrice = CountUtil.add(totalPrice, CountUtil.mul(simpleResponse.getRetailPrice(), simpleResponse.getGoodsQty()));
                        //算会员折扣(先判断是否是会员还是零售会员)
                        if (identityType == 2 || null != appCustomer.getCustomerType() && appCustomer.getCustomerType().equals(AppCustomerType.MEMBER)) {
                            memberDiscount = CountUtil.add(memberDiscount, CountUtil.mul(CountUtil.sub(simpleResponse.getRetailPrice(),
                                    simpleResponse.getVipPrice()), simpleResponse.getGoodsQty()));
                        }
                    }
                }
            // 本品集合 用来计算立减促销
            List<OrderGoodsSimpleResponse> bGoodsList = ArrayListUtils.deepCopyList(goodsInfo);

            //赠品的数量和标识
            if (AssertUtil.isNotEmpty(giftsInfo)) {
                for (GoodsIdQtyParam goodsIdQtyParam : giftsList) {
                    for (OrderGoodsSimpleResponse aGiftInfo : giftsInfo) {
                        if (aGiftInfo.getId().equals(goodsIdQtyParam.getId())) {
                            aGiftInfo.setGoodsQty(aGiftInfo.getGoodsQty() + goodsIdQtyParam.getQty());
                            aGiftInfo.setRetailPrice(0D);
                            break;
                        }
                        aGiftInfo.setGoodsLineType(AppGoodsLineType.PRESENT.getValue());
                    }
                }
                //合并商品和赠品集合
                goodsInfo.addAll(giftsInfo);
            }
            //产品券加标识
            if (AssertUtil.isNotEmpty(productCouponInfo)) {
                for (OrderGoodsSimpleResponse orderGoodsSimpleResponse : productCouponInfo) {
                    for (GoodsIdQtyParam goodsIdQtyParam : couponList) {
                        if (orderGoodsSimpleResponse.getId().equals(goodsIdQtyParam.getId())) {
                            orderGoodsSimpleResponse.setGoodsQty(goodsIdQtyParam.getQty());
                            break;
                        }
                    }
                    orderGoodsSimpleResponse.setGoodsLineType(AppGoodsLineType.PRODUCT_COUPON.getValue());
                    //算产品券总金额
                    proCouponDiscount = CountUtil.add(proCouponDiscount, CountUtil.mul(orderGoodsSimpleResponse.getRetailPrice(), orderGoodsSimpleResponse.getGoodsQty()));
                }
                //合并商品和赠品集合
                if (AssertUtil.isNotEmpty(goodsInfo)) {
                    goodsInfo.addAll(productCouponInfo);
                } else {
                    goodsInfo = productCouponInfo;
                }
            }

            //计算订单金额小计
            //********* 计算促销立减金额 *************
            if (identityType == 6) {
                List<PromotionDiscountListResponse> discountListResponseList = actService.countDiscount(userId, AppIdentityType.getAppIdentityTypeByValue(identityType), bGoodsList, appCustomer.getCusId(), "GOODS");
                for (PromotionDiscountListResponse discountResponse : discountListResponseList) {
                    orderDiscount = CountUtil.add(orderDiscount, discountResponse.getDiscountPrice());
                    PromotionSimpleInfo promotionSimpleInfo = new PromotionSimpleInfo();
                    promotionSimpleInfo.setPromotionId(discountResponse.getPromotionId());
                    promotionSimpleInfo.setDiscount(discountResponse.getDiscountPrice());
                    promotionSimpleInfo.setEnjoyTimes(discountResponse.getEnjoyTimes());
                    giftList.add(promotionSimpleInfo);
                }
            }
            totalOrderAmount = CountUtil.sub(totalPrice, memberDiscount, orderDiscount);


            Double storePreDeposit = 0.00;
            Double storeCreditMoney = 0.00;
            Double storeSubvention = 0.00;


            if (identityType == 2) {
                //获取装饰公司门店预存款，信用金，现金返利。
                storePreDeposit = storeService.findPreDepositBalanceByUserId(userId);
                storeCreditMoney = storeService.findCreditMoneyBalanceByUserId(userId);
                storeSubvention = storeService.findSubventionBalanceByUserId(userId);
            }

            //由于运费不抵扣乐币及优惠券,避免分摊出现负,运费放最后计算
            // 运费计算
            //2018-04-01 generation 产品卷金额加进运费计算
//                freight = deliveryFeeRuleService.countDeliveryFee(identityType, cityId, CountUtil.add(totalOrderAmount, 0), goodsInfo);
            freight = this.deliveryFeeRuleService.countDeliveryFeeNew(identityType, cityId, CountUtil.add(totalOrderAmount,proCouponDiscount), goodsInfo, countyName);
            totalOrderAmount = CountUtil.add(totalOrderAmount, freight);

            MaOrderCalulatedAmountResponse maOrderCalulatedAmountResponse = new MaOrderCalulatedAmountResponse();
            maOrderCalulatedAmountResponse.setTotalGoodsAmount(totalPrice == null ? 0.00 : totalPrice);
            maOrderCalulatedAmountResponse.setMemberDiscount(memberDiscount == null ? 0.00 : memberDiscount);
            maOrderCalulatedAmountResponse.setPromotionDiscount(orderDiscount);
            maOrderCalulatedAmountResponse.setTotalOrderAmount(totalOrderAmount);
            if (identityType == 2) {
                maOrderCalulatedAmountResponse.setStCreditMoney(storeCreditMoney == null ? 0.00 : storeCreditMoney);
                maOrderCalulatedAmountResponse.setStPreDeposit(storePreDeposit == null ? 0.00 : storePreDeposit);
                maOrderCalulatedAmountResponse.setStSubvention(storeSubvention == null ? 0.00 : storeSubvention);
            }
            maOrderCalulatedAmountResponse.setFreight(freight);

            //非门店自提,为城市库存充足及门店库存充足
            if (!AppDeliveryType.SELF_TAKE.equals(photoOrderDTO.getSysDeliveryType())) {
                //2018-04-01 generation 修改 提示所有城市库存不足的商品
                //判断库存的特殊处理
                List<Long> goodsIdList = appOrderService.existOrderGoodsInventory(cityId, goodsList, giftsList, null);
                if (goodsIdList != null && goodsIdList.size() > 0) {
                    String message = "商品 ";
                    for (Long gid : goodsIdList) {
                        GoodsDO goodsDO = goodsService.queryById(gid);
                        message += "“";
                        message += goodsDO.getSkuName();
                        message += "” ";
                    }
                    message += "仓库库存不足，请更改购买数量!";
                    //如果这里发现库存不足还是要返回去商品列表
//                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "该商品:" + goodsDO.getSkuName() + "商品库存不足！", goodsSettlement);
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, message, null);
                    logger.info("enterOrder OUT,跳转确认订单失败，出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }
            }

            //创建返回类
            MaPhotoOrderDetailResponse maPhotoOrderDetailResponse = new MaPhotoOrderDetailResponse();
            maPhotoOrderDetailResponse.setDetailedAddress(detailedAddress.toString());
            maPhotoOrderDetailResponse.setReceiverName(photoOrderDTO.getReceiverName());
            maPhotoOrderDetailResponse.setReceiverPhone(photoOrderDTO.getReceiverPhone());
            maPhotoOrderDetailResponse.setMaPhotoOrderGoodsDetailResponse(maPhotoOrderGoodsDetailResponseList);
            maPhotoOrderDetailResponse.setMaOrderCalulatedAmountResponse(maOrderCalulatedAmountResponse);
            maPhotoOrderDetailResponse.setCityName(cityName);
            maPhotoOrderDetailResponse.setIdentityType(identityType);

            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, maPhotoOrderDetailResponse);
            logger.info("orderDetail OUT,跳转确认订单成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;


        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常,跳转确认订单失败!", null);
            logger.warn("orderDetail EXCEPTION,跳转确认订单失败,出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    @RequestMapping(value = "/get/deliveryTime/{cityName}", method = RequestMethod.GET)
    public ResultDTO<Object> getCityDeliveryTime(@PathVariable(value = "cityName") String cityName) {
        logger.info("getCityDeliveryTime CALLED,获取城市配送时间列表，入参 cityName:{}", cityName);

        ResultDTO<Object> resultDTO;
        if (null == cityName) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "城市名不允许为空", null);
            logger.warn("getCityDeliveryTime OUT,获取城市配送时间列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            List<CityDeliveryTime> deliveryTimeList = cityService.findCityDeliveryTimeByCityName(cityName);

            List<String> futureDays = DateUtil.getFutureDays(6);
            List<CityDeliveryTimeResponse> responseList = new ArrayList<>();

            CityDeliveryTimeResponse responseDay1 = new CityDeliveryTimeResponse();
            List<CityDeliveryTime> day1DeliveryTime = new ArrayList<>();

            responseDay1.setDay(DateUtil.getFutureDate(0));
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            int hour = calendar.get(Calendar.HOUR_OF_DAY);

            for (int i = 0; i < deliveryTimeList.size(); i++) {
                if (i == 0) {
                    if (hour < deliveryTimeList.get(i).getStartHour()) {
                        deliveryTimeList.remove(0);
//                        Collections.copy(day1DeliveryTime, deliveryTimeList); copy方法报错:Source does not fit in dest 使用下面方法代替
                        day1DeliveryTime = new ArrayList<>(deliveryTimeList);
                        break;
                    }
                } else {
                    if (deliveryTimeList.get(i).getStartHour() > hour) {
                        day1DeliveryTime.add(deliveryTimeList.get(i));
                    }
                }
            }
            responseDay1.setDeliveryTime(transformDeliveryTimeToString(day1DeliveryTime));
            responseList.add(responseDay1);

            for (int i = 0; i < 6; i++) {
                CityDeliveryTimeResponse response = new CityDeliveryTimeResponse();
                response.setDay(futureDays.get(i));
                response.setDeliveryTime(transformDeliveryTimeToString(deliveryTimeList));
                responseList.add(response);
            }
            List<String> citydeliveryTimeList = new ArrayList<>();
            for (CityDeliveryTimeResponse cityDeliveryTimeResponse : responseList) {
                if (null != cityDeliveryTimeResponse && null != cityDeliveryTimeResponse.getDeliveryTime() && cityDeliveryTimeResponse.getDeliveryTime().size() > 0) {
                    for (String time : cityDeliveryTimeResponse.getDeliveryTime()) {
                        StringBuffer buffer = new StringBuffer();
                        buffer.append(cityDeliveryTimeResponse.getDay());
                        buffer.append(time);
                        citydeliveryTimeList.add(buffer.toString());
                    }
                }
            }
            resultDTO = new ResultDTO<Object>(CommonGlobal.COMMON_CODE_SUCCESS, null, citydeliveryTimeList);
            logger.info("getCityDeliveryTime CALLED,获取城市配送时间列表成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知异常,获取城市配送时间列表失败", null);
            logger.warn("customerLogin EXCEPTION,获取城市配送时间列表失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    public List<String> transformDeliveryTimeToString(List<CityDeliveryTime> deliveryTimeList) {
        List<String> resultList = new ArrayList<>();
        for (CityDeliveryTime deliveryTime : deliveryTimeList) {
            String s = (deliveryTime.getStartHour() < 10 ? "0" + deliveryTime.getStartHour() : deliveryTime.getStartHour()) +
                    ":" +
                    (deliveryTime.getStartMinute() < 10 ? "0" + deliveryTime.getStartMinute() : deliveryTime.getStartMinute()) +
                    "-" +
                    (deliveryTime.getEndHour() < 10 ? "0" + deliveryTime.getEndHour() : deliveryTime.getEndHour()) +
                    ":" +
                    (deliveryTime.getEndMinute() < 10 ? "0" + deliveryTime.getEndMinute() : deliveryTime.getEndMinute());
            resultList.add(s);
        }
        return resultList;
    }

    /**
     * 查询顾客产品券信息
     *
     * @return
     */
    @RequestMapping(value = "/find/customer/productCoupon",method = RequestMethod.GET)
    public ResultDTO<Object> findCustomerProductCouponPageByCustomerId(Long createPeopleId){
        logger.info("customerProductCoupon CALLED,获取顾客可用产品券，入参 cusId {}", createPeopleId);
        try {
            if (null == createPeopleId) {
                logger.info("customerProductCoupon OUT,顾客id为空，获取顾客可用产品券失败，出参 cusId:{}", createPeopleId);
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "顾客id为空，获取顾客可用产品券失败！", null);
            }

                List<ProductCouponResponse> productCouponList = customerService.findProductCouponByCustomerId(createPeopleId);
            if (null != productCouponList && productCouponList.size() >0) {
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "查询顾客产品券成功", productCouponList);
            }else{
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "该顾客没有可使用产品券", productCouponList);
            }

        } catch (Exception e) {
            logger.warn("customerProductCoupon EXCEPTION,获取顾客可用产品券失败，出参 resultDTO:{}");
            logger.warn("{}", e);
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知异常，查询顾客产品券信息失败！", null);
        }
    }

}

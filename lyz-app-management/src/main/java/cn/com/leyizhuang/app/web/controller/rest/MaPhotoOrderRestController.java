package cn.com.leyizhuang.app.web.controller.rest;

import cn.com.leyizhuang.app.core.config.shiro.ShiroUser;
import cn.com.leyizhuang.app.core.constant.AppDeliveryType;
import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.core.constant.MaterialListType;
import cn.com.leyizhuang.app.core.utils.SmsUtils;
import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.core.utils.order.OrderUtils;
import cn.com.leyizhuang.app.foundation.dto.PhotoOrderDTO;
import cn.com.leyizhuang.app.foundation.pojo.GridDataVO;
import cn.com.leyizhuang.app.foundation.pojo.MaterialListDO;
import cn.com.leyizhuang.app.foundation.pojo.PhotoOrderDO;
import cn.com.leyizhuang.app.foundation.pojo.PhotoOrderGoodsDO;
import cn.com.leyizhuang.app.foundation.pojo.goods.GoodsDO;
import cn.com.leyizhuang.app.foundation.pojo.management.MaterialChangeDetailLog;
import cn.com.leyizhuang.app.foundation.pojo.management.MaterialChangeHeadLog;
import cn.com.leyizhuang.app.foundation.pojo.management.goods.GoodsCategoryDO;
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
import cn.com.leyizhuang.app.foundation.vo.management.order.PhotoOrderVO;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.core.constant.PhotoOrderStatus;
import cn.com.leyizhuang.common.core.constant.PhotoOrderType;
import cn.com.leyizhuang.common.core.exception.data.InvalidDataException;
import cn.com.leyizhuang.common.foundation.pojo.SmsAccount;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageInfo;
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
            if ("顾客".equals(AppIdentityType.CUSTOMER.getDescription())) {
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
                                                                         String peopleType, String selectCreateOrderPeopleConditions) {
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
            maCreateOrderPeopleResponsePageInfo = maCustomerService.maFindCreatePeople(page, size, selectCreateOrderPeopleConditions, peopleType);
            maCreateOrderPeopleResponseList = maCreateOrderPeopleResponsePageInfo.getList();


            logger.warn("findCreateOrderPeople ,获取下单人列表成功", maCreateOrderPeopleResponseList.size());
            return new GridDataVO<MaCreateOrderPeopleResponse>().transform(maCreateOrderPeopleResponseList, maCreateOrderPeopleResponsePageInfo.getTotal());

//            if (null != guideId && -1 != guideId){
//                AppEmployee employee = employeeService.findById(guideId);
//                deliveryAddressResponseList = deliveryAddressService.getDefaultDeliveryAddressListByUserIdAndIdentityType(page,size,employee.getEmpId(),AppIdentityType.DECORATE_MANAGER,sellerAddressConditions);
//                deliveryAddressResponses = deliveryAddressResponseList.getList();
//                logger.warn("findAddressByUserMobile ,获取下单人地址库成功", deliveryAddressResponses.size());
//                return new GridDataVO<DeliveryAddressResponse>().transform(deliveryAddressResponses, deliveryAddressResponseList.getTotal());
//            }
//
//            if ("顾客".equals(identityType)){
//                AppCustomer customer =customerService.findByMobile(userMobile);
//                deliveryAddressResponseList = deliveryAddressService.getDefaultDeliveryAddressListByUserIdAndIdentityType(page,size,customer.getCusId(),AppIdentityType.CUSTOMER,sellerAddressConditions);
//                deliveryAddressResponses = deliveryAddressResponseList.getList();
//            }else if ("装饰公司经理".equals(identityType)){
//                AppEmployee employee = employeeService.findByMobile(userMobile);
//                deliveryAddressResponseList = deliveryAddressService.getDefaultDeliveryAddressListByUserIdAndIdentityType(page,size,employee.getEmpId(),AppIdentityType.DECORATE_MANAGER,sellerAddressConditions);
//                deliveryAddressResponses = deliveryAddressResponseList.getList();
//            }else if ("导购".equals(identityType)){
//                AppEmployee employee = employeeService.findByMobile(userMobile);
//                deliveryAddressResponseList = deliveryAddressService.getDefaultDeliveryAddressListByUserIdAndIdentityType(page,size,employee.getEmpId(),AppIdentityType.SELLER,sellerAddressConditions);
//                deliveryAddressResponses = deliveryAddressResponseList.getList();
//            }
//            logger.warn("findCreateOrderPeople ,获取下单人列表成功", deliveryAddressResponses.size());
//            return new GridDataVO<DeliveryAddressResponse>().transform(deliveryAddressResponses, deliveryAddressResponseList.getTotal());
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
    public ResultDTO<Object> findMaterialGoodsList(Long userId, String identityTypeValue, String updatePhotoOrderNo) {
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
                if (null != materialListDOList && materialListDOList.size() > 0){
                    for (MaUpdateMaterialResponse maUpdateMaterialResponse : materialListDOList){
                        maUpdateMaterialResponse.setProxyId(photoOrderVO.getProxyId());
                    }
                }
            } else {
                if (!"CUSTOMER".equals(identityTypeValue)) {
                    identityTypeValue = "OTHER";
                }
                materialListDOList = this.maMaterialListService.findMaAllMaterialListByPhotoNumber(updatePhotoOrderNo, identityTypeValue);
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

}

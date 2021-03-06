package cn.com.leyizhuang.app.web.controller.materialList;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.core.constant.MaterialListType;
import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.pojo.MaterialListDO;
import cn.com.leyizhuang.app.foundation.pojo.QuickOrderRelationDO;
import cn.com.leyizhuang.app.foundation.pojo.goods.GoodsDO;
import cn.com.leyizhuang.app.foundation.pojo.order.MaterialAuditSheet;
import cn.com.leyizhuang.app.foundation.pojo.request.GoodsIdQtyParam;
import cn.com.leyizhuang.app.foundation.pojo.response.materialList.CouponMaterialListResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.materialList.MaterialCustomerCouponResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.materialList.MaterialWorkerAuditResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.materialList.NormalMaterialListResponse;
import cn.com.leyizhuang.app.foundation.pojo.user.AppCustomer;
import cn.com.leyizhuang.app.foundation.service.*;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * @author GenerationRoad
 * @date 2017/10/18
 */
@RestController
@RequestMapping("/app/materialList")
public class MaterialListController {
    private static final Logger logger = LoggerFactory.getLogger(MaterialListController.class);

    @Autowired
    private MaterialListService materialListServiceImpl;

    @Resource
    private MaterialAuditSheetService materialAuditSheetService;

    @Autowired
    private QuickOrderRelationService quickOrderRelationServiceImpl;

    @Autowired
    private AppCustomerService customerService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private CommonService commonService;

    @Autowired
    private AppOrderService appOrderServiceImpl;

    /**
     * @param
     * @return
     * @throws
     * @title 单或多个商品加入下料清单
     * @descripe
     * @author GenerationRoad
     * @date 2017/10/18
     */
    @PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> addMaterialList(Long userId, Integer identityType, String params) {
        logger.info("addMaterialList CALLED,单或多个商品加入下料清单，入参 userId:{} identityType:{} params:{}", userId, identityType, params);
        ResultDTO<Object> resultDTO;
        try {
            if (null == userId) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "userId不能为空！", null);
                logger.info("addMaterialList OUT,单或多个商品加入下料清单失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == identityType) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空！",
                        null);
                logger.info("addMaterialList OUT,单或多个商品加入下料清单失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == params) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "商品和数量信息不能为空！", null);
                logger.info("addMaterialList OUT,单或多个商品加入下料清单失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            Map<Long, Integer> goodsMap = new HashMap<Long, Integer>();
            String[] param = params.split(",");
            for (String s : param) {
                String[] goodsParam = s.split("-");
                goodsMap.put(Long.parseLong(goodsParam[0]), Integer.parseInt(goodsParam[1]));
            }
            List<MaterialListDO> materialListSave = new ArrayList<>();
            List<MaterialListDO> materialListUpdate = new ArrayList<>();
            for (Map.Entry<Long, Integer> entry : goodsMap.entrySet()) {
                GoodsDO goodsDO = goodsService.findGoodsById(entry.getKey());
                if (null != goodsDO) {
                    //查询经理普通商品
                    MaterialListDO materialListDO = materialListServiceImpl.findByUserIdAndIdentityTypeAndGoodsId(userId,
                            AppIdentityType.getAppIdentityTypeByValue(identityType), entry.getKey());
                    //查询经理料单商品
                    MaterialListDO materialAuditListDO = materialListServiceImpl.findAuditListByUserIdAndIdentityTypeAndGoodsId(userId,
                            AppIdentityType.getAppIdentityTypeByValue(identityType), entry.getKey());
                    if (null == materialListDO && null == materialAuditListDO) {
                        MaterialListDO materialListDOTemp = transformRepeat(goodsDO);
                        materialListDOTemp.setUserId(userId);
                        materialListDOTemp.setIdentityType(AppIdentityType.getAppIdentityTypeByValue(identityType));
                        materialListDOTemp.setQty(entry.getValue());
                        materialListDOTemp.setMaterialListType(MaterialListType.NORMAL);
                        materialListSave.add(materialListDOTemp);
                    } else {
                        if (null != materialListDO) {
                            materialListDO.setQty(materialListDO.getQty() + entry.getValue());
                            materialListUpdate.add(materialListDO);
                        }
                        if (null != materialAuditListDO) {
                            materialAuditListDO.setQty(materialAuditListDO.getQty() + entry.getValue());
                            materialListUpdate.add(materialAuditListDO);
                        }
                    }
                } else {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "id为" + entry.getKey() + "" +
                            "的商品不存在!", null);
                }
            }
            commonService.saveAndUpdateMaterialList(materialListSave, materialListUpdate);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
            logger.info("addMaterialList OUT,单或多个商品加入下料清单成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知异常,单或多个商品加入下料清单失败!", null);
            logger.warn("addMaterialList EXCEPTION,单或多个商品加入下料清单失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    /**
     * @param
     * @return
     * @throws
     * @title 编辑下料清单商品数量
     * @descripe
     * @author GenerationRoad
     * @date 2017/10/18
     */
    @PostMapping(value = "/edit/number", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> editNumber(Long userId, Integer identityType, Long materialListId, Integer qty) {
        logger.info("editNumber CALLED,编辑下料清单商品数量，入参 userId:{}, identityType:{}, materialListId:{}, qty{}", userId, identityType, materialListId, qty);
        ResultDTO<Object> resultDTO;
        try {
            if (null == userId) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "userId不能为空！", null);
                logger.info("editNumber OUT,编辑下料清单商品数量失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == identityType) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空！",
                        null);
                logger.info("editNumber OUT,编辑下料清单商品数量失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == materialListId) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "商品ID不能为空！", null);
                logger.info("editNumber OUT,编辑下料清单商品数量失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == qty || qty <= 0) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "商品数量不能为空！", null);
                logger.info("editNumber OUT,编辑下料清单商品数量失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }

            this.materialListServiceImpl.modifyQty(materialListId, qty);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
            logger.info("editNumber OUT,编辑下料清单商品数量成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知异常,编辑下料清单商品数量失败!", null);
            logger.warn("editNumber EXCEPTION,编辑下料清单商品数量失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    /**
     * @param
     * @return
     * @throws
     * @title 删除下料清单商品
     * @descripe
     * @author GenerationRoad
     * @date 2017/10/18
     */
    @PostMapping(value = "/delete", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> deleteMaterialList(Long userId, Integer identityType, String materialListIds) {
        logger.info("deleteMaterialList CALLED,删除下料清单商品，入参 userId:{} identityType:{} materialListId:{}", userId, identityType, materialListIds);

        ResultDTO<Object> resultDTO;
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "userId不能为空！", null);
            logger.info("deleteMaterialList OUT,删除下料清单商品失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == materialListIds) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "下料清单商品ID不能为空！", null);
            logger.info("deleteMaterialList OUT,删除下料清单商品失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        String[] param = materialListIds.split(",");
        List<Long> list = new ArrayList<Long>();
        for (int i = 0; i < param.length; i++) {
            list.add(Long.parseLong(param[i]));
        }
        this.materialListServiceImpl.deleteMaterialList(list);
        resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
        logger.info("deleteMaterialList OUT,删除下料清单商品成功，出参 resultDTO:{}", resultDTO);
        return resultDTO;
    }


    /**
     * @param
     * @return
     * @throws
     * @title 下料清单列表
     * @descripe
     * @author GenerationRoad
     * @date 2017/10/25
     */
    @PostMapping(value = "/list", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> getMaterialList(Long userId, Integer identityType) throws UnsupportedEncodingException {
        logger.info("getMaterialList CALLED,获取下料清单列表，入参 userId:{} identityType:{}", userId, identityType);
        ResultDTO<Object> resultDTO;
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "userId不能为空！", null);
            logger.info("getMaterialList OUT,获取下料清单列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型错误！", null);
            logger.info("getMaterialList OUT,获取下料清单列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        //所有用户返回自己的商品
        List<NormalMaterialListResponse> normalMaterialListRespons = this.materialListServiceImpl.findByUserIdAndIdentityType(userId, identityType);
        //设置备注信息
        String remark = "";
        Long deliveryId = null;
        for (NormalMaterialListResponse response : normalMaterialListRespons) {
            if (null != response.getDeliveryId()){
                deliveryId = response.getDeliveryId();
            }
            if (StringUtils.isNotBlank(response.getRemark())) {
                remark = response.getRemark();
                break;
            }
        }
        //创建工人料单返回对象
        MaterialWorkerAuditResponse materialWorkerAuditResponse = new MaterialWorkerAuditResponse();
        //创建顾客产品券返回对象
        MaterialCustomerCouponResponse materialCustomerCouponResponse = new MaterialCustomerCouponResponse();

        Map<String, Object> returnMap = new HashMap<>(4);
        AppIdentityType appIdentityType = AppIdentityType.getAppIdentityTypeByValue(identityType);

        if (identityType == 2) {
            //查询的是所有的商品下料清单（这个集合对象中的料单号和类型是一样的）
            List<NormalMaterialListResponse> materialListDOS = materialListServiceImpl.findMaterialListByUserIdAndTypeAndAuditIsNotNull(userId, appIdentityType);

            if (null != materialListDOS && !materialListDOS.isEmpty()) {
                //只需得到一个料单对象中料单编号
                MaterialListDO materialListDO = materialListServiceImpl.findAuditListByUserIdAndIdentityTypeAndGoodsId(userId,
                        appIdentityType, materialListDOS.get(0).getGoodsId());
                String auditNo = materialListDO.getAuditNo();
                //可以得到一个审核料单
                MaterialAuditSheet materialAuditSheet = materialAuditSheetService.queryByAuditNo(auditNo);

                //返回工人料单商品
                materialWorkerAuditResponse.setGoodsList(materialListDOS);
                materialWorkerAuditResponse.setAuditNo(auditNo);
                materialWorkerAuditResponse.setWorker(materialAuditSheet.getEmployeeName());
                materialWorkerAuditResponse.setRemark(materialAuditSheet.getRemark());
                materialWorkerAuditResponse.setReceiver(materialAuditSheet.getReceiver());
                materialWorkerAuditResponse.setReceiverPhone(materialAuditSheet.getReceiverPhone());
                materialWorkerAuditResponse.setDeliveryCity(materialAuditSheet.getDeliveryCity());
                materialWorkerAuditResponse.setDeliveryCounty(materialAuditSheet.getDeliveryCounty());
                materialWorkerAuditResponse.setDeliveryStreet(materialAuditSheet.getDeliveryStreet());
                materialWorkerAuditResponse.setResidenceName(materialAuditSheet.getResidenceName());
                materialWorkerAuditResponse.setDetailedAddress(materialAuditSheet.getDetailedAddress());

                deliveryId = materialAuditSheet.getDeliveryId();
            }
        }
        if (identityType == 6 || identityType == 0) {
            List<CouponMaterialListResponse> listResponses = null;
            if (identityType == 0) {
                listResponses = materialListServiceImpl.findGuideMaterialListByUserIdAndCusIdAndIdentityType(userId, appIdentityType);
            } else {
                listResponses = materialListServiceImpl.findCustomerMaterialListByUserIdAndIdentityType(userId, appIdentityType);
            }
            if (null != listResponses && !listResponses.isEmpty()) {
                //集合对象中都是同一个顾客，所以取其中一个顾客Id
                AppCustomer appCustomer = customerService.findById(listResponses.get(0).getCusId());
                //设置返回信息
                materialCustomerCouponResponse.setMobile(appCustomer.getMobile());
                materialCustomerCouponResponse.setCustomer(appCustomer.getName());
            }
            //产品券不显示价格
//            if (null != listResponses && !listResponses.isEmpty()) {
//                for (CouponMaterialListResponse couponMaterialListResponse : listResponses) {
//                    couponMaterialListResponse.setRetailPrice(0.00);
//                }
//            }
            materialCustomerCouponResponse.setCouponsList(listResponses);
        }
//        List<MaterialListType> materialListTypes = new ArrayList<MaterialListType>();
//        materialListTypes.add(MaterialListType.PHOTO_ORDER);
//        List<PhotoOrderMaterialListResponse> photoOrderMaterialListResponses = this.materialListServiceImpl.findByUserIdAndIdentityTypeAndMaterialListType(userId, appIdentityType, materialListTypes);
//        List<MaterialPhotoOrderResponse> materialPhotoOrderResponses = new ArrayList<MaterialPhotoOrderResponse>();
//        if (null != photoOrderMaterialListResponses) {
//            for (int i = 0; i < photoOrderMaterialListResponses.size(); i++) {
//                PhotoOrderMaterialListResponse photoOrderMaterialListResponse = photoOrderMaterialListResponses.get(i);
//                if (null != photoOrderMaterialListResponse && null != photoOrderMaterialListResponse.getPhotoOrderNo()) {
//                    Boolean flag = true;
//                    for (int j = 0; j < materialPhotoOrderResponses.size(); j++) {
//                        if (photoOrderMaterialListResponse.getPhotoOrderNo().equals(materialPhotoOrderResponses.get(j).getPhotoOrderNo())) {
//                            materialPhotoOrderResponses.get(j).getGoodsList().add(photoOrderMaterialListResponse);
//                            flag = false;
//                        }
//                    }
//                    if (flag) {
//                        MaterialPhotoOrderResponse materialPhotoOrderResponse = new MaterialPhotoOrderResponse();
//                        materialPhotoOrderResponse.setPhotoOrderNo(photoOrderMaterialListResponse.getPhotoOrderNo());
//                        List<PhotoOrderMaterialListResponse> goodsList = new ArrayList<PhotoOrderMaterialListResponse>();
//                        goodsList.add(photoOrderMaterialListResponse);
//                        materialPhotoOrderResponse.setGoodsList(goodsList);
//                        materialPhotoOrderResponses.add(materialPhotoOrderResponse);
//                    }
//                }
//            }
//        }

//        returnMap.put("photoListRes",materialPhotoOrderResponses);
        returnMap.put("couponListRes", materialCustomerCouponResponse);
        returnMap.put("auditListRes", materialWorkerAuditResponse);
        returnMap.put("materialListRes", normalMaterialListRespons);
        returnMap.put("remark", remark);
        returnMap.put("deliveryId", deliveryId);
        resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, returnMap);
        logger.info("getMaterialList OUT,获取下料清单列表成功，出参 resultDTO:{}", resultDTO);
        return resultDTO;
    }

    /**
     * @param
     * @return
     * @throws
     * @title 再来一单加入下料清单
     * @descripe
     * @author GenerationRoad
     * @date 2017/11/10
     */
    @PostMapping(value = "/again/add", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> addAgainMaterialList(Long userId, Integer identityType, String orderNumber) {
        logger.info("addAgainMaterialList CALLED,再来一单加入下料清单，入参 userId:{} identityType:{} orderNumber:{}", userId, identityType, orderNumber);
        ResultDTO<Object> resultDTO;
        try {
            if (null == userId) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "userId不能为空！", null);
                logger.info("addAgainMaterialList OUT,再来一单加入下料清单失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == identityType) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空！",
                        null);
                logger.info("addAgainMaterialList OUT,再来一单加入下料清单失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == orderNumber) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "订单号不能为空！", null);
                return resultDTO;
            }
            List<MaterialListDO> materialListDOList = this.appOrderServiceImpl.getGoodsInfoByOrderNumber(orderNumber);
            if (null == materialListDOList || materialListDOList.size() == 0) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "无此订单信息！",
                        null);
                logger.info("addAgainMaterialList OUT,再来一单加入下料清单失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            List<MaterialListDO> materialListSave = new ArrayList<>();
            List<MaterialListDO> materialListUpdate = new ArrayList<>();
            for (MaterialListDO materialList : materialListDOList) {
                MaterialListDO materialListDO = materialListServiceImpl.findByUserIdAndIdentityTypeAndGoodsId(userId,
                        AppIdentityType.getAppIdentityTypeByValue(identityType), materialList.getGid());
                if (null == materialListDO) {
                    materialList.setUserId(userId);
                    materialList.setIdentityType(AppIdentityType.getAppIdentityTypeByValue(identityType));
                    materialList.setMaterialListType(MaterialListType.NORMAL);
                    if (null != materialList.getCoverImageUri()) {
                        String uri[] = materialList.getCoverImageUri().split(",");
                        materialList.setCoverImageUri(uri[0]);
                    }
                    materialListSave.add(materialList);
                } else {
                    materialListDO.setQty(materialListDO.getQty() + materialList.getQty());
                    materialListUpdate.add(materialListDO);
                }
            }
            commonService.saveAndUpdateMaterialList(materialListSave, materialListUpdate);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
            logger.info("addAgainMaterialList OUT,再来一单加入下料清单成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知异常,再来一单加入下料清单失败!", null);
            logger.warn("addAgainMaterialList EXCEPTION,再来一单加入下料清单失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }


    /**
     * 根据用户id、用户身份、商品ID获取
     * 该商品在用户下料清单中数量
     *
     * @param userId       用户ID
     * @param identityType 用户身份类型
     * @param goodsId      商品ID
     * @return 用户下料清单单品数量
     */
    @PostMapping(value = "/goods/qty", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> getMaterialListSingleGoodsQty(Long userId, Integer identityType, Long goodsId) {
        logger.info("getMaterialListSingleGoodsQty CALLED,获取下料清单单品数量，" +
                "入参 userId:{}, identityType:{},goodsId:{}", userId, identityType, goodsId);
        ResultDTO<Object> resultDTO;
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "userId不能为空！", null);
            logger.info("getMaterialListSingleGoodsQty OUT,获取下料清单单品数量失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空！", null);
            logger.info("getMaterialListSingleGoodsQty OUT,获取下料清单单品数量失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == goodsId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "商品id不能为空！", null);
            logger.info("getMaterialListSingleGoodsQty OUT,获取下料清单单品数量失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        GoodsIdQtyParam goodsIdQtyParam = new GoodsIdQtyParam();
        if (identityType == 2) {
            MaterialListDO materialListDO = materialListServiceImpl.findAuditListByUserIdAndIdentityTypeAndGoodsId(userId,
                    AppIdentityType.getAppIdentityTypeByValue(identityType), goodsId);
            if (materialListDO != null) {
                goodsIdQtyParam.setId(materialListDO.getId());
                goodsIdQtyParam.setQty(materialListDO.getQty());
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, goodsIdQtyParam);
                logger.info("getMaterialListSingleGoodsQty OUT,获取下料清单单品数量成功，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
        }
        goodsIdQtyParam = this.materialListServiceImpl.findGoodsQtyByUserIdAndIdentityTypeAndGoodsId(
                userId, AppIdentityType.getAppIdentityTypeByValue(identityType), goodsId);
        resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, goodsIdQtyParam);
        logger.info("getMaterialListSingleGoodsQty OUT,获取下料清单单品数量成功，出参 resultDTO:{}", resultDTO);
        return resultDTO;
    }

    /**
     * @param userId       用户id
     * @param identityType 用户身份
     * @param goodsList    商品列表
     * @return 更新状态
     */
    @PostMapping(value = "/edit/batch", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> updateOrSaveMaterialListByGoodsId(Long userId, Integer identityType, String goodsList) {
        logger.info("updateOrSaveMaterialListByGoodsId CALLED,更新下料清单单品数量，" +
                "入参 userId:{}, identityType:{},goodsList:{}", userId, identityType, goodsList);
        ResultDTO<Object> resultDTO;
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "userId不能为空！", null);
            logger.info("updateOrSaveMaterialListByGoodsId OUT,更新下料清单单品数量失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空！", null);
            logger.info("updateOrSaveMaterialListByGoodsId OUT,更新下料清单单品数量失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (!(null != goodsList && goodsList.length() > 0)) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "商品列表不能为空！", null);
            logger.info("updateOrSaveMaterialListByGoodsId OUT,更新下料清单单品数量失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }

        try {
            Map<Long, Integer> goodsMap = new HashMap<>();
            ObjectMapper objectMapper = new ObjectMapper();

            JavaType javaType1 = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, GoodsIdQtyParam.class);
            List<GoodsIdQtyParam> goodsInfoList = objectMapper.readValue(goodsList, javaType1);

            List<MaterialListDO> materialListSave = new ArrayList<>();
            List<MaterialListDO> materialListUpdate = new ArrayList<>();
            for (GoodsIdQtyParam param : goodsInfoList) {
                GoodsDO goodsDO = goodsService.findGoodsById(param.getId());
                if (null != goodsDO) {
                    MaterialListDO materialListDO = materialListServiceImpl.findByUserIdAndIdentityTypeAndGoodsId(userId,
                            AppIdentityType.getAppIdentityTypeByValue(identityType), param.getId());
                    if (null == materialListDO) {
                        MaterialListDO materialListDOTemp = transformRepeat(goodsDO);
                        materialListDOTemp.setUserId(userId);
                        materialListDOTemp.setIdentityType(AppIdentityType.getAppIdentityTypeByValue(identityType));
                        materialListDOTemp.setMaterialListType(MaterialListType.NORMAL);
                        materialListDOTemp.setQty(param.getQty());
                        materialListSave.add(materialListDOTemp);
                    } else {
                        materialListDO.setQty(param.getQty());
                        materialListUpdate.add(materialListDO);
                    }
                } else {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "id为" + param.getId() + "" +
                            "的商品不存在!", null);
                }
            }
            commonService.saveAndUpdateMaterialList(materialListSave, materialListUpdate);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
            logger.info("updateOrSaveMaterialListByGoodsId OUT,更新下料清单单品数量成功,出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (IOException e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知异常,更新下料清单单品数量失败!", null);
            logger.warn("updateOrSaveMaterialListByGoodsId EXCEPTION,更新下料清单单品数量成功，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }


    @PostMapping(value = "/goods/delete", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> deleteMaterialListGoodsById(Long userId, Integer identityType,
                                                         @RequestParam(value = "goodsIdArray", required = false) Long[] goodsIdArray) {
        logger.info("deleteMaterialListGoodsById CALLED,删除下料清单商品，入参 userId:{} identityType:{} " +
                "goodsIdArray:{}", userId, identityType, goodsIdArray);

        ResultDTO<Object> resultDTO;
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "userId不能为空！", null);
            logger.info("deleteMaterialListGoodsById OUT,删除下料清单商品失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户身份类型不能为空！", null);
            logger.info("deleteMaterialListGoodsById OUT,删除下料清单商品失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == goodsIdArray) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "商品ID不能为空！", null);
            logger.info("deleteMaterialListGoodsById OUT,删除下料清单商品失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        List<Long> goodsIdList = Arrays.asList(goodsIdArray);
        this.materialListServiceImpl.deleteMaterialListByUserIdAndIdentityTypeAndGoodsId(userId,
                AppIdentityType.getAppIdentityTypeByValue(identityType), goodsIdList);
        resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
        logger.info("deleteMaterialListGoodsById OUT,删除下料清单商品成功，出参 resultDTO:{}", resultDTO);
        return resultDTO;
    }

    /**
     * @param
     * @return
     * @throws
     * @title 快捷下单加入下料清单
     * @descripe
     * @author GenerationRoad
     * @date 2017/11/21
     */
    @PostMapping(value = "/quickOrder/add", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> addQuickOrderMaterialList(Long userId, Integer identityType, String params) {
        logger.info("addQuickOrderMaterialList CALLED,快捷下单加入下料清单，入参 userId:{} identityType:{} params:{}", userId, identityType, params);
        ResultDTO<Object> resultDTO;
        try {
            if (null == userId) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "userId不能为空！", null);
                logger.info("addQuickOrderMaterialList OUT,快捷下单加入下料清单失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == identityType) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空！",
                        null);
                logger.info("addQuickOrderMaterialList OUT,快捷下单加入下料清单失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == params) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "商品和数量信息不能为空！", null);
                logger.info("addQuickOrderMaterialList OUT,快捷下单加入下料清单失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            Map<String, Integer> goodsMap = new HashMap();
            String[] param = params.split(",");
            for (String s : param) {
                String goodsParam[] = s.split("-");
                goodsMap.put(goodsParam[0], Integer.parseInt(goodsParam[1]));
            }
            List<MaterialListDO> materialListSave = new ArrayList<>();
            List<MaterialListDO> materialListUpdate = new ArrayList<>();
            String msg = "";
            for (Map.Entry<String, Integer> entry : goodsMap.entrySet()) {
                GoodsDO goodsDO = this.quickOrderRelationServiceImpl.findByNumber(userId, AppIdentityType.getAppIdentityTypeByValue(identityType), entry.getKey());
                if (null != goodsDO) {
                    MaterialListDO materialListDO = materialListServiceImpl.findByUserIdAndIdentityTypeAndGoodsId(userId,
                            AppIdentityType.getAppIdentityTypeByValue(identityType), goodsDO.getGid());
                    if (null == materialListDO) {
                        MaterialListDO materialListDOTemp = transformRepeat(goodsDO);
                        materialListDOTemp.setUserId(userId);
                        materialListDOTemp.setIdentityType(AppIdentityType.getAppIdentityTypeByValue(identityType));
                        materialListDOTemp.setQty(entry.getValue());
                        materialListDOTemp.setMaterialListType(MaterialListType.NORMAL);
                        materialListSave.add(materialListDOTemp);
                    } else {
                        materialListDO.setQty(materialListDO.getQty() + entry.getValue());
                        materialListUpdate.add(materialListDO);
                    }
                } else {
                    QuickOrderRelationDO quickOrderRelationDO = this.quickOrderRelationServiceImpl.findQuickOrderRelationDOByNumber(entry.getKey());
                    if (null != msg && !"".equals(msg)) {
                        msg += "、";
                    }
                    if (null != quickOrderRelationDO && null != quickOrderRelationDO.getGoodsName()) {
                        msg += "‘";
                        msg += null == quickOrderRelationDO.getGoodsName() ? "" : quickOrderRelationDO.getGoodsName();
                        msg += "’";
                    }

                }
            }
            commonService.saveAndUpdateMaterialList(materialListSave, materialListUpdate);
            if (null == msg || "".equals(msg)) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
            } else {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "商品" + msg + "已下架!", null);
            }
            logger.info("addQuickOrderMaterialList OUT,快捷下单加入下料清单成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知异常,快捷下单加入下料清单失败!", null);
            logger.warn("addQuickOrderMaterialList EXCEPTION,快捷下单加入下料清单失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    /**
     * @title   购买产品卷下料清单列表
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2018/6/20
     */
    @PostMapping(value = "/buyCoupon/list", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> getProductCouponMaterialList(Long userId, Integer identityType) throws UnsupportedEncodingException {
        logger.info("getProductCouponMaterialList CALLED,购买产品卷下料清单列表，入参 userId:{} identityType:{}", userId, identityType);
        ResultDTO<Object> resultDTO;
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "userId不能为空！", null);
            logger.info("getProductCouponMaterialList OUT,购买产品卷下料清单列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型错误！", null);
            logger.info("getProductCouponMaterialList OUT,购买产品卷下料清单列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        //判断创单人身份是否合法
        if (!(identityType == 0 || identityType == 6)) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "亲爱的用户，您暂未开通此功能！", "");
            logger.warn("getProductCouponMaterialList OUT,购买产品卷下料清单列表失败,出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        //所有用户返回自己的商品
        List<NormalMaterialListResponse> normalMaterialListRespons = this.materialListServiceImpl.findBuyCouponGoodsByUserIdAndIdentityType(userId, identityType);
        Map<String, Object> returnMap = new HashMap<>(4);
        returnMap.put("materialListRes", normalMaterialListRespons);
        resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, returnMap);
        logger.info("getProductCouponMaterialList OUT,购买产品卷下料清单列表成功，出参 resultDTO:{}", resultDTO);
        return resultDTO;
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
}

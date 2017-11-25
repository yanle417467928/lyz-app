package cn.com.leyizhuang.app.web.controller.materialList;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.core.constant.MaterialListType;
import cn.com.leyizhuang.app.foundation.pojo.MaterialListDO;
import cn.com.leyizhuang.app.foundation.pojo.goods.GoodsDO;
import cn.com.leyizhuang.app.foundation.pojo.order.MaterialAuditSheet;
import cn.com.leyizhuang.app.foundation.pojo.request.GoodsIdQtyParam;
import cn.com.leyizhuang.app.foundation.pojo.response.MaterialAuditGoPayResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.materialList.CouponMaterialListResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.materialList.NormalMaterialListResponse;
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

    @Resource
    private MaterialAuditGoodsInfoService materialAuditGoodsInfoService;

    @Autowired
    private QuickOrderRelationService quickOrderRelationServiceImpl;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private CommonService commonService;

    @Autowired
    private AppOrderService AppOrderServiceImpl;

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
            Map<Long, Integer> goodsMap = new HashMap();
            String[] param = params.split(",");
            for (String s : param) {
                String goodsParam[] = s.split("-");
                goodsMap.put(Long.parseLong(goodsParam[0]), Integer.parseInt(goodsParam[1]));
            }
            List<MaterialListDO> materialListSave = new ArrayList<>();
            List<MaterialListDO> materialListUpdate = new ArrayList<>();
            for (Map.Entry<Long, Integer> entry : goodsMap.entrySet()) {
                GoodsDO goodsDO = goodsService.findGoodsById(entry.getKey());
                if (null != goodsDO) {
                    MaterialListDO materialListDO = materialListServiceImpl.findByUserIdAndIdentityTypeAndGoodsId(userId,
                            AppIdentityType.getAppIdentityTypeByValue(identityType), entry.getKey());
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
            if (null == qty) {
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
    public ResultDTO<Object> getMaterialList(Long userId,Long cusId, Integer identityType) {
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
        //创建返回对象
        MaterialAuditGoPayResponse materialAuditGoPayResponse = new MaterialAuditGoPayResponse();
        List<CouponMaterialListResponse> listResponses = null;
        Map<String, Object> returnMap = new HashMap<>(3);
        AppIdentityType appIdentityType = AppIdentityType.getAppIdentityTypeByValue(identityType);

        if (identityType == 2) {
            //查询的是所有的商品下料清单（这个集合对象中的料单号和类型是一样的）
            List<NormalMaterialListResponse> materialListDOS = materialListServiceImpl.findMaterialListByUserIdAndTypeAndAuditIsNotNull(userId, appIdentityType);

            if (materialListDOS != null) {
                //只需得到一个料单对象中料单编号
                MaterialListDO materialListDO = materialListServiceImpl.findByUserIdAndIdentityTypeAndGoodsId(userId,
                        appIdentityType, materialListDOS.get(0).getGoodsId());
                String auditNo = materialListDO.getAuditNo();
                //可以得到一个审核料单
                MaterialAuditSheet materialAuditSheet = materialAuditSheetService.queryByAuditNo(auditNo);

                //返回工人料单商品
                materialAuditGoPayResponse.setGoodsList(materialListDOS);
                materialAuditGoPayResponse.setAuditNo(auditNo);
                materialAuditGoPayResponse.setWorker(materialAuditSheet.getEmployeeName());

            }
        }
        if (identityType == 6 || identityType == 0) {
            if (identityType == 0){
                if (null == cusId){
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "代下单顾客Id不能为空！", null);
                    logger.info("getMaterialList OUT,获取下料清单列表失败，出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }
                listResponses = materialListServiceImpl.findGuideMaterialListByUserIdAndCusIdAndIdentityType(userId,cusId,appIdentityType);
            }else {
                listResponses = materialListServiceImpl.findCoutomerMaterialListByUserIdAndIdentityType(userId, appIdentityType);
            }
            if (listResponses != null) {
                for (CouponMaterialListResponse couponMaterialListResponse : listResponses) {
                    couponMaterialListResponse.setRetailPrice(0.00);
                }
            }
        }
        returnMap.put("couponListRes",listResponses);
        returnMap.put("auditListRes", materialAuditGoPayResponse);
        returnMap.put("materialListRes", normalMaterialListRespons);
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
            Map<Long, Integer> goodsMap = new HashMap();
            List<MaterialListDO> materialListDOList = this.AppOrderServiceImpl.getGoodsInfoByOrderNumber(orderNumber);
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

        Map<Long, Integer> goodsQty = this.materialListServiceImpl.findGoodsQtyByUserIdAndIdentityTypeAndGoodsId(
                userId, AppIdentityType.getAppIdentityTypeByValue(identityType), goodsId);
        resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, goodsQty);
        logger.info("getUsableProductCoupon OUT,获取下料清单列表成功，出参 resultDTO:{}", resultDTO);
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
            for (Map.Entry<String, Integer> entry : goodsMap.entrySet()) {
                GoodsDO goodsDO = this.quickOrderRelationServiceImpl.findByNumber(entry.getKey());
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
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "id为" + entry.getKey() + "" +
                            "的商品不存在!", null);
                }
            }
            commonService.saveAndUpdateMaterialList(materialListSave, materialListUpdate);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
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

    private MaterialListDO transformRepeat(GoodsDO goodsDO){
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

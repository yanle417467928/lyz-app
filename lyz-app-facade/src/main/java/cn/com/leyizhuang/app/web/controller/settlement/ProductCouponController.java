package cn.com.leyizhuang.app.web.controller.settlement;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.core.constant.MaterialListType;
import cn.com.leyizhuang.app.foundation.pojo.MaterialListDO;
import cn.com.leyizhuang.app.foundation.pojo.goods.GoodsDO;
import cn.com.leyizhuang.app.foundation.pojo.request.ProductCouponRequest;
import cn.com.leyizhuang.app.foundation.pojo.response.OrderUsableProductCouponResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.materialList.UsedMoreProductCoupon;
import cn.com.leyizhuang.app.foundation.service.CommonService;
import cn.com.leyizhuang.app.foundation.service.GoodsService;
import cn.com.leyizhuang.app.foundation.service.MaterialListService;
import cn.com.leyizhuang.app.foundation.service.ProductCouponService;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/10/19
 */
@RestController
@RequestMapping("/app/productCoupon")
public class ProductCouponController {
    private static final Logger logger = LoggerFactory.getLogger(ProductCouponController.class);

    @Autowired
    private ProductCouponService productCouponServiceImpl;

    @Resource
    private GoodsService goodsService;

    @Autowired
    private MaterialListService materialListServiceImpl;

    @Autowired
    private CommonService commonService;

    /**
     * @param
     * @return
     * @throws
     * @title 获取订单可用产品券列表
     * @descripe
     * @author GenerationRoad
     * @date 2017/10/19
     */
    @PostMapping(value = "/get/usable/product/coupon", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> getUsableProductCoupon(Long userId, Integer identityType, String goodsParam, String giftParam) {
        logger.info("getUsableProductCoupon CALLED,获取订单可用产品券列表，入参 userId:{} identityType:{} goodsParam:{} giftParam:{}", userId, identityType, goodsParam, giftParam);
        ResultDTO<Object> resultDTO;
        try {
            if (null == userId) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "userId不能为空！", null);
                logger.info("getUsableProductCoupon OUT,获取订单可用产品券列表失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == goodsParam) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "商品和数量信息不能为空！", null);
                logger.info("getUsableProductCoupon OUT,获取订单可用产品券列表失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            List<Long> goodsIds = new ArrayList<Long>();
            String[] param = goodsParam.split(",");
            for (int i = 0; i < param.length; i++) {
                String[] goodsIdParam = param[i].split("-");
                if (goodsIdParam.length != 2) {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "商品和数量信息不能为空！", null);
                    logger.info("getUsableProductCoupon OUT,获取订单可用产品券列表失败，出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }
                goodsIds.add(Long.parseLong(goodsIdParam[0]));
            }
            List<OrderUsableProductCouponResponse> productCouponResponseList = this.productCouponServiceImpl.findProductCouponByCustomerIdAndGoodsId(userId, goodsIds);

            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, productCouponResponseList);
            logger.info("getUsableProductCoupon OUT,获取订单可用产品券列表成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知异常,获取订单可用产品券列表失败!", null);
            logger.warn("getUsableProductCoupon EXCEPTION,获取订单可用产品券列表失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    /**
     * 顾客产品券转入下料清单
     *
     * @param productCouponRequest 请求参数
     * @return 是否加入成功
     */
    @PostMapping(value = "/transform/materialList", produces = "application/json;charset=UTF-8")
    public ResultDTO productCouponTransformMaterialList(@RequestBody ProductCouponRequest productCouponRequest) {

        logger.info("productCouponTransformMaterialList CALLED,顾客点击使用产品券通过加入下料清单，入参 productCouponRequest:{}", productCouponRequest);

        ResultDTO<Object> resultDTO;
        if (null == productCouponRequest.getUserId()) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空", null);
            logger.info("productCouponTransformMaterialList OUT,顾客点击使用产品券通过加入下料清单失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == productCouponRequest.getIdentityType()) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空", null);
            logger.info("productCouponTransformMaterialList OUT,顾客点击使用产品券通过加入下料清单失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == productCouponRequest.getProductCouponList() || productCouponRequest.getProductCouponList().isEmpty()) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "找不到产品券信息", null);
            logger.info("productCouponTransformMaterialList OUT,顾客点击使用产品券通过加入下料清单失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }

        Long userId = productCouponRequest.getUserId();
        Integer identityType = productCouponRequest.getIdentityType();
        List<UsedMoreProductCoupon> requestList = productCouponRequest.getProductCouponList();

        Long cusId = 0L;
        try {
            List<MaterialListDO> materialListSave = new ArrayList<>();
            List<MaterialListDO> materialListUpdate = new ArrayList<>();
            //如果是代下单顾客Id不可为空
            if (identityType == 6 || identityType == 0) {
                if (identityType == 0) {
                    if (null == productCouponRequest.getCusId()) {
                        resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "代下单顾客Id不能为空！", null);
                        logger.info("productCouponTransformMaterialList OUT,顾客点击使用产品券通过加入下料清单失败，出参 resultDTO:{}", resultDTO);
                        return resultDTO;
                    }
                    cusId = productCouponRequest.getCusId();
                    //导购下料清单中不能有两种顾客的产品券
                    Boolean isOther = materialListServiceImpl.existOtherMaterialCouponByUserIdAndIdentityType(userId, cusId, identityType);
                    if (isOther) {
                        resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "下料清单中已存在其他顾客的产品券", null);
                        logger.info("productCouponTransformMaterialList OUT,顾客点击使用产品券通过加入下料清单失败，出参 resultDTO:{}", resultDTO);
                        return resultDTO;
                    }
                }
                //从页面传过来的数组中有券ID 和数量查询出商品ID和数量装入Map
                for (UsedMoreProductCoupon goodsIdQtyParam : requestList) {

                    Long goodsId = goodsIdQtyParam.getGid();
                    int qty = goodsIdQtyParam.getQty();
                    int totalQty = goodsIdQtyParam.getTotalQty();
                    GoodsDO goodsDO = goodsService.findGoodsById(goodsId);
                    if (null != goodsDO) {
                        //查询下料清单中是否有重复产品券
                        MaterialListDO materialListDO = null;
                        if (identityType == 6) {
                            materialListDO = materialListServiceImpl.findCouponTransformByUserIdAndIdentityTypeAndGoodsId(userId,
                                    AppIdentityType.getAppIdentityTypeByValue(identityType), goodsId);
                        } else {
                            materialListDO = materialListServiceImpl.findCouponTransformByUserIdAndCusIdAndIdentityTypeAndGoodsId(userId,
                                    cusId, AppIdentityType.getAppIdentityTypeByValue(identityType), goodsId);
                        }
                        //没有就新增
                        if (null == materialListDO) {
                            MaterialListDO materialListDOTemp = new MaterialListDO();
                            materialListDOTemp.setUserId(userId);
                            materialListDOTemp.setIdentityType(AppIdentityType.getAppIdentityTypeByValue(identityType));
                            materialListDOTemp.setGid(goodsDO.getGid());
                            materialListDOTemp.setSku(goodsDO.getSku());
                            materialListDOTemp.setQty(goodsIdQtyParam.getQty());
                            materialListDOTemp.setSkuName(goodsDO.getSkuName());
                            materialListDOTemp.setGoodsSpecification(goodsDO.getGoodsSpecification());
                            materialListDOTemp.setGoodsUnit(goodsDO.getGoodsUnit());
                            materialListDOTemp.setMaterialListType(MaterialListType.COUPON_TRANSFORM);
                            if (null != goodsDO.getCoverImageUri()) {
                                String uri[] = goodsDO.getCoverImageUri().split(",");
                                materialListDOTemp.setCoverImageUri(uri[0]);
                            }
                            if (identityType == 0) {
                                materialListDOTemp.setCusId(cusId);
                            }
                            materialListSave.add(materialListDOTemp);
                        } else {
                            //先判断顾客使用产品券有没有超过剩余的总量：传入使用的数量 + 数据库清单中同商品Id的数量 < 剩余总量
                            if ((materialListDO.getQty() + qty) > totalQty) {
                                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "使用产品券超过剩余数量!", null);
                                logger.info("productCouponTransformMaterialList OUT,顾客点击使用产品券通过加入下料清单失败，出参 resultDTO:{}", resultDTO);
                                return resultDTO;
                            }
                            //如果没超过剩余数量，就继续加
                            materialListDO.setQty(materialListDO.getQty() + qty);
                            materialListUpdate.add(materialListDO);
                        }
                    } else {
                        resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "id为" + goodsId + "" +
                                "的产品券对应商品不存在!", null);
                        logger.info("productCouponTransformMaterialList OUT,顾客点击使用产品券通过加入下料清单失败，出参 resultDTO:{}", resultDTO);
                        return resultDTO;
                    }
                }
            }
            commonService.saveAndUpdateMaterialList(materialListSave, materialListUpdate);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
            logger.info("productCouponTransformMaterialList OUT,顾客点击使用产品券通过加入下料清单成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，顾客点击使用产品券通过加入下料清单失败", null);
            logger.warn("productCouponTransformMaterialList EXCEPTION,顾客点击使用产品券通过加入下料清单失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }
}

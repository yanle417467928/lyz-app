package cn.com.leyizhuang.app.web.controller.settlement;

import cn.com.leyizhuang.app.core.constant.AppGoodsLineType;
import cn.com.leyizhuang.app.foundation.pojo.GoodsPrice;
import cn.com.leyizhuang.app.foundation.pojo.request.GoodsIdQtyParam;
import cn.com.leyizhuang.app.foundation.pojo.request.OrderGoodsSimpleRequest;
import cn.com.leyizhuang.app.foundation.pojo.request.settlement.PromotionSimpleInfo;
import cn.com.leyizhuang.app.foundation.pojo.response.OrderGoodsSimpleResponse;
import cn.com.leyizhuang.app.foundation.pojo.user.AppEmployee;
import cn.com.leyizhuang.app.foundation.service.AppEmployeeService;
import cn.com.leyizhuang.app.foundation.service.AppOrderService;
import cn.com.leyizhuang.app.foundation.service.GoodsService;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import cn.com.leyizhuang.common.util.CountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @author GenerationRoad
 * @date 2017/10/19
 */
@RestController
@RequestMapping("/app/goodsMoney")
public class GetGoodsMoneyController {
    private static final Logger logger = LoggerFactory.getLogger(GetGoodsMoneyController.class);

    @Autowired
    private GoodsService goodsServiceImpl;

    @Autowired
    private AppOrderService appOrderService;

    @Autowired
    private AppEmployeeService appEmployeeService;

    @PostMapping(value = "/get", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> getGoodsMoney(Long userId, Integer identityType, String params) {
        logger.info("getGoodsMoney CALLED,获取付款方式，入参 userId:{} identityType:{} params:{}", userId, identityType, params);
        ResultDTO<Object> resultDTO;
        try {
            if (null == userId) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "userId不能为空！", null);
                logger.info("getGoodsMoney OUT,获取付款方式失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == identityType || 6 != identityType) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型错误！", null);
                logger.info("getGoodsMoney OUT,获取付款方式失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == params) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "商品和数量信息不能为空！", null);
                logger.info("getGoodsMoney OUT,获取付款方式失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            List<Long> goodsIds = new ArrayList<Long>();
            Map<Long, Integer> map = new HashMap<>();
            String[] param = params.split(",");
            for (int i = 0; i < param.length; i++) {
                String[] goodsIdParam = param[i].split("-");
                if (goodsIdParam.length != 2) {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "商品和数量信息不能为空！", null);
                    logger.info("getGoodsMoney OUT,获取订单可用产品券列表失败，出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }
                goodsIds.add(Long.parseLong(goodsIdParam[0]));
                map.put(Long.parseLong(goodsIdParam[0]), Integer.parseInt(goodsIdParam[1]));
            }
            Double totalPrice = 0.00;
            List<GoodsPrice> priceList = this.goodsServiceImpl.getGoodsPriceByCustomerAndGoodsId(userId, goodsIds);
            for (int i = 0; i < priceList.size(); i++) {
                GoodsPrice goodsPrice = priceList.get(i);
                Set<Long> key = map.keySet();
                for (Long goodId : key) {
                    if (goodId.equals(goodsPrice.getGid())) {
                        totalPrice = CountUtil.add(totalPrice, CountUtil.mul(goodsPrice.getRetailPrice(), map.get(goodId)));
                    }
                }
            }

            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, totalPrice);
            logger.info("getGoodsMoney OUT,获取付款方式成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知异常,获取付款方式失败!", null);
            logger.warn("getGoodsMoney EXCEPTION,获取付款方式失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    /**
     * 确认商品计算工人订单总金额
     *
     * @param
     * @return
     * @author Jerry
     */
    @PostMapping(value = "/worker", produces = "application/json;charset=UTF-8")
    public ResultDTO getGoodsMoneyOfWorker(@RequestBody OrderGoodsSimpleRequest goodsSimpleRequest) {

        logger.info("getGoodsMoneyOfWorker CALLED,确认商品计算工人订单总金额，入参 goodsSimpleRequest:{}", goodsSimpleRequest);

        ResultDTO resultDTO;
        if (null == goodsSimpleRequest || goodsSimpleRequest.getGoodsList().isEmpty()) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "找不到对象！", null);
            logger.info("getGoodsMoneyOfWorker OUT,确认商品计算工人订单总金额失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == goodsSimpleRequest.getUserId()) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空", null);
            logger.info("getGoodsMoneyOfWorker OUT,确认商品计算工人订单总金额失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == goodsSimpleRequest.getIdentityType()) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户身份不能为空", null);
            logger.info("getGoodsMoneyOfWorker OUT,确认商品计算工人订单总金额失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        Long userId = goodsSimpleRequest.getUserId();
        Integer identityType = goodsSimpleRequest.getIdentityType();
        List<GoodsIdQtyParam> goodsList = goodsSimpleRequest.getGoodsList();
        List<PromotionSimpleInfo> promotionSimpleInfos = goodsSimpleRequest.getGiftList();
        if (identityType != 3) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户身份类型错误", null);
            logger.info("getGoodsMoneyOfWorker OUT,确认商品计算工人订单总金额失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            int goodsQty = 0;
            int giftQty = 0;
            Double totalPrice = 0.00;
            List<Long> goodsIds = new ArrayList<Long>();
            List<Long> giftIds = new ArrayList<Long>();
            Map<String, Object> goodsSettlement = new HashMap<>();
            List<OrderGoodsSimpleResponse> goodsInfo;
            List<OrderGoodsSimpleResponse> giftInfo;
            List<GoodsIdQtyParam> giftsList = new ArrayList<>();
            AppEmployee employee = appEmployeeService.findById(userId);
            for (GoodsIdQtyParam aGoodsList : goodsList) {
                goodsIds.add(aGoodsList.getId());
                goodsQty = goodsQty + aGoodsList.getQty();
            }
            if (promotionSimpleInfos != null && !promotionSimpleInfos.isEmpty()) {
                for (PromotionSimpleInfo promotionSimpleInfo : promotionSimpleInfos) {
                    if (null != promotionSimpleInfo.getPresentInfo()) {
                        giftsList.addAll(promotionSimpleInfo.getPresentInfo());
                        for (GoodsIdQtyParam goodsIdQtyParam : promotionSimpleInfo.getPresentInfo()) {
                            giftIds.add(goodsIdQtyParam.getId());
                            giftQty = giftQty + goodsIdQtyParam.getQty();
                        }
                    }
                }
            }
            //获取商品信息
            goodsInfo = goodsServiceImpl.findGoodsListByEmployeeIdAndGoodsIdList(userId, goodsIds);
            //获取赠品信息
            giftInfo = goodsServiceImpl.findGoodsListByEmployeeIdAndGoodsIdList(userId, giftIds);

            //正品的数量这里需要判断是否和赠品有相同产品，然后算总数量检查库存
            Long msg = appOrderService.existOrderGoodsInventory(employee.getCityId(), goodsList, giftsList, null);
            if (msg != null) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "商品ID:" + msg + ";商品库存不足！", null);
                logger.info("getGoodsMoneyOfWorker OUT,确认商品计算工人订单总金额失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            //计算总金额
            List<GoodsPrice> priceList = this.goodsServiceImpl.getGoodsPriceByEmployeeAndGoodsId(userId, goodsIds);
            for (int i = 0; i < priceList.size(); i++) {
                GoodsPrice goodsPrice = priceList.get(i);
                for (int j = 0; j < goodsList.size(); j++) {
                    if (goodsPrice.getGid().equals(goodsList.get(j).getId())) {
                        totalPrice = CountUtil.add(totalPrice, CountUtil.mul(goodsPrice.getRetailPrice(), goodsList.get(j).getQty()));
                    }
                }
            }
            //加本品标识
            if (goodsInfo != null) {
                for (OrderGoodsSimpleResponse simpleResponse : goodsInfo) {
                    simpleResponse.setGoodsLineType(AppGoodsLineType.GOODS.getValue());
                }
            }
            //赠品的数量和标识
            if (giftInfo != null) {
                for (OrderGoodsSimpleResponse aGiftInfo : giftInfo) {
                    for (GoodsIdQtyParam goodsIdQtyParam : giftsList) {
                        if (aGiftInfo.getId().equals(goodsIdQtyParam.getId())) {
                            aGiftInfo.setGoodsQty(goodsIdQtyParam.getQty());
                            break;
                        }
                    }
                    aGiftInfo.setGoodsLineType(AppGoodsLineType.PRESENT.getValue());
                }
                //合并商品和赠品集合
                goodsInfo.addAll(giftInfo);

                //设置返回赠品数组，是否要包含促销信息
//                List<Map> promotionsGiftList = null;
//
//                for (PromotionSimpleInfo promotionSimpleInfo : promotionSimpleInfos) {
//                    Map tempMap = new HashMap();
//                    for (GoodsIdQtyParam goodsIdQtyParam : promotionSimpleInfo.getPresentInfo()) {
//                        List<OrderGoodsSimpleResponse> tempGoodsInfo = new ArrayList<>();
//                        for (OrderGoodsSimpleResponse simpleResponse : giftInfo) {
//                            if (goodsIdQtyParam.getId().equals(simpleResponse.getId())){
//                                simpleResponse.setGoodsLineType(AppGoodsLineType.PRESENT.getValue());
//                                tempGoodsInfo.add(simpleResponse);
//                            }
//                        }
//                    }
//                    tempMap.put("promotionId",promotionSimpleInfo.getPromotionId());
//                }
            }

            goodsSettlement.put("totalQty", goodsQty + giftQty);
            goodsSettlement.put("totalPrice", totalPrice);
            goodsSettlement.put("totalGoodsInfo", goodsInfo);
            goodsSettlement.put("promotionInfo", promotionSimpleInfos);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, goodsSettlement);
            logger.info("getGoodsMoney OUT,确认商品计算工人订单总金额成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知异常,确认商品计算工人订单总金额失败!", null);
            logger.warn("getGoodsMoneyOfWorker EXCEPTION,确认商品计算工人订单总金额失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }
}

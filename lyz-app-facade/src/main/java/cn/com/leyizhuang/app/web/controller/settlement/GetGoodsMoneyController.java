package cn.com.leyizhuang.app.web.controller.settlement;

import cn.com.leyizhuang.app.foundation.pojo.GoodsPrice;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderGoodsSimpleRequest;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderGoodsSimpleResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.UserGoodsResponse;
import cn.com.leyizhuang.app.foundation.service.GoodsService;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import cn.com.leyizhuang.common.util.CountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
                    if (goodId.equals(goodsPrice.getGid())){
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
     * @author Jerry
     * @param userId
     * @param identityType
     * @param goodsList
     * @return
     */
    @PostMapping(value = "/worker", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> getGoodsMoneyOfWorker(@RequestParam Long userId,@RequestParam Integer identityType,@RequestParam List<OrderGoodsSimpleRequest> goodsList) {

        logger.info("getGoodsMoneyOfWorker CALLED,确认商品计算工人订单总金额，入参 userId:{},identityType:{},goodsList:{}", userId,identityType,goodsList);
        ResultDTO resultDTO;
        if (goodsList.isEmpty()) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "找不到对象！", null);
            logger.info("getGoodsMoneyOfWorker OUT,确认商品计算工人订单总金额失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }

        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空", null);
            logger.info("getGoodsMoneyOfWorker OUT,确认商品计算工人订单总金额失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户身份不能为空", null);
            logger.info("getGoodsMoneyOfWorker OUT,确认商品计算工人订单总金额失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (identityType != 3){
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户身份类型错误", null);
            logger.info("getGoodsMoneyOfWorker OUT,确认商品计算工人订单总金额失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try{
            int totalQty = 0;
            Double totalPrice = 0.00;
            List<Long> goodsIds = new ArrayList<Long>();
            Map<String,Object> goodsSettlement = new HashMap<>();
            List<OrderGoodsSimpleResponse> goodsInfo;
            for (int i = 0; i <goodsList.size(); i++) {
                if (!goodsList.get(i).getIsGift()) {
                    goodsIds.add(goodsList.get(i).getId());
                }
                totalQty++;
            }
            goodsInfo = goodsServiceImpl.findGoodsListByEmployeeIdAndGoodsIdList(userId,goodsIds);
            for (int i = 0; i <goodsInfo.size() ; i++) {
                for (int j = 0; j < goodsList.size(); j++) {
                    if (goodsList.get(i).getId().equals(goodsInfo.get(i).getId())) {
                        if (goodsList.get(i).getIsGift()) {
                            goodsInfo.get(i).setIsGift(Boolean.TRUE);
                            goodsInfo.get(i).setGoodsQty(goodsInfo.get(i).getGoodsQty() - goodsList.get(i).getNum());
                        }
                        goodsInfo.get(i).setGoodsQty(goodsList.get(i).getNum());
                    }
                }
            }
            List<GoodsPrice> priceList = this.goodsServiceImpl.getGoodsPriceByEmployeeAndGoodsId(userId, goodsIds);
            for (int i = 0; i < priceList.size(); i++) {
                GoodsPrice goodsPrice = priceList.get(i);
                for (int j = 0; j <goodsList.size() ; j++) {
                    if (goodsPrice.getGid().equals(goodsList.get(j).getId())){
                        totalPrice = CountUtil.add(totalPrice, CountUtil.mul(goodsPrice.getRetailPrice(), goodsList.get(j).getNum()));
                    }
                }
            }
            goodsSettlement.put("totalQty",totalQty);
            goodsSettlement.put("totalPrice",totalPrice);
            goodsSettlement.put("totalGoodsInfo",goodsInfo);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, goodsSettlement);
            logger.info("getGoodsMoney OUT,确认商品计算工人订单总金额成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }catch (Exception e){
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知异常,确认商品计算工人订单总金额失败!", null);
            logger.warn("getGoodsMoneyOfWorker EXCEPTION,确认商品计算工人订单总金额失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }
}

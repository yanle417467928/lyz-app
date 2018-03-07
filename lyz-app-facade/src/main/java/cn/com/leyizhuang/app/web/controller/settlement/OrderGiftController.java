package cn.com.leyizhuang.app.web.controller.settlement;

import cn.com.leyizhuang.app.core.constant.AppGoodsLineType;
import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.foundation.pojo.goods.GoodsDO;
import cn.com.leyizhuang.app.foundation.pojo.request.GoodsIdQtyParam;
import cn.com.leyizhuang.app.foundation.pojo.request.settlement.GoodsSimpleInfo;
import cn.com.leyizhuang.app.foundation.pojo.response.GiftListResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.GiftListResponseGoods;
import cn.com.leyizhuang.app.foundation.pojo.response.OrderGoodsSimpleResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.PromotionsGiftListResponse;
import cn.com.leyizhuang.app.foundation.pojo.user.AppCustomer;
import cn.com.leyizhuang.app.foundation.pojo.user.AppEmployee;
import cn.com.leyizhuang.app.foundation.service.*;
import cn.com.leyizhuang.app.web.controller.customer.CustomerController;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 计算订单赠品
 *
 * @author Richard
 * Created on 2017-11-03 11:11
 **/
@RestController
@RequestMapping(value = "/app/gift")
public class OrderGiftController {

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);
    @Autowired
    private GoodsPriceService goodsPriceService;

    @Resource
    private AppCustomerService appCustomerService;

    @Resource
    private AppEmployeeService appEmployeeService;

    @Autowired
    private AppActService actService;

    @Resource
    private GoodsService goodsService;

    @Resource
    private AppOrderService appOrderService;

    @PostMapping(value = "/list", produces = "application/json;charset=UTF-8")
    public ResultDTO<GiftListResponse> materialListStepToGiftList(Long userId, Integer identityType, String goodsArray) {

        logger.info("materialListStepToGiftList CALLED,下料清单跳转赠品列表,入参userId {} identityType{} goodsArray:{}",userId,identityType, goodsArray);

        ResultDTO<GiftListResponse> resultDTO;
        //获取商品相关信息（id，数量，是否赠品）
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JavaType javaType1 = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, GoodsSimpleInfo.class);
            List<GoodsSimpleInfo> goodsList = objectMapper.readValue(goodsArray, javaType1);
            //创建计算库存参数
            Long cityId = 0L;
            List<GoodsIdQtyParam> giftsList = new ArrayList<>();
            List<GoodsIdQtyParam> couponList = new ArrayList<>();
            List<GoodsIdQtyParam> goodsItyList = new ArrayList<>();

            // 普通商品ids
            List<Long> goodsIdList = new ArrayList<>();
            // 券商品ids
            List<Long> coupunGoodsIdList = new ArrayList<>();
            for (GoodsSimpleInfo goodsSimpleInfo : goodsList) {
                if (AppGoodsLineType.PRODUCT_COUPON.getValue().equalsIgnoreCase(goodsSimpleInfo.getGoodsLineType())) {
                    coupunGoodsIdList.add(goodsSimpleInfo.getId());
                    //取产品券数量后面计算库存
                    GoodsIdQtyParam param = new GoodsIdQtyParam(goodsSimpleInfo.getId(), goodsSimpleInfo.getQty());
                    couponList.add(param);
                }else{
                    goodsIdList.add(goodsSimpleInfo.getId());
                    //取本品数量后面计算库存
                    GoodsIdQtyParam param = new GoodsIdQtyParam(goodsSimpleInfo.getId(), goodsSimpleInfo.getQty());
                    goodsItyList.add(param);
                }
            }

            /******计算促销******/
            List<OrderGoodsSimpleResponse> goodsInfo = new ArrayList<>();
            List<PromotionsGiftListResponse> promotionsGiftList = new ArrayList<>();
            if(identityType == 6){
                //取城市ID
                AppCustomer customer = appCustomerService.findById(userId);
                cityId = customer.getCityId();
                //获取商品信息
                goodsInfo = goodsService.findGoodsListByCustomerIdAndGoodsIdList(userId, goodsIdList);
            }else if(identityType == 0){
                //取城市ID
                AppEmployee employee = appEmployeeService.findById(userId);
                cityId = employee.getCityId();
                goodsInfo = goodsService.findGoodsListByEmployeeIdAndGoodsIdList(userId, goodsIdList);
            }else if(identityType == 2){
                //取城市ID
                AppEmployee employee = appEmployeeService.findById(userId);
                cityId = employee.getCityId();
            }

            if (goodsInfo != null && goodsInfo.size() > 0){
                //为商品设置数量
                Iterator<OrderGoodsSimpleResponse> orderGoodsSimpleResponseiterator = goodsInfo.iterator();
                while (orderGoodsSimpleResponseiterator.hasNext()) {
                    OrderGoodsSimpleResponse goods = orderGoodsSimpleResponseiterator.next();
                    for (GoodsSimpleInfo info : goodsList) {
                        if (info.getId().equals(goods.getId())) {
                            goods.setGoodsQty(info.getQty());
                            break;
                        }
                    }
                }
                promotionsGiftList = actService.countGift(userId,AppIdentityType.getAppIdentityTypeByValue(identityType),goodsInfo);
            }

            /******计算促销******/

            List<GiftListResponseGoods> responseGoodsList = new ArrayList<>();
            responseGoodsList = goodsPriceService.findGoodsPriceListByGoodsIdsAndUserIdAndIdentityType(
                    goodsIdList, userId, AppIdentityType.getAppIdentityTypeByValue(identityType));
            if (responseGoodsList != null && responseGoodsList.size() > 0){
                //为返商品集合设置数量和赠品属性
                Iterator<GiftListResponseGoods> iterator = responseGoodsList.iterator();
                while (iterator.hasNext()) {
                    GiftListResponseGoods goods = iterator.next();
                    for (GoodsSimpleInfo info : goodsList) {
                        if (info.getId().equals(goods.getGoodsId())) {
                            goods.setQty(info.getQty());
                            goods.setIsGift(false);
                            break;
                        }
                    }
                }
            }


            // 券商品集合
            List<GiftListResponseGoods> responseCouponGoodsList = new ArrayList<>();
            responseCouponGoodsList = goodsPriceService.findGoodsPriceListByGoodsIdsAndUserIdAndIdentityType(
                    coupunGoodsIdList, userId, AppIdentityType.getAppIdentityTypeByValue(identityType));

            if(responseCouponGoodsList != null && responseCouponGoodsList.size() > 0){
                //为返商品集合设置数量和赠品属性
                Iterator<GiftListResponseGoods> iterator2 = responseCouponGoodsList.iterator();
                while (iterator2.hasNext()) {
                    GiftListResponseGoods goods = iterator2.next();
                    for (GoodsSimpleInfo info : goodsList) {
                        if (info.getId().equals(goods.getGoodsId())) {
                            goods.setQty(info.getQty());
                            goods.setIsGift(false);
                            break;
                        }
                    }
                }
            }
            // 取赠品数量后面计算库存
            promotionsGiftList.forEach(promotionsGiftListResponse -> giftsList.addAll(promotionsGiftListForEach(promotionsGiftListResponse)));
            //判断库存的特殊处理
            Long gid = appOrderService.existOrderGoodsInventory(cityId, goodsItyList, giftsList, couponList);
            if (gid != null) {
                GoodsDO goodsDO = goodsService.queryById(gid);
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "商品:" + goodsDO.getSkuName() + "对不起,商品库存不足！", null);
                logger.info("materialListStepToGiftList OUT,下料清单跳转赠品列表失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }

            GiftListResponse giftListResponse = new GiftListResponse();
            giftListResponse.setGoodsList(responseGoodsList);
            giftListResponse.setCouponGoodsList(responseCouponGoodsList);
            giftListResponse.setPromotionsGiftList(promotionsGiftList);

            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, giftListResponse);
            logger.info("materialListStepToGiftList OUT,下料清单跳转赠品列表成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知异常,下料清单跳转赠品列表失败!", null);
            logger.warn("materialListStepToGiftList EXCEPTION,下料清单跳转赠品列表失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    private List<GoodsIdQtyParam> promotionsGiftListForEach(PromotionsGiftListResponse response) {

        List<GoodsIdQtyParam> idQtyParamList = new ArrayList<>();
        if (null != response.getGiftList() && response.getGiftList().size() > 0) {
            List<GiftListResponseGoods> giftList = response.getGiftList();
            for (GiftListResponseGoods giftListResponseGoods : giftList) {
                GoodsIdQtyParam param = new GoodsIdQtyParam(giftListResponseGoods.getGoodsId(), giftListResponseGoods.getQty());
                idQtyParamList.add(param);
            }
        }
        return idQtyParamList;
    }
}

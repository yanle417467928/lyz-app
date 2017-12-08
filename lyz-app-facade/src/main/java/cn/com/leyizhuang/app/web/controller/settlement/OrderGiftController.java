package cn.com.leyizhuang.app.web.controller.settlement;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.foundation.pojo.request.settlement.GoodsSimpleInfo;
import cn.com.leyizhuang.app.foundation.pojo.response.GiftListResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.GiftListResponseGoods;
import cn.com.leyizhuang.app.foundation.pojo.response.OrderGoodsSimpleResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.PromotionsGiftListResponse;
import cn.com.leyizhuang.app.foundation.pojo.user.AppCustomer;
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

    @PostMapping(value = "/list", produces = "application/json;charset=UTF-8")
    public ResultDTO<GiftListResponse> materialListStepToGiftList(Long userId, Integer identityType, String goodsArray) {

        logger.info("materialListStepToGiftList CALLED,下料清单跳转赠品列表,入参 goodsArray:{}", goodsArray);

        ResultDTO<GiftListResponse> resultDTO;
        //获取商品相关信息（id，数量，是否赠品）
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JavaType javaType1 = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, GoodsSimpleInfo.class);
            List<GoodsSimpleInfo> goodsList = objectMapper.readValue(goodsArray, javaType1);

            List<Long> goodsIdList = new ArrayList<>();
            for (GoodsSimpleInfo goodsSimpleInfo : goodsList) {
                goodsIdList.add(goodsSimpleInfo.getId());
            }

            /******计算促销******/
            List<OrderGoodsSimpleResponse> goodsInfo = null;
            List<PromotionsGiftListResponse> promotionsGiftList = new ArrayList<>();
            if(identityType == 6){
                //获取商品信息
                goodsInfo = goodsService.findGoodsListByCustomerIdAndGoodsIdList(userId, goodsIdList);
            }else if(identityType == 0){
                goodsInfo = goodsService.findGoodsListByEmployeeIdAndGoodsIdList(userId, goodsIdList);
            }

            //为返商品集合设置数量和赠品属性
            Iterator<OrderGoodsSimpleResponse> orderGoodsSimpleResponseiterator = goodsInfo.iterator();
            while (orderGoodsSimpleResponseiterator.hasNext()) {
                OrderGoodsSimpleResponse goods = orderGoodsSimpleResponseiterator.next();
                for (GoodsSimpleInfo info : goodsList) {
                    if (info.getId().equals(goods.getId())) {
                        goods.setGoodsQty(info.getNum());
                        break;
                    }
                }
            }
            promotionsGiftList = actService.countGift(userId,AppIdentityType.getAppIdentityTypeByValue(identityType),goodsInfo);
            /******计算促销******/

            List<GiftListResponseGoods> responseGoodsList = new ArrayList<>();
            GiftListResponseGoods gift = new GiftListResponseGoods();
            gift.setIsGift(true);
            gift.setQty(1);
            gift.setGoodsId(32L);
            gift.setCoverImageUri("http://img1.leyizhuang.com.cn/app/images/goods/2298/20170413115354094.png");
            gift.setGoodsSpecification("12MM");
            gift.setGoodsUnit("张");
            gift.setRetailPrice(0d);
            gift.setSkuName("龙牌家装石膏板9.5mm");
            GiftListResponseGoods gift1 = new GiftListResponseGoods();
            gift1.setIsGift(true);
            gift1.setQty(2);
            gift1.setGoodsId(33L);
            gift1.setCoverImageUri("http://img3.leyizhuang.com.cn/app/images/goods/2511/20170413115747766.jpg");
            gift1.setGoodsSpecification("15MM");
            gift1.setGoodsUnit("张");
            gift1.setRetailPrice(0d);
            gift1.setSkuName("龙牌耐潮石膏板9.5mm");

            responseGoodsList = goodsPriceService.findGoodsPriceListByGoodsIdsAndUserIdAndIdentityType(
                    goodsIdList, userId, AppIdentityType.getAppIdentityTypeByValue(identityType));

            //为返商品集合设置数量和赠品属性
            Iterator<GiftListResponseGoods> iterator = responseGoodsList.iterator();
            while (iterator.hasNext()) {
                GiftListResponseGoods goods = iterator.next();
                for (GoodsSimpleInfo info : goodsList) {
                    if (info.getId().equals(goods.getGoodsId())) {
                        goods.setQty(info.getNum());
                        goods.setIsGift(false);
                        break;
                    }
                }
            }

            GiftListResponse giftListResponse = new GiftListResponse();
            giftListResponse.setGoodsList(responseGoodsList);
            giftListResponse.setPromotionsGiftList(promotionsGiftList);

            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, giftListResponse);
            logger.info("materialListStepToGiftList OUT,确认商品计算工人订单总金额成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知异常,下料清单跳转赠品列表失败!", null);
            logger.warn("materialListStepToGiftList EXCEPTION,确认商品计算工人订单总金额失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }
}

package cn.com.leyizhuang.app.web.controller.settlement;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.foundation.pojo.order.GoodsSimpleInfo;
import cn.com.leyizhuang.app.foundation.pojo.response.GiftListResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.GiftListResponseGoods;
import cn.com.leyizhuang.app.foundation.service.GoodsPriceService;
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

    @Autowired
    private GoodsPriceService goodsPriceService;

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);


    @PostMapping(value = "/list", produces = "application/json;charset=UTF-8")
    public ResultDTO<GiftListResponse> materialListStepToGiftList(Long userId, Integer identityType, String goodsArray) {

        logger.info("materialListStepToGiftList CALLED,下料清单跳转赠品列表,入参 goodsArray:{}", goodsArray);

        ResultDTO<GiftListResponse> resultDTO;
        //获取商品相关信息（id，数量，是否赠品）
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JavaType javaType1 = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, GoodsSimpleInfo.class);
            List<GoodsSimpleInfo> goodsList = objectMapper.readValue(goodsArray, javaType1);
            //TODO 计算促销

            List<Long> promotionIdList = new ArrayList<>();
            List<Long> goodsIdList = new ArrayList<>();
            for (GoodsSimpleInfo goodsSimpleInfo : goodsList) {
                goodsIdList.add(goodsSimpleInfo.getId());
            }
            List<GiftListResponseGoods> responseGoodsList = new ArrayList<>();
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
            giftListResponse.setPromotionIds(promotionIdList);

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
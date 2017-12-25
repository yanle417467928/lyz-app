package cn.com.leyizhuang.app.foundation.service;


import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.foundation.pojo.activity.ActBaseDO;
import cn.com.leyizhuang.app.foundation.pojo.activity.ActGiftDetailsDO;
import cn.com.leyizhuang.app.foundation.pojo.activity.ActGoodsMappingDO;
import cn.com.leyizhuang.app.foundation.pojo.activity.ActStoreDO;
import cn.com.leyizhuang.app.foundation.pojo.response.OrderGoodsSimpleResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.PromotionDiscountListResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.PromotionsGiftListResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.PromotionsListResponse;
import com.github.pagehelper.PageInfo;
import org.springframework.ui.ModelMap;

import java.util.List;

/**
 * 促销服务
 * Created by panjie on 2017/11/22.
 */
public interface AppActService {

    List<ActBaseDO> queryList();

    PageInfo<ActBaseDO> queryPageVO(Integer page, Integer size ,String keywords,String status);

    void save(ActBaseDO baseDO, List<ActGoodsMappingDO> goodsList, List<ActGiftDetailsDO> giftList, Double subAmount, List<ActStoreDO> storeList);

    void edit(ActBaseDO baseDO, List<ActGoodsMappingDO> goodsList, List<ActGiftDetailsDO> giftList, Double subAmount, List<ActStoreDO> storeDOList);

    ModelMap getModelMapByActBaseId(ModelMap map, Long id);

    int insertBatch();

    /*获取全部促销结果*/
    PromotionsListResponse countAct(Long userId , AppIdentityType userType, List<OrderGoodsSimpleResponse> goodsInfoList);

    /*获取赠品促销结果*/
    List<PromotionsGiftListResponse> countGift(Long userId , AppIdentityType userType, List<OrderGoodsSimpleResponse> goodsInfoList);

    /*获取优惠结果*/
    List<PromotionDiscountListResponse> countDiscount(Long userId, AppIdentityType userType, List<OrderGoodsSimpleResponse> goodsInfoList);

    /*检查促销是否过期*/
    Boolean checkActOutTime(List<Long> actIdList);

    void publishAct(Long id);

    void inValid(Long id);
}

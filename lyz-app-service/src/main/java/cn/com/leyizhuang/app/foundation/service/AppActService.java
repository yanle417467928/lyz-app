package cn.com.leyizhuang.app.foundation.service;


import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.foundation.pojo.activity.ActBaseDO;
import cn.com.leyizhuang.app.foundation.pojo.activity.ActGiftDetailsDO;
import cn.com.leyizhuang.app.foundation.pojo.activity.ActGoodsMappingDO;
import cn.com.leyizhuang.app.foundation.pojo.activity.ActStoreDO;
import cn.com.leyizhuang.app.foundation.pojo.request.settlement.PromotionSimpleInfo;
import cn.com.leyizhuang.app.foundation.pojo.response.*;
import com.github.pagehelper.PageInfo;
import org.springframework.ui.ModelMap;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
 * 促销服务
 * Created by panjie on 2017/11/22.
 */
public interface AppActService {

    List<ActBaseDO> queryList();

    List<ActBaseDO> queryValidListByStoreId(Long storeId);

    List<PromotionListViewResponse> queryValidRepListByStoreId(Long userId , AppIdentityType identityType, Long storeId);

    PageInfo<ActBaseDO> queryPageVO(Integer page, Integer size ,String keywords,String status);

    /**
     * 新增
     * @param baseDO 基本信息
     * @param goodsList 本品集合
     * @param giftList 赠品集合
     * @param subAmount 立减金额
     * @param storeList 门店集合
     * @param discount 折扣
     */
    void save(ActBaseDO baseDO, List<ActGoodsMappingDO> goodsList, List<ActGiftDetailsDO> giftList, Double subAmount, List<ActStoreDO> storeList,Double discount);

    void edit(ActBaseDO baseDO, List<ActGoodsMappingDO> goodsList, List<ActGiftDetailsDO> giftList, Double subAmount, List<ActStoreDO> storeDOList,Double discount);

    ActBaseDO findById(Long promotionId);

    ModelMap getModelMapByActBaseId(ModelMap map, Long id);

    int insertBatch();

    /*获取全部促销结果*/
    PromotionsListResponse countAct(Long userId , AppIdentityType userType, List<OrderGoodsSimpleResponse> goodsInfoList,Long cusId,String scope) throws UnsupportedEncodingException;

    /*获取赠品促销结果*/
    List<PromotionsGiftListResponse> countGift(Long userId , AppIdentityType userType, List<OrderGoodsSimpleResponse> goodsInfoList,Long cusId,String scope) throws UnsupportedEncodingException;

    /*获取优惠结果*/
    List<PromotionDiscountListResponse> countDiscount(Long userId, AppIdentityType userType, List<OrderGoodsSimpleResponse> goodsInfoList,Long cusId,String scope) throws UnsupportedEncodingException;

    /*检查促销是否过期*/
    Boolean checkActOutTime(List<Long> actIdList);

    void publishAct(Long id);

    void inValid(Long id);

    Map<Long,Double> returnGcActIdAndJXDiscunt(List<PromotionSimpleInfo> simpleInfo);
}

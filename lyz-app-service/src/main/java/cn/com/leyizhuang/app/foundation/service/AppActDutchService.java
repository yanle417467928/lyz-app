package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderGoodsInfo;
import cn.com.leyizhuang.app.foundation.pojo.request.settlement.PromotionSimpleInfo;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * 分摊服务类
 * Created by panjie on 2017/12/11.
 */
public interface AppActDutchService {

    /**
     * 新增明细 分摊金额
     * @param userId
     * @param identityType
     * @param promotionSimpleInfoList
     * @param orderGoodsInfoList
     * @return
     */
    List<OrderGoodsInfo> addGoodsDetailsAndDutch(Long userId, AppIdentityType identityType, List<PromotionSimpleInfo> promotionSimpleInfoList, List<OrderGoodsInfo> orderGoodsInfoList) throws UnsupportedEncodingException;

    /**
     * 计算并 设置退货单价
     * @param goodsInfoList
     * @return
     */
    List<OrderGoodsInfo> countReturnPrice(List<OrderGoodsInfo> goodsInfoList);
}

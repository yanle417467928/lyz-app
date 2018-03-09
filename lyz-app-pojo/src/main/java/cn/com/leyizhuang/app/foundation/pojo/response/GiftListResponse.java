package cn.com.leyizhuang.app.foundation.pojo.response;

import cn.com.leyizhuang.app.core.constant.AppDeliveryType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/10/25
 */
@Getter
@Setter
@ToString
public class GiftListResponse {
    // 本品list
    List<GiftListResponseGoods> goodsList = new ArrayList<>();

    // 产品券List
    List<GiftListResponseGoods> couponGoodsList = new ArrayList<>();

    // 促销赠品List
    List<PromotionsGiftListResponse> promotionsGiftList = new ArrayList<>();

    /**
     * 系统判定配送方式
     */
    AppDeliveryType sysDeliveryType;
}

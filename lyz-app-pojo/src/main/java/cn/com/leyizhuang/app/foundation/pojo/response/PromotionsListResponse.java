package cn.com.leyizhuang.app.foundation.pojo.response;

import lombok.*;

import java.util.List;

/**
 * 促销响应类
 * Created by panjie on 2017/12/5.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PromotionsListResponse {

    // 优惠立减促销集合
    List<PromotionDiscountListResponse> promotionDiscountList;

    // 赠品促销集合
    List<PromotionsGiftListResponse> promotionGiftList;

    // 产品券促销集合

    // 现金券促销集合
}

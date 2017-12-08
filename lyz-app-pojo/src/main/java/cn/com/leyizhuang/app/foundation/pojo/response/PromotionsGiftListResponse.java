package cn.com.leyizhuang.app.foundation.pojo.response;

import lombok.*;

import java.util.List;

/**
 * 赠品促销响应类
 * Created by panjie on 2017/12/5.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PromotionsGiftListResponse {

    // 促销id
    private Long promotionId;

    // 促销标题
    private String promotionTitle;

    // 最大可选赠品数量
    private Integer maxChooseNumber;

    // 赠品集合
    List<GiftListResponseGoods> giftList;
}

package cn.com.leyizhuang.app.foundation.pojo.response;

import lombok.*;

import java.util.List;

/**
 * 优惠立减促销响应类
 * Created by panjie on 2017/12/5.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PromotionDiscountListResponse {

    // 促销id
    private Long promotionId;

    // 促销标题
    private String promotionTitle;

    // 参与此促销次数
    private Integer enjoyTimes;

    // 优惠金额
    private Double discountPrice;

}

package cn.com.leyizhuang.app.foundation.pojo.response;

import lombok.*;

import java.util.List;

/**
 * 促销视图模型
 * Created by panjie on 2018/3/20.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PromotionListViewResponse {

    // 促销id
    private Long promotionId;

    // 促销标题
    private String promotionTitle;

    // 图片
    private String picUrl;

    // 城市id
    private Long cityId;

    // 参与商品集合
    private List<GiftListResponseGoods> goodsList;

}

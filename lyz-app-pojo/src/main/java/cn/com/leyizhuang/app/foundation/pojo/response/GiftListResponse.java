package cn.com.leyizhuang.app.foundation.pojo.response;

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

    List<GiftListResponseGoods> goodsList = new ArrayList<>();


    List<PromotionsGiftListResponse> promotionsGiftList = new ArrayList<>();

}

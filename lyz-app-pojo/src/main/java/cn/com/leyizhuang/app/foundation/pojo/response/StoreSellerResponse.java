package cn.com.leyizhuang.app.foundation.pojo.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/11/2
 */
@Getter
@Setter
@ToString
public class StoreSellerResponse {

    //归属导购ID
    private Long sellerId;
    //归属门店id
    private Long storeId;
    //是否可修改归属导购
    private Boolean isPassable;
    //门店列表
    private List<StoreResponse> storeList;
    //导购列表
    private List<SellerResponse> sellerList;
}

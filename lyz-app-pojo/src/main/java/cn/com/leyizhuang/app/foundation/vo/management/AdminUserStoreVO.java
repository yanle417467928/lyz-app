package cn.com.leyizhuang.app.foundation.vo.management;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author GenerationRoad
 * @date 2018/2/27
 */
@Getter
@Setter
@ToString
public class AdminUserStoreVO {

    private Long id;
    //用户id
    private Long uid;
    //门店id
    private Long storeId;
    //城市id
    private Long cityId;
    // 城市名称
    private String cityName;
    //门店名称
    private String storeName;
}

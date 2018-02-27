package cn.com.leyizhuang.app.foundation.pojo.management;

import lombok.*;

/**
 * @author GenerationRoad
 * @date 2018/2/26
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AdminUserStoreDO {
    private Long id;
    //用户id
    private Long uid;
    //门店id
    private Long storeId;
    //城市id
    private Long cityId;
}

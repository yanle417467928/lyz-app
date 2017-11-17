package cn.com.leyizhuang.app.foundation.pojo.response;

import lombok.*;

/**
 * 乐币变动明细返回类
 * Created by caiyu on 2017/11/8.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LeBiVariationLogResPonse {
    //变动时间
    private String variationTime;
    //变动类型
    private String leBiVariationType;
    //变动数量
    private int variationQuantity;
    //乐币使用订单号
    private String orderNum;
    //乐币总数量
    private int totalQuantity;
}

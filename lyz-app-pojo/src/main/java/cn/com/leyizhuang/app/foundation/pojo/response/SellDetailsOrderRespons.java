package cn.com.leyizhuang.app.foundation.pojo.response;

import lombok.*;

/**
 * Created by 12421 on 2018/7/14.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SellDetailsOrderRespons {

    // 单号
    private  String ordNo;
    // 出货时间
    private  String shipmentTime;
    // 单号类型 order / return
    private  String orderType;
}

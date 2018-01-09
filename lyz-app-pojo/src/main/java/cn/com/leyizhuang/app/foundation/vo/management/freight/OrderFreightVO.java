package cn.com.leyizhuang.app.foundation.vo.management.freight;

import cn.com.leyizhuang.app.foundation.pojo.management.city.SimpleCityParam;
import cn.com.leyizhuang.app.foundation.pojo.management.order.SimpleOrderBillingDetails;
import cn.com.leyizhuang.app.foundation.pojo.management.store.SimpleStoreParam;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OrderFreightVO {

    private Long id;
    //城市信息
    private String cityName;
    //门店信息
    private SimpleStoreParam storeId;
    //订单号
    private String ordNo;
    //下单人
    private String creatorName;
    //下单人电话
    private String creatorPhone;
    //简化订单账款明细
    private SimpleOrderBillingDetails simpleOrderBillingDetails;
}

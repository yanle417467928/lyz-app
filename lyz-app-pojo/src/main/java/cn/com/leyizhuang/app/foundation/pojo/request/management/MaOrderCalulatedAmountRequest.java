package cn.com.leyizhuang.app.foundation.pojo.request.management;

import cn.com.leyizhuang.app.core.constant.AppDeliveryType;
import cn.com.leyizhuang.app.foundation.pojo.GoodsSkuQtyParam;
import lombok.*;

import java.util.List;

/**
 * Created by caiyu on 2018/6/8.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MaOrderCalulatedAmountRequest {
    /**
     * 用户Id
     */
    private Long userId;
    /**
     * 用户身份类型
     */
    private Integer identityType;
    /**
     * 代下单客户id
     */
    private Long customerId;
    /**
     * 传输的商品本品集合
     */
    private String goodsList;
    /**
     * 系统判定配送方式
     */
    private String sysDeliveryType;
    /**
     * 门店id
     */
    private Long storeId;
    /**
     * 城市id
     */
    private Long cityId;
    /**
     * 区县
     */
    private String county;
}

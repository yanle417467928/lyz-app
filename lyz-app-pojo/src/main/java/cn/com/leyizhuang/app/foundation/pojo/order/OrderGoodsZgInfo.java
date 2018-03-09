package cn.com.leyizhuang.app.foundation.pojo.order;

import lombok.*;

/**
 * 专供产品销售明细
 * Created by panjie on 2018/3/8.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderGoodsZgInfo {

    private Long id;

    /**
     * 顾客id
     */
    private Long userId;

    /**
     * 商品id
     */
    private Long gid;

    /**
     * sku
     */
    private String sku;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 订单商品明细行ID
     */

    private Long goodsLineId;

    /**
     * 创建时间
     */
    private Data createTime;
}

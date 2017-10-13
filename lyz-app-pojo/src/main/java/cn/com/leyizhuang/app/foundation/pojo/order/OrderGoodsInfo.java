package cn.com.leyizhuang.app.foundation.pojo.order;

import lombok.*;

/**
 * 订单商品明细
 *
 * @author Richard
 * Created on 2017-10-10 11:45
 **/
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderGoodsInfo {

    private Long id;

    //订单号
    private String orderNumber;

    //商品编码
    private String sku;

    //零售价
    private Double retailPrice;

    //会员价
    private Double VIPPrice;

    //经销价
    private Double wholesalePrice;

    //分摊价
    private Double sharePrice;

    //订单数量
    private Integer orderQuantity;

    //出货数量
    private Integer outboundQuantity;

    //已退货数量
    private Integer returnQuantity;

    //可退数量
    private Integer returnableQuantity;

    //是否赠品
    private Boolean isGift;

}

package cn.com.leyizhuang.app.foundation.pojo.order;

import cn.com.leyizhuang.app.core.constant.AppGoodsLineType;
import lombok.*;

import java.util.Date;

/**
 * 订单经销差价返还明细
 *
 * @author Richard
 * Created on 2018-01-19 11:26
 **/
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OrderJxPriceDifferenceReturnDetails {

    private Long id;

    /**
     * 订单id
     */
    private Long oid;

    /**
     * 订单号
     */
    private String orderNumber;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 门店id
     */
    private Long storeId;

    /**
     * 门店编码
     */
    private String storeCode;

    /**
     * 商品编码
     */
    private String sku;

    /**
     * 经销差价单价
     */
    private Double unitPrice;

    /**
     * 数量
     */
    private Integer quantity;

    /**
     * 经销差价总额
     */
    private Double amount;

    /**
     * 收款单号
     */
    private String receiptNumber;

    private AppGoodsLineType goodsLineType;
}

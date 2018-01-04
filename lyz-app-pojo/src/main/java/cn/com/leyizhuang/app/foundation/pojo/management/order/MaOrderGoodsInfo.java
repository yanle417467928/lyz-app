package cn.com.leyizhuang.app.foundation.pojo.management.order;

import lombok.*;

import java.math.BigDecimal;
/**
 * 订单商品信息
 *
 * @author liuh
 * Created on 2017-11-03 14:14
 **/
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MaOrderGoodsInfo {
    private Long id;
    //商品编码
    private String sku;
    //商品名称
    private String skuName;
    //商品数量
    private Integer orderQty;
    //商品单价
    private BigDecimal returnPrice;
    //商品金额
    private BigDecimal settlementPrice;
}

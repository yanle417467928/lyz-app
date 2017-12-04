package cn.com.leyizhuang.app.foundation.pojo.returnOrder;

import lombok.*;

/**
 * 退单商品
 * Created by caiyu on 2017/12/2.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderReturnGoodsInfo {

    private Long id;
    /**
     * 退单Id
     */
    private Long roid;
    /**
     * 退单编号
     */
    private String returnNo;
    /**
     * 商品Id
     */
    private Long gid;
    /**
     * 商品编码
     */
    private String sku;
    /**
     * 商品名
     */
    private String skuName;
    /**
     * 零售价
     */
    private Double retailPrice;
    /**
     * 会员价
     */
    private Double vipPrice;
    /**
     * 经销价
     */
    private Double wholesalePrice;
    /**
     * 分摊价
     */
    private Double returnPrice;
    /**
     * 退单数量
     */
    private int returnQty;
}

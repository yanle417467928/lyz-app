package cn.com.leyizhuang.app.foundation.pojo.management.returnOrder;

import cn.com.leyizhuang.app.core.constant.AppGoodsLineType;
import lombok.*;

/**
 * 退单商品
 * Created by liuh on 2017/12/2.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MaReturnOrderGoodsInfo {

    private Long id;

    /**
     * 订单商品行Id
     */
    private Long orderGoodsId;
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
     * 结算价
     */
    private Double settlementPrice;
    /**
     * 退单数量
     */
    private int returnQty;
    /**
     * 商品行类型（暂定）
     */
    private AppGoodsLineType goodsLineType;
    /**
     * 公司标识
     */
    private String companyFlag;
}

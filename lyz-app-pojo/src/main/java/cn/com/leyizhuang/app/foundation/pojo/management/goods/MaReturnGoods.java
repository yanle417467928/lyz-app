package cn.com.leyizhuang.app.foundation.pojo.management.goods;

import cn.com.leyizhuang.app.core.constant.AppGoodsLineType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * @author liuh
 * @date 2017/11/06
 */
@Getter
@Setter
@ToString
public class MaReturnGoods {

    //商品id
    private Long gid;
    //商品sku
    private String sku;
    //商品名称
    private String skuName;
    //退货数量
    private Integer returnQty;
    //退货单价
    private BigDecimal returnPrice;
    //退货金额
    private BigDecimal totalPrice;
}

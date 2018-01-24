package cn.com.leyizhuang.app.foundation.pojo.management.goods;

import cn.com.leyizhuang.app.core.constant.AppDeliveryType;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品出货记录
 *
 * @author liuh
 * create_time 2018-1-15 14:19:50
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class GoodsShippingInfo {
    private Long id;
    //商品ID
    private Long gid;
    //商品编码
    private String sku;
    //价格
    private BigDecimal price;
    //商品名称
    private String skuName;
    //出货时间
    private Date shippingTime;
    //数量
    private Integer qty;
    //变更类型
    private String changeType;
    //变更类型描述
    private String changeTypeDesc;
    //关联单号
    private String referenceNumber;
    //出货单创建人
    private String creator;
    //配送类型
    private AppDeliveryType deliveryType;
    //出货门店
    private String store;
    //城市
    private String city;

}


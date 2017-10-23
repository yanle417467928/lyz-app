package cn.com.leyizhuang.app.foundation.pojo.order;

import lombok.*;

/**
 * 装饰公司物料审核单商品明细
 * Created by caiyu on 2017/10/17.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MaterialAuditGoodsInfo {
    //id
    private Long id;
    //物料审核单头id
    private Long auditHeaderID;
    //商品id
    private Long gid;
    //商品编码
    private String sku;
    //商品名称
    private String skuName;
    //商品数量
    private int qty;
    //商品规格
    private String goodsSpecification;
    //商品单位
    private String goodsUnit;
    //商品图片uri
    private String coverImageUri;
    //是否是赠品
    private Boolean isGift;
}

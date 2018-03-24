package cn.com.leyizhuang.app.datatransfer.pojo;

import lombok.*;

/**
 * 一期订单商品表 数据迁移用
 * Created by panjie on 2018/3/24.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TdOrderGoods {

    private Long id;

    // 商品ID
    
    private Long goodsId;

    // 商品名称

    private String goodsTitle;

    // 商品简介
    
    private String goodsSubTitle;

    // 商品封面
    
    private String goodsCoverImageUri;

    // 商品的SKU
    
    private String sku;

    // 商品版本颜色
    
    private String goodsColor;

    // 商品版本容量
    
    private String goodsCapacity;

    // 商品版本名称
    
    private String goodsVersion;

    // 商品销售方式
    
    private Integer goodsSaleType;

    // 成交价

    private Double price;

    // 真实价格
    
    private Double realPrice;

    // 商品数量
    
    private Long quantity;

    // 发货数量
    
    private Long deliveredQuantity;

    // 积分
    
    private Long points;

    // 是否申请了退还该商品？
    
    private Boolean isReturnApplied;

    // 是否已评价
    
    private Boolean isCommented;

    // 评论ID
    
    private Long commentId;

    // 商品品牌标题
    
    private String brandTitle;

    // 商品的品牌id
    
    private Long brandId;

    // 可退数量
    
    private Long canReturnNumber;

    // 退款单价
    
    private Double returnUnitPrice;

    // 使用产品券的数量，默认为0
    
    private Long couponNumber;

    // 使用指定产品现金券的数量，默认为0
    
    private Long cashNumber;

    // 退货数量
    
    private Long returnNumber;

    // 促销赠品价格
    
    private Double giftPrice;

    // 退货单号
    
    private String returnNoteNumber;

    // 分单号
    
    private String subOrderNumber;

    // 表示已选类型（目前：0. 代表商品；1. 代表产品券）
    
    private Long type;

    // 是否是券
    
    private Boolean isCoupon;

    // 调色包归属商品SKU zp
    
    private String ownerGoodsSku;
    // 活动id(可以为多个) zp
    
    private String activityId;

    // 券金额
    
    private Double couponMoney;

    // 该商品运费单价

    private Double deliveryUnit;

    // 满减分摊金额

    private Double shareUnit;

    // 是否墙面辅料

    private Boolean isWallAccessory = false;

//	// order表id
//	@Transient
//	private Long tdOrderId;
//
//	// returnNote表id
//	@Transient
//	private Long tdReturnId;

    // 经销价

    private Double jxPrice = 0d;

    // 经销差价

    private Double jxDif = 0d;

    // 单品经销差价总额

    private Double difTotal = 0d;

    // 如果参加 折扣促销 则记录id
    
    private Long act_id;

    // 分类树
    private String categoryTree;
}

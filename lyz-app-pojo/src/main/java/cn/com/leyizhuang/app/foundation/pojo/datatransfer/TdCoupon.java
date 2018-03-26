package cn.com.leyizhuang.app.foundation.pojo.datatransfer;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @author GenerationRoad
 * @date 2018/3/26
 */
@Getter
@Setter
@ToString
public class TdCoupon {
    private Long id;

    // 优惠券获取方式：1. 后台手动发放 2. 用户抢券 3. 退货新生成券 4.系统自动发放给会员
    private Long typeId;

    // 优惠券限用分类类型ID: 1. 通用现金券；2. 指定商品现金券；3. 产品券；4.数据导入
    private Long typeCategoryId;

    // 券所属公司ID
    private Long brandId;

    // 券所属公司名称
    private String brandTitle;

    /*
     * 可使用商品id（针对于通用 现金券，其值为null）
     *
     * @author dengxiao
     */
    private Long goodsId;

    /*
     * 针对于产品券，所显示的产品的图片
     */
    private String picUri;

    // 可使用的商品名称
    private String goodsName;

    // 优惠券名称
    private String typeTitle;

    // 优惠券描述
    private String typeDescription;

    // 优惠券图片
    private String typePicUri;

    // 金额
    private Double price;

    // 实际使用金额
    private Double realPrice;

    // 是否已领用
    private Boolean isDistributted;

    // 领用日期
    private Date getTime;

    // 领用用户
    private String username;

    // 是否已使用
    private Boolean isUsed;

    // 使用日期
    private Date useTime;

    // 使用订单id
    private Long orderId;

    // 使用订单号
    private String orderNumber;

    // 是否过期
    private Boolean isOutDate;

    // 过期日期
    private Date expireTime;

    // 剩余数量
    private Long leftNumber;

    // 领取数量
    private Long getNumber;

    // 手机号
    private String mobile;

    // 排序号
    private Double sortId;

    // 使用日期
    private Date addTime;
    // 门店编号
    private String diySiteCode;
    // 门店名称
    private String diySiteTital;

    // 销顾名称
    private String sellerRealName;

    // 销顾账号
    private String sellerUsername;

    // 销顾id
    private Long sellerId;

    // 城市id
    private Long cityId;
    // 城市名
    private String cityName;

    // 用户编号（ebs）
    private String customerId;

    // sku
    private String sku;

    // 使用门店code
    private String useDiySiteCode;

    // 使用门店名称
    private String useDiySiteTitle;

    // 使用门店id
    private Long useDiySiteId;

    // 使用销顾名称
    private String useSellerRealName;

    // 使用销顾账号
    private String useSellerUsername;

    // 使用销顾id
    private Long useSellerId;

    // 是否是购买的券
    private Boolean isBuy;

    // 买券时候的价格
    private Double buyPrice;

    // 券订单号
    private String couponOrderNumber;

    // 购买时的价值
    private Double tradePrice;

    // 标识
    private String sign;
}

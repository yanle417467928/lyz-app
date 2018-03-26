package cn.com.leyizhuang.app.foundation.pojo.datatransfer;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @author GenerationRoad
 * @date 2018/3/24
 */
@Getter
@Setter
@ToString
public class TdOrderData {
    private Long id;

    private Date createTime;

    // 收货人是否是主家
    private Boolean receiverIsMember = Boolean.FALSE;

    // 订单归属用户名
    private String username;

    // 主单号
    private String mainOrderNumber;

    // 门店ID
    private Long storeId;

    // 门店标题
    private String storeTitle;

    // 门店编码
    private String storeCode;

    // 导购ID
    private Long sellerId;

    // 导购姓名
    private String sellerName;

    // 导购电话号码
    private String sellerPhone;

    // 出货单号
    private String shipmentNumber;

    // 商品总金额
    private Double totalGoodsPrice = 0d;

    // 会员折扣
    private Double memberDiscount = 0d;

    // 满减金额
    private Double activitySub = 0d;

    // 产品券扣减金额
    private Double proCouponFee = 0d;

    // 现金券扣减金额
    private Double cashCouponFee = 0d;

    // 预存款使用额
    private Double balanceUsed = 0d;

    // 线上支付金额
    private Double onlinePay = 0d;

    // 运费
    private Double deliveryFee = 0d;

    // 应付金额
    private Double leftPrice = 0d;

    // 代收金额
    private Double agencyRefund = 0d;

    // 配送收款现金
    private Double deliveryCash = 0d;

    // 配送收款POS
    private Double deliveryPos = 0d;

    // 导购收款现金
    private Double sellerCash = 0d;

    // 导购收款POS
    private Double sellerPos = 0d;

    // 导购收款其他
    private Double sellerOther = 0d;

    // 多余的收款金额
    private Double redundant = 0d;

    // 收款之后的欠款金额
    private Double due = 0d;

    // 归还会员的金额
    private Double refundBalance = 0d;

    // 配送员备注
    private String remark;
}

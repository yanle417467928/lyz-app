package cn.com.leyizhuang.app.foundation.pojo.datatransfer;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.Date;

/**
 * @author GenerationRoad
 * @date 2018/3/24
 */
@Getter
@Setter
@ToString
public class TdOrder {

    private Long id;

    // 订单号(分单号)
    private String orderNumber;

    // 用户id
    private Long userId;

    // 拆分的收货地址分为：城市 + 区 + 街道 + 详细地址
    // 省
    private String province;

    // 城市
    private String city;

    // 区
    private String disctrict;

    // 街道
    private String subdistrict;

    // 详细地址
    private String detailAddress;

    // 收货地址
    private String shippingAddress;

    // 收货人
    private String shippingName;

    // 收货电话
    private String shippingPhone;

    // 邮政编码
    private String postalCode;

    // 支付方式
    private Long payTypeId;

    // 支付方式名称
    private String payTypeTitle;

    // 支付方式手续费
    private Double payTypeFee;

    // 配送方式名称
    private String deliverTypeTitle;

    // 配送费用
    private Double deliverFee;

    // 配送门店id
    private Long diySiteId;

    // 配送门店编码
    private String diySiteCode;

    // 配送门店名称
    private String diySiteName;

    // 配送门店联系电话
    private String diySitePhone;

    // 物流备注
    private String remarkInfo;

    // 下单时间
    private Date orderTime;

    // 取消时间
    private Date cancelTime;

    // 确认时间
    private Date checkTime;

    // 付款时间
    private Date payTime;

    // 配送时间
    private Date deliveryTime;

    // 发货时间
    private Date sendTime;

    // 收货时间
    private Date receiveTime;

    // 评价时间
    private Date commentTime;

    // 完成时间
    private Date finishTime;

    // 取消申请时间
    private Date cancelApplyTime;

    // 退款时间
    private Date refundTime;

    // 订单状态 1:待审核 2:待付款 3:待出库 4:待签收 5: 待评价 6: 已完成 7: 已取消 8:用户删除 9:退货中 10：退货确认
    // 11：退货取消 12: 退货完成
    private Long statusId;

    // 订单类型
    private Long typeId;

    // 是否取消订单
    private Boolean isCancel;

    // 是否退款
    private Boolean isRefund;

    // 退款金额
    private Double refund;

    // 退款详细
    private String handleDetail;

    // 用户
    private String username;

    // 配送人
    private String distributionPerson;

    // 收款人
    private String moneyReceivePerson;

    // 商品总金额
    private Double totalGoodsPrice;

    // 使用可提现预存款金额
    private Double cashBalanceUsed;

    // 使用不可提现预存款金额
    private Double unCashBalanceUsed;

    // 订单总金额
    private Double totalPrice;

    // 实收款
    private Double actualPay;

    // 排序号
    private Double sortId;

    // 是否在线付款
    private Boolean isOnlinePay;

    // 配送日期(yyyy-MM-dd)
    private String deliveryDate;

    // 配送时间段
    private Long deliveryDetailId;

    // 订单备注
    private String remark;

    // 是否参加了促销
    private Boolean isPromotion;

    // 使用现金券额度
    private Double cashCoupon;

    // 使用产品券情况
    private String productCoupon;

    // 使用产品券id，多个之间以","分割
    private String productCouponId;

    // 使用现金券的id，多个之间以","分割
    private String cashCouponId;

    // 品牌名称
    private String brandTitle;

    // 品牌id
    private Long brandId;

    // 使用优惠券限额
    private Double limitCash;

    // 签收图片地址
    private String photo;

    // 是否是券
    private Boolean isCoupon;

    // 主单号
    private String mainOrderNumber;

    // 实际支付预存款总额
    private Double allActualPay;

    // 实际总单金额
    private Double allTotalPay;

    // 销售顾问的id
    private Long sellerId;

    // 销售顾问的用户名
    private String sellerUsername;

    // 销售顾问的真实姓名
    private String sellerRealName;

    // 是否使用预存款
    private Boolean isUsedBalance;

    // 订单用户是否绑定导购
    private Boolean haveSeller;

    // 是否是代下单
    private Boolean isSellerOrder;

    // 真实用户名
    private String realUserUsername;

    // 真实用户id
    private Long realUserId;

    // 真实用户的真实姓名
    private String realUserRealName;

    // 第三方支付的金额
    private Double otherPay;

    // POS支付的金额
    private Double posPay;

    // 现金支付的金额
    private Double cashPay;

    // 新增：2016-08-25收款方式其他，只有门店能够使用
    private Double backOtherPay;

    // 有效时间(超过有效时间未支付将重新计算价格)
    private Date validTime;
    // 其他收入(不参与其他计算 只影响代收金额) zp
    private Double otherIncome;

    // 购买的优惠券使用情况
    private String buyCouponId;

    // 促销减少的金额
    private Double activitySubPrice = 0d;

    // 是否以一口价的形式收取运费
    private Boolean isFixedDeliveryFee;

    // 应收运费
    private Double receivableFee;

    // 分单占主单的比例
    private Double point;

    // 上楼方式
    private String upstairsType = "不上楼";

    // 楼层
    private Long floor = 1l;

    // 上楼费
    private Double upstairsFee = 0d;

    // 已收上楼费（预存款）
    private Double upstairsBalancePayed = 0d;

    // 已收上楼费（第三方）
    private Double upstairsOtherPayed = 0d;

    // 剩余上楼费
    private Double upstairsLeftFee = 0d;

    // 公司承担的运费
    private Double companyDeliveryFee = 0d;

    // 装饰公司使用的信用额度
    private Double credit = 0d;

    // 装饰公司使用的赞助金
    private Double promotionMoneyPayed = 0d;

    // 经销总价
    private Double jxTotalPrice = 0d;

    // 产品券优惠券金额
    private Double proCouponFee;

    // 调色费
    private Double colorFee = 0d;

    // 会员差价金额
    private Double difFee = 0d;

    // 显示在界面上的剩余金额
    private Double notPayedFee = 0d;

    // 收货人是否是主家
    private Boolean receiverIsMember = Boolean.FALSE;

    // 传递给wms的应收
    private Double mockReceivable;

    // 传递给WMS的代收，同时也是订单实际的代收
    private Double agencyRefund = 0d;

    // 拍照下单标志
    private Boolean isPhotoOrder = Boolean.FALSE;

    //使用钱包金额
    private Double walletMoney = 0d;

    private Double alipayMoney = 0d;

    //上次更新时间
    private Timestamp lastUpdateTime;

    //纸质销货单号
    private String paperSalesNumber;
}

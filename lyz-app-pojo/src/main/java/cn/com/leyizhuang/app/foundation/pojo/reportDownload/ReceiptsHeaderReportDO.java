package cn.com.leyizhuang.app.foundation.pojo.reportDownload;

/**
 * 收款头 表
 * Created by panjie on 2018/6/7.
 */
public class ReceiptsHeaderReportDO {
    //城市
    private String cityName;
    //门店名称
    private String storeName;
    //门店类型
    private String storeType;
    //付款时间
    private String payTime;
    //退款时间
    private String returnPayTime;
    //订/退单号
    private String orderNumber;
    //退单号
    private String returnOrderNumber;
    //备注
    private String remarks;
    //导购姓名
    private String sellerName;

    // 订单金额

    // 支付金额

    // 会员折扣金额
    private Double memberDiscount = 0.00;
    // 订单折扣
    private Double promotionDiscount = 0.00;
    // 优惠券折扣
    private Double cashCouponDiscount = 0.00;
    // 产品券抵扣
    private Double productCouponDiscount = 0.00;
    // 运费
    private Double freight = 0.00;
    private Double cus_pre_deposit = 0.00;
    private Double st_pre_deposit = 0.00;
    private Double st_credit_money = 0.00;
    private Double st_subvention = 0.00;
    private Double amount_payable = 0.00;
    private Double emp_credit_money = 0.00;
    private Double collection_amount = 0.00;
    private Double wechatAmount = 0.00;
    private Double alipayAmount = 0.00;
    private Double arrearage = 0.00;
    private String isPayUp;
    private String payUpTime;





}

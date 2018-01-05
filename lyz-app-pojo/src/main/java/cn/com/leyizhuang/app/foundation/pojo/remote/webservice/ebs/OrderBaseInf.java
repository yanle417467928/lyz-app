package cn.com.leyizhuang.app.foundation.pojo.remote.webservice.ebs;

import cn.com.leyizhuang.app.core.constant.AppDeliveryType;
import cn.com.leyizhuang.app.core.constant.AppOrderSubjectType;
import cn.com.leyizhuang.app.core.constant.AppWhetherFlag;
import cn.com.leyizhuang.app.core.constant.ProductType;
import lombok.*;

import java.util.Date;

/**
 * 订单基础信息
 *
 * @author Richard
 * Created on 2018-01-02 13:39
 **/
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OrderBaseInf {

    private Long id;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 是否传输成功
     */
    private AppWhetherFlag sendFlag;

    /**
     * 错误信息
     */
    private String errorMsg;

    /**
     * 传输成功时间
     */
    private Date sendTime;

    /**
     * 订单（主单）号
     */
    private String mainOrderNumber;

    /**
     * 分单号
     */
    private String orderNumber;

    /**
     * 分公司 id
     */
    private Long sobId;

    /**
     * 发货日期
     */
    private Date orderDate;

    /**
     * 订单下单主体类型，装饰公司、门店
     */
    private AppOrderSubjectType orderSubjectType;

    /**
     * APP单据产品类型
     */
    private ProductType productType;

    /**
     * 销售单类型
     * 1.要货单："B2B" HR产品直接无价批发给直营门店（以后在SCRM-APP下单）
     * 2.要货单："B2B" HR产品经销价批发给经销门店（分销业务）
     * 3.销售订单："B2C" LYZ和YR产品直接零售价销售给门店（目前无此业务）
     * 4.销售订单："B2C" HR,LYZ和YR产品直接零售价销售给会员
     */
    private Long orderTypeId;

    /**
     * 顾客id
     */
    private Long userId;

    /**
     * 导购id
     */
    private Long salesConsultId;

    /**
     * 装饰公司经理id
     */
    private Long decorateManagerId;

    /**
     * 是否用券标识（Y/N）
     */
    private String couponFlag;

    /**
     * 是否开票(Y/N)
     */
    private String invoiceFlag;

    /**
     * 是否使用信用额度(Y/N)
     */
    private String creditFlag;

    /**
     * 门店组织id
     */
    private Long storeOrgId;

    /**
     * 门店编码
     */
    private String storeCode;

    /**
     * 配送方式
     */
    private AppDeliveryType deliveryTypeTitle;

    /**
     * 订单商品总金额
     */
    private Double orderAmt;

    /**
     * 应付金额
     */
    private Double recAmt;

    /**
     * 乐币折扣
     */
    private Double lebiDiscount;

    /**
     * 门店现金返利折扣
     */
    private Double storeSubventionDiscount;

    /**
     * 促销折扣
     */
    private Double promotionDiscount;

    /**
     * 会员折扣
     */
    private Double memberDiscount;

    /**
     * 优惠券折扣
     */
    private Double cashCouponDiscount;

    /**
     * 产品券折扣
     */
    private Double productCouponDiscount;


}

package cn.com.leyizhuang.app.foundation.pojo.remote.webservice.ebs;

import cn.com.leyizhuang.app.core.constant.AppDeliveryType;
import cn.com.leyizhuang.app.core.constant.OrderBillingPaymentType;
import cn.com.leyizhuang.app.core.constant.ProductType;
import lombok.*;

import java.util.Date;

/**
 * 订单收款信息
 *
 * @author Richard
 * Created on 2018-01-02 13:39
 **/
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OrderReceiptInf {

    private Long id;

    /**
     * 订单（主单）号
     */
    private String mainOrderNumber;

    /**
     * 分公司 id
     */
    private String sobId;

    /**
     * 收款方式
     */
    private OrderBillingPaymentType receiptType;

    /**
     * 顾客id
     */
    private Long userId;

    /**
     * 导购id
     */
    private Long salesConsultId;

    /**
     * 门店组织id
     */
    private Long storeOrgId;

    /**
     * 门店编码
     */
    private String storeCode;

    /**
     * 收款时间
     */
    private Date receiptDate;


    /**
     * 收款金额
     */
    private Double orderAmt;

    /**
     * 收款方式说明
     */
    private String description;
    /**
     * 应付金额
     */
    private Double recAmount;

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

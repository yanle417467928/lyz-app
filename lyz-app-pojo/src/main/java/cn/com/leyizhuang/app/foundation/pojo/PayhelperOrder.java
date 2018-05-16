package cn.com.leyizhuang.app.foundation.pojo;

import cn.com.leyizhuang.app.core.constant.OrderBillingPaymentType;
import cn.com.leyizhuang.app.core.constant.PayhelperType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @author Jerry.Ren
 * create 2018-05-14 9:55
 * desc:装饰公司代付订单实体类
 **/
@Getter
@Setter
@ToString
public class PayhelperOrder {

    private Long id;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 订单ID
     */
    private Long oid;
    /**
     * 订单号
     */
    private String orderNumber;
    /**
     * 代付金额
     */
    private Double payhelperAmount;
    /**
     * 代付人ID
     */
    private Long payhelperId;
    /**
     * 代付人类型
     */
    private PayhelperType payhelperType;
    /**
     * 是否已支付
     */
    private Boolean isPayOver;
    /**
     * 支付类型
     */
    private OrderBillingPaymentType payType;
}

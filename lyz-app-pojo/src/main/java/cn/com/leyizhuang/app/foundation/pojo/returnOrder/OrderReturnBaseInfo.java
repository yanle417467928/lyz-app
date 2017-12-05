package cn.com.leyizhuang.app.foundation.pojo.returnOrder;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.core.constant.AppOrderReturnStatus;
import lombok.*;

import java.util.Date;

/**
 * 退单基础类
 * Created by caiyu on 2017/12/2.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OrderReturnBaseInfo {
    private Long roid;
    /**
     * 订单id
     */
    private Long orderId;
    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 创建订单时间
     */
    private Date orderTime;
    /**
     * 创建退单时间
     */
    private Date returnTime;
    /**
     * 退单号
     */
    private Date returnNo;
    /**
     * 退单类型（线上支付，货到付款）
     */
    private String returnType;
    /**
     * 退款金额
     */
    private Double returnPrice;
    /**
     * 备注
     */
    private String remarksInfo;
    /**
     * 创建退单人id
     */
    private Long creatorId;
    /**
     * 创建退单人类型
     */
    private AppIdentityType creatorIdentityType;
    /**
     * 创建退单人电话号码
     */
    private String creatorPhone;
    /**
     * 顾客id
     */
    private Long customerId;
    /**
     * 顾客姓名
     */
    private String customerName;
    /**
     * 顾客类型（零售、会员）
     */
    private String customerType;
    /**
     * 退货原因
     */
    private String reasonInfo;
    /**
     * 退货物流方式
     */
    private String orderType;
    /**
     * 退货物流状态
     */
    private AppOrderReturnStatus returnStatus;

}

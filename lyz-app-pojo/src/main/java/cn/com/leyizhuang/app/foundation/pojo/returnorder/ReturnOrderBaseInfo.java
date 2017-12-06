package cn.com.leyizhuang.app.foundation.pojo.returnorder;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.core.constant.AppOrderReturnStatus;
import cn.com.leyizhuang.app.core.constant.AppOrderType;
import cn.com.leyizhuang.app.core.constant.ReturnOrderType;
import lombok.*;

import java.util.Date;

/**
 * Created by caiyu on 2017/12/6.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ReturnOrderBaseInfo {
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
    private String returnNo;
    /**
     * 退单类型（CANCEL_RETURN(1, "取消退货"), REFUSED_RETURN(2, "拒签退货"), NORMAL_RETURN(3, "正常退货")）
     */
    private ReturnOrderType returnType;
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
     * 退货原因
     */
    private String reasonInfo;
    /**
     * 退货物流方式
     */
    private AppOrderType orderType;
    /**
     * 退货物流状态
     */
    private AppOrderReturnStatus returnStatus;

}

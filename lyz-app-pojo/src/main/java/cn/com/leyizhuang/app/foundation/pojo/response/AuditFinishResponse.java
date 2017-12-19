package cn.com.leyizhuang.app.foundation.pojo.response;

import lombok.*;

/**
 * 配送员已完成单返回类
 * Created by caiyu on 2017/12/18.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AuditFinishResponse {
    /**
     * 出货单号
     */
    private String shipperNumber;
    /**
     * 订单号
     */
    private String orderNumber;
    /**
     * 收货人姓名
     */
    private String receiver;
    /**
     * 收货人电话
     */
    private String receiverPhone;
    /**
     * 导购姓名
     */
    private String sellerName;
    /**
     * 导购电话
     */
    private String sellerPhone;
    /**
     * 收货地址
     */
    private String shippingAddress;
    /**
     * 配送日期时间
     */
    private String deliveryTime;
    /**
     * 代收金额
     */
    private Double collectionAmount;
    /**
     * 审核状态
     */
    private String auditStatus;
}

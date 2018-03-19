package cn.com.leyizhuang.app.foundation.pojo.response;

import cn.com.leyizhuang.common.core.constant.ArrearsAuditStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author GenerationRoad
 * @date 2017/11/24
 */
@Setter
@Getter
@ToString
public class SellerArrearsAuditResponse {
    private Long id;
    //订单编号
    private String orderNumber;
    //会员姓名
    private String customerName;
    //会员电话
    private String customerPhone;
    //配送员姓名
    private String deliveryName;
    //配送员电话
    private String deliveryPhone;
    //申请时间
    private String createTime;
    //订单欠款
    private Double orderMoney;
    //欠款金额
    private Double arrearsMoney;
    //收款方式
    private String paymentMethod;

    /**
     * 代收金额
     */
    private Double agencyMoney;
    /**
     * 实收金额
     */
    private Double realMoney;

    //状态
    private String status;
    public void setStatus(ArrearsAuditStatus arrearsAuditStatus) {
        this.status = arrearsAuditStatus.getValue();
    }
}

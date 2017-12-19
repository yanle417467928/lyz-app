package cn.com.leyizhuang.app.foundation.pojo.response;

import cn.com.leyizhuang.common.core.constant.ArrearsAuditStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author GenerationRoad
 * @date 2017/12/19
 */
@Setter
@Getter
@ToString
public class DeliveryArrearsAuditResponse {
    //代收金额
    private Double agencyMoney;

    //实收金额
    private Double realMoney;

    //收款方式
    private String paymentMethod;

    //状态
    private String status;

    public void setStatus(ArrearsAuditStatus arrearsAuditStatus) {
        this.status = arrearsAuditStatus.getValue();
    }
}

package cn.com.leyizhuang.app.foundation.pojo;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.core.constant.CustomerPreDepositChangeType;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 客户预存款便更明细类
 * @author GenerationRoad
 * @date 2017/11/20
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CusPreDepositLogDO {

    private Long id;
    /**
     * 生成时间
     */
    private LocalDateTime createTime;
    /**
     * 变更金额
     */
    private Double changeMoney;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 使用订单号
     */
    private String orderNumber;
    /**
     * 变更类型
     */
    private CustomerPreDepositChangeType changeType;
    /**
     * 变更类型说明
     */
    private String changeTypeDesc;
    /**
     * 客户id
     */
    private Long cusId;
    /**
     * 操作人员
     */
    private Long operatorId;
    /**
     * 操作人员类型
     */
    private AppIdentityType operatorType;
    /**
     * 操作人员ip
     */
    private String operatorIp;
    /**
     * 变更后预存款
     */
    private Double balance;
    /**
     * 修改原因
     */
    private String detailReason;
    /**
     * 到账时间
     */
    private LocalDateTime transferTime;
    /**
     * 商户订单号
     */
    private String merchantOrderNumber;

    public void setUserIdAndOperatorinfo(Long cusId, Long operatorId, AppIdentityType operatorType, String operatorIp) {
        this.cusId = cusId;
        this.operatorId = operatorId;
        this.operatorType = operatorType;
        this.operatorIp = operatorIp;
    }

    public void setCreateTimeAndChangeMoneyAndType(LocalDateTime createTime, Double changeMoney, CustomerPreDepositChangeType changeType) {
        this.createTime = createTime;
        this.changeMoney = changeMoney;
        this.changeType = changeType;
        this.changeTypeDesc = changeType.getDescription();
    }

    public void setOrderNumberAndRemarksAndDetailReason(String orderNumber, String remarks, String detailReason) {
        this.orderNumber = orderNumber;
        this.remarks = remarks;
        this.detailReason = detailReason;
    }
}

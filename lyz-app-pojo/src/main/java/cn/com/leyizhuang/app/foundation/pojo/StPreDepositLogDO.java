package cn.com.leyizhuang.app.foundation.pojo;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.core.constant.StorePreDepositChangeType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * 门店预存款便更明细类
 * @author GenerationRoad
 * @date 2017/11/21
 */
@Getter
@Setter
@ToString
public class StPreDepositLogDO {

    private Long id;
    /**
     *  生成时间
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
    private StorePreDepositChangeType changeType;
    /**
     * 客户id
     */
    private Long storeId;
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

    public void setUserIdAndOperatorinfo(Long storeId, Long operatorId, AppIdentityType operatorType, String operatorIp) {
        this.storeId = storeId;
        this.operatorId = operatorId;
        this.operatorType = operatorType;
        this.operatorIp = operatorIp;
    }

    public void setCreateTimeAndChangeMoneyAndType(LocalDateTime createTime, Double changeMoney, StorePreDepositChangeType changeType) {
        this.createTime = createTime;
        this.changeMoney = changeMoney;
        this.changeType = changeType;
    }

    public void setOrderNumberAndRemarksAndDetailReason(String orderNumber, String remarks, String detailReason) {
        this.orderNumber = orderNumber;
        this.remarks = remarks;
        this.detailReason = detailReason;
    }
}

package cn.com.leyizhuang.app.foundation.pojo;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.common.core.constant.PreDepositChangeType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @author GenerationRoad
 * @date 2017/11/20
 */
@Getter
@Setter
@ToString
public class CusPreDepositLogDO {

    private Long id;
    //生成时间
    private LocalDateTime createTime;
    //变更金额
    private Double changeMoney;
    //备注
    private String remarks;
    //使用订单号
    private String orderNumber;
    //变更类型
    private PreDepositChangeType type;
    //客户id
    private Long cusId;
    //操作人员
    private Long operatorId;
    //操作人员类型
    private AppIdentityType operatorType;
    //操作人员ip
    private String operatorIp;
    //变更后预存款
    private Double balance;
    //修改原因
    private String detailReason;
    //到账时间
    private LocalDateTime transferTime;
    //商户订单号
    private String userOrderNumber;

    public void setUserIdAndOperatorinfo(Long cusId, Long operatorId, AppIdentityType operatorType, String operatorIp){
        this.cusId = cusId;
        this.operatorId = operatorId;
        this.operatorType = operatorType;
        this.operatorIp = operatorIp;
    }

    public void setCreateTimeAndChangeMoneyAndType(LocalDateTime createTime, Double changeMoney, PreDepositChangeType type){
        this.createTime = createTime;
        this.changeMoney = changeMoney;
        this.type = type;
    }

    public void setOrderNumberAndRemarksAndDetailReason(String orderNumber, String remarks, String detailReason){
        this.orderNumber = orderNumber;
        this.remarks = remarks;
        this.detailReason = detailReason;
    }
}

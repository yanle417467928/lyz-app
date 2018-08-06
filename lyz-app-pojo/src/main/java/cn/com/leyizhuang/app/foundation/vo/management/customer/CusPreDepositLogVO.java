package cn.com.leyizhuang.app.foundation.vo.management.customer;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.core.constant.CustomerPreDepositChangeType;
import cn.com.leyizhuang.common.util.TimeTransformUtils;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 顾客预存款变更明细类
 * @author GenerationRoad
 * @date 2018/1/11
 */
@Getter
@Setter
@ToString
public class CusPreDepositLogVO {
    private Long id;
    /**
     * 生成时间
     */
    private String createTime;
    /**
     * 变更金额
     */
    private String changeMoney;
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
    private String changeType;
    /**
     * 变更后预存款
     */
    private String balance;
    /**
     * 到账时间
     */
    private String transferTime;
    /**
     * 商户订单号
     */
    private String merchantOrderNumber;
    /*门店名称*/
    private String storeName;
    /*真实姓名*/
    private String name;
    /*手机号码*/
    private String mobile;
    /*操作人id*/
    private String operatorId;
    /**
     * 操作人员类型
     */
    private String operatorType;
    /**
     * 操作人员ip
     */
    private String operatorIp;
    /**
     * 修改原因
     */
    private String detailReason;

    private String operatorName;

    public void setChangeType(CustomerPreDepositChangeType changeType){
        this.changeType = changeType.getDescription();
    }
    public void setCreateTime(LocalDateTime createTime){
        this.createTime = TimeTransformUtils.df.format(createTime);
    }

    public void setTransferTime(LocalDateTime transferTime){
        this.transferTime = TimeTransformUtils.df1.format(transferTime);
    }

    public void setOperatorType(AppIdentityType appIdentityType){
        this.operatorType = appIdentityType.getDescription();
    }
}

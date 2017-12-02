package cn.com.leyizhuang.app.foundation.pojo;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.core.constant.StoreCreditMoneyChangeType;
import lombok.*;

import java.util.Date;

/**
 * 门店信用金变更日志
 *
 * @author GenerationRoad
 * @date 2017/11/27
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class StoreCreditMoneyChangeLog {

    private Long id;
    /**
     * 门店id
     */
    private Long storeId;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 可用额度变更金额
     */
    private Double changeAmount;

    /**
     * 可用额度变更后金额
     */
    private Double creditLimitAvailableAfterChange;
    /**
     * 相关单号
     */
    private String referenceNumber;
    /**
     * 变更类型
     */
    private StoreCreditMoneyChangeType changeType;
    /**
     * 变更类型描述
     */
    private String changeTypeDesc;
    /**
     * 操作人员id
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
     * 备注
     */
    private String remark;
}

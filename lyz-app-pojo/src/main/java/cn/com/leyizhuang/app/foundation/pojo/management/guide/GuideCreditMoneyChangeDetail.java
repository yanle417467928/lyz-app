package cn.com.leyizhuang.app.foundation.pojo.management.guide;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.core.constant.EmpCreditMoneyChangeType;
import lombok.*;

import java.util.Date;

/**
 * 导购信用额度变更日志
 *
 * @author Richard
 * Created on 2017-10-19 16:03
 **/
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class GuideCreditMoneyChangeDetail {

    private Long id;
    /**
     * 导购id
     */
    private Long empId;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 临时额度变更金额
     */
    private Double tempCreditLimitChangeAmount;
    /**
     * 临时额度变更后金额
     */
    private Double tempCreditLimitAfterChange;
    /**
     * 可用额度变更金额
     */
    private Double creditLimitAvailableChangeAmount;

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
    private EmpCreditMoneyChangeType changeType;
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

}

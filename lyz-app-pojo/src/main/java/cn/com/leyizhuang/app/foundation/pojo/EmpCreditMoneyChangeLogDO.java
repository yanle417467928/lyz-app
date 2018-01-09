package cn.com.leyizhuang.app.foundation.pojo;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.core.constant.EmpCreditMoneyChangeType;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EmpCreditMoneyChangeLogDO {
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
     * 临时额度改变id
     */
    private Long tempCreditChangeId;
    /**
     * 固定额度改变id
     */
    private Long fixedCreditChangeId;
    /**
     * 可用额度变更id
     */
    private Long availableCreditChangId;
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

    public static final EmpCreditMoneyChangeLogDO transform(EmpCreditMoneyChangeLog log) {
        if (null != log) {
            EmpCreditMoneyChangeLogDO empCreditMoneyChangeLogDO = new EmpCreditMoneyChangeLogDO();
            empCreditMoneyChangeLogDO.setEmpId(log.getEmpId());
            empCreditMoneyChangeLogDO.setCreateTime(log.getCreateTime());
            empCreditMoneyChangeLogDO.setReferenceNumber(log.getReferenceNumber());
            empCreditMoneyChangeLogDO.setChangeTypeDesc(log.getChangeTypeDesc());
            empCreditMoneyChangeLogDO.setChangeType(log.getChangeType());
            empCreditMoneyChangeLogDO.setChangeTypeDesc(log.getChangeTypeDesc());
            empCreditMoneyChangeLogDO.setOperatorId(log.getOperatorId());
            empCreditMoneyChangeLogDO.setOperatorType(log.getOperatorType());
            empCreditMoneyChangeLogDO.setOperatorId(log.getOperatorId());
            return empCreditMoneyChangeLogDO;
        } else {
            return null;
        }
    }

}

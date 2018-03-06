package cn.com.leyizhuang.app.foundation.pojo.response;

import cn.com.leyizhuang.app.core.constant.EmpCreditMoneyChangeLogKind;
import cn.com.leyizhuang.app.core.constant.EmpCreditMoneyChangeType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Jerry.Ren
 * Notes: 导购信用金变动返回对象
 * Created with IntelliJ IDEA.
 * Date: 2017/12/18.
 * Time: 17:52.
 */
@Getter
@Setter
@ToString
public class EmployeeCreditMoneyLogResponse {

    private Long id;
    /**
     * 生成时间
     */
    private String createTime;
    /**
     * 关联单号
     */
    private String referenceNumber;
    /**
     * 代下单顾客姓名
     */
    private String customerName;
    /**
     * 代下单顾客电话
     */
    private String customerPhone;
    /**
     * 变更金额
     */
    private Double changeMoney;
    /**
     * 变更记录枚举
     */
    private EmpCreditMoneyChangeType changeType;
    /**
     * 变更日志类别
     */
    private EmpCreditMoneyChangeLogKind kind;
    /**
     * 变更类型
     */
    private String type;

    public void attributeKindSetByChangeType(EmployeeCreditMoneyLogResponse response) {

        if (EmpCreditMoneyChangeType.ADMIN_RECHARGE.equals(response.getChangeType()) ||
                EmpCreditMoneyChangeType.AVALIABLED_CHANGE_BY_FIXE.equals(response.getChangeType()) ||
                EmpCreditMoneyChangeType.AVALIABLED_CHANGE_BY_TEMP.equals(response.getChangeType()) ||
                EmpCreditMoneyChangeType.TEMPORARY_ADJUSTMENT.equals(response.getChangeType()) ||
                EmpCreditMoneyChangeType.FIXEDAMOUNT_ADJUSTMENT.equals(response.getChangeType()) ||
                EmpCreditMoneyChangeType.TEMPORARY_CLEAR.equals(response.getChangeType())) {
            response.setKind(EmpCreditMoneyChangeLogKind.SYS_ADJUSTMENT);
        } else {
            response.setKind(EmpCreditMoneyChangeLogKind.ORD_ADJUSTMENT);
        }
    }
}

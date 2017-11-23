package cn.com.leyizhuang.app.foundation.pojo.order;

import lombok.*;

import java.util.Date;

/**
 * 装饰公司订单主体信息
 *
 * @author Ricahrd
 * Created on 2017-10-10 10:52
 **/
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FitOrderSubjectInfo {

    /**
     *  装饰公司编码
     */
    private String companyCode;

    /**
     * 下单装饰公司组织全编码
     */
    private String companyStructureCode;

    /**
     * 下单人id
     */
    private Long creatorIdFit;

    /**
     * 下单人姓名
     */
    private String creatorNameFit;

    /**
     * 下单人手机
     */
    private String creatorPhoneFit;

    /**
     * 员工id
     */
    private Long employeeId;

    /**
     * 员工姓名
     */
    private String employeeName;

    /**
     * 经理id
     */
    private Long managerId;

    //经理姓名
    private String managerName;

    //审核人id
    private Long auditorId;

    //审核人姓名
    private String auditorName;

    //审核人电话
    private String auditorPhone;

    //审核时间
    private Date auditingTime;


}

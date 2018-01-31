package cn.com.leyizhuang.app.foundation.pojo.management.decorativeCompany;

import lombok.*;

import java.util.Date;

/**
 * 装饰公司合同
 * Created by caiyu on 2018/1/30.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DecorativeCompanyContract {
    /**
     * 自增主键
     */
    private Long id;
    /**
     * 装饰公司id
     */
    private Long companyId;
    /**
     * 装饰公司名称
     */
    private String companyName;
    /**
     * 联系电话
     */
    private String companyPhone;
    /**
     * 入场费
     */
    private Double admissionFee;
    /**
     * 质保金
     */
    private Double qualityAssuranceMoney;
    /**
     * 合同开始日期
     */
    private Date contractStartDate;
    /**
     * 合同结束日期
     */
    private Date contractEndDate;
    /**
     * 业务经理编号
     */
    private String managerCode;
    /**
     * 业务经理姓名
     */
    private String managerName;
    /**
     * 是否开票
     */
    private Boolean isBilling;
    /**
     * 开票名称
     */
    private String invoiceName;
    /**
     * 开票税号
     */
    private String invoiceNumber;
    /**
     * 单位地址
     */
    private String unitAddress;
    /**
     * 开票银行
     */
    private String bank;
    /**
     * 银行账户号
     */
    private String bankNumber;
    /**
     * 是否现结
     */
    private Boolean isNowKnot;
    /**
     * 非现结天数
     */
    private Integer notNowKnotDays;
    /**
     * 对账日期
     */
    private String checkMoneyDate;
    /**
     * 回款日期
     */
    private String repaymentsDate;
}

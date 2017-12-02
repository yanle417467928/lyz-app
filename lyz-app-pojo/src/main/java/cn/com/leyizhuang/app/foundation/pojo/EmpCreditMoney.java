package cn.com.leyizhuang.app.foundation.pojo;

import lombok.*;

import java.util.Date;

/**
 * 导购信用额度
 *
 * @author Richard
 * Created on 2017-10-19 16:03
 **/
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EmpCreditMoney {

    private Long id;
    /**
     * 员工id
     */
    private Long empId;
    /**
     * 固定信用额度
     */
    private Double creditLimit;
    /**
     * 临时信用额度
     */
    private Double tempCreditLimit;
    /**
     * 剩余信用额度
     */
    private Double creditLimitAvailable;
    /**
     * 是否启用
     */
    private Boolean isActive;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 上次更新时间
     */
    private Date lastUpdateTime;


}

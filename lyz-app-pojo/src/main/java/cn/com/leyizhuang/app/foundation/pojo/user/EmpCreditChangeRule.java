package cn.com.leyizhuang.app.foundation.pojo.user;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 导购余额改变规则
 * Created by 12421 on 2018/6/30.
 */
@Setter
@Getter
@ToString
public class EmpCreditChangeRule {

    private Long empId;

    private String empName;

    private Double changeMoney;
}

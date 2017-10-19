package cn.com.leyizhuang.app.foundation.pojo;

import lombok.*;

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

    private Long guideId;

    private Double creditLimit;

    private Double tempCreditLimit;

    private Double creditLimitAvailable;

    private Boolean isActive;


}

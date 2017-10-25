package cn.com.leyizhuang.app.foundation.pojo;

import cn.com.leyizhuang.common.foundation.pojo.BaseDO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created with IntelliJ IDEA.
 * Created by Jerry.Ren
 * Date: 2017/10/25.
 * Time: 9:48.
 */
@Getter
@Setter
@ToString
public class SellerCreditMoney extends BaseDO {

    //导购固定额度creditLimit
    private Double limit;
    //导购临时额度tempCreditLimit
    private Double temp;
    //导购的可用额度creditLimitAvailable
    private Double available;
}

package cn.com.leyizhuang.app.foundation.pojo;

import lombok.*;

/**
 * Created with IntelliJ IDEA.
 * 后台导购可用额度改变
 *
 * @author liuh
 * Date: 2018/01/04.
 * Time: 10:41.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class EmpTempCreditMoneyChangeLog {
    private Long id;
    //临时额度改变总量
    private Double tempCreditLimitChangeAmount;
    //临时额度改变后余量
    private Double tempCreditLimitAfterChange;
}

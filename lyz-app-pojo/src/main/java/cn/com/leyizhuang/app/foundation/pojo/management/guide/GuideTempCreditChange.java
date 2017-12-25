package cn.com.leyizhuang.app.foundation.pojo.management.guide;

import lombok.*;

import java.math.BigDecimal;
/**
 * Created with IntelliJ IDEA.
 * 后台导购零时额度改变
 *
 * @author liuh
 * Date: 2017/11/23.
 * Time: 10:41.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class GuideTempCreditChange {

    private Long id;
   //零时额度改变总量
    private BigDecimal tempCreditLimitChangeAmount;
   //零时额度改变后余量
    private BigDecimal tempCreditLimitAfterChange;
}

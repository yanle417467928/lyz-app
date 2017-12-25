package cn.com.leyizhuang.app.foundation.pojo.management.guide;

import lombok.*;

import java.math.BigDecimal;
/**
 * Created with IntelliJ IDEA.
 * 后台导购固定额度改变
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
public class GuideFixedCreditChange {

    private Long id;
   //固定额度改变总量
    private BigDecimal fixedCreditLimitChangeAmount;
    //固定额度改变余量
    private BigDecimal fixedCreditLimitAfterChange;
}

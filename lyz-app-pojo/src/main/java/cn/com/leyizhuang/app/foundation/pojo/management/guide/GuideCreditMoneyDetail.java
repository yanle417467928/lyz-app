package cn.com.leyizhuang.app.foundation.pojo.management.guide;

import lombok.*;

import java.math.BigDecimal;
/**
 * Created with IntelliJ IDEA.
 * 后台导购所有额度变更
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
public class GuideCreditMoneyDetail {
    //员工id
    private Long empId;
    //改变之前固定额度
    private BigDecimal originalCreditLimit;
    //改变之后固定额度
    private BigDecimal creditLimit;
    //改变之前临时额度
    private BigDecimal originalTempCreditLimit;
    //改变之后临时额度
    private BigDecimal tempCreditLimit;
    //改变之前可用额度
    private BigDecimal originalCreditLimitAvailable;
    //改变之后可用额度
    private BigDecimal creditLimitAvailable;
}

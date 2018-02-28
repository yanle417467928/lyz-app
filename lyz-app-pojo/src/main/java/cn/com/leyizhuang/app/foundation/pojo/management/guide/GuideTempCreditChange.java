package cn.com.leyizhuang.app.foundation.pojo.management.guide;

import lombok.*;

import java.math.BigDecimal;
/**
 * Created with IntelliJ IDEA.
 * 后台导购临时额度改变
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
   //临时额度改变总量
    private BigDecimal tempCreditLimitChangeAmount;
   //临时额度改变后余量
    private BigDecimal tempCreditLimitAfterChange;
    //变更类型
    private String changeType;
    //变更类型描述
    private String changeTypeDesc;
}

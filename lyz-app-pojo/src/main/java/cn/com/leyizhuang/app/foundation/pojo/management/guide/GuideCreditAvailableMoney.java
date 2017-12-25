package cn.com.leyizhuang.app.foundation.pojo.management.guide;

import lombok.*;

import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * 后台导购可用额度
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
public class GuideCreditAvailableMoney {
    //员工id
    private Long id;
    //可用额度
    private BigDecimal creditLimitAvailable;
}

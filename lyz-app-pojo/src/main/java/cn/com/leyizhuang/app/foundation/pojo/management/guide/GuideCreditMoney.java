package cn.com.leyizhuang.app.foundation.pojo.management.guide;


import lombok.*;

import java.math.BigDecimal;
import java.util.Date;
/**
 * Created with IntelliJ IDEA.
 * 后台导购信用金
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
public class GuideCreditMoney {

    private Long id;
    //员工ID
    private Long empId;
    //固定额度
    private BigDecimal creditLimit;
    //临时额度
    private BigDecimal tempCreditLimit;
    //可用额度
    private BigDecimal creditLimitAvailable;
    //上次修改时间
    private Date lastUpdateTime;
    //上次修改时间
    private Date createTime;
    //是否激活
    private Boolean isActive;
}

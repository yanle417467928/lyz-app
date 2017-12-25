package cn.com.leyizhuang.app.foundation.pojo.management.decorativeCompany;

import lombok.*;

import java.math.BigDecimal;
/**
 * Created with IntelliJ IDEA.
 * 后台装饰公司信用金
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
public class DecorativeCompanyCredit {

    private Long  cid;
   //装饰公司信用金
    private BigDecimal credit;
}

package cn.com.leyizhuang.app.foundation.pojo.management.decorativeCompany;

import lombok.*;

import java.math.BigDecimal;
/**
 * Created with IntelliJ IDEA.
 * 后台装饰公司赞助金
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
public class DecorativeCompanySubvention {

    private Long sid;
    //装饰公司赞助金
    private BigDecimal sponsorship;
}

package cn.com.leyizhuang.app.foundation.pojo.management.decorativeCompany;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

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
    //上次更新时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date sponsorshipLastUpdateTime;
}

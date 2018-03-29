package cn.com.leyizhuang.app.foundation.pojo.management.decorativeCompany;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

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
    //门店id
    private Long storeId;
   //装饰公司信用金
    private BigDecimal credit;
    //签约信用金
    private BigDecimal creditLimit;
    //上次更新时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date creditLastUpdateTime;
}

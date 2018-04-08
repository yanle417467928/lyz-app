package cn.com.leyizhuang.app.foundation.pojo.management.decorativeCompany;

/**
 * Created with IntelliJ IDEA.
 * 后台装饰公司信息
 *
 * @author liuh
 * Date: 2017/11/23.
 * Time: 10:41.
 */

import cn.com.leyizhuang.app.foundation.pojo.management.city.SimpleCityParam;
import cn.com.leyizhuang.app.foundation.vo.management.city.CityDetailVO;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DecorativeCompanyInfo {

    private Long id;

    //装饰公司编码
    private String storeCode;

    //装饰公司Id
    private Long storeId;

    //门店名称
    private String storeName;

    // 是否生效
    private Boolean enable;

    //信用金
    private BigDecimal credit;

    //信用金上次更新时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date creditLastUpdateTime;

    //城市名称
    private String cityName;

    //赞助金
    private BigDecimal sponsorship;

    //赞助金上次更新时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date sponsorshipLastUpdateTime;

    //签约信用金
    private BigDecimal creditLimit;
}

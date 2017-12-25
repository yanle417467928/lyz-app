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

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DecorativeCompanyInfo {

    private Long id;

    //装饰公司编码
    private String storeCode;

    //城市ID
    private SimpleCityParam cityCode;

    //门店名称
    private String storeName;

    // 是否生效
    private Boolean enable;

    //信用金
    private DecorativeCompanyCredit credit;

    //赞助金
    private DecorativeCompanySubvention sponsorship;
}

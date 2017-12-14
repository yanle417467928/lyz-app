package cn.com.leyizhuang.app.foundation.pojo.decorativeCompany;

import cn.com.leyizhuang.app.foundation.vo.CityVO;
import lombok.*;

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

    //城市ID
    private CityVO cityCode;

    //门店名称
    private String storeName;

    // 是否生效
    private Boolean enable;

    //信用金
    private DecorativeCompanyCredit credit;

    //赞助金
    private DecorativeCompanySubvention sponsorship;
}

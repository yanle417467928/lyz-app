package cn.com.leyizhuang.app.foundation.vo.management.guide;

import cn.com.leyizhuang.app.foundation.pojo.management.city.SimpleCityParam;
import cn.com.leyizhuang.app.foundation.pojo.management.guide.GuideCreditMoney;
import cn.com.leyizhuang.app.foundation.pojo.management.store.SimpleStoreParam;
import lombok.*;
/**
 * 导购详情VO
 *
 * @author liuh
 * Created on 2017-11-03 14:14
 **/
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class GuideDetailVO {
    //员工id
    private Long id;
    //登录名
    private String loginName;
    //姓名
    private String name;
    //城市id
    private SimpleCityParam cityId;
    //门店id
    private SimpleStoreParam storeId;
   //导购额度
    private GuideCreditMoney guideCreditMoney;
}

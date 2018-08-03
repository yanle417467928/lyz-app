package cn.com.leyizhuang.app.foundation.pojo.response;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import lombok.*;

/**
 * Created by caiyu on 2018/5/31.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MaCreateOrderPeopleResponse {

    //下单人id
    private Long peopleId;

    //下单人身份类型
    private AppIdentityType identityType;

    //下单人姓名
    private String name;

    //下单人电话
    private String phone;

    //下单人归属门店名
    private String storeName;

    //下单人归属门店code
    private String storeCode;

    //下单人会员等级
    private String rankCode;

}

package cn.com.leyizhuang.app.foundation.pojo;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.common.foundation.pojo.BaseDO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author GenerationRoad
 * @date 2017/9/28
 */
@Setter
@Getter
@ToString
public class DeliveryAddressDO extends BaseDO {

    //收货人姓名
    private String receiver;
    //收货人号码
    private String receiverPhone;
    //收货省份
    private String deliveryProvince;
    //收货城市
    private String deliveryCity;
    //收货县
    private String deliveryCounty;
    //收货街道
    private String deliveryStreet;
    //收货详细地址
    private String detailedAddress;
    //小区名
    private String residenceName;
    //会员ID
    private Long userId;
    //身份类型
    private AppIdentityType identityType;
    //状态
    private Boolean status;
    //是否默认收货地址
    private Boolean isDefault;


}

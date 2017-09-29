package cn.com.leyizhuang.app.foundation.pojo;

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
    private String deliveryName;
    //收货人号码
    private String deliveryPhone;
    //收货城市
    private String deliveryCity;
    //收货县
    private String deliveryCounty;
    //收货街道
    private String DELIVERYSTREET;
    //收货详细地址
    private String detailedAddress;
    //小区名
    private String villageName;
    //会员ID
    private Long customerId;
    //状态
    private Boolean STATUS;


}

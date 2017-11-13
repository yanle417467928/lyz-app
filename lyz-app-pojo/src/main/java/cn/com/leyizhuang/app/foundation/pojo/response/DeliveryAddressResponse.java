package cn.com.leyizhuang.app.foundation.pojo.response;

import cn.com.leyizhuang.app.foundation.pojo.DeliveryAddressDO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/9/28
 */
@Getter
@Setter
@ToString
public class DeliveryAddressResponse {

    //自增主键
    private Long id;
    //收货人姓名
    private String deliveryName;
    //收货人号码
    private String deliveryPhone;
    //收货城市
    private String deliveryCity;
    //收货县
    private String deliveryCounty;
    //收货街道
    private String deliveryStreet;
    //收货详细地址
    private String detailedAddress;
    //小区名
    private String villageName;
    //是否默认收货地址
    private Boolean isDefault;

}

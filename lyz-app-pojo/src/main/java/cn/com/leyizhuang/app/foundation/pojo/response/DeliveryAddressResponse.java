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
//    private Long id;
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

    public static final DeliveryAddressResponse transform(DeliveryAddressDO deliveryAddressDO) {
        if (null != deliveryAddressDO) {
            DeliveryAddressResponse deliveryAddressResponse = new DeliveryAddressResponse();
//            deliveryAddressResponse.setId(deliveryAddressDO.getId());
            deliveryAddressResponse.setDeliveryName(deliveryAddressDO.getDeliveryName());
            deliveryAddressResponse.setDeliveryPhone(deliveryAddressDO.getDeliveryPhone());
            deliveryAddressResponse.setDeliveryCity(deliveryAddressDO.getDeliveryCity());
            deliveryAddressResponse.setDeliveryCounty(deliveryAddressDO.getDeliveryCounty());
            deliveryAddressResponse.setDELIVERYSTREET(deliveryAddressDO.getDELIVERYSTREET());
            deliveryAddressResponse.setDetailedAddress(deliveryAddressDO.getDetailedAddress());
            deliveryAddressResponse.setVillageName(deliveryAddressDO.getVillageName());
            return deliveryAddressResponse;
        } else {
            return null;
        }
    }

    public static final List<DeliveryAddressResponse> transform(List<DeliveryAddressDO> deliveryAddressDOList) {
        List<DeliveryAddressResponse> deliveryAddressResponseList;
        if (null != deliveryAddressDOList && deliveryAddressDOList.size() > 0) {
            deliveryAddressResponseList = new ArrayList<>(deliveryAddressDOList.size());
            deliveryAddressDOList.forEach(DeliveryAddressDO -> deliveryAddressResponseList.add(transform(DeliveryAddressDO)));
        } else {
            deliveryAddressResponseList = new ArrayList<>(0);
        }
        return deliveryAddressResponseList;
    }

}

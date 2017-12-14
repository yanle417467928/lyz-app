package cn.com.leyizhuang.app.foundation.vo;

import cn.com.leyizhuang.app.foundation.pojo.city.CityDeliveryTime;
import lombok.*;

import java.util.Date;
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CityDeliveryTimeVO {

    private Long id;

    private Long cityId;

    private String cityName;

    private String startHour;

    private String startMinute;

    private String endHour;

    private String endMinute;

    private String startTime;

    private String endTime;

    public static final CityDeliveryTime transform(CityDeliveryTimeVO cityDeliveryTimeVO) {
        if (null != cityDeliveryTimeVO) {
            String startTime =cityDeliveryTimeVO.getStartTime();
            String endTime =cityDeliveryTimeVO.getEndTime();
            String[] startTimeAll= startTime.split(":");
            String[] endTimeAll = endTime.split(":");
            CityDeliveryTime cityDeliveryTime = new CityDeliveryTime();
            cityDeliveryTime.setId(cityDeliveryTimeVO.getId());
            cityDeliveryTime.setCityId(cityDeliveryTimeVO.getCityId());
            cityDeliveryTime.setCityName(cityDeliveryTimeVO.getCityName());
            cityDeliveryTime.setStartHour(Integer.parseInt(startTimeAll[0]));
            cityDeliveryTime.setEndHour(Integer.parseInt(endTimeAll[0]));
            cityDeliveryTime.setStartMinute(Integer.parseInt(startTimeAll[1]));
            cityDeliveryTime.setEndMinute(Integer.parseInt(endTimeAll[1]));
            return cityDeliveryTime;
        } else {
            return null;
        }
    }

}

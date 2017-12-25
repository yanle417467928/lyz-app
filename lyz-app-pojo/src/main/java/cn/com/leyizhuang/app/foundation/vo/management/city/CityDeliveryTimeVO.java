package cn.com.leyizhuang.app.foundation.vo.management.city;

import cn.com.leyizhuang.app.foundation.pojo.management.city.CityDeliveryTime;
import lombok.*;
/**
 * 城市配送时间VO
 *
 * @author liuh
 * Created on 2017-11-03 14:14
 **/
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CityDeliveryTimeVO {

    private Long id;
   //城市id
    private Long cityId;
    //城市名称
    private String cityName;
    //开始小时
    private String startHour;
    //开始分
    private String startMinute;
    //结束小时
    private String endHour;
    //结束分
    private String endMinute;
    //开始时间
    private String startTime;
    //结束时间
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

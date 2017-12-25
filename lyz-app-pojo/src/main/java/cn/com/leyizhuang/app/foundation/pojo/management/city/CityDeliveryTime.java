package cn.com.leyizhuang.app.foundation.pojo.management.city;

import cn.com.leyizhuang.app.foundation.vo.management.city.CityDeliveryTimeVO;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


/**
 * 后台城市配送时间
 *
 * @author liuh
 * Created on 2017-11-10 15:20
 **/

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CityDeliveryTime {

    private Long id;
    //城市id
    private Long cityId;
    //城市名称
    private String cityName;
    //开始小时
    private Integer startHour;
    //开始分
    private Integer startMinute;
    //结束小时
    private Integer endHour;
    //结束分
    private Integer endMinute;

    public static final CityDeliveryTimeVO transform(CityDeliveryTime cityDeliveryTime) {
        if (null != cityDeliveryTime) {
            CityDeliveryTimeVO cityDeliveryTimeVO = new CityDeliveryTimeVO();
            cityDeliveryTimeVO.setId(cityDeliveryTime.getId());
            cityDeliveryTimeVO.setCityId(cityDeliveryTime.getCityId());
            cityDeliveryTimeVO.setCityName(cityDeliveryTime.getCityName());
            if(cityDeliveryTime.getStartHour()>=0&&cityDeliveryTime.getStartHour()<10){
                cityDeliveryTimeVO.setStartHour("0"+cityDeliveryTime.getStartHour());
            }else if(null==cityDeliveryTime.getStartHour()){
                cityDeliveryTimeVO.setStartHour("00");
            }else{ cityDeliveryTimeVO.setStartHour(""+cityDeliveryTime.getStartHour());
            }

            if(cityDeliveryTime.getStartMinute()>=0&&cityDeliveryTime.getStartMinute()<10){
                cityDeliveryTimeVO.setStartMinute("0"+cityDeliveryTime.getStartMinute());
            }else if(null==cityDeliveryTime.getStartMinute()){
                cityDeliveryTimeVO.setStartMinute("00");
            }else{ cityDeliveryTimeVO.setStartMinute(""+cityDeliveryTime.getStartMinute());
            }

            if(cityDeliveryTime.getEndHour()>=0&&cityDeliveryTime.getEndHour()<10){
                cityDeliveryTimeVO.setEndHour("0"+cityDeliveryTime.getEndHour());
            }else if(null==cityDeliveryTime.getStartHour()){
                cityDeliveryTimeVO.setEndHour("00");
            }else{ cityDeliveryTimeVO.setEndHour(""+cityDeliveryTime.getEndHour());
            }

            if(cityDeliveryTime.getEndMinute()>=0&&cityDeliveryTime.getEndMinute()<10){
                cityDeliveryTimeVO.setEndMinute("0"+cityDeliveryTime.getEndMinute());
            }else if(null==cityDeliveryTime.getEndMinute()){
                cityDeliveryTimeVO.setEndMinute("00");
            }else{ cityDeliveryTimeVO.setEndMinute(""+cityDeliveryTime.getEndMinute());
            }

            cityDeliveryTimeVO.setStartTime(cityDeliveryTimeVO.getStartHour()+":"+cityDeliveryTimeVO.getStartMinute());
            cityDeliveryTimeVO.setEndTime(cityDeliveryTimeVO.getEndHour()+":"+cityDeliveryTimeVO.getEndMinute());


              return cityDeliveryTimeVO;
        } else {
            return null;
        }
    }

    public static final List<CityDeliveryTimeVO> transform(List<CityDeliveryTime> cityDeliveryTimeList) {
        List<CityDeliveryTimeVO> cityDeliveryTimeVOList;
        if (null != cityDeliveryTimeList && cityDeliveryTimeList.size() > 0) {
            cityDeliveryTimeVOList = new ArrayList<>(cityDeliveryTimeList.size());
            cityDeliveryTimeList.forEach(cityDeliveryTime -> cityDeliveryTimeVOList.add(transform(cityDeliveryTime)));
        } else {
            cityDeliveryTimeVOList = new ArrayList<>(0);
        }
        return cityDeliveryTimeVOList;
    }

}

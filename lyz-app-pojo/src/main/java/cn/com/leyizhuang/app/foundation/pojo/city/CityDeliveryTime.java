package cn.com.leyizhuang.app.foundation.pojo.city;

import lombok.*;

/**
 * 城市配送时间
 *
 * @author Richard
 * Created on 2017-11-10 15:20
 **/

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CityDeliveryTime {

    private Long id;

    private Long cityId;

    private String cityName;

    private Integer startHour;

    private Integer startMinute;

    private Integer endHour;

    private Integer endMinute;
}

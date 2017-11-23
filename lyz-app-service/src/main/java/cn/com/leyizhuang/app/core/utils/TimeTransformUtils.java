package cn.com.leyizhuang.app.core.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * @author GenerationRoad
 * @date 2017/9/21
 */
public class TimeTransformUtils {


    public static Date localDateTimeToDate(LocalDateTime localDateTime) {

        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = localDateTime.atZone(zoneId);
        Date date = Date.from(zdt.toInstant());
        return date;
    }

}

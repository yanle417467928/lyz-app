package cn.com.leyizhuang.app.core.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author GenerationRoad
 * @date 2017/9/21
 */
public class TimeTransformUtils {

    private static DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static Date localDateTimeToDate(LocalDateTime localDateTime){

        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = localDateTime.atZone(zoneId);
        Date date = Date.from(zdt.toInstant());
        return date;
    }


    public static LocalDateTime stringToLocalDateTime(String time){
        if (null != time && time.length() >= 19){
            time = time.substring(0, 19);
            return LocalDateTime.parse(time, df);
        }else{
            return null;
        }
    }


}

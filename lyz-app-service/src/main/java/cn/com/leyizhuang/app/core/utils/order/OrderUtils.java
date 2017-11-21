package cn.com.leyizhuang.app.core.utils.order;

import cn.com.leyizhuang.app.foundation.pojo.city.City;
import cn.com.leyizhuang.app.foundation.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * 订单相关工具类
 *
 * @author Richard
 * Created on 2017-11-16 10:48
 **/
@Component
public class OrderUtils {

    @Autowired
    private static CityService cityService;

    public static String generateOrderNumber(Long cityId){
        if (null != cityId){
            City city = cityService.findById(cityId);
            if (null != city && null != city.getBriefSpell()){
                String orderNumber = city.getBriefSpell()+"_XN";
                Calendar calendar = Calendar.getInstance();
                Date date = calendar.getTime();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
                String timeStamp = sdf.format(date);
                orderNumber+=timeStamp;
                Random random = new Random();
                String randomNumber = random.nextInt(900000) + 100000+"";
                orderNumber+=randomNumber;
                System.out.println(orderNumber);
                return orderNumber;
            }else{
                return null;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        String orderNumber = generateOrderNumber(1L);
        System.out.println(orderNumber);
    }
}

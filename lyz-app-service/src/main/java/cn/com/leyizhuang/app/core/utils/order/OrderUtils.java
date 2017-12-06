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
 *         Created on 2017-11-16 10:48
 **/
@Component
public class OrderUtils {

    private static CityService cityService;


    @Autowired
    public OrderUtils(CityService cityService) {
        OrderUtils.cityService = cityService;
    }

    public static CityService getCityService() {
        return cityService;
    }

    @Autowired
    public static void setCityService(CityService cityService) {
        OrderUtils.cityService = cityService;
    }

    public static String generateOrderNumber(Long cityId) {
        if (null != cityId) {
            City city = cityService.findById(cityId);
            if (null != city && null != city.getBriefSpell()) {
                String orderNumber = city.getBriefSpell() + "_XN";
                Calendar calendar = Calendar.getInstance();
                Date date = calendar.getTime();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
                String timeStamp = sdf.format(date);
                orderNumber += timeStamp;
                Random random = new Random();
                String randomNumber = random.nextInt(900000) + 100000 + "";
                orderNumber += randomNumber;
                System.out.println(orderNumber);
                return orderNumber;
            } else {
                return null;
            }
        }
        return null;
    }

    /**
     * @param
     * @return
     * @throws
     * @title 生成充值单号
     * @descripe
     * @author GenerationRoad
     * @date 2017/11/22
     */
    public static String generateRechargeNumber(Long cityId) {
        if (null != cityId) {
            City city = cityService.findById(cityId);
            StringBuilder orderNumberTemp = new StringBuilder();

            if (null != city && null != city.getBriefSpell()) {
                String orderNumber = city.getBriefSpell();
                orderNumberTemp.append(orderNumber);
            } else {
                orderNumberTemp.append("MR");
            }
            orderNumberTemp.append("_CZ");
            Calendar calendar = Calendar.getInstance();
            Date date = calendar.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            String timeStamp = sdf.format(date);
            orderNumberTemp.append(timeStamp);
            Random random = new Random();
            orderNumberTemp.append(random.nextInt(900000) + 100000);
            return orderNumberTemp.toString();
        }
        return null;
    }

    /**
     * @param
     * @return
     * @throws
     * @title 生成退货单号
     * @descripe
     * @author GenerationRoad
     * @date 2017/11/22
     */
    public static String getReturnNumber() {
        StringBuilder orderReturnNumber = new StringBuilder();
        orderReturnNumber.append("T");
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String timeStamp = sdf.format(date);
        orderReturnNumber.append(timeStamp);
        return orderReturnNumber.toString();
    }

    public static void main(String[] args) {
        String orderNumber = OrderUtils.generateOrderNumber(1L);
        System.out.println(orderNumber);
    }
}

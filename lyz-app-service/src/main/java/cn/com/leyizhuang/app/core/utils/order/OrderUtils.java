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


    /**
     * 生成分单号
     *
     * @param companyFlag     分公司
     * @param mainOrderNumber 主单号
     * @return 分单号
     */
    public static String generateSeparateOrderNumber(String companyFlag, String mainOrderNumber) {
        if (null != companyFlag && null != mainOrderNumber) {
            return mainOrderNumber.replace("XN", companyFlag);
        }
        return null;
    }

    /**
     * 生成分退单号
     *
     * @param companyFlag      分公司
     * @param mainReturnNumber 主单号
     * @return 分单号
     */
    public static String generateSeparateReturnOrderNumber(String companyFlag, String mainReturnNumber) {
        if (null != companyFlag && null != mainReturnNumber) {
            return mainReturnNumber.substring(0, 1) + "_" + companyFlag + mainReturnNumber.substring(1);
        }
        return null;
    }

    /**
     * 生成订单号
     *
     * @param cityId 城市id
     * @return 订单号
     */
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
                String randomNumber = random.nextInt(900) + 100 + "";
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
     * 生成收款单号
     *
     * @param cityId 城市id
     * @return 收款单号
     */
    public static String generateReceiptNumber(Long cityId) {
        if (null != cityId) {
            City city = cityService.findById(cityId);
            if (null != city && null != city.getBriefSpell()) {
                String receiptNumber = city.getBriefSpell() + "_RC";
                Calendar calendar = Calendar.getInstance();
                Date date = calendar.getTime();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                String timeStamp = sdf.format(date);
                receiptNumber += timeStamp;
                Random random = new Random();
                String randomNumber = random.nextInt(900) + 100 + "";
                receiptNumber += randomNumber;
                System.out.println(receiptNumber);
                return receiptNumber;
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
     * 生成提现单号
     *
     * @param cityId 城市id
     * @return 提现单号
     */
    public static String generateWithdrawNumber(Long cityId) {
        if (null != cityId) {
            City city = cityService.findById(cityId);
            StringBuilder withNumberTemp = new StringBuilder();

            if (null != city && null != city.getBriefSpell()) {
                String cityBriefNo = city.getBriefSpell();
                withNumberTemp.append(cityBriefNo);
            } else {
                return null;
            }
            withNumberTemp.append("_TX");
            Calendar calendar = Calendar.getInstance();
            Date date = calendar.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            String timeStamp = sdf.format(date);
            withNumberTemp.append(timeStamp);
            Random random = new Random();
            withNumberTemp.append(random.nextInt(900000) + 100000);
            return withNumberTemp.toString();
        }
        return null;
    }

    /**
     * 生成退单号
     *
     * @return 退单号
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

    public static Double replaceNullWithZero(Double d) {
        if (null == d) {
            return 0D;
        }
        return d;
    }


    /**
     * 生成退款单据号
     *
     * @return 退款单据号
     */
    public static String getRefundNumber() {
        StringBuilder orderRefundNumber = new StringBuilder();
        orderRefundNumber.append("TK_");
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String timeStamp = sdf.format(date);
        orderRefundNumber.append(timeStamp);
        return orderRefundNumber.toString();
    }


    /**
     * @title   生成拍照单号
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2018/3/19
     */
    public static String generatePhotoOrderNumber(Long cityId) {
        if (null != cityId) {
            City city = cityService.findById(cityId);
            StringBuilder orderNumberTemp = new StringBuilder();
            if (null != city && null != city.getBriefSpell()) {
                String orderNumber = city.getBriefSpell();
                orderNumberTemp.append(orderNumber);
            } else {
                orderNumberTemp.append("MR");
            }
            orderNumberTemp.append("_PZ");
            Calendar calendar = Calendar.getInstance();
            Date date = calendar.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            String timeStamp = sdf.format(date);
            orderNumberTemp.append(timeStamp);
            Random random = new Random();
            orderNumberTemp.append(random.nextInt(900) + 100);
            return orderNumberTemp.toString();
        }
        return null;
    }


    public static void main(String[] args) {
        String orderNumber = OrderUtils.generateOrderNumber(1L);
        System.out.println(orderNumber);
    }

    /**
     * @title   生成装饰公司信用金账单单号
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2018/3/19
     */
    public static String generateCreditBillingNo(Long cityId) {
        if (null != cityId) {
            City city = cityService.findById(cityId);
            StringBuilder orderNumberTemp = new StringBuilder();
            if (null != city && null != city.getBriefSpell()) {
                String orderNumber = city.getBriefSpell();
                orderNumberTemp.append(orderNumber);
            } else {
                orderNumberTemp.append("MR");
            }
            orderNumberTemp.append("_DC");
            Calendar calendar = Calendar.getInstance();
            Date date = calendar.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            String timeStamp = sdf.format(date);
            orderNumberTemp.append(timeStamp);
            Random random = new Random();
            orderNumberTemp.append(random.nextInt(900) + 100);
            return orderNumberTemp.toString();
        }
        return null;
    }
}

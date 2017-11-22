package cn.com.leyizhuang.app.web.controller.city;

import cn.com.leyizhuang.app.core.constant.ApplicationConstant;
import cn.com.leyizhuang.app.core.utils.DateUtil;
import cn.com.leyizhuang.app.foundation.pojo.city.City;
import cn.com.leyizhuang.app.foundation.pojo.city.CityDeliveryTime;
import cn.com.leyizhuang.app.foundation.pojo.response.CityDeliveryTimeResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.CityListResponse;
import cn.com.leyizhuang.app.foundation.service.GoodsService;
import cn.com.leyizhuang.app.foundation.service.impl.CityServiceImpl;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;

/**
 * 城市控制器
 *
 * @author Richard
 * Created on 2017-09-21 14:22
 **/
@RestController
@RequestMapping(value = "/app/city")
public class CityController {

    private static final Logger logger = LoggerFactory.getLogger(CityController.class);

    @Resource
    private CityServiceImpl cityService;

    @Resource
    private GoodsService goodsService;


    /**
     * 获取城市列表
     *
     * @return ResultDTO
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ResultDTO getCityList() {
        logger.info("customerLogin CALLED,获取城市列表，入参:");

        ResultDTO resultDTO;
        List<City> cityList = cityService.findAll();
        List<CityListResponse> responseList = new ArrayList<>();
        for (City c : cityList) {
            CityListResponse response = new CityListResponse();
            response.setCityId(c.getCityId());
            response.setCityName(c.getName());
            responseList.add(response);
        }
        if (responseList.size() > 0) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, responseList);
            logger.info("customerLogin OUT,城市列表获取成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } else {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
            logger.info("customerLogin OUT,城市列表获取成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }

    }


    @RequestMapping(value = "/get/deliveryTime", method = RequestMethod.POST)
    public ResultDTO<Object> getCityDeliveryTime(Long cityId) {
        logger.info("getCityDeliveryTime CALLED,获取城市配送时间列表，入参 cityId:{}", cityId);

        ResultDTO<Object> resultDTO;
        if (null == cityId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "城市ID不允许为空", null);
            logger.warn("getCityDeliveryTime OUT,获取城市配送时间列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            List<CityDeliveryTime> deliveryTimeList = cityService.findCityDeliveryTimeByCityId(cityId);

            List<String> futureDays = DateUtil.getFutureDays(6);
            List<CityDeliveryTimeResponse> responseList = new ArrayList<>();

            CityDeliveryTimeResponse responseDay1 = new CityDeliveryTimeResponse();
            List<CityDeliveryTime> day1DeliveryTime = new ArrayList<>();

            responseDay1.setDay(DateUtil.getFutureDate(0));
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            int hour = calendar.get(Calendar.HOUR_OF_DAY);

            for (int i = 0; i < deliveryTimeList.size(); i++) {
                if (i == 0) {
                    if (hour < deliveryTimeList.get(i).getStartHour()) {
                        deliveryTimeList.remove(0);
                        Collections.copy(day1DeliveryTime, deliveryTimeList);
                        break;
                    }
                } else {
                    if (deliveryTimeList.get(i).getStartHour() > hour) {
                        day1DeliveryTime.add(deliveryTimeList.get(i));
                    }
                }
            }
            responseDay1.setDeliveryTime(transformDeliveryTimeToString(day1DeliveryTime));
            responseList.add(responseDay1);

            for (int i = 0; i < 6; i++) {
                CityDeliveryTimeResponse response = new CityDeliveryTimeResponse();
                response.setDay(futureDays.get(i));
                response.setDeliveryTime(transformDeliveryTimeToString(deliveryTimeList));
                responseList.add(response);
            }
            resultDTO = new ResultDTO<Object>(CommonGlobal.COMMON_CODE_SUCCESS, null, responseList);
            logger.info("getCityDeliveryTime CALLED,获取城市配送时间列表成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知异常,获取城市配送时间列表失败", null);
            logger.warn("customerLogin EXCEPTION,获取城市配送时间列表失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }

    }


    /**
     * 获取下单时可用自提门店信息
     *
     * @param userId       用户id
     * @param identityType 身份类型
     * @param goodsIds     商品id
     * @return 可选自提门店信息
     */
    @RequestMapping(value = "/get/selfTakeStore/available", method = RequestMethod.POST)
    public ResultDTO<Object> getSelfTakeStoreAvailable(Long userId, Integer identityType, @RequestParam(value = "goodsIds",
            required = false) Long[] goodsIds) {
        logger.info("getSelfTakeStoreAvailable CALLED,获取可选自提门店信息，入参 userId:{},identityType:{},goodsIds:{}",
                userId, identityType, goodsIds);

        ResultDTO<Object> resultDTO;
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户ID不允许为空", null);
            logger.warn("getSelfTakeStoreAvailable OUT,获取可选自提门店信息失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "身份类型不允许为空", null);
            logger.warn("getSelfTakeStoreAvailable OUT,获取可选自提门店信息失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == goodsIds) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "商品ID不允许为空", null);
            logger.warn("getSelfTakeStoreAvailable OUT,获取可选自提门店信息失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } else if (goodsIds.length == 0) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "商品ID不允许为空", null);
            logger.warn("getSelfTakeStoreAvailable OUT,获取可选自提门店信息失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            //商品id数组转化成list
            List<Long> goodsIdList = Arrays.asList(goodsIds);
            String[] forbiddenSelfTakeCompany = ApplicationConstant.FORBIDDEN_SELFT_TAKE_COMPANY_FLAG.split("|");
            List<String> forbiddenSelfTakeCompanyList = Arrays.asList(forbiddenSelfTakeCompany);
            List<String> companyFlag = goodsService.findCompanyFlagListById(goodsIdList);
            if (null != companyFlag && !companyFlag.isEmpty()){
               for(String flag:companyFlag){
                   if (forbiddenSelfTakeCompanyList.contains(flag)){
                       resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "辅料商品无法门店自提!", null);
                       logger.info("getCityDeliveryTime CALLED,获取可选自提门店信息失败，出参 resultDTO:{}", resultDTO);
                       return resultDTO;
                   }
               }


            }else {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "未查询到产品所属公司信息", null);
                logger.info("getCityDeliveryTime CALLED,获取可选自提门店信息失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
            logger.info("getCityDeliveryTime CALLED,获取可选自提门店信息失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知异常,获取可选自提门店信息失败",
                    null);
            logger.warn("getCityDeliveryTime EXCEPTION,获取可选自提门店信息失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }

    }


    public List<String> transformDeliveryTimeToString(List<CityDeliveryTime> deliveryTimeList) {
        List<String> resultList = new ArrayList<>();
        for (CityDeliveryTime deliveryTime : deliveryTimeList) {
            String s = deliveryTime.getStartHour() + ":" + deliveryTime.getStartMinute() + "-" +
                    deliveryTime.getEndHour() + ":" + deliveryTime.getEndMinute();
            resultList.add(s);
        }
        return resultList;
    }


}

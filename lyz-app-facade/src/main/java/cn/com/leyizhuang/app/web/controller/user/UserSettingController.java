package cn.com.leyizhuang.app.web.controller.user;

import cn.com.leyizhuang.app.foundation.pojo.*;
import cn.com.leyizhuang.app.foundation.pojo.response.DeliveryAddressResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.UserInformationResponse;
import cn.com.leyizhuang.app.foundation.service.*;
import cn.com.leyizhuang.app.web.controller.employee.EmployeeController;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Created by Jerry.Ren
 * Date: 2017/9/28.
 * Time: 11:57.
 */
@RestController
@RequestMapping(value = "/app/user/setting")
public class UserSettingController {

    private static final Logger logger = LoggerFactory.getLogger(UserSettingController.class);

    @Resource
    private IAppCustomerService customerService;

    @Resource
    private IAppEmployeeService employeeService;

    @Resource
    private IAppStoreService storeService;

    @Resource
    private ICityService cityService;

    @Autowired
    private DeliveryAddressService deliveryAddressServiceImpl;

    /**
     * 获取个人信息
     * @param userId 用户Id
     * @param type 用户类型
     * @return
     */
    @PostMapping(value = "/get/information",produces="application/json;charset=UTF-8")
    public ResultDTO<UserInformationResponse> personalInformationGet(Long userId, Integer type){

        logger.info("personalInformationGet CALLED,获取个人信息，入参 userId {},type{}", userId, type);

        ResultDTO<UserInformationResponse> resultDTO;
        if (userId == null){
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "userId不能为空！", null);
            logger.info("personalInformationGet OUT,获取个人信息失败，出参 resultDTO:{}",resultDTO);
            return resultDTO;
        }

        if (null == type){
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空",
                    null);
            logger.info("personalInformationGet OUT,获取个人信息失败，出参 resultDTO:{}",resultDTO);
            return resultDTO;
        }
        if(type == 6){
            AppCustomer appCustomer =  customerService.findById(userId);
            if (appCustomer==null){
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户不存在！", null);
                logger.info("personalInformationGet OUT,获取个人信息失败，出参 resultDTO:{}",resultDTO);
                return resultDTO;
            }
            UserInformationResponse informationResponse = transform(appCustomer);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "获取个人信息成功！", informationResponse);
            logger.info("personalInformationGet OUT,获取个人信息成功，出参 resultDTO:{}",resultDTO);
            return resultDTO;
        }

        AppEmployee appEmployee =  employeeService.findById(userId);
        if (appEmployee==null){
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户不存在！", null);
            logger.info("personalInformationGet OUT,获取个人信息失败，出参 resultDTO:{}",resultDTO);
            return resultDTO;
        }
        UserInformationResponse informationResponse = transform(appEmployee);
        resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "获取个人信息成功！", informationResponse);
        logger.info("personalInformationGet OUT,获取个人信息成功，出参 resultDTO:{}",resultDTO);
        return resultDTO;

    }

    @PostMapping(value = "/get/deliveryAddress",produces="application/json;charset=UTF-8")
    public ResultDTO<List> getDeliveryAddress(Long userId){
        logger.info("getDeliveryAddress CALLED,获取收货地址，入参 userId {},type{}", userId);

        ResultDTO<List> resultDTO;
        if (userId == null){
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "userId不能为空！", null);
            logger.info("getDeliveryAddress OUT,获取收货地址，出参 resultDTO:{}",resultDTO);
            return resultDTO;
        }
        List<DeliveryAddressDO> deliveryAddressDOList = this.deliveryAddressServiceImpl.queryList(userId);
        List<DeliveryAddressResponse> deliveryAddressResponseList = DeliveryAddressResponse.transform(deliveryAddressDOList);

        resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "获取收货地址！", deliveryAddressResponseList);
        logger.info("getDeliveryAddress OUT,获取收货地址，出参 resultDTO:{}",resultDTO);
        return resultDTO;
    }

    private UserInformationResponse transform(AppEmployee appEmployee){
        UserInformationResponse informationResponse = new UserInformationResponse();
        informationResponse.setPicUrl(appEmployee.getPicUrl());
        informationResponse.setName(appEmployee.getName());
        informationResponse.setSex(appEmployee.getSex().getValue());
        informationResponse.setMobile(appEmployee.getMobile());
        informationResponse.setBirthday(appEmployee.getBirthday());

        City city = cityService.findById(appEmployee.getCityId());
        if(city!=null){
            informationResponse.setCityName(city.getTitle());
        }
        AppStore appStore = storeService.findById(appEmployee.getStoreId());
        if (appStore != null) {
            informationResponse.setStoreName(appStore.getStoreName());
        }
        return informationResponse;
    }

    private UserInformationResponse transform(AppCustomer appCustomer){
        UserInformationResponse informationResponse = new UserInformationResponse();
        informationResponse.setPicUrl(appCustomer.getPicUrl());
        informationResponse.setName(appCustomer.getNickName());
        informationResponse.setSex(appCustomer.getSex().getValue());
        informationResponse.setMobile(appCustomer.getMobile());
        informationResponse.setBirthday(appCustomer.getBirthday());

        City city = cityService.findById(appCustomer.getCityId());
        if(city != null){
            informationResponse.setCityName(city.getTitle());
        }
        AppStore appStore = storeService.findById(appCustomer.getStoreId());
        if (appStore != null) {
            informationResponse.setStoreName(appStore.getStoreName());
        }
        AppEmployee guide = employeeService.findByUserId(appCustomer.getId());
        if (guide != null) {
            informationResponse.setGuideName(guide.getName());
        }
        return informationResponse;
    }
}

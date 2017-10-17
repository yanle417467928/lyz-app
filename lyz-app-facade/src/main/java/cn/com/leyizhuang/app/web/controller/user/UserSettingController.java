package cn.com.leyizhuang.app.web.controller.user;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.core.constant.FunctionalFeedbackStatusEnum;
import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.pojo.*;
import cn.com.leyizhuang.app.foundation.pojo.request.DeliveryAddressRequest;
import cn.com.leyizhuang.app.foundation.pojo.response.DeliveryAddressResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.UserInformationResponse;
import cn.com.leyizhuang.app.foundation.service.*;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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

    @Resource
    private DeliveryAddressService deliveryAddressServiceImpl;

    @Autowired
    private FunctionalFeedbackService functionalFeedbackServiceImpl;

    /**
     * 获取个人信息
     *
     * @param userId       用户Id
     * @param identityType 用户类型
     * @return
     */
    @PostMapping(value = "/get/information", produces = "application/json;charset=UTF-8")
    public ResultDTO<UserInformationResponse> personalInformationGet(Long userId, Integer identityType) {

        logger.info("personalInformationGet CALLED,获取个人信息，入参 userId {},identityType{}", userId, identityType);

        ResultDTO<UserInformationResponse> resultDTO;
        if (userId == null) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "userId不能为空！", null);
            logger.info("personalInformationGet OUT,获取个人信息失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }

        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空",
                    null);
            logger.info("personalInformationGet OUT,获取个人信息失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (identityType == 6) {
            AppCustomer appCustomer = customerService.findById(userId);
            if (appCustomer == null) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户不存在！", null);
                logger.info("personalInformationGet OUT,获取个人信息失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            UserInformationResponse informationResponse = transform(appCustomer);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, informationResponse);
            logger.info("personalInformationGet OUT,获取个人信息成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }

        AppEmployee appEmployee = employeeService.findById(userId);
        if (appEmployee == null) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户不存在！", null);
            logger.info("personalInformationGet OUT,获取个人信息失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        UserInformationResponse informationResponse = transform(appEmployee);
        resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, informationResponse);
        logger.info("personalInformationGet OUT,获取个人信息成功，出参 resultDTO:{}", resultDTO);
        return resultDTO;

    }

    /**
     * @param userId
     * @return
     * @throws
     * @title 获取收货地址
     * @descripe
     * @author GenerationRoad
     * @date 2017/9/29
     */
    @PostMapping(value = "/deliveryAddress/list", produces = "application/json;charset=UTF-8")
    public ResultDTO<List> getDeliveryAddress(Long userId,Integer identityType) {
        logger.info("getDeliveryAddress CALLED,获取收货地址，入参 userId {},identityType", userId,identityType);

        ResultDTO<List> resultDTO;
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "userId不能为空！", null);
            logger.info("getDeliveryAddress OUT,获取收货地址失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "identityType不能为空！", null);
            logger.info("getDeliveryAddress OUT,获取收货地址失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        List<DeliveryAddressResponse> deliveryAddressResponseList = this.deliveryAddressServiceImpl.queryListByUserIdAndStatusIsTrue(userId,AppIdentityType.getAppUserTypeByValue(identityType));

        resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, deliveryAddressResponseList);
        logger.info("getDeliveryAddress OUT,获取收货地址成功，出参 resultDTO:{}", resultDTO);
        return resultDTO;
    }

    /**
     * @param userId
     * @param identityType
     * @param deliveryAddress
     * @return
     * @throws
     * @title 顾客新增收货地址
     * @descripe
     * @author GenerationRoad
     * @date 2017/10/7
     */
    @PostMapping(value = "/deliveryAddress/add", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> addDeliveryAddress(Long userId, Integer identityType, DeliveryAddressRequest deliveryAddress) {
        logger.info("addDeliveryAddress CALLED,顾客新增收货地址，入参 userId:{} identityType:{} deliveryAddress:{}", userId, identityType, deliveryAddress);
        ResultDTO<Object> resultDTO;
        try {
            if (null == userId) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "userId不能为空！", null);
                logger.info("addDeliveryAddress OUT,顾客新增收货地址失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == identityType || 6 != identityType) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型错误！",
                        null);
                logger.info("addDeliveryAddress OUT,顾客新增收货地址失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == deliveryAddress) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "收货人地址信息不能为空！", null);
                logger.info("addDeliveryAddress OUT,顾客新增收货地址失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == deliveryAddress.getDeliveryName()) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "收货人姓名不能为空！", null);
                logger.info("addDeliveryAddress OUT,顾客新增收货地址失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == deliveryAddress.getDeliveryPhone()) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "收货人号码不能为空！", null);
                logger.info("addDeliveryAddress OUT,顾客新增收货地址失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == deliveryAddress.getDeliveryCity()) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "收货城市不能为空！", null);
                logger.info("addDeliveryAddress OUT,顾客新增收货地址失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == deliveryAddress.getDeliveryCounty()) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "收货县不能为空！", null);
                logger.info("addDeliveryAddress OUT,顾客新增收货地址失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == deliveryAddress.getDeliveryStreet()) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "收货街道不能为空！", null);
                logger.info("addDeliveryAddress OUT,顾客新增收货地址失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == deliveryAddress.getDetailedAddress()) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "收货详细地址不能为空！", null);
                logger.info("addDeliveryAddress OUT,顾客新增收货地址失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            this.deliveryAddressServiceImpl.addDeliveryAddress(userId, deliveryAddress);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
            logger.info("addDeliveryAddress OUT,顾客新增收货地址成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知异常,注册失败", null);
            logger.warn("addDeliveryAddress EXCEPTION,顾客新增收货地址失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    /**
     * @param userId
     * @param identityType
     * @param deliveryAddress
     * @return
     * @throws
     * @title 顾客编辑收货地址
     * @descripe
     * @author GenerationRoad
     * @date 2017/10/7
     */
    @PostMapping(value = "/deliveryAddress/edit", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> modifyDeliveryAddress(Long userId, Integer identityType, DeliveryAddressRequest deliveryAddress) {
        logger.info("addDeliveryAddress CALLED,顾客编辑收货地址，入参 userId:{} identityType:{} deliveryAddress:{}", userId, identityType, deliveryAddress);
        ResultDTO<Object> resultDTO;
        try {
            if (null == userId) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "userId不能为空！", null);
                logger.info("modifyDeliveryAddress OUT,顾客编辑收货地址失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == identityType || 6 != identityType) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型错误！",
                        null);
                logger.info("modifyDeliveryAddress OUT,顾客编辑收货地址失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == deliveryAddress) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "收货人地址信息不能为空！", null);
                logger.info("modifyDeliveryAddress OUT,顾客编辑收货地址失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == deliveryAddress.getId()) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "编辑收货地址信息错误！", null);
                logger.info("modifyDeliveryAddress OUT,顾客编辑收货地址失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == deliveryAddress.getDeliveryName()) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "收货人姓名不能为空！", null);
                logger.info("modifyDeliveryAddress OUT,顾客编辑收货地址失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == deliveryAddress.getDeliveryPhone()) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "收货人号码不能为空！", null);
                logger.info("modifyDeliveryAddress OUT,顾客编辑收货地址失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == deliveryAddress.getDeliveryCity()) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "收货城市不能为空！", null);
                logger.info("modifyDeliveryAddress OUT,顾客编辑收货地址失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == deliveryAddress.getDeliveryCounty()) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "收货县不能为空！", null);
                logger.info("modifyDeliveryAddress OUT,顾客编辑收货地址失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == deliveryAddress.getDeliveryStreet()) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "收货街道不能为空！", null);
                logger.info("modifyDeliveryAddress OUT,顾客编辑收货地址失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == deliveryAddress.getDetailedAddress()) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "收货详细地址不能为空！", null);
                logger.info("modifyDeliveryAddress OUT,顾客编辑收货地址失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            this.deliveryAddressServiceImpl.modifyDeliveryAddress(userId, deliveryAddress);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
            logger.info("modifyDeliveryAddress OUT,顾客编辑收货地址成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知异常,注册失败", null);
            logger.warn("modifyDeliveryAddress EXCEPTION,顾客编辑收货地址失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    @PostMapping(value = "/deliveryAddress/delete", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> deleteDeliveryAddress(Long userId, Integer identityType, Long deliveryAddressId) {
        logger.info("deleteDeliveryAddress CALLED,顾客删除收货地址，入参 userId:{} identityType:{} deliveryAddressId:{}", userId, identityType, deliveryAddressId);

        ResultDTO<Object> resultDTO;
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "userId不能为空！", null);
            logger.info("deleteDeliveryAddress OUT,顾客删除收货地址失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == deliveryAddressId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "需要删除的收货地址ID不能为空！", null);
            logger.info("deleteDeliveryAddress OUT,顾客删除收货地址失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        this.deliveryAddressServiceImpl.deleteDeliveryAddress(deliveryAddressId);
        resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
        logger.info("deleteDeliveryAddress OUT,顾客删除收货地址成功，出参 resultDTO:{}", resultDTO);
        return resultDTO;
    }

    /**
     * 用户修改手机号码
     *
     * @param userId
     * @param identityType
     * @param mobile
     * @return
     */
    @PostMapping(value = "/set/information/mobile", produces = "application/json;charset=UTF-8")
    public ResultDTO setInformationOfMobile(Long userId, Integer identityType, String mobile) {

        logger.info("setInformationOfMobile CALLED,用户修改手机号码，入参 userId {},mobile {},identityType{}", userId, mobile, identityType);

        ResultDTO resultDTO;
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空", null);
            logger.info("setInformationOfMobile OUT,用户修改手机号码失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (StringUtils.isBlank(mobile)) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户电话不能为空", null);
            logger.info("setInformationOfMobile OUT,用户修改手机号码失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空", null);
            logger.info("setInformationOfMobile OUT,用户修改手机号码失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (identityType == 6) {

            customerService.modifyMobileByCustomerId(userId, mobile);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
            logger.info("setInformationOfMobile OUT,用户修改手机号码成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }

        employeeService.modifyMobileByEmployeeId(userId, mobile);
        resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
        logger.info("setInformationOfMobile OUT,用户修改手机号码成功，出参 resultDTO:{}", resultDTO);
        return resultDTO;
    }

    /**
     * @title  功能反馈
     * @descripe 功能反馈
     * @param userId
     * @return resultDTO
     * @throws Exception
     * @author GenerationRoad
     * @date 2017/10/10
     */
    @PostMapping(value = "/functionalFeedback/add", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> addFunctionalFeedback(Long userId, Integer identityType, @RequestParam("myfiles") MultipartFile[] files,
                                                   String type, String content, String phone) {
        logger.info("addFunctionalFeedback CALLED,功能反馈，入参 userId:{} identityType:{} files:{}," +
                "type:{}, content:{}, phone:{}", userId, identityType, files, type, content, phone);
        ResultDTO<Object> resultDTO;
        try {
            if (null == userId) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "userId不能为空！", null);
                logger.info("addFunctionalFeedback OUT,功能反馈失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == identityType) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型错误！",
                        null);
                logger.info("addFunctionalFeedback OUT,功能反馈失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == type) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "反馈类型不能为空！", null);
                logger.info("addFunctionalFeedback OUT,功能反馈失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == content) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "反馈内容不能为空！", null);
                logger.info("addFunctionalFeedback OUT,功能反馈失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == phone) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "联系电话不能为空！", null);
                logger.info("addFunctionalFeedback OUT,功能反馈失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }

            //上传图片

            FunctionalFeedbackDO functionalFeedbackDO = new FunctionalFeedbackDO();
            functionalFeedbackDO.setType(type);
            functionalFeedbackDO.setContent(content);
            functionalFeedbackDO.setPhone(phone);
            functionalFeedbackDO.setPictureUrl("");
            functionalFeedbackDO.setStatus(FunctionalFeedbackStatusEnum.NOT_CHECKED);
            functionalFeedbackDO.setUserId(userId);
            functionalFeedbackDO.setUserType(AppIdentityType.getAppUserTypeByValue(identityType));
            this.functionalFeedbackServiceImpl.save(functionalFeedbackDO);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
            logger.info("addFunctionalFeedback OUT,功能反馈成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知异常,注册失败", null);
            logger.warn("addFunctionalFeedback EXCEPTION,功能反馈失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    private UserInformationResponse transform(AppEmployee appEmployee) {
        UserInformationResponse informationResponse = new UserInformationResponse();
        informationResponse.setPicUrl(appEmployee.getPicUrl());
        informationResponse.setName(appEmployee.getName());
        informationResponse.setSex(appEmployee.getSex().getValue());
        informationResponse.setMobile(appEmployee.getMobile());
        informationResponse.setBirthday(appEmployee.getBirthday());

        City city = cityService.findById(appEmployee.getCityId());
        if (city != null) {
            informationResponse.setCityName(city.getName());
        }
        AppStore appStore = storeService.findById(appEmployee.getStoreId());
        if (appStore != null) {
            informationResponse.setStoreName(appStore.getStoreName());
        }
        return informationResponse;
    }

    private UserInformationResponse transform(AppCustomer appCustomer) {
        UserInformationResponse informationResponse = new UserInformationResponse();
        informationResponse.setPicUrl(appCustomer.getPicUrl());
        informationResponse.setName(appCustomer.getNickName());
        informationResponse.setSex(appCustomer.getSex().getValue());
        informationResponse.setMobile(appCustomer.getMobile());
        informationResponse.setBirthday(appCustomer.getBirthday());

        City city = cityService.findById(appCustomer.getCityId());
        if (city != null) {
            informationResponse.setCityName(city.getName());
        }
        AppStore appStore = storeService.findById(appCustomer.getStoreId());
        if (appStore != null) {
            informationResponse.setStoreName(appStore.getStoreName());
        }
        AppEmployee guide = employeeService.findByUserId(appCustomer.getCusId());
        if (guide != null) {
            informationResponse.setGuideName(guide.getName());
        }
        return informationResponse;
    }
}

package cn.com.leyizhuang.app.web.controller.user;

import cn.com.leyizhuang.app.core.constant.*;
import cn.com.leyizhuang.app.core.utils.DateUtil;
import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.core.utils.oss.FileUploadOSSUtils;
import cn.com.leyizhuang.app.foundation.pojo.*;
import cn.com.leyizhuang.app.foundation.pojo.request.DeliveryAddressRequest;
import cn.com.leyizhuang.app.foundation.pojo.request.UserSetInformationReq;
import cn.com.leyizhuang.app.foundation.pojo.response.*;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
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
    private AppCustomerService customerService;

    @Resource
    private AppEmployeeService employeeService;

    @Resource
    private AppStoreService storeService;

    @Resource
    private CityService cityService;

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
     * 用户修改个人信息
     *
     * @param userInformation 修改用户传输对象
     * @return
     */
    @PostMapping(value = "/set/information", produces = "application/json;charset=UTF-8")
    public ResultDTO personalInformationSet(UserSetInformationReq userInformation) {

        logger.info("personalInformationSet CALLED,用户修改个人信息，入参 userInformation {}", userInformation);

        ResultDTO resultDTO;
        if (null == userInformation.getUserId()) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户不能为空", null);
            logger.info("personalInformationSet OUT,用户修改个人信息失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == userInformation.getIdentityType()) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空", null);
            logger.info("personalInformationSet OUT,用户修改个人信息失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            if (userInformation.getIdentityType() == 6) {
                AppCustomer appCustomer = transformAppCustomer(userInformation);
                customerService.update(appCustomer);
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
                logger.info("personalInformationSet OUT,用户修改个人信息成功，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            AppEmployee appEmployee = transformAppEmployee(userInformation);
            employeeService.update(appEmployee);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
            logger.info("personalInformationSet OUT,用户修改个人信息成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知异常,用户修改个人信息失败!", null);
            logger.warn("personalInformationSet EXCEPTION,用户修改个人信息失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }


    /**
     * 用户修改手机号码
     *
     * @param userId
     * @param identityType
     * @return
     */
    @PostMapping(value = "/set/mobile", produces = "application/json;charset=UTF-8")
    public ResultDTO personalMobileSet(Long userId, Integer identityType, String mobile) {

        logger.info("personalMobileSet CALLED,用户修改手机号码，入参 userId {},identityType{},mobile{}", userId, identityType, mobile);

        ResultDTO<UserInformationResponse> resultDTO;
        if (userId == null) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "userId不能为空！", null);
            logger.info("personalMobileSet OUT,用户修改手机号码失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空",
                    null);
            logger.info("personalMobileSet OUT,用户修改手机号码失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (StringUtils.isBlank(mobile)) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "新手机号码不能为空",
                    null);
            logger.info("personalMobileSet OUT,用户修改手机号码失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            if (identityType == 6) {
                customerService.modifyCustomerMobileByUserId(userId, mobile);
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
                logger.info("personalMobileSet OUT,用户修改手机号码成功，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            } else {
                employeeService.modifyEmployeeMobileByUserId(userId, mobile);
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
                logger.info("personalMobileSet OUT,用户修改手机号码成功，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知异常,用户修改手机号码失败!", null);
            logger.warn("personalMobileSet EXCEPTION,用户修改手机号码失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
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
    public ResultDTO<List> getDeliveryAddress(Long userId, Integer identityType) {
        logger.info("getDeliveryAddress CALLED,获取收货地址，入参 userId {},identityType", userId, identityType);

        ResultDTO<List> resultDTO;
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户信息不能为空！", null);
            logger.info("getDeliveryAddress OUT,获取收货地址失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空！", null);
            logger.info("getDeliveryAddress OUT,获取收货地址失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        List<DeliveryAddressResponse> deliveryAddressResponseList = this.deliveryAddressServiceImpl.queryListByUserIdAndStatusIsTrue(userId, AppIdentityType.getAppIdentityTypeByValue(identityType));

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
            if (null == identityType) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空！",
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
            this.deliveryAddressServiceImpl.addDeliveryAddress(userId, AppIdentityType.getAppIdentityTypeByValue(identityType), deliveryAddress);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
            logger.info("addDeliveryAddress OUT,顾客新增收货地址成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知异常,顾客新增收货地址失败!", null);
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
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户信息不能为空！", null);
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
            this.deliveryAddressServiceImpl.modifyDeliveryAddress(userId, AppIdentityType.getAppIdentityTypeByValue(identityType), deliveryAddress);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
            logger.info("modifyDeliveryAddress OUT,顾客编辑收货地址成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知异常,顾客编辑收货地址失败!", null);
            logger.warn("modifyDeliveryAddress EXCEPTION,顾客编辑收货地址失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }


    /**
     * 删除收货地址
     *
     * @param userId
     * @param identityType
     * @param deliveryAddressId
     * @return
     */
    @PostMapping(value = "/deliveryAddress/delete", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> deleteDeliveryAddress(Long userId, Integer identityType, Long deliveryAddressId) {
        logger.info("deleteDeliveryAddress CALLED,顾客删除收货地址，入参 userId:{} identityType:{} deliveryAddressId:{}", userId, identityType, deliveryAddressId);

        ResultDTO<Object> resultDTO;
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户信息不能为空！", null);
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
     * @param userId
     * @return resultDTO
     * @throws Exception
     * @title 功能反馈
     * @descripe 功能反馈
     * @author GenerationRoad
     * @date 2017/10/10
     */
    @PostMapping(value = "/feedback/add")
    public ResultDTO<Object> addFunctionalFeedback(Long userId, Integer identityType, @RequestParam(value = "myfiles", required = false) MultipartFile[] files,
                                                   String type, String content, String phone) {
        logger.info("addFunctionalFeedback CALLED,功能反馈，入参 userId:{} identityType:{} files:{}," +
                "type:{}, content:{}, phone:{}", userId, identityType, files, type, content, phone);
        ResultDTO<Object> resultDTO;
        try {
            if (null == userId) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户信息不能为空！", null);
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
            String photos = "";
            for (int i = 0; i < files.length; i++) {
                photos += FileUploadOSSUtils.uploadProfilePhoto(files[i], "fedback/photo");
                photos += ",";
            }

            FunctionalFeedbackDO functionalFeedbackDO = new FunctionalFeedbackDO();
            functionalFeedbackDO.setType(FunctionFeedBackType.getFunctionFeedBackTypeByValue(type));
            functionalFeedbackDO.setContent(content);
            functionalFeedbackDO.setPhone(phone);
            functionalFeedbackDO.setPictureUrl(photos);
            functionalFeedbackDO.setStatus(FunctionalFeedbackStatusEnum.NOT_CHECKED);
            functionalFeedbackDO.setUserId(userId);
            functionalFeedbackDO.setIdentityType(AppIdentityType.getAppIdentityTypeByValue(identityType));
            functionalFeedbackDO.setCreateTime(LocalDateTime.now());
            this.functionalFeedbackServiceImpl.save(functionalFeedbackDO);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
            logger.info("addFunctionalFeedback OUT,功能反馈成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知异常,功能反馈失败!", null);
            logger.warn("addFunctionalFeedback EXCEPTION,功能反馈失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    private UserInformationResponse transform(AppEmployee appEmployee) {
        UserInformationResponse informationResponse = new UserInformationResponse();
        informationResponse.setPicUrl(appEmployee.getPicUrl());
        informationResponse.setName(appEmployee.getName());
        informationResponse.setNikeName(appEmployee.getLoginName());
        informationResponse.setSex(appEmployee.getSex().getValue());
        informationResponse.setMobile(appEmployee.getMobile());
        informationResponse.setBirthday(DateUtil.formatDate(appEmployee.getBirthday(), "yyyy-MM-dd"));

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
        informationResponse.setName(appCustomer.getName());
        informationResponse.setNikeName(appCustomer.getNickName());
        informationResponse.setSex(appCustomer.getSex().getValue());
        informationResponse.setMobile(appCustomer.getMobile());
        if (null != appCustomer.getBirthday()) {
            informationResponse.setBirthday(DateUtil.formatDate(appCustomer.getBirthday(), "yyyy-MM-dd"));
        }
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

    private AppCustomer transformAppCustomer(UserSetInformationReq userInformation) {
        AppCustomer appCustomer = new AppCustomer();
        appCustomer.setCusId(userInformation.getUserId());
        appCustomer.setNickName(userInformation.getNikeName());
        appCustomer.setName(userInformation.getName());
        String birthday = userInformation.getBirthday();
        if (StringUtils.isNotBlank(birthday)) {
            appCustomer.setBirthday(DateUtil.parseDate(birthday));
        }
        String sex = userInformation.getSex();
        if (StringUtils.isNotBlank(sex)) {
            appCustomer.setSex(SexType.getSexTypeByValue(userInformation.getSex()));
        }
        if (null != userInformation.getHeadPic()) {
            String picUrl = FileUploadOSSUtils.uploadProfilePhoto(userInformation.getHeadPic(), "profile/photo/");
            appCustomer.setPicUrl(picUrl);
        }
        return appCustomer;
    }

    private AppEmployee transformAppEmployee(UserSetInformationReq userInformation) {
        AppEmployee appEmployee = new AppEmployee();
        appEmployee.setEmpId(userInformation.getUserId());
        appEmployee.setName(userInformation.getName());
        String birthday = userInformation.getBirthday();
        if (StringUtils.isNotBlank(birthday)) {
            appEmployee.setBirthday(DateUtil.parseDate(userInformation.getBirthday()));
        }
        String sex = userInformation.getSex();
        if (StringUtils.isNotBlank(sex)) {
            appEmployee.setSex(SexType.getSexTypeByValue(sex));
        }
        if (null != userInformation.getHeadPic()) {
            String picUrl = FileUploadOSSUtils.uploadProfilePhoto(userInformation.getHeadPic(), "profile/photo/");
            appEmployee.setPicUrl(picUrl);
        }
        return appEmployee;
    }


   /**
    * @title 获取客户归属门店和导购
    * @descripe
    * @param
    * @return
    * @throws
    * @author GenerationRoad
    * @date 2017/11/3
    */
    @PostMapping(value = "/storeSeller/get", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> getStoreSeller(Long userId, Integer identityType, Long cityId) {
        logger.info("getStoreSeller CALLED,获取客户归属门店和导购，入参 userId {},identityType{}, cityId{}", userId, identityType, cityId);

        ResultDTO<Object> resultDTO;
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户信息不能为空！", null);
            logger.info("getStoreSeller OUT,获取客户归属门店和导购失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType || identityType != 6) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型错误！", null);
            logger.info("getStoreSeller OUT,获取客户归属门店和导购失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == cityId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "归属城市信息不能为空！", null);
            logger.info("getStoreSeller OUT,获取客户归属门店和导购失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        AppCustomer appCustomer = this.customerService.findStoreSellerByCustomerId(userId);
        StoreSellerResponse storeSeller = new StoreSellerResponse();
        if (null != appCustomer){
            storeSeller.setStoreId(appCustomer.getStoreId());
            storeSeller.setSellerId(appCustomer.getSalesConsultId());
            AppStore store = storeService.findById(appCustomer.getStoreId());
            if (null == appCustomer.getBindingTime() || store.getIsDefault()){
                storeSeller.setIsPassable(Boolean.TRUE);
            } else {
                Date date = new Date();
                if (DateUtil.intervalDay(appCustomer.getBindingTime(), date) > 60) {
                    storeSeller.setIsPassable(Boolean.TRUE);
                } else {
                    storeSeller.setIsPassable(Boolean.FALSE);
                }
            }
            storeSeller.setSellerList(this.employeeService.findSellerByStoreIdAndIdentityType(appCustomer.getStoreId(), AppIdentityType.getAppIdentityTypeByValue(0)));
        }
        storeSeller.setStoreList(this.storeService.findStoreByCityId(cityId));
        resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, storeSeller);
        logger.info("getStoreSeller OUT,获取客户归属门店和导购成功，出参 resultDTO:{}", resultDTO);
        return resultDTO;
    }

    /**
     * @title  根据门店ID查询所有导购
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2017/11/3
     */
    @PostMapping(value = "/seller/get", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> getSeller(Long userId, Integer identityType, Long storeId) {
        logger.info("getSeller CALLED,根据门店ID查询所有导购，入参 userId {},identityType{}, storeId{}", userId, identityType, storeId);

        ResultDTO<Object> resultDTO;
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户信息不能为空！", null);
            logger.info("getSeller OUT,根据门店ID查询所有导购失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型错误！", null);
            logger.info("getSeller OUT,根据门店ID查询所有导购失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == storeId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "归属城市信息不能为空！", null);
            logger.info("getSeller OUT,根据门店ID查询所有导购失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        List<SellerResponse> sellerResponseList = this.employeeService.findSellerByStoreIdAndIdentityType(storeId, AppIdentityType.getAppIdentityTypeByValue(0));
        resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, sellerResponseList);
        logger.info("getSeller OUT,根据门店ID查询所有导购成功，出参 resultDTO:{}", resultDTO);
        return resultDTO;
    }

    /**
     * @title   顾客绑定服务导购
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2017/11/3
     */
    @PostMapping(value = "/binding/seller", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> customerBindingSeller(Long userId, Integer identityType, Long storeId, Long sellerId) {
        logger.info("customerBindingSeller CALLED,顾客绑定服务导购，入参 userId {},identityType{},storeId {},sellerId{}", userId, identityType, storeId, sellerId);

        ResultDTO<Object> resultDTO;
        try {
            if (null == userId) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户信息不能为空！", null);
                logger.info("customerBindingSeller OUT,服务导购绑定失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == identityType || identityType != 6) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型错误！", null);
                logger.info("customerBindingSeller OUT,服务导购绑定失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == storeId) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "门店信息不能为空！", null);
                logger.info("customerBindingSeller OUT,服务导购绑定失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == sellerId) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "导购信息不能为空！", null);
                logger.info("customerBindingSeller OUT,服务导购绑定失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            AppCustomer customer = customerService.findById(userId);
            AppStore store = storeService.findById(customer.getStoreId());
            Date date = new Date();
            if ((!store.getIsDefault()) && null != customer.getBindingTime() && DateUtil.intervalDay(customer.getBindingTime(), date) < 60) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "您现在不能修改你的归属导购！", null);
                logger.info("customerBindingSeller OUT,服务导购绑定失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (customer == null) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户不存在！", null);
                logger.info("customerBindingSeller OUT,服务导购绑定失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            AppEmployee seller = employeeService.findById(sellerId);
            if (null != seller && seller.getStoreId() == storeId){
                customer.setSalesConsultId(sellerId);
                customer.setStoreId(storeId);
                customer.setCustomerType(AppCustomerType.MEMBER);
                customer.setBindingTime(new Date());
                customerService.update(customer);
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null,null);
                logger.info("customerBindingSeller OUT,服务导购绑定成功，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            } else {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "userId不能为空！", null);
                logger.info("customerBindingSeller OUT,服务导购绑定失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知异常,绑定导购失败", null);
            logger.warn("customerBindingSeller EXCEPTION,服务导购绑定失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }
}

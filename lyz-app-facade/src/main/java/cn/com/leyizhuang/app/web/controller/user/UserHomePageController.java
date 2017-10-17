package cn.com.leyizhuang.app.web.controller.user;

import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.pojo.AppCustomer;
import cn.com.leyizhuang.app.foundation.pojo.AppEmployee;
import cn.com.leyizhuang.app.foundation.pojo.response.CustomerListResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.EmployeeListResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.UserHomePageResponse;
import cn.com.leyizhuang.app.foundation.service.IAppCustomerService;
import cn.com.leyizhuang.app.foundation.service.IAppEmployeeService;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 个人心中接口
 * Created with IntelliJ IDEA.
 * Created by Jerry.Ren
 * Date: 2017/9/27.
 * Time: 13:59.
 */

@RestController
@RequestMapping(value = "/app/user")
public class UserHomePageController {

    private static final Logger logger = LoggerFactory.getLogger(UserHomePageController.class);

    @Resource
    private IAppCustomerService customerService;

    @Resource
    private IAppEmployeeService employeeService;

    /**
     * 个人主页的信息
     *
     * @param userId       用户Id
     * @param identityType 用户类型
     * @return
     */
    @PostMapping(value = "/homepage", produces = "application/json;charset=UTF-8")
    public ResultDTO personalHomepage(Long userId, Integer identityType) {

        logger.info("personalHomepage CALLED,获取个人主页，入参 userId {},identityType{}", userId, identityType);

        ResultDTO<UserHomePageResponse> resultDTO;
        if (userId == null) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "userId不能为空！", null);
            logger.info("personalHomepage OUT,获取个人主页失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }

        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空",
                    null);
            logger.info("personalHomepage OUT,获取个人主页失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (identityType == 6) {
            AppCustomer appCustomer = customerService.findById(userId);
            if (appCustomer == null) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户不存在！", null);
                logger.info("personalHomepage OUT,获取个人主页失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            UserHomePageResponse userHomePageResponse = new UserHomePageResponse();
            userHomePageResponse.setName(appCustomer.getNickName());
            userHomePageResponse.setPicUrl(appCustomer.getPicUrl());
            AppEmployee guide = employeeService.findByUserId(userId);
            if (guide != null) {
                userHomePageResponse.setGuideName(guide.getName());
                userHomePageResponse.setGuideMobile(guide.getMobile());
            }
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, userHomePageResponse);
            logger.info("personalHomepage OUT,获取个人主页成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }

        AppEmployee appEmployee = employeeService.findById(userId);

        if (appEmployee == null) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户不存在！", null);
            logger.info("personalHomepage OUT,获取个人主页失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        UserHomePageResponse userHomePageResponse = new UserHomePageResponse();
        userHomePageResponse.setName(appEmployee.getName());
        userHomePageResponse.setPicUrl(appEmployee.getPicUrl());
        userHomePageResponse.setNumber(appEmployee.getLoginName());
        resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, userHomePageResponse);
        logger.info("personalHomepage OUT,获取个人主页成功，出参 resultDTO:{}", resultDTO);
        return resultDTO;
    }

    /**
     * 导购获取我的顾客列表接口
     *
     * @param userId
     * @param identityType
     * @return
     */
    @PostMapping(value = "/customer/list", produces = "application/json;charset=UTF-8")
    public ResultDTO getCustomersList(Long userId, Integer identityType) {

        logger.info("getCustomersList CALLED,获取我的顾客列表，入参 userId {},identityType{}", userId, identityType);

        ResultDTO<Object> resultDTO;
        if (userId == null) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "userId不能为空！", null);
            logger.info("getCustomersList OUT,获取我的顾客列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户身份不能为空", null);
            logger.info("getCustomersList OUT,获取我的顾客列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            List<CustomerListResponse> appCustomerList = customerService.findListByUserIdAndIdentityType(userId, identityType);

            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, appCustomerList);
            logger.info("getCustomersList OUT,获取我的顾客列表成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，获取我的顾客列表失败", null);
            logger.warn("getCustomersList EXCEPTION,获取我的顾客列表失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    /**
     * 导购搜索我的顾客
     *
     * @param keywords
     * @param userId
     * @param identityType
     * @return
     */
    @PostMapping(value = "/search/customer", produces = "application/json;charset=UTF-8")
    public ResultDTO searchCustomerList(String keywords, Long userId, Integer identityType) {

        logger.info("searchCustomerList CALLED,搜索我的顾客，入参 keywords {},userId{},identityType{}", keywords, userId, identityType);
        ResultDTO<Object> resultDTO;
        if (StringUtils.isBlank(keywords)) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "搜索关键词不能为空！", null);
            logger.info("searchCustomerList OUT,搜索我的顾客失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id{userId}不能为空！", null);
            logger.info("searchCustomerList OUT,搜索我的顾客失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户身份不能为空", null);
            logger.info("getCustomersList OUT,搜索我的顾客失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            List<CustomerListResponse> appCustomerList = customerService.searchByUserIdAndKeywordsAndIdentityType(userId, keywords, identityType);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null,
                    (appCustomerList != null && appCustomerList.size() > 0) ? appCustomerList : null);
            logger.info("searchCustomerList OUT,搜索我的顾客成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，搜索我的顾客失败", null);
            logger.warn("searchCustomerList EXCEPTION,搜索我的顾客失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    /**
     * 装饰公司经理获取工人列表
     *
     * @param userId
     * @return
     */
    @PostMapping(value = "/decorateEmployee/list", produces = "application/json;charset=UTF-8")
    public ResultDTO getDecorateEmployeeList(Long userId, Integer identityType) {

        logger.info("getDecorateEmployeeList CALLED,获取我的员工列表，入参 userId {},identityType{}", userId, identityType);

        ResultDTO<Object> resultDTO;
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "userId不能为空！", null);
            logger.info("getDecorateEmployeeList OUT,获取我的员工列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户身份不能为空", null);
            logger.info("getDecorateEmployeeList OUT,获取我的员工列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            List<EmployeeListResponse> appEmployeeList = employeeService.findDecorateEmployeeListByUserIdAndIdentityType(userId, identityType);

            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, appEmployeeList);
            logger.info("getDecorateEmployeeList OUT,获取我的员工列表成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，获取我的员工列表失败", null);
            logger.warn("getDecorateEmployeeList EXCEPTION,获取我的员工列表失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }
}

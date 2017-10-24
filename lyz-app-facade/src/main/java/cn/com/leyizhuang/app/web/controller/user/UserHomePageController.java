package cn.com.leyizhuang.app.web.controller.user;

import cn.com.leyizhuang.app.core.constant.AppUserLightStatus;
import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.pojo.AppEmployee;
import cn.com.leyizhuang.app.foundation.pojo.response.CustomerListResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.EmployeeHomePageResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.EmployeeListResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.CustomerHomePageResponse;
import cn.com.leyizhuang.app.foundation.service.AppCustomerService;
import cn.com.leyizhuang.app.foundation.service.AppEmployeeService;
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
    private AppCustomerService customerService;

    @Resource
    private AppEmployeeService employeeService;

    /**
     * 个人主页的信息
     *
     * @param userId       用户Id
     * @param identityType 用户类型
     * @return
     */
    @PostMapping(value = "/homepage", produces = "application/json;charset=UTF-8")
    public ResultDTO getPersonalHomepage(Long userId, Integer identityType) {

        logger.info("personalHomepage CALLED,获取个人主页，入参 userId {},identityType{}", userId, identityType);

        ResultDTO<Object> resultDTO;
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
        try{
            if (identityType == 6) {
                CustomerHomePageResponse customerHomePageResponse = customerService.findCustomerInfoByUserId(userId);
                String parseLight = AppUserLightStatus.valueOf(customerHomePageResponse.getLight()).getValue();
                customerHomePageResponse.setLight(parseLight);
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, customerHomePageResponse);
                logger.info("personalHomepage OUT,获取个人主页成功，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }else {
                EmployeeHomePageResponse employeeHomePageResponse = employeeService.findEmployeeInfoByUserIdAndIdentityType(userId,identityType);
                // TODO 配送员还需要查询配送订单数量
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, employeeHomePageResponse);
                logger.info("personalHomepage OUT,获取个人主页成功，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，获取个人主页失败", null);
            logger.info("personalHomepage OUT,获取个人主页失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}",e);
            return resultDTO;
        }
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

            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null,
                    (appCustomerList != null && appCustomerList.size() > 0) ? appCustomerList : null);
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

            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null,
                    (appEmployeeList != null && appEmployeeList.size() > 0) ? appEmployeeList : null);
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

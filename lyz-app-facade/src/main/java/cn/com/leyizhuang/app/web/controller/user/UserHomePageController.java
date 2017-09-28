package cn.com.leyizhuang.app.web.controller.user;

import cn.com.leyizhuang.app.foundation.pojo.AppCustomer;
import cn.com.leyizhuang.app.foundation.pojo.AppEmployee;
import cn.com.leyizhuang.app.foundation.pojo.response.UserHomePageResponse;
import cn.com.leyizhuang.app.foundation.service.IAppCustomerService;
import cn.com.leyizhuang.app.foundation.service.IAppEmployeeService;
import cn.com.leyizhuang.app.web.controller.employee.EmployeeController;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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

    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    @Resource
    private IAppCustomerService customerService;

    @Resource
    private IAppEmployeeService employeeService;

    /**
     * 个人主页的信息
     * @param userId 用户Id
     * @param type 用户类型(0导购，1配送员，2经理，3工人，4顾客)
     * @return
     */
    @PostMapping(value = "/homepage",produces="application/json;charset=UTF-8")
    public ResultDTO personalHomepage(Long userId,int type){

        logger.info("personalHomepage CALLED,获取个人主页，入参 userId {},type{}", userId, type);

        ResultDTO<UserHomePageResponse> resultDTO;
        if (userId == null){
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "userId不能为空！", null);
            logger.info("personalHomepage OUT,获取个人主页，出参 resultDTO:{}",resultDTO);
            return resultDTO;
        }

        int [] types = {0,1,2,3};

        if (ArrayUtils.contains(types,type)){
            AppEmployee appEmployee =  employeeService.findById(userId);
            if (appEmployee==null){
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户不存在！", null);
                logger.info("personalHomepage OUT,获取个人主页，出参 resultDTO:{}",resultDTO);
                return resultDTO;
            }
            UserHomePageResponse userHomePageResponse = new UserHomePageResponse();
            userHomePageResponse.setName(appEmployee.getName());
            userHomePageResponse.setPicUrl(appEmployee.getPicUrl());
            userHomePageResponse.setNumber(appEmployee.getLoginName());
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "获取个人主页信息成功！", null);
            logger.info("personalHomepage OUT,获取个人主页成功，出参 resultDTO:{}",resultDTO);
            return resultDTO;

        }else if(type == 4){
            AppCustomer appCustomer =  customerService.findById(userId);
            if (appCustomer==null){
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户不存在！", null);
                logger.info("personalHomepage OUT,获取个人主页，出参 resultDTO:{}",resultDTO);
                return resultDTO;
            }
            UserHomePageResponse userHomePageResponse = new UserHomePageResponse();
            userHomePageResponse.setNumber(appCustomer.getNickName());
            userHomePageResponse.setPicUrl(appCustomer.getPicUrl());
            AppEmployee guide = employeeService.findByUserId(userId);
            if (guide != null) {
                userHomePageResponse.setGuideName(guide.getName());
                userHomePageResponse.setGuideMobile(guide.getMobile());
            }
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "获取个人主页信息成功！", null);
            logger.info("personalHomepage OUT,获取个人主页成功，出参 resultDTO:{}",resultDTO);
            return resultDTO;
        }
        resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空",
                null);
        logger.info("personalHomepage OUT,获取个人主页，出参 resultDTO:{}",resultDTO);
        return resultDTO;
    }

}

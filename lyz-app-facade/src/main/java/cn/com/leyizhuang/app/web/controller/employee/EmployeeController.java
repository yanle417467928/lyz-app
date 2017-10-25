package cn.com.leyizhuang.app.web.controller.employee;

import cn.com.leyizhuang.app.core.constant.JwtConstant;
import cn.com.leyizhuang.app.core.utils.JwtUtils;
import cn.com.leyizhuang.app.foundation.pojo.AppEmployee;
import cn.com.leyizhuang.app.foundation.pojo.SellerCreditMoney;
import cn.com.leyizhuang.app.foundation.pojo.request.EmployeeLoginParam;
import cn.com.leyizhuang.app.foundation.pojo.response.EmployeeLoginResponse;
import cn.com.leyizhuang.app.foundation.service.AppEmployeeService;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.core.utils.Base64Utils;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * App 员工相关http接口
 *
 * @author Richard
 *         Created on 2017-09-19 9:44
 **/
@RestController
@RequestMapping(value = "/app/employee")
public class EmployeeController {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    @Resource
    private AppEmployeeService appEmployeeService;

    /**
     * App 员工登录接口
     *
     * @param loginParam 登录参数
     * @param response   响应对象
     * @return ResultDTO
     */
    @PostMapping(value = "/login", produces = "application/json;charset=UTF-8")
    public ResultDTO<EmployeeLoginResponse> employeeLogin(EmployeeLoginParam loginParam, HttpServletResponse response) {
        logger.info("employeeLogin CALLED,员工登录，入参 loginParam:{}", loginParam.toString());
        ResultDTO<EmployeeLoginResponse> resultDTO;
        try {
            if (null == loginParam.getName() || "".equalsIgnoreCase(loginParam.getName())) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户名不允许为空！", null);
                logger.info("employeeLogin OUT,员工登录失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == loginParam.getPassword() || "".equalsIgnoreCase(loginParam.getPassword())) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "密码不允许为空！", null);
                logger.info("employeeLogin OUT,员工登录失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            AppEmployee employee = appEmployeeService.findByLoginName(loginParam.getName());
            if (employee == null) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "没有找到该用户！", null);
                logger.info("employeeLogin OUT,员工登录失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            } else {
                String md5Password = DigestUtils.md5DigestAsHex((Base64Utils.decode(loginParam.getPassword()) + employee.getSalt()).getBytes("UTF-8"));
                if (md5Password.compareTo(employee.getPassword()) != 0) {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "密码错误！", null);
                    logger.info("employeeLogin OUT,员工登录失败，出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }
            }
            //拼装accessToken
            String accessToken = JwtUtils.createJWT(String.valueOf(employee.getEmpId()), String.valueOf(employee.getLoginName()),
                    JwtConstant.EXPPIRES_SECOND * 1000);
            System.out.println(accessToken);
            response.setHeader("token", accessToken);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, new EmployeeLoginResponse(employee.getIdentityType().getValue(),employee.getEmpId()));
            logger.info("employeeLogin OUT,员工登录成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知异常", null);
            logger.warn("employeeLogin EXCEPTION,员工登录出现异常,出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    /**
     * App 员工修改密码
     *
     * @param mobile   员工手机号码
     * @param password 密码
     * @return ResultDTO
     */
    @PostMapping(value = "/password/modify", produces = "application/json;charset=UTF-8")
    public ResultDTO<String> employeeModifyPassword(String mobile, String password) {
        logger.info("employeeModifyPassword CALLED,员工修改密码，参数: mobile{},password{}", mobile, password);
        ResultDTO<String> resultDTO;
        try {
            if (null == mobile || "".equalsIgnoreCase(mobile)) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "手机号码不允许为空！", null);
                logger.info("employeeModifyPassword OUT,员工修改密码失败，返回值resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == password || "".equalsIgnoreCase(password)) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "密码不允许为空！", null);
                logger.info("employeeModifyPassword OUT,员工修改密码失败，返回值resultDTO:{}", resultDTO);
                return resultDTO;
            }
            AppEmployee employee = appEmployeeService.findByMobile(mobile);
            if (employee == null) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "手机号码不存在！", null);
                logger.info("employeeModifyPassword OUT,员工修改密码失败，返回值resultDTO:{}", resultDTO);
                return resultDTO;
            } else {
                String md5Password = DigestUtils.md5DigestAsHex((Base64Utils.decode(password) + employee.getSalt()).getBytes("UTF-8"));
                AppEmployee newEmployee = new AppEmployee();
                newEmployee.setEmpId(employee.getEmpId());
                newEmployee.setPassword(md5Password);
                appEmployeeService.update(newEmployee);
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
                logger.info("employeeModifyPassword OUT,员工修改密码失败，返回值resultDTO:{}", resultDTO);
                return resultDTO;
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知异常，密码修改失败", null);
            logger.warn("employeeModifyPassword EXCEPTION,员工修改密码出现未知异常,返回值resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    /**
     * 获取导购信用金余额
     * @param userId
     * @param identityType
     * @return
     */
    @PostMapping(value = "/creditMoney/balance", produces = "application/json;charset=UTF-8")
    public ResultDTO<SellerCreditMoney> getGuideCreditMoneyBalance(Long userId, Integer identityType){

        logger.info("getGuideCreditMoneyBalance CALLED,获取导购信用金余额，入参 userId {},identityType{}", userId, identityType);

        ResultDTO<SellerCreditMoney> resultDTO;
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空", null);
            logger.info("getGuideCreditMoneyBalance OUT,获取导购信用金余额失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空",
                    null);
            logger.info("getGuideCreditMoneyBalance OUT,获取导购信用金余额失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            SellerCreditMoney sellerCreditMoney = appEmployeeService.findCreditMoneyBalanceByUserIdAndIdentityType(userId,identityType);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, sellerCreditMoney);
            logger.info("getGuideCreditMoneyBalance OUT,获取导购信用金余额成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，获取导购信用金余额失败", null);
            logger.warn("getGuideCreditMoneyBalance EXCEPTION,获取导购信用金余额失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }
}

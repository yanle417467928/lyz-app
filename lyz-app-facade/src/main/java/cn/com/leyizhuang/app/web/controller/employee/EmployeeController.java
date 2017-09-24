package cn.com.leyizhuang.app.web.controller.employee;

import cn.com.leyizhuang.app.core.constant.JwtConstant;
import cn.com.leyizhuang.app.core.utils.JwtUtils;
import cn.com.leyizhuang.app.foundation.pojo.AppEmployee;
import cn.com.leyizhuang.app.foundation.pojo.request.EmployeeLoginParam;
import cn.com.leyizhuang.app.foundation.pojo.response.EmployeeLoginResponse;
import cn.com.leyizhuang.app.foundation.service.IAppEmployeeService;
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
 * Created on 2017-09-19 9:44
 **/
@RestController
@RequestMapping(value = "/app/employee")
public class EmployeeController {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    @Resource
    private IAppEmployeeService appEmployeeService;

    /**
     *  App 员工登录接口
     * @param loginParam
     * @param response
     * @return
     */
    @PostMapping(value = "/login",produces="application/json;charset=UTF-8")
    public ResultDTO<EmployeeLoginResponse> employeeLogin(EmployeeLoginParam loginParam, HttpServletResponse response) {
        ResultDTO<EmployeeLoginResponse> resultDTO;
        try {
            if (null == loginParam.getName() || "".equalsIgnoreCase(loginParam.getName())) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户名不允许为空！", null);
                return resultDTO;
            }
            if (null == loginParam.getPassword() || "".equalsIgnoreCase(loginParam.getPassword())) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "密码不允许为空！", null);
                return resultDTO;
            }
            AppEmployee employee = appEmployeeService.findByLoginName(loginParam.getName());
            if (employee == null) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "没有找到该用户！", null);
                return resultDTO;
            } else {
                String md5Password = DigestUtils.md5DigestAsHex((Base64Utils.decode(loginParam.getPassword()) + employee.getSalt()).getBytes("UTF-8"));
                if (md5Password.compareTo(employee.getPassword()) != 0) {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "密码错误！", null);
                    return resultDTO;
                }
            }
            //拼装accessToken
            String accessToken = JwtUtils.createJWT(String.valueOf(employee.getId()),String.valueOf(employee.getLoginName()),
                     JwtConstant.EXPPIRES_SECOND *1000);
            System.out.println(accessToken);
            response.setHeader("token", accessToken);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, new EmployeeLoginResponse(employee.getEmployeeType().getValue()));
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("登录出现异常");
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知异常", null);
            return resultDTO;
        }
    }

    /** App 员工修改密码
     * @param mobile
     * @param password
     * @return
     */
    @PostMapping(value = "/password/modify",produces="application/json;charset=UTF-8")
    public ResultDTO<String> employeeModifyPassword(String mobile, String password) {
        ResultDTO<String> resultDTO;
        try {
            if (null == mobile || "".equalsIgnoreCase(mobile)) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "手机号码不允许为空！", null);
                return resultDTO;
            }
            if (null == password || "".equalsIgnoreCase(password)) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "密码不允许为空！", null);
                return resultDTO;
            }
            AppEmployee employee = appEmployeeService.findByMobile(mobile);
            if (employee == null) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "手机号码不存在！", null);
                return resultDTO;
            } else {
                String md5Password = DigestUtils.md5DigestAsHex((Base64Utils.decode(password) + employee.getSalt()).getBytes("UTF-8"));
                AppEmployee newEmployee = new AppEmployee();
                newEmployee.setId(employee.getId());
                newEmployee.setPassword(md5Password);
                appEmployeeService.update(newEmployee);
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS,null,null);
                return resultDTO;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("修改密码出现未知异常");
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知异常，密码修改失败", null);
            return resultDTO;
        }
    }

}

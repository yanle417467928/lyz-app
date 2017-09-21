package cn.com.leyizhuang.app.web.controller.employee;

import cn.com.leyizhuang.app.core.constant.JwtConstant;
import cn.com.leyizhuang.common.core.utils.Base64Utils;
import cn.com.leyizhuang.app.core.utils.JwtUtils;
import cn.com.leyizhuang.app.foundation.pojo.AppUser;
import cn.com.leyizhuang.app.foundation.pojo.LoginParam;
import cn.com.leyizhuang.app.foundation.pojo.rest.EmployeeLoginResponse;
import cn.com.leyizhuang.app.foundation.service.IAppUserService;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * 获取token的接口
 *
 * @author Richard
 * Created on 2017-09-19 9:44
 **/
@RestController
public class EmployeeController {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    @Resource
    private IAppUserService appUserService;

    @PostMapping(value = "/app/employee/login",produces="application/json;charset=UTF-8")
    public ResultDTO<EmployeeLoginResponse> employeeLogin(LoginParam loginParam, HttpServletResponse response) {
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
            AppUser user = appUserService.findByLoginName(loginParam.getName());
            if (user == null) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "没有找到该用户！", null);
                return resultDTO;
            } else {
                String md5Password = DigestUtils.md5DigestAsHex((Base64Utils.decode(loginParam.getPassword()) + user.getSalt()).getBytes("UTF-8"));
                if (md5Password.compareTo(user.getPassword()) != 0) {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "密码错误！", null);
                    return resultDTO;
                }
            }
            //拼装accessToken
            String accessToken = JwtUtils.createJWT(String.valueOf(user.getId()),String.valueOf(user.getLoginName()),
                     JwtConstant.EXPPIRES_SECOND *1000);
            System.out.println(accessToken);
            response.setHeader("token", accessToken);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, new EmployeeLoginResponse(user.getUserType().getValue()));
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("登录出现异常");
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知异常", null);
            return resultDTO;
        }
    }

}

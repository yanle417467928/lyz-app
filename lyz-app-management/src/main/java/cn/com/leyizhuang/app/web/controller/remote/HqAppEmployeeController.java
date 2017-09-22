package cn.com.leyizhuang.app.web.controller.remote;

import cn.com.leyizhuang.app.core.constant.AppUserType;
import cn.com.leyizhuang.app.core.constant.SexType;
import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.pojo.AppUser;
import cn.com.leyizhuang.app.foundation.service.impl.AppAdminAppUserService;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.core.utils.Base64Utils;
import cn.com.leyizhuang.common.foundation.pojo.HqAppEmployeeDTO;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;

/**
 * HQ~APP员工相关操作
 *
 * @author Richard
 * Created on 2017-09-20 13:14
 **/
@RestController
@RequestMapping(value = "/remote/employee")
public class HqAppEmployeeController {
    private static final Logger logger = LoggerFactory.getLogger(HqAppEmployeeController.class);

    @Autowired
    private AppAdminAppUserService appUserService;

    @PostMapping(value = "sync")
    public ResultDTO<String> employeeSync(@RequestBody HqAppEmployeeDTO employeeDTO) {
        if (null != employeeDTO) {
            if (StringUtils.isBlank(employeeDTO.getNumber())) {
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE,"工号{number} 不允许为空！",null);
            }
            if (StringUtils.isBlank(employeeDTO.getName())) {
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE,"姓名{name} 不允许为空！",null);
            }
            if (StringUtils.isBlank(employeeDTO.getPassword())) {
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE,"密码{password} 不允许为空！",null);
            }
            if (StringUtils.isBlank(employeeDTO.getPositionType())) {
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE,"职位{postionType} 不允许为空！",null);
            }
            if(null == employeeDTO.getStatus()){
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE,"状态{status} 不允许为空！",null);
            }
            String password = Base64Utils.decode(employeeDTO.getPassword());
            AppUser appUser = new AppUser();
            appUser.setLoginName(employeeDTO.getNumber());
            appUser.setName(employeeDTO.getName());
            appUser.setMobile(employeeDTO.getMobile());
            appUser.setBirthday(employeeDTO.getBirthday());
            appUser.setSex(employeeDTO.getSex() ? SexType.MALE :SexType.FEMALE);
            appUser.setStatus(employeeDTO.getStatus() != 0);
            appUser.setUserType(AppUserType.SELLER);
            String salt = appUser.generateSalt();
            appUser.setSalt(salt);
            try {
                String md5Password = DigestUtils.md5DigestAsHex((password + salt).getBytes("UTF-8"));
                appUser.setPassword(md5Password);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                logger.warn("员工{}密码生成失败，请重新同步！",employeeDTO.getNumber());
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE,"员工密码生成失败！",null);
            }
            appUserService.save(appUser);
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS,null,null);
        }
        return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "员工信息为空！", null);
    }
}

package cn.com.leyizhuang.app.remote;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.core.constant.AppSellerType;
import cn.com.leyizhuang.app.core.constant.SexType;
import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.pojo.AppEmployee;
import cn.com.leyizhuang.app.foundation.pojo.City;
import cn.com.leyizhuang.app.foundation.service.AppEmployeeService;
import cn.com.leyizhuang.app.foundation.service.CityService;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.core.utils.Base64Utils;
import cn.com.leyizhuang.common.foundation.pojo.dto.HqAppEmployeeDTO;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
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

    @Resource
    private AppEmployeeService employeeService;

    @Resource
    private CityService cityService;


    @PostMapping(value = "sync")
    public ResultDTO<String> employeeSync(@RequestBody HqAppEmployeeDTO employeeDTO) {
        if (null != employeeDTO) {
            if (StringUtils.isBlank(employeeDTO.getNumber())) {
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "工号{number} 不允许为空！", null);
            }
            if (StringUtils.isBlank(employeeDTO.getName())) {
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "姓名{name} 不允许为空！", null);
            }
            if (StringUtils.isBlank(employeeDTO.getPassword())) {
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "密码{password} 不允许为空！", null);
            }
            if (StringUtils.isBlank(employeeDTO.getPositionType())) {
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "职位{positionType} 不允许为空！", null);
            }
            if (!employeeDTO.getPositionType().equalsIgnoreCase("DG") &&
                    !employeeDTO.getPositionType().equalsIgnoreCase("DZ") &&
                    !employeeDTO.getPositionType().equalsIgnoreCase("DJL")) {
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "职位类型不在约定范围之内！", null);
            }
            if(StringUtils.isBlank(employeeDTO.getCityNumber())){
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "员工城市编码{cityNumber}不允许为空！", null);
            }
            if (null == employeeDTO.getStatus()) {
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "状态{status} 不允许为空！", null);
            }
            String password = Base64Utils.decode(employeeDTO.getPassword());
            AppEmployee employee = new AppEmployee();
            employee.setLoginName(employeeDTO.getNumber());
            employee.setName(employeeDTO.getName());
            employee.setMobile(employeeDTO.getMobile());
            employee.setBirthday(employeeDTO.getBirthday());
            employee.setSex(employeeDTO.getSex() ? SexType.MALE : SexType.FEMALE);
            employee.setStatus(employeeDTO.getStatus() != 0);
            switch (employeeDTO.getPositionType()) {
                case "DG":
                    employee.setIdentityType(AppIdentityType.SELLER);
                    employee.setSellerType(AppSellerType.SELLER);
                    break;
                case "DZ":
                    employee.setIdentityType(AppIdentityType.SELLER);
                    employee.setSellerType(AppSellerType.SUPERVISOR);
                    break;
                case "DJL":
                    employee.setIdentityType(AppIdentityType.SELLER);
                    employee.setSellerType(AppSellerType.MANAGER);
                    break;
                case "PSY":
                    employee.setIdentityType(AppIdentityType.DELIVERY_CLERK);
                    break;
                case"ZSJL":
                    employee.setIdentityType(AppIdentityType.DECORATE_MANAGER);
                    break;
                case"ZSGR":
                    employee.setIdentityType(AppIdentityType.DECORATE_EMPLOYEE);
                    break;
            }
            City city = cityService.findByCityNumber(employeeDTO.getCityNumber());
            employee.setCityId(city.getCityId());
            String salt = employee.generateSalt();
            employee.setSalt(salt);
            try {
                String md5Password = DigestUtils.md5DigestAsHex((password + salt).getBytes("UTF-8"));
                employee.setPassword(md5Password);
                employeeService.save(employee);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                logger.warn("员工{}密码生成失败，请重新同步！", employeeDTO.getNumber());
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "员工密码生成失败！", null);
            } catch (DuplicateKeyException ex) {
                ex.printStackTrace();
                logger.warn("用户名已存在！");
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户名已存在", null);
            }

            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
        }
        return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "员工信息为空！", null);
    }

    /**
     * 同步修改员工信息
     * @param employeeDTO
     * @return
     */
    @PostMapping(value = "/update")
    public ResultDTO<String> updateEmployee(@RequestBody HqAppEmployeeDTO employeeDTO) {
        logger.warn("updateEmployee CALLED,同步修改员工信息，入参 employeeDTO:{}", employeeDTO);
        if (null != employeeDTO) {
            String password = Base64Utils.decode(employeeDTO.getPassword());
            AppEmployee employee = employeeService.findByLoginName(employeeDTO.getNumber());
            employee.setName(employeeDTO.getName());
            employee.setMobile(employeeDTO.getMobile());
            employee.setBirthday(employeeDTO.getBirthday());
            employee.setSex(employeeDTO.getSex() ? SexType.MALE : SexType.FEMALE);
            employee.setStatus(employeeDTO.getStatus() != 0);
            switch (employeeDTO.getPositionType()) {
                case "DG":
                    employee.setIdentityType(AppIdentityType.SELLER);
                    employee.setSellerType(AppSellerType.SELLER);
                    break;
                case "DZ":
                    employee.setIdentityType(AppIdentityType.SELLER);
                    employee.setSellerType(AppSellerType.SUPERVISOR);
                    break;
                case "DJL":
                    employee.setIdentityType(AppIdentityType.SELLER);
                    employee.setSellerType(AppSellerType.MANAGER);
                    break;
                case "PSY":
                    employee.setIdentityType(AppIdentityType.DELIVERY_CLERK);
                    break;
                case"ZSJL":
                    employee.setIdentityType(AppIdentityType.DECORATE_MANAGER);
                    break;
                case"ZSGR":
                    employee.setIdentityType(AppIdentityType.DECORATE_EMPLOYEE);
                    break;
            }
            City city = cityService.findByCityNumber(employeeDTO.getCityNumber());
            employee.setCityId(city.getCityId());
            String salt = employee.generateSalt();
            employee.setSalt(salt);
            try {
                String md5Password = DigestUtils.md5DigestAsHex((password + salt).getBytes("UTF-8"));
                employee.setPassword(md5Password);
                employeeService.updateByLoginName(employee);
                logger.warn("同步修改员工信息成功！");
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
            }catch (Exception e){
                e.printStackTrace();
                logger.warn("deleteEmployee EXCEPTION,同步修改员工信息失败，出参 resultDTO:{}",e);
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "同步修改员工信息失败！", null);
            }
        }
        logger.warn("员工信息为空！");
        return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "员工信息为空！", null);
    }

    /**
     * 同步删除员工信息
     * @return
     */
    @PostMapping(value = "/delete")
    public ResultDTO<String> deleteEmployee(String loginName) {
        logger.warn("deleteEmployee CALLED,同步删除员工信息，入参 loginName:{}", loginName);
        if (StringUtils.isBlank(loginName)){
            logger.warn("deleteStore OUT,同步删除员工信息失败，出参 loginName:{}", loginName);
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "员工编号为空！", null);
        }
        try{
            employeeService.deleteByLoginName(loginName);
            logger.warn("同步删除员工信息成功！");
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
        }catch (Exception e){
            logger.warn("deleteEmployee EXCEPTION,同步删除员工信息失败，出参 resultDTO:{}",e);
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "未知异常，同步删除员工信息失败！", null);

        }
    }

}

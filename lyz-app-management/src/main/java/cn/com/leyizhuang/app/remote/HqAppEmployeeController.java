package cn.com.leyizhuang.app.remote;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.core.constant.AppSellerType;
import cn.com.leyizhuang.app.core.constant.SexType;
import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.pojo.AppStore;
import cn.com.leyizhuang.app.foundation.pojo.city.City;
import cn.com.leyizhuang.app.foundation.pojo.user.AppEmployee;
import cn.com.leyizhuang.app.foundation.service.AppEmployeeService;
import cn.com.leyizhuang.app.foundation.service.AppStoreService;
import cn.com.leyizhuang.app.foundation.service.CityService;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.core.utils.Base64Utils;
import cn.com.leyizhuang.common.foundation.pojo.dto.HqAppEmployeeDTO;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import cn.com.leyizhuang.common.util.TimeTransformUtils;
import com.esotericsoftware.minlog.Log;
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
@RequestMapping(value = "/remote/employee", produces = "application/json;charset=UTF-8")
public class HqAppEmployeeController {
    private static final Logger logger = LoggerFactory.getLogger(HqAppEmployeeController.class);

    @Resource
    private AppEmployeeService employeeService;

    @Resource
    private CityService cityService;

    @Resource
    private AppStoreService appStoreService;

    @PostMapping(value = "/sync")
    public ResultDTO<String> employeeSync(@RequestBody HqAppEmployeeDTO employeeDTO) {
        logger.warn("employeeSync CALLED,同步新增员工信息，入参 employeeDTO:{}", employeeDTO);
        if (null != employeeDTO) {
            if (StringUtils.isBlank(employeeDTO.getNumber())) {
                logger.warn("employeeSync OUT,同步新增员工信息失败，出参 number:{}", employeeDTO.getNumber());
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "工号{number} 不允许为空！", null);
            }
            if (StringUtils.isBlank(employeeDTO.getName())) {
                logger.warn("employeeSync OUT,同步新增员工信息失败，出参 name:{}", employeeDTO.getName());
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "姓名{name} 不允许为空！", null);
            }
            if (StringUtils.isBlank(employeeDTO.getPassword())) {
                logger.warn("employeeSync OUT,同步新增员工信息失败，出参 password:{}", employeeDTO.getPassword());
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "密码{password} 不允许为空！", null);
            }
            AppEmployee appEmployee = employeeService.findByMobile(employeeDTO.getMobile());
            if (null != appEmployee) {
                logger.warn("employeeSync OUT,同步新增员工信息失败，出参 password:{}", employeeDTO.getPassword());
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "该电话号码已使用", null);
            }
            if ("导购".equals(employeeDTO.getPosition())) {
                if (StringUtils.isBlank(employeeDTO.getPositionType())) {
                    logger.warn("employeeSync OUT,同步新增员工信息失败，身份类型positionType 不允许为空！出参 positionType:{}", employeeDTO.getPositionType());
                    return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "身份类型positionType 不允许为空！", null);
                }
            }
            if (!employeeDTO.getPosition().equalsIgnoreCase("导购") &&
                    !employeeDTO.getPosition().equalsIgnoreCase("装饰公司经理") &&
                    !employeeDTO.getPosition().equalsIgnoreCase("装饰公司员工") &&
                    !employeeDTO.getPosition().equalsIgnoreCase("配送员")) {
                logger.warn("employeeSync OUT,同步新增员工信息失败，出参 positionType:{}", employeeDTO.getPosition());
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "职位类型不在约定范围之内！", null);
            }
            if (StringUtils.isBlank(employeeDTO.getCityNumber())) {
                logger.warn("employeeSync OUT,同步新增员工信息失败，出参 cityNumber:{}", employeeDTO.getCityNumber());
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "员工城市编码{cityNumber}不允许为空！", null);
            }
            if (null == employeeDTO.getStatus()) {
                logger.warn("employeeSync OUT,同步新增员工信息失败，出参 status:{}", employeeDTO.getStatus());
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "状态{status} 不允许为空！", null);
            }
            String password = Base64Utils.decode(employeeDTO.getPassword());
            AppEmployee employee = new AppEmployee();
            employee.setLoginName(employeeDTO.getNumber());
            employee.setName(employeeDTO.getName());
            employee.setMobile(employeeDTO.getMobile());
            if (null != employeeDTO.getBirthday()) {
                employee.setBirthday(employeeDTO.getBirthday());
            }
            employee.setSex(employeeDTO.getSex() ? SexType.MALE : SexType.FEMALE);
            employee.setStatus(employeeDTO.getStatus() != 0);
            employee.setPicUrl(employeeDTO.getPicUrl());
            employee.setManagerId(employeeDTO.getManagerId());
            employee.setCreateTime(TimeTransformUtils.UDateToLocalDateTime(employeeDTO.getCreateTime()));
            if ("导购".equals(employeeDTO.getPosition())) {
                employee.setIdentityType(AppIdentityType.SELLER);
                if ("普通导购".equals(employeeDTO.getPositionType())) {
                    employee.setSellerType(AppSellerType.SELLER);
                } else if ("店长".equals(employeeDTO.getPositionType())) {
                    employee.setSellerType(AppSellerType.SUPERVISOR);
                } else if ("店经理".equals(employeeDTO.getPositionType())) {
                    employee.setSellerType(AppSellerType.MANAGER);
                }
            } else if ("装饰公司经理".equals(employeeDTO.getPosition())) {
                employee.setSellerType(null);
                employee.setIdentityType(AppIdentityType.DECORATE_MANAGER);
            } else if ("装饰公司员工".equals(employeeDTO.getPosition())) {
                employee.setSellerType(null);
                employee.setIdentityType(AppIdentityType.DECORATE_EMPLOYEE);
            } else if ("配送员".equals(employeeDTO.getPosition())) {
                employee.setSellerType(null);
                employee.setIdentityType(AppIdentityType.DELIVERY_CLERK);
                employee.setDeliveryClerkNo(employeeDTO.getDeliveryClerkNo());
            }
            City city = cityService.findByCityNumber(employeeDTO.getCityNumber());
            if (null == city) {
                logger.warn("employeeSync OUT,同步新增员工信息失败，出参 city:{}", city);
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "未查询到此城市,不允许为空！", null);
            }
            AppStore store = appStoreService.findByStoreCode(employeeDTO.getStoreCode());
            if (null == store) {
                logger.warn("employeeSync OUT,同步新增员工信息失败，出参 store:{}", store);
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "未查询到此门店,不允许为空！", null);
            }
            if ("装饰公司经理".equals(employeeDTO.getPosition()) || "装饰公司员工".equals(employeeDTO.getPosition())) {
                if (!"ZS".equals(store.getStoreType().getValue())) {
                    logger.warn("employeeSync OUT,同步新增员工信息失败，出参 store:{}", store);
                    return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "职位类型与门店类型不匹配", null);
                }
            }
            if ("导购".equals(employeeDTO.getPosition()) || "配送员".equals(employeeDTO.getPosition())) {
                if ("ZS".equals(store.getStoreType().getValue())) {
                    logger.warn("employeeSync OUT,同步新增员工信息失败，出参 store:{}", store);
                    return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "职位类型与门店类型不匹配", null);
                }
            }
            employee.setCityId(city.getCityId());
            employee.setStoreId(store.getStoreId());
            if (null != employeeDTO.getManagerNumber()) {
                AppEmployee superios = employeeService.findByLoginName(employeeDTO.getManagerNumber());
                if(null !=superios) {
                    employee.setManagerId(superios.getEmpId());
                }
            }
            String salt = employee.generateSalt();
            employee.setSalt("26d419524d88b85a573f2de536bd63ac");
            try {
                String md5Password = DigestUtils.md5DigestAsHex((password + salt).getBytes("UTF-8"));
                employee.setPassword("ee1ffccda9260a1dda9318f8430877e2");
                employeeService.save(employee);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                logger.warn("员工{}密码生成失败，请重新同步！", employeeDTO.getNumber());
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "员工密码生成失败！", null);
            } catch (DuplicateKeyException ex) {
                ex.printStackTrace();
                logger.warn("用户名或电话已存在！");
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户名或电话已存在", null);
            }
            logger.warn("employeeSync OUT,同步新增员工信息成功，出参 name:{}", employee);
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
        } else {
            logger.warn("employeeSync OUT,同步新增员工信息失败，出参 name:{}", employeeDTO);
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "员工信息为空！", null);
        }
    }

    /**
     * 同步修改员工信息
     *
     * @param employeeDTO
     * @return
     */
    @PostMapping(value = "/update")
    public ResultDTO<String> updateEmployee(@RequestBody HqAppEmployeeDTO employeeDTO) {
        logger.warn("updateEmployee CALLED,同步修改员工信息，入参 employeeDTO:{}", employeeDTO);
        if (null != employeeDTO) {
            Boolean isEmpty;
            City city = cityService.findByCityNumber(employeeDTO.getCityNumber());
            if (null == city) {
                logger.warn("employeeSync OUT,同步修改员工信息失败，出参 city:{}", city);
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "未查询到此城市,不允许为空！", null);
            }
            AppStore store = appStoreService.findByStoreCode(employeeDTO.getStoreCode());
            if (null == store) {
                logger.warn("employeeSync OUT,同步修改员工信息失败，出参 store:{}", store);
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "未查询到此门店,不允许为空！", null);
            }
            //String password = Base64Utils.decode(employeeDTO.getPassword());
            AppEmployee newEmployee = new AppEmployee();
            AppEmployee employee = employeeService.findByMobile(employeeDTO.getMobileBefore());
            if (null == employee) {
                isEmpty = Boolean.FALSE;
                newEmployee.setName(employeeDTO.getName());
                newEmployee.setMobile(employeeDTO.getMobile());
                if (null != employeeDTO.getBirthday()) {
                    newEmployee.setBirthday(employeeDTO.getBirthday());
                }
                newEmployee.setSex(employeeDTO.getSex() ? SexType.MALE : SexType.FEMALE);
                newEmployee.setStatus(employeeDTO.getStatus() != 0);
                newEmployee.setPicUrl(employeeDTO.getPicUrl());
                //newEmployee.setManagerId(employeeDTO.getManagerId());
                newEmployee.setCreateTime(TimeTransformUtils.UDateToLocalDateTime(employeeDTO.getCreateTime()));
                if ("导购".equals(employeeDTO.getPosition())) {
                    newEmployee.setIdentityType(AppIdentityType.SELLER);
                    if ("普通导购".equals(employeeDTO.getPositionType())) {
                        newEmployee.setSellerType(AppSellerType.SELLER);
                    } else if ("店长".equals(employeeDTO.getPositionType())) {
                        newEmployee.setSellerType(AppSellerType.SUPERVISOR);
                    } else if ("店经理".equals(employeeDTO.getPositionType())) {
                        newEmployee.setSellerType(AppSellerType.MANAGER);
                    }
                } else if ("装饰公司经理".equals(employeeDTO.getPosition())) {
                    newEmployee.setIdentityType(AppIdentityType.DECORATE_MANAGER);
                    newEmployee.setSellerType(null);
                } else if ("装饰公司员工".equals(employeeDTO.getPosition())) {
                    newEmployee.setIdentityType(AppIdentityType.DECORATE_EMPLOYEE);
                    newEmployee.setSellerType(null);
                } else if ("配送员".equals(employeeDTO.getPosition())) {
                    newEmployee.setIdentityType(AppIdentityType.DELIVERY_CLERK);
                    newEmployee.setSellerType(null);
                    newEmployee.setDeliveryClerkNo(employeeDTO.getDeliveryClerkNo());
                }
                newEmployee.setLoginName(employeeDTO.getNumber());
                newEmployee.setCityId(city.getCityId());
                newEmployee.setStoreId(store.getStoreId());
                if (null != employeeDTO.getManagerNumber()) {
                    AppEmployee superios = employeeService.findByLoginName(employeeDTO.getManagerNumber());
                    if(null !=superios){
                        newEmployee.setManagerId(superios.getEmpId());
                    }
                }
            } else {
                isEmpty = Boolean.TRUE;
                employee.setName(employeeDTO.getName());
                employee.setMobile(employeeDTO.getMobile());
                if (null != employeeDTO.getBirthday()) {
                    employee.setBirthday(employeeDTO.getBirthday());
                }
                employee.setSex(employeeDTO.getSex() ? SexType.MALE : SexType.FEMALE);
                employee.setStatus(employeeDTO.getStatus() != 0);
                employee.setPicUrl(employeeDTO.getPicUrl());
                employee.setManagerId(employeeDTO.getManagerId());
                employee.setCreateTime(TimeTransformUtils.UDateToLocalDateTime(employeeDTO.getCreateTime()));
                if ("导购".equals(employeeDTO.getPosition())) {
                    employee.setIdentityType(AppIdentityType.SELLER);
                    if ("普通导购".equals(employeeDTO.getPositionType())) {
                        employee.setSellerType(AppSellerType.SELLER);
                    } else if ("店长".equals(employeeDTO.getPositionType())) {
                        employee.setSellerType(AppSellerType.SUPERVISOR);
                    } else if ("店经理".equals(employeeDTO.getPositionType())) {
                        employee.setSellerType(AppSellerType.MANAGER);
                    }
                } else if ("装饰公司经理".equals(employeeDTO.getPosition())) {
                    employee.setIdentityType(AppIdentityType.DECORATE_MANAGER);
                    employee.setSellerType(null);
                } else if ("装饰公司员工".equals(employeeDTO.getPosition())) {
                    employee.setIdentityType(AppIdentityType.DECORATE_EMPLOYEE);
                    employee.setSellerType(null);
                } else if ("配送员".equals(employeeDTO.getPosition())) {
                    employee.setIdentityType(AppIdentityType.DELIVERY_CLERK);
                    employee.setSellerType(null);
                    employee.setDeliveryClerkNo(employeeDTO.getDeliveryClerkNo());
                }
                employee.setLoginName(employeeDTO.getNumber());
                employee.setCityId(city.getCityId());
                employee.setStoreId(store.getStoreId());
                if (null != employeeDTO.getManagerNumber()) {
                    AppEmployee superios = employeeService.findByLoginName(employeeDTO.getManagerNumber());
                    if(null !=superios) {
                        employee.setManagerId(superios.getEmpId());
                    }
                }
            }
            //String salt = employee.generateSalt();
            //employee.setSalt(salt);
            try {
                //String md5Password = DigestUtils.md5DigestAsHex((password + salt).getBytes("UTF-8"));
                //employee.setPassword(md5Password);
                if (isEmpty) {
                    employeeService.update(employee);
                } else {
                    newEmployee.setSalt("26d419524d88b85a573f2de536bd63ac");
                    newEmployee.setPassword("ee1ffccda9260a1dda9318f8430877e2");
                    employeeService.save(newEmployee);
                }
                logger.warn("同步修改员工信息成功！");
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.warn("deleteEmployee EXCEPTION,同步修改员工信息失败，出参 resultDTO:{}", e);
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "同步修改员工信息失败！", null);
            }
        } else

        {
            logger.warn("员工信息为空！");
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "员工信息为空！", null);
        }

    }


    /**
     * 同步重置员工密码
     *
     * @param
     * @return
     */
    @PostMapping(value = "/restPassWord")
    public ResultDTO<String> restPassWord(@RequestBody String mobile) {
        logger.warn("restPassWord CALLED,重置员工密码，入参 mobile:{}", mobile);
        if (null != mobile) {
            AppEmployee employee = employeeService.findByMobile(mobile);
            if (null == employee) {
                logger.warn("employeeSync OUT,同步重置员工密码失败，出参 employee:{}", employee);
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "未找到该员工信息！", null);
            }
            try {
                employee.setSalt("26d419524d88b85a573f2de536bd63ac");
                employee.setPassword("ee1ffccda9260a1dda9318f8430877e2");
                employeeService.update(employee);
                logger.warn("同步重置员工密码成功！");
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.warn("deleteEmployee EXCEPTION,同步重置员工密码失败，出参 resultDTO:{}", e);
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "同步修改员工信息失败！", null);
            }
        } else {
            logger.warn("员工信息为空！");
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "员工信息为空！", null);
        }
    }


    /**
     * 同步删除员工信息
     *
     * @return
     */
    @PostMapping(value = "/delete")
    public ResultDTO<String> deleteEmployee(String loginName) {
        logger.warn("deleteEmployee CALLED,同步删除员工信息，入参 loginName:{}", loginName);
        if (StringUtils.isBlank(loginName)) {
            logger.warn("deleteStore OUT,同步删除员工信息失败，出参 loginName:{}", loginName);
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "员工编号为空！", null);
        }
        try {
            employeeService.deleteByLoginName(loginName);
            logger.warn("同步删除员工信息成功！");
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("deleteEmployee EXCEPTION,同步删除员工信息失败，出参 resultDTO:{}", e);
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "未知异常，同步删除员工信息失败！", null);
        }
    }

}

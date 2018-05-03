package cn.com.leyizhuang.app.web.controller.employee;

import cn.com.leyizhuang.app.core.bean.GridDataVO;
import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.core.constant.AppSystemType;
import cn.com.leyizhuang.app.core.constant.JwtConstant;
import cn.com.leyizhuang.app.core.constant.StorePreDepositChangeType;
import cn.com.leyizhuang.app.core.utils.JwtUtils;
import cn.com.leyizhuang.app.foundation.pojo.message.AppUserDevice;
import cn.com.leyizhuang.app.foundation.pojo.request.EmployeeLoginParam;
import cn.com.leyizhuang.app.foundation.pojo.response.*;
import cn.com.leyizhuang.app.foundation.pojo.user.AppEmployee;
import cn.com.leyizhuang.app.foundation.service.*;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.core.utils.Base64Utils;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import cn.com.leyizhuang.common.util.AssertUtil;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

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
    private AppEmployeeService appEmployeeService;

    @Autowired
    private StorePreDepositLogService storePreDepositLogServiceImpl;

    @Autowired
    private EmployeeCreditMoneyLogService employeeCreditMoneyLogService;

    @Autowired
    private StoreCreditMoneyLogService storeCreditMoneyLogServiceImpl;

    @Autowired
    private StoreSubventionLogService storeSubventionLogServiceImpl;

    @Resource
    private AppUserDeviceService userDeviceService;

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
            if (null == loginParam.getSystemType() || "".equalsIgnoreCase(loginParam.getSystemType())) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "systemType不允许为空！", null);
                logger.info("employeeLogin OUT,员工登录失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == loginParam.getClientId() || "".equalsIgnoreCase(loginParam.getClientId())) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "clientId不允许为空！", null);
                logger.info("employeeLogin OUT,员工登录失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == loginParam.getDeviceId() || "".equalsIgnoreCase(loginParam.getDeviceId())) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "deviceId不允许为空！", null);
                logger.info("employeeLogin OUT,员工登录失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            AppEmployee employee = appEmployeeService.findByLoginName(loginParam.getName());
            if (employee == null) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "没有找到该用户！", null);
                logger.info("employeeLogin OUT,员工登录失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }else if (null != employee.getStatus() && !employee.getStatus()){
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE,"该员工账号已被禁用!",null);
                logger.info("employeeLogin OUT,员工登录失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }else {
                String md5Password = DigestUtils.md5DigestAsHex((Base64Utils.decode(loginParam.getPassword()) + employee.getSalt()).getBytes("UTF-8"));
                if (md5Password.compareTo(employee.getPassword()) != 0) {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "密码错误！", null);
                    logger.info("employeeLogin OUT,员工登录失败，出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }
            }
            AppUserDevice device = userDeviceService.findByClientIdAndDeviceIdAndUserIdAndIdentityType(loginParam.getClientId(), loginParam.getDeviceId(), employee.getEmpId(), employee.getIdentityType());
            if (null == device) {
                device = new AppUserDevice(null, employee.getEmpId(), employee.getIdentityType(), AppSystemType.getAppSystemTypeByValue(loginParam.getSystemType()),
                        loginParam.getClientId(), loginParam.getDeviceId(), new Date(), new Date());
                userDeviceService.addUserDevice(device);
            } else {
                device.setLastLoginTime(new Date());
                userDeviceService.updateLastLoginTime(device);
            }
            //拼装accessToken
            String accessToken = JwtUtils.createJWT(String.valueOf(employee.getEmpId()), String.valueOf(employee.getLoginName()),
                    JwtConstant.EXPPIRES_SECOND * 1000);
            System.out.println(accessToken);
            response.setHeader("token", accessToken);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null,
                    new EmployeeLoginResponse(employee.getIdentityType().getValue(), null == employee.getSellerType() ? "" : employee.getSellerType().getValue(),
                            employee.getEmpId(), employee.getCityId()));
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
                String md5Password = DigestUtils.md5DigestAsHex((Base64Utils.decode(password) +
                        employee.getSalt()).getBytes("UTF-8"));
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
     *
     * @param userId
     * @param identityType
     * @return
     */
    @PostMapping(value = "/creditMoney/balance", produces = "application/json;charset=UTF-8")
    public ResultDTO<SellerCreditMoneyResponse> getGuideCreditMoneyBalance(Long userId, Integer identityType) {

        logger.info("getGuideCreditMoneyBalance CALLED,获取导购信用金余额，入参 userId {},identityType{}", userId, identityType);

        ResultDTO<SellerCreditMoneyResponse> resultDTO;
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
        Boolean active = appEmployeeService.existsSellerCreditByUserId(userId);
        if (null == active || !active) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "该导购没有开通信用额度",
                    null);
            logger.info("getGuideCreditMoneyBalance OUT,获取导购信用金余额失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            SellerCreditMoneyResponse sellerCreditMoneyResponse = appEmployeeService.findCreditMoneyBalanceByUserIdAndIdentityType(userId, identityType);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, sellerCreditMoneyResponse);
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

    /**
     * @param
     * @return
     * @throws
     * @title 员工修改密码
     * @descripe
     * @author GenerationRoad
     * @date 2017/11/2
     */
    @PostMapping(value = "/password/edit", produces = "application/json;charset=UTF-8")
    public ResultDTO<String> employeeEditPassword(Long userId, Integer identityType, String oldPassword, String newPassword) {
        logger.info("employeeModifyPassword CALLED,员工修改密码，参数: userId{},identityType{},oldPassword{},newPassword{}", userId, identityType, oldPassword, newPassword);
        ResultDTO<String> resultDTO;
        try {
            if (null == userId) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空", null);
                logger.info("employeeEditPassword OUT,获取导购信用金余额失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == identityType || identityType == 6) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型错误！",
                        null);
                logger.info("employeeEditPassword OUT,获取导购信用金余额失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == oldPassword || "".equalsIgnoreCase(oldPassword)) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "原密码不允许为空！", null);
                logger.info("employeeEditPassword OUT,员工修改密码失败，返回值resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == newPassword || "".equalsIgnoreCase(newPassword)) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "新密码不允许为空！", null);
                logger.info("employeeEditPassword OUT,员工修改密码失败，返回值resultDTO:{}", resultDTO);
                return resultDTO;
            }
            AppEmployee employee = appEmployeeService.findByIdAndStatusIsTrue(userId);
            if (employee == null) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "该用户不存在！", null);
                logger.info("employeeEditPassword OUT,员工修改密码失败，返回值resultDTO:{}", resultDTO);
                return resultDTO;
            } else {
                String md5OldPassword = DigestUtils.md5DigestAsHex((Base64Utils.decode(oldPassword) + employee.getSalt()).getBytes("UTF-8"));
                if (md5OldPassword.compareTo(employee.getPassword()) != 0) {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "原密码错误！", null);
                    logger.info("employeeLogin OUT,员工登录失败，出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }
                String md5NewPassword = DigestUtils.md5DigestAsHex((Base64Utils.decode(newPassword) + employee.getSalt()).getBytes("UTF-8"));
                AppEmployee newEmployee = new AppEmployee();
                newEmployee.setEmpId(employee.getEmpId());
                newEmployee.setPassword(md5NewPassword);
                appEmployeeService.update(newEmployee);
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
                logger.info("employeeEditPassword OUT,员工修改密码成功，返回值resultDTO:{}", resultDTO);
                return resultDTO;
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知异常，密码修改失败", null);
            logger.warn("employeeEditPassword EXCEPTION,员工修改密码出现未知异常,返回值resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    /**
     * @param
     * @return
     * @throws
     * @title 获取装饰公司钱包充值记录
     * @descripe
     * @author GenerationRoad
     * @date 2017/11/8
     */
    @PostMapping(value = "/PreDeposit/recharge/log", produces = "application/json;charset=UTF-8")
    public ResultDTO getStoreRechargePreDepositLog(Long userId, Integer identityType, Integer page, Integer size) {

        logger.info("getStoreRechargePreDepositLog CALLED,获取装饰公司钱包充值记录，入参 userId {},identityType{},page:{},size:{}", userId, identityType, page, size);

        ResultDTO<Object> resultDTO;
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空", null);
            logger.info("getStoreRechargePreDepositLog OUT,获取装饰公司钱包充值记录失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType || identityType != 2) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型错误！",
                    null);
            logger.info("getStoreRechargePreDepositLog OUT,获取装饰公司钱包充值记录失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == page) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "页码不能为空",
                    null);
            logger.info("getStoreRechargePreDepositLog OUT,获取装饰公司钱包充值记录失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == size) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "单页显示条数不能为空",
                    null);
            logger.info("getStoreRechargePreDepositLog OUT,获取装饰公司钱包充值记录失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            List<StorePreDepositChangeType> preDepositChangeTypeList = StorePreDepositChangeType.getRechargeType();
            PageInfo<PreDepositLogResponse> preDepositLogResponseList = this.storePreDepositLogServiceImpl.findByUserIdAndType(userId, preDepositChangeTypeList, page, size);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, new GridDataVO<PreDepositLogResponse>().transform(preDepositLogResponseList));
            logger.info("getStoreRechargePreDepositLog OUT,获取装饰公司钱包充值记录成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，获取装饰公司钱包充值记录失败", null);
            logger.warn("getStoreRechargePreDepositLog EXCEPTION,获取装饰公司钱包充值记录失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    /**
     * @param
     * @return
     * @throws
     * @title 获取装饰公司钱包消费记录
     * @descripe
     * @author GenerationRoad
     * @date 2017/11/7
     */
    @PostMapping(value = "/PreDeposit/consumption/log", produces = "application/json;charset=UTF-8")
    public ResultDTO getStoreConsumptionPreDepositLog(Long userId, Integer identityType, Integer page, Integer size) {

        logger.info("getStoreConsumptionPreDepositLog CALLED, 获取装饰公司钱包消费记录，入参 userId {},identityType{},page:{},size:{}", userId, identityType, page, size);

        ResultDTO<Object> resultDTO;
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空", null);
            logger.info("getStoreConsumptionPreDepositLog OUT, 获取装饰公司钱包消费记录失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType || identityType != 2) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型错误！",
                    null);
            logger.info("getStoreConsumptionPreDepositLog OUT, 获取装饰公司钱包消费记录失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == page) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "页码不能为空",
                    null);
            logger.info("getStoreConsumptionPreDepositLog OUT,获取装饰公司钱包消费记录失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == size) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "单页显示条数不能为空",
                    null);
            logger.info("getStoreConsumptionPreDepositLog OUT,获取装饰公司钱包消费记录失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            List<StorePreDepositChangeType> preDepositChangeTypeList = StorePreDepositChangeType.getConsumptionType();
            PageInfo<PreDepositLogResponse> preDepositLogResponseList = this.storePreDepositLogServiceImpl.findByUserIdAndType(userId, preDepositChangeTypeList, page, size);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, new GridDataVO<PreDepositLogResponse>().transform(preDepositLogResponseList));
            logger.info("getStoreConsumptionPreDepositLog OUT, 获取装饰公司钱包消费记录成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常， 获取装饰公司钱包消费记录失败", null);
            logger.warn("getStoreConsumptionPreDepositLog EXCEPTION, 获取装饰公司钱包消费记录失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    /**
     * @param
     * @return
     * @throws
     * @title 获取装饰公司信用金变更记录
     * @descripe
     * @author GenerationRoad
     * @date 2017/11/27
     */
    @PostMapping(value = "/creditMoney/log", produces = "application/json;charset=UTF-8")
    public ResultDTO getStoreCreditMoneyLog(Long userId, Integer identityType, Integer page, Integer size) {

        logger.info("getStoreCreditMoneyLog CALLED, 获取装饰公司信用金变更记录，入参 userId {},identityType{}, page{}, size{}", userId, identityType, page, size);

        ResultDTO<Object> resultDTO;
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空", null);
            logger.info("getStoreCreditMoneyLog OUT, 获取装饰公司信用金变更记录失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == size) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "单页显示条数不能为空",
                    null);
            logger.info("getCustomerSignDetail OUT,获取装饰公司信用金变更记录失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType || identityType != 2) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型错误！",
                    null);
            logger.info("getStoreCreditMoneyLog OUT, 获取装饰公司信用金变更记录失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            PageInfo<StoreCreditMoneyLogResponse> storeCreditMoneyLogResponseList = this.storeCreditMoneyLogServiceImpl.findByUserId(userId, page, size);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, new GridDataVO<StoreCreditMoneyLogResponse>().transform(storeCreditMoneyLogResponseList));
            logger.info("getStoreCreditMoneyLog OUT, 获取装饰公司信用金变更记录成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常， 获取装饰公司信用金变更记录失败", null);
            logger.warn("getStoreCreditMoneyLog EXCEPTION, 获取装饰公司信用金变更记录失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    /**
     * 获取导购信用金变更记录
     *
     * @param userId
     * @param identityType
     * @return
     */
    @PostMapping(value = "/seller/creditMoney/log", produces = "application/json;charset=UTF-8")
    public ResultDTO getSellerCreditMoneyLog(Long userId, Integer identityType, Integer page, Integer size) {

        logger.info("getSellerCreditMoneyLog CALLED, 获取导购信用金变更记录，入参 userId {},identityType{}, page{}, size{}", userId, identityType, page, size);

        ResultDTO<Object> resultDTO;
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空", null);
            logger.info("getSellerCreditMoneyLog OUT, 获取导购信用金变更记录失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == page) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "页码不能为空",
                    null);
            logger.info("getCustomerSignDetail OUT,获取导购信用金变更记录失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == size) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "单页显示条数不能为空",
                    null);
            logger.info("getCustomerSignDetail OUT,获取导购信用金变更记录失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType || identityType != 0) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型错误！", null);
            logger.info("getSellerCreditMoneyLog OUT, 获取导购信用金变更记录失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        Boolean hasCredit = appEmployeeService.existsSellerCreditByUserId(userId);
        if (!hasCredit) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "该导购未开通信用额度！", null);
            logger.info("getSellerCreditMoneyLog OUT, 获取导购信用金变更记录失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            PageInfo<EmployeeCreditMoneyLogResponse> employeeCreditMoneyLogResponseList = this.employeeCreditMoneyLogService.findAllEmployeeCreditMoneyLogByUserId(userId, page, size);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null,
                    AssertUtil.isNotEmpty(employeeCreditMoneyLogResponseList) ? new GridDataVO<EmployeeCreditMoneyLogResponse>().transform(employeeCreditMoneyLogResponseList) : null);
            logger.info("getSellerCreditMoneyLog OUT, 获取导购信用金变更记录成功，出参 resultDTO:{}", employeeCreditMoneyLogResponseList.getList());
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常， 获取导购信用金变更记录失败！", null);
            logger.warn("getSellerCreditMoneyLog EXCEPTION, 获取导购信用金变更记录失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    /**
     * @param
     * @return
     * @throws
     * @title 获取装饰公司现金返利变更记录
     * @descripe
     * @author GenerationRoad
     * @date 2017/11/27
     */
    @PostMapping(value = "/subvention/log", produces = "application/json;charset=UTF-8")
    public ResultDTO getStoreSubventionLog(Long userId, Integer identityType, Integer page, Integer size) {

        logger.info("getStoreSubventionLog CALLED, 获取装饰公司现金返利变更记录，入参 userId {},identityType{},page:{},size:{}", userId, identityType, page, size);

        ResultDTO<Object> resultDTO;
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空", null);
            logger.info("getStoreSubventionLog OUT, 获取装饰公司现金返利变更记录失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType || identityType != 2) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型错误！",
                    null);
            logger.info("getStoreSubventionLog OUT, 获取装饰公司现金返利变更记录失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == page) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "页码不能为空",
                    null);
            logger.info("getStoreSubventionLog OUT,获取装饰公司现金返利变更记录失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == size) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "单页显示条数不能为空",
                    null);
            logger.info("getStoreSubventionLog OUT,获取装饰公司现金返利变更记录失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            PageInfo<StoreSubventionLogResponse> storeSubventionLogResponseList = this.storeSubventionLogServiceImpl.findByUserId(userId, page, size);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, new GridDataVO<StoreSubventionLogResponse>().transform(storeSubventionLogResponseList));
            logger.info("getStoreSubventionLog OUT, 获取装饰公司现金返利变更记录成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常， 获取装饰公司现金返利变更记录失败", null);
            logger.warn("getStoreSubventionLog EXCEPTION, 获取装饰公司现金返利变更记录失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    /**
     * 获取导购二维码
     *
     * @param userID       用户id
     * @param identityType 用户类型
     * @return 返回二维码
     */
    @PostMapping(value = "/qr/code", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> getQrCodeByUserID(Long userID, Integer identityType) {
        logger.info("getQrCodeByUserID CALLED, 获取导购二维码，入参 userID {},identityType{}", userID, identityType);
        ResultDTO<Object> resultDTO;

        if (null == userID) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空！", null);
            logger.info("getQrCodeByUserID OUT, 获取导购二维码失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空！",
                    null);
            logger.info("getQrCodeByUserID OUT, 获取导购二维码失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (identityType != 0) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型错误！",
                    null);
            logger.info("getQrCodeByUserID OUT, 获取导购二维码失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }

        try {
            String qrCode = appEmployeeService.getQrCodeByUserID(userID);
            QrCodeResponse qrCodeResponse = new QrCodeResponse();
            qrCodeResponse.setQrCode(qrCode);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, qrCodeResponse);
            logger.info("getQrCodeByUserID OUT, 获取导购二维码成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常， 获取导购二维码失败", null);
            logger.warn("getQrCodeByUserID EXCEPTION, 获取导购二维码失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }


    /**
     * @title   获取导购门店所有专供类型
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2018/5/2
     */
    @PostMapping(value = "/store/rankClassification", produces = "application/json;charset=UTF-8")
    public ResultDTO getStoreRankClassification(Long userId, Integer identityType) {
        logger.info("getStoreRankClassification CALLED,获取导购门店所有专供类型，入参 userId {},identityType{}", userId, identityType);

        ResultDTO<Object> resultDTO;
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空", null);
            logger.info("getStoreRankClassification OUT,获取导购门店所有专供类型失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType || identityType != 0) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "此用户身份不支持此功能！",
                    null);
            logger.info("getStoreRankClassification OUT,获取导购门店所有专供类型失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            List<StoreRankClassification> rankClassifications = this.appEmployeeService.getStoreRankClassification(userId, AppIdentityType.getAppIdentityTypeByValue(identityType));
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, rankClassifications);
            logger.info("getStoreRankClassification OUT,获取导购门店所有专供类型成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，获取导购门店所有专供类型失败", null);
            logger.warn("getStoreRankClassification EXCEPTION,获取导购门店所有专供类型失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }
}

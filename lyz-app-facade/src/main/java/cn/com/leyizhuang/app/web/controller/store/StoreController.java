package cn.com.leyizhuang.app.web.controller.store;

import cn.com.leyizhuang.app.core.bean.GridDataVO;
import cn.com.leyizhuang.app.core.constant.StorePreDepositChangeType;
import cn.com.leyizhuang.app.foundation.pojo.response.PreDepositLogResponse;
import cn.com.leyizhuang.app.foundation.service.AppEmployeeService;
import cn.com.leyizhuang.app.foundation.service.AppStoreService;
import cn.com.leyizhuang.app.foundation.service.StorePreDepositLogService;
import cn.com.leyizhuang.app.web.controller.user.UserHomePageController;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Created by Jerry.Ren
 * Date: 2017/10/13.
 * Time: 9:31.
 */
@RestController
@RequestMapping(value = "/app/store")
public class StoreController {

    private static final Logger logger = LoggerFactory.getLogger(UserHomePageController.class);

    @Resource
    private AppStoreService appStoreService;

    @Autowired
    private StorePreDepositLogService storePreDepositLogService;

    @Autowired
    private AppEmployeeService appEmployeeService;

    /**
     * 获取门店赞助金余额
     *
     * @param userId
     * @return
     */
    @PostMapping(value = "/subvention/balance", produces = "application/json;charset=UTF-8")
    public ResultDTO getStoreSubventionBalance(Long userId, Integer identityType) {

        logger.info("getStoreSubventionBalance CALLED,获取门店赞助金，入参 userId {},identityType{}", userId, identityType);

        ResultDTO resultDTO;
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空", null);
            logger.info("getStoreSubventionBalance OUT,获取门店赞助金失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空或没有权限",
                    null);
            logger.info("getStoreSubventionBalance OUT,获取门店赞助金失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            if (identityType == 2) {
                Double balance = appStoreService.findSubventionBalanceByUserId(userId);
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, balance);
                logger.info("getStoreSubventionBalance OUT,获取门店赞助金成功，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            } else {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "没有权限",
                        null);
                logger.info("getStoreSubventionBalance OUT,获取门店赞助金失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，获取门店赞助金失败", null);
            logger.warn("getStoreSubventionBalance EXCEPTION,获取门店赞助金失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    /**
     * 获取门店信用金余额
     *
     * @param userId
     * @return
     */
    @PostMapping(value = "/creditMoney/balance", produces = "application/json;charset=UTF-8")
    public ResultDTO getStoreCreditMoneyBalance(Long userId, Integer identityType) {

        logger.info("getStoreCreditMoneyBalance CALLED,获取门店信用金，入参 userId {},identityType{}", userId, identityType);

        ResultDTO resultDTO;
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空", null);
            logger.info("getStoreCreditMoneyBalance OUT,获取门店信用金失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空",
                    null);
            logger.info("getStoreCreditMoneyBalance OUT,获取门店信用金失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            if (identityType == 2) {
                Double balance = appStoreService.findCreditMoneyBalanceByUserId(userId);
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, balance);
                logger.info("getStoreCreditMoneyBalance OUT,获取门店信用金成功，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            } else {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "没有权限",
                        null);
                logger.info("getStoreCreditMoneyBalance OUT,获取门店信用金失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，获取门店信用金失败", null);
            logger.warn("getStoreCreditMoneyBalance EXCEPTION,获取门店信用金失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    /**
     * 获取门店预存款余额
     *
     * @param userId
     * @return
     */
    @PostMapping(value = "/preDeposit/balance", produces = "application/json;charset=UTF-8")
    public ResultDTO getStoreRechargePreDepositBalance(Long userId, Integer identityType) {

        logger.info("getStorePreDepositBalance CALLED,获取门店预存款余额，入参 userId {},identityType{}", userId, identityType);

        ResultDTO resultDTO;
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空", null);
            logger.info("getStorePreDepositBalance OUT,获取门店预存款余额失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空",
                    null);
            logger.info("getStorePreDepositBalance OUT,获取门店预存款余额失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            if (identityType == 2) {
                Double balance = appStoreService.findPreDepositBalanceByUserId(userId);
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, balance);
                logger.info("getStorePreDepositBalance OUT,获取门店预存款余额成功，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            } else {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "没有权限",
                        null);
                logger.info("getStorePreDepositBalance OUT,获取门店预存款余额失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，获取门店预存款余额失败", null);
            logger.warn("getStorePreDepositBalance EXCEPTION,获取门店预存款余额失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }


    /**
     * 获取门店预存款充值明细
     * @param userId
     * @param identityType
     * @return
     */
    @PostMapping(value = "/preDeposit/recharge/log", produces = "application/json;charset=UTF-8")
    public ResultDTO getStoreRechargePreDepositLog(Long userId, Integer identityType,Integer page, Integer size) {

        logger.info("getStoreRechargePreDepositLog CALLED,获取门店钱包充值记录，入参 userId {},identityType{},page:{}, size:{}", userId, identityType,page,size);

        ResultDTO<Object> resultDTO;
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空", null);
            logger.info("getStoreRechargePreDepositLog OUT,获取门店钱包钱包充值记录失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if ( null==identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空！",
                    null);
            logger.info("getStoreRechargePreDepositLog OUT,获取门店钱包钱包充值记录失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == page) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "页码不能为空",
                    null);
            logger.info("getStoreRechargePreDepositLog OUT,获取门店钱包钱包充值记录失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == size) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "单页显示条数不能为空",
                    null);
            logger.info("getStoreRechargePreDepositLog OUT,获取门店钱包钱包充值记录失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            if(identityType == 0&&"SUPERVISOR".equals(appEmployeeService.isSupervisor(userId))){
                List<StorePreDepositChangeType> preDepositChangeTypeList = StorePreDepositChangeType.getRechargeType();
                PageInfo<PreDepositLogResponse> preDepositLogResponseList = this.storePreDepositLogService.findPreDepositChangeLog(userId, preDepositChangeTypeList, page, size);
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, new GridDataVO<PreDepositLogResponse>().transform(preDepositLogResponseList));
                logger.info("getStoreRechargePreDepositLog OUT,获取门店钱包钱包充值记录成功，出参 resultDTO:{}", resultDTO);
            }else {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "没有权限",
                        null);
                logger.info("getStorePreDepositBalance OUT,获取门店预存款余额失败，出参 resultDTO:{}", resultDTO);
            }
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，获取门店钱包充值记录失败", null);
            logger.warn("getStoreRechargePreDepositLog EXCEPTION,获取门店公司钱包充值记录失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    /**
     * @param
     * @return
     * @throws
     * @title 获取门店预存款消费记录
     * @descripe
     */
    @PostMapping(value = "/preDeposit/consumption/log", produces = "application/json;charset=UTF-8")
    public ResultDTO getStoreConsumptionPreDepositLog(Long userId, Integer identityType,Integer page, Integer size) {

        logger.info("getStoreConsumptionPreDepositLog CALLED, 获取门店钱包消费记录，入参 userId {},identityType{},page:{}, size:{}", userId, identityType,page,size);

        ResultDTO<Object> resultDTO;
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空", null);
            logger.info("getStoreConsumptionPreDepositLog OUT, 获取门店钱包消费记录失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if ( null==identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空！",
                    null);
            logger.info("getStoreConsumptionPreDepositLog OUT,获取门店钱包消费记录失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == page) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "页码不能为空",
                    null);
            logger.info("getStoreConsumptionPreDepositLog OUT,获取门店钱包消费记录失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == size) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "单页显示条数不能为空",
                    null);
            logger.info("getStoreConsumptionPreDepositLog OUT,获取门店钱包消费记录失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            if(identityType == 0&&"SUPERVISOR".equals(appEmployeeService.isSupervisor(userId))) {
                List<StorePreDepositChangeType> preDepositChangeTypeList = StorePreDepositChangeType.getConsumptionType();
                PageInfo<PreDepositLogResponse> preDepositLogResponseList = this.storePreDepositLogService.findPreDepositChangeLog(userId, preDepositChangeTypeList,page, size);
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, new GridDataVO<PreDepositLogResponse>().transform(preDepositLogResponseList));
                logger.info("getStoreConsumptionPreDepositLog OUT, 获取门店钱包消费记录成功，出参 resultDTO:{}", resultDTO);
            }else{
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "没有权限",
                        null);
                logger.info("getStorePreDepositBalance OUT,获取门店预存款余额失败，出参 resultDTO:{}", resultDTO);
            }
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常， 获取门店钱包消费记录失败", null);
            logger.warn("getStoreConsumptionPreDepositLog EXCEPTION, 获取门店钱包消费记录失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    /**
     * @param
     * @return
     * @throws
     * @title 获取门店返利记录
     * @descripe
     */

    @PostMapping(value = "/rebate/log", produces = "application/json;charset=UTF-8")
    public ResultDTO getStoreRebateLog(Long userId, Integer identityType,Integer page, Integer size) {

        logger.info("getStoreRebateLog CALLED, 获取门店返利记录，入参 userId {},identityType{},page:{}, size:{}", userId, identityType,page,size);

        ResultDTO<Object> resultDTO;
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空", null);
            logger.info("getStoreRebateLog OUT, 获取门店返利记录失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if ( null==identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空！",
                    null);
            logger.info("getStoreRebateLog OUT,获取门店返利记录失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == page) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "页码不能为空",
                    null);
            logger.info("getStoreRebateLog OUT,获取门店返利记录失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == size) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "单页显示条数不能为空",
                    null);
            logger.info("getStoreRebateLog OUT,获取门店返利记录失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {

            if(identityType == 0 && "SUPERVISOR".equals(appEmployeeService.isSupervisor(userId))) {
                List<StorePreDepositChangeType> reabateTypeList = StorePreDepositChangeType.getRebateType();
                PageInfo<PreDepositLogResponse> preDepositLogResponseList = this.storePreDepositLogService.findPreDepositChangeLog(userId, reabateTypeList,page, size);
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, new GridDataVO<PreDepositLogResponse>().transform(preDepositLogResponseList));
                logger.info("getStoreRebateLog OUT, 获取门店返利记录成功，出参 resultDTO:{}", resultDTO);
            }else{
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "没有权限",
                        null);
                logger.info("getStoreRebateLog OUT,获取门店返利记录失败，出参 resultDTO:{}", resultDTO);
            }
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常， 获取门店返利记录失败", null);
            logger.warn("getStoreRebateLog EXCEPTION, 获取门店返利记录失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }


    /**
     * 获取门店预存款余额(无装饰公司)
     *
     * @param userId
     * @return
     */
    @PostMapping(value = "/preDepositBalance", produces = "application/json;charset=UTF-8")
    public ResultDTO getStorePreDepositBalance(Long userId, Integer identityType) {

        logger.info("getStorePreDepositBalance CALLED,获取门店预存款余额，入参 userId {},identityType{}", userId, identityType);

        ResultDTO resultDTO;
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空", null);
            logger.info("getStorePreDepositBalance OUT,获取门店预存款余额失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空",
                    null);
            logger.info("getStorePreDepositBalance OUT,获取门店预存款余额失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            if (identityType == 0) {
                //判断是否为店长
                if("SUPERVISOR".equals(appEmployeeService.isSupervisor(userId))){
                    Double balance = appStoreService.findPreDepositBalanceByUserId(userId);
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, balance);
                    logger.info("getStorePreDepositBalance OUT,获取门店预存款余额成功，出参 resultDTO:{}", resultDTO);
                }else{
                    logger.info("getStorePreDepositBalance OUT,获取门店预存款余额失败，没有权限");
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "没有权限",null);
                }
                return resultDTO;
            } else {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "没有权限",
                        null);
                logger.info("getStorePreDepositBalance OUT,获取门店预存款余额失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
        }catch(Exception e){
             e.printStackTrace();
             resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，获取门店预存款余额失败", null);
             logger.warn("getStorePreDepositBalance EXCEPTION,获取门店预存款余额失败，出参 resultDTO:{}", resultDTO);
             logger.warn("{}", e);
             return resultDTO;
        }
    }
}

package cn.com.leyizhuang.app.web.controller.store;

import cn.com.leyizhuang.app.foundation.service.AppStoreService;
import cn.com.leyizhuang.app.web.controller.user.UserHomePageController;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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
            if (identityType == 0 || identityType == 2) {
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
     * 获取门店充值明细
     *
     * @param userId
     * @return
     */
/*    @PostMapping(value = "/preDeposit/recharge/log", produces = "application/json;charset=UTF-8")
    public ResultDTO getStoreRechargePreDepositLog(Long userId, Integer identityType) {

        logger.info("getStoreRechargePreDepositLog CALLED,获取门店钱包充值记录，入参 userId {},identityType{}", userId, identityType);

        ResultDTO<Object> resultDTO;
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空", null);
            logger.info("getStoreRechargePreDepositLog OUT,获取装饰公司钱包充值记录失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType || identityType == 6 || identityType == 1 || identityType == 3) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型错误！",
                    null);
            logger.info("getStoreRechargePreDepositLog OUT,获取装饰公司钱包充值记录失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            List<StorePreDepositChangeType> preDepositChangeTypeList = StorePreDepositChangeType.getRechargeType();
            List<PreDepositLogResponse> preDepositLogResponseList = this.storePreDepositLogServiceImpl.findByUserIdAndType(userId, preDepositChangeTypeList);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, preDepositLogResponseList);
            logger.info("getStoreRechargePreDepositLog OUT,获取装饰公司钱包充值记录成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，获取装饰公司钱包充值记录失败", null);
            logger.warn("getStoreRechargePreDepositLog EXCEPTION,获取装饰公司钱包充值记录失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }*/
}

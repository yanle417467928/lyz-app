package cn.com.leyizhuang.app.web.controller.store;

import cn.com.leyizhuang.app.foundation.service.IAppStoreService;
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
    private IAppStoreService appStoreService;

    /**
     * 获取门店赞助金余额
     * @param storeId
     * @return
     */
    @PostMapping(value = "/subvention", produces = "application/json;charset=UTF-8")
    public ResultDTO getStoreSubventionBalance(Long storeId){

        logger.info("getStoreSubventionBalance CALLED,获取门店赞助金，入参 storeId{}", storeId);

        ResultDTO resultDTO;
        if (storeId == null) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "门店id不能为空！", null);
            logger.info("getStoreSubventionBalance OUT,获取门店赞助金失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            Double balance = appStoreService.findSubventionBalanceByStoreId(storeId);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, balance);
            logger.info("getStoreSubventionBalance OUT,获取门店赞助金成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
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
     * @param storeId
     * @return
     */
    @PostMapping(value = "/creditMoney", produces = "application/json;charset=UTF-8")
    public ResultDTO getStoreCreditMoneyBalance(Long storeId){

        logger.info("getStoreCreditMoneyBalance CALLED,获取门店信用金，入参 storeId{}", storeId);

        ResultDTO resultDTO;
        if (storeId == null) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "门店id不能为空！", null);
            logger.info("getStoreCreditMoneyBalance OUT,获取门店信用金失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            Double balance = appStoreService.findCreditMoneyBalanceByStoreId(storeId);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, balance);
            logger.info("getStoreCreditMoneyBalance OUT,获取门店信用金成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
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
     * @param storeId
     * @return
     */
    @PostMapping(value = "/preDeposit", produces = "application/json;charset=UTF-8")
    public ResultDTO getStorePreDepositBalance(Long storeId) {

        logger.info("getStorePreDepositBalance CALLED,获取门店预存款余额，入参 storeId{}", storeId);

        ResultDTO resultDTO;
        if (storeId == null) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "门店id不能为空！", null);
            logger.info("getStorePreDepositBalance OUT,获取门店预存款余额失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            Double balance = appStoreService.findPreDepositBalanceByStoreId(storeId);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, balance);
            logger.info("getStorePreDepositBalance OUT,获取门店预存款余额成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，获取门店预存款余额失败", null);
            logger.warn("getStorePreDepositBalance EXCEPTION,获取门店预存款余额失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }
}

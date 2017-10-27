package cn.com.leyizhuang.app.web.controller.order;

import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.pojo.management.User;
import cn.com.leyizhuang.app.foundation.pojo.request.OrderLockExpendRequest;
import cn.com.leyizhuang.app.foundation.service.AppCustomerService;
import cn.com.leyizhuang.app.foundation.service.AppEmployeeService;
import cn.com.leyizhuang.app.foundation.service.AppStoreService;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 订单相关接口
 *
 * @author Richard
 * Created on 2017-10-23 17:02
 **/
@RestController
@RequestMapping(value = "/app/order")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private AppCustomerService appCustomerService;

    @Autowired
    private AppEmployeeService appEmployeeService;

    @Autowired
    private AppStoreService appStoreService;

    @PostMapping(value = "/create", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> createOrder(){
        return null;
    }

    /**
     * 用户锁定订单相关款项和库存
     * @param lockExpendRequest
     * @return
     */
    @PostMapping(value = "/lock", produces = "application/json;charset=UTF-8")
    public ResultDTO lockOrder(OrderLockExpendRequest lockExpendRequest){

        logger.info("lockOrder CALLED,用户锁定订单相关款项和库存，入参 lockExpendRequest:{}", lockExpendRequest );

        ResultDTO resultDTO;
        if (null == lockExpendRequest) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "找不到对象！", null);
            logger.info("lockOrder OUT,用户锁定订单相关款项和库存失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }

        if (null == lockExpendRequest.getUserId()) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空", null);
            logger.info("lockOrder OUT,用户锁定订单相关款项和库存失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == lockExpendRequest.getIdentityType()) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户身份不能为空", null);
            logger.info("lockOrder OUT,用户锁定订单相关款项和库存失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        Long userId = lockExpendRequest.getUserId();
        Integer identityType = lockExpendRequest.getIdentityType();
        try {
            if (null != lockExpendRequest.getCustomerDeposit() && identityType == 6) {

                int result = appCustomerService.lockCustomerDepositByUserIdAndDeposit(
                        userId, lockExpendRequest.getCustomerDeposit());
                if (result == 0) {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "客户预存款余额不足!", null);
                    logger.info("lockOrder OUT,用户锁定订单相关款项和库存失败,客户预存款余额不足，出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }
            }
            if (null != lockExpendRequest.getGuideCredit() && identityType == 0) {

                int result = appEmployeeService.lockGuideCreditByUserIdAndCredit(
                        userId, lockExpendRequest.getGuideCredit());
                if (result == 0) {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "导购信用额度不足!", null);
                    logger.info("lockOrder OUT,用户锁定订单相关款项和库存失败，导购信用额度不足 出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }
            }
            if (null != lockExpendRequest.getStoreDeposit() && (identityType ==0 || identityType ==2)) {

                int result = appStoreService.lockStoreDepositByUserIdAndStoreDeposit(
                        userId, lockExpendRequest.getStoreDeposit());
                if (result == 0) {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户所属门店预存款余额不足!", null);
                    logger.info("lockOrder OUT,用户锁定订单相关款项和库存失败，用户所属门店预存款余额不足 出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }
            }
            if (null != lockExpendRequest.getStoreCredit() && identityType == 2) {

                int result = appStoreService.lockStoreCreditByUserIdAndCredit(
                        userId, lockExpendRequest.getStoreCredit());
                if (result == 0) {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户所属门店信用额度余额不足!", null);
                    logger.info("lockOrder OUT,用户锁定订单相关款项和库存失败，用户所属门店信用额度余额不足 出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }
            }
            if (null != lockExpendRequest.getStoreSubvention() && identityType == 2) {

                int result = appStoreService.lockStoreSubventionByUserIdAndSubvention(
                        userId, lockExpendRequest.getStoreSubvention());
                if (result == 0) {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户所属门店现金返利余额不足!", null);
                    logger.info("lockOrder OUT,用户锁定订单相关款项和库存失败，用户所属门店现金返利余额不足 出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }
            }
            if (null != lockExpendRequest.getLebiQty() && identityType == 6) {

                int result = appCustomerService.lockCustomerLebiByUserIdAndQty(
                        userId, lockExpendRequest.getLebiQty());
                if (result == 0) {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "顾客乐币剩余数量不足!", null);
                    logger.info("lockOrder OUT,用户锁定订单相关款项和库存失败，顾客乐币剩余数量不足 出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }
            }
            if (null != lockExpendRequest.getStoreInventory()) {

                int result = appStoreService.lockStoreInventoryByUserIdAndIdentityTypeAndInventory(
                        userId, identityType, lockExpendRequest.getStoreInventory());
                if (result == 0) {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户所属门店库存不足!", null);
                    logger.info("lockOrder OUT,用户锁定订单相关款项和库存失败，用户所属门店库存不足 出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }
            }
            if (null != lockExpendRequest.getCityInventory()) {

                int result = appStoreService.lockCityInventoryByUserIdAndIdentityTypeAndInventory(
                        userId, identityType, lockExpendRequest.getCityInventory());
                if (result == 0) {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "城市库存不足!", null);
                    logger.info("lockOrder OUT,用户锁定订单相关款项和库存失败，城市库存不足 出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }
            }
            if (null != lockExpendRequest.getProductCoupon() && identityType == 6) {

                int result = appCustomerService.lockCustomerProductCouponByUserIdAndProductCoupon(
                        userId, lockExpendRequest.getProductCoupon());
                if (result == 0) {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "顾客产品券数量不足!", null);
                    logger.info("lockOrder OUT,用户锁定订单相关款项和库存失败，顾客产品券数量不足 出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }
            }
            if (null != lockExpendRequest.getCashCoupon() && identityType == 6) {

                int result = appCustomerService.lockCustomerCashCouponByUserIdAndProductCoupon(
                        userId, identityType, lockExpendRequest.getCashCoupon());
                if (result == 0) {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "顾客现金券数量不足!", null);
                    logger.info("lockOrder OUT,用户锁定订单相关款项和库存失败，顾客现金券数量不足 出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，用户锁定订单相关款项和库存失败", null);
            logger.warn("getGoodsListByUserIdAndIdentityType EXCEPTION,用户锁定订单相关款项和库存失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
        resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "找不到对象！", null);
        logger.info("lockOrder OUT,用户锁定订单相关款项和库存失败，出参 resultDTO:{}", resultDTO);
        return resultDTO;
    }
}

package cn.com.leyizhuang.app.web.controller.settlement;

import cn.com.leyizhuang.app.foundation.pojo.CustomerLeBi;
import cn.com.leyizhuang.app.foundation.pojo.response.OrderUsableProductCouponResponse;
import cn.com.leyizhuang.app.foundation.service.AppCustomerService;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/10/19
 */
@RestController
@RequestMapping("/app/paymentMethods")
public class GetPaymentMethodsController {
    private static final Logger logger = LoggerFactory.getLogger(GetPaymentMethodsController.class);

    @Autowired
    private AppCustomerService appCustomerServiceImpl;

    /**
     * @title   获取付款方式
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2017/10/19
     */
    @PostMapping(value = "/get", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> getPaymentMethods(Long userId, Integer identityType) {
        logger.info("getPaymentMethods CALLED,获取付款方式，入参 userId:{} identityType:{}", userId, identityType);
        ResultDTO<Object> resultDTO;
        try {
            if (null == userId) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "userId不能为空！", null);
                logger.info("getPaymentMethods OUT,获取付款方式失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == identityType || 6 != identityType) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型错误！", null);
                logger.info("getPaymentMethods OUT,获取付款方式失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            Boolean isCashOnDelivery  = this.appCustomerServiceImpl.existsByCustomerId(userId);

            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, isCashOnDelivery);
            logger.info("getPaymentMethods OUT,获取付款方式成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知异常,获取付款方式失败!", null);
            logger.warn("getPaymentMethods EXCEPTION,获取付款方式失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    /**
     * 获取乐币折扣商品金额
     * @param userId 用户ID
     * @param identityType 身份类型
     * @param goodsMoney 需支付商品金额
     * @return
     */
    @PostMapping(value = "/rebate/lebi", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> paymentMethodsOfLeBiRebate(Long userId, Integer identityType,Double goodsMoney) {

        logger.info("paymentMethodsOfLeBiRebate CALLED,乐币折扣商品金额，入参 userId:{},identityType:{},goodsMoney{}", userId, identityType,goodsMoney);

        ResultDTO<Object> resultDTO;
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "userId不能为空！", null);
            logger.info("paymentMethodsOfLeBiRebate OUT,乐币折扣商品金额失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType || 6 != identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型错误！", null);
            logger.info("paymentMethodsOfLeBiRebate OUT,乐币折扣商品金额失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }

        if (null == goodsMoney) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "需支付金额不能为空！", null);
            logger.info("paymentMethodsOfLeBiRebate OUT,乐币折扣商品金额失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            CustomerLeBi customerLeBi = appCustomerServiceImpl.findLeBiByUserIdAndGoodsMoney(userId,goodsMoney);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null,customerLeBi );
            logger.info("paymentMethodsOfLeBiRebate OUT,乐币折扣商品金额成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知异常,乐币折扣商品金额失败!", null);
            logger.warn("paymentMethodsOfLeBiRebate EXCEPTION,乐币折扣商品金额失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }
}

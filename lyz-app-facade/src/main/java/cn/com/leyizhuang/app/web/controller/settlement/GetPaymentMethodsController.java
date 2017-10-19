package cn.com.leyizhuang.app.web.controller.settlement;

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

}

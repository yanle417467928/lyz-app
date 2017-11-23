package cn.com.leyizhuang.app.web.controller.deliveryClerk;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.foundation.pojo.response.PaymentMethodResponse;
import cn.com.leyizhuang.app.foundation.service.PaymentMethodService;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/11/15
 */
@RestController
@RequestMapping(value = "/app/distributionAgent")
public class PaymentMethodController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentMethodController.class);

    @Autowired
    private PaymentMethodService paymentMethodServiceImpl;

    /**
     * @param
     * @return
     * @throws
     * @title 获取配送员收款方式
     * @descripe
     * @author GenerationRoad
     * @date 2017/11/15
     */
    @PostMapping(value = "/list", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> getMethodList(Long userId, Integer identityType, Long cityId) {
        logger.info("getMethodList CALLED,获取配送员收款方式，入参 userId:{} identityType:{} cityId{}", userId, identityType, cityId);
        ResultDTO<Object> resultDTO;
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "userId不能为空！", null);
            logger.info("getMethodList OUT,获取配送员收款方式失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空！", null);
            logger.info("getMethodList OUT,获取配送员收款方式失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == cityId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "城市信息不能为空！", null);
            logger.info("getMethodList OUT,获取配送员收款方式失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }

        List<PaymentMethodResponse> paymentmethods = this.paymentMethodServiceImpl.findByTypeAndCityId(AppIdentityType.getAppIdentityTypeByValue(identityType), cityId);
        resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, paymentmethods);
        logger.info("getMethodList OUT,获取配送员收款方式成功，出参 resultDTO:{}", resultDTO);
        return resultDTO;
    }

}

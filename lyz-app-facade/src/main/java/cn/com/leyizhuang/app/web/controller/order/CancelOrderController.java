package cn.com.leyizhuang.app.web.controller.order;

import cn.com.leyizhuang.app.foundation.pojo.response.OperationReasonsResponse;
import cn.com.leyizhuang.app.foundation.service.OperationReasonsService;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.core.constant.OperationReasonType;
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
 * @date 2017/11/13
 */
@RestController
@RequestMapping(value = "/app/cancelOrder")
public class CancelOrderController {

    private static final Logger logger = LoggerFactory.getLogger(CancelOrderController.class);

    @Autowired
    private OperationReasonsService operationReasonsServiceImpl;


    /**
     * @title   获取取消订单原因列表
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2017/11/13
     */
    @PostMapping(value = "/cancelReasons", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> getCancelReasons(Long userId, Integer identityType) {
        logger.info("getCancelReasons CALLED,获取取消订单原因列表，入参 userId:{} identityType:{}", userId, identityType);
        ResultDTO<Object> resultDTO;
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "userId不能为空！", null);
            logger.info("getCancelReasons OUT,获取取消订单原因列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型错误！", null);
            logger.info("getCancelReasons OUT,获取取消订单原因列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }

        List<OperationReasonsResponse> operationReasonsServiceList = this.operationReasonsServiceImpl.findAllByType(OperationReasonType.CANCEL);
        resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, operationReasonsServiceList);
        logger.info("getCancelReasons OUT,获取取消订单原因列表成功，出参 resultDTO:{}", resultDTO);
        return resultDTO;
    }

}

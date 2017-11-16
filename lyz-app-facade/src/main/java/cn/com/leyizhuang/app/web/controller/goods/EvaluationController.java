package cn.com.leyizhuang.app.web.controller.goods;

import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.pojo.request.OrderGoodsEvaluationRequest;
import cn.com.leyizhuang.app.foundation.service.OrderEvaluationService;
import cn.com.leyizhuang.app.web.controller.MaterialAuditSheetController;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 商品评价接口
 * Created by caiyu on 2017/11/16.
 */
@RestController
@RequestMapping(value = "/app/evaluation")
public class EvaluationController {

    private static final Logger logger = LoggerFactory.getLogger(EvaluationController.class);

    @Resource
    private OrderEvaluationService orderEvaluationService;

    public ResultDTO<Object> addGoodsEvaluation(OrderGoodsEvaluationRequest orderGoodsEvaluationRequest) {
        ResultDTO<Object> resultDTO;
        logger.info("addGoodsEvaluation CALLED,新增商品评价，入参 orderGoodsEvaluationRequest:{}", orderGoodsEvaluationRequest);

        if (null == orderGoodsEvaluationRequest.getUserId()) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空", null);
            logger.info("addGoodsEvaluation OUT,新增商品评价失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == orderGoodsEvaluationRequest.getIdentityType()) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空", null);
            logger.info("addGoodsEvaluation OUT,新增商品评价失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (StringUtils.isBlank(orderGoodsEvaluationRequest.getOrderNumber())) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "订单编号不能为空", null);
            logger.info("addGoodsEvaluation OUT,新增商品评价失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == orderGoodsEvaluationRequest.getProductStar()) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "产品星级不能为空", null);
            logger.info("addGoodsEvaluation OUT,新增商品评价失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == orderGoodsEvaluationRequest.getLogisticsStar()) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "物流星级不能为空", null);
            logger.info("addGoodsEvaluation OUT,新增商品评价失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == orderGoodsEvaluationRequest.getServiceStars()) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "服务星级不能为空", null);
            logger.info("addGoodsEvaluation OUT,新增商品评价失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            //新增商品评价
            orderEvaluationService.addOrderEvaluation(orderGoodsEvaluationRequest);

            return null;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，新增商品评价失败", null);
            logger.warn("addGoodsEvaluation EXCEPTION,新增商品评价失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }
}

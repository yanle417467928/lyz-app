package cn.com.leyizhuang.app.web.controller.orderreturn;

import cn.com.leyizhuang.app.foundation.service.AppOrderService;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Jerry.Ren
 * Notes: 退货单接口
 * Created with IntelliJ IDEA.
 * Date: 2017/12/4.
 * Time: 9:34.
 */

@RestController
@RequestMapping("/app/return")
public class OrderReturnController {

    private static final Logger logger = LoggerFactory.getLogger(OrderReturnController.class);

    @Resource
    private AppOrderService appOrderService;

    /**
     * 获取用户退货单列表
     *
     * @param userId
     * @param identityType
     * @return
     */
    @RequestMapping(value = "/list")
    public ResultDTO getReturnOrderList(Long userId, Integer identityType) {

        logger.info("getReturnOrderList CALLED,获取用户退货单列表，入参 userID:{}, identityType:{}", userId, identityType);

        ResultDTO<Object> resultDTO;
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空", null);
            logger.info("getReturnOrderList OUT,获取用户退货单列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空", null);
            logger.info("getReturnOrderList OUT,获取用户退货单列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
//        List<OrderReturnBaseInfo> baseInfos = appOrderReturnService.findOrderReturnListByUserIdAndIdentityType(userId,identityType);
        resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
        logger.info("getOrderLogisticsResponse OUT,配送员修改物流状态成功，出参 resultDTO:{}", resultDTO);
        return resultDTO;
    }
}

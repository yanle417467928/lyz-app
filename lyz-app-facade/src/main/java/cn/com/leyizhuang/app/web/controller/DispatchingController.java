package cn.com.leyizhuang.app.web.controller;

import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.pojo.response.WaitDeliveryResponse;
import cn.com.leyizhuang.app.foundation.pojo.user.AppEmployee;
import cn.com.leyizhuang.app.foundation.service.AppEmployeeService;
import cn.com.leyizhuang.app.foundation.service.OrderDeliveryInfoDetailsService;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 配送员控制类
 * Created by caiyu on 2017/11/21.
 */
@RestController
@RequestMapping(value = "/app/dispatching")
public class DispatchingController {
    private static final Logger logger = LoggerFactory.getLogger(DispatchingController.class);

    @Resource
    private AppEmployeeService appEmployeeService;

    @Resource
    private OrderDeliveryInfoDetailsService orderDeliveryInfoDetailsService;

    /**
     * 配送员获取待配送列表
     * @param userID    用户id
     * @param identityType  用户类型
     * @return  返回待配送列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> getDispatchingList(Long userID,Integer identityType){
        ResultDTO<Object> resultDTO;
        logger.info("getDispatchingList CALLED,配送员获取待配送列表，入参 userID:{}, identityType:{}", userID, identityType);
        if (null == userID) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空", null);
            logger.info("getDispatchingList OUT,获配送员获取待配送列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空", null);
            logger.info("getDispatchingList OUT,获配送员获取待配送列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (identityType != 1) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "非配送员不能查看待配送列表", null);
            logger.info("getDispatchingList OUT,获配送员获取待配送列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }

        try {
            AppEmployee appEmployee = appEmployeeService.findById(userID);
            if (null == appEmployee){
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "未查到此配送员", null);
                logger.info("getDispatchingList OUT,获配送员获取待配送列表失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (StringUtils.isBlank(appEmployee.getDeliveryClerkNo())){
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "配送员编号为空", null);
                logger.info("getDispatchingList OUT,获配送员获取待配送列表失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            List<WaitDeliveryResponse> waitDeliveryResponseList = orderDeliveryInfoDetailsService.getOrderBeasInfoByOperatorNo(appEmployee.getDeliveryClerkNo());
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, waitDeliveryResponseList);
            logger.info("getDispatchingList OUT,配送员获取待配送列表成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }catch (Exception e){
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，获配送员获取待配送列表失败", null);
            logger.warn("getDispatchingList EXCEPTION,获配送员获取待配送列表失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }
}

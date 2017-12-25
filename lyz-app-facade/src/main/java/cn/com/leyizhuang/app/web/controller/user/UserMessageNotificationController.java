package cn.com.leyizhuang.app.web.controller.user;

import cn.com.leyizhuang.app.foundation.pojo.response.MessageNotificationListResponse;
import cn.com.leyizhuang.app.foundation.service.MessageNotificationService;
import cn.com.leyizhuang.app.foundation.service.OrderDeliveryInfoDetailsService;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author GenerationRoad
 * @date 2017/10/10
 */
@RestController
@RequestMapping("/app/user/message")
public class UserMessageNotificationController {

    private static final Logger logger = LoggerFactory.getLogger(UserSettingController.class);

    @Resource
    private MessageNotificationService messageNotificationService;

    @Resource
    private OrderDeliveryInfoDetailsService orderDeliveryInfoDetailsService;

    /**
     * @param
     * @return
     * @throws
     * @title 消息通知列表
     * @descripe
     * @author GenerationRoad
     * @date 2017/10/10
     */
    @PostMapping(value = "/list", produces = "application/json;charset=UTF-8")
    public ResultDTO<List> getMessageNotificationList(Long userId, Integer identityType) {
        logger.info("getMessageNotificationList CALLED, 获取消息通知列表，入参 userId {},identityType{}", userId, identityType);

        ResultDTO<List> resultDTO;
        if (userId == null) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "userId不能为空！", null);
            logger.info("getMessageNotificationList OUT, 获取消息通知列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }

        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空",
                    null);
            logger.info("getMessageNotificationList OUT,获取消息通知列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        List<MessageNotificationListResponse> messageNotificationListResponseList = this.messageNotificationService.queryListByUserIdAndUserType(userId, identityType);
        for (MessageNotificationListResponse messageNotificationListResponse : messageNotificationListResponseList) {
            //修改已读状态
            messageNotificationListResponse.setIsRead(true);
            messageNotificationService.modifyMessageNotification(messageNotificationListResponse);
        }
        resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, messageNotificationListResponseList);
        logger.info("getMessageNotificationList OUT,获取消息通知列表成功，出参 resultDTO:{}", resultDTO);
        return resultDTO;
    }


    /**
     * 获取用户通消息知数目
     *
     * @param userId
     * @param identityType
     * @return
     */
    @PostMapping(value = "/unread/number", produces = "application/json;charset=UTF-8")
    public ResultDTO getUserUnreadNotifyMessage(Long userId, Integer identityType) {

        logger.info("getUserUnreadNotifyMessage CALLED,获取用户通消息知数目，入参 userId {},identityType{}", userId, identityType);

        ResultDTO resultDTO;
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空", null);
            logger.info("getUserUnreadNotifyMessage OUT,获取用户通消息知数目失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空", null);
            logger.info("getUserUnreadNotifyMessage OUT,获取用户通消息知数目失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            Map<String, Integer> returnMap = new HashMap<>(2);

            int countN = messageNotificationService.countUnreadNotifyMessage(userId, identityType);
            int countL = orderDeliveryInfoDetailsService.countUnreadLogisticsMessage(userId, identityType);

            returnMap.put("notifyMsg", countN);
            returnMap.put("logisticMsg", countL);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, returnMap);
            logger.info("getUserUnreadNotifyMessage OUT,获取用户通消息知数目成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，获取用户通消息知数目失败", null);
            logger.warn("getUserUnreadNotifyMessage EXCEPTION,获取用户通消息知数目失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }
}

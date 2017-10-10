package cn.com.leyizhuang.app.web.controller.user;

import cn.com.leyizhuang.app.foundation.pojo.response.MessageNotificationListResponse;
import cn.com.leyizhuang.app.foundation.service.MessageNotificationService;
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
 * @date 2017/10/10
 */
@RestController
@RequestMapping("/app/user/message")
public class UserMessageNotificationController {

    private static final Logger logger = LoggerFactory.getLogger(UserSettingController.class);

    @Autowired
    private MessageNotificationService messageNotificationServiceImpl;

    /**  
     * @title 消息通知列表
     * @descripe
     * @param
     * @return 
     * @throws 
     * @author GenerationRoad
     * @date 2017/10/10
     */
    @PostMapping(value = "/list", produces = "application/json;charset=UTF-8")
    public ResultDTO<List> getMessageNotificationList(Long userId, Integer identityType){
        logger.info("getMessageNotificationList CALLED, 获取消息通知列表，入参 userId {},identityType{}", userId, identityType);

        ResultDTO<List> resultDTO;
        if (userId == null){
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "userId不能为空！", null);
            logger.info("getMessageNotificationList OUT, 获取消息通知列表失败，出参 resultDTO:{}",resultDTO);
            return resultDTO;
        }

        if (null == identityType){
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空",
                    null);
            logger.info("getMessageNotificationList OUT,获取消息通知列表失败，出参 resultDTO:{}",resultDTO);
            return resultDTO;
        }
        List<MessageNotificationListResponse> messageNotificationListResponseList = this.messageNotificationServiceImpl.queryListByUserIdAndUserType(userId, identityType);
        resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, messageNotificationListResponseList);
        logger.info("getMessageNotificationList OUT,获取消息通知列表成功，出参 resultDTO:{}",resultDTO);
        return resultDTO;
    }

}

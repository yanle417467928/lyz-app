package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.response.MessageNotificationListResponse;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/10/10
 */
public interface MessageNotificationService {

    List<MessageNotificationListResponse> queryListByUserIdAndUserType(Long userId, Integer userType);

    /**
     * 获取未读消息条数
     *
     * @param userId       ID
     * @param identityType 身份
     * @return 未读数
     * @author Jerry
     */
    int countUnreadNotifyMessage(Long userId, Integer identityType);

    /**
     * 修改消息
     *
     * @param messageNotificationListResponse 推送消息返回对象
     */
    void modifyMessageNotification(MessageNotificationListResponse messageNotificationListResponse);


    MessageNotificationListResponse findNotification(Long userId, Integer identityType,Long messageId);
}

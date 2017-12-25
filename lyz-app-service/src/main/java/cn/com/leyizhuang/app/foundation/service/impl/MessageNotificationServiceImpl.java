package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.foundation.dao.MessageNotificationDAO;
import cn.com.leyizhuang.app.foundation.pojo.response.MessageNotificationListResponse;
import cn.com.leyizhuang.app.foundation.service.MessageNotificationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/10/10
 */
@Service
@Transactional
public class MessageNotificationServiceImpl implements MessageNotificationService {
    private MessageNotificationDAO messageNotificationDAO;

    public MessageNotificationServiceImpl(MessageNotificationDAO messageNotificationDAO) {
        this.messageNotificationDAO = messageNotificationDAO;
    }

    @Override
    public List<MessageNotificationListResponse> queryListByUserIdAndUserType(Long userId, Integer userType) {
        return this.messageNotificationDAO.queryListByUserIdAndUserType(userId, AppIdentityType.getAppIdentityTypeByValue(userType));
    }

    @Override
    public int countUnreadNotifyMessage(Long userId, Integer identityType) {
        return this.messageNotificationDAO.countUnreadNotifyMessage(userId, AppIdentityType.getAppIdentityTypeByValue(identityType));
    }

    @Override
    public void modifyMessageNotification(MessageNotificationListResponse messageNotificationListResponse) {
        messageNotificationDAO.modifyMessageNotification(messageNotificationListResponse);
    }
}

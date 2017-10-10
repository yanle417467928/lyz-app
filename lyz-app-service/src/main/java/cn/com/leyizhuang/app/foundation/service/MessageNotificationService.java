package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.response.MessageNotificationListResponse;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/10/10
 */
public interface MessageNotificationService {

    List<MessageNotificationListResponse> queryListByUserIdAndUserType(@Param("userId") Long userId, @Param("userType") Integer userType);
}

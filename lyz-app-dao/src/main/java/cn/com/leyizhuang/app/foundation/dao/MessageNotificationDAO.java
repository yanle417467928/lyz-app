package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.foundation.pojo.response.MessageNotificationListResponse;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/10/10
 */
@Repository
public interface MessageNotificationDAO {

    List<MessageNotificationListResponse> queryListByUserIdAndUserType(@Param("userId") Long userId, @Param("identityType") AppIdentityType identityType);

}

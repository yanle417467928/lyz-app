package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.foundation.pojo.message.AppUserDevice;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户设备 DAO
 *
 * @author Richard
 * @date 2017/11/22
 */
@Repository
public interface AppUserDeviceDAO {

    AppUserDevice findByClientIdAndDeviceIdAndUserIdAndIdentityType(@Param(value = "clientId") String clientId,
                                                                    @Param(value = "deviceId") String deviceId,
                                                                    @Param(value = "userId") Long userId,
                                                                    @Param(value = "identityType") AppIdentityType identityType);

    void addUserDevice(AppUserDevice device);

    void updateLastLoginTime(AppUserDevice device);

    List<AppUserDevice> findAppUserDeviceByUserIdAndIdentityType(@Param(value = "userId") Long userId,
                                                                 @Param(value = "identityType") AppIdentityType identityType);

}

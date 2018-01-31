package cn.com.leyizhuang.app.foundation.service;


import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.foundation.pojo.message.AppUserDevice;

import java.util.List;

/**
 * 用户设备服务
 * Created by Richard on 2017/11/22.
 */
public interface AppUserDeviceService {

    AppUserDevice findByClientIdAndDeviceIdAndUserIdAndIdentityType(String clientId, String deviceId, Long userId, AppIdentityType identityType);

    void addUserDevice(AppUserDevice device);

    void updateLastLoginTime(AppUserDevice device);

    List<AppUserDevice> findAppUserDeviceByUserIdAndIdentityType(Long userId, AppIdentityType identityType);
}

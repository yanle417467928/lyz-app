package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.message.AppUserDevice;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 用户设备 DAO
 *
 * @author Richard
 * @date 2017/11/22
 */
@Repository
public interface AppUserDeviceDAO {

    AppUserDevice findByClientIdAndDeviceId(@Param(value = "clientId") String clientId,
                                            @Param(value = "deviceId") String deviceId);

    void addUserDevice(AppUserDevice device);

    void updateLastLoginTime(AppUserDevice device);
}

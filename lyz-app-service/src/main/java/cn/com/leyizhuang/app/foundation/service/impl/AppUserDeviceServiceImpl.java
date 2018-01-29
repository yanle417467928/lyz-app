package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.dao.AppUserDeviceDAO;
import cn.com.leyizhuang.app.foundation.pojo.message.AppUserDevice;
import cn.com.leyizhuang.app.foundation.service.AppUserDeviceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 用户设备服务实现
 *
 * @author Richard
 * Created on 2017-09-21 14:25
 **/
@Service
public class AppUserDeviceServiceImpl implements AppUserDeviceService {

    @Resource
    private AppUserDeviceDAO deviceDAO;

    @Override
    public AppUserDevice findByClientIdAndDeviceIdAndIdentityType(String clientId, String deviceId,AppIdentityType identityType) {
        if(StringUtils.isNotBlank(clientId) && StringUtils.isNotBlank(deviceId)){
            return deviceDAO.findByClientIdAndDeviceIdAndIdentityType(clientId,deviceId,identityType);
        }
        return null;
    }

    @Override
    public void addUserDevice(AppUserDevice device) {
        if (null != device){
            deviceDAO.addUserDevice(device);
        }
    }

    @Override
    public void updateLastLoginTime(AppUserDevice device) {
        if (null != device){
            deviceDAO.updateLastLoginTime(device);
        }
    }
}

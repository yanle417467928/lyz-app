package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.SystemSettingDAO;
import cn.com.leyizhuang.app.foundation.service.SystemSettingService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Created on 2018-01-26 18:01
 **/
@Service
public class SystemSettingServiceImpl implements SystemSettingService {

    @Resource
    private SystemSettingDAO systemSettingDAO;

    @Override
    public String getSystemSettingValue(String key) {
        return systemSettingDAO.getSystemSettingValue(key);
    }
}

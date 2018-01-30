package cn.com.leyizhuang.app.foundation.dao;

import org.springframework.stereotype.Repository;

/**
 * 系统设置相关
 *
 * @author Richard
 * Created on 2017-09-19 11:26
 **/
@Repository
public interface SystemSettingDAO {

    String getSystemSettingValue(String key);
}

package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.common.foundation.pojo.SmsAccount;
import org.springframework.stereotype.Repository;

/**
 * lyz-app-facade用户数据仓库
 *
 * @author Richard
 * Created on 2017-09-19 11:26
 **/
@Repository
public interface SmsAccountDAO {
    SmsAccount findOne();
}

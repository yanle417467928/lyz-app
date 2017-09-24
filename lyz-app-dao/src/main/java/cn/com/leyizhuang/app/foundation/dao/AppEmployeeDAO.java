package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.AppEmployee;
import org.springframework.stereotype.Repository;

/**
 * lyz-app-facade 员工数据仓库
 *
 * @author Richard
 * Created on 2017-09-19 11:26
 **/
@Repository
public interface AppEmployeeDAO {

    void save(AppEmployee appEmployee);

    AppEmployee findByLoginName(String loginName);

    AppEmployee findByMobile(String mobile);

    void update(AppEmployee newEmployee);
}

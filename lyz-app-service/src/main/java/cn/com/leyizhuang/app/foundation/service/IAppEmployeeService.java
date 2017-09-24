package cn.com.leyizhuang.app.foundation.service;


import cn.com.leyizhuang.app.foundation.pojo.AppEmployee;

/**
 * lyz-app-facade用户服务接口
 *
 * @author Richard
 * Created on 2017-09-19 11:18
 **/
public interface IAppEmployeeService {

    void save(AppEmployee appEmployee);

    AppEmployee findByLoginName(String name);

    AppEmployee findByMobile(String mobile);

    void update(AppEmployee newEmployee);
}

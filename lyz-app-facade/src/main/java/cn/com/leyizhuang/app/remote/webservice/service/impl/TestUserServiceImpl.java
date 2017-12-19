package cn.com.leyizhuang.app.remote.webservice.service.impl;

import cn.com.leyizhuang.app.remote.webservice.TestUser;
import cn.com.leyizhuang.app.remote.webservice.service.TestUserService;
import com.alibaba.fastjson.JSON;

import javax.jws.WebService;

/**
 * @author Created on 2017-12-19 11:24
 **/
@WebService(targetNamespace="http://cn.com.leyizhuang.app.remote.webservice.service",endpointInterface = "cn.com.leyizhuang.app.remote.webservice.service.TestUserService")
public class TestUserServiceImpl implements TestUserService {
    @Override
    public String getName(String userId) {
        return "liyd-" + userId;
    }

    @Override
    public String getUser(String userId) {
        TestUser testUser = new TestUser();
        testUser.setUsername("YL");
        testUser.setAge("20");
        testUser.setUserId("1");
        return JSON.toJSONString(testUser);
    }
}

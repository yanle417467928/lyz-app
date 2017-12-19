package cn.com.leyizhuang.app.remote.webservice.service;

import cn.com.leyizhuang.app.remote.webservice.TestUser;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * @author Created on 2017-12-19 11:22
 **/
@WebService(targetNamespace="http://cn.com.leyizhuang.app.remote.webservice.service")
public interface TestUserService {

    @WebMethod
    String getName(@WebParam(name = "userId") String userId);
    @WebMethod
    String getUser(String userId);
}

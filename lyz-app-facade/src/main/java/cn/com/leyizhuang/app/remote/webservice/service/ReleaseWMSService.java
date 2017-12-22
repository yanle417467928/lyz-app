package cn.com.leyizhuang.app.remote.webservice.service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * @author Created on 2017-12-19 11:22
 **/
@WebService(targetNamespace="http://cn.com.leyizhuang.app.remote.webservice.service")
public interface ReleaseWMSService {

    /**
     * 发布wms接口
     *
     * @param table
     * @param type
     * @param xml
     * @return wms返回数据
     */
    @WebMethod
    String GetWMSInfo(@WebParam(name = "STRTABLE") String table,
                      @WebParam(name = "STRTYPE") String type,
                      @WebParam(name = "XML") String xml);

    //***************************下面是调用测试***********************************
    @WebMethod
    String getName(@WebParam(name = "userId") String userId);
    @WebMethod
    String getUser(String userId);
}

package cn.com.leyizhuang.app.remote.webservice.service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * @author liuh
 * Created on 2017-12-19 11:22
 **/
@WebService(targetNamespace = "http://cn.com.leyizhuang.app.remote.webservice.service")
public interface ReleaseEBSService {

    /**
     * 发布ebs接口
     *
     * @param table
     * @param type
     * @param xml
     * @return ebs返回数据
     */
    @WebMethod
    String GetEBSInfo(@WebParam(name = "STRTABLE") String table,
                      @WebParam(name = "STRTYPE") String type,
                      @WebParam(name = "XML") String xml);

}

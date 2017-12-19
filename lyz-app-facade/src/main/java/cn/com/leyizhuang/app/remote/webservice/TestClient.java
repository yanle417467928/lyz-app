package cn.com.leyizhuang.app.remote.webservice;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;

/**
 * @author Created on 2017-12-19 13:18
 **/
public class TestClient {
    public static void main(String[] args) throws Exception {
        JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
        Client client = dcf.createClient("http://localhost:9999/webservice/user?wsdl");
        Object[] objects=client.invoke("getUser","1");
        System.out.println("*****"+objects[0].toString());
    }
}

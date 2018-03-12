package cn.com.leyizhuang.app.core.config;

import cn.com.leyizhuang.app.remote.webservice.service.ReleaseWMSService;
import cn.com.leyizhuang.app.remote.webservice.service.impl.ReleaseWMSServiceImpl;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.ws.Endpoint;

/**
 * @author Created on 2017-12-19 11:28
 **/
@Configuration
@AutoConfigureAfter(DeployConfiguration.class)
public class WebServiceWmsConfiguration {

    @Bean
    public SpringBus springBusWms() {
        SpringBus bus = new SpringBus();
        bus.setId("wms");
        return bus;
    }

    @Bean
    public ServletRegistrationBean wmsServlet() {
        CXFServlet cxfServlet = new CXFServlet();
        cxfServlet.setBus(springBusWms());
        ServletRegistrationBean servletBean = new ServletRegistrationBean(
                cxfServlet, "/services/*");
        servletBean.setName("wms");
        servletBean.setLoadOnStartup(1);
        return servletBean;
    }


    @Bean
    public ReleaseWMSService wmsService() {
        return new ReleaseWMSServiceImpl();
    }

    @Bean
    public Endpoint endpointWms() {
        EndpointImpl endpoint = new EndpointImpl(springBusWms(), wmsService());
        endpoint.publish("/webservice");
        System.out.println("wmsWebservice 发布成功！！！");
        return endpoint;
    }

}

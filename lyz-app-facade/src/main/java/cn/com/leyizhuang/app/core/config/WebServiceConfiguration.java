package cn.com.leyizhuang.app.core.config;

import cn.com.leyizhuang.app.core.constant.AppApplicationConstant;
import cn.com.leyizhuang.app.remote.webservice.service.ReleaseWMSService;
import cn.com.leyizhuang.app.remote.webservice.service.impl.ReleaseWMSServiceImpl;
import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.beans.factory.annotation.Value;
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
public class WebServiceConfiguration {

    @Value("${deploy.wms.url}")
    private String wmsUrl;

    @Value("${deploy.ebs.url}")
    private String ebsUrl;

    @Bean
    public ServletRegistrationBean cxfServlet() {
        AppApplicationConstant.wmsUrl = wmsUrl;
        AppApplicationConstant.ebsUrl = ebsUrl;
        CXFServlet cxfServlet = new CXFServlet();
        ServletRegistrationBean servletDef = new ServletRegistrationBean(
                cxfServlet, "/services/*");
        servletDef.setLoadOnStartup(1);
        return servletDef;
    }

    @Bean(name = Bus.DEFAULT_BUS_ID)
    public SpringBus springBus() {
        return new SpringBus();
    }

    @Bean
    public ReleaseWMSService wmsService() {
        return new ReleaseWMSServiceImpl();
    }

    @Bean
    public Endpoint endpoint() {
        EndpointImpl endpoint = new EndpointImpl(springBus(), wmsService());
        endpoint.publish("/webservice");
        return endpoint;
    }
}

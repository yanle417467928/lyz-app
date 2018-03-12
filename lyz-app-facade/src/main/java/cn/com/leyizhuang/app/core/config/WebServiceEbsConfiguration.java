package cn.com.leyizhuang.app.core.config;

import cn.com.leyizhuang.app.remote.webservice.service.ReleaseEBSService;
import cn.com.leyizhuang.app.remote.webservice.service.ReleaseWMSService;
import cn.com.leyizhuang.app.remote.webservice.service.impl.ReleaseEBSServiceImpl;
import cn.com.leyizhuang.app.remote.webservice.service.impl.ReleaseWMSServiceImpl;
import org.apache.cxf.Bus;
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
public class WebServiceEbsConfiguration {

    @Bean
    public SpringBus springBusEbs() {
        SpringBus bus = new SpringBus();
        bus.setId("ebs");
        return bus;
    }

    @Bean
    public ServletRegistrationBean ebsServlet() {
        CXFServlet cxfServlet = new CXFServlet();
        cxfServlet.setBus(springBusEbs());
        ServletRegistrationBean servletBean = new ServletRegistrationBean(
                cxfServlet, "/ebs/services/*");
        servletBean.setName("ebs");
        servletBean.setLoadOnStartup(1);
        return servletBean;
    }



    @Bean
    public ReleaseEBSService ebsService() {
        return new ReleaseEBSServiceImpl();
    }

    @Bean
    public Endpoint endpointEBS() {
        EndpointImpl endpoint = new EndpointImpl(springBusEbs(), ebsService());
        endpoint.publish("/webservice");
        System.out.println("ebsWebservice 发布成功！！！");
        return endpoint;
    }



}

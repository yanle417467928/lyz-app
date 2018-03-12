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
/*@Configuration
@AutoConfigureAfter(DeployConfiguration.class)*/
public class WebServiceEbsConfiguration {

//    @Value("${deploy.wms.url}")
//    private String wmsUrl;
//
//    @Value("${deploy.ebs.url}")
//    private String ebsUrl;

    @Bean
    public ServletRegistrationBean ebsCxfServlet() {
//        AppApplicationConstant.wmsUrl = wmsUrl;
//        AppApplicationConstant.ebsUrl = ebsUrl;
        CXFServlet ebsCxfServlet = new CXFServlet();
        ebsCxfServlet.setBus(springBusEbs());
        ServletRegistrationBean servletDef = new ServletRegistrationBean(
                ebsCxfServlet, "/ebs/services/*");
        servletDef.setName("ebs");
        servletDef.setLoadOnStartup(1);
        return servletDef;
    }

    @Bean
    public SpringBus springBusEbs() {
        SpringBus bus = new SpringBus();
        bus.setId("ebs");
        return bus;
    }

    @Bean
    public ReleaseEBSService ebsService() {
        return new ReleaseEBSServiceImpl();
    }

    @Bean
    public Endpoint endpoint() {
        EndpointImpl endpoint = new EndpointImpl(springBusEbs(), ebsService());
        endpoint.publish("/webservice");
        System.out.println("ebsWebservice 发布成功！！！");
        return endpoint;
    }



}

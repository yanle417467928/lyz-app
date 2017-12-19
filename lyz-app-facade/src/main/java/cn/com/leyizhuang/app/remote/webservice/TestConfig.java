package cn.com.leyizhuang.app.remote.webservice;

import cn.com.leyizhuang.app.remote.webservice.service.TestUserService;
import cn.com.leyizhuang.app.remote.webservice.service.impl.TestUserServiceImpl;
import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.ws.Endpoint;

/**
 * @author Created on 2017-12-19 11:28
 **/
@Configuration
public class TestConfig {
    @Bean
    public ServletRegistrationBean cxfServlet() {
        return new ServletRegistrationBean(new CXFServlet(), "/webservice/*");
    }

    @Bean(name = Bus.DEFAULT_BUS_ID)
    public SpringBus springBus() {
        return new SpringBus();
    }

    @Bean
    public TestUserService testUserService() {
        return new TestUserServiceImpl();
    }

    @Bean
    public Endpoint endpoint() {
        EndpointImpl endpoint = new EndpointImpl(springBus(), testUserService());
        endpoint.publish("/user");
        return endpoint;
    }
}

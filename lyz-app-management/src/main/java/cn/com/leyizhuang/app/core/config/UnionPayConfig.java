package cn.com.leyizhuang.app.core.config;

import cn.com.leyizhuang.app.web.AutoLoadServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description: ${todo}
 * @Author Richard
 * @Date 2018/6/8 11:00
 */
@Configuration
public class UnionPayConfig {

    @Bean
    public ServletRegistrationBean autoLoadServlet() {
        AutoLoadServlet autoLoadServlet = new AutoLoadServlet();
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(
                autoLoadServlet, "/autoLoadServlet");
        servletRegistrationBean.setLoadOnStartup(1);
        return servletRegistrationBean;
    }
}

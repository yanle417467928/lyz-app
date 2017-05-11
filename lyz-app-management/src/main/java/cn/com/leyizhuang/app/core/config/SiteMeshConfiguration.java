package cn.com.leyizhuang.app.core.config;

import cn.com.leyizhuang.app.web.filter.SiteMeshFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author CrazyApeDX
 *         Created on 2017/5/6.
 */
@Configuration
public class SiteMeshConfiguration {

    @Bean
    @ConditionalOnWebApplication
    public FilterRegistrationBean siteMeshFilter() {
        return new FilterRegistrationBean(new SiteMeshFilter());
    }
}

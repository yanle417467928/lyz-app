package cn.com.leyizhuang.app;

import cn.com.leyizhuang.app.web.filter.JwtTokenFilter;
import cn.com.leyizhuang.druid.annotation.EnableDruidDataSource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author CrazyApeDX
 * Created on 2017/4/29.
 */
@SpringBootApplication
@EnableDruidDataSource
@EnableEurekaClient
@MapperScan(basePackages = "cn.com.leyizhuang.app.foundation.dao")
@EnableTransactionManagement
public class ApplicationStarter{
    public static void main(String[] args) {
        SpringApplication.run(ApplicationStarter.class, args);
    }

    @Bean
    public FilterRegistrationBean jwtFilterRegistrationBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        JwtTokenFilter jwtTokenFilter = new JwtTokenFilter();
        registrationBean.setFilter(jwtTokenFilter);
        registrationBean.addUrlPatterns("/app/*");
        registrationBean.addInitParameter("excludedPages", "/app/employee/login,/app/customer/login," +
                "/app/customer/registry,/app/city/list,/app/employee/password/modify,/app/sms/verifyCode/send," +
                "/app/alipay/return/async,/app/wechatpay/return/async");
        return registrationBean;
    }

}

package cn.com.leyizhuang.app;

import cn.com.leyizhuang.druid.annotation.EnableDruidDataSource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author CrazyApeDX
 *         Created on 2017/4/29.
 */
@EnableEurekaClient
@SpringBootApplication
@EnableDruidDataSource
@EnableTransactionManagement
@MapperScan(basePackages = "cn.com.leyizhuang.app.foundation.dao")
public class ApplicationStarter {
    public static void main(String[] args) {
        SpringApplication.run(ApplicationStarter.class, args);
    }
}

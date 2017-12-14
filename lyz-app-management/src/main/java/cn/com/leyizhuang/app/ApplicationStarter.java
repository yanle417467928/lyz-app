package cn.com.leyizhuang.app;

import cn.com.leyizhuang.druid.annotation.EnableDruidDataSource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author CrazyApeDX
 * Created on 2017/4/29.
 */
@SpringBootApplication
@EnableDruidDataSource
@EnableTransactionManagement
@EnableDiscoveryClient
@EnableHystrix
@EnableHystrixDashboard
@EnableScheduling
@MapperScan(basePackages = "cn.com.leyizhuang.app.foundation.dao")
public class ApplicationStarter {
    public static void main(String[] args) {
        SpringApplication.run(ApplicationStarter.class, args);
    }

}

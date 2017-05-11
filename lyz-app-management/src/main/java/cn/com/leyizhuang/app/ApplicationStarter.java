package cn.com.leyizhuang.app;

import cn.com.leyizhuang.druid.annotation.EnableDruidDataSource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author CrazyApeDX
 *         Created on 2017/4/29.
 */
@SpringBootApplication
@EnableDruidDataSource
@MapperScan(basePackages = "cn.com.leyizhuang.app.foundation.dao")
public class ApplicationStarter {
    public static void main(String[] args) {
        SpringApplication.run(ApplicationStarter.class, args);
    }
}

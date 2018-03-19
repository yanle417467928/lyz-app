package cn.com.leyizhuang.app.quartz;

import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Richard
 * Created on 2018-03-15 15:19
 **/
@SpringBootApplication
public class QuartzApplication {
    private static final org.slf4j.Logger Logger = LoggerFactory.getLogger(QuartzApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(QuartzApplication.class, args);

        System.out.println("【【【【【【 简单Quartz-Config-Cluster微服务 】】】】】】已启动.");
    }

}

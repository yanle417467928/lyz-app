package cn.com.leyizhuang.app.quartz.config;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * 数据源配置。
 *
 * @author hmilyylimh
 * @version 0.0.1
 * @date 2017/12/17
 */
@Configuration
public class DataSourceConfig {


    private Environment env;

    @Value("${spring.datasource.druid.config.url}")
    private String dataSourceUrl;

    @Value("${spring.datasource.druid.config.username}")
    private String username;

    @Value("${spring.datasource.druid.config.password}")
    private String password;

    @Bean
    public DataSource dataSource() {
        DataSource configDataSource = new DataSource();
        configDataSource.setUrl(dataSourceUrl);
        configDataSource.setDriverClassName("com.mysql.jdbc.Driver");
        configDataSource.setUsername(username);
        configDataSource.setPassword(password);
        configDataSource.setInitialSize(5);
        configDataSource.setDefaultAutoCommit(true);

        if (configDataSource.getUrl().startsWith("jdbc:mysql://ip")) {
            throw new RuntimeException("请配置数据源地址");
        }
        if (configDataSource.getUsername().startsWith("username")) {
            throw new RuntimeException("请配置数据源用户名");
        }
        if (configDataSource.getPassword().startsWith("password")) {
            throw new RuntimeException("请配置数据源密码");
        }

        // 这些配置看个人需要，需要的话可以全部配置起来，我这里写Demo的话就没有全部配置起来了
        //configDataSource.setInitialSize();
        //configDataSource.setMinEvictableIdleTimeMillis();
        //configDataSource.setNumTestsPerEvictionRun();
        //configDataSource.setTestWhileIdle();
        //configDataSource.setMaxActive();
        //configDataSource.setMaxIdle();
        //configDataSource.setMinIdle();
        //configDataSource.setMaxWait();
        //configDataSource.setRemoveAbandoned();
        //configDataSource.setRemoveAbandonedTimeout();
        //configDataSource.setValidationQuery();
        //configDataSource.setValidationQueryTimeout();
        //configDataSource.setValidationInterval();
        //configDataSource.setTimeBetweenEvictionRunsMillis();

        return configDataSource;
    }
}

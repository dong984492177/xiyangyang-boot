package com.ywt.console;

import com.alibaba.druid.pool.DruidDataSource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

import javax.sql.DataSource;

/**
 * @Author: huangchaoyang
 * @Description: console启动类
 * @Version: 1.0
 * @Create: 2021/1/12
 * @Copyright: 云网通信息科技
 */
@SpringBootApplication(scanBasePackages = {"com.ywt.console.*","com.ywt.common.config.*","com.ywt.common.helper"})
@MapperScan("com.ywt.console.mapper")
@EnableAsync
@EnableTransactionManagement
@EnableWebSocket
@EnableDiscoveryClient
@RefreshScope
public class ConsoleApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConsoleApplication.class, args);
    }

    /**
     * 阿里druid监控
     *
     * @return
     * @throws Exception
     */
    @Bean("duridDatasource")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource druidDataSource() {
       return new DruidDataSource();
    }
}

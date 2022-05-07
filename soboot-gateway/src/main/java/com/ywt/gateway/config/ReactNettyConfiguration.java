package com.ywt.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorResourceFactory;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: huangchaoyang
 * @Description: 定义netty worker线程数
 * @Version: 1.0
 * @Create: 2021/1/12
 */
@Configuration
@Slf4j
public class ReactNettyConfiguration {
    @Value("${reactor.netty.worker-count}")
    private String workerCount;

    @Bean
    public ReactorResourceFactory reactorClientResourceFactory() {
        System.setProperty("reactor.netty.ioWorkerCount", workerCount);
        log.info("worker线程数={}",workerCount);
        return new ReactorResourceFactory();
    }
}

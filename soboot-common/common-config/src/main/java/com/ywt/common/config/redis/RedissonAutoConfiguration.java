package com.ywt.common.config.redis;

import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: huangchaoyang
 * @Description: Redisson自动装配类
 * @Version: 1.0
 * @Create: 2021/1/12
 */
@Configuration
@ConditionalOnClass({RedissonClient.class, RedissonService.class})
public class RedissonAutoConfiguration {
    @Bean
    public RedissonService redissonService() {
        return new RedissonService();
    }
}

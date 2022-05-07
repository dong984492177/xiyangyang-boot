package com.ywt.common.config.oss;

import com.aliyun.oss.OSSClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 */
@Configuration
@EnableConfigurationProperties({ AliyunOSSProperties.class })
@ConditionalOnProperty(prefix = "oss.aliyun", value = "isEnable", havingValue = "true")
@ConditionalOnClass({OSSClient.class})
public class AliyunOSSClientAutoConfiguration {
    private AliyunOSSProperties properties;

    public AliyunOSSClientAutoConfiguration(AliyunOSSProperties properties) {
        this.properties = properties;
    }

    @Bean
    public AliyunOSSClientUtil aliyunOSSClientUtil() {
        return new AliyunOSSClientUtil(properties);
    }
}

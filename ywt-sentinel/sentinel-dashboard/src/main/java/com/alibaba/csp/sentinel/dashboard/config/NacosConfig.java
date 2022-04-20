package com.alibaba.csp.sentinel.dashboard.config;

import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.DegradeRuleEntity;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.FlowRuleEntity;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.ParamFlowRuleEntity;
import com.alibaba.csp.sentinel.dashboard.domain.DegradeRuleConvert;
import com.alibaba.csp.sentinel.dashboard.domain.FlowRuleConvert;
import com.alibaba.csp.sentinel.dashboard.domain.ParamFlowRuleConvert;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.config.ConfigFactory;
import com.alibaba.nacos.api.config.ConfigService;

/**
 * @author hcy
 * @description
 * @date 2020/6/5 10:06
 * @Copyright: 云网通信息科技
 */
@RefreshScope
@Configuration
public class NacosConfig {

    @Value("${sentinel.datasource.nacos.server-addr}")
    private String serverAddr;

    @Value("${sentinel.datasource.nacos.enable:false}")
    private boolean enable;

    @Value("${sentinel.datasource.nacos.namespace}")
    private String namespace;

    @Value("${server.port}")
    private String port;

    @Bean
    public FlowRuleConvert flowRuleEntityEncoder() {
        return new FlowRuleConvert();
    }

    @Bean
    public Converter<String, List<FlowRuleEntity>> flowRuleEntityDecoder() {
        return s -> JSON.parseArray(s, FlowRuleEntity.class);
    }

    @Bean
    public ParamFlowRuleConvert paramFlowRuleEntityEncoder() {
        return new ParamFlowRuleConvert();
    }

    @Bean
    public Converter<String, List<ParamFlowRuleEntity>> paramFlowRuleEntityDecoder() {
        return s -> JSON.parseArray(s, ParamFlowRuleEntity.class);
    }

    @Bean
    public DegradeRuleConvert degradeRuleEntityEncoder() {
        return new DegradeRuleConvert();
    }

    @Bean
    public Converter<String, List<DegradeRuleEntity>> degradeRuleEntityDecoder() {
        return s -> JSON.parseArray(s, DegradeRuleEntity.class);
    }

    @Bean
    public ConfigService nacosConfigService() throws Exception {
        Properties properties = new Properties();
        properties.put("serverAddr",serverAddr);
        properties.put("namespace",namespace);
        return ConfigFactory.createConfigService(properties);
    }


    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}

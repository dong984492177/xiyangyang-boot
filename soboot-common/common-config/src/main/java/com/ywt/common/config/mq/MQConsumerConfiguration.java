package com.ywt.common.config.mq;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * @Author: huangchaoyang
 * @Description: mq消费者配置
 * @Version: 1.0
 * @Create: 2021/1/12
 */
@Slf4j
@Configuration
@EnableConfigurationProperties({ RocketMQProperties.class })
@ConditionalOnProperty(prefix = "rocketmq", value = "isEnable", havingValue = "true")
public class MQConsumerConfiguration {
    private RocketMQProperties properties;

    private ApplicationContext applicationContext;

    public MQConsumerConfiguration(RocketMQProperties properties, ApplicationContext applicationContext) {
        this.properties = properties;
        this.applicationContext = applicationContext;
    }

    /**
     * SpringBoot启动时加载所有消费者
     */
    @PostConstruct
    public void initConsumer() {
        Map<String, AbstractRocketConsumer> consumers = applicationContext.getBeansOfType(AbstractRocketConsumer.class);
        if (consumers.size() == 0) {
            log.info("init rocket consumer 0");
        }
        for (String beanName : consumers.keySet()) {
            AbstractRocketConsumer consumer = consumers.get(beanName);
            consumer.init();
            createConsumer(consumer);
            log.info("init success consumer title {} , topics {} , tags {}", consumer.getConsumerTitle(), consumer.getTopics(),
                    consumer.getTags());
        }
    }

    /**
     * 通过消费者信心创建消费者
     */
    public void createConsumer(AbstractRocketConsumer arc) {
        //DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(this.properties.getGroupName());
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer();
        consumer.setConsumerGroup(arc.getGroup());
        consumer.setNamesrvAddr(this.properties.getNamesrvAddr());
        consumer.setConsumeThreadMin(this.properties.getConsumerConsumeThreadMin());
        consumer.setConsumeThreadMax(this.properties.getConsumerConsumeThreadMax());
        consumer.registerMessageListener(arc.getMessageListener());
        /**
         * 设置Consumer第一次启动是从队列头部开始消费还是队列尾部开始消费 如果非第一次启动，那么按照上次消费的位置继续消费
         */
        // consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        /**
         * 设置消费模型，集群还是广播，默认为集群
         */
        // consumer.setMessageModel(MessageModel.CLUSTERING);

        /**
         * 设置一次消费消息的条数，默认为1条
         */
        consumer.setConsumeMessageBatchMaxSize(this.properties.getConsumerConsumeMessageBatchMaxSize());
        try {
            consumer.subscribe(arc.getTopics(), arc.getTags());
            consumer.start();
            arc.setMqPushConsumer(consumer);
        } catch (MQClientException e) {
            log.error("info consumer title {}", arc.getConsumerTitle(), e);
        }
    }
}

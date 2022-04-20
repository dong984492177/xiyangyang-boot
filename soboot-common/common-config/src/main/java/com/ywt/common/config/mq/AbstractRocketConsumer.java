package com.ywt.common.config.mq;

import lombok.Data;
import org.apache.rocketmq.client.consumer.MQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;

@Data
public abstract class AbstractRocketConsumer implements RocketConsumer {
    protected String topics;
    protected String tags;
    protected String group;
    protected MessageListenerConcurrently messageListener;
    protected String consumerTitle;
    protected MQPushConsumer mqPushConsumer;

    /**
     * 必要的信息
     *
     * @param topics
     * @param tags
     * @param consumerTitle
     */
    public void necessary(String topics, String tags, String consumerTitle) {
        this.topics = topics;
        this.tags = tags;
        this.group = tags;
        this.consumerTitle = consumerTitle;
    }

    /**
     * 必要的信息
     *
     * @param topics
     * @param tags
     * @param consumerTitle
     */
    public void necessary(String topics, String tags, String group, String consumerTitle) {
        this.topics = topics;
        this.tags = tags;
        this.group = group;
        this.consumerTitle = consumerTitle;
    }

    public abstract void init();

    @Override
    public void registerMessageListener(MessageListenerConcurrently messageListener) {
        this.messageListener = messageListener;
    }
}

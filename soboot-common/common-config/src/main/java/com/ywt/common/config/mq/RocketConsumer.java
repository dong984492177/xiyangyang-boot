package com.ywt.common.config.mq;

import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 * @Copyright: 云网通信息科技
 */
public interface RocketConsumer {
    /**
     * 初始化消费者
     */
    public abstract void init();

    /**
     * 注册监听
     *
     * @param messageListener
     */
    public void registerMessageListener(MessageListenerConcurrently messageListener);
}

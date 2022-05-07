package com.ywt.console.config;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: huangchaoyang
 * @Description: mq消息发送service
 * @Version: 1.0
 * @Create: 2022-05-07 17:51:07
 * @Copyright: 互邦老宝贝
 */
@Component
public class MQProduceService {

    private static Logger logger = LoggerFactory.getLogger(MQProduceService.class);

    @Autowired
    private DefaultMQProducer defaultMQProducer;

    public void send(Message msg) {
        try {
            SendResult sendResult = defaultMQProducer.send(msg);
            logger.info("mq Message:{} , status:{},  topic:{},  tags:{}", new String(msg.getBody()), sendResult.getSendStatus(), msg.getTopic(), msg.getTags());
        } catch (Exception e) {
            logger.error("mq send fail", e);
        }
    }

    public void sendDelay(Message msg,long deleyTime) {
        try {
            SendResult sendResult = defaultMQProducer.send(msg,deleyTime);
            logger.info("mq Message:{} , status:{},  topic:{},  tags:{}", new String(msg.getBody()), sendResult.getSendStatus(), msg.getTopic(), msg.getTags());
        } catch (Exception e) {
            logger.error("mq send fail", e);
        }
    }
}

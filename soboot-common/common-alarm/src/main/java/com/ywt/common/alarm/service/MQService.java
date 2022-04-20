package com.ywt.common.alarm.service;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.ywt.common.alarm.constant.MqTopicConstant;
import com.ywt.common.alarm.entity.Alarm;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 * @Copyright: 云网通信息科技
 */
@Slf4j
@Service
public class MQService {

	@Autowired
    private DefaultMQProducer defaultMQProducer;

	public void sendAlarm(Alarm alarm) {
	    Message message = new Message(MqTopicConstant.TOPIC_ALARM_COMMON_REPORT, MqTopicConstant.TAG_ALARM_COMMON_REPORT, JSON.toJSONString(alarm).getBytes());
	    send(message);
	}

    private void send(Message msg) {
        try {
            SendResult sendResult = defaultMQProducer.send(msg);
            log.info("mq Message:{} , status:{},  topic:{},  tags:{}", new String(msg.getBody()), sendResult.getSendStatus(), msg.getTopic(), msg.getTags());
        } catch (Exception e) {
            log.error("mq send fail", e);
        }
    }

}

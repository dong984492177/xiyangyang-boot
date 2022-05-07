package com.ywt.console.biz;

import com.ywt.console.config.MQProduceService;
import com.ywt.console.constant.TopicConstant;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * @Author: huangchaoyang
 * @Description:  mq发送消息聚合biz
 * @Version: 1.0
 * @Create: 2022-05-07 17:19:07
 * @Copyright: 互邦老宝贝
 */
@Component
public class MQBizService {

    @Autowired
    private MQProduceService mqProduceService;

    /**
     * 写任务处理详情
     * @param body json串
     */
    public void writeTaskDetailLog(String body){

        Message message = new Message(TopicConstant.TOPIC_TASK_DETAIL_LOG, TopicConstant.TAG_TASK_DETAIL_LOG, body.getBytes());
        mqProduceService.send(message);
    }
}

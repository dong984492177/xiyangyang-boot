package com.ywt.console.consume;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ywt.common.config.mq.AbstractRocketConsumer;
import com.ywt.console.constant.TopicConstant;
import com.ywt.console.entity.UserTask;
import com.ywt.console.entity.activiti.TaskCheckDetail;
import com.ywt.console.service.ITaskCheckDetailService;
import com.ywt.console.service.IUserTaskService;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2022-05-07 17:27:07
 * @Copyright: 互邦老宝贝
 */
@Component
public class TaskCheckDetailConsume extends AbstractRocketConsumer {

    private static Logger logger = LoggerFactory.getLogger(TaskCheckDetailConsume.class);

    @Autowired
    private ITaskCheckDetailService taskCheckDetailService;

    @Autowired
    private IUserTaskService userTaskService;

    @Override
    public void init() {
        // 设置主题,标签与消费者标题
        super.necessary(TopicConstant.TOPIC_TASK_DETAIL_LOG, TopicConstant.TAG_TASK_DETAIL_LOG, "写任务处理详情");
        //消费者具体执行逻辑
        registerMessageListener((msgs, context) -> {
            msgs.forEach(msg -> {
                TaskCheckDetail taskCheckDetail = JSONObject.parseObject(msg.getBody(), TaskCheckDetail.class);
                logger.info("写任务处理详情开始入库,{}", JSON.toJSONString(taskCheckDetail));
                UserTask userTask = userTaskService.queryByInstanceId(taskCheckDetail.getProcessInstanceId());
                taskCheckDetail.setTaskId(userTask.getId());
                taskCheckDetailService.save(taskCheckDetail);
                logger.info("写任务处理详情入库成功");
            });
            // 标记该消息已经被成功消费
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
    }
}

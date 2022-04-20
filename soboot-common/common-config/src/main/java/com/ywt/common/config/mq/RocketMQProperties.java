package com.ywt.common.config.mq;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author: huangchaoyang
 * @Description: mq属性配置
 * @Version: 1.0
 * @Create: 2021/1/12
 * @Copyright: 云网通信息科技
 */
@Data
@ConfigurationProperties(prefix = "rocketmq")
public class RocketMQProperties {
    //是否开启自动配置
    private boolean isEnable = false;
    //mq的nameserver地址
    private String namesrvAddr = "localhost:9876";
    //发送同一类消息的设置为同一个group，保证唯一,默认不需要设置，rocketmq会使用ip@pid(pid代表jvm名字)作为唯一标示
    private String groupName = "default";
    //消息最大长度 单位B 默认1024*1024*4(4M)
    private int producerMaxMessageSize = 1024;
    //发送消息超时时间,默认3000
    private int producerSendMsgTimeout = 2000;
    //发送消息失败重试次数，默认2
    private int producerRetryTimesWhenSendFailed = 2;
    //消息压缩
    private int producerCompressMsgBodyOverHowmuch = 2048;
    //消费者线程数量
    private int consumerConsumeThreadMin = 5;
    private int consumerConsumeThreadMax = 30;
    //设置一次消费消息的条数，默认为1条
    private int consumerConsumeMessageBatchMaxSize = 1;
}

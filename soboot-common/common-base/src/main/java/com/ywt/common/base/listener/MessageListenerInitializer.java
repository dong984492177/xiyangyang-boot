package com.ywt.common.base.listener;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author: huangchaoyang
 * @Description: 利用spring容器事件初始化MessageListener
 * @Version: 1.0
 * @Create: 2021/1/12
 * @Copyright: 云网通信息科技
 */
@Component
public class MessageListenerInitializer implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // 根容器为Spring容器
            Map<String,Object> beans = event.getApplicationContext().getBeansWithAnnotation(Listener.class);
            for(Object bean : beans.values()){
                MessageListener messageListener = (MessageListener)bean;
                MessageKit.register(messageListener);
            }
    }
}

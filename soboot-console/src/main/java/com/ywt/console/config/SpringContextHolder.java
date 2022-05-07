package com.ywt.console.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * @Author: huangchaoyang
 * @Description: 容器上下文
 * @Version: 1.0
 * @Create: 2021/1/12
 */
@Lazy(false)
@Service
public class SpringContextHolder implements ApplicationContextAware {

    private static ApplicationContext applicationContext=null;

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        this.applicationContext = applicationContext;
    }
    /**
     * 获取applicationContext
     * @return
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) {
        return (T) applicationContext.getBean(name);
    }

    public static <T> T getBean(Class<T> requiredType) {
        if(null!=applicationContext){
            return applicationContext.getBean(requiredType);
        }
        return null;
    }
}

package com.ywt.common.base.listener;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 * @Copyright: 云网通信息科技
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Component
public @interface Listener {

	int DEFAULT_WEIGHT = 10;

	int weight() default DEFAULT_WEIGHT;

	boolean async() default true;

	String[] action();
}

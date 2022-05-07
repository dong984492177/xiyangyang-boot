package com.ywt.common.base.annotation;

import com.ywt.common.enums.ActionType;
import com.ywt.common.enums.OperationType;
import com.ywt.common.enums.ActionType;
import com.ywt.common.enums.OperationType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: huangchaoyang
 * @Description: 注解对象行为
 * @Version: 1.0
 * @Create: 2021/1/12
 */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Action {

    //类型
    ActionType actionType() default ActionType.UNKNOWN;

    //操作
    String operation() default "";

    OperationType operationType() default OperationType.UNKNOWN;
}

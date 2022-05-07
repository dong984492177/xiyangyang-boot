package com.ywt.common.base.exception;

/**
 * @Author: huangchaoyang
 * @Description: Bean对象映射异常
 * @Version: 1.0
 * @Create: 2021/1/12
 */
public class BeanMappingException extends RuntimeException {

    private static final long serialVersionUID = 6373045327623092554L;

    public BeanMappingException() {
        this("Bean对象映射异常");
    }

    public BeanMappingException(String message) {
        super(message);
    }

    public BeanMappingException(Throwable cause) {
        super(cause);
    }

    public BeanMappingException(String message, Throwable cause) {
        super(message, cause);
    }
}

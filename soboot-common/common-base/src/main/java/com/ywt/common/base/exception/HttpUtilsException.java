package com.ywt.common.base.exception;

/**
 * @Author: huangchaoyang
 * @Description: HttpUtils异常
 * @Version: 1.0
 * @Create: 2021/1/12
 */
public class HttpUtilsException extends RuntimeException {

    private static final long serialVersionUID = 6373045327623092554L;

    public HttpUtilsException() {
        this("HttpUtils异常");
    }

    public HttpUtilsException(String message) {
        super(message);
    }

    public HttpUtilsException(Throwable cause) {
        super(cause);
    }

    public HttpUtilsException(String message, Throwable cause) {
        super(message, cause);
    }
}

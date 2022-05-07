package com.ywt.common.exception;

/**
 * @Author: huangchaoyang
 * @Description: 默认API异常
 * @Version: 1.0
 * @Create: 2021/1/12
 */
public class DefaultApiException extends RuntimeException {
    private static final long serialVersionUID = 591990235347646359L;

    private String errorCode;

    public DefaultApiException() {
        this("Api调用异常");
    }

    public DefaultApiException(String message) {
        super(message);
    }

    public DefaultApiException(Throwable cause) {
        super(cause);
    }

    public DefaultApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public DefaultApiException(String errorCode, String message, Throwable cause) {
        this(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}

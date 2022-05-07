package com.ywt.common.exception;

import org.springframework.http.HttpStatus;

/**
 * @Author: huangchaoyang
 * @Description: 统一WEB异常, 其他自定义异常均继承
 * @Version: 1.0
 * @Create: 2021/1/12
 */
public class DefaultWebException extends RuntimeException {

    private static final long serialVersionUID = -5670383575187845333L;

    private HttpStatus httpStatus;

    private String errorCode;

    public DefaultWebException(HttpStatus httpStatus, String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
    }

    public DefaultWebException(String errorCode, String message, Throwable cause) {
        // 默认500状态码
        this(HttpStatus.INTERNAL_SERVER_ERROR, errorCode, message, cause);
    }

    public DefaultWebException(String errorCode, String message) {
        this(errorCode, message, null);
    }

    public DefaultWebException() {
        this("DefaultApi调用异常");
    }

    public DefaultWebException(String message) {
        super(message);
    }

    public DefaultWebException(Throwable cause) {
        this("DefaultApi调用异常", cause);
    }

    public DefaultWebException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}

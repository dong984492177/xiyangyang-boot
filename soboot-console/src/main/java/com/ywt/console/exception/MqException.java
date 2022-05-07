package com.ywt.console.exception;

import com.ywt.common.exception.DefaultWebException;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 */
public class MqException extends DefaultWebException {

    private static final long serialVersionUID = -194180511961808100L;

    public MqException() {
        this("消息队列操作出错");
    }

    public MqException(String message) {
        super(message);
    }

    public MqException(Throwable cause) {
        super(cause);
    }

    public MqException(String message, Throwable cause) {
        super(message, cause);
    }

    public MqException(Integer errorCode, String message) {
        super(errorCode + "", message, null);
    }
}

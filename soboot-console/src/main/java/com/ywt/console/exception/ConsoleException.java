package com.ywt.console.exception;


import com.ywt.common.exception.DefaultWebException;

/**
 * @Author: Administrator
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 * @Coyright: 云网通信息科技
 */
public class ConsoleException extends DefaultWebException {

    private static final long serialVersionUID = 7087235479322598815L;

    public ConsoleException() {
        this("系统异常");
    }

    public ConsoleException(String message) {
        super(message);
    }

    public ConsoleException(Throwable cause) {
        super(cause);
    }

    public ConsoleException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConsoleException(Integer errorCode, String message) {
        super(errorCode + "", message);
    }
}

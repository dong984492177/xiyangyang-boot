package com.ywt.common.base.exception;

import java.io.IOException;

/**
 * @Author: huangchaoyang
 * @Description: 未授权异常
 * @Version: 1.0
 * @Create: 2021/1/12
 */
public class UnauthorizedException extends IOException {

    public UnauthorizedException() {
        this("系统异常");
    }

    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException(Throwable cause) {
        super(cause);
    }

    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }
}

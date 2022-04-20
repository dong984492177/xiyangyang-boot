package com.ywt.message.api.exception;

import com.ywt.common.exception.DefaultApiException;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 * @Copyright: 云网通信息科技
 */
public class MessageSendException extends DefaultApiException {

	private static final long serialVersionUID = 1L;

	public MessageSendException() {
        this("消息发送异常");
    }

    public MessageSendException(String message){
        super(message);
    }

    public MessageSendException(Throwable cause){
        super(cause);
    }

    public MessageSendException(String message, Throwable cause){
        super(cause);
    }
}

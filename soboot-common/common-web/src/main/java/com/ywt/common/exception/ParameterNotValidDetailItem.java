package com.ywt.common.exception;


import java.io.Serializable;

/**
 * @Author: huangchaoyang
 * @Description: 参数验证出错详情条目
 * @Version: 1.0
 * @Create: 2021/1/12
 */
public class ParameterNotValidDetailItem implements Serializable {

    private static final long serialVersionUID = 248477069666505614L;
    /**
     * 验证出错的字段名
     */
    private String field;

    /**
     * 验证出错的消息
     */
    private String message;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

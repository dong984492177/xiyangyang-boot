package com.ywt.message.bean;

import com.ywt.common.bean.AbstractBaseApiBean;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MessageLog extends AbstractBaseApiBean {
    private static final long serialVersionUID = 5787557281719626322L;
    private Integer id;

    /**
     * 模板key
     */
    private String templateKey;

    /**
     * 消息标题
     */
    private String title;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 收件人
     */
    private String receiver;

    /**
     * 标识符
     */
    private String identifier;

    private LocalDateTime createdTime;
}

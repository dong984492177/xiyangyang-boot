package com.ywt.message.bean;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class MessageSendTask {
    /**
     * 消息模板Key
     */
    private String templateKey;

    /**
     * 接收人列表
     */
    private List<String> receiver;

    /**
     * 用于替换模板内容的数据
     */
    private Map<String, String> replacement;

    public MessageSendTask() {
        this(null);
    }

    public MessageSendTask(String templateKey) {
        this.templateKey = templateKey;
    }
}

package com.ywt.message.bean;

import com.ywt.common.bean.AbstractBaseApiBean;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MessageTemplate extends AbstractBaseApiBean {
    private static final long serialVersionUID = -812221854453811694L;

    private Integer id;

    /**
     * 模板类型, 默认text
     * text
     * image
     * audio
     */
    private String type;


    /**
     * 模板名称
     */
    private String name;

    /**
     * 模版的key(唯一)
     */
    private String templateKey;


    /**
     * 模板标题
     */
    private String title;

    /**
     * 模板内容
     */
    private String content;

    /**
     * 签名(支持签名和内容分开的情况)
     */
    private String sign;

    private LocalDateTime createdTime;
    /**
     * 修改时间
     */
    private LocalDateTime updatedTime;
}

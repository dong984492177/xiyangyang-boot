package com.ywt.message.bean;

import java.time.LocalDateTime;

import com.ywt.common.bean.AbstractBaseApiBean;
import com.ywt.common.enums.EnableDisableStatus;
import com.ywt.common.enums.YesNoStatus;
import com.ywt.message.constant.MessageType;
import com.ywt.message.constant.MessageVendorType;

import lombok.Data;

@Data
public class MessageVendor extends AbstractBaseApiBean {
    private static final long serialVersionUID = 2333475787991752331L;
    private Integer id;

    /**
     * Vendor名称
     */
    private String name;

    /**
     * Vendor类型
     */
    private MessageVendorType vendorType;

    /**
     * Vendor对应消息类型
     */
    private MessageType messageType;

    /**
     * 访问Url
     */
    private String accessUrl;

    /**
     * 访问key的id
     */
    private String accessKeyId;

    /**
     * 访问key的秘钥
     */
    private String accessKeySecret;

    private YesNoStatus isSandbox;

    /**
     * 每个 Vendor 额外的扩展配置项
     */
    private String extraConfig;

    /**
     * 是否启用
     */
    private EnableDisableStatus enableStatus;

    private LocalDateTime createdTime;
    /**
     * 修改时间
     */
    private LocalDateTime updatedTime;
}

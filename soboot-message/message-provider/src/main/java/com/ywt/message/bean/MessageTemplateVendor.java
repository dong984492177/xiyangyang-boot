package com.ywt.message.bean;

import com.ywt.common.bean.AbstractBaseApiBean;
import com.ywt.common.enums.EnableDisableStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageTemplateVendor extends AbstractBaseApiBean {
    private static final long serialVersionUID = -8109703530967390514L;
    private Integer id;

    /**
     * 模板ID
     */
    private Integer templateId;

    /**
     *供应商ID
     */
    private Integer vendorId;

    /**
     * 外部模板ID
     */
    private String externalTemplateId;

    /**
     * 默认接收人列表
     */
    private List<String> defaultReceiverList;

    /**
     * 同一模板多个 vendor 关系时区分用的标识
     */
    private String identifier;

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

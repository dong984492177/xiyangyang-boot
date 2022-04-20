package com.ywt.common.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.ywt.common.enums.DeleteStatus;
import com.ywt.common.enums.DeleteStatus;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.Date;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = -1758826173926257722L;

    @Builder.Default
    private Integer isDelete = DeleteStatus.NOT_DELETE.getValue();

    private Date deleteTime;

    private Date createTime;

    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private Integer createBy;

    private Date updateTime;

    @TableField(value = "update_by", fill = FieldFill.INSERT_UPDATE)
    private Integer updateBy;
}

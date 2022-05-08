package com.ywt.console.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ywt.common.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ApiModel(value="任务分类表对象", description="任务分类表")
@TableName("t_task_category")
public class TaskCategory extends BaseEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "分类名称")
    private String name;

    @ApiModelProperty(value = "父类id")
    private Integer parentId;

    @ApiModelProperty(value = "排序(值越大优先级越高)")
    private Integer sort;

    @ApiModelProperty(value = "是否启用:1-是 0-否")
    private String status;

    @ApiModelProperty(value = "分类所属类型id")
    private String categoryTypeId;
}

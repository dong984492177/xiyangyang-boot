package com.ywt.console.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ywt.common.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;

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
@ApiModel(value="任务信息基础表对象", description="任务信息基础表")
@TableName("t_user_task")
public class UserTask extends BaseEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "发起人")
    private String userName;

    @ApiModelProperty(value = "任务所属类别id")
    private Integer taskCategoryId;

    @ApiModelProperty(value = "任务所属类别名称")
    private String taskCategoryName;

    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @ApiModelProperty(value = "流程实例id")
    private String instanceId;

    @ApiModelProperty(value = "任务状态")
    private String state;

    @ApiModelProperty(value = "备注")
    private String remark;
}

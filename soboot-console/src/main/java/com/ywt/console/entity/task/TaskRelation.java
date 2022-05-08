package com.ywt.console.entity.task;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * @Create: 2022-05-06 14:37:06
 * @Copyright: 互邦老宝贝
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value="任务审核详情对象", description="任务审核详情表")
@TableName("t_task_check_detail")
public class TaskRelation extends BaseEntity {

    @TableId(type = IdType.AUTO)
    @ApiModelProperty("id")
    private String id;

    @TableField("status")
    @ApiModelProperty("状态:0-未通过 1-通过")
    private String status;

    @TableField("user_name")
    @ApiModelProperty("用户名称")
    private String userName;

    @TableField("check_user_name")
    @ApiModelProperty("审核者姓名")
    private String checkUserName;

    @TableField("check_user_id")
    @ApiModelProperty("审核者id")
    private Integer checkUserId;

    @TableField("task_id")
    @ApiModelProperty("任务id")
    private Integer taskId;

    @TableField("check_time")
    @ApiModelProperty("审核时间")
    private Date checkTime;

    @TableField(exist = false)
    @ApiModelProperty("实例id")
    private String processInstanceId;
}

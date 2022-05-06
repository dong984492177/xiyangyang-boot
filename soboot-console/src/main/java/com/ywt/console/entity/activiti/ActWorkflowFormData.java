package com.ywt.console.entity.activiti;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2022-05-06 14:37:06
 * @Copyright: 互邦老宝贝
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("动态表单")
@TableName("act_workflow_formdata")
public class ActWorkflowFormData {

    @TableId(type= IdType.AUTO)
    @ApiModelProperty("唯一标识符")
    private Long id;

    @TableField("business_key")
    @ApiModelProperty("事务key")
    private String businessKey;

    @TableField("form_key")
    @ApiModelProperty("表单Key")
    private String formKey;

    @TableField("control_id")
    @ApiModelProperty("表单id")
    private String controlId;

    @TableField("control_name")
    @ApiModelProperty("表单名称")
    private String controlName;

    @TableField("control_value")
    @ApiModelProperty("表单值")
    private String controlValue;

    @TableField("task_node_name")
    @ApiModelProperty("任务节点名称")
    private String taskNodeName;

    @TableField("create_name")
    @ApiModelProperty("创建者姓名")
    private String createName;

    @TableField("create_by")
    private String createBy;

    @TableField("create_time")
    private Date createTime;
}

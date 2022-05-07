package com.ywt.console.models.resmodel;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.Date;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2022-05-03
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserTaskResModel {

    @ApiModelProperty(value = "id", dataType = "int")
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

    @ApiModelProperty(value = "创建时间")
    private Date createTime;
}

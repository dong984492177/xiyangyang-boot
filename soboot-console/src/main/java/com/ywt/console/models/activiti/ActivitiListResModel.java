package com.ywt.console.models.activiti;

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
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class ActivitiListResModel {

    @ApiModelProperty(value = "id", dataType = "string")
    private String id;

    @ApiModelProperty(value = "类别", dataType = "string")
    private String key;

    @ApiModelProperty(value = "名称", dataType = "string")
    private String name;

    @ApiModelProperty(value = "版本", dataType = "int")
    private Integer version;

    @ApiModelProperty(value = "部署id", dataType = "string")
    private String deploymentId;

    @ApiModelProperty(value = "部署时间", dataType = "date")
    private Date deploymentTime;

    @ApiModelProperty(value = "状态", dataType = "suspendState")
    private Integer suspendState;

    private String resourceName;
}

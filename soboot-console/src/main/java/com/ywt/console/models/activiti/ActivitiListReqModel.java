package com.ywt.console.models.activiti;

import com.ywt.console.models.QueryModel;
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
public class ActivitiListReqModel extends QueryModel {

    @ApiModelProperty(value = "类别", dataType = "string")
    private String key;

    @ApiModelProperty(value = "名称", dataType = "string")
    private String name;

    @ApiModelProperty(value = "版本", dataType = "string")
    private String version;

    @ApiModelProperty(value = "开始时间", dataType = "date")
    private Date startTime;

    @ApiModelProperty(value = "结束时间", dataType = "date")
    private Date endTime;
}

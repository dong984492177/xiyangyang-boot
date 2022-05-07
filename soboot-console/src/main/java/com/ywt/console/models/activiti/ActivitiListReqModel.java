package com.ywt.console.models.activiti;

import com.ywt.console.models.QueryModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

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
}

package com.ywt.console.models.activiti;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * @Author: zhangsan
 * @Description:
 * @Version: 1.0
 * @Create: 2022-05-03
 * @Coyright: 喜阳阳信息科技
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class ActivitiListResModel {

    @ApiModelProperty(value = "id", dataType = "int")
    private Integer id;

    @ApiModelProperty(value = "类别", dataType = "string")
    private String key;

    @ApiModelProperty(value = "名称", dataType = "string")
    private String name;

    @ApiModelProperty(value = "版本", dataType = "string")
    private String version;
}

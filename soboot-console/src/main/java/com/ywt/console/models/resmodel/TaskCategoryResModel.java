package com.ywt.console.models.resmodel;

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
public class TaskCategoryResModel {

    @ApiModelProperty(value = "id")
    private Integer id;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "排序(值越大优先级越高)")
    private Integer sort;

    @ApiModelProperty(value = "父类id")
    private Integer parentId;

    @ApiModelProperty(value = "是否启用:1-是 0-否")
    private String status;
}

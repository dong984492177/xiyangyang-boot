package com.ywt.console.models.reqmodel;

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
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskCategoryReqModel extends QueryModel {

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "是否启用:1-是 0-否")
    private String status;
}

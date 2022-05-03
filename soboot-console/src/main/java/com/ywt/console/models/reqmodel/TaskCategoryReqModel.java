package com.ywt.console.models.reqmodel;

import com.ywt.console.models.QueryModel;
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
public class TaskCategoryReqModel extends QueryModel {

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "是否启用:1-是 0-否")
    private String status;
}

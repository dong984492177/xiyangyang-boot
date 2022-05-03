package com.ywt.console.models.reqmodel;

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
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserTaskReqModel {

    @ApiModelProperty(value = "id")
    private Integer id;

    @ApiModelProperty(value = "状态")
    private String state;
}

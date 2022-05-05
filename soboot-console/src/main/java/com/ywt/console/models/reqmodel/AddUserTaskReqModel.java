package com.ywt.console.models.reqmodel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

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
@ApiModel(value = "AddUserTaskReqModel")
public class AddUserTaskReqModel {

    @ApiModelProperty(value = "状态")
    private String state;

    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    //@TODO 多个分类id
    @ApiModelProperty(value = "分类id")
    private Integer taskCategoryId;

    @ApiModelProperty(value = "备注")
    private String remark;
}

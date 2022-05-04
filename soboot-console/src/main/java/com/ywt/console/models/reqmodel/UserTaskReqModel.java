package com.ywt.console.models.reqmodel;

import com.ywt.console.models.QueryModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

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
public class UserTaskReqModel extends QueryModel {

    @ApiModelProperty(value = "发起人")
    private String userName;

    @ApiModelProperty(value = "任务所属类别id")
    private Integer taskCategoryId;

    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @ApiModelProperty(value = "流程实例id")
    private String instanceId;

    @ApiModelProperty(value = "任务状态")
    private String state;

    @ApiModelProperty(value = "创建人",hidden = true)
    private String createBy;
}

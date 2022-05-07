package com.ywt.console.models.reqmodel.systemreqmodels;

import com.ywt.console.models.QueryModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 */
@Data
@NoArgsConstructor
public class QueryUserListReqModel extends QueryModel {

    @ApiModelProperty(value = "用户名称", dataType = "String", required = false)
    private String userName;
    @ApiModelProperty(value = "手机号", dataType = "String", required = false)
    private String phonenumber;
    @ApiModelProperty(value = "开始时间", dataType = "String", required = false)
    private String beginTime;
    @ApiModelProperty(value = "结束时间", dataType = "String", required = false)
    private String endTime;
    @ApiModelProperty(value = "部门ID", dataType = "String", required = false)
    private String deptId;
}

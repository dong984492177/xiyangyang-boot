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
public class QueryRoleListReqModel extends QueryModel {

    @ApiModelProperty(value = "角色名称", dataType = "String", required = false)
    private String roleName;
    @ApiModelProperty(value = "权限", dataType = "String", required = false)
    private String roleKey;
    @ApiModelProperty(value = "是否禁用", dataType = "String", required = false)
    private int isDelete=0;
    @ApiModelProperty(value = "开始时间", dataType = "String", required = false)
    private String beginTime;
    @ApiModelProperty(value = "结束时间", dataType = "String", required = false)
    private String endTime;
}

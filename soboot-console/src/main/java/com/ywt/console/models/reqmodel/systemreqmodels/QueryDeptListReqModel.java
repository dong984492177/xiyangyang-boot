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
public class QueryDeptListReqModel extends QueryModel {

    @ApiModelProperty(value = "父ID", dataType = "int", required = false)
    private Integer parentId;
    @ApiModelProperty(value = "部门名称", dataType = "String", required = false)
    private String deptName;
    @ApiModelProperty(value = "是否禁用", dataType = "String", required = false)
    private Integer isDelete=0;
}

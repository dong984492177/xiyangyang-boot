package com.ywt.console.models.reqmodel.systemreqmodels;

import com.ywt.console.models.QueryModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 */
@Data
@NoArgsConstructor
public class QueryExcludeDeptListReqModel extends QueryModel {

    @ApiModelProperty(value = "部门ID", dataType = "Integer", required = true)
    @NotNull
    private Integer deptId;
}

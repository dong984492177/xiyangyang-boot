package com.ywt.console.models.reqmodel.systemreqmodels;

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
public class AddDeptReqModel {

    @ApiModelProperty(value = "部门名称")
    private String deptName;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "状态")
    private Integer isDelete;

    @ApiModelProperty(value = "排序")
    private Integer orderNum;

    @ApiModelProperty(value = "父ID")
    private Integer parentId;

    @ApiModelProperty(value = "领导")
    private String leader;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "备注")
    private String remark;
}

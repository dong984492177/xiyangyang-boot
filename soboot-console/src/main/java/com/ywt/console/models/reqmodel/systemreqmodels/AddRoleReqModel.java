package com.ywt.console.models.reqmodel.systemreqmodels;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 */
@Data
@NoArgsConstructor
public class AddRoleReqModel {

    @ApiModelProperty(value = "权限字符串")
    private String roleKey;

    @ApiModelProperty(value = "角色名称")
    private String roleName;

    @ApiModelProperty(value = "排序")
    private Integer roleSort;

    @ApiModelProperty(value = "是否代理商 0否 1是")
    private Integer agent;

    @ApiModelProperty(value = "菜单权限ID")
    private List<Integer> menuIds;

    @ApiModelProperty(value = "是否删除")
    private Integer isDelete;

    @ApiModelProperty(value = "备注")
    private String remark;
}

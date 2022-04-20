package com.ywt.console.models.reqmodel.systemreqmodels;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 * @Copyright: 云网通信息科技
 */
@Data
@NoArgsConstructor
public class UpdateUserReqModel {

    @ApiModelProperty(value = "id", required = true)
    @NotNull(message = "ID不能为空")
    private Integer id;

    @ApiModelProperty(value = "用户名称")
    @NotEmpty(message = "名称不能为空")
    private String userName;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "状态")
    @NotNull(message = "状态不能为空")
    private Integer isDelete;

    @ApiModelProperty(value = "性别")
    @NotNull(message = "性别不能为空")
    private Integer gender;

    @ApiModelProperty(value = "部门ID")
    @NotNull(message = "部门ID不能为空")
    private Integer deptId;

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "手机号")
    @NotEmpty(message = "名称不能为空")
    private String phonenumber;

    @ApiModelProperty(value = "角色ID")
    @NotNull(message = "角色ID不能为空")
    private List<Integer> roleIds;

    @ApiModelProperty(value = "酒店ID")
    private List<Integer> hotelIds;

    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty("是否是管理员")
    private Integer isDeptAdmin;
    @ApiModelProperty("是否可以查看所有酒店(0:不是1: 是)")
    private Integer isQueryAllHotel;
    @ApiModelProperty("删除时间")
    private Date deleteTime;
}

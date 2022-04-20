package com.ywt.console.models.resmodel.systemresmodels;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class QueryUserDetailResModel {

    @ApiModelProperty(value = "部门id")
    private Integer id;

    @ApiModelProperty(value = "用户姓名")
    private String userName;

    @ApiModelProperty(value = "用户昵称")
    private String nickName;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "手机号")
    private String phonenumber;

    @ApiModelProperty(value = "性别")
    private Integer gender;

    @ApiModelProperty(value = "部门ID")
    private Integer deptId;

    @ApiModelProperty(value = "头像地址")
    private String avatar;

    @ApiModelProperty(value = "状态")
    private Integer isDelete;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    private List<Integer> roleIds;

    private List<Integer> hotelIds;
    @ApiModelProperty(value = "是否为超级管理员")
    private Boolean isAdminByRoleKey;
    @ApiModelProperty(value = "是否为管理员")
    private Integer isDeptAdmin;
    @ApiModelProperty(value = "是否可以查看所有酒店(0:不是1: 是)")
    private Integer isQueryAllHotel;

}

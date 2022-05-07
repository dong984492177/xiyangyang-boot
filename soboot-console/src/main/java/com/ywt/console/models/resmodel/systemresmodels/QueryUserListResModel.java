package com.ywt.console.models.resmodel.systemresmodels;

import com.ywt.console.entity.SysDept;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 */
@Data
@NoArgsConstructor
public class QueryUserListResModel {

    @ApiModelProperty(value = "部门id")
    private Integer id;

    @ApiModelProperty(value = "用户姓名")
    private String userName;

    @ApiModelProperty(value = "用户对应的角色名称")
    private String roleName;

    @ApiModelProperty(value = "用户昵称")
    private String nickName;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "手机号")
    private String phonenumber;

    @ApiModelProperty(value = "性别")
    private Integer gender;

    @ApiModelProperty(value = "头像地址")
    private String avatar;

    @ApiModelProperty(value = "状态")
    private Integer isDelete;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "部门")
    private SysDept dept;

    private String isLock;
    private Integer isDeptAdmin;
    private Integer allyId;
    @ApiModelProperty(value = "删除时间")
    private Date deleteTime;
}

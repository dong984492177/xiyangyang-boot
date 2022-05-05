package com.ywt.console.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 * @Copyright: 云网通信息科技
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "SysUser对象", description = "用户信息表")
public class SysUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "部门ID")
    private Integer deptId;

    @ApiModelProperty(value = "用户账号")
    private String userName;

    @ApiModelProperty(value = "用户昵称")
    private String nickName;

    @ApiModelProperty(value = "用户类型（00系统用户）")
    private String userType;

    @ApiModelProperty(value = "用户邮箱")
    private String email;

    @ApiModelProperty(value = "手机号码")
    private String phonenumber;

    @ApiModelProperty(value = "用户性别（0男 1女 2未知）")
    private Integer gender;

    @ApiModelProperty(value = "头像地址")
    private String avatar;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "最后登陆IP")
    private String loginIp;

    @ApiModelProperty(value = "最后登陆时间")
    private Date loginDate;

    @ApiModelProperty(value = "酒店id")
    private String hotelId;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "逻辑删除标记字段 其他:禁用 0：激活")
    private Integer isDelete;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "创建人")
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private Integer createBy;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "更新人")
    @TableField(value = "update_by", fill = FieldFill.INSERT_UPDATE)
    private Integer updateBy;

    @ApiModelProperty(value = "账号是否锁定")
    @TableField(value = "is_lock")
    private String isLock;

    @TableField(value = "is_dept_admin")
    private Integer isDeptAdmin;

    @TableField("delete_time")
    private Date deleteTime;

    @TableField(exist = false)
    private String roleName;
}

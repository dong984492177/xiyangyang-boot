package com.ywt.console.entity.log;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ywt.common.model.BaseModel;

import lombok.Data;

/**
 * @Author: huangchaoyang
 * @Description: 登录日志
 * @Version: 1.0
 * @Create: 2021/1/12
 * @Copyright: 云网通信息科技
 */
@Data
@TableName("sys_login_log")
public class LoginLog extends BaseModel {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String targetId;

    private String targetValue;

    private String remoteAddr;

    private String userAgent;

    private String origin;

    private String loginStatus;

    private Date createTime;

}

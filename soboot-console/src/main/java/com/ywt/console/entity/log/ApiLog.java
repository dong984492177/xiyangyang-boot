package com.ywt.console.entity.log;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ywt.common.model.BaseModel;

import lombok.Data;

/**
 * @Author: huangchaoyang
 * @Description: api日志
 * @Version: 1.0
 * @Create: 2021/1/12
 * @Copyright: 云网通信息科技
 */
@Data
@TableName("sys_api_log")
public class ApiLog extends BaseModel {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String businessType;

    private String targetType;

    private String targetValue;

    private String operationType;

    private String operationValue;

    private String remoteAddr;

    private String userAgent;

    private String requestUri;

    private String method;

    private Date requestTime;

    private String params;

    private String exception;

    private String targetName;

}

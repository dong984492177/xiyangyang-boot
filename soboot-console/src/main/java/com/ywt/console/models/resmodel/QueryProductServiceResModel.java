package com.ywt.console.models.resmodel;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 * @Copyright: 云网通信息科技
 */
@Data
@NoArgsConstructor
public class QueryProductServiceResModel {

    @ApiModelProperty(value = "id")
    private Integer id;
    @ApiModelProperty(value = "产品ID")
    private Integer productId;
    @ApiModelProperty(value = "服务名称")
    private String serviceName;
    @ApiModelProperty(value = "控制code")
    private String serviceCode;
    @ApiModelProperty(value = "控制code值")
    private String serviceValue;
    @ApiModelProperty(value = "指令类型")
    private Integer serviceType;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @ApiModelProperty(value = "产品名称")
    private String productName;
    @ApiModelProperty(value = "产品线")
    private Integer productLine;
}

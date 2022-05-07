package com.ywt.console.models.reqmodel;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 */
@Data
@NoArgsConstructor
public class AddProductServiceReqModel {

    @ApiModelProperty(value = "产品ID", required = true)
    @NotNull(message = "产品ID不能为空")
    private Integer productId;

    @ApiModelProperty(value = "服务名称", required = true)
    @NotEmpty(message = "请输入正确的服务名称")
    private String serviceName;

    @ApiModelProperty(value = "控制code")
    @NotEmpty(message = "请输入正确的控制code")
    private String serviceCode;

    @ApiModelProperty(value = "控制code值")
    private String serviceValue;

    @ApiModelProperty(value = "指令类型")
    private Integer serviceType;

    @ApiModelProperty(value = "备注")
    private String remark;

}

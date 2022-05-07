package com.ywt.console.models.reqmodel;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 */
@Data
@NoArgsConstructor
public class AddProductReqModel {

    @ApiModelProperty(value = "产品类别", required = true)
    @NotEmpty(message = "请输入正确的酒店ID")
    private String categoryKey;

    @ApiModelProperty(value = "名称", required = true)
    @NotEmpty(message = "请输入正确的名称")
    private String name;

    @ApiModelProperty(value = "设备型号", required = true)
    @NotEmpty(message = "请输入正确的设备型号")
    private String productModel;

    @ApiModelProperty(value = "固件版本", required = true)
    private String firmwareVersion;

    @ApiModelProperty(value = "协议")
    private String protocol;

    @ApiModelProperty(value = "软件版本")
    private String softwareVersion;

    @ApiModelProperty(value = "硬件版本")
    private String hardwareVersion;

    @ApiModelProperty(value = "制造商")
    private String manufacturer;

    @ApiModelProperty(value = "网络类型")
    private String networkType;

    @ApiModelProperty(value = "备注")
    private String remark;

}

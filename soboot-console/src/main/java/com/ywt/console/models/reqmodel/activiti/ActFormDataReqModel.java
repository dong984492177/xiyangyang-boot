package com.ywt.console.models.reqmodel.activiti;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2022-05-06 14:30:06
 * @Copyright: 互邦老宝贝
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("动态表单请求model")
public class ActFormDataReqModel {

    /** 表单id */
    @ApiModelProperty(name="表单id")
    private String controlId;
    private String controlType;



    /** 表单名称 */
    @ApiModelProperty(name="表单名称")
    private String controlLable;
    private String controlIsParam;

    /** 表单值 */
    @ApiModelProperty(name="表单值")
    private String controlValue;
    private String controlDefault;
}

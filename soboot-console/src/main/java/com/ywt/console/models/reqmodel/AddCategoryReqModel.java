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
 * @Copyright: 云网通信息科技
 */
@Data
@NoArgsConstructor
public class AddCategoryReqModel {

    @ApiModelProperty(value = "产品类别", required = true)
    @NotEmpty(message = "产品类别不能为空")
    private String categoryKey;

    @ApiModelProperty(value = "产品编码", required = true)
    private String categoryCode;

    @ApiModelProperty(value = "产品类别名称", required = true)
    @NotEmpty(message = "产品类别名称不能为空")
    private String categoryName;
}

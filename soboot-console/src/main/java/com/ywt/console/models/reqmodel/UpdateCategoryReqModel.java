package com.ywt.console.models.reqmodel;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 * @Copyright: 云网通信息科技
 */
@Data
@NoArgsConstructor
public class UpdateCategoryReqModel {


    @ApiModelProperty(value = "id", required = true)
    @NotNull(message = "ID不能为空")
    private Integer id;

    @ApiModelProperty(value = "产品类别")
    private String categoryKey;

    @ApiModelProperty(value = "产品编码")
    private String categoryCode;

    @ApiModelProperty(value = "产品类别名称")
    private String categoryName;

    @ApiModelProperty(value = "产品别名")
    private String aliasName;
}

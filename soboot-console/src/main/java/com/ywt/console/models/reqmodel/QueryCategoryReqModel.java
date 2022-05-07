package com.ywt.console.models.reqmodel;

import com.ywt.console.models.QueryModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 */
@EqualsAndHashCode(callSuper = false)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class QueryCategoryReqModel extends QueryModel {

    @ApiModelProperty(value = "类别", dataType = "string")
    private String categoryKey;
    @ApiModelProperty(value = "编码")
    private String categoryCode;
}

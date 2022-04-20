package com.ywt.console.models.reqmodel;

import com.ywt.console.models.QueryModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 * @Copyright: 云网通信息科技
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class QueryProductReqModel extends QueryModel {

    @ApiModelProperty(value = "产品类别", dataType = "string")
    private String categoryKey;

    @ApiModelProperty(value = "名称", dataType = "string")
    private String name;

    @ApiModelProperty(value = "设备型号", dataType = "string")
    private String productModel;

    @ApiModelProperty(value = "产品线", dataType = "int")
    private Integer productLine;

    public QueryProductReqModel(Integer id) {
        this.setId(id);
    }
}

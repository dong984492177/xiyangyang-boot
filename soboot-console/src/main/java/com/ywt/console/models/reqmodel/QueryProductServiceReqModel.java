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
public class QueryProductServiceReqModel extends QueryModel {

    @ApiModelProperty(value = "产品ID", dataType = "int")
    private Integer productId;

    @ApiModelProperty(value = "服务名称", dataType = "string")
    private String serviceName;

    @ApiModelProperty(value = "类别", dataType = "int")
    private String categoryKey;

    private Integer productLine;

    private String serviceCode;

    private String serviceValue;
}

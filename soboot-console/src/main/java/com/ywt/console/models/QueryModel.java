package com.ywt.console.models;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 */
@Data
@NoArgsConstructor
public class QueryModel {

    @ApiModelProperty(value = "id", dataType = "int")
    private Integer id;

    @ApiModelProperty(value = "页数", dataType = "int")
    private Integer pageNo = 1;

    @ApiModelProperty(value = "每页条数", dataType = "int")
    private Integer pageSize = 20;

    @ApiModelProperty(value = "排序", dataType = "string")
    private String orderBy;

    //降序还是升序
    private String sort;

}

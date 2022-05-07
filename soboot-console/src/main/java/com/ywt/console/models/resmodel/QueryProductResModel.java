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
 */
@Data
@NoArgsConstructor
public class QueryProductResModel {


    @ApiModelProperty(value = "id")
    private Integer id;
    @ApiModelProperty(value = "产品类别")
    private String categoryKey;
    @ApiModelProperty(value = "名称")
    private String name;
    @ApiModelProperty(value = "产品线")
    private Integer productLine;
    @ApiModelProperty(value = "设备型号")
    private String productModel;
    @ApiModelProperty(value = "制造商")
    private String manufacturer;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
}

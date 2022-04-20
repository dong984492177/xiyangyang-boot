package com.ywt.console.models.resmodel;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 * @Copyright: 云网通信息科技
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QueryCategoryResModel {

    @ApiModelProperty(value = "Id")
    private Integer id;

    @ApiModelProperty(value = "类型")
    private String categoryKey;

    @ApiModelProperty(value = "编码")
    private String categoryCode;

    @ApiModelProperty(value = "类型名称")
    private String categoryName;

    @ApiModelProperty(value = "产品别名")
    private String aliasName;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    private String categoryId;

    private Integer isDelete;

    private Integer productLine;
}

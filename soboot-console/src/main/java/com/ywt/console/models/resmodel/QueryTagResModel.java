package com.ywt.console.models.resmodel;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
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
@Builder
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class QueryTagResModel implements Serializable {

    @ApiModelProperty(value = "标签id")
    private Integer id;

    @ApiModelProperty(value = "酒店编码")
    private Integer hotelId;

    @ApiModelProperty(value = "酒店编码")
    private Integer categoryId;

    @ApiModelProperty(value = "标签名称")
    private String tagName;

    @ApiModelProperty(value = "产品类别")
    private String categoryKey;

    @ApiModelProperty(value = "产品类别名称")
    private String categoryName;

    @ApiModelProperty(value = "创建时间")
    private Date  createTime;

    @ApiModelProperty(value = "备注")
    private String remark;
}

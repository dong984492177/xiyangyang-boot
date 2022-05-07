package com.ywt.console.models.reqmodel;

import com.ywt.console.models.QueryModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 */
@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
@ToString(callSuper = true)
public class QueryTagConfigReqModel extends QueryModel {

    @ApiModelProperty(value = "tagName")
    private  String tagName;

    private  String categoryId;

    private int productLine;

    private Integer hotelId;
}

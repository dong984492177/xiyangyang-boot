package com.ywt.console.models.reqmodel;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

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
public class AddTagReqModel {

    @ApiModelProperty(value = "标签id")
    private Integer id;

    @ApiModelProperty(value = "标签名称")
    @Length(max = 20,message = "超过最大长度20")
    private String tagName;

    @ApiModelProperty(value = "酒店编码")
    @NotNull(message = "酒店编码不能为空")
    private Integer hotelId;

    @ApiModelProperty(value = "产品类别")
    private Integer categoryId;

    @ApiModelProperty(value = "备注")
    @Length(max = 100,message = "超过最大长度100")
    private String remark;

    private Integer tagId;
}

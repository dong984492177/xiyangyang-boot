package com.ywt.console.models.reqmodel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 * @Copyright: 云网通信息科技
 */
@Data
@ApiModel("图片文件的base64请求对象")
@NoArgsConstructor
public class FileBase64ReqModel {
    @ApiModelProperty(value = "图片文件的base64字符串", required = true)
    private List<String> arrBase64;
}

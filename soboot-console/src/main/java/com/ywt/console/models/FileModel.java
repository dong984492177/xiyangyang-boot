package com.ywt.console.models;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;


@Data
@ApiModel("文件实体类")
public class FileModel {

    @NotBlank(message = "文件不能为空")
    @ApiModelProperty(value = "base64串", dataType = "string")
    private String file;

    @NotBlank(message = "文件名不能为空")
    @ApiModelProperty(value = "文件名", dataType = "string")
    private String fileName;
}

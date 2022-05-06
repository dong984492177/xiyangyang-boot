package com.ywt.console.models.activiti;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2022-05-06 15:51:06
 * @Copyright: 互邦老宝贝
 */
@Data
@Builder
@ApiModel("")
public class HistoryTaskResModel {

    @ApiModelProperty("任务节点名称")
    private String taskNodeName;

    @ApiModelProperty("创建者姓名")
    private String createName;

    @ApiModelProperty("创建时间")
    private String createdDate;

    @ApiModelProperty("子集合")
    private List<TaskFormDataResModel> formDataResModelList;
}

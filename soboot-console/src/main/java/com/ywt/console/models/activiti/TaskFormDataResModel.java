package com.ywt.console.models.activiti;

import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2022-05-06 15:52:06
 * @Copyright: 互邦老宝贝
 */
@Data
@Builder
@ApiModel
public class TaskFormDataResModel {

    private String title;

    private String value;
}

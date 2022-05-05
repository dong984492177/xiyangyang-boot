package com.ywt.console.models;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 * @Copyright: 云网通信息科技
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeleteModel {

    @ApiModelProperty(value = "ids", required = true)
    @NotNull(message = "ID不能为空")
    private List<Integer> ids;
}

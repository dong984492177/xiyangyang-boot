package com.ywt.console.models;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
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
public class UserAddReqModel {

    @ApiModelProperty(value = "用户名称")
    private String name;

    @ApiModelProperty(value = "手机号")
    @NotEmpty(message = "手机号不能为空")
    private String phone;

    @ApiModelProperty(value = "要添加的手机号")
    @NotEmpty(message = "手机号不能为空")
    private String oprPhone;

    @ApiModelProperty(value = "级别")
    private Integer level;

    @ApiModelProperty(value = "角色")
    private Integer role;

    @ApiModelProperty(value = "过期日期")
    private Long invalidTime;

}

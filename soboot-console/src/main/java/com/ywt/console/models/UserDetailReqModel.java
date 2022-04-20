package com.ywt.console.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 * @Copyright: 云网通信息科技
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailReqModel {

    @NotNull(message = "用户手机号")
    private String phone;
}

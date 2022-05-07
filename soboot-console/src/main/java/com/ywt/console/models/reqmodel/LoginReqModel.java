package com.ywt.console.models.reqmodel;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 */
@Data
@NoArgsConstructor
public class LoginReqModel {

    private String account;

    private String password;

}

package com.ywt.gateway.model;

import java.io.Serializable;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 */
@Getter
@Setter
@Builder
public class ResponseInfo implements Serializable {
    private String requestId;
    private Object result;
}

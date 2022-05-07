package com.ywt.console.models.resmodel;

import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class QueryTagNameResModel {

    // 设备ID
    private int deviceId;
    // 端口ID
    private int portId;
    private String deviceName;
    private String tagName;
    // 设备mac地址
    private String macAddress;
    private String port;
    private Integer productId;

}

package com.ywt.console.service.websocketservice;

import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder
@AllArgsConstructor
public class WebSocketMessage {
    private String msg;
    // private String macAddress;
}

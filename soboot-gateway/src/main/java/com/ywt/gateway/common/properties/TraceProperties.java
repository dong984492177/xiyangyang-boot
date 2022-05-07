package com.ywt.gateway.common.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author: huangchaoyang
 * @Description: 开启链路追踪配置
 * @Version: 1.0
 * @Create: 2021/1/12
 */
@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "xiot.trace")
public class TraceProperties {
    /**
     * 是否开启日志链路追踪
     */
    private boolean open = false;

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }
}

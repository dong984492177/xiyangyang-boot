package com.ywt.gateway.filter;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

/**
 * @Author: huangchaoyang
 * @Description: 过滤打印拦截器
 * @Version: 1.0
 * @Create: 2021/1/12
 */
public class SimpleFilter extends Filter<ILoggingEvent> {

    private String flag;

    @Override
    public FilterReply decide(ILoggingEvent event) {
        if (event.getMessage().contains(flag)) {
            return FilterReply.ACCEPT;
        } else {
            return FilterReply.NEUTRAL;
        }
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}

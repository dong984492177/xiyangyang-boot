package com.ywt.gateway.filter;

import com.ywt.gateway.common.constant.LogConstant;
import com.ywt.gateway.common.properties.TraceProperties;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;


/**
 * @Author: huangchaoyang
 * @Description: 日志链路追踪
 * @Version: 1.0
 * @Create: 2021/1/12
 */
@Component
public class TraceFilter implements GlobalFilter, Ordered {

    @Autowired
    private TraceProperties traceProperties;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        if (traceProperties.isOpen()) {
            //可以加入埋点
            //生成日志id,id必须全局唯一
            String traceId = UUID.randomUUID().toString();//@TODO
            MDC.put(LogConstant.LOG_TRACE_ID, traceId);
            ServerHttpRequest serverHttpRequest = exchange.getRequest().mutate()
                    .headers(h -> h.add(LogConstant.TRACE_ID_HEADER, traceId))
                    .build();

            ServerWebExchange build = exchange.mutate().request(serverHttpRequest).build();
            return chain.filter(build);
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 1;
    }

}

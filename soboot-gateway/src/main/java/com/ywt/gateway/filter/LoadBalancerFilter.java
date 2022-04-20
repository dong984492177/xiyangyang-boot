package com.ywt.gateway.filter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;



import static org.springframework.cloud.gateway.filter.LoadBalancerClientFilter.LOAD_BALANCER_CLIENT_FILTER_ORDER;


/**
 * @Author: huangchaoyang
 * @Description: 控制服务调用(不同版本）适用于灰度发布
 * @Version: 1.0
 * @Create: 2021/1/12
 * @Copyright: 云网通信息科技
 */
public class LoadBalancerFilter {

   /* public LoadBalancerFilter(LoadBalancerClient loadBalancer, LoadBalancerProperties properties) {
        super(loadBalancer, properties);
    }*/

    /*@Override
    public int getOrder() {
        return LOAD_BALANCER_CLIENT_FILTER_ORDER;
    }

    *//*@Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return chain.filter(exchange);
    }*//*

   *//* @Override
    protected ServiceInstance choose(ServerWebExchange exchange) {
        HttpHeaders headers = exchange.getRequest().getHeaders();
        return loadBalancer.choose(((URI) exchange.getAttribute(GATEWAY_REQUEST_URL_ATTR)).getHost());
    }*//*

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

    *//*    System.out.println(Thread.currentThread().getName());
        String version = exchange.getRequest().getHeaders().getFirst("Version");
        if (StringUtils.isNotBlank(version)) {
            RibbonFilterContext currentContext = RibbonFilterContextHolder.getCurrentContext();
            currentContext.add("Version", version);
        }
        Mono<Void> mono = chain.filter(exchange).subscriberContext(ctx -> ctx.put("Version", version));
        return mono;*//*
        return null;
    }
*/
}

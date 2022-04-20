package com.ywt.gateway.filter;

import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import com.ywt.gateway.decorator.RecorderServerHttpRequestDecorator;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.alibaba.fastjson.JSON;
import com.ywt.common.base.util.CoreDateUtils;
import com.ywt.gateway.model.RequestInfo;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 * @Copyright: 云网通信息科技
 */
//@Component
@Slf4j
public class AccessLogGlobalFilter implements GlobalFilter, Ordered {

    private Logger requestLog = LoggerFactory.getLogger("requestLog");

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        RequestInfo requestInfo = new RequestInfo();
        Date  startTime = new Date();

        //封装body数据存在异步失效问题
        RecorderServerHttpRequestDecorator requestDecorator = new RecorderServerHttpRequestDecorator(request);
        //InetSocketAddress address = requestDecorator.getRemoteAddress();
        HttpMethod method = requestDecorator.getMethod();
        URI url = requestDecorator.getURI();
        HttpHeaders headers = requestDecorator.getHeaders();
        //读取requestBody传参
       /* Object cachedRequestBodyObject = exchange.getAttributes().get(FilterConstant.CACHED_REQUEST_BODY_OBJECT_KEY);
        if (cachedRequestBodyObject != null) {
            byte[] data = (byte[]) cachedRequestBodyObject;
            String body = new String(data);
            requestInfo.setParamBody(body);
        }*/

        try {
            if(exchange.getRequest().getHeaders().getContentType() != null) {
                Flux<DataBuffer> body = request.getBody();
                StringBuilder sb = new StringBuilder();
                body.subscribe(buffer -> {
                    byte[] bytes = new byte[buffer.readableByteCount()];
                    buffer.read(bytes);
                    String bodyString = new String(bytes, StandardCharsets.UTF_8);
                    sb.append(bodyString);
                    requestInfo.setParamBody(sb.toString());
                });
            }
        }catch(Exception e){
            log.info("Only one connection receive subscriber allowed");
        }
        requestInfo.setAuthCode(String.valueOf(headers.get("Authorization")));
        requestInfo.setRemoteAddr(returnIp(request));
        requestInfo.setHttpHost(headers.getHost().getHostString());
        requestInfo.setRequestTime(CoreDateUtils.formatDateTime(startTime));
        StringBuilder builder = new StringBuilder(url.getPath());
        builder.append(" ").append(method.name());
        requestInfo.setRequest(builder.toString());

        ServerHttpResponse response = exchange.getResponse();

        DataBufferFactory bufferFactory = response.bufferFactory();
        ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(response) {
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                if (body instanceof Flux) {
                    Flux<? extends DataBuffer> fluxBody = (Flux<? extends DataBuffer>) body;
                    return super.writeWith(fluxBody.map(dataBuffer -> {
                        Date endTime = new Date();
                        byte[] content = new byte[dataBuffer.readableByteCount()];
                        dataBuffer.read(content);
                        String responseResult = new String(content, Charset.forName("UTF-8"));
                        requestInfo.setStatus(this.getStatusCode().toString());
                        long durationMills = CoreDateUtils.duration(startTime,endTime);
                        requestInfo.setRequestTimeCount(durationMills/1000);
                        //normalMsg.append(";header=").append(this.getHeaders());
                        //normalMsg.append(";body_bytes_sent=").append(responseResult);
                        requestInfo.setBodySent(responseResult);
                        //写入文件
                        requestLog.warn(JSON.toJSONString(requestInfo));
                        return bufferFactory.wrap(content);
                    }));
                }
                return super.writeWith(body);
            }
        };

        return chain.filter(exchange.mutate().request(requestDecorator).response(decoratedResponse).build());
    }

    @Override
    public int getOrder() {
        return -2;
    }

    private String returnIp(ServerHttpRequest request){
        HttpHeaders headers = request.getHeaders();
        String ip = headers.getFirst("x-forwarded-for");
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            if (ip.indexOf(",") != -1) {
                ip = ip.split(",")[0];
            }
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddress().getAddress().getHostAddress();
        }
        return ip;
    }
}

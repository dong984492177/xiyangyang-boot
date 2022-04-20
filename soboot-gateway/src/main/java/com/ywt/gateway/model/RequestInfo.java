package com.ywt.gateway.model;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 * @Copyright: 云网通信息科技
 */
@Data
public class RequestInfo implements Serializable {

    /**
     * 客户端地址
     */
    private String remoteAddr;

    /**
     * 客户端用户名称
     */
    private String remoteUser;

    /**
     * 访问时间和时区
     */
    private String timeLocal;

    /**
     * 请求的URI和HTTP协议 "GET /article-10000.html HTTP/1.1"
     */
    private String request;

    /**
     * 请求地址
     */
    private String httpHost;

    /**
     * HTTP请求状态
     */
    private String status;
    /**
     * body_bytes_sent 发送给客户端文件内容大小
     */
    private String  bodySent;
    /**
     * 请求来源
     */
    private String httpReferer;
    /**
     * 终端浏览器等信息
     */
    private String httpUserAgent;
    /**
     * 请求时间
     */
    private String requestTime;
    /**
     * 请求总时间
     */
    private long requestTimeCount;

    private String authCode;

    /**
     *  发送给服务端内容大小
     */
    private String  paramBody;
}

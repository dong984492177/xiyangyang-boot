package com.ywt.common.base.constant;

/**
 * @Author: huangchaoyang
 * @Description: 全局常量
 * @Version: 1.0
 * @Create: 2021/1/12
 */
public class XiotConstant {

    public static final String LOCK_PREFIX = "__LOCK__";

    public static final String LOCK_SEPARATOR = ":";

    public static final String SERVICE_VALUE_SEPARATOR = ":";

    public static final String JWT_TOKEN_HEADER = "Authorization";

    // 需要验证权限的TOKEN前缀
    public static final String JWT_TOKEN_PREFIX = "Bearer ";

    // 不需要验证权限的TOKEN前缀
    public static final String JWT_UNAUTHORIZATION_TOKEN_PREFIX = "TOKEN ";

    public static final Long JWT_TOKEN_TIME = 1000L * 60 * 60 * 3;

    public static final String JWT_PERMISSON = "PERMISSON";

    public static final String JWT_ROUTERS = "ROUTERS";

    public static final String VERIFY_SIGN = "verifySign";

    public static final String VERIFY_TIME = "verifyTime";

    public static final String KUNLUN_HEAD_GATEWAY = "gatewayMacAddress";

    public static final String WEB_SOCKET_CLIENT_LINKED = "web_socket_client_linked";

    public static final String WEB_SOCKET_CLIENT_DEVICE_LINKED = "web_socket_client_device_linked";
}

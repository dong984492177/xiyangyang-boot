package com.ywt.common.bean;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 */
public abstract class ClientRequestContextHolder {

    private final static ThreadLocal<ClientRequestContext> clientRequestContextThreadLocal = new ThreadLocal<>();

    public static ClientRequestContext current() {
        ClientRequestContext clientRequestContext = clientRequestContextThreadLocal.get();
        if (clientRequestContext == null) {
            clientRequestContext = new ClientRequestContext();
            clientRequestContextThreadLocal.set(clientRequestContext);
        }
        return clientRequestContext;
    }

    public static void reset() {
        clientRequestContextThreadLocal.remove();
    }
}

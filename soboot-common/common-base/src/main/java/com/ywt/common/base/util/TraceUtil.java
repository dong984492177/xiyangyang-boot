package com.ywt.common.base.util;

import java.util.UUID;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 * @Copyright: 云网通信息科技
 */
public class TraceUtil {
    public static String getRequestId() {
        String requestId = UUID.randomUUID().toString();;
        return requestId;
    }
}

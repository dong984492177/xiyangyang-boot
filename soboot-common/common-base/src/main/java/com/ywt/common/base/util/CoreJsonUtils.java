package com.ywt.common.base.util;

import com.alibaba.fastjson.JSON;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 */
public class CoreJsonUtils {
    public static String objToString(Object obj) {
        return JSON.toJSONString(obj);
    }

    public static Object stringToObj(String str) {
        return JSON.parse(str);
    }

}

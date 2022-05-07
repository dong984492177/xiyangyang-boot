package com.ywt.gateway.filter;

import java.util.Map;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 */
public interface RibbonFilterContext {

    RibbonFilterContext add(String key, String value);

    String get(String key);

    RibbonFilterContext remove(String key);

    Map<String, String> getAttributes();
}

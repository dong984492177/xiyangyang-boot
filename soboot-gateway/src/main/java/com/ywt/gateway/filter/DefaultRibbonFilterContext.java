package com.ywt.gateway.filter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 */
public class DefaultRibbonFilterContext implements RibbonFilterContext {


    private final Map<String, String> attributes = new HashMap<>();

    /**
     * @param     key
     * @param  value
     */
    @Override
    public RibbonFilterContext add(String key, String value) {
        attributes.put(key, value);
        return this;
    }

    /**
     * @param   key
     * @return  value
     */
    @Override
    public String get(String key) {
        return attributes.get(key);
    }

    /**
     * @param key
     */
    @Override
    public RibbonFilterContext remove(String key) {
        attributes.remove(key);
        return this;
    }

    /**
     * @return
     */
    @Override
    public Map<String, String> getAttributes() {
        return Collections.unmodifiableMap(attributes);
    }
}

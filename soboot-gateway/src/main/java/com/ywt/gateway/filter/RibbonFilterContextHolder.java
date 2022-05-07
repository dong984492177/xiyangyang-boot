package com.ywt.gateway.filter;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 */
public class RibbonFilterContextHolder {

    /**
     * 存储当前上下文
     */
    private static final ThreadLocal<RibbonFilterContext> contextHolder = new InheritableThreadLocal<RibbonFilterContext>() {
        @Override
        protected RibbonFilterContext initialValue() {
            return new DefaultRibbonFilterContext();
        }
    };

    /**
     *
     * @return the current context
     */
    public static RibbonFilterContext getCurrentContext() {
        return contextHolder.get();
    }

    /**
     * 清除上下文
     */
    public static void clearCurrentContext() {
        contextHolder.remove();
    }
}

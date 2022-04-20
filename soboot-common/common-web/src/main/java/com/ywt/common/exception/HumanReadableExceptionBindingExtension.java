package com.ywt.common.exception;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 * @Copyright: 云网通信息科技
 */
public interface HumanReadableExceptionBindingExtension {

    Iterable<Class<? extends Throwable>> getBindings();
}

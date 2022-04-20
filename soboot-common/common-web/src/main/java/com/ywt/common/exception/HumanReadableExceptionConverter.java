package com.ywt.common.exception;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 * @Copyright: 云网通信息科技
 */
public interface HumanReadableExceptionConverter {

    DefaultWebException convert(Throwable e);

    boolean canConvert(Throwable e);
}

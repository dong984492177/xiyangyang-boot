package com.ywt.common.controller;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;


/**
 * @Author: huangchaoyang
 * @Description: 控制器基础支持类
 * @Version: 1.0
 * @Create: 2021/1/12
 */
public abstract class BaseController {
    /**
     * 空串
     */
    protected static String EMPTY_STR = StringUtils.EMPTY;

    /**
     * 日志对象
     */
    protected Logger log = LoggerFactory.getLogger(getClass());

    /**
     * 获得当前请求对象
     *
     * @return
     */
    protected HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }
}

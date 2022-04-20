package com.ywt.common.response;

import com.ywt.common.bean.PageWrapper;

import java.io.Serializable;

/**
 * @Author: huangchaoyang
 * @Description: 统一响应体基类
 * @Version: 1.0
 * @Create: 2021/1/12
 * @Copyright: 云网通信息科技
 */
public interface ResponseDataWrapper<T> extends Serializable {

    int getCode();

    String getMessage();

    String getDatetime();

    long getTimestamp();

    T getData();

    void setData(T data);

    String getRedirect();

    PageWrapper getPage();

    int getEncrypt();

    Integer getMessageLevel();
}


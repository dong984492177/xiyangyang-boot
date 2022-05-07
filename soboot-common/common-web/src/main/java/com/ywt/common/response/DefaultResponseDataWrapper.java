package com.ywt.common.response;

import com.ywt.common.bean.PageWrapper;
import com.ywt.common.constant.ResCodeConstant;
import lombok.Data;

/**
 * @Author: huangchaoyang
 * @Description: 统一响应体
 * @Version: 1.0
 * @Create: 2021/1/12
 */
@Data
public class DefaultResponseDataWrapper<T> implements ResponseDataWrapper<T> {

    private static final long serialVersionUID = 5997663628531833347L;
    private int code = ResCodeConstant.SUCCESS;

    private String message = "操作成功";

    /**
     * 消息级别
     */
    private Integer messageLevel;

    /**
     * 建议跳转
     */
    private String redirect;

    /**
     * GMT 时间
     */
    private String datetime;

    private long timestamp;

    /**
     * 业务数据
     */
    private T data;

    /**
     * 加密类型
     */
    private int encrypt;

    /**
     * 分页信息
     */
    private PageWrapper page;

    public static <T> DefaultResponseDataWrapper<T> success(){
        return new DefaultResponseDataWrapper<>();
    }

    public static <T> DefaultResponseDataWrapper<T> success(T data){
        DefaultResponseDataWrapper<T> wrapper = new DefaultResponseDataWrapper<>();
        wrapper.setData(data);
        return wrapper;
    }

    public static <T> DefaultResponseDataWrapper<T> success(int code){
        DefaultResponseDataWrapper<T> wrapper = new DefaultResponseDataWrapper<>();
        wrapper.setCode(code);
        return wrapper;
    }

    public static <T> DefaultResponseDataWrapper<T> success(int code, T data){
        DefaultResponseDataWrapper<T> wrapper = new DefaultResponseDataWrapper<>();
        wrapper.setCode(code);
        wrapper.setData(data);
        return wrapper;
    }

    public static <T> DefaultResponseDataWrapper<T> fail(int code, String msg){
        DefaultResponseDataWrapper<T> wrapper = new DefaultResponseDataWrapper<>();
        wrapper.setCode(code);
        wrapper.setMessage(msg);
        return wrapper;
    }

}

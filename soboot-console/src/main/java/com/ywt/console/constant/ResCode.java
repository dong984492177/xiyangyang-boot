package com.ywt.console.constant;

import com.ywt.common.constant.ResCodeConstant;

/**
 * @Author: huangchaoyang
 * @Description: 响应结果状态码
 * @Version: 1.0
 * @Create: 2021/1/12
 */
public class ResCode extends ResCodeConstant {
    /**
     * 返回失败code
     */
    public static final Integer FAIL = 810000;
    /**
     * 创建失败
     */
    public static final Integer CREATE_ERROR = 810010;
    /**
     * 更新失败
     */
    public static final Integer UPDATE_ERROR = 810011;
    /**
     * 查询失败
     */
    public static final Integer GET_ERROR = 810012;
    /**
     * 密码错误
     */
    public static final Integer PWD_ERROR = 810013;
    /**
     * 禁用
     */
    public static final Integer FORBIDDEN = 810014;
    /**
     * TOKEN过期
     */
    public static final Integer TOKEN_OVERDUE = 810015;

    /**
     * 账户锁定
     */
    public static final Integer LOCKED = 810016;

    /**
     * 业务参数错误包括值没有
     */
    public static final Integer PARAM_ERROR = 810107;

    /**
     * 数据库操作错误
     */
    public static final Integer PERSITENCE_ERROR = 810108;

    /**
     * 空指针
     */
    public static final Integer NULLPOINTER_ERROR = 810109;

    /**
     * 上传error
     */
    public static final Integer FILEUPLOAD_ERROR = 810110;
}

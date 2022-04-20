package com.ywt.gateway.common.constant;

/**
 * @Author: huangchaoyang
 * @Description: 返回结果状态消息
 * @Version: 1.0
 * @Create: 2021/1/12
 * @Copyright: 云网通信息科技
 */
public class ResultMessage {


    // 成功
    public static final String SUCCESS = "成功";

    //获取数据成功
    public static final String GETDATASUCCESS = "获取数据成功";

    // 暂无数据
    public static final String NODATA = "暂无数据";

    // 系统错误
    public static final String SYSTEMERROR = "系统错误";

    // 超出页面查询范围
    public static final String OUTRANGEOFPAGE = "超出页面查询范围";

    //图形验证码输入错误
    public static final String VERIFICATIONCODEERROR = "图形验证码输入错误";

    //参数错误
    public static final String PARAMETERERROR = "接口参数有误，请检查输入项";

    // 登录 token 过期
    public static final String LOGINTOKENEXPIRED = "登录过期，请重新登录";

    // 文件上传超出限制
    public static final String FILESIZEOUTOFLIMITED = "文件上传超出限制";

    //
    public static final String SYSTEMEXCEPTION = "出现异常";

    public static final String INTERNALSERVERERROR = "Internal Server Error";

    public static final String SERVICENOTFOUND = "服务未找到!";

    public static final String OVERSYSTEMLIMIT = "请求频率超过流控阈值!";
}


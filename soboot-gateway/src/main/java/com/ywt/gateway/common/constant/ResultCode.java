package com.ywt.gateway.common.constant;

/**
 * @Author: huangchaoyang
 * @Description: 返回结果状态码
 * @Version: 1.0
 * @Create: 2021/1/12
 * @Copyright: 云网通信息科技
 */
public class ResultCode {

    // 成功
    public static final String SUCCESS = "0";

    // 暂无数据
    public static final String NODATA = "1";

    // 系统错误
    public static final String SYSTEMERROR = "2";

    //图形验证码
    public static final String VERIFICATIONCODE = "3";

    //接口参数有误
    public static final String PARAMETERERROR = "4";

    // 超出页面查询范围
    public static final String OUTRANGEOFPAGE = "5";

    // 登录 token 过期
    public static final String LOGINTOKENEXPIRED = "6";

    // 文件上传超出限制
    public static final String FILESIZEOUTOFLIMITED = "7";

    //业务类
    //支付来源
    public static final String PAYFROMIOS="1";
    public static final String PAYFROMANDROID="2";

    //支付方式
    public static final String PAYTYPEWX="1";
    public static final String PAYTYPEALI="2";

    //ajax请求成功
    public static final String ajaxSuccess="200";
}


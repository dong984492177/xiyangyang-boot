package com.ywt.gateway.common.result;

import com.ywt.gateway.common.constant.ResultCode;
import com.ywt.gateway.common.constant.ResultMessage;
import org.apache.commons.lang.StringUtils;

import java.util.LinkedHashMap;

/**
 * @Author: huangchaoyang
 * @Description: 自定义异常处理
 * @Version: 1.0
 * @Create: 2021/1/12
 * @Copyright: 云网通信息科技
 */
public class JsonResult {

    // 是否成功
    private boolean success = true;
    // 错误代码
    private String errorCode = "-1";
    // 提示信息
    private String msg = "操作成功";
    // 封装 json 的 map
    private LinkedHashMap<String, Object> body = new LinkedHashMap();

    /**
     * 成功情况初始化
     */
    public static JsonResult initSuccess() {
        JsonResult result = new JsonResult();
        result.setSuccess(true);
        result.setErrorCode(ResultCode.SUCCESS);
        result.setMsg(ResultMessage.SUCCESS);

        return result;
    }

    /**
     * 失败情况初始化
     */
    public static JsonResult initFailure() {
        JsonResult result = new JsonResult();
        result.setSuccess(false);
        result.setErrorCode(ResultCode.SYSTEMERROR);
        result.setMsg(ResultMessage.SYSTEMERROR);

        return result;
    }

    public LinkedHashMap<String, Object> getBody() {
        return body;
    }

    public void setBody(LinkedHashMap<String, Object> body) {
        this.body = body;
    }

    public void put(String key, Object value){//向json中添加属性，在js中访问，请调用data.map.key
        body.put(key, value);
    }

    public void remove(String key){
        body.remove(key);
    }


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {//向json中添加属性，在js中访问，请调用data.msg
        this.msg = msg;
    }


    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    /**
     * 获取常规错误的JsonResult
     */
    public static JsonResult getCommonError() {
        JsonResult result = new JsonResult();
        result.setGlobalSystemError();
        return result;
    }

    /**
     * 获取没有数据错误的JsonResult
     */
    public static JsonResult getNoDataError() {
        JsonResult result = new JsonResult();
        result.setGlobalNoData();
        return result;
    }

    /**
     * 获取没有数据错误的JsonResult
     */
    public static JsonResult getNoDataError(String msg) {
        JsonResult result = new JsonResult();
        result.setGlobalNoData();
        result.setMsg(msg);
        return result;
    }

    /**
     * 获取参数验证错误的JsonResult
     */
    public static JsonResult getParamError(String paramName) {
        JsonResult result = new JsonResult();

        if (StringUtils.isNotBlank(paramName)) {
            result.setGlobalParameterError(paramName);
        } else {
            result.setGlobalParameterError();
        }
        return result;
    }

    /**
     * 获取token失效的JsonResult
     */
    public static JsonResult getLoginTokenExpired() {
        JsonResult result = new JsonResult();
        result.setErrorCode(ResultCode.LOGINTOKENEXPIRED);
        result.setMsg(ResultMessage.LOGINTOKENEXPIRED);
        result.setSuccess(false);
        return result;
    }

    /**
     * 获取自定义错误码与错误信息的JsonResult （此JsonResult代表失败）
     * @param errorCode
     * @param msg
     * @return
     */
    public static JsonResult getCustomError(String errorCode, String msg) {
        JsonResult result = new JsonResult();
        result.setErrorCode(errorCode);
        result.setMsg(msg);
        result.setSuccess(false);
        return result;
    }

    /**
     * 统一设置 成功
     */
    public void setGlobalSuccess() {
        this.errorCode = ResultCode.SUCCESS;
        this.msg = ResultMessage.SUCCESS;
        this.success = true;
    }

    /**
     * 统一设置 暂无数据
     */
    public void setGlobalNoData() {
        this.errorCode = ResultCode.NODATA;
        this.msg = ResultMessage.NODATA;
        this.success = true;
    }

    /**
     * 统一设置	系统错误
     */
    public void setGlobalSystemError() {
        this.errorCode = ResultCode.SYSTEMERROR;
        this.msg = ResultMessage.SYSTEMERROR;
        this.success = false;
    }

    /**
     * 统一设置	系统错误，并输出异常
     */
    public void setGlobalSystemError(Exception e) {
        this.errorCode = ResultCode.SYSTEMERROR;
        this.msg = ResultMessage.SYSTEMERROR;
        this.success = false;
        this.getBody().put("系统错误，输出异常信息：", e.toString());
    }

    /**
     * 统一设置	系统错误，并打印 msg
     */
    public void setGlobalSystemError(String msg) {
        this.errorCode = ResultCode.SYSTEMERROR;
        this.msg = msg;
        this.success = false;
        System.out.println(msg);
    }

    /**
     * 统一设置 超出页面查询范围
     */
    public void setGlobalOutRangeOfPage() {
        this.errorCode = ResultCode.OUTRANGEOFPAGE;
        this.msg = ResultMessage.OUTRANGEOFPAGE;
        this.success = false;
    }

    /**
     * 统一设置 参数有误
     */
    public void setGlobalParameterError() {
        this.errorCode = ResultCode.PARAMETERERROR;
        this.msg = ResultMessage.PARAMETERERROR;
        this.success = false;
    }

    /**
     * 统一设置 参数有误
     */
    public void setGlobalParameterError(String msg) {
        this.errorCode = ResultCode.PARAMETERERROR;
        this.msg = msg;
        this.success = false;
    }

    @Override
    public String toString() {
        return "JsonResult{" +
                "success=" + success +
                ", errorCode='" + errorCode + '\'' +
                ", msg='" + msg + '\'' +
                ", body=" + body +
                '}';
    }
}

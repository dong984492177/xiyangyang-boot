package com.ywt.common.exception;

import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: huangchaoyang
 * @Description: 参数验证异常
 * @Version: 1.0
 * @Create: 2021/1/12
 * @Copyright: 云网通信息科技
 */
public class ParameterNotValidException extends DefaultWebException {

    private static final long serialVersionUID = -2815740934180973436L;

    private List<ParameterNotValidDetailItem> detailItemList = new ArrayList<>();

    public ParameterNotValidException(HttpStatus httpStatus, String errorCode, String message, Throwable cause) {
        super(httpStatus, errorCode, message, cause);
    }

    public ParameterNotValidException(String errorCode, String message) {
        this(errorCode, message, null);
    }

    public ParameterNotValidException(String errorCode, String message, Throwable cause) {
        // 默认400状态码
        this(HttpStatus.BAD_REQUEST, errorCode, message, cause);
    }

    public ParameterNotValidException() {
        this("参数验证异常");
    }

    public ParameterNotValidException(String message) {
        super(message);
    }

    public ParameterNotValidException(Throwable cause) {
        super(cause);
    }

    public ParameterNotValidException(String message, Throwable cause) {
        super(message, cause);
    }

    public List<ParameterNotValidDetailItem> getDetailItemList() {
        return detailItemList;
    }

    public void setDetailItemList(List<ParameterNotValidDetailItem> detailItemList) {
        this.detailItemList = detailItemList;
    }
}

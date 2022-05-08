package com.ywt.console.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Charsets;
import com.google.common.base.Throwables;
import com.ywt.common.base.util.CoreRequestUtils;
import com.ywt.common.bean.ClientRequestContextHolder;
import com.ywt.common.constant.AttributeKeyConstant;
import com.ywt.common.controller.BaseController;
import com.ywt.common.exception.*;
import com.ywt.common.response.DefaultResponseDataWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: huangchaoyang
 * @Description: web应用层全局异常处理者
 * @Version: 1.0
 * @Create: 2021/1/12
 */
@Slf4j
@ControllerAdvice(annotations = RestController.class)
public class GlobalApiExceptionHandler extends BaseController {

    public GlobalApiExceptionHandler() {
        log.info("异常处理器初始化成功");
    }

    @Autowired(required = false)
    private HumanReadableExceptionBindingExtension humanReadableExceptionBindingExtension;

    @Autowired(required = false)
    private List<HumanReadableExceptionConverter> humanReadableExceptionConverterList;

    @ResponseBody
    @ExceptionHandler(Throwable.class)
    public ResponseEntity<DefaultResponseDataWrapper<String>> allExceptionHandler(Throwable throwable) {
        // 进行异常分类和日志处理
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        String errorCode = "-1";
        String errorMsg = "未知错误";
        String errorDetail = null;

        if (humanReadableExceptionConverterList != null) {
            for (HumanReadableExceptionConverter converter : humanReadableExceptionConverterList) {
                if (throwable instanceof DefaultWebException) {
                    break;
                }

                if (converter.canConvert(throwable)) {
                    throwable = converter.convert(throwable);
                }
            }
        }

        Throwable e = throwable;

        try {
            if (throwable instanceof MethodArgumentNotValidException) {
                // Valid 验证参数失败的异常
                BindingResult bindingResult = ((MethodArgumentNotValidException) throwable).getBindingResult();

                if (bindingResult != null && bindingResult.getErrorCount() > 0) {
                    List<ParameterNotValidDetailItem> detailItemList = new ArrayList<>();
                    for (ObjectError objectError : bindingResult.getAllErrors()) {
                        ParameterNotValidDetailItem detailItem = new ParameterNotValidDetailItem();
                        detailItem.setMessage(objectError.getDefaultMessage());

                        if (objectError instanceof FieldError) {
                            detailItem.setField(((FieldError) objectError).getField());
                        }

                        detailItemList.add(detailItem);
                    }

                    errorDetail = JSON.toJSONString(detailItemList);
                }

                throw new ParameterNotValidException("810001", "请求参数验证失败", throwable);
            }

            if (throwable instanceof ParameterNotValidException) {
                List<ParameterNotValidDetailItem> detailItemList = ((ParameterNotValidException) throwable).getDetailItemList();
                if (detailItemList != null && !detailItemList.isEmpty()) {
                    errorDetail = JSON.toJSONString(detailItemList);
                }
            }
        } catch (Exception ex) {
            e = ex;
        }

        // 如果是有定义的异常
        if (e instanceof DefaultWebException) {
            DefaultWebException exception = (DefaultWebException) e;
            if (exception.getHttpStatus() != null) {
                httpStatus = exception.getHttpStatus();
            }
            if (StringUtils.isNotBlank(exception.getErrorCode())) {
                errorCode = exception.getErrorCode();
            }
            if (StringUtils.isNotBlank(exception.getMessage())) {
                errorMsg = exception.getMessage();
            }
            if (exception instanceof ParameterNotValidException) {
                ParameterNotValidException parameterNotValidException = (ParameterNotValidException) exception;
                List<ParameterNotValidDetailItem> detailItemList = parameterNotValidException.getDetailItemList();
                if (detailItemList != null && !detailItemList.isEmpty()) {
                    String errorDetailMessage = StringUtils.join(detailItemList.stream().map(ParameterNotValidDetailItem::getMessage).collect(Collectors.toList()), ",");
                    errorMsg += ": " + errorDetailMessage;
                }
            }
        } else if (e instanceof IllegalArgumentException) {
            // 非法参数的异常, 通常来自于Assert断言判断, 如果是正常业务会出现的断言出错应当由断言发起方给出正确的错误消息
            errorMsg = e.getMessage();
        } else {
            boolean humanReadable = false;
            if (humanReadableExceptionBindingExtension != null) {
                Iterable<Class<? extends Throwable>> exceptionBindings = humanReadableExceptionBindingExtension.getBindings();
                if (exceptionBindings != null) {
                    for (Class<? extends Throwable> binding : exceptionBindings) {
                        if (binding.isInstance(e)) {
                            errorMsg = e.getMessage();
                            humanReadable = true;
                        }
                    }
                }
            }
            if (!humanReadable) {
                errorDetail = StringUtils.substring(e.getMessage(), 0, 512);
            }
        }

        printError(e, errorCode, errorMsg, errorDetail);

        DefaultResponseDataWrapper<String> defaultResponseDataWrapper = new DefaultResponseDataWrapper<>();
        defaultResponseDataWrapper.setCode(Integer.parseInt(errorCode));
        defaultResponseDataWrapper.setMessage(errorMsg);
        defaultResponseDataWrapper.setTimestamp(new Date().getTime());
        return new ResponseEntity<>(defaultResponseDataWrapper, HttpStatus.OK);
    }

    private void printError(Throwable ex, String errorCode, String errorMsg, String errorDetail) {
        StringBuilder stringBuffer = new StringBuilder();

        //得到请求内容
        String requestBody = EMPTY_STR;
        HttpServletRequest request = getRequest();
        if (request.getAttribute(AttributeKeyConstant.REQUEST_CONTENT_BYTES) != null) {
            requestBody = new String((byte[]) request.getAttribute(AttributeKeyConstant.REQUEST_CONTENT_BYTES), Charsets.UTF_8);
        }

        //获取请求表单
        stringBuffer
                .append("URI: ").append(request.getMethod()).append(StringUtils.SPACE)
                .append(request.getRequestURI()).append(StringUtils.LF)
                .append("Parameter: ");
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String name = paramNames.nextElement();
            stringBuffer
                    .append(name).append("=")
                    .append(request.getParameter(name)).append("&");
        }
        if (stringBuffer.length() > 0) {
            stringBuffer.delete(stringBuffer.length() - 1, stringBuffer.length());
        }
        String requestParams = stringBuffer.toString();
        stringBuffer.delete(0, stringBuffer.length());

        //获取headers
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            stringBuffer
                    .append(name).append(": ")
                    .append(request.getHeader(name)).append(StringUtils.LF);
        }
        String headers = stringBuffer.toString();
        stringBuffer.delete(0, stringBuffer.length());

        //组织日志内容
        stringBuffer
                .append("接口调用出现异常")
                .append("\n*******************************")
                .append("\n[ErrorCode]")
                .append("\n%s")
                .append("\n[RequestParams]")
                .append("\n%s")
                .append("\n[RequestBody]")
                .append("\n%s")
                .append("\n[ErrorMessage]")
                .append("\n%s")
                .append("\n[ErrorDetail]")
                .append("\n%s")
                .append("\n[ExceptionStackTrace]")
                .append("\n%s")
                .append("\n[Headers]")
                .append("\n%s")
                .append("\n[AccessToken]")
                .append("\n%s")
                .append("\n[SessionToken]")
                .append("\n%s")
                .append("\n[UserToken]")
                .append("\n%s")
                .append("\n[UserId]")
                .append("\n%s")
                .append("\n[ClientIP]")
                .append("\n%s")
                .append("\n*******************************\n");

        String logInfo = String.format(stringBuffer.toString(),
                errorCode, requestParams, requestBody, errorMsg, errorDetail,
                Throwables.getStackTraceAsString(ex), headers,
                ClientRequestContextHolder.current().getAccessToken(),
                ClientRequestContextHolder.current().getSessionToken(),
                ClientRequestContextHolder.current().getUserToken(),
                ClientRequestContextHolder.current().getUserId(), CoreRequestUtils.getClientIP(request));

        log.error(logInfo);
    }

}

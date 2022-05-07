package com.ywt.console.config;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.ywt.common.base.util.StringUtils;
import com.ywt.common.response.DefaultResponseDataWrapper;
import com.ywt.console.constant.ResCode;
import com.ywt.console.exception.ConsoleException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.stream.Collectors.joining;

/**
 * @Author: huangchaoyang
 * @Description:  全局controller异常拦截
 * @Version: 1.0
 * @Create: 2021/1/12
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler implements ResponseBodyAdvice {

    static Map<String,String> map = new ConcurrentHashMap<>();

    @ExceptionHandler
    public DefaultResponseDataWrapper handler(Exception exception) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        HttpServletRequest request = (HttpServletRequest)attributes.getRequest();
        String requestURI = request.getRequestURI();
        //打印 请求信息 判断请求是什么请求
        log.info(request.getMethod());
        if (request.getMethod().equalsIgnoreCase(RequestMethod.GET.name()) || request.getMethod().equalsIgnoreCase(RequestMethod.DELETE.name())) {
            Map<String, String[]> parameterMap = request.getParameterMap();
            JSONObject paramMap = new JSONObject();
            parameterMap.forEach((key, value) -> paramMap.put(key, Arrays.stream(value).collect(joining(","))));
            log.info("请求进入-" + requestURI + "-" + request.getMethod() + ":" + paramMap.toJSONString());
        }else{
            log.info("请求进入-" + requestURI + "-" + request.getMethod());
        }

        map.put("code",ResCode.FAIL.toString());
        map.put("msg","出现未知错误");
        String message = exception.getMessage();
        log.info(message);
        if (exception instanceof ConsoleException) {
            map.put("code", ResCode.PARAM_ERROR.toString());
            map.put("msg",message);
            return DefaultResponseDataWrapper.fail(ResCode.PARAM_ERROR, exception.getMessage());
        }
        if (exception instanceof PersistenceException) {
            map.put("code",ResCode.PERSITENCE_ERROR.toString());
            map.put("msg",message);
            return DefaultResponseDataWrapper.fail(500,"数据库操作失败");
        }

        if (exception instanceof MethodArgumentNotValidException) {
            map.put("code",ResCode.PARAM_ERROR.toString());
            map.put("msg",message);
            if (((MethodArgumentNotValidException) exception).getBindingResult().getFieldErrors().size() > 0) {
                String field = ((MethodArgumentNotValidException) exception).getBindingResult().getFieldErrors().get(0).getField();
                String defaultMessage = ((MethodArgumentNotValidException) exception).getBindingResult().getFieldErrors().get(0).getDefaultMessage();
                return DefaultResponseDataWrapper.fail(500, field+":"+defaultMessage);
            }
            return DefaultResponseDataWrapper.fail(500, exception.getMessage());
        }
        if (exception.getCause() instanceof JsonMappingException) {
            map.put("code",ResCode.PARAM_ERROR.toString());
            map.put("msg",message);
            return DefaultResponseDataWrapper.fail(500,"参数错误,请检查输入的参数");
        } else if (exception instanceof MultipartException) {
            map.put("code",ResCode.FILEUPLOAD_ERROR.toString());
            map.put("msg",message);
            if ((((MultipartException) exception).getRootCause()) instanceof FileUploadBase.FileSizeLimitExceededException) {
                return DefaultResponseDataWrapper.fail(500,"上传失败:请上传小于300M的文件");
            } else {
                return DefaultResponseDataWrapper.fail(500,"上传失败");
            }
        }

        if (StringUtils.isBlank(message)) {
            message = "系统异常！";
        }

        return DefaultResponseDataWrapper.fail(-1,message);
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {

      /*  if (o instanceof DefaultResponseDataWrapper) {
            return o;
        }
        return DefaultResponseDataWrapper.fail(Integer.valueOf(ObjectUtil.isNull(map.get("code"))?"0":map.get("code")),map.get("msg"));*/
      return o;
    }
}

package com.ywt.console.config;

import com.alibaba.fastjson.JSONObject;
import com.ywt.console.exception.ConsoleException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Map;

import static java.util.stream.Collectors.joining;

/**
 * @Author: huangchaoyang
 * @Description: 请求日志aop
 * @Version: 1.0
 * @Create: 2021/1/12
 * @Copyright: 云网通信息科技
 */
@Component
@Aspect
public class RequestLogAop {
    private Logger logger = LoggerFactory.getLogger(RequestLogAop.class);

    @Around("execution(* com.ywt.console.controller..*(..))")
    public Object doAroud(ProceedingJoinPoint pjp){
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = sra.getRequest();
        String requestURI = request.getRequestURI();
        //判断请求是什么请求
        if (request.getMethod().equalsIgnoreCase(RequestMethod.GET.name()) || request.getMethod().equalsIgnoreCase(RequestMethod.DELETE.name())) {
            Map<String, String[]> parameterMap = request.getParameterMap();
            JSONObject paramMap = new JSONObject();
            parameterMap.forEach((key, value) -> paramMap.put(key, Arrays.stream(value).collect(joining(","))));
            logger.info("请求进入-" + requestURI + "-" + request.getMethod() + ":" + paramMap.toJSONString());
        } else if (request.getMethod().equalsIgnoreCase(RequestMethod.POST.name()) || request.getMethod().equalsIgnoreCase(RequestMethod.PUT.name())) {
            Object[] args = pjp.getArgs();
            StringBuilder stringBuilder = new StringBuilder();
            if (args.length != 0) {
                Arrays.stream(args).forEach(object -> {
                    if (object != null) stringBuilder.append(object.toString());
                });
            }
            if (stringBuilder.length() == 0){
                stringBuilder.append("{}");
            }
            logger.info("请求进入-" + requestURI + "-" + request.getMethod() + ":" + stringBuilder.toString());
        }
        try {
            Object proceed = pjp.proceed();
            if (proceed != null) {
                logger.info("请求返回-" + requestURI + "-" + request.getMethod() + ":" + proceed.toString());
            }
            return proceed;
        } catch (Throwable e) {
            logger.error("请求返回-" + requestURI + "-" + request.getMethod() + ":" + e.getCause().getMessage());
            throw new ConsoleException(e.toString());
        }
    }
}

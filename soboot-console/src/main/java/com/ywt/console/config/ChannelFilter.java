package com.ywt.console.config;


import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 * @Copyright: 云网通信息科技
 */
@Component
public class ChannelFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest servletRequest, HttpServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException {
        String contentType = servletRequest.getContentType();

        if (contentType != null &&
                (contentType.contains("multipart/form-data") || contentType.contains("x-www-form-urlencoded"))) {
            servletRequest.getParameterMap();
        }
        ServletRequest requestWrapper = null;
        if(servletRequest instanceof HttpServletRequest) {
             requestWrapper = new RequestWrapper(servletRequest);
        }
        filterChain.doFilter(requestWrapper, servletResponse);
    }
}

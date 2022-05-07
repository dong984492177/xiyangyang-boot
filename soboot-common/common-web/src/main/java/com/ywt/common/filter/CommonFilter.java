package com.ywt.common.filter;

import com.ywt.common.base.constant.CharsetConstant;
import com.ywt.common.base.util.CoreJsonUtils;
import com.ywt.common.exception.DefaultWebException;
import com.ywt.common.response.DefaultResponseDataWrapper;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class CommonFilter extends OncePerRequestFilter {

    protected void writerRes(HttpServletResponse response, DefaultResponseDataWrapper<String> responseModel) {
        try {
            response.setCharacterEncoding(CharsetConstant.CHARSET_UTF8);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(CoreJsonUtils.objToString(responseModel));
        } catch (IOException e) {
            throw new DefaultWebException(e);
        }
    }
}

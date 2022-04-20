package com.ywt.console.config;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 * @Copyright: 云网通信息科技
 */
public class RequestWrapper extends HttpServletRequestWrapper {

    private final byte[] bodyCopier;

    public RequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        //bodyCopier = StreamUtils.copyToByteArray(request.getInputStream());
        bodyCopier = HttpHelper.getBodyString(request).getBytes(Charset.forName("UTF-8"));
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return new ServletInputStreamCopier(bodyCopier);
    }

    public byte[] getCopy() {
        return this.bodyCopier;
    }

    public String getBody() {
        return new String(this.bodyCopier, StandardCharsets.UTF_8);
    }

}

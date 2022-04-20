package com.ywt.common.base.util;

import com.google.common.base.Charsets;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Maps;
import com.ywt.common.base.exception.UnauthorizedException;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Proxy;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Author: huangchaoyang
 * @Description: http工具类
 * @Version: 1.0
 * @Create: 2021/1/12
 * @Copyright: 云网通信息科技
 */
public class CoreHttpUtils {
    private static final Logger logger = LoggerFactory.getLogger(CoreHttpUtils.class);

    /**
     * 所有HttpMediaType定义
     */
    public enum HttpMediaType {
        TEXT_PLAIN("text/plain"),
        APPLICATION_JSON("application/json"),
        APPLICATION_XML("application/xml"),
        APPLICATION_FORM("application/x-www-form-urlencoded"),
        ;

        private String value;

        HttpMediaType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * 允许的stringBody类型post请求的MediaTypeSet
     */
    private static final Set<HttpMediaType> stringBodyHttpMediaTypes = new HashSet<>();

    static {
        stringBodyHttpMediaTypes.add(HttpMediaType.TEXT_PLAIN);
        stringBodyHttpMediaTypes.add(HttpMediaType.APPLICATION_JSON);
        stringBodyHttpMediaTypes.add(HttpMediaType.APPLICATION_XML);
    }

    private static final OkHttpClient okHttpClient = new OkHttpClient();

    /**
     * 默认超时时间, 单位:秒
     */
    public static final int DEFAULT_TIMEOUT = 10;

    /**
     * 解析 key=value&key=value的键值
     * @param contents 数据
     * @param encoding 编码
     * @return map
     */
    public static Map<String, String> parseQueryString(String contents, String encoding) {
        Map<String, String> map = new HashMap<>();
        String[] keyValues = StringUtils.split(contents, "&");
        for (String keyValue : keyValues) {
            String key = keyValue.substring(0, keyValue.indexOf("="));
            String value = keyValue.substring(keyValue.indexOf("=") + 1);

            try {
                if (encoding != null && !encoding.isEmpty()) {
                    value = URLDecoder.decode(value, encoding);
                } else {
                    value = URLDecoder.decode(value, Charsets.UTF_8.name());
                }
            } catch (UnsupportedEncodingException e) {
                logger.error(e.getMessage(), e);
            }

            map.put(key, value);
        }
        return map;
    }

    /**
     * 拼接请求字符串到最终Url
     * @param baseUrl 原始url
     * @param params 请求参数
     * @param encode 是否进行Url编码
     * @param charset 如果进行编码的字符集
     * @return 最终合成的Url
     */
    public static String concatQueryString(String baseUrl, Map<String, String> params, boolean encode, Charset charset) {
        // 先合并参数
        List<String> paramsList = params.keySet().stream()
                .map(key -> {
                    Charset c = encode ? charset : null;
                    if (c == null && encode) {
                        c = Charsets.UTF_8;
                    }
                    String val;
                    try {
                        val = encode ? URLEncoder.encode(params.get(key), charset.name()) : params.get(key);
                    } catch (UnsupportedEncodingException e) {
                        val = params.get(key);
                        logger.error("编码出错, val=" + val, e);
                    }

                    return String.format("%s=%s", key, params.get(key));
                }).collect(Collectors.toList());

        String delimiter = baseUrl.contains("?") ? "&" : "?";
        return baseUrl + delimiter + StringUtils.join(paramsList, "&");
    }

    /**
     * 发起http get请求
     * @param url 请求地址
     * @return responseBody字符串
     * @throws IOException
     */
    public static String get(String url) throws IOException {
        return CoreHttpUtils.get(url, Charsets.UTF_8.name(), DEFAULT_TIMEOUT);
    }

    /**
     * 发起http get请求
     * @param url 请求地址
     * @param encoding 字符编码
     * @param timeout 超时时间,单位:秒
     * @return responseBody字符串
     * @throws IOException
     */
    public static String get(String url, String encoding, int timeout) throws IOException {
        return getWithHeaders(url, null, encoding, timeout);
    }

    /**
     * 发起http get请求
     * @param url 请求地址
     * @param headerMap 自定义请求头信息
     * @param encoding 字符编码
     * @param timeout 超时时间,单位:秒
     * @return responseBody字符串
     * @throws IOException
     */
    public static String getWithHeaders(String url, Map<String, String> headerMap, String encoding, int timeout) throws IOException {
        return getWithHeadersByProxy(url, headerMap, encoding, timeout, null, null, null);
    }

    /**
     * 发起http get请求
     * @param url 请求地址
     * @param headerMap 自定义请求头信息
     * @param encoding 字符编码
     * @param timeout 超时时间,单位:秒
     * @param proxy 代理
     * @param proxyUser 代理用户名
     * @param proxyPassword 代理密码
     * @return responseBody字符串
     * @throws IOException
     */
    public static String getWithHeadersByProxy(String url, Map<String, String> headerMap, String encoding, int timeout, Proxy proxy, String proxyUser, String proxyPassword) throws IOException {
        if (url == null || url.isEmpty()) {
            throw new IOException("CoreHttpUtils GET String error : url must be not null");
        }

        if (encoding == null || encoding.isEmpty()) {
            throw new IOException("CoreHttpUtils GET String error : encoding must be not null");
        }

        if (timeout < 0) {
            throw new IOException("CoreHttpUtils GET String error : timeout must be greater than 0");
        }

        Authenticator proxyAuthenticator = null;
        if (StringUtils.isNotBlank(proxyUser)) {
            proxyAuthenticator = (route, response) -> {
                String credential = Credentials.basic(proxyUser, MoreObjects.firstNonNull(proxyPassword, StringUtils.EMPTY));
                return response.request().newBuilder()
                        .header("Proxy-Authorization", credential)
                        .build();
            };
        }

        OkHttpClient.Builder builder = okHttpClient.newBuilder();
        if (proxy != null) {
            builder.proxy(proxy);
        }

        if (proxyAuthenticator != null) {
            builder.proxyAuthenticator(proxyAuthenticator);
        }

        OkHttpClient clientWithTimeout = builder
                .readTimeout(timeout, TimeUnit.SECONDS)
                .connectTimeout(timeout, TimeUnit.SECONDS)
                .build();

        logger.debug("CoreHttpUtils GET String Executing Request : {}", url);

        Request.Builder requestBuilder = new Request.Builder().url(url);
        if (headerMap != null && !headerMap.isEmpty()) {
            headerMap.keySet().forEach(key -> requestBuilder.addHeader(key, headerMap.get(key)));
        }

        Request request = requestBuilder.build();

        try (Response response = clientWithTimeout.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("CoreHttpUtils GET String ResponseBody error : " + response);
            }

            ResponseBody responseBody = response.body();
            if (responseBody == null) {
                throw new IOException("CoreHttpUtils GET String ResponseBody error : " + response);
            }
            String result = new String(responseBody.bytes(), encoding);
            logger.debug("CoreHttpUtils GET String ResponseBody : {}", result);
            return result;
        }
    }

    /**
     * 发起post json string 请求
     * @param url 请求地址
     * @param json 请求string数据
     * @return responseBody字符串
     * @throws IOException
     */
    public static String postJSON(String url, String json) throws IOException {
        Map<String, String> headMap = Maps.newHashMap();
        headMap.put("Content-Type", HttpMediaType.APPLICATION_JSON.getValue() + "; charset=" + Charsets.UTF_8.name());
        return CoreHttpUtils.postJSONWithHeaders(url, headMap, json);
    }

    /**
     * 发起post json string 请求
     * @param url 请求地址
     * @param headerMap 请求header
     * @param json 请求string数据
     * @return responseBody字符串
     * @throws IOException
     */
    public static String postJSONWithHeaders(String url, Map<String, String> headerMap, String json) throws IOException {
        return CoreHttpUtils.postWithStringBody(url, headerMap, json, HttpMediaType.APPLICATION_JSON, Charsets.UTF_8.name(), DEFAULT_TIMEOUT);
    }

    /**
     * 发起post xml string 请求
     * @param url 请求地址
     * @param xml 请求string数据
     * @return responseBody字符串
     * @throws IOException
     */
    public static String postXML(String url, String xml) throws IOException {
        return CoreHttpUtils.postXMLWithHeaders(url, null, xml);
    }

    /**
     * 发起post xml string 请求
     * @param url 请求地址
     * @param xml 请求string数据
     * @param charset 字符集
     * @param timeout 超时时间
     * @return responseBody字符串
     * @throws IOException
     */
    public static String postXMLWithCharsetAndTimeout(String url, String xml, String charset, int timeout) throws IOException {
        return CoreHttpUtils.postWithStringBody(url, null, xml, HttpMediaType.APPLICATION_XML, charset, timeout);
    }

    /**
     * 发起post xml string 请求
     * @param url 请求地址
     * @param headerMap 请求header
     * @param xml 请求string数据
     * @return responseBody字符串
     * @throws IOException
     */
    public static String postXMLWithHeaders(String url, Map<String, String> headerMap, String xml) throws IOException {
        return CoreHttpUtils.postWithStringBody(url, headerMap, xml, HttpMediaType.APPLICATION_XML, Charsets.UTF_8.name(), DEFAULT_TIMEOUT);
    }

    /**
     * 发起post text plain string 请求
     * @param url 请求地址
     * @param text 请求string数据
     * @return responseBody字符串
     * @throws IOException
     */
    public static String postTEXT(String url, String text) throws IOException {
        return CoreHttpUtils.postTEXTWithHeaders(url, null, text);
    }

    /**
     * 发起post text plain string 请求
     * @param url 请求地址
     * @param headerMap 请求header
     * @param text 请求string数据
     * @return responseBody字符串
     * @throws IOException
     */
    public static String postTEXTWithHeaders(String url, Map<String, String> headerMap, String text) throws IOException {
        return CoreHttpUtils.postWithStringBody(url, headerMap, text, HttpMediaType.TEXT_PLAIN, Charsets.UTF_8.name(), DEFAULT_TIMEOUT);
    }

    /**
     * 发起post urlEncodedForm 请求
     * @param url 请求地址
     * @param bodyMap 请求form数据
     * @return responseBody字符串
     * @throws IOException
     */
    public static String postUrlEncodedForm(String url, Map<String, String> bodyMap) throws IOException {
        return CoreHttpUtils.postWithUrlEncodedFormBody(url, null, bodyMap, Charsets.UTF_8.name(), DEFAULT_TIMEOUT);
    }

    /**
     * 发起post urlEncodedForm 请求
     * @param url 请求地址
     * @param headerMap 请求header
     * @param bodyMap 请求form数据
     * @param encoding 字符编码
     * @param timeout 超时时间, 单位:秒
     * @return responseBody字符串
     * @throws IOException
     */
    public static String postWithUrlEncodedFormBody(String url, Map<String, String> headerMap, Map<String, String> bodyMap, String encoding, int timeout) throws IOException {
        if (url == null || url.isEmpty()) {
            throw new IOException("CoreHttpUtils POST UrlEncodedForm error : url must be not null");
        }

        if (encoding == null || encoding.isEmpty()) {
            throw new IOException("CoreHttpUtils POST UrlEncodedForm error : encoding must be not null");
        }

        if (timeout < 0) {
            throw new IOException("CoreHttpUtils POST UrlEncodedForm error : timeout must be greater than 0");
        }

        if (bodyMap == null || bodyMap.isEmpty()) {
            throw new IOException("CoreHttpUtils POST UrlEncodedForm error : bodyMap must be not null");
        }

        OkHttpClient clientWithTimeout = okHttpClient.newBuilder()
                .readTimeout(timeout, TimeUnit.SECONDS)
                .connectTimeout(timeout, TimeUnit.SECONDS)
                .build();

        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        bodyMap.keySet().forEach(key -> formBodyBuilder.add(key, bodyMap.get(key)));

        RequestBody formBody = formBodyBuilder.build();

        Request.Builder requestBuilder = new Request.Builder().url(url);
        if (headerMap != null && !headerMap.isEmpty()) {
            headerMap.keySet().forEach(key -> requestBuilder.addHeader(key, headerMap.get(key)));
        }

        Request request = requestBuilder.post(formBody).build();

        logger.debug("CoreHttpUtils POST UrlEncodedForm Executing Request : {}", request.url().toString());
        logger.debug("CoreHttpUtils POST UrlEncodedForm Request body : {}", formBody.toString());

        try (Response response = clientWithTimeout.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("CoreHttpUtils POST UrlEncodedForm ResponseBody error : " + response);
            }
            ResponseBody responseBody = response.body();
            if (responseBody == null) {
                throw new IOException("CoreHttpUtils POST UrlEncodedForm ResponseBody error : " + response);
            }
            String result = new String(responseBody.bytes(), encoding);
            logger.debug("CoreHttpUtils POST UrlEncodedForm ResponseBody : {}", result);
            return result;
        }
    }

    /**
     * 发起post string 请求
     * @param url 请求地址
     * @param headerMap 请求header
     * @param body 请求string数据
     * @param bodyMediaType 请求string数据类型
     * @param encoding 字符编码
     * @param timeout 超时时间, 单位:秒
     * @return responseBody字符串
     * @throws IOException
     */
    public static String postWithStringBody(String url, Map<String, String> headerMap, String body, HttpMediaType bodyMediaType, String encoding, int timeout) throws IOException {
        if (url == null || url.isEmpty()) {
            throw new IOException("CoreHttpUtils POST String error : url must be not null");
        }

        if (encoding == null || encoding.isEmpty()) {
            throw new IOException("CoreHttpUtils POST String error : encoding must be not null");
        }

        if (timeout < 0) {
            throw new IOException("CoreHttpUtils POST String error : timeout must be greater than 0");
        }

        if (body == null || body.isEmpty()) {
            throw new IOException("CoreHttpUtils POST String error : bodyMap must be not null");
        }

        if (bodyMediaType == null) {
            throw new IOException("CoreHttpUtils POST String error : bodyMediaType must be not null");
        }

        if (!stringBodyHttpMediaTypes.contains(bodyMediaType)) {
            throw new RuntimeException("post发送stringBody失败: bodyMediaType不合法! bodyMediaType=" + bodyMediaType.getValue());
        }

        OkHttpClient clientWithTimeout = okHttpClient.newBuilder()
                .readTimeout(timeout, TimeUnit.SECONDS)
                .connectTimeout(timeout, TimeUnit.SECONDS)
                .build();

        MediaType mediaType = MediaType.parse(bodyMediaType.getValue() + "; charset=" + encoding.toLowerCase());

        RequestBody requestBody = RequestBody.create(mediaType, body);

        Request.Builder requestBuilder = new Request.Builder().url(url);
        if (headerMap != null && !headerMap.isEmpty()) {
            headerMap.keySet().forEach(key -> requestBuilder.addHeader(key, headerMap.get(key)));
        }

        Request request = requestBuilder.post(requestBody).build();

        logger.info("CoreHttpUtils POST String Executing Request : {}", request.url().toString());
        logger.info("CoreHttpUtils POST String Request body : {}", body);

        try (Response response = clientWithTimeout.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                if(response.code() == 401){
                    throw new UnauthorizedException("CoreHttpUtils POST String ResponseBody error : " + response);
                }
                throw new IOException("CoreHttpUtils POST String ResponseBody error : " + response);
            }

            ResponseBody responseBody = response.body();
            if (responseBody == null) {
                throw new IOException("CoreHttpUtils POST String ResponseBody error : " + response);
            }
            String result = new String(responseBody.bytes(), encoding);
            logger.info("CoreHttpUtils POST String ResponseBody : {}", result);
            return result;
        }
    }

    /**
     * 以post方式发送multipart请求
     * @param url 请求url
     * @param headerMap 请求header map
     * @param formFieldMap 除文件类型字段外的字段
     * @param fileFieldKey 文件字段名
     * @param fileFieldVal 文件字段值
     * @param fileMediaType 文件媒体类型
     * @param fileByteArr 文件字节数组
     * @return
     * @throws IOException
     */
    public static String postWithByteArray(String url, Map<String, String> headerMap, Map<String, String> formFieldMap,
                                           String fileFieldKey, String fileFieldVal, MediaType fileMediaType, byte[] fileByteArr) throws IOException {
        if (StringUtils.isBlank(url)) {
            throw new IOException("CoreHttpUtils POST multipart error : url must be not null");
        }

        if (StringUtils.isBlank(fileFieldKey)) {
            throw new IOException("CoreHttpUtils POST multipart error : fileFieldKey must be not null");
        }

        if (StringUtils.isBlank(fileFieldVal)) {
            throw new IOException("CoreHttpUtils POST multipart error : fileFieldVal must be not null");
        }

        if (fileMediaType == null) {
            throw new IOException("CoreHttpUtils POST multipart error : fileMediaType must be not null");
        }

        if (fileByteArr == null) {
            throw new IOException("CoreHttpUtils POST multipart error : fileByteArr must be not null");
        }

        OkHttpClient clientWithTimeout = okHttpClient.newBuilder()
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build();

        Request.Builder requestBuilder = new Request.Builder().url(url);

        // header
        if (headerMap != null && !headerMap.isEmpty()) {
            headerMap.keySet().forEach(key -> requestBuilder.addHeader(key, headerMap.get(key)));
        }

        MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);

        // form key value pair
        if (formFieldMap != null && !formFieldMap.isEmpty()) {
            formFieldMap.keySet().forEach(key -> multipartBodyBuilder.addFormDataPart(key, formFieldMap.get(key)));
        }

        // form file
        multipartBodyBuilder.addFormDataPart(fileFieldKey, fileFieldVal, RequestBody.create(fileMediaType, fileByteArr));

        Request request = requestBuilder.post(multipartBodyBuilder.build()).build();

        logger.debug("CoreHttpUtils POST multipart Executing Request : {}", request.url().toString());

        try (Response response = clientWithTimeout.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("CoreHttpUtils POST multipart ResponseBody error : " + response);
            }

            ResponseBody responseBody = response.body();
            if (responseBody == null) {
                throw new IOException("CoreHttpUtils POST multipart ResponseBody error : " + response);
            }
            String result = new String(responseBody.bytes(), Charsets.UTF_8.name());
            logger.debug("CoreHttpUtils POST multipart ResponseBody : {}", result);
            return result;
        }
    }

    /**
     * 发送http head请求
     * @param url 请求url地址
     * @return response http code
     * @throws IOException
     */
    public static int headRequest(String url) throws IOException {
        OkHttpClient clientWithTimeout = okHttpClient.newBuilder()
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .head()
                .build();

        try (Response response = clientWithTimeout.newCall(request).execute()) {
            return response.code();
        }
    }

    /**
     * 发起delete string 请求
     * @param url 请求地址
     * @return responseBody字符串
     * @throws IOException
     */
    public static String delete(String url) throws IOException {
        Map<String, String> headMap = Maps.newHashMap();
        headMap.put("Content-Type", HttpMediaType.APPLICATION_JSON.getValue() + "; charset=" + Charsets.UTF_8.name());
        return CoreHttpUtils.deleteJSONWithHeaders(url,null);
    }


    /**
     * 发起delete json string 请求
     * @param url 请求地址
     * @param headerMap 请求header
     * @return responseBody字符串
     * @throws IOException
     */
    public static String deleteJSONWithHeaders(String url, Map<String, String> headerMap) throws IOException {
        return CoreHttpUtils.deleteWithString(url, headerMap, Charsets.UTF_8.name(), DEFAULT_TIMEOUT);
    }

    /**
     * 发起delete string 请求
     * @param url 请求地址
     * @param headerMap 请求header
     * @param encoding 字符编码
     * @param timeout 超时时间, 单位:秒
     * @return responseBody字符串
     * @throws IOException
     */
    public static String deleteWithString(String url, Map<String, String> headerMap, String encoding, int timeout) throws IOException {
        if (url == null || url.isEmpty()) {
            throw new IOException("CoreHttpUtils DELETE String error : url must be not null");
        }

        if (encoding == null || encoding.isEmpty()) {
            throw new IOException("CoreHttpUtils DELETE String error : encoding must be not null");
        }

        if (timeout < 0) {
            throw new IOException("CoreHttpUtils DELETE String error : timeout must be greater than 0");
        }

        OkHttpClient clientWithTimeout = okHttpClient.newBuilder()
                .readTimeout(timeout, TimeUnit.SECONDS)
                .connectTimeout(timeout, TimeUnit.SECONDS)
                .build();

        Request.Builder requestBuilder = new Request.Builder().url(url);
        if (headerMap != null && !headerMap.isEmpty()) {
            headerMap.keySet().forEach(key -> requestBuilder.addHeader(key, headerMap.get(key)));
        }

        Request request = requestBuilder.delete().build();

        logger.info("CoreHttpUtils DELETE String Executing Request : {}", request.url().toString());

        try (Response response = clientWithTimeout.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                if(response.code() == 401){
                    throw new UnauthorizedException("CoreHttpUtils DELETE String ResponseBody error : " + response);
                }
                throw new IOException("CoreHttpUtils DELETE String ResponseBody error : " + response);
            }

            ResponseBody responseBody = response.body();
            if (responseBody == null) {
                throw new IOException("CoreHttpUtils DELETE String ResponseBody error : " + response);
            }
            String result = new String(responseBody.bytes(), encoding);
            logger.info("CoreHttpUtils DELETE String ResponseBody : {}", result);
            return result;
        }
    }

    /**
     * 发起post json string 请求
     * @param url 请求地址
     * @param headerMap 请求header
     * @param json 请求string数据
     * @return responseBody字符串
     * @throws IOException
     */
    public static String putJSONWithHeaders(String url, Map<String, String> headerMap, String json) throws IOException {
        return CoreHttpUtils.putWithStringBody(url, headerMap, json, HttpMediaType.APPLICATION_JSON, Charsets.UTF_8.name(), DEFAULT_TIMEOUT);
    }


    /**
     * 发起put json string 请求
     * @param url 请求地址
     * @param json 请求string数据
     * @return responseBody字符串
     * @throws IOException
     */
    public static String putJSON(String url, String json) throws IOException {
        Map<String, String> headMap = Maps.newHashMap();
        headMap.put("Content-Type", HttpMediaType.APPLICATION_JSON.getValue() + "; charset=" + Charsets.UTF_8.name());
        return CoreHttpUtils.putJSONWithHeaders(url, headMap, json);
    }

    /**
     * 发起put string 请求
     * @param url 请求地址
     * @param headerMap 请求header
     * @param body 请求string数据
     * @param bodyMediaType 请求string数据类型
     * @param encoding 字符编码
     * @param timeout 超时时间, 单位:秒
     * @return responseBody字符串
     * @throws IOException
     */
    public static String putWithStringBody(String url, Map<String, String> headerMap, String body, HttpMediaType bodyMediaType, String encoding, int timeout) throws IOException {
        if (url == null || url.isEmpty()) {
            throw new IOException("CoreHttpUtils PUT String error : url must be not null");
        }

        if (encoding == null || encoding.isEmpty()) {
            throw new IOException("CoreHttpUtils PUT String error : encoding must be not null");
        }

        if (timeout < 0) {
            throw new IOException("CoreHttpUtils PUT String error : timeout must be greater than 0");
        }

        if (body == null || body.isEmpty()) {
            throw new IOException("CoreHttpUtils PUT String error : bodyMap must be not null");
        }

        if (bodyMediaType == null) {
            throw new IOException("CoreHttpUtils PUT String error : bodyMediaType must be not null");
        }

        if (!stringBodyHttpMediaTypes.contains(bodyMediaType)) {
            throw new RuntimeException("put发送stringBody失败: bodyMediaType不合法! bodyMediaType=" + bodyMediaType.getValue());
        }

        OkHttpClient clientWithTimeout = okHttpClient.newBuilder()
                .readTimeout(timeout, TimeUnit.SECONDS)
                .connectTimeout(timeout, TimeUnit.SECONDS)
                .build();

        MediaType mediaType = MediaType.parse(bodyMediaType.getValue() + "; charset=" + encoding.toLowerCase());

        RequestBody requestBody = RequestBody.create(mediaType, body);

        Request.Builder requestBuilder = new Request.Builder().url(url);
        if (headerMap != null && !headerMap.isEmpty()) {
            headerMap.keySet().forEach(key -> requestBuilder.addHeader(key, headerMap.get(key)));
        }

        Request request = requestBuilder.put(requestBody).build();

        logger.info("CoreHttpUtils PUT String Executing Request : {}", request.url().toString());
        logger.info("CoreHttpUtils PUT String Request body : {}", body);

        try (Response response = clientWithTimeout.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                if(response.code() == 401){
                    throw new UnauthorizedException("CoreHttpUtils PUT String ResponseBody error : " + response);
                }
                throw new IOException("CoreHttpUtils PUT String ResponseBody error : " + response);
            }

            ResponseBody responseBody = response.body();
            if (responseBody == null) {
                throw new IOException("CoreHttpUtils PUT String ResponseBody error : " + response);
            }
            String result = new String(responseBody.bytes(), encoding);
            logger.info("CoreHttpUtils PUT String ResponseBody : {}", result);
            return result;
        }
    }
}

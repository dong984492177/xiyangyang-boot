package com.ywt.common.bean;

import com.google.common.collect.Maps;
import com.ywt.common.enums.EncryptType;
import com.ywt.common.enums.EncryptType;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.Map;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 * @Copyright: 云网通信息科技
 */
public class ClientRequestContext implements Serializable {

    private static final long serialVersionUID = -4012761070773890186L;

    /**
     * 访问令牌
     */
    private String accessToken;

    /**
     * 是否验证签名
     */
    private boolean validateSignature = true;

    /**
     * 当前用户会话令牌
     */
    private String sessionToken;

    /**
     * 用户长登录令牌
     */
    private String userToken;

    /**
     * 本次请求的随机串
     */
    private String nonce;

    /**
     * 本次请求执行的命令类型
     */
    private String command;

    /**
     * 本次请求的加密类型
     */
    private EncryptType encryptType = EncryptType.NONE;

    private byte[] encryptIV;

    /**
     * 是否经HTTPS加密
     */
    private boolean usingHttps;

    /**
     * 应用ID
     */
    private String appId;

    /**
     * 应用密钥
     */
    private String appSecret;

    /**
     * 请求客户端的ID
     */
    private String clientId;

    /**
     * 用户OpenID
     */
    private String userOpenId;

    /**
     * 用户ID
     */
    private Long userId;

    /** 附加属性 **/
    private String base64RequestData;

    private boolean serializeLongAsString;

    private final Map<String, Object> attributes = Maps.newHashMap();

    public HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    public HttpServletResponse getResponse() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
    }

    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    public void setAttribute(String key, Object val) {
        attributes.put(key, val);
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getUserOpenId() {
        return userOpenId;
    }

    public void setUserOpenId(String userOpenId) {
        this.userOpenId = userOpenId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getBase64RequestData() {
        return base64RequestData;
    }

    public void setBase64RequestData(String base64RequestData) {
        this.base64RequestData = base64RequestData;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public EncryptType getEncryptType() {
        return encryptType;
    }

    public void setEncryptType(EncryptType encryptType) {
        this.encryptType = encryptType;
    }

    public byte[] getEncryptIV() {
        return encryptIV;
    }

    public void setEncryptIV(byte[] encryptIV) {
        this.encryptIV = encryptIV;
    }

    public boolean isUsingHttps() {
        return usingHttps;
    }

    public void setUsingHttps(boolean usingHttps) {
        this.usingHttps = usingHttps;
    }

    public boolean isValidateSignature() {
        return validateSignature;
    }

    public void setValidateSignature(boolean validateSignature) {
        this.validateSignature = validateSignature;
    }

    public boolean isSerializeLongAsString() {
        return serializeLongAsString;
    }

    public void setSerializeLongAsString(boolean serializeLongAsString) {
        this.serializeLongAsString = serializeLongAsString;
    }
}

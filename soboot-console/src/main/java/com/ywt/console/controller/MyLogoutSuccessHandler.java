package com.ywt.console.controller;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.ywt.common.base.constant.CharsetConstant;
import com.ywt.common.base.constant.XiotConstant;
import com.ywt.common.base.util.CoreDateUtils;
import com.ywt.common.base.util.JwtTokenUtils;
import com.ywt.common.config.redis.RedissonService;
import com.ywt.common.response.DefaultResponseDataWrapper;
import com.ywt.console.entity.SysUser;
import com.ywt.console.entity.log.LoginLog;
import com.ywt.console.mapper.LoginLogMapper;
import com.ywt.console.mapper.SysUserMapper;
import com.ywt.console.models.UserPhoneToken;
import com.ywt.console.utils.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 */
@Slf4j
@Component
public class MyLogoutSuccessHandler implements LogoutSuccessHandler {
    @Autowired
    private RedissonService redissonService;

   @Autowired
   private SysUserMapper userMapper;

   @Autowired
   private LoginLogMapper loginLogMapper;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("退出成功");
        String tokenStr = request.getHeader(XiotConstant.JWT_TOKEN_HEADER);
        if (!StringUtils.isEmpty(tokenStr)) {
            tokenStr = tokenStr.replaceAll(XiotConstant.JWT_TOKEN_PREFIX, "");
            String subject = JwtTokenUtils.getSubject(tokenStr);
            if (null == subject) {
                return;
            }
            UserPhoneToken token = JSONObject.parseObject(subject, UserPhoneToken.class);

            //插入log
            saveLog(request,token);
            String key = Util.getPermissonKey(token.getPhone());
            redissonService.deleteString(key);

            String routerKey = Util.getRouterKey(token.getUserId());
            redissonService.deleteString(routerKey);
        }

        writerRes(response, DefaultResponseDataWrapper.success());
    }

    private void writerRes(HttpServletResponse response, DefaultResponseDataWrapper<String> responseModel) {
        try {
            response.setCharacterEncoding(CharsetConstant.CHARSET_UTF8);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(JSONUtil.toJsonStr(responseModel));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

   public void saveLog(HttpServletRequest request,UserPhoneToken token){
        LoginLog loginLog = new LoginLog();
        loginLog.setRemoteAddr(com.ywt.common.base.util.StringUtils.getRemoteAddr(request));
        loginLog.setCreateTime(CoreDateUtils.convertTo(LocalDateTime.now()));
        loginLog.setOrigin(getOrigin(loginLog,request));
        loginLog.setLoginStatus("0");

        Integer userId = token.getUserId();
        SysUser user = userMapper.selectById(userId);
        if(!ObjectUtils.isEmpty(user)){
            loginLog.setTargetId(String.valueOf(userId));
           loginLog.setTargetValue(user.getUserName());
            // 保存日志信息
           loginLogMapper.insert(loginLog);
       }
    }

    private String getOrigin(LoginLog loginLog,HttpServletRequest request){

        String userAgent = request.getHeader("user-agent").toLowerCase();
        loginLog.setUserAgent(userAgent);
        if(userAgent.indexOf("android") != -1){
            //安卓
            return "android";
        }else if(userAgent.indexOf("iphone") != -1 || userAgent.indexOf("ipad") != -1 || userAgent.indexOf("ipod") != -1){
            //苹果
            return "ios";
        }else{
            //电脑
            return "pc";
        }
    }
}

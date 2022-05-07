package com.ywt.console.config;

import com.alibaba.fastjson.JSONObject;
import com.ywt.common.base.util.CoreDateUtils;
import com.ywt.common.base.util.JwtTokenUtils;
import com.ywt.common.base.util.StringUtils;
import com.ywt.console.entity.SysUser;
import com.ywt.console.entity.log.LoginLog;
import com.ywt.console.mapper.LoginLogMapper;
import com.ywt.console.mapper.SysUserMapper;
import com.ywt.console.models.UserPhoneToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

/**
 * @Author: huangchaoyang
 * @Description: 登录日志拦截器
 * @Version: 1.0
 * @Create: 2021/1/12
 */
@Slf4j
public class LoginLogInterceptor implements HandlerInterceptor {

    private static LoginLogMapper loginLogMapper = SpringContextHolder.getBean(LoginLogMapper.class);
    private static SysUserMapper userMapper = SpringContextHolder.getBean(SysUserMapper.class);

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

        log.debug(StringUtils.getRemoteAddr(request));
        log.debug("Header: " + request.getHeader("user-agent"));
        log.debug("RequestURI: " + request.getRequestURI());
        log.debug("Method: " + request.getMethod());
        log.debug("ParameterMap: " + request.getParameterMap().toString());
        //保存日志信息
        saveLog(request, handler, ex, null);
    }

    /**
     * 保存日志
     */
    public static void saveLog(HttpServletRequest request, Object handler, Exception ex, String title){

        String tokenString = request.getHeader("Authorization").split("Bearer ")[1];
        String subject = JwtTokenUtils.getSubject(tokenString);
        UserPhoneToken token = JSONObject.parseObject(subject, UserPhoneToken.class);
        LoginLog loginLog = new LoginLog();
        loginLog.setRemoteAddr(StringUtils.getRemoteAddr(request));
        loginLog.setCreateTime(CoreDateUtils.convertTo(LocalDateTime.now()));
        loginLog.setOrigin(getOrigin(loginLog,request));
        loginLog.setLoginStatus("1");
        // 异步保存日志
        new LoginLogInterceptor.SaveLogThread(loginLog, handler, ex,token).start();
    }
    public static class SaveLogThread extends Thread{

        private LoginLog loginLog;
        private Object handler;
        private Exception ex;
        private UserPhoneToken token;

        public SaveLogThread(LoginLog loginLog, Object handler, Exception ex,UserPhoneToken token){
            super(LoginLogInterceptor.SaveLogThread.class.getSimpleName());
            this.loginLog = loginLog;
            this.handler = handler;
            this.ex = ex;
            this.token = token;
        }

        @Override
        public void run() {

            Integer userId = token.getUserId();
            SysUser user = userMapper.selectById(userId);
            if(!ObjectUtils.isEmpty(user)){
                loginLog.setTargetId(String.valueOf(userId));
                loginLog.setTargetValue(user.getUserName());
                // 保存日志信息
                loginLogMapper.insert(loginLog);
            }
        }
    }

    public static String getOrigin(LoginLog loginLog,HttpServletRequest request){

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

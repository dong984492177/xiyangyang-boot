package com.ywt.console.config;

import cn.hutool.json.JSONUtil;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ywt.common.base.constant.CharsetConstant;
import com.ywt.common.base.constant.XiotConstant;
import com.ywt.common.base.util.CoreDateUtils;
import com.ywt.common.base.util.JwtTokenUtils;
import com.ywt.common.response.DefaultResponseDataWrapper;
import com.ywt.console.constant.ResCode;
import com.ywt.console.entity.SysUser;
import com.ywt.console.entity.log.LoginLog;
import com.ywt.console.mapper.LoginLogMapper;
import com.ywt.console.mapper.SysUserMapper;
import com.ywt.console.models.UserPhoneToken;
import com.ywt.console.models.reqmodel.LoginReqModel;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.util.ObjectUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * @Author: huangchaoyang
 * @Description: jwt登录过滤器
 * @Version: 1.0
 * @Create: 2021/1/12
 * @Copyright: 云网通信息科技
 */
public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter{

    private static LoginLogMapper loginLogMapper = SpringContextHolder.getBean(LoginLogMapper.class);
    private static SysUserMapper userMapper = SpringContextHolder.getBean(SysUserMapper.class);

    protected JWTLoginFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
    }

    public JWTLoginFilter(AuthenticationManager authenticationManager, String url) {
        super(url);
        this.setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {
        String collect = httpServletRequest.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        LoginReqModel loginReqModel = JSON.parseObject(collect, LoginReqModel.class);
        return getAuthenticationManager().authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginReqModel.getAccount(),
                        loginReqModel
                )
        );
    }

    @Override protected void successfulAuthentication(HttpServletRequest req,
                                                      HttpServletResponse res,
                                                      FilterChain chain,
                                                      Authentication auth) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        DefaultResponseDataWrapper<JSONObject> responseModel = new DefaultResponseDataWrapper<>();
        Object userId = authentication.getPrincipal();
        Object phone = authentication.getCredentials();
        res.setCharacterEncoding(CharsetConstant.CHARSET_UTF8);
        res.setContentType(MediaType.APPLICATION_JSON_VALUE);
        Date date = new Date();
        JSONObject object = new JSONObject();
        UserPhoneToken userPhoneToken = UserPhoneToken.builder().userId((Integer)userId).phone((String)phone).build();
        String tokenString = JwtTokenUtils.createToken(JSONObject.toJSONString(userPhoneToken), date);
        object.put("access_token",tokenString );

        saveLog(req,userPhoneToken);

        object.put("expiration", date.getTime() + XiotConstant.JWT_TOKEN_TIME);
        responseModel.setData(object);
        res.getWriter().write(JSONUtil.toJsonStr(responseModel));
    }

    @Override protected void unsuccessfulAuthentication(HttpServletRequest req,
                                                        HttpServletResponse res, AuthenticationException failed) throws IOException {
        DefaultResponseDataWrapper<String> responseModel = new DefaultResponseDataWrapper<>();
        responseModel.setCode(ResCode.FAIL);
        if (StringUtils.equals(failed.getLocalizedMessage(), ResCode.PWD_ERROR.toString())) {
            responseModel.setMessage("密码错误");
        } else if (StringUtils.equals(failed.getLocalizedMessage(), ResCode.GET_ERROR.toString())) {
            responseModel.setMessage("用户不存在");
        }  else if (StringUtils.equals(failed.getLocalizedMessage(), ResCode.FORBIDDEN.toString())) {
            responseModel.setMessage("用户已被禁用");
        } else if(StringUtils.equals(failed.getLocalizedMessage(), ResCode.LOCKED.toString())){
            responseModel.setMessage("账号被锁,请一分钟后重试!");
        } else{
            responseModel.setMessage("登录异常");
        }
        res.setCharacterEncoding(CharsetConstant.CHARSET_UTF8);
        res.setContentType(MediaType.APPLICATION_JSON_VALUE);
        res.getWriter().write(JSONUtil.toJsonStr(responseModel));
    }

    /**
     * 保存日志
     */
    public static void saveLog(HttpServletRequest request,UserPhoneToken token){

        LoginLog loginLog = new LoginLog();
        loginLog.setRemoteAddr(com.ywt.common.base.util.StringUtils.getRemoteAddr(request));
        loginLog.setCreateTime(CoreDateUtils.convertTo(LocalDateTime.now()));
        loginLog.setOrigin(getOrigin(loginLog,request));
        loginLog.setLoginStatus("1");
        // 异步保存日志
        new JWTLoginFilter.SaveLogThread(loginLog,token).start();
    }
    public static class SaveLogThread extends Thread{

        private LoginLog loginLog;
        private UserPhoneToken token;

        public SaveLogThread(LoginLog loginLog,UserPhoneToken token){
            super(LoginLogInterceptor.SaveLogThread.class.getSimpleName());
            this.loginLog = loginLog;
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

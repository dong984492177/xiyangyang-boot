/*
package com.ywt.console.config;

import cn.hutool.core.thread.threadlocal.NamedThreadLocal;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ywt.common.base.annotation.Action;
import com.ywt.common.base.constant.XiotConstant;
import com.ywt.common.base.util.CoreDateUtils;
import com.ywt.common.base.util.Exceptions;
import com.ywt.common.base.util.JwtTokenUtils;
import com.ywt.common.base.util.StringUtils;
import com.ywt.common.enums.OperationType;
import com.ywt.common.enums.TargetType;
import com.xiot.yuquan.ctl.api.bean.log.ApiLogModel;
import com.xiot.yuquan.ctl.api.service.log.ApiLogApiService;
import com.ywt.console.entity.SysUser;
import com.ywt.console.mapper.HotelOuterDeviceAccountMapper;
import com.ywt.console.mapper.OuterDeviceMapper;
import com.ywt.console.models.UserPhoneToken;
import com.ywt.console.service.IDeviceService;
import com.ywt.console.service.ISceneService;
import com.ywt.console.service.ISysUserService;
import com.ywt.console.service.impl.GatewayServiceImpl;
import com.ywt.console.service.impl.HotelServiceServiceImpl;
import com.ywt.console.service.impl.SceneModelServiceImpl;
import com.ywt.console.service.impl.VoiceCommandTagServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.utils.IOUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


*/
/*
 * @Author: huangchaoyang
 * @Description: api???????????????
 * @Version: 1.0
 * @Create: 2021/1/12
 * @Copyright: ?????????????????????
 *//*

@Component
@Slf4j
public class ApiLogInterceptor implements HandlerInterceptor {

    //private static ApiLogMapper apiLogMapper = SpringContextHolder.getBean(ApiLogMapper.class);
    //private static SysUserMapper userMapper = SpringContextHolder.getBean(SysUserMapper.class);
    @Reference
    private ApiLogApiService apiLogApiService;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private GatewayServiceImpl gatewayService;

    @Autowired
    private ISceneService sceneService;

    @Autowired
    private SceneModelServiceImpl sceneModelServiceImpl;

    @Autowired
    private VoiceCommandTagServiceImpl voiceCommandTagService;

    @Autowired
    private OuterDeviceMapper outerDeviceMapper;

    @Autowired
    private HotelOuterDeviceAccountMapper hotelOuterDeviceAccountMapper;

    @Autowired
    private HotelServiceServiceImpl hotelServiceService;

    @Autowired
    private IDeviceService deviceService;

    private static final ThreadLocal<Long> startTimeThreadLocal =
            new NamedThreadLocal<Long>("ThreadLocal StartTime");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        if (log.isDebugEnabled()) {
            //????????????
            long beginTime = System.currentTimeMillis();
            startTimeThreadLocal.set(beginTime);
            log.debug("????????????: {}  URI: {}", new SimpleDateFormat("hh:mm:ss.SSS")
                    .format(beginTime), request.getRequestURI());
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {

        if (modelAndView != null) {
            log.info("ViewName: " + modelAndView.getViewName());
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) throws Exception {

        log.debug(StringUtils.getRemoteAddr(request));
        log.info("Header: " + request.getHeader("user-agent"));
        log.info("RequestURI: " + request.getRequestURI());
        log.info("Method: " + request.getMethod());
        log.info("ParameterMap: " + JSONObject.toJSONString(request.getParameterMap()));

        log.info("apiLogApiService: " + apiLogApiService);
        //??????????????????
        if (handler instanceof HandlerMethod) {
            Method m = ((HandlerMethod) handler).getMethod();
            Action rp = m.getAnnotation(Action.class);
            if (rp != null) {
                log.info("rp: " + rp);
                log.info("????????????????????????");
                saveLog(request, handler, ex, null, rp);
                log.info("??????????????????????????????");
            }
        }
    }

    */
/**
     * @param request
     * @param handler
     * @param ex
     * @param title
     * @param rp
     * @description ????????????
     *//*

    public void saveLog(HttpServletRequest request, Object handler, Exception ex, String title, Action rp) {
        Integer userId = 0;
        String tokenString = request.getHeader(XiotConstant.JWT_TOKEN_HEADER);
        if (!ObjectUtils.isEmpty(tokenString) && tokenString.contains(XiotConstant.JWT_TOKEN_PREFIX)) {
            tokenString = tokenString.split(XiotConstant.JWT_TOKEN_PREFIX)[1];
            String subject = JwtTokenUtils.getSubject(tokenString);
            UserPhoneToken token = JSONObject.parseObject(subject, UserPhoneToken.class);
            userId = token.getUserId();
        }

        ApiLogModel apiLog = new ApiLogModel();
        apiLog.setRemoteAddr(StringUtils.getRemoteAddr(request));
        apiLog.setRequestUri(request.getRequestURI());
        apiLog.setMethod(request.getMethod());

        Map<String, Object> map = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()))) {
            final String body = IOUtils.read(reader);
            if (JSON.parse(body) instanceof JSONObject) {
                map = (Map<String, Object>) JSON.parse(body);
                log.info("????????????????????????");
                // ??????????????????
                new SaveLogThread(apiLog, handler, ex, userId, map, rp).start();
            } else {
                log.info("jsonArray????????????,????????????");
            }
        } catch (Exception e) {
            log.error("??????requestbody???????????????{}", e);
        }
    }

    public class SaveLogThread extends Thread {

        private ApiLogModel apiLog;
        private Object handler;
        private Exception ex;
        private Integer userId;
        private Map<String, Object> map;
        private Action rp;

        public SaveLogThread(ApiLogModel apiLog, Object handler, Exception ex, Integer userId, Map<String, Object> map, Action rp) {
            super(SaveLogThread.class.getSimpleName());
            this.apiLog = apiLog;
            this.handler = handler;
            this.ex = ex;
            this.userId = userId;
            this.map = map;
            this.rp = rp;
        }

        @Override
        public void run() {
            log.info("??????????????????????????????");
            if (userId > 0) {
                apiLog.setTargetType(TargetType.USER.getAliasName());
            } else {
                apiLog.setTargetType(TargetType.INTERFACE.getAliasName());
            }
            Method m = ((HandlerMethod) handler).getMethod();
            Action rp = m.getAnnotation(Action.class);
            apiLog.setOperationType(rp.operationType().getAliasName());
            apiLog.setOperationValue(rp.operation());
            apiLog.setTargetValue(String.valueOf(userId));
            apiLog.setRequestTime(CoreDateUtils.convertTo(LocalDateTime.now()));
            //??????????????????
            log.info("userId:" + userId);
            SysUser user = sysUserService.getById(userId);
            apiLog.setTargetName(user.getUserName());
            apiLog.setBusinessType(rp.actionType().getAliasName());
            // ????????????????????????????????????
            apiLog.setException(Exceptions.getStackTraceAsString(ex));
            // ??????????????????
            log.info("??????????????????????????????,apiLog:" + apiLog);
            apiLogApiService.addApiLog(apiLog);
            log.info("????????????????????????????????????,apiLog:" + apiLog);
        }
    }
}
*/

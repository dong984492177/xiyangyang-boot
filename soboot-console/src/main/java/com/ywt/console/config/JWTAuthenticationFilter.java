package com.ywt.console.config;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.ywt.common.base.constant.XiotConstant;
import com.ywt.common.base.util.JwtTokenUtils;
import com.ywt.common.config.redis.RedissonService;
import com.ywt.common.response.DefaultResponseDataWrapper;
import com.ywt.console.models.UserPhoneToken;
import com.ywt.console.utils.Util;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static com.ywt.console.constant.ResCode.FAIL;
import static com.ywt.console.constant.ResCode.TOKEN_OVERDUE;

/**
 * @Author: huangchaoyang
 * @Description: 验证授权过滤器
 * @Version: 1.0
 * @Create: 2021/1/12
 * @Copyright: 云网通信息科技
 */
@Component
@Slf4j
public class JWTAuthenticationFilter extends BasicAuthenticationFilter {
    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Autowired
    public void setRedissonService(RedissonService redissonService) {
        JWTAuthenticationFilter.redissonService = redissonService;
    }

    private static RedissonService redissonService;

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        DefaultResponseDataWrapper responseModel = new DefaultResponseDataWrapper();
        String tokenStr = request.getHeader(XiotConstant.JWT_TOKEN_HEADER);
        if (!StringUtils.isEmpty(tokenStr)) {
            try {
                tokenStr = tokenStr.replaceAll(XiotConstant.JWT_TOKEN_PREFIX, "");
                if (JwtTokenUtils.isExpiration(tokenStr)) {
                    responseModel.setMessage("token已过期");
                    responseModel.setCode(TOKEN_OVERDUE);
                    writerRes(response, responseModel);
                    return;
                }
                String subject = JwtTokenUtils.getSubject(tokenStr);
                if (null == subject) {
                    responseModel.setMessage("token异常");
                    responseModel.setCode(FAIL);
                    writerRes(response, responseModel);
                    return;
                }
                UserPhoneToken token = JSONObject.parseObject(subject, UserPhoneToken.class);
                String key = Util.getPermissonKey(token.getPhone());
                Object phone = redissonService.getRBucket(key).get();
                List<GrantedAuthority> authorities = (List<GrantedAuthority>) phone;
                SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(token.getUserId(), token.getPhone(), authorities));
            } catch (ExpiredJwtException e) {
                log.error(e.toString());
                responseModel.setMessage("token已过期");
                responseModel.setCode(TOKEN_OVERDUE);
                writerRes(response, responseModel);
                return;
            } catch (UnsupportedJwtException e) {
                log.error(e.toString());
                responseModel.setMessage("token已失效");
                responseModel.setCode(FAIL);
                writerRes(response, responseModel);
                return;
            } catch (IllegalArgumentException e) {
                log.error(e.toString());
                responseModel.setMessage("token不能为空");
                responseModel.setCode(FAIL);
                writerRes(response, responseModel);
                return;
            } catch (Exception e) {
                log.error(e.toString());
                responseModel.setMessage("token异常");
                responseModel.setCode(FAIL);
                writerRes(response, responseModel);
                return;
            }
        }

        chain.doFilter(request, response);
    }

    private void writerRes(HttpServletResponse response, DefaultResponseDataWrapper responseModel) {
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/json; charset=utf-8");
            response.getWriter().write(JSONUtil.toJsonStr(responseModel));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package com.ywt.console.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

import com.ywt.common.base.constant.XiotConstant;

/**
 * @Author: huangchaoyang
 * @Description: JwtTokenUtils
 * @Version: 1.0
 * @Create: 2021/1/12
 * @Copyright: 云网通信息科技
 */
public class JwtTokenUtils {

    public static String SECRET = "jwtsecret-yunwangtong";
    public static String ISS = "yunwangtong";

    // 过期时间是3600秒，既是1个小时
    public static Long EXPIRATION = 3600L;

    // 选择了记住我之后的过期时间为7天
    public static Long EXPIRATION_REMEMBER = 604800L;

    // 创建token
    public static String createToken(String subject, Date date) {
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .setIssuer(ISS)
                .setSubject(subject)
                .setIssuedAt(date)
                .setExpiration(new Date(XiotConstant.JWT_TOKEN_TIME + date.getTime()))
                .compact();
    }

    // 从token中获取用户
    public static String getSubject(String token) {
        return getTokenBody(token).getSubject();
    }

    // 是否已过期
    public static Boolean isExpiration(String token) {
        return getTokenBody(token).getExpiration().before(new Date());
    }

    private static Claims getTokenBody(String token) {
        try{
            return Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token)
                    .getBody();
        }catch (ExpiredJwtException ex){
            return ex.getClaims();
        }
    }
}

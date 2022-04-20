package com.ywt.common.base.util;

import com.ywt.common.base.constant.XiotConstant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

/**
 * @Author: huangchaoyang
 * @Description: JwtTokenUtils
 * @Version: 1.0
 * @Create: 2021/1/12
 * @Copyright: 云网通信息科技
 */
public class JwtTokenUtils {

    public static String SECRET = "jwtsecret-xiezhu";
    public static String ISS = "xiezhu";

    // 创建token
    public static String createToken(String subject, Date date) {
        return createToken(subject, date, XiotConstant.JWT_TOKEN_TIME);
    }

    // 创建token
    public static String createToken(String subject, Date date, Long expirationTime) {
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .setIssuer(ISS)
                .setSubject(subject)
                .setIssuedAt(date)
                .setExpiration(new Date(expirationTime + date.getTime()))
                .compact();
    }

    // 从token中获取用户名
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

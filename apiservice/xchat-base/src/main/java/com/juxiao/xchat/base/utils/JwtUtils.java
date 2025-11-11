package com.juxiao.xchat.base.utils;

import com.juxiao.xchat.base.web.WebServiceException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * @author chris
 * @Title:
 * @date 2019-05-15
 * @time 16:35
 */
@Slf4j
public class JwtUtils {

    /**
     * 加密秘钥
     */
    private static final String SECRET = "aHR0cHM6Ly9teS5vc2NoaW5hLm5ldC91LzM2ODE4Njg=";
    /**
     * 有效时间
     */
    private static final long EXPIRE = 600;

    /**
     * 生成Token签名
     * @param userId 用户ID
     * @return 签名字符串
     */
    public static String generateToken(long userId) {
        Date nowDate = new Date();
        // 过期时间
        Date expireDate = new Date(nowDate.getTime() + EXPIRE * 1000);
        return Jwts.builder().setHeaderParam("typ", "JWT").setSubject(String.valueOf(userId)).setIssuedAt(nowDate)
                .setExpiration(expireDate).signWith(SignatureAlgorithm.HS512, SECRET).compact();
    }

    /**
     * 获取签名信息
     * @param token
     * @author geYang
     * @date 2018-05-18 16:47
     */
    public static Claims getClaimByToken(String token) throws WebServiceException {
        try {
            return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            log.debug("validate is token error ", e);
            throw new WebServiceException("token已过期");
        }
    }



    /**
     * 判断Token是否过期
     * @param expiration expiration
     * @return true 过期
     */
    public static boolean isTokenExpired(Date expiration) {
        return expiration.before(new Date());
    }
}

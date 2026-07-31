package com.qk.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.Map;

public class JwtUtils {

    private static final String SECRET_KEY = "cWluZ2tl";
    private static final long EXPIRATION_TIME = 12 * 60 * 60 * 1000;

    public static String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public static Claims parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 验证 token 是否有效
     */
    public static boolean validateToken(String token) {
        try {
            Claims claims = parseToken(token);
            // 检查是否过期
            Date expiration = claims.getExpiration();
            Date now = new Date();
            boolean isValid = expiration.after(now);
            System.out.println("token 验证: " + isValid + ", 过期时间: " + expiration + ", 当前时间: " + now);
            return isValid;
        } catch (Exception e) {
            System.out.println("token 验证失败: " + e.getMessage());
            return false;
        }
    }

    /**
     * 从 token 中获取用户 ID
     */
    public static Integer getUserIdFromToken(String token) {
        try {
            Claims claims = parseToken(token);
            return claims.get("id", Integer.class);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 从 token 中获取用户名
     */
    public static String getUsernameFromToken(String token) {
        try {
            Claims claims = parseToken(token);
            return claims.get("username", String.class);
        } catch (Exception e) {
            return null;
        }
    }
}
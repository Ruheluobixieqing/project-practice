package com.example.backendpractice.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {
    // JWT 密钥 (实际项目中将从配置文件读取)
    private static final String SECRET = "mySecretKeyForJWTTokenGenerationThatIsLongEnough";
    private static final SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes());

    // Token 过期时间 （24小时）
    private static final long EXPIRATION_TIME = 86400000;   // 24 h


    /**
     * 生成 JWT Token
     * @param username 用户名
     * @return JWT Token 字符串
     */
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)                         // 设置主题（用户名)
                .setIssuedAt(new Date())                       // 设置签发时间
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))     // 设置过期时间
                .signWith(key, SignatureAlgorithm.HS256)      // 使用密钥签名
                .compact();                                   // 生成 Token 字符串
    }           

    /**
     * 从 Token 中提取用户名
     * @param token JWT Token
     * @return 用户名
     */
    public String extractUsername(String token) {
    return extractClaims(token).getSubject();
    }

    /**
     * 检查 Token 是否过期
     * @param token JWT Token
     * @return true 表示过期， false 表示未过期
     */
    public boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    /**
     * 验证 Token 是否有效
     * @param token JWT Token
     * @param username 用户名
     * @return true 表示有效， false 表示无效
     */
    public boolean validateToken(String token, String username) {
        try {
            String tokenUsername = extractUsername(token);
            return tokenUsername.equals(username) && !isTokenExpired(token);
        }
        catch (Exception e) {
            return false;
        }
    }

    /**
     * 解析 Token 获取 claims
     * @param token JWT Token
     * @return claims 对象
     */
    private Claims extractClaims(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
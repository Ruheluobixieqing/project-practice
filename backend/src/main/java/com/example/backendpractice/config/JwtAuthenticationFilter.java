package com.example.backendpractice.config;

import com.example.backendpractice.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 添加 JWT 验证逻辑
        // 1.从请求头获取 Authorization
        String authHeader = request.getHeader("Authorization");

        // 2.检查是否有 Bearer Token
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            // 3.提取 Token (去掉"Bearer "前缀)
            String token = authHeader.substring(7);

            try {
                // 4.验证 Token 并提取用户名
                String username = jwtUtil.extractUsername(token);

                // 5.如果 Token 有效且当前没有认证信息
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                    // 6.验证 Token 是否过期
                    if (!jwtUtil.isTokenExpired(token)) {
                        // 7.创建认证对象
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());
                        
                        // 8.设置认证对象到 SecurityContext
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
            }
            catch (Exception e) {
            // Token 无效，不设置认证信息，让 Spring Security 处理
                System.out.println("JWT Token认证失败: " + e.getMessage());
            }
        }

        // 9.继续执行后续过滤器
        filterChain.doFilter(request, response);
    }
}
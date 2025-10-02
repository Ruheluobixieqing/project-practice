package com.example.backendpractice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration                          // 告诉 Spring 这是一个配置类
@EnableWebSecurity                      // 启用 Web 安全功能
public class SecurityConfig {

    @Bean                               // 告诉 Spring 这个方法会创建一个重要对象
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 配置安全规则
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.disable())
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/api/auth/**").permitAll()                        // 登录相关接口，所有人均可访问
                .requestMatchers("/api/users", "/api/users/**").permitAll()         // 用户列表和创建用户接口，所有人均可访问   
                .requestMatchers("/api/hello/**").permitAll()                       // Hello 接口，开放访问
                .requestMatchers("/favicon.ico").permitAll()                        // 网站图标
                .requestMatchers("/error").permitAll()                              // 错误页面
                .anyRequest().authenticated()                                       // 其他所有请求都需要登录
            )
            .formLogin(form -> form.disable())                      // 禁用默认的登录表单
            .httpBasic(basic -> basic.disable())                    // 禁用 HTTP Basic 认证
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            );                                                      // 设置为无状态（适合 JWT）
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
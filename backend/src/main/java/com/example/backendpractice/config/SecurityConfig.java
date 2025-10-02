package com.example.backendpractice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration                          // 告诉 Spring 这是一个配置类
@EnableWebSecurity                      // 启用 Web 安全功能
public class SecurityConfig {

    @Bean                               // 告诉 Spring 这个方法会创建一个重要对象
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 配置安全规则
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/api/auth/**").permitAll()        // 登录相关接口，所有人均可访问
                .requestMatchers("/api/users").permitAll()          // 用户列表，暂时开放所有人访问
                .requestMatchers("/hello/**").permitAll()           // Hello 接口，开放访问
                .anyRequest().authenticated()                       // 其他所有请求都需要登录
            );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
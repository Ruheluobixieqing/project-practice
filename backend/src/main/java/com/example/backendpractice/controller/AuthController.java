package com.example.backendpractice.controller;

import com.example.backendpractice.entity.User;
import com.example.backendpractice.repository.UserRepository;
import com.example.backendpractice.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")             // 指定基础路径
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    /** 
     * 用户登录接口
     * 简化版本：只需要用户名即可登录（暂时跳过密码验证）
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            String username = loginRequest.getUsername();

            // 检查用户名是否为空
            if (username == null || username.trim().isEmpty()) {
                // 用户名为空或空串
                return ResponseEntity.badRequest().body(createErrorResponse("用户名不能为空"));
            }

            // 查找用户
            Optional<User> userOptional = userRepository.findByUsername(username);

            if (!userOptional.isPresent()) {
                return ResponseEntity.badRequest().body(createErrorResponse("用户不存在"));
            }

            User user = userOptional.get();

            // 生成 JWT Token
            String token = jwtUtil.generateToken(user.getUsername());

            // 返回成功响应
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "登录成功");
            response.put("token", token);
            response.put("user", createUserInfo(user));

            return ResponseEntity.ok(response);
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(createErrorResponse("登录失败:" + e.getMessage()));
        }
    }

    /**
     * 验证 Token 接口
     * 用于检查 Token 是否有效
     */
    @PostMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestBody TokenRequest tokenRequest) {
        try {
            String token = tokenRequest.getToken();

            if (token == null || token.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(createErrorResponse("Token不能为空"));
            }

            // 从 Token 中提取用户名
            String username = jwtUtil.extractUsername(token);

            // 验证 Token 是否有效
            if (jwtUtil.validateToken(token, username)) {
                // 查找用户信息
                Optional<User> userOptional = userRepository.findByUsername(username);

                if (userOptional.isPresent()) {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", true);
                    response.put("message", "Token有效");
                    response.put("user", createUserInfo(userOptional.get()));

                    return ResponseEntity.ok(response);
                }
            }

            return ResponseEntity.badRequest().body(createErrorResponse("Token 无效"));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse("Token 验证失败: " + e.getMessage()));
        }

    }

    /**
     * 创建用户信息对象（不包含敏感信息）
     */
    private Map<String, Object> createUserInfo(User user) {
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", user.getId());
        userInfo.put("username", user.getUsername());
        userInfo.put("email", user.getEmail());
        userInfo.put("role", user.getRole());
        userInfo.put("enabled", user.isEnabled());
        userInfo.put("createdAt", user.getCreatedAt());
        return userInfo;
    }

    /**
     *  创建错误响应
     */
    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> error = new HashMap<>();
        error.put("success", false);
        error.put("message", message);
        return error;
    }

    // 内部类：登录请求
    public static class LoginRequest {
        private String username;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }

    // 内部类：Token 验证请求
    public static class TokenRequest {
        private String token;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
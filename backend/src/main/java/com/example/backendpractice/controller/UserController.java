package com.example.backendpractice.controller;

import com.example.backendpractice.entity.User;
import com.example.backendpractice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController                   // 告诉 Spring-boot 这是一个 REST API 控制器
@RequestMapping("/api/users")     // 指定基础路径
@CrossOrigin(origins = "*")
public class UserController {
    
    // 静态常量（包括 Logger）
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired                    // Spring 自动注入 UserRepository
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    // 添加各种 API 方法

    // GetMapping 用于处理 HTTP GET 请求
    // GET 请求通常用于查询数据
    // 不会修改服务器数据
    // 可以被浏览器直接访问

    @GetMapping
    public List<User> getAllUsers() {
        // 获取所有用户方法
        return userService.getAllUsers();
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user) {

        try {
            User savedUser = userService.createUser(user);
            return ResponseEntity.ok(savedUser);
        }
        catch (IllegalArgumentException e) {
            // 返回 400 错误和错误信息
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    // 根据 ID 获取用户 - GET /api/users/{id}
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        try {
            Optional<User> user = userService.getUserById(id);
            if (user.isPresent()) {
                return ResponseEntity.ok(user.get());
            }
            else {
                return ResponseEntity.notFound().build();
            }
        }
        catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile() {
        // 该接口应该只有登录的用户可以访问
        Map<String, Object> response = new HashMap<>();
        response.put("message", "这是受保护的用户资料接口");
        response.put("timestamp", LocalDateTime.now());
        return ResponseEntity.ok(response);
    }

    // 更新用户 - PUT /api/users/{id}
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user_new) {
        try {
            User updatedUser = userService.updateUser(id, user_new);
            return ResponseEntity.ok(updatedUser);
        }
        catch (IllegalArgumentException e){
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    // 删除用户 - DELETE /api/users/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "用户删除成功");
            return ResponseEntity.ok(response);
        }
        catch (IllegalArgumentException e){
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

}
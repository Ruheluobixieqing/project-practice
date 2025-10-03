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
        return userRepository.findAll();      // Repository的findAll()方法是Spring Data JPA自动提供的，无需手写SQL
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
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        }
        else{
            return ResponseEntity.notFound().build();
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
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            // 更新用户信息（保持原有的 id 和 timestamp 不变）
            user.setUsername(user_new.getUsername());
            user.setEmail(user_new.getEmail());
            
            User updateUser = userRepository.save(user);
            return ResponseEntity.ok(updateUser);
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    // 删除用户 - DELETE /api/users/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isPresent()){
            // 通过 id 删除用户
            userRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

}
package com.example.backendpractice.controller;

import com.example.backendpractice.entity.User;
import com.example.backendpractice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController                   // 告诉 Spring-boot 这是一个 REST API 控制器
@RequestMapping("/api/users")     // 指定基础路径
public class UserController {

    @Autowired                    // Spring 自动注入 UserRepository
    private UserRepository userRepository;

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

    // 创建新用户 - POST /api/users
    @PostMapping
    public User createUser(@RequestBody User user) {

        // username, email 字段由前端发送
        // 例如，前端会发送: {"username": "张三", "email": "zhangsan@email.com"}

        // 设置创建时间
        // 时间戳由服务器设置，而不是前端，故此处需要重新设置
        user.setTimestamp(LocalDateTime.now());

        // 保存到数据库
        return userRepository.save(user);
    }
    
    // 根据 ID 获取用户 - GET /api/users/{id}
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userRepository.findById(id).orElse(null);
    }

}
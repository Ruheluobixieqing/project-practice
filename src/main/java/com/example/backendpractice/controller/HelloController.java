package com.example.backendpractice.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.Map;
import java.util.HashMap;

@RestController                 // 告诉 spring 这是 REST API 控制器
@RequestMapping("/api")         // 设置基础路径

public class HelloController {
    // 创建一个简单的 GET 接口
    @GetMapping("/hello")
    public String sayHello() {
        return "Hello!This is Bolide's first API!";
    }

    @GetMapping("/bolide")
    public String selfIntroduction() {
        return "I'm Bolide who is a student of NJUST.";
    }

    // 创建一个 POST 接口
    @PostMapping("/greet")
    public Map<String, Object> greetUser(@RequestBody Map<String, String> userInfo){
        String name = userInfo.get("name");

        Map<String, Object> response = new HashMap<>();
        response.put("message", "你好," + name + "!");
        response.put("status", "success");
        response.put("timestamp", java.time.LocalDateTime.now().toString());

        return response;
    }

    // 路径参数示例 - 获取用户信息
    @GetMapping("/user/{id}")   // {id} 是路径变量
    public Map<String, Object> getUserById(@PathVariable Long id){          // @PathVariable 用于告诉 Spring 把 URL 的 {id} 参数传递给 id 变量
        Map<String, Object> user = new HashMap<>();
        user.put("id", id);
        user.put("name", "用户" + id);
        user.put("message", "这是通过路径参数获取的用户信息");
        user.put("timestamp", java.time.LocalDateTime.now().toString());
        
        return user;
    }
}

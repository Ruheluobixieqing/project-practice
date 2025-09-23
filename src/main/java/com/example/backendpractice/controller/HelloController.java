package com.example.backendpractice.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.Map;
import java.util.HashMap;

@RestController                 // 告诉 spring 这是 REST API 控制器
@RequestMapping("/api")         // 设置基础路径

// 创建一个简单的 GET 接口
public class HelloController {
    @GetMapping("/hello")
    public String sayHello() {
        return "Hello!This is Bolide's first API!";
    }

    @GetMapping("/bolide")
    public String selfIntroduction() {
        return "I'm Bolide who is a student of NJUST.";
    }

    @PostMapping("/greet")
    public Map<String, Object> greetUser(@RequestBody Map<String, String> userInfo){
        String name = userInfo.get("name");

        Map<String, Object> response = new HashMap<>();
        response.put("message", "你好," + name + "!");
        response.put("status", "success");
        response.put("timestamp", java.time.LocalDateTime.now().toString());

        return response;
    }

}

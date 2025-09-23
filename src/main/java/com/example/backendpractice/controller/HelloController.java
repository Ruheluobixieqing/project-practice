package com.example.backendpractice.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
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

    // 查询参数示例 - 搜索功能
    @GetMapping("/search")
    public Map<String, Object> search(@RequestParam String keyword,                        // 必须参数，如果 URL 中没有会报错
                                      @RequestParam(defaultValue = "1") int page,          // 可选参数，有默认值
                                      @RequestParam(defaultValue = "10") int size){        // 自动类型转换 String -> int
        Map<String, Object> result = new HashMap<>();
        result.put("keyword", keyword);
        result.put("page", page);
        result.put("size", size);
        result.put("message", "搜索关键词: " + keyword + ", 第" + page + "页, 每页" + size + "条结果");
        result.put("timestamp", java.time.LocalDateTime.now().toString());

        return result;
    }

    // PUT 请求 - 更新用户信息
    @PutMapping("/user/{id}")
    public Map<String, Object> updateUser(@PathVariable Long id,
                                          @RequestBody Map<String, String> userInfo){
        String name = userInfo.get("name");
        String email = userInfo.get("email");

        Map<String, Object> response = new HashMap<>();
        response.put("id", id);
        response.put("name", name);
        response.put("email", email);
        response.put("message", "用户ID " + id + " 的信息已经更新");
        response.put("operation", "UPDATE");
        response.put("timestamp", java.time.LocalDateTime.now().toString());

        return response;
    }

    // DELETE请求 - 删除用户
    @DeleteMapping("/user/{id}")
    public Map<String, Object> deleteUser(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        response.put("id", id);
        response.put("message", "用户ID " + id + " 已被删除");
        response.put("operation", "DELETE");
        response.put("status", "success");
        response.put("timestamp", java.time.LocalDateTime.now().toString());
    
        return response;
    }
}

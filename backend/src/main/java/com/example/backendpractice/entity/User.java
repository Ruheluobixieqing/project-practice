package com.example.backendpractice.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false, length = 50)
    private String username;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    // 新增：密码字段
    @Column(name = "password", nullable = false)
    private String password;

    // 新增：用户角色
    @Column(name = "role", nullable = false)
    private String role = "USER";   // 默认为普通用户

    // 新增：账户状态
    @Column(name = "enabled", nullable = false)
    private boolean enabled = true;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    // 无参构造方法（必须有）
    public User() {}

    // 有参构造方法（ id 自动生成，故不包含）
    public User(String username, String email, LocalDateTime createdAt){
        this.username = username;
        this.email = email;
        this.createdAt = createdAt;
    }

    // Getter方法
    public Long getId(){ 
        return id; 
    }
    public String getUsername(){
        return username;
    }
    public String getEmail(){
        return email;
    }
    public String getPassword() {
        return password;
    }
    public String getRole(){
        return role;
    }
    public boolean isEnabled(){
        return enabled;
    }
    public LocalDateTime getCreatedAt(){
        return createdAt;
    }

    // Setter 方法
    public void setUsername(String username){
        this.username = username;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public void setRole(String role){
        this.role = role;
    }
    public void setEnabled(boolean enabled){
        this.enabled = enabled;
    }
    public void setCreatedAt(LocalDateTime createdAt){
        this.createdAt = createdAt;
    }

    @Override
    public String toString(){
        return "User{" + "id=" + id + ", username=" + username + ", email=" + email + ", createdAt=" + createdAt +"}";
    }
}
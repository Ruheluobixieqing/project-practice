package com.example.backendpractice.service;

import com.example.backendpractice.entity.User;
import com.example.backendpractice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 添加业务方法
    /**
     * 创建新用户
     * @param user 用户信息
     * @return 创建成功的用户
     * @throws IllegalArgumentException 当用户数据无效时报错
     */
    public User createUser(User user) {
        logger.info("开始创建用户: {}", user.getUsername());

        // 1.业务验证
        validateUserForCreation(user);

        // 2.检查邮箱是否已经存在
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("邮箱已经存在，请勿重复创建！");
        }

        // 3.密码加密
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            logger.debug("正在加密用户密码...");
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        // 4.设置默认值
        user.setCreatedAt(LocalDateTime.now());
        if (user.getRole() == null ) {
            user.setRole("USER");
        }
        user.setEnabled(true);

        // 5.保存用户
        User savedUser = userRepository.save(user);
        logger.info("用户创建成功: ID={}, 用户名={}", savedUser.getId(), savedUser.getUsername());

        return savedUser;
    }

    /**
     * 验证用户创建数据
     */
    public void validateUserForCreation(User user) {
        if (user == null) {
            throw new IllegalArgumentException("用户信息不能为空！");
        }
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("用户名不能为空！");
        }
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("邮箱不能为空！");
        }
    }

    /** 
     * 获取所有用户
     * @return 用户列表
     */
    public List<User> getAllUsers() {
        logger.info("获取所有用户列表");
        return userRepository.findAll();
    }

    /**
     * 根据 ID 获取用户
     * @param id 用户 ID
     * @return 用户信息，若不存在则返回空
     */
    public Optional<User> getUserById(Long id) {
        logger.info("根据 ID 获取用户: {}", id);

        if( id == null) {
            throw new IllegalArgumentException("用户 ID 不能为空!");
        }
        
        return userRepository.findById(id);
    }
}
package com.example.backendpractice.repository;

import com.example.backendpractice.entity.User;
import com.example.backendpractice.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DisplayName("用户Repository测试")
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    // 添加测试方法
    @Test
    @DisplayName("应该能够保存用户")
    public void shouldSaveUser() {
        // 1.准备测试数据 (Arrange)
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("password123");
        user.setRole("USER");
        user.setEnabled(true);
        user.setCreatedAt(LocalDateTime.now());

        // 2.执行测试操作 (Act)
        User savedUser = userRepository.save(user);

        // 3.验证结果 (Assert)
        assertNotNull(savedUser.getId());                             // 验证 ID 被自动生成
        assertEquals("testuser", savedUser.getUsername());            // 判断用户名
        assertEquals("test@example.com", savedUser.getEmail());       // 判断邮箱
        assertEquals("password123", savedUser.getPassword());         // 验证原始密码（Repository 层不做加密处理）
        assertEquals("USER", savedUser.getRole());                    // 判断角色
        assertTrue(savedUser.isEnabled());                            // 判断是否启用
    }
}
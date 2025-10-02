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
        User user = new User("testuser", "test@example.com", "password123", "USER", true, LocalDateTime.now());

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

    @Test
    @DisplayName("应该能够根据用户名查找用户")
    public void shouldFindUserByUsername() {
        // 1.准备测试数据 (Arrange)
        User user = new User();
        user.setUsername("findtest");
        user.setEmail("findtest@example.com");
        user.setPassword("password123");
        user.setRole("USER");
        user.setEnabled(true);
        user.setCreatedAt(LocalDateTime.now());

        // 先保存用户
        userRepository.save(user);

        // 2.执行测试操作 (Act)
        Optional<User> foundUser = userRepository.findByUsername("findtest");

        // 3.验证结果 (Assert)
        assertTrue(foundUser.isPresent());                                          // 验证找到了用户
        assertEquals("findtest", foundUser.get().getUsername());                    // 验证用户名
        assertEquals("findtest@example.com", foundUser.get().getEmail());           // 验证邮箱
        assertEquals("password123", foundUser.get().getPassword());                 // 验证密码
    }

    @Test
    @DisplayName("应该能根据邮箱查找用户")
    public void shouldFindUserByEmail() {
        // 1.准备测试数据 (Arrange)
        User user = new User();
        user.setUsername("findtest");
        user.setEmail("findtest@example.com");
        user.setPassword("password123");
        user.setRole("USER");
        user.setEnabled(true);
        user.setCreatedAt(LocalDateTime.now());

        // 先保存用户
        userRepository.save(user);

        // 2.执行测试操作 (Act)
        Optional<User> foundUser = userRepository.findByEmail("findtest@example.com");

        // 3.验证结果 (Assert)
        assertTrue(foundUser.isPresent());                                          // 验证找到了用户
        assertEquals("findtest", foundUser.get().getUsername());                    // 验证用户名
        assertEquals("findtest@example.com", foundUser.get().getEmail());           // 验证邮箱
        assertEquals("password123", foundUser.get().getPassword());                 // 验证密码
    }

    @Test
    @DisplayName("应该允许相同用户名但不同邮箱的用户")
    public void shouldAllowSameUsernameDifferentEmail() {
        // 1.准备测试数据 (Arrange)
        // 创建相同用户名但是不同邮箱的用户
        User user1 = new User("sameuser", "user1@example.com", "password123", "USER", true, LocalDateTime.now());
        User user2 = new User("sameuser", "user2@example.com", "password123", "USER", true, LocalDateTime.now());

        // 2.执行测试操作 (Act)
        User savedUser1 = userRepository.save(user1);
        User savedUser2 = userRepository.save(user2);

        // 3.验证结果
        assertNotNull(savedUser1.getId());
        assertNotNull(savedUser2.getId());
        assertNotEquals(savedUser1.getId(), savedUser2.getId());
        assertEquals("sameuser", savedUser1.getUsername());
        assertEquals("sameuser", savedUser2.getUsername());
        assertEquals("user1@example.com", savedUser1.getEmail());
        assertEquals("user2@example.com", savedUser2.getEmail());
    }
}
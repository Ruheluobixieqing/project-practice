package com.example.backendpractice.service;

import com.example.backendpractice.entity.User;
import com.example.backendpractice.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)                           // 启用 Mockito 框架，类似 Respository 测试中的 @DataJpaTest
@DisplayName("用户Service测试")
public class UserServiceTest {

    /**
     * @Mock
     * 创建模拟对象
     * 可以控制它的行为
     */
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    /**
     * @InjectMocks
     * 将 Mock 对象注入到被测试的 Service 中
     * UserService 会使用模拟的 Repository 和 PasswordEncoder
     */
    @InjectMocks
    private UserService userService;

    private User testUser;

    /**
     * @BeforeEach
     * 每个测试前执行
     * 准备通用的测试数据
     */
    @BeforeEach
    void setUp() {
        // 准备输入数据
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("rawPassword");
    }

    // 添加测试方法
    @Test
    @DisplayName("应该能够成功创建用户")
    public void shouldCreateUserSuccessfully() {
        // 1.准备测试数据 (Arrange)
        User savedUser = new User("testuser","test@example.com","encodedPassword","USER",true,LocalDateTime.now());

        // 2.设置 Mock 行为
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("rawPassword")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // 3.执行测试操作 (Act)
        User result = userService.createUser(testUser);

        // 4.验证结果
        assertNotNull(result);
        assertEquals(savedUser.getUsername(), result.getUsername());
        assertEquals(savedUser.getEmail(), result.getEmail());
        assertEquals(savedUser.getPassword(), result.getPassword());
        assertEquals(savedUser.getRole(), result.getRole());
        assertEquals(savedUser.isEnabled(), result.isEnabled());

        // 验证 Mock 交互
        verify(userRepository).findByEmail("test@example.com");
        verify(passwordEncoder).encode("rawPassword");
        verify(userRepository).save(any(User.class));
    }
}
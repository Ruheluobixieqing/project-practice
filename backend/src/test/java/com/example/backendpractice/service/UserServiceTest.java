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
import org.mockito.ArgumentCaptor;

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

    @Test
    @DisplayName("当邮箱已经存在时应该抛出异常")
    public void shouldThrowExceptionWhenEmailExists() {
        // 1.准备测试数据 (Arrange)
        User existingUser = new User("existing", "test@example.com", "password", "USER", true, LocalDateTime.now());

        // 2.设置 Mock 行为 - 模拟邮箱已存在
        when(userRepository.findByEmail(existingUser.getEmail())).thenReturn(Optional.of(existingUser));

        // 3.执行测试并验证异常 (Ack & Assert)
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.createUser(testUser));

        // 4.验证异常信息
        assertEquals("邮箱已经存在，请勿重复创建！", exception.getMessage());

        // 5.验证交互 - 应该检查了邮箱，但不应该保存用户
        verify(userRepository).findByEmail("test@example.com");
        verify(userRepository, never()).save(any(User.class));
        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    @DisplayName("应该正确设置用户的默认值")
    public void shouldSetDefaultValues() {
        
        testUser.setRole(null);                    // 不设置角色，让 Service 设置默认值
        User savedUser = new User("testuser", "test@example.com", "encodedPassword", "USER", true, LocalDateTime.now());

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("rawPassword")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        User result = userService.createUser(testUser);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User capturedUser = userCaptor.getValue();
        assertEquals("USER", capturedUser.getRole());                   // 验证默认角色
        assertTrue(capturedUser.isEnabled());                           // 验证默认启用状态
        assertNotNull(capturedUser.getCreatedAt());                     // 验证创建时间被设置
        assertEquals("encodedPassword", capturedUser.getPassword());    // 验证密码被加密

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
    }

    @Test
    @DisplayName("验证用户创建数据 - 用户为 null 时应该抛出异常")
    public void shouldThrowExceptionWhenUserIsNull() {

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.validateUserForCreation(null));

        assertEquals("用户信息不能为空！", exception.getMessage());
    }

    @Test
    @DisplayName("验证用户创建数据 - 用户名为空时应该抛出异常")
    public void shouldThrowExceptionWhenUsernameisEmpty() {
        
        User user = new User();
        user.setUsername("");
        user.setEmail("test@example.com");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.validateUserForCreation(user));

        assertEquals("用户名不能为空！", exception.getMessage());
    }

    @Test
    @DisplayName("验证用户创建数据 - 用户名为空白时应该抛出异常")
    public void shouldThrowExceptionWhenUsernameIsBlank() {
        
        User user = new User();
        user.setUsername("   ");
        user.setEmail("test@example.com");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.validateUserForCreation(user));

        assertEquals("用户名不能为空！", exception.getMessage());
    }

    @Test
    @DisplayName("验证用户创建数据 - 邮箱为空时应该抛出异常")
    public void shouldThrowExceptionWhenEmailIsEmpty() {
        
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("");  // 空字符串
    
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.validateUserForCreation(user));
    
        assertEquals("邮箱不能为空！", exception.getMessage());
    }
}
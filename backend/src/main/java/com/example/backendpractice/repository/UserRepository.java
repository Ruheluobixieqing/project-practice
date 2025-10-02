package com.example.backendpractice.repository;

import java.util.Optional;
import com.example.backendpractice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    // Spring会自动提供以下基本方法：
    // save(User user) - 保存或更新用户
    // findById(Long id) - 根据ID查找用户
    // findAll() - 查找所有用户
    // deleteById(Long id) - 根据ID删除用户
    // count() - 统计用户数量

    // Spring Data JPA 会根据方法名自动生成实现

    // 查询关键词：
    // findBy - 查询
    // countBy - 计数
    // deleteBy - 删除
    // existBy - 存在性检查

    // 字段连接
    // findByUsername - 根据 username 字段查询
    // findByEmail - 根据 username 字段查询
    // findByUsernameAndEmail - 根据 username AND email 查询

    // 较为复杂的查询需要 @Query 注释，手动实现

    // 根据用户名查找用户
    Optional<User> findByUsername(String username);

    // 根据邮箱查找用户
    Optional<User> findByEmail(String email);
}
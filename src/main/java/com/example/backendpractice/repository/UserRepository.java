package com.example.backendpractice.repository;

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
}
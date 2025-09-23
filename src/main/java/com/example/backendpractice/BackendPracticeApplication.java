/*
 * 什么是包(package)？
 * - 包就像文件夹，用来组织Java类
 * - 包名必须和文件夹路径完全一致
 * - com.example.backendpractice 对应文件夹：com/example/backendpractice
 * - 这样可以避免不同包中的类名冲突
 */
package com.example.backendpractice;

/*
 * 什么是import？
 * - import就是"导入"，告诉Java我要使用其他地方的类
 * - 就像在Word中插入图片一样，需要先"导入"才能使用
 */
import org.springframework.boot.SpringApplication;           // 导入Spring Boot的启动器类
import org.springframework.boot.autoconfigure.SpringBootApplication;  // 导入Spring Boot的主注解

/*
 * 什么是注解(@)？
 * - 注解就像"标签"，给代码贴上特殊说明
 * - @SpringBootApplication 这个标签告诉Spring：
 *   "这是一个Spring Boot应用的主类"
 * 
 * 这个注解实际上包含了3个功能：
 * 1. @Configuration - 告诉Spring这是配置文件
 * 2. @EnableAutoConfiguration - 让Spring自动配置功能(比如数据库、Web服务器)
 * 3. @ComponentScan - 让Spring自动寻找项目中的其他组件
 */
@SpringBootApplication
public class BackendPracticeApplication {
    
    /*
     * 什么是main方法？
     * - 这是Java程序的"大门"，程序从这里开始运行
     * - 就像手机APP的启动图标，点击后程序开始执行
     * - public static void main(String[] args) 是固定写法
     * 
     * 参数解释：
     * - public：任何地方都可以调用
     * - static：不需要创建对象就能调用
     * - void：这个方法不返回任何值
     * - String[] args：命令行参数(现在可以忽略)
     */
    public static void main(String[] args) {
        /*
         * SpringApplication.run() 是Spring Boot的"魔法方法"
         * 这一行代码会自动完成很多工作：
         * 
         * 1. 启动一个Web服务器(Tomcat) - 让你的程序能接收网络请求
         * 2. 连接数据库 - 根据配置自动连接MySQL
         * 3. 扫描你写的代码 - 找到所有的Controller、Service等
         * 4. 配置各种功能 - JSON处理、数据验证等
         * 
         * 参数说明：
         * - BackendPracticeApplication.class：告诉Spring以这个类为主配置
         * - args：传递命令行参数给Spring Boot
         */
        SpringApplication.run(BackendPracticeApplication.class, args);
        
        // 当这行代码执行后，你的Web应用就启动了！
        // 默认会在 http://localhost:8080 运行
    }
}

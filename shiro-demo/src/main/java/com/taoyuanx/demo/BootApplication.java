package com.taoyuanx.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author dushitaoyuan
 * @date 2019/9/2622:46
 * @desc: 统计任务启动类
 */
@MapperScan(basePackages = "com.taoyuanx.demo.mapper")
@SpringBootApplication
@EnableScheduling
@EnableCaching
public class BootApplication {
    public static void main(String[] args) {
        SpringApplication.run(BootApplication.class, args);
    }
}

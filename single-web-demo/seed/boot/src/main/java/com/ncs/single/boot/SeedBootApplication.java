package com.ncs.single.boot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @author dushitaoyuan
 * @date 2019/9/2622:46
 * @desc: 启动类
 */
@SpringBootApplication
@MapperScan(basePackages = "com.ncs.single.boot.dao")
public class SeedBootApplication  extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(SeedBootApplication.class, args);
    }
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(SeedBootApplication.class);
    }


}

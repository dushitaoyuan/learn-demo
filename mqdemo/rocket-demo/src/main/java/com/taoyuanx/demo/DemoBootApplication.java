package com.taoyuanx.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;


/**
 * @author dushitaoyuan
 * @desc: 启动类
 */
@SpringBootApplication
@MapperScan(basePackages = "com.ncs.analysis.app.dao")
public class DemoBootApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(DemoBootApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(DemoBootApplication.class);
    }

}

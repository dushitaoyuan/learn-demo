package com.taoyuanx.demo;

import org.apache.dubbo.config.spring.context.annotation.EnableDubboConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@EnableDubboConfig
public class DubboConsumerApplicatioin {
    public static void main(String[] args) {
        SpringApplication.run(DubboConsumerApplicatioin.class, args);
    }
}

package com.taoyuanx.demo;

import com.taoyuanx.thrift.autoconfigure.EnableThriftClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@EnableThriftClient
public class ThriftClientApplicatioin {
    public static void main(String[] args) {
        SpringApplication.run(ThriftClientApplicatioin.class, args);
    }
}

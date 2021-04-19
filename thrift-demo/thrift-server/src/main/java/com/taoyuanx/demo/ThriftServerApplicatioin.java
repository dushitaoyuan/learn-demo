package com.taoyuanx.demo;

import com.taoyuanx.thrift.autoconfigure.EnableThriftServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@EnableThriftServer
public class ThriftServerApplicatioin {
    public static void main(String[] args) {
        SpringApplication.run(ThriftServerApplicatioin.class, args);
    }
}

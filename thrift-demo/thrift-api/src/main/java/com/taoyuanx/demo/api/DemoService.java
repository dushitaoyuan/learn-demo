package com.taoyuanx.demo.api;

import com.taoyuanx.demo.api.dto.HelloRequest;
import com.taoyuanx.demo.api.dto.HelloResponse;
import io.airlift.drift.annotations.ThriftMethod;
import io.airlift.drift.annotations.ThriftService;

/**
 * @author dushitaoyuan
 * @date 2020/7/13
 */
@ThriftService
public interface DemoService {
    @ThriftMethod
    String hello(String demo);

    @ThriftMethod
    HelloResponse hello2(HelloRequest helloRequest);
}

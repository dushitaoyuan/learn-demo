package com.taoyuanx.demo.impl;

import com.taoyuanx.demo.api.DemoService;
import com.taoyuanx.demo.api.dto.HelloRequest;
import com.taoyuanx.demo.api.dto.HelloResponse;
import io.airlift.drift.annotations.ThriftMethod;
import io.airlift.drift.annotations.ThriftService;

/**
 * @author dushitaoyuan
 * @date 2020/7/13
 */
public class DemoServiceImpl implements DemoService {
    @Override
    public String hello(String demo) {
        return "呦呦切克闹" + demo;
    }

    @Override
    public HelloResponse hello2(HelloRequest helloRequest) {
        HelloResponse helloResponse = new HelloResponse();
        helloResponse.setMsg(helloRequest.getName() + "\t" + helloRequest.getValue());
        return helloResponse;
    }
}
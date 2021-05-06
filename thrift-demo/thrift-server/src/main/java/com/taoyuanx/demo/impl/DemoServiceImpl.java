package com.taoyuanx.demo.impl;

import com.taoyuanx.demo.api.DemoService;
import com.taoyuanx.demo.api.dto.HelloRequest;
import com.taoyuanx.demo.api.dto.HelloResponse;
import com.taoyuanx.thrift.core.server.ThriftServiceImpl;
import lombok.extern.slf4j.Slf4j;

/**
 * @author dushitaoyuan
 * @date 2020/7/13
 */
@ThriftServiceImpl(port = 9091)
@Slf4j
public class DemoServiceImpl implements DemoService {
    @Override
    public String hello(String demo) {
        return "呦呦切克闹" + demo;
    }

    @Override
    public HelloResponse hello2(HelloRequest helloRequest) {
        HelloResponse helloResponse = new HelloResponse();
        helloResponse.setMsg(helloRequest.getName() + "\t" + helloRequest.getValue());
        log.warn("服务调用{}", helloRequest);
        return helloResponse;
    }
}

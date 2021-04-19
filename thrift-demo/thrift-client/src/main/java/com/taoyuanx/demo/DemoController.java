package com.taoyuanx.demo;

import com.taoyuanx.demo.api.DemoService;
import com.taoyuanx.thrift.core.client.ThriftClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dushitaoyuan
 * @date 2020/7/13
 */
@RestController
@Slf4j
public class DemoController {
    @ThriftClient(serverList = "localhost:9090")
    DemoService demoService;

    @ThriftClient(serverList = "localhost:9090")
    DemoService demoService2;

    @GetMapping("hello")
    public String hello(String demo) {
        log.debug("log demo");
        demoService2.hello(demo);
        return demoService.hello(demo);
    }
}

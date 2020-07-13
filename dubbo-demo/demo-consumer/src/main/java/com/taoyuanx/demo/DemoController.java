package com.taoyuanx.demo;

import com.taoyuanx.demo.api.DemoService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dushitaoyuan
 * @date 2020/7/13
 */
@RestController

public class DemoController {
    @DubboReference
    DemoService demoService;

    @GetMapping("hello")
    public String hello(String demo) {
        return demoService.hello(demo);
    }
}

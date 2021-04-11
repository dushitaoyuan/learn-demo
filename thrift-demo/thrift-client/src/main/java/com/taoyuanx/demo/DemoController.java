package com.taoyuanx.demo;

import com.taoyuanx.demo.api.DemoService;
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
    DemoService demoService;

    @GetMapping("hello")
    public String hello(String demo) {
        log.debug("log demo");
        return demoService.hello(demo);
    }
}

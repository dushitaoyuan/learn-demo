package com.taoyuanx.demo.impl;

import com.taoyuanx.demo.api.DemoService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Component;

/**
 * @author dushitaoyuan
 * @date 2020/7/13
 */
@DubboService
@Component
public class DemoServiceImpl implements DemoService {
    @Override
    public String hello(String demo) {
        return "呦呦切克闹" + demo;
    }
}

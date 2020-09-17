package com.ncs.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author dushitaoyuan
 */
@RestController
public class DemoController {


    @PostMapping("hello")
    public String hello(String hello) {
        return "hello " + hello;
    }

    @PostMapping("json")
    @ResponseBody
    public Map hello(@RequestBody Map map) {
        map.put("hello","hello");
        return map;
    }
}

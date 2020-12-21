package com.taoyuanx.demo.controller;

import com.taoyuanx.demo.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class LoginController {

    private static final String INDEX_PAGE = "index", LOGIN_PAGE = "login";



    @GetMapping("/login")
    public String loginPage() {
        return LOGIN_PAGE;
    }

    /**
     * 退出
     */
    @GetMapping(value = "/logout")
    public String logout() {
        return "redirect:/login";
    }

    @GetMapping(value = "/errorTest")
    public String error() {
        throw new ServiceException(101, "测试异常");
    }

    @GetMapping("/index")
    public String index() {
        return INDEX_PAGE;
    }

    @GetMapping("/public")
    public String page() {
        return "public";
    }
}

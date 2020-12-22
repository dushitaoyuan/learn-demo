package com.taoyuanx.demo.controller;

import com.taoyuanx.demo.exception.ServiceException;
import com.taoyuanx.demo.utils.SessionUtil;
import com.taoyuanx.demo.vo.LoginUserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Role;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Slf4j
public class LoginController {

    private static final String INDEX_PAGE = "index", LOGIN_PAGE = "login";


    @GetMapping("/login")
    public String loginPage() {
        return LOGIN_PAGE;
    }


    @GetMapping(value = "/errorTest")
    public String error() {
        throw new ServiceException(101, "测试异常");
    }

    @GetMapping(value = "/admin")
    @ResponseBody
    @PreAuthorize("hasRole('admin')")
    public LoginUserVo admin() {
        return SessionUtil.getConcurrentUser();
    }

    @GetMapping(value = "/audit")
    @ResponseBody
    @PreAuthorize("hasRole('audit')")
    public LoginUserVo audit() {
        return SessionUtil.getConcurrentUser();
    }

    @GetMapping("/index")
    @PreAuthorize("hasAuthority('index')")
    public String index() {
        return INDEX_PAGE;
    }

    @GetMapping("/public")
    public String page() {
        return "public";
    }
}

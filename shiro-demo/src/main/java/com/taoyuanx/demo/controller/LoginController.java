package com.taoyuanx.demo.controller;

import com.taoyuanx.demo.dto.LoginForm;
import com.taoyuanx.demo.entity.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@Slf4j
public class LoginController {
    /**
     * 登录
     */
    @RequestMapping("/login")
    @ResponseBody
    public String login(@RequestBody LoginForm user) {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(user.getUsername(), user.getPassword());
        try {
            subject.login(usernamePasswordToken);
        } catch (AuthenticationException e) {
            log.warn("登录异常", e);
            return "账号或密码错误！";
        } catch (AuthorizationException e) {
            e.printStackTrace();
            return "没有权限";
        }
        return "login success";
    }

    /**
     * 退出
     */
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    @ResponseBody
    public String logout() {
        SecurityUtils.getSubject().logout();
        return "logout success";

    }

    @RequiresRoles("admin")
    @RequiresPermissions("index")
    @RequestMapping("/index")
    public String index() {
        return "index";
    }
}

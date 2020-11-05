package com.taoyuanx.demo.controller;

import com.taoyuanx.demo.api.Result;
import com.taoyuanx.demo.api.ResultBuilder;
import com.taoyuanx.demo.config.shiro.UserLoginToken;
import com.taoyuanx.demo.dto.LoginForm;
import com.taoyuanx.demo.entity.UserEntity;
import com.taoyuanx.demo.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@Slf4j
public class LoginController {

    private  static  final String INDEX_PAGE="index",LOGIN_PAGE="login";
    /**
     * 登录
     */
    @PostMapping("/login")
    @ResponseBody
    public Result login(@RequestBody LoginForm user) {
        Subject subject = SecurityUtils.getSubject();
        try {
            UserLoginToken userLoginToken = new UserLoginToken();
            userLoginToken.setUsername(user.getUsername());
            userLoginToken.setPassword(user.getPassword().toCharArray());
            userLoginToken.setRememberMe(user.isRememberMe());
            subject.login(userLoginToken);
        } catch (AuthenticationException e) {
            log.warn("登录异常", e);
            throw new ServiceException("账号或密码错误！");
        }
        return ResultBuilder.success(user.getUsername());
    }

    @GetMapping("/login")
    public String loginPage() {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            return "redirect:/index";
        }
        if (subject.isRemembered()) {
            PrincipalCollection principals = subject.getPrincipals();
            subject.runAs(principals);
            return "redirect:/index";
        }
        return LOGIN_PAGE;
    }

    /**
     * 退出
     */
    @GetMapping(value = "/logout")
    public String logout() {
        SecurityUtils.getSubject().logout();
        return "redirect:/login";
    }

    @RequiresUser
    @GetMapping("/index")
    public String index() {
        return INDEX_PAGE;
    }

    @GetMapping("/public")
    public String page() {
        return "public";
    }
}

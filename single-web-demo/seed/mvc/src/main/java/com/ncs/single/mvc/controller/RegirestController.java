package com.ncs.single.mvc.controller;

import com.ncs.single.mvc.enums.RoleTypeEnum;
import com.ncs.single.mvc.security.RequireRole;
import com.ncs.single.mvc.service.AccountService;
import com.ncs.single.mvc.utils.SessionUserUtil;
import com.ncs.single.mvc.vo.LoginForm;
import com.ncs.single.mvc.vo.RegistForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @author dushitaoyuan
 * @desc 注册
 * @date 2019/12/18
 */
@Controller
@RequireRole(role = RoleTypeEnum.PUBLIC)
public class RegirestController {
    @Autowired
    AccountService accountService;
    @PostMapping({"regist"})
    public String login(@Valid  RegistForm registForm, HttpServletRequest request) {
        accountService.regist(registForm);
        return "redirect:/login";
    }
}


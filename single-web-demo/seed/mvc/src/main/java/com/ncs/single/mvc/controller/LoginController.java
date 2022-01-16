package com.ncs.single.mvc.controller;

import com.ncs.single.mvc.commons.SessionConstants;
import com.ncs.single.mvc.enums.RoleTypeEnum;
import com.ncs.single.mvc.security.RequireRole;
import com.ncs.single.mvc.service.AccountService;
import com.ncs.single.mvc.utils.SessionUserUtil;
import com.ncs.single.mvc.vo.LoginForm;
import com.ncs.single.mvc.vo.LoginUserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author lianglei
 * @date 2019/10/16 15:06
 * @desc 登录
 **/
@Controller
@RequireRole(role = RoleTypeEnum.PUBLIC)
public class LoginController {

    @Autowired
    AccountService accountService;

    @PostMapping({"login"})
    public String login(LoginForm loginForm, HttpServletRequest request) {
        SessionUserUtil.setUser(accountService.login(loginForm), request);
        return "redirect:/index";
    }
    @PostMapping({"login/test"})
    public String loginTest(HttpServletRequest request) {
        LoginUserVo loginUserVo = new LoginUserVo(null,null);
        loginUserVo.setAccountId(1L);
        loginUserVo.setName("测试demo");
        loginUserVo.setRole(RoleTypeEnum.ADMIN);
        SessionUserUtil.setUser(loginUserVo, request);
        SessionUserUtil.setAttribute(SessionConstants.REGIST_VAFY_CODE,"123456",request);
        return "redirect:/index";
    }

    @GetMapping({"loginOut"})
    public String loginOut(Model model, HttpServletRequest request) {
        request.getSession().invalidate();
        return "redirect:/login.jsp";
    }

    @GetMapping({"/index", ""})
    public String index(Model model, HttpServletRequest request) {
        return "index";
    }
}

package com.taoyuanx.demo.utils;

import com.taoyuanx.demo.vo.LoginUserVo;
import org.springframework.security.core.context.SecurityContextHolder;


/**
 * 会话工具
 */
public class SessionUtil {
    public static LoginUserVo getConcurrentUser() {
        return (LoginUserVo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}

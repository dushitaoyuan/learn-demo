package com.taoyuanx.demo.utils;

import com.taoyuanx.demo.vo.LoginUserVo;
import org.apache.shiro.SecurityUtils;


/**
 * 会话工具
 */
public class SessionUtil {
    public static LoginUserVo getConcurrentUser() {
        return (LoginUserVo) SecurityUtils.getSubject().getPrincipal();
    }


}

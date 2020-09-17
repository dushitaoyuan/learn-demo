package com.taoyuanx.demo.service;

import com.taoyuanx.demo.vo.LoginUserVo;


/**
 *
 */

public interface LoginService {
    LoginUserVo getByUsername(String username);
}

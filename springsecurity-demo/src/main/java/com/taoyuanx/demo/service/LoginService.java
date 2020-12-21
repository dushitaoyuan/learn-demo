package com.taoyuanx.demo.service;


import com.taoyuanx.demo.entity.UserEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 *
 */

public interface LoginService extends UserDetailsService {


    UserEntity  findByUserName(String username);
}

package com.taoyuanx.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.taoyuanx.demo.entity.UserEntity;
import com.taoyuanx.demo.mapper.PermissionMapper;
import com.taoyuanx.demo.mapper.RoleMapper;
import com.taoyuanx.demo.mapper.UserMapper;
import com.taoyuanx.demo.service.LoginService;
import com.taoyuanx.demo.vo.LoginUserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author dushitaoyuan
 * @date 2020/9/8
 */


@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    RoleMapper roleMapper;
    @Autowired
    PermissionMapper permissionMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity dbUser = findByUserName(username);
        /**
         * 角色
         */
        String ROLE_PREFIX = "ROLE_";
        List<SimpleGrantedAuthority> roleList = roleMapper.listRoleByUserId(dbUser.getId()).stream().map(role -> {
            return new SimpleGrantedAuthority(ROLE_PREFIX + role.getName());
        }).collect(Collectors.toList());
        /**
         * 权限
         */
        List<SimpleGrantedAuthority> permissionList = permissionMapper.listPermissionByUserId(dbUser.getId()).stream().map(permission -> {
            return new SimpleGrantedAuthority(permission.getValue());
        }).collect(Collectors.toList());
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>(roleList);
        authorityList.addAll(permissionList);

        LoginUserVo loginUserVo = new LoginUserVo(username, dbUser.getPassword(), authorityList);
        loginUserVo.setUser(dbUser);
        return loginUserVo;
    }


    @Override
    public UserEntity findByUserName(String username) {
        UserEntity dbUser = userMapper.selectOne(new LambdaQueryWrapper<UserEntity>().eq(UserEntity::getUsername, username));
        return dbUser;
    }
}

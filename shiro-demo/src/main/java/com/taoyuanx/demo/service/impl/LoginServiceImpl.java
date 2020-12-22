package com.taoyuanx.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.taoyuanx.demo.entity.PermissionEntity;
import com.taoyuanx.demo.entity.UserEntity;
import com.taoyuanx.demo.entity.RoleEntity;
import com.taoyuanx.demo.mapper.RoleMapper;
import com.taoyuanx.demo.mapper.UserMapper;
import com.taoyuanx.demo.mapper.PermissionMapper;
import com.taoyuanx.demo.service.LoginService;
import com.taoyuanx.demo.vo.LoginUserVo;
import com.vip.vjtools.vjkit.mapper.BeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public LoginUserVo getByUsername(String username) {
        UserEntity user = userMapper.selectOne(new LambdaQueryWrapper<UserEntity>().eq(UserEntity::getUsername, username));
        if (null == user) {
            return null;
        }
        /**
         * 角色
         */
        List<RoleEntity> roleList = roleMapper.listRoleByUserId(user.getId());
        /**
         * 权限
         */
        List<PermissionEntity> permissionList = permissionMapper.listPermissionByUserId(user.getId());
        LoginUserVo loginUserVo = BeanMapper.map(user, LoginUserVo.class);
        loginUserVo.setRoleList(roleList);
        loginUserVo.setPermissionList(permissionList);
        return loginUserVo;
    }
}

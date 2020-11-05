package com.taoyuanx.demo.config.shiro;


import com.taoyuanx.demo.exception.ServiceException;
import com.taoyuanx.demo.service.LoginService;
import com.taoyuanx.demo.vo.LoginUserVo;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;


public class DemoShiroRealm extends AuthorizingRealm {

    @Autowired
    private LoginService loginService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        Object primaryPrincipal = principalCollection.getPrimaryPrincipal();
        LoginUserVo user = null;
        if (primaryPrincipal instanceof String) {
            String name = (String) principalCollection.getPrimaryPrincipal();
            user = loginService.getByUsername(name);
        } else if (primaryPrincipal instanceof LoginUserVo) {
            user = (LoginUserVo) primaryPrincipal;
        }
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();

        user.getRoleList().stream().filter(Objects::nonNull).forEach(role -> {
            simpleAuthorizationInfo.addRole(role.getName());
        });
        user.getPermissionList().stream().filter(Objects::nonNull).forEach(permission -> {
            simpleAuthorizationInfo.addStringPermission(permission.getValue());
        });
        return simpleAuthorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        if (authenticationToken.getPrincipal() == null) {
            return null;
        }
        if (authenticationToken instanceof UserLoginToken) {
            String username = authenticationToken.getPrincipal().toString();
            LoginUserVo user = loginService.getByUsername(username);
            if (user == null) {
                throw new UnknownAccountException("账号不存在！");
            } else {
                SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(user, user.getPassword(), null, String.valueOf(user.getId()));
                return simpleAuthenticationInfo;
            }
        }
        throw new ServiceException("登录逻辑异常,请检查！");
    }
}

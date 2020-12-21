package com.taoyuanx.demo.vo;

import com.taoyuanx.demo.entity.PermissionEntity;
import com.taoyuanx.demo.entity.UserEntity;
import com.taoyuanx.demo.entity.RoleEntity;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * @author dushitaoyuan
 * @date 2020/9/8
 */
public class LoginUserVo  extends User {
    @Getter
    @Setter
    private UserEntity user;




    public LoginUserVo(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public LoginUserVo(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }

}

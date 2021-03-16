package com.taoyuanx.demo.vo;

import com.taoyuanx.demo.entity.PermissionEntity;
import com.taoyuanx.demo.entity.UserEntity;
import com.taoyuanx.demo.entity.RoleEntity;
import lombok.Data;

import java.util.List;

/**
 * @author dushitaoyuan
 * @date 2020/9/8
 */
@Data
public class LoginUserVo extends UserEntity {
    /**
     * 角色
     */
    List<RoleEntity> roleList ;
    /**
     * 权限
     */
    List<PermissionEntity> permissionList ;
}

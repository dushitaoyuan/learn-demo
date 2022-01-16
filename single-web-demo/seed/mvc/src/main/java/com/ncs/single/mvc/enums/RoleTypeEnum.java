package com.ncs.single.mvc.enums;

import com.ncs.single.mvc.security.RequireRole;

/**
 * @author dushitaoyuan
 * @desc 角色类型
 * @date 2019/12/17
 */
public enum RoleTypeEnum {
    /**
     * 权限范围,数值越大权限越小
     */
    ADMIN(1, "超级管理员"),
    COMMON(2, "普通用户"),
    LOGIN(5, "登录用户"),
    PUBLIC(6, "匿名用户");


    public int code;
    public String desc;

    RoleTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }


    private   static  boolean isMatch(RoleTypeEnum[] roles, Integer roleCode){
        for(RoleTypeEnum role:roles){
            if(roleCode<=role.code){
                return true;
            }
        }
        return false;
    }

    public    static RoleTypeEnum roleType(Integer roleCode){
        for(RoleTypeEnum role: RoleTypeEnum.values()){
            if(roleCode<=role.code){
                return role;
            }
        }
        return null;
    }

    public  static  boolean hasRole(RequireRole requireRole, RoleTypeEnum currentRole){
        if(requireRole==null){
            return true;
        }

        RoleTypeEnum[] needRoles = requireRole.role();
        if(needRoles==null){
            return true;
        }
        if(currentRole==null){
            currentRole= RoleTypeEnum.PUBLIC;
        }
        return  isMatch(needRoles,currentRole.code);

    }
}

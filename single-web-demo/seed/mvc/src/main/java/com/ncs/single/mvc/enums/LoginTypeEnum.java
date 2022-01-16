package com.ncs.single.mvc.enums;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author dushitaoyuan
 * @desc 登录类型
 * @date 2019/12/17
 */
public enum LoginTypeEnum {

    USERNAME_LOGIN(1, "用户名登录"),
    PHONE_LOGIN(2, "手机号登录"),
    EMAIL_LOGIN(3, "邮箱登录");


    public int code;
    public String desc;
    private  static  final Map<Integer, LoginTypeEnum> enumHolder=new HashMap<>();
    static {
        LoginTypeEnum[] loginTypeArray = LoginTypeEnum.values();
        Arrays.stream(loginTypeArray).forEach(loginType -> {
            enumHolder.put(loginType.code,loginType);
        });
    }
    LoginTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public static  boolean supportLogin(Integer loginType){
        return enumHolder.containsKey(loginType);
    }
    public static LoginTypeEnum type(Integer loginType){
        return enumHolder.get(loginType);
    }
}

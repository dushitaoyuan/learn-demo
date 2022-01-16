package com.ncs.single.mvc.enums;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author dushitaoyuan
 * @desc 注册类型
 * @date 2019/12/17
 */
public enum RegistTypeEnum {

    USERNAME_REGIST(1, "用户名注册"),
    PHONE_REGIST(2, "手机号注册"),
    EMAIL_REGIST(3, "邮箱注册");


    public int code;
    public String desc;
    private  static  final Map<Integer, RegistTypeEnum> enumHolder=new HashMap<>();
    static {
        RegistTypeEnum[] registTypeArray = RegistTypeEnum.values();
        Arrays.stream(registTypeArray).forEach(registType -> {
            enumHolder.put(registType.code,registType);
        });
    }
    RegistTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public static  boolean supportRegist(Integer registType){
        return enumHolder.containsKey(registType);
    }
    public static RegistTypeEnum type(Integer registType){
        return enumHolder.get(registType);
    }
}

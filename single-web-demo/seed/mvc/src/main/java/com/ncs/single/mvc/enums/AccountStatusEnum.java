package com.ncs.single.mvc.enums;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author dushitaoyuan
 * @desc 账户状态枚举
 * @date 2019/12/18
 */
public enum  AccountStatusEnum {
    NORMAL(1, "正常"),
    LOCKED(2, "锁定"),
    ABNORMAL(3, "异常"),
    DELETED(0, "删除");


    public int code;
    public String desc;
    private  static  final Map<Integer, AccountStatusEnum> enumHolder=new HashMap<>();
    static {
        AccountStatusEnum[] enumArray  = AccountStatusEnum.values();
        Arrays.stream(enumArray).forEach(enumObj -> {
            enumHolder.put(enumObj.code,enumObj);
        });
    }
    AccountStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public static AccountStatusEnum type(Integer accoutStatus){
        return enumHolder.get(accoutStatus);
    }
}

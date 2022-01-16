package com.ncs.springbase.core.aop;

import com.ncs.springbase.core.aop.anno.Auth;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author dushitaoyuan
 * @desc aop 工具类
 * @date 2019/12/27
 */
public class AopUtil {
    public  static  Object findValue(Object[] args,String[] parameterNames,String needName){
        if(Objects.nonNull(parameterNames)){
            int index=0;
            for(String name:parameterNames){
                if(needName.equals(name)){
                    return args[index];
                }
                index++;
            }
        }
        return null;
    }

    public  static  boolean isUnAuth(Auth auth){
        if(Objects.isNull(auth)||(Objects.nonNull(auth)&&(!auth.auth()||auth.needRole().equals("public")))){
            return true;
        }
        return false;
    }
}

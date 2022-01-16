package com.ncs.springbase.core.aop;

import com.ncs.springbase.core.aop.anno.Auth;
import org.aspectj.lang.reflect.MethodSignature;

import java.util.Objects;

/**
 * @author dushitaoyuan
 * @desc HelloWorldServiceAspect
 * @date 2019/12/27
 *
 */
public aspect HelloWorldServiceAspect {
    /**
     * 所有public 方法切点
     */
    public pointcut allPublicMethodPoint(): execution(public * com.ncs.springbase.core.aop.HelloWorldService+.*(..));
    /**
     * 所有私有 方法切点
     */
    public pointcut allPrivateMethodPoint(): execution(private * com.ncs.springbase.core.aop.HelloWorldService+.*(..));
    /**
     * 注解 方法切点
     */
    public pointcut allAuthMethodPoint(): execution(public * com.ncs.springbase.core.aop.HelloWorldService+.*(..))&& @annotation(Auth);

    Object around(): allPublicMethodPoint(){
        System.out.println("around before");
        Object result = proceed();
        System.out.println("around after" + result);
        return result;
    }
    Object around(): allPrivateMethodPoint(){
        System.out.println("private aop");
        return proceed();
    }
    before(): allAuthMethodPoint(){
        System.out.println("only auth before");
         MethodSignature methodSignature = (MethodSignature) thisJoinPoint.getSignature();
         Auth auth = methodSignature.getMethod().getAnnotation(Auth.class);
         if(AopUtil.isUnAuth(auth)){
             return;
         }
         Object currentRole=AopUtil.findValue(thisJoinPoint.getArgs(),methodSignature.getParameterNames(),"role");
         if(Objects.nonNull(currentRole) && !currentRole.toString().equals("admin")){
             throw  new RuntimeException("权限异常");
         }
    }



}
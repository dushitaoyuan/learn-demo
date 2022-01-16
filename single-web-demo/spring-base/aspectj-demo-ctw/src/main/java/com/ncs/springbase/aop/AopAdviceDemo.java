package com.ncs.springbase.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

@Aspect
public  class AopAdviceDemo {
    private static final String name = AopAdviceDemo.class.getSimpleName() + "\t";

    @Pointcut("execution(* com.ncs.springbase.service.*.*(..))")
    public  void aspectjPointCut(){};

    @Around("aspectjPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {

        System.out.println(name + "@Around 前");
        Object returnValue = point.proceed(point.getArgs());
        System.out.println(name + "@Around 后");
        return returnValue;
    }

    @Before("aspectjPointCut()")
    public void before(JoinPoint point) {
        System.out.println(name + "@Before");
    }

    @AfterReturning(pointcut = "aspectjPointCut()",
            returning = "returnValue")
    public void afterReturning(JoinPoint point, Object returnValue) {
        System.out.println(name + "@AfterReturning");
    }

    @After("aspectjPointCut()")
    public void after(JoinPoint point) {
        System.out.println(name + "@After");
    }
}
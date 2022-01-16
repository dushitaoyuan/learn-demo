package com.ncs.sprinbase.core.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author dushitaoyuan
 * @desc EventAop demo
 * @date 2019/12/27
 */
@Aspect
@Slf4j
@Component
public class EventAop {
    @Pointcut(
            value = "execution(* com.ncs.sprinbase.core.controller.*.*(..))&&@annotation(com.ncs.sprinbase.core.log.OperateAudit)")
    public void eventPointCut() {
    }

    @Around("eventPointCut()")
    public Object log(ProceedingJoinPoint point) throws Throwable {
        log.info("event before");

        Object result = point.proceed(point.getArgs());

        log.info("event end");

        return result;
    }
}



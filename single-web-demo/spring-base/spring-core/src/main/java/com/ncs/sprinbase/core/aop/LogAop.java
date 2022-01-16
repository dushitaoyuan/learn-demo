package com.ncs.sprinbase.core.aop;

import java.lang.reflect.Method;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.ncs.sprinbase.core.utils.SpringContextUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;

import com.ncs.sprinbase.core.dao.LogDao;
import com.ncs.sprinbase.core.entity.LogEntity;
import com.ncs.sprinbase.core.log.OperateAudit;
import com.ncs.sprinbase.core.utils.IdGenUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @author dushitaoyuan
 * @desc logAop demo
 * @date 2019/12/27
 */
@Aspect
@Slf4j
@Component
public class LogAop {
    private static ThreadLocal<String>              msgThreadLocal        = new ThreadLocal<>();
    private static ThreadLocal<Map<String, Object>> msgContextThreadLocal = new ThreadLocal<>();
    LogDao  logDao;
    public LogAop(){
        this.logDao=SpringContextUtil.getBean(LogDao.class);
    }
    @Pointcut(
            value = "execution(* com.ncs.sprinbase.core.controller.*.*(..))&&@annotation(com.ncs.sprinbase.core.log.OperateAudit)")
    public void logPointCut() {}


    @Around("logPointCut()")
    public Object log(ProceedingJoinPoint point) throws Throwable {
        log.info("log before");
        try {
            Object result = point.proceed();
            return result;
        } catch (Throwable throwable) {
            throw throwable;
        } finally {
            OperateAudit operateAudit = calcCode(point);

            if (operateAudit != null) {
                log.debug("操作代码:[{}]", operateAudit.code());
                LogEntity log = new LogEntity();
                log.setCode(operateAudit.code());
                log.setMsg(msg());
                log.setCreatetime(new Date());
                log.setId(IdGenUtil.genLongId());
                Map<String, Object> context = msgContextThreadLocal.get();

                if (context != null) {
                    log.setMsgContext(JSON.toJSONString(context));
                }
                clearThreadLocal();
                saveLog(log);
            }
            log.info("log end");
        }
    }
    /**
     * 亦可使用内存日志队列,批量存
     */
    @Async("logPool")
    public void saveLog(LogEntity log) {
        logDao.saveLog(log);
    }

    public static void logContext(String contextKey, Object contextValue) {
        Map<String, Object> logContext = msgContextThreadLocal.get();

        if (logContext == null) {
            logContext = new HashMap<>();
            msgContextThreadLocal.set(logContext);
        }

        if (Objects.nonNull(contextKey) && Objects.nonNull(contextValue)) {
            logContext.put(contextKey, contextValue);
        }
    }

    public static void logMsg(String msg) {
        msgThreadLocal.set(msg);
    }


    public static String msg() {
        return msgThreadLocal.get();
    }
    public OperateAudit calcCode(ProceedingJoinPoint point) {
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        Method          method          = methodSignature.getMethod();
        Class           methodClass     = point.getTarget().getClass();
        OperateAudit methodAnno = AnnotationUtils.findAnnotation(method, OperateAudit.class);

        if (Objects.nonNull(methodAnno)) {
            return methodAnno;
        } else {
            return AnnotationUtils.findAnnotation(methodClass, OperateAudit.class);
        }
    }

    private   void clearThreadLocal(){
        msgThreadLocal.remove();
        msgContextThreadLocal.remove();
    }


}



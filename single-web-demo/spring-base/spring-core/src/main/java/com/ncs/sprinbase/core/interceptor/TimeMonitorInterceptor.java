package com.ncs.sprinbase.core.interceptor;

import java.lang.annotation.Annotation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * 执行时间监控拦截器
 */
public class TimeMonitorInterceptor extends HandlerInterceptorAdapter {
    private static Logger     LOG       = LoggerFactory.getLogger(TimeMonitorInterceptor.class);
    private ThreadLocal<Long> timeLocal = new ThreadLocal<Long>();

    /**
     *执行时间告警阈值
     * */
    private Long warningLimit = 3000L;

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        Long start = timeLocal.get();

        if (null != start) {
            long   end        = System.currentTimeMillis();
            String requestUrl = request.getRequestURI();
            long   time       = end - start;

            if (time > warningLimit) {
                LOG.warn("[{}] 执行时间超长,执行时间为:{}", requestUrl, time);
            }

            LOG.debug("[{}] 执行时间为:{}", requestUrl, time);
            timeLocal.remove();
        }
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if ((handler != null) && (handler instanceof HandlerMethod)) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            TimeMonitor   monitor       = getAnno(handlerMethod, TimeMonitor.class);

            if (monitor.need()) {
                timeLocal.set(System.currentTimeMillis());
            }
        }

        return true;
    }

    private <T> T getAnno(HandlerMethod handlerMethod, Class authAnnoType) {
        Annotation methodAnno = AnnotationUtils.findAnnotation(handlerMethod.getMethod(), authAnnoType);

        if (methodAnno == null) {
            return (T) AnnotationUtils.findAnnotation(handlerMethod.getBeanType(), authAnnoType);
        } else {
            return (T) methodAnno;
        }
    }

    public Long getWarningLimit() {
        return warningLimit;
    }

    public void setWarningLimit(Long warningLimit) {
        this.warningLimit = warningLimit;
    }
}



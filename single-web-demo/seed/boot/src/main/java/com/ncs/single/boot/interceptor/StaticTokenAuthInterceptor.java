package com.ncs.single.boot.interceptor;


import com.ncs.commons.utils.PropertiesUtil;
import com.ncs.single.boot.anno.RequireToken;
import com.ncs.single.boot.commons.TokenConstants;
import com.ncs.single.boot.exception.UnAuthException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;

/**
 * @desc 静态token拦截器
 **/
@Component
public class StaticTokenAuthInterceptor extends HandlerInterceptorAdapter {
    private static Logger LOG = LoggerFactory.getLogger(StaticTokenAuthInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        if (handler != null && handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            RequireToken requireToken = getAuthAnno(handlerMethod, RequireToken.class);
            if (requireToken != null && requireToken.require()) {
                if (!PropertiesUtil.getSystemProperty(TokenConstants.STATIC_TOKEN_KEY).equals(getToken(request))) {
                    LOG.debug("请求uri {},权限不足,已拦截", uri);
                    throw new UnAuthException("操作非法,token 参数不存在");
                }
            }
        }
        return true;
    }

    private <T> T getAuthAnno(HandlerMethod handlerMethod, Class authAnnoType) {
        /**
         * 优先获取方法上,其次获取类上的注解
         */
        Annotation methodAnno = AnnotationUtils.findAnnotation(handlerMethod.getMethod(), authAnnoType);
        if (methodAnno == null) {
            return (T) AnnotationUtils.findAnnotation(handlerMethod.getBeanType(), authAnnoType);
        } else {
            return (T) methodAnno;
        }
    }

    private String getToken(HttpServletRequest request) {
        String token = request.getHeader(TokenConstants.REQUEST_TOKEN_KEY);
        if (StringUtils.hasText(token)) {
            return token;
        }
        return request.getParameter(TokenConstants.REQUEST_TOKEN_KEY);

    }

}

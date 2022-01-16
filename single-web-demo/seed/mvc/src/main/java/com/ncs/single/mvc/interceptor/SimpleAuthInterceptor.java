package com.ncs.single.mvc.interceptor;


import com.ncs.commons.utils.ResponseUtil;
import com.ncs.single.mvc.enums.RoleTypeEnum;
import com.ncs.single.mvc.exception.UnAuthException;
import com.ncs.single.mvc.security.RequireRole;
import com.ncs.single.mvc.utils.SessionUserUtil;
import com.ncs.single.mvc.vo.LoginUserVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;

public class SimpleAuthInterceptor extends HandlerInterceptorAdapter {
    private static Logger LOG = LoggerFactory.getLogger(SimpleAuthInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        LOG.debug("请求 uri -> {}", uri);
        LoginUserVo loinUser = SessionUserUtil.getLoinUser(request);
        if (loinUser == null) {
            if (handler != null && handler instanceof HandlerMethod) {
                HandlerMethod handlerMethod = (HandlerMethod) handler;
                RequireRole requireRole = getAuthAnno(handlerMethod, RequireRole.class);
                if (RoleTypeEnum.hasRole(requireRole, null)) {
                    return true;
                }
            }
            if (ResponseUtil.isAjaxRequest(request)) {
                //超时,重新登录
                response.setStatus(HttpStatus.REQUEST_TIMEOUT.value());
                return false;
            } else {
                //转发到登录
                response.sendRedirect(request.getServletContext().getContextPath());
            }

            return false;
        } else {
            if (handler != null && handler instanceof HandlerMethod) {
                HandlerMethod handlerMethod = (HandlerMethod) handler;
                RoleTypeEnum role = loinUser.getRole();
                RequireRole requireRole = getAuthAnno(handlerMethod, RequireRole.class);
                if (RoleTypeEnum.hasRole(requireRole, role)) {
                    return true;
                }
                LOG.debug("请求uri {},权限不足,已拦截", uri);
                throw new UnAuthException("操作非法");
            }
            return true;
        }
    }




    private <T> T getAuthAnno(HandlerMethod handlerMethod, Class authAnnoType) {
        Annotation methodAnno = AnnotationUtils.findAnnotation(handlerMethod.getMethod(), authAnnoType);
        if (methodAnno == null) {
            return (T) AnnotationUtils.findAnnotation(handlerMethod.getBeanType(), authAnnoType);
        } else {
            return (T) methodAnno;
        }
    }

}

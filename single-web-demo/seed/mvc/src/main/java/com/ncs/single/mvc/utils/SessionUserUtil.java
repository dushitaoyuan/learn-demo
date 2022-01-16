package com.ncs.single.mvc.utils;

import com.ncs.single.mvc.commons.SessionConstants;
import com.ncs.single.mvc.vo.LoginUserVo;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.Optional;

/**
 * @author dushitaoyuan
 * @date 2019/10/11 18:18
 * @desc 会话用户相关工具
 **/
public class SessionUserUtil {
    public static LoginUserVo getLoinUser(HttpServletRequest request) {
        Object attribute = request.getSession().getAttribute(SessionConstants.USER);
        if (Objects.isNull(attribute)) {
            return null;
        }
        return (LoginUserVo) attribute;
    }

    public static LoginUserVo getLoinUser() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return getLoinUser(request);
    }

    public static HttpServletRequest getCurrentRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    public static void setUser(LoginUserVo loginUserVo, HttpServletRequest request) {
        request.getSession().setAttribute(SessionConstants.USER, loginUserVo);
    }
    public static void setAttribute(String key ,Object value, HttpServletRequest request) {
        request.getSession().setAttribute(key, value);
    }
    public static <T> T getAttribute(String key,HttpServletRequest request) {
        Optional<Object> value = Optional.ofNullable(request.getSession().getAttribute(key));
        if(value.isPresent()){
            return  (T) value.get();
        }
        return null;
    }

}

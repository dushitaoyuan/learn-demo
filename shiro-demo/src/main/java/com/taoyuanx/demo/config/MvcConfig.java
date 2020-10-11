package com.taoyuanx.demo.config;


import com.taoyuanx.demo.api.Result;
import com.taoyuanx.demo.api.ResultBuilder;
import com.taoyuanx.demo.api.ResultCode;
import com.taoyuanx.demo.exception.ServiceException;
import com.taoyuanx.demo.thymeleaf.CustomExtendDialect;
import com.taoyuanx.demo.utils.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.context.annotation.Bean;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.security.auth.message.AuthException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;

/**
 * @author dushitaoyuan
 * @desc mvc 配置
 * @date 2020/9/8
 */
@ControllerAdvice
@Slf4j
public class MvcConfig implements WebMvcConfigurer, ResponseBodyAdvice<Object> {


    // 统一结果返回
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        Class<?> containingClass = returnType.getContainingClass();
        if (containingClass.getClass().getName().startsWith("com.taoyuanx")&&AnnotationUtils.findAnnotation(containingClass, RestController.class) != null || AnnotationUtils.findAnnotation(returnType.getMethod(), ResponseBody.class) != null) {
            return true;
        }
        return false;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends
            HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (body instanceof Result) {
            return body;
        }
        if (body instanceof ResponseEntity) {
            return body;
        }
        if (body instanceof String) {
            return body;
        }
        return ResultBuilder.success(body);
    }
    // 统一异常处理

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result handle(Throwable e, HttpServletResponse response) {
        Integer httpStatus = 500;
        String msg = e.getMessage();
        Integer errorCode = 500;
        ResponseStatus responseStatus = AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class);
        if (responseStatus != null) {
            httpStatus = responseStatus.code().value();
        }
        if (e instanceof AuthException || e instanceof UnauthorizedException || e instanceof UnauthenticatedException) {
            if (httpStatus != null) {
                httpStatus = 401;
            }
            errorCode = ResultCode.UNAUTHORIZED.code;
        } else if (e instanceof ServiceException) {
            httpStatus = 200;
            errorCode = ((ServiceException) e).getErrorCode();
        } else if (e instanceof MethodArgumentNotValidException) {
            httpStatus = ResultCode.PARAM_ERROR.code;
            errorCode = httpStatus;
            msg = handleMethodArgumentNotValidException(((MethodArgumentNotValidException) e).getBindingResult());
        } else if (e instanceof ConstraintViolationException) {
            httpStatus = ResultCode.PARAM_ERROR.code;
            errorCode = httpStatus;
            msg = handleConstraintViolationException((ConstraintViolationException) e);
        } else if (e instanceof BindException) {
            httpStatus = ResultCode.PARAM_ERROR.code;
            errorCode = httpStatus;
            msg = handleMethodArgumentNotValidException(((BindException) e).getBindingResult());
        } else {
            log.warn("系统异常", e);
            msg = "系统异常";
        }
        response.setStatus(httpStatus);
        return ResultBuilder.failed(errorCode, msg);
    }


    /**
     * 参数异常处理
     */
    private String handleConstraintViolationException(ConstraintViolationException e) {
        String msg = e.getMessage();
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        if (!CollectionUtils.isEmpty(constraintViolations)) {
            StringBuilder msgBuf = new StringBuilder();
            for (ConstraintViolation constraintViolation : constraintViolations) {
                msgBuf.append(constraintViolation.getMessage()).append(",");
            }
            if (msgBuf.charAt(msgBuf.length() - 1) == ',') {
                msgBuf.deleteCharAt(msgBuf.length() - 1);
            }
            msg = msgBuf.toString();
        }
        return msg;
    }

    private String handleMethodArgumentNotValidException(BindingResult bindingResult) {
        String msg = "";
        List<ObjectError> objectErrors = bindingResult.getAllErrors();
        if (!CollectionUtils.isEmpty(objectErrors)) {
            StringBuilder msgBuf = new StringBuilder();
            for (ObjectError objectError : objectErrors) {
                msgBuf.append(objectError.getDefaultMessage()).append(",");
            }
            if (msgBuf.charAt(msgBuf.length() - 1) == ',') {
                msgBuf.deleteCharAt(msgBuf.length() - 1);
            }
            msg = msgBuf.toString();
        }
        return msg;
    }

    /**
     * 跨域配置
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")
                .allowCredentials(true)
                .maxAge(3600)
                .allowedHeaders("*");
    }
    @Bean
    public SpringContextUtil springContextUtil() {
        return new SpringContextUtil();
    }
    @Bean
    public CustomExtendDialect customExtendDialect() {
        return new CustomExtendDialect();
    }


}

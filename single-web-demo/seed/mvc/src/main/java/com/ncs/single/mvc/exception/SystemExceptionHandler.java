package com.ncs.single.mvc.exception;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.google.common.base.Joiner;
import com.ncs.commons.api.Result;
import com.ncs.commons.api.ResultBuilder;
import com.ncs.commons.api.ResultCode;
import com.ncs.commons.utils.ResponseUtil;
import com.ncs.single.mvc.utils.ExceptionHandUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 全局异常处理
 */
@Order(value=Ordered.HIGHEST_PRECEDENCE)
public class SystemExceptionHandler implements HandlerExceptionResolver {
    public static final Logger LOG = LoggerFactory.getLogger(SystemExceptionHandler.class);

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
                                         Exception e) {
        ModelAndView modelAndView = new ModelAndView();
        InnerError innerError = ExceptionHandUtil.handleException(request, response, e);
        if (ExceptionHandUtil.isJson(request, handler)) {
            Result errorResult = ResultBuilder.failed(innerError.getErrorCode(), innerError.getErrorMsg());
            //处理json请求
            ResponseUtil.responseJson(response, JSON.toJSONString(errorResult), innerError.getHttpCode());
            return modelAndView;
        } else {
            // 处理页面转发请求
            modelAndView.setStatus(HttpStatus.valueOf(innerError.getHttpCode()));
            ResultCode errorCode = ResultCode.resultCode(innerError.getErrorCode());
            switch (errorCode) {
                case INTERNAL_SERVER_ERROR:
                    ExceptionHandUtil.sendRedirect(request, response, "/static/htmls/errors/error.html");
                    break;
                case NOT_FOUND:
                    ExceptionHandUtil.sendRedirect(request, response, "/static/htmls/errors/404.html");
                    break;
                case UNAUTHORIZED:
                    ExceptionHandUtil.sendRedirect(request, response, "/static/htmls/errors/no-auth.html");
                    break;
            }
            LOG.error("请求地址[{}] 出现异常，方法：{}.{}，异常摘要:{}", request.getRequestURI(), e);
            return modelAndView;

        }
    }
}






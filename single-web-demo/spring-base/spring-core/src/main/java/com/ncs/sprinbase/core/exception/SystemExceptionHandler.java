package com.ncs.sprinbase.core.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;

import com.ncs.sprinbase.core.commons.Result;
import com.ncs.sprinbase.core.commons.ResultBuilder;
import com.ncs.sprinbase.core.commons.ResultCode;
import com.ncs.sprinbase.core.utils.ExceptionHandUtil;
import com.ncs.sprinbase.core.utils.ResponseUtil;

/**
 * 全局异常处理
 */
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class SystemExceptionHandler implements HandlerExceptionResolver {
    public static final Logger LOG = LoggerFactory.getLogger(SystemExceptionHandler.class);

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
                                         Exception e) {
        ModelAndView modelAndView = new ModelAndView();
        InnerError   innerError   = ExceptionHandUtil.handleException(request, response, e);

        if (ExceptionHandUtil.isJson(request, handler)) {
            Result errorResult = ResultBuilder.failed(innerError.getErrorCode(), innerError.getErrorMsg());

            // 处理json请求
            ResponseUtil.responseJson(response, JSON.toJSONString(errorResult), innerError.getHttpCode());

            return modelAndView;
        } else {

            // 处理页面转发请求
            modelAndView.setStatus(HttpStatus.valueOf(innerError.getHttpCode()));

            ResultCode errorCode = ResultCode.resultCode(innerError.getErrorCode());

            switch (errorCode) {
            case INTERNAL_SERVER_ERROR :
                ExceptionHandUtil.sendRedirect(request, response, "/static/htmls/errors/error.html");

                break;

            case NOT_FOUND :
                ExceptionHandUtil.sendRedirect(request, response, "/static/htmls/errors/404.html");

                break;

            case UNAUTHORIZED :
                ExceptionHandUtil.sendRedirect(request, response, "/static/htmls/errors/no-auth.html");

                break;
            }

            LOG.error("请求地址[{}] 出现异常，方法：{}.{}，异常摘要:{}", request.getRequestURI(), e);

            return modelAndView;
        }
    }
}


